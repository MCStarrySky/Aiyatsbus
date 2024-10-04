/*
 * This file is part of Vulpecula, licensed under the MIT License.
 *
 *  Copyright (c) 2018 Bkm016
 *
 *  Permission is hereby granted, free of charge, to any person obtaining a copy
 *  of this software and associated documentation files (the "Software"), to deal
 *  in the Software without restriction, including without limitation the rights
 *  to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *  copies of the Software, and to permit persons to whom the Software is
 *  furnished to do so, subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in all
 *  copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 *  SOFTWARE.
 */
package com.mcstarrysky.aiyatsbus.module.kether.property.bukkit

import com.mcstarrysky.aiyatsbus.core.util.coerceDouble
import com.mcstarrysky.aiyatsbus.core.util.coerceFloat
import com.mcstarrysky.aiyatsbus.core.util.serialized
import com.mcstarrysky.aiyatsbus.module.kether.AiyatsbusGenericProperty
import com.mcstarrysky.aiyatsbus.module.kether.AiyatsbusProperty
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.World
import org.bukkit.util.Vector
import taboolib.common.OpenResult
import taboolib.platform.util.toProxyLocation

/**
 * Vulpecula
 * top.lanscarlos.vulpecula.bacikal.property
 *
 * @author Lanscarlos
 * @since 2023-03-22 14:03
 */
@AiyatsbusProperty(
    id = "location-bukkit",
    bind = Location::class
)
class PropertyLocation : AiyatsbusGenericProperty<Location>("location-bukkit") {

    override fun readProperty(instance: Location, key: String): OpenResult {
        val property: Any? = when (key) {
            "clone" -> instance.clone()
            "taboo" -> instance.toProxyLocation()
            "block" -> instance.block
            "blockX", "block-x" -> instance.blockX
            "blockY", "block-y" -> instance.blockY
            "blockZ", "block-z" -> instance.blockZ
            "chunk" -> instance.chunk
            "direction" -> instance.direction
            "world" -> instance.world

            "x" -> instance.x
            "y" -> instance.y
            "z" -> instance.z

            "yaw" -> instance.yaw
            "pitch" -> instance.pitch

            "isWorldLoaded", "world-loaded", "loaded" -> instance.isWorldLoaded
            "length" -> instance.length()
            "lengthSquared", "length-squared", "squared" -> instance.lengthSquared()
            "serialize" -> instance.serialize()
            "serialized" -> instance.serialized
            "toVector", "vector" -> instance.toVector() // io.papermc.paper.math.Position#toVector
            "toString", "string" -> instance.toString()
            "zero" -> instance.zero()
            else -> return OpenResult.failed()
        }
        return OpenResult.successful(property)
    }

    override fun writeProperty(instance: Location, key: String, value: Any?): OpenResult {
        when (key) {
            "direction" -> instance.direction = value as? Vector ?: return OpenResult.successful()
            "world" -> {
                val world = when (value) {
                    is World -> value
                    is String -> Bukkit.getWorld(value)
                    is Location -> value.world
                    else -> null
                }
                instance.world = world ?: return OpenResult.successful()
            }
            "x" -> instance.x = value?.coerceDouble() ?: return OpenResult.successful()
            "y" -> instance.y = value?.coerceDouble() ?: return OpenResult.successful()
            "z" -> instance.z = value?.coerceDouble() ?: return OpenResult.successful()
            "pitch" -> instance.pitch = value?.coerceFloat(0f) ?: return OpenResult.successful()
            "yaw" -> instance.yaw = value?.coerceFloat() ?: return OpenResult.successful()
            else -> return OpenResult.failed()
        }
        return OpenResult.successful()
    }
}