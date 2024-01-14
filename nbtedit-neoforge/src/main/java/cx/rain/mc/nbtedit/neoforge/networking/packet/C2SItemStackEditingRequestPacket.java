package cx.rain.mc.nbtedit.neoforge.networking.packet;

import cx.rain.mc.nbtedit.NBTEdit;
import cx.rain.mc.nbtedit.neoforge.networking.NBTEditNetworkingImpl;
import cx.rain.mc.nbtedit.networking.NBTEditEditingHelper;
import cx.rain.mc.nbtedit.networking.NBTEditSavingHelper;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.handling.PlayPayloadContext;

public class C2SItemStackEditingRequestPacket implements CustomPacketPayload {
    private final ItemStack itemStack;

    public C2SItemStackEditingRequestPacket(ItemStack stack) {
        itemStack = stack;
    }

    public C2SItemStackEditingRequestPacket(FriendlyByteBuf buf) {
        itemStack = buf.readItem();
    }

    public static void handle(C2SItemStackEditingRequestPacket packet, PlayPayloadContext context) {
        context.workHandler().submitAsync(() -> {
            var optional = context.player();
            if (optional.isPresent()) {
                var player = optional.get();
                if (player instanceof ServerPlayer serverPlayer) {
                    NBTEditEditingHelper.editItemStack(serverPlayer, packet.itemStack);
                }
            }
        });
    }

    @Override
    public void write(FriendlyByteBuf buffer) {
        buffer.writeItem(itemStack);
    }

    @Override
    public ResourceLocation id() {
        return NBTEditNetworkingImpl.C2S_ITEM_STACK_EDITING_PACKET_ID;
    }
}
