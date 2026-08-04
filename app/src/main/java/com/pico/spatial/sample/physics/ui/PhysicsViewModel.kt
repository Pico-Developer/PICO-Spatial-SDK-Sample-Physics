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
package com.pico.spatial.sample.physics.ui

import androidx.annotation.StringRes
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pico.spatial.core.container.SpatialViewContent
import com.pico.spatial.core.ecs.Entity
import com.pico.spatial.core.ecs.event.Event
import com.pico.spatial.sample.physics.R
import com.pico.spatial.sample.physics.manager.EventManager
import com.pico.spatial.sample.physics.manager.PhysicsManager
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class PhysicsViewModel : ViewModel() {
    val playPhase = mutableStateOf(PlayPhase.START)
    val phaseHolding = mutableStateOf(false)

    // Manage Manager instances and establish hierarchical relationships
    private val physicsManager = PhysicsManager()
    private val eventManager = EventManager()

    init {
        // Set up collision event callback
        eventManager.collisionEnterCallback = physicsManager.collisionEnterCallback
    }

    fun changeToNextState() =
        viewModelScope.launch {
            playPhase.value =
                when (playPhase.value) {
                    PlayPhase.START -> {
                        physicsManager.setupCollision()
                        PlayPhase.TRIGGER
                    }
                    PlayPhase.TRIGGER -> {
                        phaseHolding.value = true
                        physicsManager.toggleRigidBody(true)
                        physicsManager.triggerFallDown()
                        delay(SIMULATION_HOLD_TIME)
                        physicsManager.toggleRigidBody(false)
                        phaseHolding.value = false
                        PlayPhase.RESTART
                    }
                    PlayPhase.RESTART -> {
                        physicsManager.reset()
                        PlayPhase.TRIGGER
                    }
                }
        }

    // Expose key methods from PhysicsManager
    fun loadScene(rootEntity: Entity) {
        physicsManager.loadScene(rootEntity)
    }

    fun subscribeCollisionEvent(content: SpatialViewContent, event: Class<out Event>) {
        eventManager.subscribeCollisionEvent(content, event)
    }

    fun unsubscribeAllCollisionEvents() {
        eventManager.unsubscribeAllCollisionEvents()
    }

    companion object {
        const val SIMULATION_HOLD_TIME = 3000L // in milliseconds
    }
}

enum class PlayPhase(@StringRes val value: Int) {
    START(R.string.button_start),
    TRIGGER(R.string.button_push_domino),
    RESTART(R.string.button_restart),
}
