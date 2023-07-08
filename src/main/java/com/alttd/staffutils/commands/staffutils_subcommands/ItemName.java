package com.alttd.staffutils.commands.staffutils_subcommands;

import com.alttd.staffutils.commands.SubCommand;
import com.alttd.staffutils.config.Messages;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.List;

public class ItemName extends SubCommand {

    private final MiniMessage miniMessage = MiniMessage.miniMessage();

    @Override
    public boolean onCommand(CommandSender commandSender, String[] args) {
        if (args.length < 2)
            return false;

        if (!(commandSender instanceof Player player)) {
            commandSender.sendMiniMessage(Messages.GENERIC.PLAYER_ONLY, null);
            return true;
        }

        ItemStack itemStack = player.getInventory().getItemInMainHand();

        if (itemStack.getType().equals(Material.AIR)) {
            player.sendMiniMessage(Messages.GENERIC.MUST_HOLD_ITEM, null);
            return false;
        }

        ItemMeta itemMeta = itemStack.getItemMeta();

        String name;
        if (args.length == 2)
            name = args[1];
        else
            name = String.join(" ", Arrays.copyOfRange(args, 1, args.length));

        itemMeta.displayName(miniMessage.deserialize(name));
        return true;
    }

    @Override
    public String getName() {
        return "itemname";
    }

    @Override
    public List<String> getTabComplete(CommandSender commandSender, String[] args) {
        return List.of();
    }

    @Override
    public String getHelpMessage() {
        return Messages.HELP.ITEM_NAME;
    }
}
