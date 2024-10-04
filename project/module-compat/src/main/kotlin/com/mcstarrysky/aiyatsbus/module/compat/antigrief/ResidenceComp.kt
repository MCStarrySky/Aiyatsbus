/*
 *  Copyright (C) 2022-2024 SummerIceBearStudio
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package com.mcstarrysky.aiyatsbus.module.compat.antigrief

import com.bekvon.bukkit.residence.Residence
import com.bekvon.bukkit.residence.containers.Flags
import com.bekvon.bukkit.residence.listeners.ResidenceEntityListener
import com.bekvon.bukkit.residence.protection.FlagPermissions
import com.bekvon.bukkit.residence.utils.Utils
import com.mcstarrysky.aiyatsbus.core.compat.AntiGrief
import com.mcstarrysky.aiyatsbus.core.compat.AntiGriefChecker
import org.bukkit.Location
import org.bukkit.entity.Entity
import org.bukkit.entity.Player
import taboolib.common.LifeCycle
import taboolib.common.platform.Awake
import java.util.*

/**
 * Aiyatsbus
 * com.mcstarrysky.aiyatsbus.module.compat.antigrief.ResidenceComp
 *
 * @author mical
 * @since 2024/4/4 13:28
 */
class ResidenceComp : AntiGrief {

    override fun canPlace(player: Player, location: Location): Boolean {
        return Optional.ofNullable(Residence.getInstance().residenceManager.getByLoc(location))
            .map { claimedResidence ->
                claimedResidence.permissions.playerHas(player, Flags.place, true)
            }
            .orElse(true)
    }

    override fun canBreak(player: Player, location: Location): Boolean {
        return Optional.ofNullable(Residence.getInstance().residenceManager.getByLoc(location))
            .map { claimedResidence ->
                claimedResidence.permissions.playerHas(player, Flags.destroy, true)
            }
            .orElse(true)
    }

    override fun canInteract(player: Player, location: Location): Boolean {
        return Optional.ofNullable(Residence.getInstance().residenceManager.getByLoc(location))
            .map { claimedResidence ->
                claimedResidence.permissions.playerHas(player, Flags.use, true)
            }
            .orElse(true)
    }

    override fun canInteractEntity(player: Player, entity: Entity): Boolean {
        return Optional.ofNullable(Residence.getInstance().residenceManager.getByLoc(entity.location))
            .map { claimedResidence ->
                claimedResidence.permissions.playerHas(player, Flags.build, true)
            }
            .orElse(true)
    }

    override fun canDamage(player: Player, entity: Entity): Boolean {
        return Optional.ofNullable(Residence.getInstance().residenceManager.getByLoc(entity.location))
            .map { claimedResidence ->
                if (entity is Player) {
                    val src =
                        Residence.getInstance().getPermsByLoc(player.location)
                            .has(Flags.pvp, FlagPermissions.FlagCombo.TrueOrNone)
                    val target =
                        Residence.getInstance().getPermsByLoc(entity.getLocation())
                            .has(Flags.pvp, FlagPermissions.FlagCombo.TrueOrNone)
                    return@map src && target && player.world.pvp
                }
                if (Utils.isAnimal(entity)) {
                    return@map claimedResidence.permissions
                        .playerHas(player, Flags.animalkilling, true)
                } else if (ResidenceEntityListener.isMonster(entity)) {
                    return@map claimedResidence.permissions
                        .playerHas(player, Flags.mobkilling, true)
                }
                null
            }
            .orElse(true)!!
    }

    override fun getAntiGriefPluginName(): String {
        return "Residence"
    }

    companion object {

        @Awake(LifeCycle.ACTIVE)
        fun init() {
            AntiGriefChecker.registerNewCompatibility(ResidenceComp())
        }
    }
}