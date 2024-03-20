package com.mcstarrysky.aiyatsbus.module.kether.action.item

import org.bukkit.enchantments.Enchantment

/**
 * Vulpecula
 * top.lanscarlos.vulpecula.bacikal.action.item
 *
 * @author Lanscarlos
 * @since 2023-03-24 16:45
 */
object ActionItemEnchantment : ActionItem.Resolver {

    override val name: Array<String> = arrayOf("enchantments", "enchantment", "enchants", "enchant")

    /**
     * item enchant &item add &type with/to &level
     * item enchant &item sub &type with/to &level
     * item enchant &item set &type with/to &level
     * item enchant &item remove &type
     * item enchant &item clear
     * item enchant &item has &type
     * item enchant &item level &type
     * */
    override fun resolve(reader: ActionItem.Reader): ActionItem.Handler<out Any?> {
        val source = reader.source().accept(reader)

        reader.mark()
        return when (reader.nextToken()) {
            "add", "plus" -> {
                reader.transfer {
                    combine(
                        source,
                        text(display = "enchantment type"),
                        trim("with", "to", then = int(display = "level"))
                    ) { item, type, level ->
                        val meta = item.itemMeta ?: return@combine item
                        val enchantment = type.asEnchantment() ?: return@combine item

                        val newLevel = if (meta.hasEnchant(enchantment)) {
                            meta.getEnchantLevel(enchantment) + level
                        } else {
                            level
                        }

                        meta.addEnchant(enchantment, newLevel, true)

                        item.also { it.itemMeta = meta }
                    }
                }
            }
            "sub", "minus" -> {
                reader.transfer {
                    combine(
                        source,
                        text(display = "enchantment type"),
                        trim("with", "to", then = int(display = "level"))
                    ) { item, type, level ->
                        val meta = item.itemMeta ?: return@combine item
                        val enchantment = type.asEnchantment() ?: return@combine item
                        val newLevel = if (meta.hasEnchant(enchantment)) {
                            meta.getEnchantLevel(enchantment) - level
                        } else {
                            level
                        }
                        meta.addEnchant(enchantment, newLevel, true)
                        item.also { it.itemMeta = meta }
                    }
                }
            }
            "modify", "set" -> {
                reader.transfer {
                    combine(
                        source,
                        text(display = "enchantment type"),
                        trim("with", "to", then = int(display = "level"))
                    ) { item, type, level ->
                        val meta = item.itemMeta ?: return@combine item
                        val enchantment = type.asEnchantment() ?: return@combine item
                        meta.addEnchant(enchantment, level, true)
                        item.also { it.itemMeta = meta }
                    }
                }
            }
            "remove", "rm" -> {
                reader.transfer {
                    combine(
                        source,
                        text(display = "enchantment type")
                    ) { item, type ->
                        val meta = item.itemMeta ?: return@combine item
                        val enchantment = type.asEnchantment() ?: return@combine item
                        meta.removeEnchant(enchantment)
                        item.also { it.itemMeta = meta }
                    }
                }
            }
            "clear" -> {
                reader.transfer {
                    combine(
                        source
                    ) { item ->
                        val meta = item.itemMeta ?: return@combine item
                        for ((it, _) in meta.enchants) {
                            meta.removeEnchant(it)
                        }
                        item.also { it.itemMeta = meta }
                    }
                }
            }
            "has", "contains" -> {
                reader.handle {
                    combine(
                        source,
                        text(display = "enchantment type")
                    ) { item, type ->
                        val enchantment = type.asEnchantment() ?: return@combine false
                        item.itemMeta?.hasEnchant(enchantment) ?: false
                    }
                }
            }
            "level", "lvl" -> {
                reader.handle {
                    combine(
                        source,
                        text(display = "enchantment type")
                    ) { item, type ->
                        val enchantment = type.asEnchantment() ?: return@combine 0
                        item.itemMeta?.getEnchantLevel(enchantment) ?: 0
                    }
                }
            }
            else -> {
                reader.reset()
                reader.handle {
                    combine(source) { item -> item.itemMeta?.enchants }
                }
            }
        }
    }

    @Suppress("deprecation")
    private fun String.asEnchantment(): Enchantment? {
        return Enchantment.values().firstOrNull { it.name.equals(this, true) }
    }
}