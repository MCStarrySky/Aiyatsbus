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