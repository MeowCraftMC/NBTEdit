package cx.rain.mc.nbtedit.fabric.networking.packet;

import cx.rain.mc.nbtedit.utility.ScreenHelper;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;

import java.util.UUID;

public class S2COpenEntityEditingGuiPacket {

    protected UUID entityUuid;
    protected int entityId;
    protected CompoundTag compoundTag;
    protected boolean isSelf;

    public S2COpenEntityEditingGuiPacket(FriendlyByteBuf buf) {
        entityUuid = buf.readUUID();
        entityId = buf.readInt();
        compoundTag = buf.readNbt();
        isSelf = buf.readBoolean();
    }

    public S2COpenEntityEditingGuiPacket(UUID uuid, int id, CompoundTag tag, boolean self) {
        entityUuid = uuid;
        entityId = id;
        compoundTag = tag;
        isSelf = self;
    }

    public void write(FriendlyByteBuf buf) {
        buf.writeUUID(entityUuid);
        buf.writeInt(entityId);
        buf.writeNbt(compoundTag);
        buf.writeBoolean(isSelf);
    }

    public static void clientHandle(Minecraft client, ClientPacketListener handler,
                                   FriendlyByteBuf buf, PacketSender responseSender) {
        var entityUuid = buf.readUUID();
        var entityId = buf.readInt();
        var compoundTag = buf.readNbt();
        var isSelf = buf.readBoolean();

        client.execute(() -> ScreenHelper.showNBTEditScreen(entityUuid, entityId, compoundTag, isSelf));
    }
}
