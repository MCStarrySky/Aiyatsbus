/*
 * This file is part of Vulpecula, licensed under the MIT License.
 *
 *  Copyright (c) 2018 Bkm016
 *
 *  Permission is hereby granted, free of charge, to any person obtaining a copy
 *  of this software and associated documentation files (the "Software"), to deal
 *  in the Software without restriction, including without limitation the rights
 *  to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *  copies of the Software, and to permit persons to whom the Software is
 *  furnished to do so, subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in all
 *  copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 *  SOFTWARE.
 */
package com.mcstarrysky.aiyatsbus.module.kether

import java.util.concurrent.CompletableFuture
import java.util.concurrent.atomic.AtomicInteger

/**
 * Vulpecula
 * top.lanscarlos.vulpecula.bacikal
 *
 * @author Lanscarlos
 * @since 2023-03-20 22:15
 */

data class Applicative2<T1, T2>(
    val t1: T1,
    val t2: T2
)

data class Applicative3<T1, T2, T3>(
    val t1: T1,
    val t2: T2,
    val t3: T3
)

data class Applicative4<T1, T2, T3, T4>(
    val t1: T1,
    val t2: T2,
    val t3: T3,
    val t4: T4
)

data class Applicative5<T1, T2, T3, T4, T5>(
    val t1: T1,
    val t2: T2,
    val t3: T3,
    val t4: T4,
    val t5: T5
)

data class Applicative6<T1, T2, T3, T4, T5, T6>(
    val t1: T1,
    val t2: T2,
    val t3: T3,
    val t4: T4,
    val t5: T5,
    val t6: T6
)

data class Applicative7<T1, T2, T3, T4, T5, T6, T7>(
    val t1: T1,
    val t2: T2,
    val t3: T3,
    val t4: T4,
    val t5: T5,
    val t6: T6,
    val t7: T7
)

data class Applicative8<T1, T2, T3, T4, T5, T6, T7, T8>(
    val t1: T1,
    val t2: T2,
    val t3: T3,
    val t4: T4,
    val t5: T5,
    val t6: T6,
    val t7: T7,
    val t8: T8
)

data class Applicative9<T1, T2, T3, T4, T5, T6, T7, T8, T9>(
    val t1: T1,
    val t2: T2,
    val t3: T3,
    val t4: T4,
    val t5: T5,
    val t6: T6,
    val t7: T7,
    val t8: T8,
    val t9: T9
)

data class Applicative10<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10>(
    val t1: T1,
    val t2: T2,
    val t3: T3,
    val t4: T4,
    val t5: T5,
    val t6: T6,
    val t7: T7,
    val t8: T8,
    val t9: T9,
    val t10: T10
)

data class Applicative11<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11>(
    val t1: T1,
    val t2: T2,
    val t3: T3,
    val t4: T4,
    val t5: T5,
    val t6: T6,
    val t7: T7,
    val t8: T8,
    val t9: T9,
    val t10: T10,
    val t11: T11
)

data class Applicative12<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12>(
    val t1: T1,
    val t2: T2,
    val t3: T3,
    val t4: T4,
    val t5: T5,
    val t6: T6,
    val t7: T7,
    val t8: T8,
    val t9: T9,
    val t10: T10,
    val t11: T11,
    val t12: T12
)

data class Applicative13<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13>(
    val t1: T1,
    val t2: T2,
    val t3: T3,
    val t4: T4,
    val t5: T5,
    val t6: T6,
    val t7: T7,
    val t8: T8,
    val t9: T9,
    val t10: T10,
    val t11: T11,
    val t12: T12,
    val t13: T13
)

data class Applicative14<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14>(
    val t1: T1,
    val t2: T2,
    val t3: T3,
    val t4: T4,
    val t5: T5,
    val t6: T6,
    val t7: T7,
    val t8: T8,
    val t9: T9,
    val t10: T10,
    val t11: T11,
    val t12: T12,
    val t13: T13,
    val t14: T14
)

data class Applicative15<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15>(
    val t1: T1,
    val t2: T2,
    val t3: T3,
    val t4: T4,
    val t5: T5,
    val t6: T6,
    val t7: T7,
    val t8: T8,
    val t9: T9,
    val t10: T10,
    val t11: T11,
    val t12: T12,
    val t13: T13,
    val t14: T14,
    val t15: T15
)

data class Applicative16<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16>(
    val t1: T1,
    val t2: T2,
    val t3: T3,
    val t4: T4,
    val t5: T5,
    val t6: T6,
    val t7: T7,
    val t8: T8,
    val t9: T9,
    val t10: T10,
    val t11: T11,
    val t12: T12,
    val t13: T13,
    val t14: T14,
    val t15: T15,
    val t16: T16
)

/**
 * 将一组 CompletableFuture 转成对应列表结果
 * */
inline fun <reified T: Any?> applicative(queue: List<CompletableFuture<T>>): CompletableFuture<List<T>> {
    val result = CompletableFuture<List<T>>()

    if (queue.isEmpty()) {
        // 队列为空
        result.complete(listOf())
    } else if (queue.size == 1) {
        // 队列仅有一个
        val future = queue.first()
        if (future.isDone) {
            result.complete(listOf(future.getNow(null)))
        } else {
            future.thenAccept { result.complete(listOf(it)) }
        }
    } else {
        // 队列有多个
        val counter = AtomicInteger(0)
        for (it in queue) {
            if (it.isDone) {
                val count = counter.incrementAndGet()

                // 判断 futures 是否全部执行完毕
                if (!result.isDone && count >= queue.size) {
                    result.complete(queue.map { it.getNow(null) })
                    return result
                }
            } else {
                it.thenRun {
                    val count = counter.incrementAndGet()

                    // 判断 futures 是否全部执行完毕
                    if (!result.isDone && count >= queue.size) {
                        result.complete(queue.map { it.getNow(null) })
                    }
                }
            }
        }
    }
    return result
}

fun <P1, P2> applicative(
    p1: CompletableFuture<P1>,
    p2: CompletableFuture<P2>
): CompletableFuture<Applicative2<P1, P2>> {
    if (p1.isDone) {
        val t1 = p1.getNow(null)
        return if (p2.isDone) {
            val t2 = p2.getNow(null)
            CompletableFuture.completedFuture(Applicative2(t1, t2))
        } else {
            p2.thenApply { t2 -> Applicative2(t1, t2) }
        }
    } else {
        return p1.thenCompose { t1 ->
            if (p2.isDone) {
                val t2 = p2.getNow(null)
                CompletableFuture.completedFuture(Applicative2(t1, t2))
            } else {
                p2.thenApply { t2 -> Applicative2(t1, t2) }
            }
        }
    }
}

fun <P1, P2, P3> applicative(
    p1: CompletableFuture<P1>,
    p2: CompletableFuture<P2>,
    p3: CompletableFuture<P3>
): CompletableFuture<Applicative3<P1, P2, P3>> {
    val future = applicative(applicative(p1, p2), p3)
    return if (future.isDone) {
        val app = future.getNow(null)
        CompletableFuture.completedFuture(
            Applicative3(
                app.t1.t1,
                app.t1.t2,
                app.t2
            )
        )
    } else {
        future.thenApply { app ->
            Applicative3(
                app.t1.t1,
                app.t1.t2,
                app.t2
            )
        }
    }
}

fun <P1, P2, P3, P4> applicative(
    p1: CompletableFuture<P1>,
    p2: CompletableFuture<P2>,
    p3: CompletableFuture<P3>,
    p4: CompletableFuture<P4>
): CompletableFuture<Applicative4<P1, P2, P3, P4>> {
    val ap = applicative(
        applicative(p1, p2),
        applicative(p3, p4)
    )
    return if (ap.isDone) {
        val app = ap.getNow(null)
        CompletableFuture.completedFuture(
            Applicative4(
                app.t1.t1,
                app.t1.t2,
                app.t2.t1,
                app.t2.t2
            )
        )
    } else {
        ap.thenApply { app ->
            Applicative4(
                app.t1.t1,
                app.t1.t2,
                app.t2.t1,
                app.t2.t2
            )
        }
    }
}

fun <P1, P2, P3, P4, P5> applicative(
    p1: CompletableFuture<P1>,
    p2: CompletableFuture<P2>,
    p3: CompletableFuture<P3>,
    p4: CompletableFuture<P4>,
    p5: CompletableFuture<P5>
): CompletableFuture<Applicative5<P1, P2, P3, P4, P5>> {
    val ap = applicative(
        applicative(p1, p2, p3),
        applicative(p4, p5)
    )
    return if (ap.isDone) {
        val app = ap.getNow(null)
        CompletableFuture.completedFuture(
            Applicative5(
                app.t1.t1,
                app.t1.t2,
                app.t1.t3,
                app.t2.t1,
                app.t2.t2
            )
        )
    } else {
        ap.thenApply { app ->
            Applicative5(
                app.t1.t1,
                app.t1.t2,
                app.t1.t3,
                app.t2.t1,
                app.t2.t2
            )
        }
    }
}

fun <P1, P2, P3, P4, P5, P6> applicative(
    p1: CompletableFuture<P1>,
    p2: CompletableFuture<P2>,
    p3: CompletableFuture<P3>,
    p4: CompletableFuture<P4>,
    p5: CompletableFuture<P5>,
    p6: CompletableFuture<P6>
): CompletableFuture<Applicative6<P1, P2, P3, P4, P5, P6>> {
    val ap = applicative(
        applicative(p1, p2, p3),
        applicative(p4, p5, p6)
    )
    return if (ap.isDone) {
        val app = ap.getNow(null)
        CompletableFuture.completedFuture(
            Applicative6(
                app.t1.t1,
                app.t1.t2,
                app.t1.t3,
                app.t2.t1,
                app.t2.t2,
                app.t2.t3
            )
        )
    } else {
        ap.thenApply { app ->
            Applicative6(
                app.t1.t1,
                app.t1.t2,
                app.t1.t3,
                app.t2.t1,
                app.t2.t2,
                app.t2.t3
            )
        }
    }
}

fun <P1, P2, P3, P4, P5, P6, P7> applicative(
    p1: CompletableFuture<P1>,
    p2: CompletableFuture<P2>,
    p3: CompletableFuture<P3>,
    p4: CompletableFuture<P4>,
    p5: CompletableFuture<P5>,
    p6: CompletableFuture<P6>,
    p7: CompletableFuture<P7>
): CompletableFuture<Applicative7<P1, P2, P3, P4, P5, P6, P7>> {
    val ap = applicative(
        applicative(p1, p2, p3, p4),
        applicative(p5, p6, p7)
    )
    return if (ap.isDone) {
        val app = ap.getNow(null)
        CompletableFuture.completedFuture(
            Applicative7(
                app.t1.t1,
                app.t1.t2,
                app.t1.t3,
                app.t1.t4,
                app.t2.t1,
                app.t2.t2,
                app.t2.t3
            )
        )
    } else {
        ap.thenApply { app ->
            Applicative7(
                app.t1.t1,
                app.t1.t2,
                app.t1.t3,
                app.t1.t4,
                app.t2.t1,
                app.t2.t2,
                app.t2.t3
            )
        }
    }
}

fun <P1, P2, P3, P4, P5, P6, P7, P8> applicative(
    p1: CompletableFuture<P1>,
    p2: CompletableFuture<P2>,
    p3: CompletableFuture<P3>,
    p4: CompletableFuture<P4>,
    p5: CompletableFuture<P5>,
    p6: CompletableFuture<P6>,
    p7: CompletableFuture<P7>,
    p8: CompletableFuture<P8>
): CompletableFuture<Applicative8<P1, P2, P3, P4, P5, P6, P7, P8>> {
    val ap = applicative(
        applicative(p1, p2, p3, p4),
        applicative(p5, p6, p7, p8)
    )
    return if (ap.isDone) {
        val app = ap.getNow(null)
        CompletableFuture.completedFuture(
            Applicative8(
                app.t1.t1,
                app.t1.t2,
                app.t1.t3,
                app.t1.t4,
                app.t2.t1,
                app.t2.t2,
                app.t2.t3,
                app.t2.t4
            )
        )
    } else {
        ap.thenApply { app ->
            Applicative8(
                app.t1.t1,
                app.t1.t2,
                app.t1.t3,
                app.t1.t4,
                app.t2.t1,
                app.t2.t2,
                app.t2.t3,
                app.t2.t4
            )
        }
    }
}

fun <P1, P2, P3, P4, P5, P6, P7, P8, P9> applicative(
    p1: CompletableFuture<P1>,
    p2: CompletableFuture<P2>,
    p3: CompletableFuture<P3>,
    p4: CompletableFuture<P4>,
    p5: CompletableFuture<P5>,
    p6: CompletableFuture<P6>,
    p7: CompletableFuture<P7>,
    p8: CompletableFuture<P8>,
    p9: CompletableFuture<P9>
): CompletableFuture<Applicative9<P1, P2, P3, P4, P5, P6, P7, P8, P9>> {
    val ap = applicative(
        applicative(p1, p2, p3, p4, p5),
        applicative(p6, p7, p8, p9)
    )
    return if (ap.isDone) {
        val app = ap.getNow(null)
        CompletableFuture.completedFuture(
            Applicative9(
                app.t1.t1,
                app.t1.t2,
                app.t1.t3,
                app.t1.t4,
                app.t1.t5,
                app.t2.t1,
                app.t2.t2,
                app.t2.t3,
                app.t2.t4
            )
        )
    } else {
        ap.thenApply { app ->
            Applicative9(
                app.t1.t1,
                app.t1.t2,
                app.t1.t3,
                app.t1.t4,
                app.t1.t5,
                app.t2.t1,
                app.t2.t2,
                app.t2.t3,
                app.t2.t4
            )
        }
    }
}

fun <P1, P2, P3, P4, P5, P6, P7, P8, P9, P10> applicative(
    p1: CompletableFuture<P1>,
    p2: CompletableFuture<P2>,
    p3: CompletableFuture<P3>,
    p4: CompletableFuture<P4>,
    p5: CompletableFuture<P5>,
    p6: CompletableFuture<P6>,
    p7: CompletableFuture<P7>,
    p8: CompletableFuture<P8>,
    p9: CompletableFuture<P9>,
    p10: CompletableFuture<P10>
): CompletableFuture<Applicative10<P1, P2, P3, P4, P5, P6, P7, P8, P9, P10>> {
    val ap = applicative(
        applicative(p1, p2, p3, p4, p5),
        applicative(p6, p7, p8, p9, p10)
    )
    return if (ap.isDone) {
        val app = ap.getNow(null)
        CompletableFuture.completedFuture(
            Applicative10(
                app.t1.t1,
                app.t1.t2,
                app.t1.t3,
                app.t1.t4,
                app.t1.t5,
                app.t2.t1,
                app.t2.t2,
                app.t2.t3,
                app.t2.t4,
                app.t2.t5
            )
        )
    } else {
        ap.thenApply { app ->
            Applicative10(
                app.t1.t1,
                app.t1.t2,
                app.t1.t3,
                app.t1.t4,
                app.t1.t5,
                app.t2.t1,
                app.t2.t2,
                app.t2.t3,
                app.t2.t4,
                app.t2.t5
            )
        }
    }
}

fun <P1, P2, P3, P4, P5, P6, P7, P8, P9, P10, P11> applicative(
    p1: CompletableFuture<P1>,
    p2: CompletableFuture<P2>,
    p3: CompletableFuture<P3>,
    p4: CompletableFuture<P4>,
    p5: CompletableFuture<P5>,
    p6: CompletableFuture<P6>,
    p7: CompletableFuture<P7>,
    p8: CompletableFuture<P8>,
    p9: CompletableFuture<P9>,
    p10: CompletableFuture<P10>,
    p11: CompletableFuture<P11>,
): CompletableFuture<Applicative11<P1, P2, P3, P4, P5, P6, P7, P8, P9, P10, P11>> {
    val ap = applicative(
        applicative(p1, p2, p3, p4, p5, p6),
        applicative(p7, p8, p9, p10, p11)
    )
    return if (ap.isDone) {
        val app = ap.getNow(null)
        CompletableFuture.completedFuture(
            Applicative11(
                app.t1.t1,
                app.t1.t2,
                app.t1.t3,
                app.t1.t4,
                app.t1.t5,
                app.t1.t6,
                app.t2.t1,
                app.t2.t2,
                app.t2.t3,
                app.t2.t4,
                app.t2.t5
            )
        )
    } else {
        ap.thenApply { app ->
            Applicative11(
                app.t1.t1,
                app.t1.t2,
                app.t1.t3,
                app.t1.t4,
                app.t1.t5,
                app.t1.t6,
                app.t2.t1,
                app.t2.t2,
                app.t2.t3,
                app.t2.t4,
                app.t2.t5
            )
        }
    }
}

fun <P1, P2, P3, P4, P5, P6, P7, P8, P9, P10, P11, P12> applicative(
    p1: CompletableFuture<P1>,
    p2: CompletableFuture<P2>,
    p3: CompletableFuture<P3>,
    p4: CompletableFuture<P4>,
    p5: CompletableFuture<P5>,
    p6: CompletableFuture<P6>,
    p7: CompletableFuture<P7>,
    p8: CompletableFuture<P8>,
    p9: CompletableFuture<P9>,
    p10: CompletableFuture<P10>,
    p11: CompletableFuture<P11>,
    p12: CompletableFuture<P12>,
): CompletableFuture<Applicative12<P1, P2, P3, P4, P5, P6, P7, P8, P9, P10, P11, P12>> {
    val ap = applicative(
        applicative(p1, p2, p3, p4, p5, p6),
        applicative(p7, p8, p9, p10, p11, p12)
    )
    return if (ap.isDone) {
        val app = ap.getNow(null)
        CompletableFuture.completedFuture(
            Applicative12(
                app.t1.t1,
                app.t1.t2,
                app.t1.t3,
                app.t1.t4,
                app.t1.t5,
                app.t1.t6,
                app.t2.t1,
                app.t2.t2,
                app.t2.t3,
                app.t2.t4,
                app.t2.t5,
                app.t2.t6
            )
        )
    } else {
        ap.thenApply { app ->
            Applicative12(
                app.t1.t1,
                app.t1.t2,
                app.t1.t3,
                app.t1.t4,
                app.t1.t5,
                app.t1.t6,
                app.t2.t1,
                app.t2.t2,
                app.t2.t3,
                app.t2.t4,
                app.t2.t5,
                app.t2.t6
            )
        }
    }
}

fun <P1, P2, P3, P4, P5, P6, P7, P8, P9, P10, P11, P12, P13> applicative(
    p1: CompletableFuture<P1>,
    p2: CompletableFuture<P2>,
    p3: CompletableFuture<P3>,
    p4: CompletableFuture<P4>,
    p5: CompletableFuture<P5>,
    p6: CompletableFuture<P6>,
    p7: CompletableFuture<P7>,
    p8: CompletableFuture<P8>,
    p9: CompletableFuture<P9>,
    p10: CompletableFuture<P10>,
    p11: CompletableFuture<P11>,
    p12: CompletableFuture<P12>,
    p13: CompletableFuture<P13>,
): CompletableFuture<Applicative13<P1, P2, P3, P4, P5, P6, P7, P8, P9, P10, P11, P12, P13>> {
    val ap = applicative(
        applicative(p1, p2, p3, p4, p5, p6, p7),
        applicative(p8, p9, p10, p11, p12, p13)
    )
    return if (ap.isDone) {
        val app = ap.getNow(null)
        CompletableFuture.completedFuture(
            Applicative13(
                app.t1.t1,
                app.t1.t2,
                app.t1.t3,
                app.t1.t4,
                app.t1.t5,
                app.t1.t6,
                app.t1.t7,
                app.t2.t1,
                app.t2.t2,
                app.t2.t3,
                app.t2.t4,
                app.t2.t5,
                app.t2.t6,
            )
        )
    } else {
        ap.thenApply { app ->
            Applicative13(
                app.t1.t1,
                app.t1.t2,
                app.t1.t3,
                app.t1.t4,
                app.t1.t5,
                app.t1.t6,
                app.t1.t7,
                app.t2.t1,
                app.t2.t2,
                app.t2.t3,
                app.t2.t4,
                app.t2.t5,
                app.t2.t6,
            )
        }
    }
}

fun <P1, P2, P3, P4, P5, P6, P7, P8, P9, P10, P11, P12, P13, P14> applicative(
    p1: CompletableFuture<P1>,
    p2: CompletableFuture<P2>,
    p3: CompletableFuture<P3>,
    p4: CompletableFuture<P4>,
    p5: CompletableFuture<P5>,
    p6: CompletableFuture<P6>,
    p7: CompletableFuture<P7>,
    p8: CompletableFuture<P8>,
    p9: CompletableFuture<P9>,
    p10: CompletableFuture<P10>,
    p11: CompletableFuture<P11>,
    p12: CompletableFuture<P12>,
    p13: CompletableFuture<P13>,
    p14: CompletableFuture<P14>,
): CompletableFuture<Applicative14<P1, P2, P3, P4, P5, P6, P7, P8, P9, P10, P11, P12, P13, P14>> {
    val ap = applicative(
        applicative(p1, p2, p3, p4, p5, p6, p7),
        applicative(p8, p9, p10, p11, p12, p13, p14)
    )
    return if (ap.isDone) {
        val app = ap.getNow(null)
        CompletableFuture.completedFuture(
            Applicative14(
                app.t1.t1,
                app.t1.t2,
                app.t1.t3,
                app.t1.t4,
                app.t1.t5,
                app.t1.t6,
                app.t1.t7,
                app.t2.t1,
                app.t2.t2,
                app.t2.t3,
                app.t2.t4,
                app.t2.t5,
                app.t2.t6,
                app.t2.t7
            )
        )
    } else {
        ap.thenApply { app ->
            Applicative14(
                app.t1.t1,
                app.t1.t2,
                app.t1.t3,
                app.t1.t4,
                app.t1.t5,
                app.t1.t6,
                app.t1.t7,
                app.t2.t1,
                app.t2.t2,
                app.t2.t3,
                app.t2.t4,
                app.t2.t5,
                app.t2.t6,
                app.t2.t7
            )
        }
    }
}

fun <P1, P2, P3, P4, P5, P6, P7, P8, P9, P10, P11, P12, P13, P14, P15> applicative(
    p1: CompletableFuture<P1>,
    p2: CompletableFuture<P2>,
    p3: CompletableFuture<P3>,
    p4: CompletableFuture<P4>,
    p5: CompletableFuture<P5>,
    p6: CompletableFuture<P6>,
    p7: CompletableFuture<P7>,
    p8: CompletableFuture<P8>,
    p9: CompletableFuture<P9>,
    p10: CompletableFuture<P10>,
    p11: CompletableFuture<P11>,
    p12: CompletableFuture<P12>,
    p13: CompletableFuture<P13>,
    p14: CompletableFuture<P14>,
    p15: CompletableFuture<P15>,
): CompletableFuture<Applicative15<P1, P2, P3, P4, P5, P6, P7, P8, P9, P10, P11, P12, P13, P14, P15>> {
    val ap = applicative(
        applicative(p1, p2, p3, p4, p5, p6, p7, p8),
        applicative(p9, p10, p11, p12, p13, p14, p15)
    )
    return if (ap.isDone) {
        val app = ap.getNow(null)
        CompletableFuture.completedFuture(
            Applicative15(
                app.t1.t1,
                app.t1.t2,
                app.t1.t3,
                app.t1.t4,
                app.t1.t5,
                app.t1.t6,
                app.t1.t7,
                app.t1.t8,
                app.t2.t1,
                app.t2.t2,
                app.t2.t3,
                app.t2.t4,
                app.t2.t5,
                app.t2.t6,
                app.t2.t7
            )
        )
    } else {
        ap.thenApply { app ->
            Applicative15(
                app.t1.t1,
                app.t1.t2,
                app.t1.t3,
                app.t1.t4,
                app.t1.t5,
                app.t1.t6,
                app.t1.t7,
                app.t1.t8,
                app.t2.t1,
                app.t2.t2,
                app.t2.t3,
                app.t2.t4,
                app.t2.t5,
                app.t2.t6,
                app.t2.t7
            )
        }
    }
}

fun <P1, P2, P3, P4, P5, P6, P7, P8, P9, P10, P11, P12, P13, P14, P15, P16> applicative(
    p1: CompletableFuture<P1>,
    p2: CompletableFuture<P2>,
    p3: CompletableFuture<P3>,
    p4: CompletableFuture<P4>,
    p5: CompletableFuture<P5>,
    p6: CompletableFuture<P6>,
    p7: CompletableFuture<P7>,
    p8: CompletableFuture<P8>,
    p9: CompletableFuture<P9>,
    p10: CompletableFuture<P10>,
    p11: CompletableFuture<P11>,
    p12: CompletableFuture<P12>,
    p13: CompletableFuture<P13>,
    p14: CompletableFuture<P14>,
    p15: CompletableFuture<P15>,
    p16: CompletableFuture<P16>,
): CompletableFuture<Applicative16<P1, P2, P3, P4, P5, P6, P7, P8, P9, P10, P11, P12, P13, P14, P15, P16>> {
    val ap = applicative(
        applicative(p1, p2, p3, p4, p5, p6, p7, p8),
        applicative(p9, p10, p11, p12, p13, p14, p15, p16)
    )
    return if (ap.isDone) {
        val app = ap.getNow(null)
        CompletableFuture.completedFuture(
            Applicative16(
                app.t1.t1,
                app.t1.t2,
                app.t1.t3,
                app.t1.t4,
                app.t1.t5,
                app.t1.t6,
                app.t1.t7,
                app.t1.t8,
                app.t2.t1,
                app.t2.t2,
                app.t2.t3,
                app.t2.t4,
                app.t2.t5,
                app.t2.t6,
                app.t2.t7,
                app.t2.t8
            )
        )
    } else {
        ap.thenApply { app ->
            Applicative16(
                app.t1.t1,
                app.t1.t2,
                app.t1.t3,
                app.t1.t4,
                app.t1.t5,
                app.t1.t6,
                app.t1.t7,
                app.t1.t8,
                app.t2.t1,
                app.t2.t2,
                app.t2.t3,
                app.t2.t4,
                app.t2.t5,
                app.t2.t6,
                app.t2.t7,
                app.t2.t8
            )
        }
    }
}