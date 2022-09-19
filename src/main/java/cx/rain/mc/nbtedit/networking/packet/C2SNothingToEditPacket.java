package cx.rain.mc.nbtedit.networking.packet;

import cx.rain.mc.nbtedit.utility.Constants;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class C2SNothingToEditPacket {
    public C2SNothingToEditPacket() {
    }

    public C2SNothingToEditPacket(ByteBuf buf) {
    }

    public void toBytes(ByteBuf buf) {
    }

    public void serverHandleOnMain(Supplier<NetworkEvent.Context> context) {
        context.get().getSender().sendSystemMessage(Component.translatable(Constants.MESSAGE_NOTHING_TO_EDIT));
    }
}
