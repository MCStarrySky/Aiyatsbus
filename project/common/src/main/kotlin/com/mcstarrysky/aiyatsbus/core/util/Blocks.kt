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
package com.mcstarrysky.aiyatsbus.core.util

import org.bukkit.Location
import org.bukkit.block.Block
import java.util.*
import java.util.concurrent.ConcurrentLinkedQueue

/**
 * Aiyatsbus
 * com.mcstarrysky.aiyatsbus.core.util.inject.Blocks
 *
 * @author mical
 * @date 2024/8/27 17:35
 */
object BlockUtils {

    private val blockDirections = listOf(
        // 六个基本方向
        doubleArrayOf(0.0, 1.0, 0.0),
        doubleArrayOf(0.0, -1.0, 0.0),
        doubleArrayOf(1.0, 0.0, 0.0),
        doubleArrayOf(-1.0, 0.0, 0.0),
        doubleArrayOf(0.0, 0.0, -1.0),
        doubleArrayOf(0.0, 0.0, 1.0),
        // 对角线方向
        doubleArrayOf(1.0, 1.0, 0.0),
        doubleArrayOf(-1.0, -1.0, 0.0),
        doubleArrayOf(1.0, -1.0, 0.0),
        doubleArrayOf(-1.0, 1.0, 0.0),
        doubleArrayOf(0.0, 1.0, -1.0),
        doubleArrayOf(0.0, -1.0, 1.0),
        doubleArrayOf(0.0, -1.0, -1.0),
        doubleArrayOf(0.0, 1.0, 1.0),
        doubleArrayOf(1.0, 0.0, -1.0),
        doubleArrayOf(-1.0, 0.0, 1.0),
        doubleArrayOf(-1.0, 0.0, -1.0),
        doubleArrayOf(1.0, 0.0, 1.0)
    )

    /**
     * 获取矿脉, amount 为获取方块位置数量, -1 则为获取完整的矿脉
     */
    fun getVein(block: Block, amount: Int? = null): List<Location> {
        val max = amount ?: -1
        val mines = LinkedList<Location>()
        val queue = ConcurrentLinkedQueue<Location>()
        // 从破坏的方块位置开始, 使用广度优先搜索 BFS 算法探索所有相同矿物位置
        queue += block.location
        while (!queue.isEmpty()) {
            // 如果达到了数量限制
            if (max != -1 && mines.size >= max) break
            // 获取当前位置
            val current = queue.poll()
            // 遍历向外一层的方块, 添加同种方块(矿物)
            blockDirections.map { current.clone().add(it[0], it[1], it[2]) }
                .filter { it.block.type == current.block.type && it !in mines }
                .forEach {
                    queue += it
                    mines += it
                }
        }
        // 防止重复挖掘自身
        return mines.filter { it != block.location }
    }
}