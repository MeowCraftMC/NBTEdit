package cx.rain.mc.nbtedit.networking.packet.common;

import cx.rain.mc.nbtedit.networking.NetworkingConstants;
import cx.rain.mc.nbtedit.networking.packet.IModPacket;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

import java.util.UUID;

public record EntityEditingPacket(CompoundTag tag, boolean readOnly, UUID uuid, int id) implements IModPacket {
    @Override
    public ResourceLocation getId() {
        return NetworkingConstants.ENTITY_EDITING_ID;
    }

    @Override
    public void write(FriendlyByteBuf buf) {
        buf.writeNbt(tag);
        buf.writeBoolean(readOnly);
        buf.writeUUID(uuid);
        buf.writeVarInt(id);
    }

    public static EntityEditingPacket read(FriendlyByteBuf buf) {
        return new EntityEditingPacket(buf.readNbt(), buf.readBoolean(), buf.readUUID(), buf.readVarInt());
    }
}
