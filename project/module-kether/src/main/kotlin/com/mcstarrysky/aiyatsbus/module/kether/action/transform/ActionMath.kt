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
package com.mcstarrysky.aiyatsbus.module.kether.action.transform

import taboolib.module.kether.*

/**
 * Chemdah
 * ink.ptms.chemdah.module.kether.ActionMath
 *
 * @author sky
 * @since 2021/6/14 2:59 下午
 */
object ActionMath {

    @KetherParser(["max"], shared = true)
    fun max() = scriptParser {
        val n1 = it.nextParsedAction()
        val n2 = it.nextParsedAction()
        actionFuture { f ->
            run(n1).double { n1 ->
                run(n2).double { n2 ->
                    f.complete(kotlin.math.max(n1, n2))
                }
            }
        }
    }

    @KetherParser(["min"], shared = true)
    fun min() = scriptParser {
        val n1 = it.nextParsedAction()
        val n2 = it.nextParsedAction()
        actionFuture { f ->
            run(n1).double { n1 ->
                run(n2).double { n2 ->
                    f.complete(kotlin.math.min(n1, n2))
                }
            }
        }
    }

    @KetherParser(["ceil"], shared = true)
    fun ceil() = scriptParser {
        val n1 = it.nextParsedAction()
        actionTake { run(n1).double { n1 -> kotlin.math.ceil(n1) } }
    }

    @KetherParser(["floor"], shared = true)
    fun floor() = scriptParser {
        val n1 = it.nextParsedAction()
        actionTake { run(n1).double { n1 -> kotlin.math.floor(n1) } }
    }
}