package ru.grasssmp.trade;

import org.bukkit.plugin.java.JavaPlugin;

public class GrassTrade extends JavaPlugin {
    private static GrassTrade instance;
    private TradeManager tradeManager;

    @Override
    public void onEnable() {
        instance = this;
        this.tradeManager = new TradeManager();

        getCommand("trade").setExecutor(new TradeCommand(this));
        getServer().getPluginManager().registerEvents(new TradeListener(this), this);

        getLogger().info("GrassTrade успешно включен!");
    }

    @Override
    public void onDisable() {
        tradeManager.closeAllTrades();
    }

    public static GrassTrade getInstance() {
        return instance;
    }

    public TradeManager getTradeManager() {
        return tradeManager;
    }
}