package cx.rain.mc.nbtedit.neoforge.networking.packet;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

public abstract class AbstractCompoundTagPacket implements CustomPacketPayload {
    private final CompoundTag tag;
    private final boolean readOnly;

    public AbstractCompoundTagPacket(CompoundTag tag, boolean readOnly) {
        this.tag = tag;
        this.readOnly = readOnly;
    }

    public AbstractCompoundTagPacket(FriendlyByteBuf buf) {
        this.tag = buf.readNbt();
        this.readOnly = buf.readBoolean();
    }

    @Override
    public void write(FriendlyByteBuf buf) {
        buf.writeNbt(this.tag);
        buf.writeBoolean(this.readOnly);
    }

    public CompoundTag getTag() {
        return tag;
    }

    public boolean isReadOnly() {
        return readOnly;
    }
}
