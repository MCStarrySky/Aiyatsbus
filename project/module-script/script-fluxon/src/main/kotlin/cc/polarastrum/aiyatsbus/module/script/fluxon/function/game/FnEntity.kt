package cc.polarastrum.aiyatsbus.module.script.fluxon.function.game

import cc.polarastrum.aiyatsbus.core.asLangOrNull
import cc.polarastrum.aiyatsbus.core.sendLang
import cc.polarastrum.aiyatsbus.core.util.Vectors
import cc.polarastrum.aiyatsbus.core.util.checkIfIsNPC
import cc.polarastrum.aiyatsbus.core.util.equippedItems
import cc.polarastrum.aiyatsbus.core.util.isBehind
import cc.polarastrum.aiyatsbus.core.util.realDamage
import cc.polarastrum.aiyatsbus.module.script.fluxon.FluxonScriptHandler
import cc.polarastrum.aiyatsbus.module.script.fluxon.relocate.FluxonRelocate
import org.bukkit.entity.Entity
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.inventory.EquipmentSlot
import org.bukkit.inventory.ItemStack
import org.bukkit.potion.PotionEffect
import org.bukkit.util.Vector
import org.tabooproject.fluxon.runtime.FluxonRuntime
import org.tabooproject.fluxon.runtime.FunctionSignature.returns
import org.tabooproject.fluxon.runtime.Type
import org.tabooproject.fluxon.runtime.java.Export
import org.tabooproject.fluxon.runtime.java.Optional
import taboolib.common.LifeCycle
import taboolib.common.Requires
import taboolib.common.platform.Awake
import taboolib.library.xseries.XPotion
//import taboolib.module.nms.getI18nName

/**
 * Aiyatsbus
 * cc.polarastrum.aiyatsbus.module.script.fluxon.function.game.FnEntity
 *
 * @author mical
 * @since 2026/1/2 00:09
 */
@Requires(missingClasses = ["!org.tabooproject.fluxon.ParseScript"])
@FluxonRelocate
object FnEntity {

    val TYPE = Type.fromClass(FnEntity::class.java)!!

    @Awake(LifeCycle.LOAD)
    fun init() {
        FluxonScriptHandler.DEFAULT_PACKAGE_AUTO_IMPORT += "aiy:entity"
        with(FluxonRuntime.getInstance()) {
            registerFunction("aiy:entity", "entity", returns(TYPE).noParams()) { it.setReturnRef(FnEntity) }
            exportRegistry.registerClass(FnEntity::class.java, "aiy:entity")
        }
    }

    @Export
    fun equippedItems(entity: LivingEntity): Map<EquipmentSlot, ItemStack> {
        return entity.equippedItems
    }

    @Export
    fun realDamage(entity: LivingEntity, damage: Double, @Optional by: Entity?) {
        entity.realDamage(damage, by)
    }

    @Export
    fun entityName(entity: Entity, @Optional player: Player?): String {
        return if (entity is Player) entity.name else entity.customName ?: entity.type.name// TODO ?: entity.getI18nName(player)
    }

    /**
     * 向实体发送语言文件消息
     *
     * Entity 继承 CommandSender, 因此非玩家实体也可以作为接收者传入, 只是不会真正显示消息.
     *
     * @param entity 接收者
     * @param node 语言节点
     * @param args 替换参数, 可以是单个值或列表, 按顺序替换 {0}, {1} ...
     */
    @Export
    fun sendLang(entity: Entity, node: String, @Optional args: Any?) {
        entity.sendLang(node, args = normalizeLangArgs(args))
    }

    /**
     * 获取实体对应的语言文件文本
     *
     * @param entity 接收者
     * @param node 语言节点
     * @param args 替换参数, 可以是单个值或列表, 按顺序替换 {0}, {1} ...
     * @return 语言文本, 节点不存在时返回 null
     */
    @Export
    fun asLang(entity: Entity, node: String, @Optional args: Any?): String? {
        return entity.asLangOrNull(node, args = normalizeLangArgs(args))
    }

    /**
     * 把脚本传入的替换参数规整成语言系统需要的数组
     *
     * 支持 null, 单个值, 数组和集合三种形式.
     */
    private fun normalizeLangArgs(args: Any?): Array<Any> {
        return when (args) {
            null -> emptyArray()
            is Collection<*> -> args.filterNotNull().toTypedArray()
            is Array<*> -> args.filterNotNull().toTypedArray()
            else -> arrayOf(args)
        }
    }

    @Export
    fun addSafetyVelocity(entity: LivingEntity, vector: Vector, @Optional checkKnockback: Boolean?) {
        Vectors.addVelocity(entity, vector, checkKnockback ?: false)
    }

    @Export
    fun isBehind(entity1: LivingEntity, entity2: LivingEntity): Boolean {
        return entity1.isBehind(entity2)
    }

    @Export
    fun addPotionEffect(
        entity: LivingEntity,
        type: String,
        duration: Int,
        amplifier: Int,
        @Optional ambient: Boolean?,
        @Optional particles: Boolean?,
        @Optional icon: Boolean?
    ) {
        entity.addPotionEffect(
            PotionEffect(
                XPotion.of(type).orElseThrow().potionEffectType ?: return,
                duration, amplifier, ambient ?: true, particles ?: true, icon ?: true
            )
        )
    }

    @Export
    fun getActivePotionEffect(entity: LivingEntity, type: String): PotionEffect? {
        return entity.activePotionEffects.filter {
            it.type == (XPotion.of(type).orElseThrow().potionEffectType)
        }.firstOrNull()
    }

    @Export
    fun hasPotionEffect(entity: LivingEntity, type: String): Boolean {
        return entity.hasPotionEffect(XPotion.of(type).orElseThrow().potionEffectType ?: return false )
    }

    @Export
    fun removePotionEffect(entity: LivingEntity, type: String) {
        entity.removePotionEffect(XPotion.of(type).orElseThrow().potionEffectType ?: return)
    }

    @Export
    fun isNPC(entity: Entity?): Boolean = entity.checkIfIsNPC()

    /**
     * 较为常用
     */
    @Export
    fun isLivingEntity(entity: Entity?): Boolean = entity is LivingEntity

    /**
     * 较为常用
     */
    @Export
    fun isPlayer(entity: Entity?): Boolean = entity is Player
}
