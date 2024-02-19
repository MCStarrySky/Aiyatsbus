package com.mcstarrysky.aiyatsbus.module.custom.splendid.mechanism.entry.`object`

import com.mcstarrysky.aiyatsbus.module.custom.splendid.mechanism.entry.internal.ObjectEntry

object ObjectString : ObjectEntry<String>() {

    override fun holderize(obj: String) = this to obj
    override fun disholderize(holder: String) = holder
}