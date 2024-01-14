package cx.rain.mc.nbtedit.neoforge.networking.packet;

import cx.rain.mc.nbtedit.NBTEdit;
import cx.rain.mc.nbtedit.neoforge.networking.NBTEditNetworkingImpl;
import cx.rain.mc.nbtedit.networking.NBTEditEditingHelper;
import cx.rain.mc.nbtedit.networking.NBTEditSavingHelper;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.handling.PlayPayloadContext;
import org.jetbrains.annotations.NotNull;

public class C2SItemStackSavingPacket implements CustomPacketPayload {
    protected ItemStack itemStack;

    protected CompoundTag compoundTag;

    public C2SItemStackSavingPacket(FriendlyByteBuf buf) {
        itemStack = buf.readItem();
        compoundTag = buf.readNbt();
    }

    public C2SItemStackSavingPacket(ItemStack stack, CompoundTag tag) {
        itemStack = stack;
        compoundTag = tag;
    }

    public static void handle(C2SItemStackSavingPacket packet, PlayPayloadContext context) {
        context.workHandler().submitAsync(() -> {
            var optional = context.player();
            if (optional.isPresent()) {
                var player = optional.get();
                if (player instanceof ServerPlayer serverPlayer) {
                    NBTEditSavingHelper.saveItemStack(serverPlayer, packet.itemStack, packet.compoundTag);
                }
            }
        });
    }

    @Override
    public void write(FriendlyByteBuf buffer) {
        buffer.writeItem(itemStack);
        buffer.writeNbt(compoundTag);
    }

    @Override
    public @NotNull ResourceLocation id() {
        return NBTEditNetworkingImpl.C2S_ITEM_STACK_SAVING_PACKET_ID;
    }
}
