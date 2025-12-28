package net.saturn.betterCombatLogger;

import net.saturn.betterCombatLogger.commands.CombatCommand;
import net.saturn.betterCombatLogger.listeners.CombatListener;
import net.saturn.betterCombatLogger.listeners.RegionListener;
import net.saturn.betterCombatLogger.managers.CombatManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class BetterCombatLogger extends JavaPlugin {

    private CombatManager combatManager;

    @Override
    public void onEnable() {
        // Check for WorldGuard
        if (getServer().getPluginManager().getPlugin("WorldGuard") == null) {
            getLogger().severe("WorldGuard not found! Disabling plugin...");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        // Save default config
        saveDefaultConfig();

        // Initialize managers
        combatManager = new CombatManager(this);

        // Register listeners
        getServer().getPluginManager().registerEvents(new CombatListener(this, combatManager), this);
        getServer().getPluginManager().registerEvents(new RegionListener(this, combatManager), this);

        // Register commands
        getCommand("combat").setExecutor(new CombatCommand(combatManager));

        getLogger().info("BetterCombatLogger has been enabled!");
    }

    @Override
    public void onDisable() {
        // Cancel all combat timers
        if (combatManager != null) {
            combatManager.shutdown();
        }
        getLogger().info("BetterCombatLogger has been disabled!");
    }

    public CombatManager getCombatManager() {
        return combatManager;
    }
}