package io.github.hello09x.fakeplayer.v1_20_R1.network;

import io.netty.channel.ChannelDuplexHandler;
import net.minecraft.network.Connection;
import net.minecraft.network.PacketSendListener;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.PacketFlow;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;

import java.net.InetAddress;

public class FakeConnection extends Connection {
    public FakeConnection(@NotNull InetAddress address) {
        super(PacketFlow.SERVERBOUND);
        this.channel = new FakeChannel(null, address);
        this.address = this.channel.remoteAddress();
        if (Bukkit.getServer().getName().contains("Leaves")) {
            this.channel.pipeline().addLast("packet_handler", new ChannelDuplexHandler());
        }
    }

    @Override
    public boolean isConnected() {
        return true;
    }

    @Override
    public void send(Packet<?> packet, PacketSendListener listener) {
    }

    @Override
    public void send(Packet<?> packet) {
    }

    @Override
    public void handleDisconnection() {
        super.handleDisconnection();
    }
}