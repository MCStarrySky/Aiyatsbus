package com.mcstarrysky.aiyatsbus.module.custom.expansion;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.functions.Function2;
import org.jetbrains.annotations.NotNull;
import taboolib.common.platform.event.EventPriority;
import taboolib.common.platform.event.ProxyListener;
import taboolib.common.platform.function.ExecutorKt;
import taboolib.common.platform.function.ListenerKt;
import taboolib.common.platform.service.PlatformExecutor;

import java.io.Closeable;
import java.util.ArrayList;
import java.util.List;

/**
 * Aiyatsbus
 * com.mcstarrysky.aiyatsbus.module.custom.expansion.AiyatsbusExpansion
 *
 * @author mical
 * @since 2024/2/26 23:09
 */
public abstract class AiyatsbusExpansion {

    protected final List<ProxyListener> listeners = new ArrayList<>();
    protected final List<PlatformExecutor.PlatformTask> executors = new ArrayList<>();

    /**
     * 初始化扩展实例
     */
    public AiyatsbusExpansion() {
        // 执行 启动时 代码
        onStarting();
    }

    /**
     * 获取拓展名称
     * @return 拓展名称
     */
    @NotNull
    public abstract String getIdentifier();

    /**
     * 获取拓展作者
     * @return 拓展作者
     */
    @NotNull
    public abstract String getAuthor();

    /**
     * 获取拓展版本号
     * @return 拓展版本号
     */

    @NotNull
    public abstract String getVersion();

    /**
     * 实例被初始化时逻辑代码
     */
    public void onStarting() {
    }

    /**
     * 扩展被卸载时运行代码
     */
    public void onStopping() {
        try {
            listeners.forEach(ListenerKt::unregisterListener);
            executors.forEach(PlatformExecutor.PlatformTask::cancel);
        } catch (Throwable ignored) {
            // FIXME
        }

        listeners.clear();
        executors.clear();
    }

    public <T> void registerListener(final Class<T> event, final EventPriority priority, final boolean ignoreCancelled, final Function2<Closeable, T, Object> func) {
        final ProxyListener listener = ListenerKt.registerBukkitListener(event, priority, ignoreCancelled, (closeable, t) -> {
            func.invoke(closeable, t);
            return Unit.INSTANCE;
        });
        listeners.add(listener);
    }

    public void registerTask(final boolean now, final boolean async, final long delay, final long period, final Function1<? super PlatformExecutor.PlatformTask, Object> executor) {
        final PlatformExecutor.PlatformTask t = ExecutorKt.submit(now, async, delay, period, (task) -> {
            executor.invoke(task);
            return Unit.INSTANCE;
        });
        executors.add(t);
    }
}
