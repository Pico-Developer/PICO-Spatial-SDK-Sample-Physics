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

import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.pico.spatial.core.container.SpatialViewContent
import com.pico.spatial.core.ecs.Entity
import com.pico.spatial.core.ecs.TransformComponent
import com.pico.spatial.core.ecs.event.CollisionEvents
import com.pico.spatial.core.math.Vector3
import com.pico.spatial.ui.design.Button
import com.pico.spatial.ui.design.ButtonDefaults
import com.pico.spatial.ui.design.PicoTheme.colorScheme
import com.pico.spatial.ui.design.Text
import com.pico.spatial.ui.foundation.content.SpatialView
import com.pico.spatial.ui.foundation.material.backgroundMaterial
import com.pico.spatial.ui.platform.Material

@Composable
fun MainScreen(viewModel: PhysicsViewModel = viewModel()) {
    val playPhase by remember { viewModel.playPhase }
    val controlButtonDisabled by remember { viewModel.phaseHolding }
    val rootEntity = remember { Entity() }
    DisposableEffect(Unit) { onDispose { viewModel.unsubscribeAllCollisionEvents() } }

    SpatialView(
        modifier = Modifier.size(2000.dp, 1440.dp),
        initial = { content, attachments ->
            content.addEntity(rootEntity)
            viewModel.loadScene(rootEntity)
            attachments.entity(ATTACHMENT_ID_CONTROL_BUTTON)?.let {
                it.components.get<TransformComponent>()?.setPosition(BUTTON_POSITION)
                content.addEntity(it)
            }
            content.subscribeCollisionEvents(viewModel)
        },
        attachments = {
            AttachmentPanel(id = ATTACHMENT_ID_CONTROL_BUTTON) {
                ControlButton(
                    semanticsText = "${playPhase.name} button",
                    text = stringResource(playPhase.value),
                    notTrigger = playPhase != PlayPhase.TRIGGER,
                    enabled = controlButtonDisabled.not(),
                ) {
                    viewModel.changeToNextState()
                }
            }
        }
    )
}

private fun SpatialViewContent.subscribeCollisionEvents(viewModel: PhysicsViewModel) {
    viewModel.subscribeCollisionEvent(this, CollisionEvents.Enter::class.java)
}

@Composable
fun ControlButton(
    semanticsText: String,
    text: String,
    notTrigger: Boolean = true,
    enabled: Boolean = true,
    onClick: () -> Unit,
) {
    Button(
        modifier =
            Modifier.semantics { contentDescription = semanticsText }
                .size(BUTTON_WIDTH, BUTTON_HEIGHT)
                .backgroundMaterial(true, Material.Thin),
        colors =
            if (notTrigger) {
                ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent,
                    contentColor = colorScheme.fillPrimary
                )
            } else {
                ButtonDefaults.buttonColors(containerColor = Color.Black)
            },
        onClick = onClick,
        enabled = enabled,
    ) {
        Text(text = text, fontSize = FONT_SIZE)
    }
}

private val BUTTON_WIDTH = 174.dp
private val BUTTON_HEIGHT = 56.dp
private val FONT_SIZE = 16.sp
private val BUTTON_POSITION = Vector3(0f, -0.3f, 0.28f)
private const val ATTACHMENT_ID_CONTROL_BUTTON = "attachment_id_control_button"
