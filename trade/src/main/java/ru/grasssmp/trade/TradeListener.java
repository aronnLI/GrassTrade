package ru.grasssmp.trade;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.ItemStack;

public class TradeListener implements Listener {
    private final GrassTrade plugin;

    public TradeListener(GrassTrade plugin) {
        this.plugin = plugin;
    }

    // Проверка, принадлежит ли слот первому игроку (левая часть)
    private boolean isSlotForP1(int slot) {
        return (slot <= 3) || (slot >= 9 && slot <= 12) || (slot >= 18 && slot <= 21) || (slot >= 27 && slot <= 30);
    }

    // Проверка, принадлежит ли слот второму игроку (правая часть)
    private boolean isSlotForP2(int slot) {
        return (slot >= 5 && slot <= 8) || (slot >= 14 && slot <= 17) || (slot >= 23 && slot <= 26) || (slot >= 32 && slot <= 35);
    }

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player player)) return;

        TradeSession session = plugin.getTradeManager().getSession(player);
        if (session == null || !event.getInventory().equals(session.getInventory())) return;

        int slot = event.getRawSlot();

        // Если кликнули по кнопке "Готов" (слот 49) или "Отмена" (слот 45)
        if (slot == 45) { // Отмена
            plugin.getTradeManager().endTrade(session);
            event.getInventory().close();
            player.sendMessage("§cТрейд отменен.");
            return;
        } else if (slot == 49) { // Готов
            // Блокируем клик, если игрок уже нажал готовность
            if (session.isReady(player)) return;

            session.setReady(player, true);
            player.sendMessage("§aВы готовы к обмену.");

            if (session.isBothReady()) {
                session.completeTrade();
                player.sendMessage("§aТрейд успешно завершен!");
                event.getInventory().close();
            }
            return;
        }

        // Если кликнули в нижнюю часть своего инвентаря (перенос вещей)
        if (slot >= 54) return;

        // Если это слот партнёра - отменяем клик
        if (player.equals(session.getPlayer1()) && isSlotForP2(slot)) {
            event.setCancelled(true);
        } else if (player.equals(session.getPlayer2()) && isSlotForP1(slot)) {
            event.setCancelled(true);
        }

        // Если один из игроков уже готов, запрещаем менять предметы
        if (session.isReady(player) || session.isReady(player.equals(session.getPlayer1()) ? session.getPlayer2() : session.getPlayer1())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onDrag(InventoryDragEvent event) {
        if (!(event.getWhoClicked() instanceof Player player)) return;
        TradeSession session = plugin.getTradeManager().getSession(player);
        if (session == null || !event.getInventory().equals(session.getInventory())) return;

        // Запрещаем перетаскивание мышкой, чтобы не сломать логику
        event.setCancelled(true);
    }

    @EventHandler
    public void onClose(InventoryCloseEvent event) {
        if (!(event.getPlayer() instanceof Player player)) return;
        TradeSession session = plugin.getTradeManager().getSession(player);

        if (session != null && event.getInventory().equals(session.getInventory())) {
            // Если трейд не был завершен (игрок просто закрыл меню)
            if (!session.isBothReady()) {
                plugin.getTradeManager().endTrade(session);
                player.sendMessage("§cТрейд отменен.");

                Player partner = player.equals(session.getPlayer1()) ? session.getPlayer2() : session.getPlayer1();
                if (partner.isOnline()) {
                    partner.closeInventory();
                    partner.sendMessage("§cТрейд отменен вторым игроком.");
                }
            }
        }
    }
}