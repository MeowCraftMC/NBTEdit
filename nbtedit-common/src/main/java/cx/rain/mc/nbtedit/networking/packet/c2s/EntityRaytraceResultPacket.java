package cx.rain.mc.nbtedit.networking.packet.c2s;

import cx.rain.mc.nbtedit.networking.NetworkingConstants;
import cx.rain.mc.nbtedit.networking.packet.IModPacket;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

import java.util.UUID;

public record EntityRaytraceResultPacket(UUID uuid, int id) implements IModPacket {
    @Override
    public ResourceLocation getId() {
        return NetworkingConstants.ENTITY_RAYTRACE_RESULT_ID;
    }

    @Override
    public void write(FriendlyByteBuf buf) {
        buf.writeUUID(uuid);
        buf.writeVarInt(id);
    }

    public static EntityRaytraceResultPacket read(FriendlyByteBuf buf) {
        return new EntityRaytraceResultPacket(buf.readUUID(), buf.readVarInt());
    }
}
