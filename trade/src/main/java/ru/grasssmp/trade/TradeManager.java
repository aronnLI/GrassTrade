package ru.grasssmp.trade;

import org.bukkit.entity.Player;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class TradeManager {
    // Храним сессии: UUID игрока -> Сессия
    private final Map<UUID, TradeSession> activeSessions = new HashMap<>();

    public boolean isTrading(Player player) {
        return activeSessions.containsKey(player.getUniqueId());
    }

    public TradeSession getSession(Player player) {
        return activeSessions.get(player.getUniqueId());
    }

    public void startTrade(Player p1, Player p2, TradeSession session) {
        activeSessions.put(p1.getUniqueId(), session);
        activeSessions.put(p2.getUniqueId(), session);
    }

    public void endTrade(TradeSession session) {
        activeSessions.remove(session.getPlayer1().getUniqueId());
        activeSessions.remove(session.getPlayer2().getUniqueId());
        session.returnItems(); // Возврат предметов, если трейд не завершен
    }

    public void closeAllTrades() {
        for (TradeSession session : activeSessions.values()) {
            session.getPlayer1().closeInventory();
            session.getPlayer2().closeInventory();
            session.returnItems();
        }
        activeSessions.clear();
    }
}