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

import taboolib.library.xseries.XMaterial
import taboolib.platform.util.buildItem

/**
 * Vulpecula
 * top.lanscarlos.vulpecula.bacikal.action.item
 *
 * @author Lanscarlos
 * @since 2023-03-23 19:26
 */
object ActionItemBuild : ActionItem.Resolver {

    override val name: Array<String> = arrayOf("build", "create")

    override fun resolve(reader: ActionItem.Reader): ActionItem.Handler<out Any?> {
        return reader.transfer {
            combine(
                text(display = "item type"),
                argument("amount", "amt", "a", then = int(1), def = 1),
                argument("durability", "dura", then = int(0), def = 0),
                argument("name", "n", then = textOrNull()),
                argument("lore", "l", then = multilineOrNull()),
                argument("shiny", then = bool(false), def = false),
                argument("colored", then = bool(true), def = true),
                argument("model", then = int(-1), def = -1),
            ) { type, amount, durability, name, lore, shiny, colored, model ->
                buildItem(type.asMaterial()) {
                    this.amount = amount
                    this.damage = durability
                    this.name = name
                    if (lore?.isNotEmpty() == true) {
                        lore.forEach { this.lore += it }
                    }
                    if (shiny) {
                        this.shiny()
                    }
                    if (colored) {
                        this.colored()
                    }
                    this.customModelData = model
                }
            }
        }
    }

    private fun String.asMaterial(): XMaterial {
        return XMaterial.matchXMaterial(this.uppercase()).let {
            if (it.isPresent) it.get() else XMaterial.STONE
        }
    }
}