package com.mcstarrysky.aiyatsbus.core.util

import com.google.gson.JsonParser
import com.mcstarrysky.aiyatsbus.core.Aiyatsbus
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer
import org.bukkit.Location
import org.bukkit.NamespacedKey
import org.bukkit.World
import org.bukkit.entity.Entity
import org.bukkit.entity.LivingEntity
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.Damageable
import org.bukkit.inventory.meta.EnchantmentStorageMeta
import org.bukkit.inventory.meta.ItemMeta
import org.bukkit.metadata.Metadatable
import org.bukkit.persistence.PersistentDataHolder
import org.bukkit.persistence.PersistentDataType
import taboolib.library.reflex.UnsafeAccess
import taboolib.module.chat.ComponentText
import taboolib.module.chat.component
import taboolib.platform.util.*
import java.io.File
import java.lang.reflect.Field

/**
 * Aiyatsbus
 * com.mcstarrysky.aiyatsbus.impl.registration.modern.Utils
 *
 * @author mical
 * @since 2024/2/17 17:07
 */

/**
 * 物品在铁砧上的操作数
 */
var ItemStack.repairCost: Int
    get() = Aiyatsbus.api().getMinecraftAPI().getRepairCost(this)
    set(value) = Aiyatsbus.api().getMinecraftAPI().setRepairCost(this, value)

/**
 * 标记
 */
fun Metadatable.mark(key: String) {
    setMeta(key, bukkitPlugin)
}

fun Metadatable.unmark(key: String) {
    removeMeta(key)
}

/**
 * 旧版 JsonParser
 * 旧版没有 parseString 静态方法
 */
val JSON_PARSER = JsonParser()

/**
 * 旧版文本序列化器
 */
val LEGACY_COMPONENT_SERIALIZER = LegacyComponentSerializer.builder()
    .character('\u00a7')
    .useUnusualXRepeatedCharacterHexFormat()
    .hexColors()
    .build()

/**
 * 将带有 &a 之类的旧版文本转换为 Adventure Component
 */
fun String.legacyToAdventure(): Component {
    return LEGACY_COMPONENT_SERIALIZER.deserialize(this)
}

/**
 * 设置 static final 字段
 */
fun Field.setStaticFinal(value: Any) {
    val offset = UnsafeAccess.unsafe.staticFieldOffset(this)
    UnsafeAccess.unsafe.putObject(UnsafeAccess.unsafe.staticFieldBase(this), offset, value)
}

/**
 * 判断物品是否为 null 或是空气方块
 */
val ItemStack?.isNull get() = this?.isAir ?: true

/**
 * 判断物品是否为附魔书
 */
val ItemStack.isEnchantedBook get() = itemMeta is EnchantmentStorageMeta

/**
 * 获取/修改物品显示名称
 */
var ItemStack.name
    get() = itemMeta?.displayName
    set(value) {
        modifyMeta<ItemMeta> { setDisplayName(value) }
    }

/**
 * 获取/修改物品耐久
 */
var ItemStack.damage
    get() = (itemMeta as? Damageable)?.damage ?: 0
    set(value) {
        modifyMeta<Damageable> { damage = value }
    }

/**
 * 物品最大耐久度
 * */
val ItemStack.maxDurability: Int
    get() = this.type.maxDurability.toInt()

/**
 * 物品耐久度
 * */
var ItemStack.dura: Int
    get() = this.maxDurability - damage
    set(value) {
        this.damage = this.maxDurability - value
    }

/**
 * 将 Location 的 世界 和 XYZ 信息序列化成文本字符串
 */
val Location.serialized get() = "${world.name},$blockX,$blockY,$blockZ"

/**
 * 对 LivingEntity 造成真实伤害, 插件和原版无法减伤
 */
fun LivingEntity.realDamage(amount: Double, who: Entity? = null) {
    health = maxOf(0.1, health - amount + 0.5)
    damage(0.5, who)
    if (isDead) health = 0.0
}


/**
 * 从 PDC 获取内容
 */
operator fun <T, Z> PersistentDataHolder.get(key: String, type: PersistentDataType<T, Z>): Z? {
    return persistentDataContainer.get(NamespacedKey("aiyatsbus", key), type)
}

/**
 * 向 PDC 设置内容
 */
operator fun <T, Z : Any> PersistentDataHolder.set(key: String, type: PersistentDataType<T, Z>, value: Z) {
    persistentDataContainer.set(NamespacedKey("aiyatsbus", key), type, value)
}

/**
 * 判断 PDC 是否包含某个键
 */
fun <T, Z : Any> PersistentDataHolder.has(key: String, type: PersistentDataType<T, Z>): Boolean {
    return persistentDataContainer.has(NamespacedKey("aiyatsbus", key), type)
}

/**
 * 从 PDC 移除内容
 */
fun PersistentDataHolder.remove(key: String) {
    return persistentDataContainer.remove(NamespacedKey("aiyatsbus", key))
}

/**
 * 将 List<String> 构建为复合文本并上色
 */
fun List<String>?.toBuiltComponent(): List<ComponentText> {
    return this?.map { it.component().buildColored() } ?: emptyList()
}

/**
 * ItemsAdder 是否存在
 */
internal val itemsAdderEnabled = runCatching { Class.forName("dev.lone.itemsadder.api.ItemsAdder") }.isSuccess

/**
 * 嵌套读取文件夹内的所有指定后缀名的文件
 */
fun File.deepRead(extension: String): List<File> {
    val files = mutableListOf<File>()
    listFiles()?.forEach {
        if (it.isDirectory) {
            files.addAll(it.deepRead(extension))
        } else if (it.extension == extension) {
            files.add(it)
        }
    }
    return files
}

/**
 * 世界是否为白天
 */
val World.isDay: Boolean
    get() = time < 12300 || time > 23850

/**
 * 世界是否为黑夜
 */
val World.isNight: Boolean
    get() = time in 12301..23849