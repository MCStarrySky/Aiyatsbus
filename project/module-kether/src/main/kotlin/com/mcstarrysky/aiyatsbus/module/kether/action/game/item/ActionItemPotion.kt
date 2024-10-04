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
package com.mcstarrysky.aiyatsbus.module.kether.action.game.item

import org.bukkit.inventory.meta.PotionMeta
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import org.bukkit.potion.PotionType
import java.awt.Color

/**
 * Vulpecula
 * top.lanscarlos.vulpecula.bacikal.action.item
 *
 * @author Lanscarlos
 * @since 2023-03-24 23:08
 */
object ActionItemPotion : ActionItem.Resolver {

    override val name: Array<String> = arrayOf("potion")

    /**
     * item potion &item add xxx
     * */
    override fun resolve(reader: ActionItem.Reader): ActionItem.Handler<out Any?> {
        val source = reader.source().accept(reader)

        reader.mark()
        return when (reader.nextToken()) {
            "add", "plus" -> {
                reader.transfer {
                    combine(
                        source,
                        text(display = "potion type"),
                        int(200, display = "duration"),
                        int(1, display = "level"),
                        argument("ambient", "amb", then = bool(false)),
                        argument("particles", "particle", "p", then = bool(true)),
                        argument("icon", "i", then = bool(true))
                    ) { item, _type, duration, level, ambient, particles, icon ->
                        val meta = item.itemMeta as? PotionMeta ?: return@combine item
                        val type = _type.asPotionEffectType() ?: return@combine item
                        val origin = meta.customEffects.firstOrNull { it.type == type }
                        if (origin != null) {
                            // 移除原有属性
                            meta.removeCustomEffect(type)
                        }
                        val potion = PotionEffect(
                            type,
                            duration + (origin?.duration ?: 0),
                            level - 1 + (origin?.amplifier ?: 0),
                            ambient ?: origin?.isAmbient ?: false,
                            particles ?: origin?.hasParticles() ?: true,
                            icon ?: origin?.hasIcon() ?: true
                        )
                        meta.addCustomEffect(potion, true)
                        item.also { it.itemMeta = meta }
                    }
                }
            }
            "sub", "minus" -> {
                reader.transfer {
                    combine(
                        source,
                        text(display = "potion type"),
                        int(200, display = "duration"),
                        int(1, display = "level"),
                        argument("ambient", "amb", then = bool(false)),
                        argument("particles", "particle", "p", then = bool(true)),
                        argument("icon", "i", then = bool(true))
                    ) { item, _type, duration, level, ambient, particles, icon ->
                        val meta = item.itemMeta as? PotionMeta ?: return@combine item
                        val type = _type.asPotionEffectType() ?: return@combine item
                        val origin = meta.customEffects.firstOrNull { it.type == type }
                        if (origin != null) {
                            // 移除原有属性
                            meta.removeCustomEffect(type)
                        }
                        val potion = PotionEffect(
                            type,
                            duration - (origin?.duration ?: 0),
                            level - 1 - (origin?.amplifier ?: 0),
                            ambient ?: origin?.isAmbient ?: false,
                            particles ?: origin?.hasParticles() ?: true,
                            icon ?: origin?.hasIcon() ?: true
                        )
                        meta.addCustomEffect(potion, true)
                        item.also { it.itemMeta = meta }
                    }
                }
            }
            "modify", "set" -> {
                reader.transfer {
                    combine(
                        source,
                        text(display = "potion type"),
                        int(200, display = "duration"),
                        int(1, display = "level"),
                        argument("ambient", "amb", then = bool(false), def = false),
                        argument("particles", "particle", "p", then = bool(true), def = true),
                        argument("icon", "i", then = bool(true), def = true)
                    ) { item, _type, duration, level, ambient, particles, icon ->
                        val meta = item.itemMeta as? PotionMeta ?: return@combine item
                        val type = _type.asPotionEffectType() ?: return@combine item
                        if (meta.hasCustomEffect(type)) {
                            // 移除原有属性
                            meta.removeCustomEffect(type)
                        }
                        val potion = PotionEffect(
                            type,
                            duration,
                            level - 1,
                            ambient,
                            particles,
                            icon
                        )
                        meta.addCustomEffect(potion, true)
                        item.also { it.itemMeta = meta }
                    }
                }
            }
            "remove", "rm" -> {
                reader.transfer {
                    combine(
                        source,
                        text(display = "potion type")
                    ) { item, type ->
                        val meta = item.itemMeta as? PotionMeta ?: return@combine item
                        meta.removeCustomEffect(type.asPotionEffectType() ?: return@combine item)
                        item.also { it.itemMeta = meta }
                    }
                }
            }
            "clear" -> {
                reader.transfer {
                    combine(
                        source
                    ) { item ->
                        val meta = item.itemMeta as? PotionMeta ?: return@combine item
                        for (potion in meta.customEffects) {
                            meta.removeCustomEffect(potion.type)
                        }
                        item.also { it.itemMeta = meta }
                    }
                }
            }
            "has", "contains" -> {
                reader.handle {
                    combine(
                        source,
                        text(display = "potion type")
                    ) { item, type ->
                        val meta = item.itemMeta as? PotionMeta ?: return@combine false
                        meta.hasCustomEffect(type.asPotionEffectType() ?: return@combine false)
                    }
                }
            }
            "color" -> {
                reader.transfer {
                    combine(
                        source,
                        trim("to", then = color(display = "potion color"))
                    ) { item, color ->
                        val meta = item.itemMeta as? PotionMeta ?: return@combine item
                        meta.color = color.toBukkit()
                        item.also { it.itemMeta = meta }
                    }
                }
            }
            else -> {
                reader.reset()
                reader.handle {
                    combine(source) { item -> (item.itemMeta as? PotionMeta)?.customEffects }
                }
            }
        }
    }

    private fun String.asPotionEffectType(): PotionEffectType? {
        return PotionEffectType.values().firstOrNull { it.name.equals(this, true) }
    }

    private fun String.asPotionType(): PotionType? {
        return PotionType.values().firstOrNull { it.name.equals(this, true) }
    }

    private fun Color.toBukkit(): org.bukkit.Color {
        return org.bukkit.Color.fromRGB(this.rgb)
    }
}