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
package com.mcstarrysky.aiyatsbus.module.kether.property.bukkit.block

import com.mcstarrysky.aiyatsbus.module.kether.AiyatsbusGenericProperty
import com.mcstarrysky.aiyatsbus.module.kether.AiyatsbusProperty
import org.bukkit.Material
import org.bukkit.block.Biome
import org.bukkit.block.Block
import org.bukkit.block.data.BlockData
import taboolib.common.OpenResult

/**
 * Vulpecula
 * top.lanscarlos.vulpecula.bacikal.property
 *
 * @author Lanscarlos
 * @since 2023-03-22 10:07
 */
@AiyatsbusProperty(
    id = "block",
    bind = Block::class
)
class PropertyBlock : AiyatsbusGenericProperty<Block>("block") {

    override fun readProperty(instance: Block, key: String): OpenResult {
        val property: Any? = when (key) {
            "type", "material", "mat" -> instance.type.name
            "location", "loc" -> instance.location
            "locationX", "location-x", "loc-x", "x" -> instance.x
            "locationY", "location-y", "loc-y", "y" -> instance.y
            "locationZ", "location-z", "loc-z", "z" -> instance.z

            "world*" -> instance.world
            "worldName", "world" -> instance.world.name
            "isEmpty", "is-empty" -> instance.isEmpty
            "isLiquid","is-liquid" -> instance.isLiquid
            "isPassable", "is-passable" -> instance.isPassable

            "biome" -> instance.biome.name
            "drops" -> instance.drops

            "lightLevel", "light-level", "light" -> instance.lightLevel
            "lightFromSky", "light-from-sky", "light-sky" -> instance.lightFromSky
            "lightFromBlocks", "light-from-blocks", "light-blocks" -> instance.lightFromBlocks

            /* 材质相关属性 */
            "isSolid", "is-solid" -> instance.type.isSolid
            "isItem", "is-item" -> instance.type.isItem
            "isRecord", "is-record" -> instance.type.isRecord
            "isOccluding", "is-occluding" -> instance.type.isOccluding
            "isInteractable", "is-interactable" -> instance.type.isInteractable
            "isFuel", "is-fuel" -> instance.type.isFuel
            "isFlammable", "is-flammable" -> instance.type.isFlammable
            "isEdible", "is-edible" -> instance.type.isEdible
            "isBurnable", "is-burnable" -> instance.type.isBurnable
            "isBlock", "is-block" -> instance.type.isBlock
            "isAir", "is-air" -> instance.type.isAir
            "hasGravity", "has-gravity", "gravity" -> instance.type.hasGravity()
            "slipperiness" -> instance.type.slipperiness
            "hardness" -> instance.type.hardness
            "slot" -> instance.type.equipmentSlot.name
            "blastResistance", "blast-resistance", "resistance" -> instance.type.blastResistance
            "creativeCategory", "creative-category", "category" -> instance.type.creativeCategory
            "blockData", "block-data" -> instance.blockData
            else -> return OpenResult.failed()
        }
        return OpenResult.successful(property)
    }

    override fun writeProperty(instance: Block, key: String, value: Any?): OpenResult {
        when (key) {
            "biome" -> {
                val name = value?.toString() ?: return OpenResult.successful()
                instance.biome = Biome.values().firstOrNull {
                    it.name.equals(name, true)
                } ?: return OpenResult.successful()
            }
            "type", "material", "mat" -> {
                val name = value?.toString() ?: return OpenResult.successful()
                instance.type = Material.values().firstOrNull {
                    it.name.equals(name, true)
                } ?: return OpenResult.successful()
            }
            "blockData", "block-data" -> { instance.blockData = value as? BlockData ?: return OpenResult.successful() }
            else -> return OpenResult.failed()
        }
        return OpenResult.successful()
    }
}