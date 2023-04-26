package cx.rain.mc.nbtedit.fabric.networking.packet;

import cx.rain.mc.nbtedit.NBTEdit;
import cx.rain.mc.nbtedit.fabric.networking.NBTEditNetworkingImpl;
import cx.rain.mc.nbtedit.utility.Constants;
import net.fabricmc.fabric.api.networking.v1.FabricPacket;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.PacketType;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;

public class C2SItemStackSavingPacket implements FabricPacket {
    public static final PacketType<C2SItemStackSavingPacket> PACKET_TYPE = PacketType.create(NBTEditNetworkingImpl.C2S_ITEM_STACK_SAVING_PACKET_ID, C2SItemStackSavingPacket::new);

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

    @Override
    public void write(FriendlyByteBuf buf) {
        buf.writeItem(itemStack);
        buf.writeNbt(compoundTag);
    }

    @Override
    public PacketType<?> getType() {
        return PACKET_TYPE;
    }

    public static void serverHandle(C2SItemStackSavingPacket packet,
                                   ServerPlayer player, PacketSender responseSender) {
        var server = player.getServer();

        server.execute(() -> {
            try {
                var item = ItemStack.of(packet.compoundTag);
                player.setItemInHand(InteractionHand.MAIN_HAND, item);

                NBTEdit.getInstance().getLogger().info("Player " + player.getName().getString() +
                        " successfully edited the tag of a ItemStack named "
                        + packet.itemStack.getDisplayName().getString() + ".");
                NBTEdit.getInstance().getLogger().debug(packet.compoundTag.getAsString());

                player.sendSystemMessage(Component.translatable(Constants.MESSAGE_SAVING_SUCCESSFUL)
                        .withStyle(ChatFormatting.GREEN));
            } catch (Exception ex) {
                player.sendSystemMessage(Component.translatable(Constants.MESSAGE_SAVING_FAILED_INVALID_NBT)
                        .withStyle(ChatFormatting.RED));

                NBTEdit.getInstance().getLogger().error("Player " + player.getName().getString() +
                        " edited the tag of ItemStack named "
                        + packet.itemStack.getDisplayName().getString() +" and caused an exception!");

                NBTEdit.getInstance().getLogger().error("NBT data: " + packet.compoundTag.getAsString());
                NBTEdit.getInstance().getLogger().error(new RuntimeException(ex).toString());
            }
        });
    }
}
