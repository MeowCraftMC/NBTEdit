package cx.rain.mc.nbtedit.networking.packet.s2c;

import cx.rain.mc.nbtedit.networking.NetworkingConstants;
import cx.rain.mc.nbtedit.networking.packet.IModPacket;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

public record RaytracePacket() implements IModPacket {
    @Override
    public ResourceLocation getId() {
        return NetworkingConstants.RAYTRACE_REQUEST_ID;
    }

    @Override
    public void write(FriendlyByteBuf buf) {
    }

    public static RaytracePacket read(FriendlyByteBuf buf) {
        return new RaytracePacket();
    }
}
