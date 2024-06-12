package cx.rain.mc.nbtedit.api.netowrking;

import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;

public interface IModNetworking {
    void sendTo(ServerPlayer player, CustomPacketPayload packet);
    void sendToServer(CustomPacketPayload packet);
}
