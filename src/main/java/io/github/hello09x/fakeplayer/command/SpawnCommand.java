package io.github.hello09x.fakeplayer.command;

import dev.jorel.commandapi.exceptions.WrapperCommandSyntaxException;
import dev.jorel.commandapi.executors.CommandArguments;
import io.github.hello09x.fakeplayer.util.MathUtils;
import io.github.tanyaofei.plugin.toolkit.database.Page;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.joml.Math;

import java.util.Collections;
import java.util.List;
import java.util.StringJoiner;

import static net.kyori.adventure.text.Component.*;
import static net.kyori.adventure.text.event.ClickEvent.runCommand;
import static net.kyori.adventure.text.format.NamedTextColor.*;

public class SpawnCommand extends AbstractCommand {

    public final static SpawnCommand instance = new SpawnCommand();

    private static String toLocationString(@NotNull Location location) {
        return location.getWorld().getName()
                + ": "
                + StringUtils.joinWith(", ",
                MathUtils.round(location.getX(), 0.5),
                MathUtils.round(location.getY(), 0.5),
                MathUtils.round(location.getZ(), 0.5));
    }

    public void spawn(@NotNull CommandSender sender, @NotNull CommandArguments args) {
        var world = (World) args.get("world");
        var location = (Location) args.get("location");
        if (world == null || location == null) {
            if (sender instanceof Player p) {
                location = p.getLocation();
            } else {
                location = Bukkit.getServer().getWorlds().get(0).getSpawnLocation();
            }
        } else {
            location = location.clone();
            location.setWorld(world);
        }

        var fakePlayer = fakeplayerManager.spawn(sender, location);
        if (fakePlayer != null) {
            sender.sendMessage(textOfChildren(
                    text("你创建了假人 ", GRAY),
                    text(fakePlayer.getName()),
                    text(", 位于 ", GRAY),
                    text(toLocationString(fakePlayer.getLocation()))
            ));
        }
    }

    public void kill(@NotNull CommandSender sender, @NotNull CommandArguments args) {
        @SuppressWarnings("unchecked")
        var targets = (List<Player>) args.get("targets");
        if (targets == null) {
            var reserved = fakeplayerManager.getAll(sender);
            if (reserved.size() == 1) {
                targets = Collections.singletonList(reserved.get(0));
            } else {
                targets = Collections.emptyList();
            }
        }

        if (targets.isEmpty()) {
            sender.sendMessage(text("没有移除任何假人", GRAY));
            return;
        }

        var names = new StringJoiner(", ");
        for (var target : targets) {
            if (fakeplayerManager.remove(target.getName())) {
                names.add(target.getName());
            }
        }
        sender.sendMessage(textOfChildren(
                text("你移除了假人: ", GRAY),
                text(names.toString())
        ));
    }

    public void list(@NotNull CommandSender sender, @NotNull CommandArguments args) {
        var page = (int) args.getOptional("page").orElse(1);
        var size = (int) args.getOptional("size").orElse(10);

        var fakers = sender.isOp()
                ? fakeplayerManager.getAll()
                : fakeplayerManager.getAll(sender);

        var total = fakers.size();
        var pages = total == 0 ? 1 : (int) Math.ceil((double) total / size);
        var p = new Page<>(
                fakers.subList((page - 1) * size, Math.min(total, page * size)),
                total,
                pages,
                page,
                size
        );

        var canTp = sender instanceof Player && sender.hasPermission(Permission.tp);
        sender.sendMessage(p.toComponent(
                "假人",
                fakeplayer -> textOfChildren(
                        text(fakeplayer.getName() + " (" + fakeplayerManager.getCreator(fakeplayer) + ")", GOLD),
                        text(" - ", GRAY),
                        text(toLocationString(fakeplayer.getLocation()), WHITE),
                        canTp ? text(" [<--传送]", AQUA).clickEvent(runCommand("/fp tp " + fakeplayer.getName())) : empty(),
                        text(" [<--移除]", RED).clickEvent(runCommand("/fp kill " + fakeplayer.getName()))
                ),
                String.format("/fp list %d %d", page - 1, size),
                String.format("/fp list %d %d", page + 1, size)
        ));
    }

    public void distance(
            @NotNull Player sender,
            @NotNull CommandArguments args
    ) throws WrapperCommandSyntaxException {
        var target = getTarget(sender, args);
        var from = target.getLocation().toBlockLocation();
        var to = sender.getLocation().toBlockLocation();

        if (!from.getWorld().equals(to.getWorld())) {
            sender.sendMessage(textOfChildren(
                    text("你离 ", GRAY),
                    text(target.getName()),
                    text(" 十分遥远", GRAY)
            ));
            return;
        }

        var euclidean = MathUtils.round(from.distance(to), 0.5);
        var x = Math.abs(from.getBlockX() - to.getBlockX());
        var y = Math.abs(from.getBlockY() - to.getBlockY());
        var z = Math.abs(from.getBlockZ() - to.getBlockZ());

        sender.sendMessage(textOfChildren(
                text("你与 ", GRAY),
                text(target.getName(), WHITE),
                text("的距离: ", GRAY),
                newline(),
                text("- 欧氏距离: ", GRAY), text(euclidean, WHITE),
                newline(),
                text("- x 距离: ", GRAY), text(x, WHITE),
                newline(),
                text("- y 距离: ", GRAY), text(y, WHITE),
                newline(),
                text("- z 距离: ", GRAY), text(z, WHITE)
        ));
    }

}
