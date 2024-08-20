package cx.rain.mc.nbtedit.networking.packet;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

public interface IModPacket {
    ResourceLocation getId();

    void write(FriendlyByteBuf buf);
}
