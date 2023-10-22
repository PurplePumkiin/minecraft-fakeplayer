package io.github.hello09x.fakeplayer.v1_20_R2.spi;


import io.github.hello09x.fakeplayer.api.action.ActionSetting;
import io.github.hello09x.fakeplayer.api.action.ActionType;
import io.github.hello09x.fakeplayer.api.spi.Action;
import io.github.hello09x.fakeplayer.api.spi.ActionTicker;
import io.github.hello09x.fakeplayer.v1_20_R2.action.*;
import org.bukkit.craftbukkit.v1_20_R2.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class ActionTickerImpl implements ActionTicker {

    /**
     * 行为类型
     */
    private final Action action;

    /**
     * 行为设置
     */
    private final ActionSetting setting;

    public ActionTickerImpl(@NotNull Player player, @NotNull ActionType action, @NotNull ActionSetting setting) {
        var handle = ((CraftPlayer) player).getHandle();
        this.action = switch (action) {
            case ATTACK -> new AttackAction(handle);
            case MINE -> new MineAction(handle);
            case USE -> new UseAction(handle);
            case JUMP -> new JumpAction(handle);
            case LOOK_AT_NEAREST_ENTITY -> new StareEntityAction(handle);
            case DROP_ITEM -> new DropItemAction(handle);
            case DROP_STACK -> new DropStackAction(handle);
            case DROP_INVENTORY -> new DropInventoryAction(handle);
        };
        this.setting = setting;
    }


    @Override
    public void tick() {
        if (setting.wait > 0) {
            setting.wait--;
            inactiveTick();
            return;
        }

        if (setting.remains == 0) {
            inactiveTick();
            return;
        }

        var effected = action.tick();
        if (effected) {
            if (setting.remains > 0) {
                setting.remains--;
            }
            setting.wait = setting.interval;
        }
    }

    @Override
    public void inactiveTick() {
        action.inactiveTick();
    }

    @Override
    public void stop() {
        action.stop();
    }

}
