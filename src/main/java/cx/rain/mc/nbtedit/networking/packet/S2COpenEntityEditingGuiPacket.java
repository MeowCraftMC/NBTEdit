package cx.rain.mc.nbtedit.networking.packet;

import cx.rain.mc.nbtedit.NBTEdit;
import cx.rain.mc.nbtedit.gui.screen.NBTEditScreen;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.UUID;
import java.util.function.Supplier;

public class S2COpenEntityEditingGuiPacket {
    protected UUID entityUuid;
    protected int entityId;
    protected CompoundTag compoundTag;
    protected boolean isSelf;

    public S2COpenEntityEditingGuiPacket(ByteBuf byteBuf) {
        var buf = new FriendlyByteBuf(byteBuf);
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

    public void toBytes(ByteBuf byteBuf) {
        var buf = new FriendlyByteBuf(byteBuf);
        buf.writeUUID(entityUuid);
        buf.writeInt(entityId);
        buf.writeNbt(compoundTag);
        buf.writeBoolean(isSelf);
    }

    public void clientHandleOnMain(Supplier<NetworkEvent.Context> context) {
        NBTEdit.getInstance().getLogger().info("Editing entity with UUID " + entityUuid + ".");
        Minecraft.getInstance().setScreen(new NBTEditScreen(entityUuid, entityId, compoundTag, isSelf));
    }
}
