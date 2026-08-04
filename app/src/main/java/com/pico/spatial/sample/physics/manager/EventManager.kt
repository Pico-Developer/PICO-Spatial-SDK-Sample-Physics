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

import com.pico.spatial.core.container.SpatialViewContent
import com.pico.spatial.core.ecs.event.CollisionEvents
import com.pico.spatial.core.ecs.event.Event
import com.pico.spatial.core.lifecycle.Cancellable

class EventManager {
    private var subscription: Cancellable? = null
    var collisionEnterCallback: ((CollisionEvents.Enter) -> Unit)? = null

    fun <T : Event> subscribeCollisionEvent(content: SpatialViewContent, event: Class<T>) {
        when (event) {
            CollisionEvents.Enter::class.java -> {
                subscription =
                    content.subscribe(
                        eventType = event,
                        subscriber = { collision -> collisionEnterCallback?.invoke(collision) }
                    )
            }
        }
    }

    fun unsubscribeAllCollisionEvents() {
        subscription?.cancel()
    }
}
