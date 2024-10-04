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

import com.mcstarrysky.aiyatsbus.core.util.dura
import taboolib.library.reflex.Reflex.Companion.invokeMethod
import taboolib.library.xseries.XMaterial

/**
 * Vulpecula
 * top.lanscarlos.vulpecula.bacikal.action.item
 *
 * @author Lanscarlos
 * @since 2023-03-24 21:21
 */
object ActionItemModify : ActionItem.Resolver {

    override val name: Array<String> = arrayOf("modify", "set")

    /**
     * item modify &item -xxx xxx
     * */
    override fun resolve(reader: ActionItem.Reader): ActionItem.Handler<out Any?> {
        return reader.transfer {
            combine(
                source(),
                argument("material", "mat", "mat", then = text(display = "material")),
                argument("amount", "amt", then = int(display = "amount")),
                argument("durability", "dura", then = int(display = "durability")),
                argument("name", "n", then = textOrNull(), def = "@NULL"),
                argument("lore", "l", then = multilineOrNull(), def = listOf("@NULL")),
                argument("model", "m", then = int(display = "model")),
            ) { item, type, amount, durability, name, lore, model ->
                val meta = item.itemMeta

                if (type != null) {
                    item.type = type.asMaterial()?.parseMaterial() ?: item.type
                }
                if (amount != null) {
                    item.amount = amount
                }
                if (durability != null) {
                    item.dura = durability
                }
                if (name != "@NULL") {
                    meta?.setDisplayName(name)
                }
                if (lore?.firstOrNull() != "@NULL") {
                    meta?.lore = lore
                }
                if (model != null) {
                    try {
                        meta?.invokeMethod<Void>("setCustomModelData", model)
                    } catch (ignored: NoSuchMethodException) {
                    }
                }

                item.also { it.itemMeta = meta }
            }
        }
    }

    private fun String.asMaterial(): XMaterial? {
        return XMaterial.matchXMaterial(this.uppercase()).let {
            if (it.isPresent) it.get() else null
        }
    }
}