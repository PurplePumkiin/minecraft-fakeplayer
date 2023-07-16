package io.github.hello09x.fakeplayer.command;

import io.github.hello09x.fakeplayer.command.admin.ReloadCommand;
import io.github.hello09x.fakeplayer.command.admin.RemoveAllCommand;
import io.github.hello09x.fakeplayer.command.player.CreateCommand;
import io.github.hello09x.fakeplayer.command.player.RemoveCommand;
import io.github.hello09x.fakeplayer.command.player.TpCommand;
import io.github.hello09x.fakeplayer.command.player.TpHereCommand;
import io.github.tanyaofei.plugin.toolkit.command.ParentCommand;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class FakePlayerCommand extends ParentCommand {

    public final static FakePlayerCommand instance = new FakePlayerCommand(
            "假人相关命令",
            "fakeplayer"
    );

    static {
        instance.register("create", CreateCommand.instance);
        instance.register("remove", RemoveCommand.instance);
        instance.register("tp", TpCommand.instance);
        instance.register("tphere", TpHereCommand.instance);
        instance.register("removeall", RemoveAllCommand.instance);
        instance.register("reload", ReloadCommand.instance);
    }

    protected FakePlayerCommand(@NotNull String description, @Nullable String permission) {
        super(description, permission);
    }

}
