package cx.rain.mc.nbtedit.api.netowrking;

import cx.rain.mc.nbtedit.networking.packet.IModPacket;
import net.minecraft.server.level.ServerPlayer;

public interface IModNetworking {
    void sendTo(ServerPlayer player, IModPacket packet);
    void sendToServer(IModPacket packet);
}
