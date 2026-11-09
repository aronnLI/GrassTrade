package ru.grasssmp.trade;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TradeCommand implements CommandExecutor {
    private final GrassTrade plugin;

    public TradeCommand(GrassTrade plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player p1)) {
            sender.sendMessage("Команда только для игроков.");
            return true;
        }

        if (args.length != 1) {
            p1.sendMessage("§cИспользование: /trade <ник>");
            return true;
        }

        Player p2 = Bukkit.getPlayer(args[0]);
        if (p2 == null || !p2.isOnline()) {
            p1.sendMessage("§cИгрок не найден.");
            return true;
        }

        if (p1.equals(p2)) {
            p1.sendMessage("§cНельзя торговать с самим собой.");
            return true;
        }

        if (plugin.getTradeManager().isTrading(p1) || plugin.getTradeManager().isTrading(p2)) {
            p1.sendMessage("§cВы или этот игрок уже участвуете в трейде.");
            return true;
        }

        TradeSession session = new TradeSession(p1, p2);
        plugin.getTradeManager().startTrade(p1, p2, session);

        p1.openInventory(session.getInventory());
        p2.openInventory(session.getInventory());

        p1.sendMessage("§aТрейд с " + p2.getName() + " начат. Положите предметы в левую часть.");
        p2.sendMessage("§aТрейд с " + p1.getName() + " начат. Положите предметы в правую часть.");
        return true;
    }
}