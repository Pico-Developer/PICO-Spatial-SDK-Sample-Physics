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
package com.pico.spatial.sample.physics.ecs

import com.pico.spatial.core.ecs.Component

/**
 * Represents a Domino in the physics simulation.
 *
 * This file contains the [DominoComponent] data and can be extended to include feature-specific
 * extensions or helper logic.
 */
class DominoComponent(val index: Int = -1) : Component() {
    // You need to override the clone method to make the custom component clonable
    override fun clone(): DominoComponent {
        return DominoComponent(index)
    }
}
