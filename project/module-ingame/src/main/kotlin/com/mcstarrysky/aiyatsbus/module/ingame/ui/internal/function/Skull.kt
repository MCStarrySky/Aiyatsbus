package com.mcstarrysky.aiyatsbus.module.ingame.ui.internal.function

import com.mcstarrysky.aiyatsbus.core.util.JSON_PARSER
import org.bukkit.Bukkit
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.SkullMeta
import taboolib.common5.util.decodeBase64
import taboolib.common5.util.parseUUID
import taboolib.platform.util.modifyMeta
import java.net.URL

/**
 * Aiyatsbus
 * com.mcstarrysky.aiyatsbus.module.ui.internal.function.Skull
 *
 * @author mical
 * @since 2024/2/18 12:21
 */
infix fun ItemStack.textured(headBase64: String): ItemStack {
    return modifyMeta<SkullMeta> {
        val profile = Bukkit.createProfile("036fd219-4618-3c50-92ab-e687de3eb47a".parseUUID(), "HamsterYDS")
        val textures = profile.textures
        textures.skin = URL(getTextureURLFromBase64(headBase64))
        profile.setTextures(textures)
        playerProfile = profile
    }
}

private fun getTextureURLFromBase64(headBase64: String): String {
    return JSON_PARSER
        .parse(String(headBase64.decodeBase64()))
        .asJsonObject
        .getAsJsonObject("textures")
        .getAsJsonObject("SKIN")
        .get("url")
        .asString
}