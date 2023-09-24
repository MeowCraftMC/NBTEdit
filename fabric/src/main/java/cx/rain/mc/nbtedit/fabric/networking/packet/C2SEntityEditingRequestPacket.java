package cx.rain.mc.nbtedit.fabric.networking.packet;

import cx.rain.mc.nbtedit.NBTEdit;
import cx.rain.mc.nbtedit.utility.Constants;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;

import java.util.UUID;

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

	public C2SEntityEditingRequestPacket(FriendlyByteBuf buf) {
		entityUuid = buf.readUUID();
		entityId = buf.readInt();
		isSelf = buf.readBoolean();
	}

	public FriendlyByteBuf write() {
		var buf = PacketByteBufs.create();
		buf.writeUUID(entityUuid);
		buf.writeInt(entityId);
		buf.writeBoolean(isSelf);
		return buf;
	}

	public static void serverHandle(MinecraftServer server, ServerPlayer player,
									ServerGamePacketListenerImpl serverGamePacketListener,
									FriendlyByteBuf buf, PacketSender sender) {
		var packet = new C2SEntityEditingRequestPacket(buf);

		NBTEdit.getInstance().getLogger().info("Player " + player.getName().getString() +
				" requested entity with UUID " + packet.entityUuid + ".");
		var entity = player.getLevel().getEntity(packet.entityUuid);
		player.sendMessage(new TranslatableComponent(Constants.MESSAGE_EDITING_ENTITY, packet.entityUuid)
				.withStyle(ChatFormatting.GREEN), Util.NIL_UUID);
		NBTEdit.getInstance().getNetworking().serverOpenClientGui(player, entity);
	}
}
