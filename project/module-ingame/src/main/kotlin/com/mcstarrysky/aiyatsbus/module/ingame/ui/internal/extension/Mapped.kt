/*
 * This file is part of ParrotX, licensed under the MIT License.
 *
 *  Copyright (c) 2020 Legoshi
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
package com.mcstarrysky.aiyatsbus.module.ingame.ui.internal.extension

import org.bukkit.inventory.Inventory
import com.mcstarrysky.aiyatsbus.module.ingame.ui.internal.config.MenuConfiguration
import taboolib.module.ui.Menu
import taboolib.module.ui.buildMenu
import taboolib.module.ui.type.PageableChest

@Suppress("unused")
class Mapped<E>(override var title: String) : Menu {

    private lateinit var config: MenuConfiguration
    private lateinit var template: String
    private lateinit var elements: () -> Iterable<E>
    private var args: MutableMap<String, Any?>.() -> Unit = {}
    private var prev: String = "Previous"
    private var next: String = "Next"

    fun configuration(config: MenuConfiguration) {
        this.config = config
    }

    fun template(keyword: String) {
        this.template = keyword
    }

    fun elements(elements: () -> Iterable<E>) {
        this.elements = elements
    }

    fun argument(builder: MutableMap<String, Any?>.() -> Unit) {
        this.args = builder
    }

    fun setPrevPage(keyword: String) {
        this.prev = keyword
    }

    fun setNextPage(keyword: String) {
        this.next = keyword
    }

    override fun build(): Inventory {
        return buildMenu<PageableChest<E>>(title) {
            val (shape, templates) = config
            val slots = shape[template].toList()
            rows(shape.rows)
            map(*shape.array)
            slots(slots)
            elements { elements().toList() }

            val _template = templates.require(template)
            onGenerate { _, element, index, slot ->
                _template(slot, index) {
                    this["element"] = element
                    args()
                }
            }
            onClick { event, element ->
                _template.handle(this@Mapped, event) {
                    this["element"] = element
                    args()
                }
            }

            config.setPreviousPage(this)
            config.setNextPage(this)

            onBuild(false) { _, inventory ->
                shape.all(template, prev, next) { slot, index, item, _ ->
                    inventory.setItem(slot, item(slot, index, args))
                }
            }
            onClick { event ->
                event.isCancelled = true
                if (event.rawSlot in shape && event.rawSlot !in slots) {
                    templates[event.rawSlot]?.handle(this, event, args)
                }
            }
        }
    }

}