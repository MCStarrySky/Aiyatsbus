package com.mcstarrysky.aiyatsbus.module.ingame.ui.internal.function

import com.mcstarrysky.aiyatsbus.core.util.JSON_PARSER
import com.mojang.authlib.GameProfile
import com.mojang.authlib.properties.Property
import org.bukkit.Bukkit
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.SkullMeta
import taboolib.common5.util.decodeBase64
import taboolib.library.reflex.Reflex.Companion.setProperty
import taboolib.module.nms.MinecraftVersion
import taboolib.platform.util.modifyMeta
import java.net.URL
import java.util.*

/**
 * Aiyatsbus
 * com.mcstarrysky.aiyatsbus.module.ui.internal.function.Skull
 *
 * @author mical
 * @since 2024/2/18 12:21
 */
infix fun ItemStack.textured(headBase64: String): ItemStack {
    return modifyMeta<SkullMeta> {
        if (MinecraftVersion.majorLegacy >= 12000) {
            val profile = Bukkit.createProfile(UUID.randomUUID(), "TabooLib")
            val textures = profile.textures
            textures.skin = URL(getTextureURLFromBase64(headBase64))
            profile.setTextures(textures)
            playerProfile = profile
        } else {
            val profile = GameProfile(UUID.randomUUID(), "TabooLib")
            val texture = if (headBase64.length in 60..100) encodeTexture(headBase64) else headBase64
            profile.properties.put("textures", Property("textures", texture, "Aiyatsbus_TexturedSkull"))

            setProperty("profile", profile)
        }
    }
}

@Suppress("HttpUrlsUsage")
fun encodeTexture(input: String): String {
    return with(Base64.getEncoder()) {
        encodeToString("{\"textures\":{\"SKIN\":{\"url\":\"http://textures.minecraft.net/texture/$input\"}}}".toByteArray())
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