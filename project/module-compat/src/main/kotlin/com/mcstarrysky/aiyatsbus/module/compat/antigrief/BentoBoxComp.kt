package com.mcstarrysky.aiyatsbus.module.compat.antigrief

import com.mcstarrysky.aiyatsbus.core.compat.AntiGrief
import com.mcstarrysky.aiyatsbus.core.compat.AntiGriefChecker
import org.bukkit.Location
import org.bukkit.entity.AbstractVillager
import org.bukkit.entity.Entity
import org.bukkit.entity.Player
import taboolib.common.LifeCycle
import taboolib.common.platform.Awake
import world.bentobox.bentobox.BentoBox
import world.bentobox.bentobox.api.user.User
import world.bentobox.bentobox.lists.Flags
import world.bentobox.bentobox.util.Util

/**
 * Aiyatsbus
 * com.mcstarrysky.aiyatsbus.module.compat.antigrief.BentoBoxComp
 *
 * @author mical
 * @since 2024/4/5 15:03
 */
class BentoBoxComp : AntiGrief {

    override fun canPlace(player: Player, location: Location): Boolean {
        return BentoBox.getInstance()
            .islands
            .getIslandAt(location)
            .map { it.isAllowed(User.getInstance(player), Flags.PLACE_BLOCKS) }
            .orElse(true)
    }

    override fun canBreak(player: Player, location: Location): Boolean {
        return BentoBox.getInstance()
            .islands
            .getIslandAt(location)
            .map { it.isAllowed(User.getInstance(player), Flags.BREAK_BLOCKS) }
            .orElse(true)
    }

    override fun canInteract(player: Player, location: Location): Boolean {
        return BentoBox.getInstance()
            .islands
            .getIslandAt(location)
            .map { it.isAllowed(User.getInstance(player), Flags.CONTAINER) }
            .orElse(true)
    }

    override fun canInteractEntity(player: Player, entity: Entity): Boolean {
        // I am not sure if I should use Flags.HURT_xxx or Flags.CONTAINER
        return entityOperation(player, entity)
    }

    override fun canDamage(player: Player, entity: Entity): Boolean {
        return entityOperation(player, entity) && (entity !is Player || entity.getWorld().pvp)
    }

    private fun entityOperation(player: Player, entity: Entity): Boolean {
        val type = if (Util.isPassiveEntity(entity))
            Flags.HURT_ANIMALS
        else if (entity is AbstractVillager)
            Flags.HURT_VILLAGERS
        else if (Util.isHostileEntity(entity))
            Flags.HURT_MONSTERS
        else Flags.CONTAINER

        return BentoBox.getInstance()
            .islands
            .getIslandAt(entity.location)
            .map { it.isAllowed(User.getInstance(player), type) }
            .orElse(true)
    }

    override fun getAntiGriefPluginName(): String {
        return "BentoBox"
    }

    companion object {

        @Awake(LifeCycle.ACTIVE)
        fun init() {
            AntiGriefChecker.registerNewCompatibility(BentoBoxComp())
        }
    }
}