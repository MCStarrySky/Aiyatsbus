package cc.polarastrum.aiyatsbus.module.compat.item

import cc.polarastrum.aiyatsbus.core.data.registry.TargetItemType
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

/**
 * 检查作为物品源的插件是否可用。
 *
 * 只要求插件已安装（不要求已经启用，避免物品源注册受插件加载顺序影响），
 * 并且 [apiClass] 存在于类路径中，
 * 避免插件存在但 API 版本不匹配时抛出类加载错误。
 *
 * @param pluginName 插件名称
 * @param apiClass 需要使用的 API 类名
 */
internal fun itemSourceAvailable(pluginName: String, apiClass: String): Boolean {
    if (Bukkit.getPluginManager().getPlugin(pluginName) == null) return false
    return runCatching { Class.forName(apiClass) }.isSuccess
}

/**
 * 第三方物品库物品目标类型的通用实现。
 *
 * 子类只需要提供 [resolveItemId]，把物品解析为物品库中的 ID 即可，
 * 物品源 API 抛出的异常会被视为「物品不属于该物品库」。
 */
internal abstract class CustomItemTargetType(
    /** 配置中填写的物品 ID */
    override val identifier: String,
    /** 最大附魔数量 */
    override val capability: Int?,
    /** 原版附魔台使用的附魔能力值 */
    override val enchantability: Int
) : TargetItemType {

    override val hasEnchantability = enchantability > 0

    /** 自定义物品不参与原版附魔台的注册 */
    override val vanillaMaterial: Material? = null

    override fun matches(item: ItemStack): Boolean {
        val actualId = runCatching { resolveItemId(item) }.getOrNull() ?: return false
        return accept(actualId)
    }

    /** 解析物品在物品库中的 ID，物品不属于该物品库时返回 null */
    protected abstract fun resolveItemId(item: ItemStack): String?

    /** 判断解析出的物品 ID 是否与配置一致，默认忽略大小写 */
    protected open fun accept(actualId: String): Boolean = actualId.equals(identifier, ignoreCase = true)
}
