package com.mcstarrysky.aiyatsbus.core.util

import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.World
import org.bukkit.entity.Player
import org.bukkit.util.Vector
import taboolib.common.platform.function.submit
import kotlin.math.roundToInt

val Location.serialized get() = "${world.name},$blockX,$blockY,$blockZ"

fun String.toLoc(): Location {
    val split = split(",")
    return Location(Bukkit.getWorld(split[0]), split[1].toDouble(), split[2].toDouble(), split[3].toDouble())
}

fun Location.add(x: Number, y: Number, z: Number) {
    add(x.toDouble(), y.toDouble(), z.toDouble())
}

fun Vector.add(x: Number, y: Number, z: Number) {
    add(vector(x, y, z))
}

fun vector(x: Number, y: Number, z: Number) = Vector(x.toDouble(), y.toDouble(), z.toDouble())

fun loc(worldName: String, x: Number, y: Number, z: Number): Location {
    return loc(Bukkit.getWorld(worldName)!!, x, y, z)
}

fun loc(world: World, x: Number, y: Number, z: Number): Location {
    return Location(world, x.toDouble(), y.toDouble(), z.toDouble())
}

fun Player?.tmpBlock(loc: Location, type: Material, duration: Double = 1.0) {
    this ?: return
    sendBlockChange(loc, type, 0)
    //TODO 应该要可以自定义是否显示破坏
    val total = (duration * 20).roundToInt()
    var current = 0
    submit(period = 1L) {
        if (++current >= total) {
            cancel()
            sendBlockDamage(loc, 0.0f)
            sendBlockChange(loc, loc.block.blockData)
        } else sendBlockDamage(loc, current.toFloat() / total)
    }
}