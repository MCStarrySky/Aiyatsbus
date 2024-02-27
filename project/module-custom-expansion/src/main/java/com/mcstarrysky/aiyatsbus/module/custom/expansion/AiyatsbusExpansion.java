package com.mcstarrysky.aiyatsbus.module.custom.expansion;

import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

/**
 * Aiyatsbus
 * com.mcstarrysky.aiyatsbus.module.custom.expansion.AiyatsbusExpansion
 *
 * @author mical
 * @since 2024/2/26 23:09
 */
public abstract class AiyatsbusExpansion {

    private final JavaPlugin plugin;


    public AiyatsbusExpansion(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public JavaPlugin getPlugin() {
        return plugin;
    }

    @NotNull
    public abstract String getIdentifier();

    @NotNull
    public abstract String getAuthor();

    @NotNull
    public abstract String getVersion();
}
