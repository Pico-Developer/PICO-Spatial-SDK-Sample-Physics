/*
 * Copyright 2025 - 2026 PICO. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.pico.spatial.sample.physics.manager

import androidx.compose.ui.graphics.Color
import com.pico.spatial.core.ecs.CollisionComponent
import com.pico.spatial.core.ecs.Entity
import com.pico.spatial.core.ecs.ModelComponent
import com.pico.spatial.core.ecs.PhysicsVelocityComponent
import com.pico.spatial.core.ecs.RigidBodyComponent
import com.pico.spatial.core.ecs.TransformComponent
import com.pico.spatial.core.ecs.event.CollisionEvents
import com.pico.spatial.core.ecs.resource.MeshResource
import com.pico.spatial.core.ecs.resource.PhysicallyBasedMaterial
import com.pico.spatial.core.ecs.resource.PhysicsMaterialResource
import com.pico.spatial.core.ecs.resource.ShapeResource
import com.pico.spatial.core.ecs.simulation.CollisionFilter
import com.pico.spatial.core.ecs.simulation.CollisionInfoDetailLevel
import com.pico.spatial.core.ecs.simulation.CollisionResponseMode
import com.pico.spatial.core.ecs.simulation.MassProperties
import com.pico.spatial.core.ecs.simulation.RigidBodyMode
import com.pico.spatial.core.math.Color4
import com.pico.spatial.core.math.EulerAngles
import com.pico.spatial.core.math.Vector3
import com.pico.spatial.sample.physics.data.assetBundle
import com.pico.spatial.sample.physics.ecs.DominoComponent
import com.pico.spatial.ui.foundation.content.toColor4
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PhysicsManager {
    private val coroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)

    private var tableEntity: Entity? = null
    private val dominoEntities = mutableListOf<Entity>()

    private var tableMesh: MeshResource? = null
    private var dominoMesh: MeshResource? = null
    private var tableShape: ShapeResource? = null
    private var dominoShape: ShapeResource? = null

    private val dominoInitPositions = mutableListOf<Vector3>()
    private val dominoInitRotations = mutableListOf<EulerAngles>()

    fun loadScene(rootEntity: Entity) {
        coroutineScope.launch {
            val model =
                withContext(Dispatchers.IO) { assetBundle.await().loadModel(PHYSICS_SCENE_NAME) }
            rootEntity.addChild(model)
            rootEntity.components[TransformComponent::class.java]?.apply {
                setPosition(INITIAL_POSITION)
                setScaleVector(INITIAL_SCALE)
            }
            initialize(rootEntity)
        }
    }

    private fun initialize(rootEntity: Entity) {
        tableEntity = rootEntity.findEntity(TABLE_NAME)
        dominoEntities.clear()
        dominoInitPositions.clear()
        dominoInitRotations.clear()

        for (i in 1..DOMINO_COUNT) {
            val name = "$DOMINO_NAME_PREFIX${i.toString().padStart(2, '0')}"
            val domino = rootEntity.findEntity(name) ?: continue
            domino.components.set(DominoComponent(i))
            dominoEntities.add(domino)

            val transform = domino.components[TransformComponent::class.java] ?: continue
            dominoInitPositions.add(transform.position)
            dominoInitRotations.add(transform.eulerAngles)
        }
    }

    fun setupCollision() {
        tableEntity?.let { table ->
            if (tableMesh == null || tableShape == null) {
                tableMesh = table.components[ModelComponent::class.java]?.mesh
                tableMesh?.let { tableShape = ShapeResource.createConvexMesh(it) }
            }
            tableShape?.let { shape ->
                table.components.set(
                    CollisionComponent(
                        listOf(shape),
                        tablePhysicsMaterial,
                        collisionResponseMode = CollisionResponseMode.COLLIDER_FULL,
                        collisionFilter = CollisionFilter.COLLISION_FILTER_DEFAULT,
                        collisionInfoDetailLevel = CollisionInfoDetailLevel.BRIEF
                    )
                )
            }
        }

        for (domino in dominoEntities) {
            if (dominoMesh == null || dominoShape == null) {
                dominoMesh = domino.components[ModelComponent::class.java]?.mesh
                dominoMesh?.let { dominoShape = ShapeResource.createConvexMesh(it) }
            }
            dominoShape?.let { shape ->
                domino.components.set(
                    CollisionComponent(
                        listOf(shape),
                        dominoPhysicsMaterial,
                        collisionResponseMode = CollisionResponseMode.COLLIDER_FULL,
                        collisionFilter = CollisionFilter.COLLISION_FILTER_DEFAULT,
                        collisionInfoDetailLevel = CollisionInfoDetailLevel.BRIEF
                    )
                )
            }
        }
    }

    fun toggleRigidBody(enabled: Boolean) {
        for (domino in dominoEntities) {
            if (enabled) {
                domino.components.set(createRigidBodyComponent())
            } else {
                domino.components.remove(RigidBodyComponent::class.java)
            }
        }
    }

    /**
     * Creates a RigidBodyComponent instance Each call creates a new instance to avoid static
     * property issues
     */
    private fun createRigidBodyComponent(): RigidBodyComponent {
        return RigidBodyComponent().apply {
            massProperties =
                MassProperties(
                    mass = 0.04f,
                    inertia = Vector3(1f),
                    centerOfMass = Vector3(0f, 0f, 0.02f)
                )
            rigidBodyMode = RigidBodyMode.DYNAMIC
            isAffectedByGravity = true
        }
    }

    fun triggerFallDown() {
        dominoEntities
            .firstOrNull()
            ?.components
            ?.set(
                PhysicsVelocityComponent(
                    linearVelocity = INITIAL_VELOCITY,
                    angularVelocity = Vector3.ZERO
                )
            )
    }

    fun reset() {
        for (i in dominoEntities.indices) {
            val domino = dominoEntities[i]
            domino.components[TransformComponent::class.java]?.apply {
                setPosition(dominoInitPositions[i])
                setEulerAngles(dominoInitRotations[i])
            }
            changeMaterial(domino, Color4.WHITE)
        }
    }

    /** Collision event callback handler */
    val collisionEnterCallback: (CollisionEvents.Enter) -> Unit = { collision ->
        val entityA = collision.entityA ?: error("One entity in collision is null")
        val entityB = collision.entityB ?: error("The other entity in collision is null")

        val dominoA = entityA.components[DominoComponent::class.java]
        val dominoB = entityB.components[DominoComponent::class.java]

        if (dominoA != null && dominoB != null) {
            val index = dominoA.index
            if (index in 1..dominoColors.size) {
                changeMaterial(entityA, dominoColors[index - 1])
            }
        }
    }

    /**
     * Array of colors used to visually indicate different domino collisions. Each color is mapped
     * to a specific domino index and changes the material color when a collision is detected.
     */
    private val dominoColors =
        arrayOf(
            Color(116, 161, 205).toColor4(), // Blue
            Color(184, 225, 229).toColor4(), // Light Cyan
            Color(117, 204, 174).toColor4(), // Teal
            Color(138, 205, 119).toColor4(), // Light Green
            Color(189, 205, 120).toColor4(), // Lime Green
            Color(169, 187, 66).toColor4(), // Olive Green
            Color(187, 169, 68).toColor4(), // Yellowish Green
            Color(187, 107, 66).toColor4(), // Orange
            Color(182, 76, 74).toColor4(), // Red
            Color(187, 68, 126).toColor4(), // Magenta
            Color(187, 66, 155).toColor4(), // Purple
            Color(187, 66, 175).toColor4(), // Light Purple
            Color(136, 66, 187).toColor4(), // Dark Purple
            Color(67, 138, 186).toColor4(), // Dark Blue
        )

    fun changeMaterial(entity: Entity, color: Color4) {
        val material =
            entity.components[ModelComponent::class.java]?.materials?.firstOrNull()
                as? PhysicallyBasedMaterial
        material?.apply {
            setBaseColor(color)
            setEmissiveColor(Color4(0.1f, 0.1f, 0.1f, 0.05f))
        }
    }

    companion object {
        const val PHYSICS_SCENE_NAME = "PicoDominoes"
        private val INITIAL_POSITION = Vector3(0f, -0.24f, 0f)
        private val INITIAL_SCALE = Vector3(1f)
        private val INITIAL_VELOCITY = Vector3(0.2f, 0f, -0.4f)
        private const val TABLE_NAME = "geo_PicoTable"
        private const val DOMINO_NAME_PREFIX = "geo_PicoDominoes_0"
        private const val DOMINO_COUNT = 14

        private val tablePhysicsMaterial =
            PhysicsMaterialResource(staticFriction = 1f, dynamicFriction = 0.9f, restitution = 0f)

        private val dominoPhysicsMaterial =
            PhysicsMaterialResource(
                staticFriction = 0.6f,
                dynamicFriction = 0.6f,
                restitution = 0.1f
            )
    }
}
