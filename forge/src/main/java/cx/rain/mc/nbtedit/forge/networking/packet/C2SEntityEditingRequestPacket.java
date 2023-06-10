package cx.rain.mc.nbtedit.forge.networking.packet;

import cx.rain.mc.nbtedit.NBTEdit;
import cx.rain.mc.nbtedit.utility.Constants;
import io.netty.buffer.ByteBuf;
import net.minecraft.ChatFormatting;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraftforge.network.NetworkEvent;

import java.util.UUID;
import java.util.function.Supplier;

public class C2SEntityEditingRequestPacket {
	/**
	 * The UUID of the entity being requested.
	 */
	private UUID entityUuid;

	private int entityId;

	private boolean isSelf;

	public C2SEntityEditingRequestPacket(UUID uuid, int id, boolean self) {
		entityUuid = uuid;
		entityId = id;
		isSelf = self;
	}

	public C2SEntityEditingRequestPacket(ByteBuf byteBuf) {
		var buf = new FriendlyByteBuf(byteBuf);
		entityUuid = buf.readUUID();
		entityId = buf.readInt();
		isSelf = buf.readBoolean();
	}

	public void toBytes(ByteBuf byteBuf) {
		var buf = new FriendlyByteBuf(byteBuf);
		buf.writeUUID(entityUuid);
		buf.writeInt(entityId);
		buf.writeBoolean(isSelf);
	}

	public void serverHandleOnMain(Supplier<NetworkEvent.Context> context) {
        var player = context.get().getSender();
        NBTEdit.getInstance().getLogger().info("Player " + player.getName().getString() +
                " requested entity with UUID " + entityUuid + ".");
		var entity = player.serverLevel().getEntity(entityUuid);
		player.sendSystemMessage(Component.translatable(Constants.MESSAGE_EDITING_ENTITY, entityUuid)
				.withStyle(ChatFormatting.GREEN));
        NBTEdit.getInstance().getNetworking().serverOpenClientGui(player, entity);
	}
}
