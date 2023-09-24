package cx.rain.mc.nbtedit.forge.networking.packet;

import cx.rain.mc.nbtedit.NBTEdit;
import cx.rain.mc.nbtedit.utility.Constants;
import io.netty.buffer.ByteBuf;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class C2SItemStackSavingPacket {
    protected ItemStack itemStack;

    protected CompoundTag compoundTag;

    public C2SItemStackSavingPacket(ByteBuf byteBuf) {
        var buf = new FriendlyByteBuf(byteBuf);
        itemStack = buf.readItem();
        compoundTag = buf.readNbt();
    }

    public C2SItemStackSavingPacket(ItemStack stack, CompoundTag tag) {
        itemStack = stack;
        compoundTag = tag;
    }

    public void toBytes(ByteBuf byteBuf) {
        var buf = new FriendlyByteBuf(byteBuf);
        buf.writeItem(itemStack);
        buf.writeNbt(compoundTag);
    }

    public void serverHandle(Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(() -> {
            var player = context.get().getSender();
            var server = player.getServer();

            server.execute(() -> {
                try {
                    var item = ItemStack.of(compoundTag);
                    player.setItemInHand(InteractionHand.MAIN_HAND, item);

                    NBTEdit.getInstance().getLogger().info("Player " + player.getName().getString() +
                            " successfully edited the tag of a ItemStack named "
                            + itemStack.getDisplayName().getString() + ".");
                    NBTEdit.getInstance().getLogger().debug(compoundTag.getAsString());

                    player.sendMessage(new TranslatableComponent(Constants.MESSAGE_SAVING_SUCCESSFUL)
                            .withStyle(ChatFormatting.GREEN), Util.NIL_UUID);
                } catch (Exception ex) {
                    player.sendMessage(new TranslatableComponent(Constants.MESSAGE_SAVING_FAILED_INVALID_NBT)
                            .withStyle(ChatFormatting.RED), Util.NIL_UUID);

                    NBTEdit.getInstance().getLogger().error("Player " + player.getName().getString() +
                            " edited the tag of ItemStack named "
                            + itemStack.getDisplayName().getString() +" and caused an exception!");

                    NBTEdit.getInstance().getLogger().error("NBT data: " + compoundTag.getAsString());
                    NBTEdit.getInstance().getLogger().error(new RuntimeException(ex).toString());
                }
            });
        });
    }
}
