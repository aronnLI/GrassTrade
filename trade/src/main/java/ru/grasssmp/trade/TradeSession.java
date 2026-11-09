package ru.grasssmp.trade;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class TradeSession {
    private final Player p1;
    private final Player p2;
    private final Inventory inventory;

    private boolean p1Ready = false;
    private boolean p2Ready = false;

    public TradeSession(Player p1, Player p2) {
        this.p1 = p1;
        this.p2 = p2;
        // Создаем меню на 54 слота
        this.inventory = Bukkit.createInventory(null, 54, "Обмен: " + p1.getName() + " ↔ " + p2.getName());
    }

    public Player getPlayer1() { return p1; }
    public Player getPlayer2() { return p2; }
    public Inventory getInventory() { return inventory; }

    public boolean isReady(Player p) {
        return p.equals(p1) ? p1Ready : p2Ready;
    }

    public void setReady(Player p, boolean ready) {
        if (p.equals(p1)) p1Ready = ready;
        else if (p.equals(p2)) p2Ready = ready;
    }

    public boolean isBothReady() {
        return p1Ready && p2Ready;
    }

    // Логика возврата вещей (если кто-то закрыл меню)
    public void returnItems() {
        for (int i = 0; i <= 3; i++) {
            if (inventory.getItem(i) != null) p1.getInventory().addItem(inventory.getItem(i));
            if (inventory.getItem(i + 5) != null) p2.getInventory().addItem(inventory.getItem(i + 5));
        }
        for (int i = 9; i <= 12; i++) {
            if (inventory.getItem(i) != null) p1.getInventory().addItem(inventory.getItem(i));
            if (inventory.getItem(i + 5) != null) p2.getInventory().addItem(inventory.getItem(i + 5));
        }
        for (int i = 18; i <= 21; i++) {
            if (inventory.getItem(i) != null) p1.getInventory().addItem(inventory.getItem(i));
            if (inventory.getItem(i + 5) != null) p2.getInventory().addItem(inventory.getItem(i + 5));
        }
        for (int i = 27; i <= 30; i++) {
            if (inventory.getItem(i) != null) p1.getInventory().addItem(inventory.getItem(i));
            if (inventory.getItem(i + 5) != null) p2.getInventory().addItem(inventory.getItem(i + 5));
        }
        inventory.clear();
    }

    // Логика успешного трейда
    public void completeTrade() {
        for (int i = 0; i <= 3; i++) {
            if (inventory.getItem(i) != null) p2.getInventory().addItem(inventory.getItem(i));
            if (inventory.getItem(i + 5) != null) p1.getInventory().addItem(inventory.getItem(i + 5));
        }
        for (int i = 9; i <= 12; i++) {
            if (inventory.getItem(i) != null) p2.getInventory().addItem(inventory.getItem(i));
            if (inventory.getItem(i + 5) != null) p1.getInventory().addItem(inventory.getItem(i + 5));
        }
        for (int i = 18; i <= 21; i++) {
            if (inventory.getItem(i) != null) p2.getInventory().addItem(inventory.getItem(i));
            if (inventory.getItem(i + 5) != null) p1.getInventory().addItem(inventory.getItem(i + 5));
        }
        for (int i = 27; i <= 30; i++) {
            if (inventory.getItem(i) != null) p2.getInventory().addItem(inventory.getItem(i));
            if (inventory.getItem(i + 5) != null) p1.getInventory().addItem(inventory.getItem(i + 5));
        }
        inventory.clear();
    }
}