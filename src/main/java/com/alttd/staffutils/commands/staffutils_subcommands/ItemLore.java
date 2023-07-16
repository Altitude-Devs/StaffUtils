package com.alttd.staffutils.commands.staffutils_subcommands;

import com.alttd.staffutils.commands.SubCommand;
import com.alttd.staffutils.config.Config;
import com.alttd.staffutils.config.Messages;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.JoinConfiguration;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ItemLore extends SubCommand {

    private final MiniMessage miniMessage = MiniMessage.miniMessage();

    @SuppressWarnings("DuplicatedCode")
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
            commandSender.sendMiniMessage(Messages.GENERIC.MUST_HOLD_ITEM, null);
            return true;
        }

        ItemMeta meta = itemStack.getItemMeta();

        if (meta == null) {
            player.sendMiniMessage(Messages.ITEM_LORE.CAN_NOT_HAVE_LORE, null);
            return true;
        }

        if (args[1].matches("\\d+")) { // Check if the first argument is a number
            int line = Integer.parseInt(args[1]);
            if (line > 9 || line < 0) {
                player.sendMiniMessage(Messages.ITEM_LORE.INVALID_LINE_NUMBER, null);
                return true;
            }

            Optional<ItemMeta> optionalItemMeta = replaceLineWithLore(line, args, meta, commandSender);
            if (optionalItemMeta.isEmpty())
                return true;
            meta = optionalItemMeta.get();
        } else {
            String loreText = String.join(" ", Arrays.copyOfRange(args, 1, args.length));
            List<String> lore = Arrays.asList(loreText.split("\\\\n"));
            meta.lore(lore.stream()
                    .map(miniMessage::deserialize)
                    .collect(Collectors.toList()));
        }

        List<Component> lore = meta.lore();
        if (lore == null) {
            commandSender.sendMiniMessage(Messages.ITEM_LORE.FAILED_SETTING_LORE, null);
            return true;
        }

        itemStack.setItemMeta(meta);
        player.sendMiniMessage(Messages.ITEM_LORE.DONE, Placeholder.component("lore", Component.join(JoinConfiguration.newlines(), lore)));
        return true;
    }

    private Optional<ItemMeta> replaceLineWithLore(int line, String[] args, ItemMeta meta, CommandSender commandSender) {
        String loreText = Arrays.stream(args, 2, args.length).collect(Collectors.joining(" "));
        List<Component> lore = meta.lore();

        if (lore == null) {
            lore = List.of(Component.empty());
        }

        if (line > lore.size()) {
            commandSender.sendMiniMessage(Messages.ITEM_LORE.INVALID_LINE_NUMBER, null);
            return Optional.empty();
        }

        if (lore.size() == line) {
            lore.add(miniMessage.deserialize(loreText));
        } else {
            lore.set(line, miniMessage.deserialize(loreText));
        }

        meta.lore(lore);
        return Optional.of(meta);
    }

    @Override
    public String getName() {
        return Config.COMMAND_NAME.ITEM_LORE;
    }

    @Override
    public List<String> getTabComplete(CommandSender commandSender, String[] args) {
        if (args.length == 2)
            return IntStream.rangeClosed(0, 9).mapToObj(String::valueOf).collect(Collectors.toList());
        return List.of();
    }

    @Override
    public String getHelpMessage() {
        return Messages.HELP.ITEM_LORE;
    }
}
