package cx.rain.mc.nbtedit.fabric.networking.packet;

import cx.rain.mc.nbtedit.NBTEdit;
import cx.rain.mc.nbtedit.utility.Constants;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundSetExperiencePacket;
import net.minecraft.network.protocol.game.ClientboundSetHealthPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.world.level.GameType;

import java.util.UUID;

public class C2SEntitySavingPacket {
	/**
	 * The id of the entity being edited.
	 */
	protected UUID entityUuid;
	/**
	 * The nbt data of the entity.
	 */
	protected CompoundTag compoundTag;

	protected int entityId;
	protected boolean isSelf;

	public C2SEntitySavingPacket(UUID uuid, int id, CompoundTag tag, boolean self) {
		entityUuid = uuid;
		compoundTag = tag;
		entityId = id;
		isSelf = self;
	}

	public C2SEntitySavingPacket(FriendlyByteBuf buf) {
		entityUuid = buf.readUUID();
		entityId = buf.readInt();
		compoundTag = buf.readNbt();
		isSelf = buf.readBoolean();
	}

	public FriendlyByteBuf write() {
		var buf = PacketByteBufs.create();
		buf.writeUUID(entityUuid);
		buf.writeInt(entityId);
		buf.writeNbt(compoundTag);
		buf.writeBoolean(isSelf);
		return buf;
	}

	public static void serverHandle(MinecraftServer server, ServerPlayer player,
									ServerGamePacketListenerImpl serverGamePacketListener,
									FriendlyByteBuf buf, PacketSender sender) {
		var packet = new C2SEntitySavingPacket(buf);

		var level = player.getLevel();
		server.execute(() -> {
			var entity = level.getEntity(packet.entityUuid);
			if (!NBTEdit.getInstance().getPermission().hasPermission(player)) {
				player.sendSystemMessage(Component.translatable(Constants.MESSAGE_NO_PERMISSION)
						.withStyle(ChatFormatting.RED));
			}

			if (entity != null) {
				try {
					GameType prevGameMode = null;
					if (entity instanceof ServerPlayer) {
						prevGameMode = ((ServerPlayer) entity).gameMode.getGameModeForPlayer();
					}
					entity.load(packet.compoundTag);
					NBTEdit.getInstance().getLogger().info("Player " + player.getName().getString() +
							" edited the tag of Entity with UUID " + packet.entityUuid + " .");
					NBTEdit.getInstance().getLogger().debug("New NBT of entity " + packet.entityUuid +
							" is " + packet.compoundTag.getAsString());

					if (entity instanceof ServerPlayer) {
						// Todo: qyl27: this is a very legacy todo.
						// qyl27: if anyone found bugs with it, please open an issue.
						// Update player info
						// This is fairly hacky.
						// Consider swapping to an event driven system, where classes can register to
						// receive entity edit events and provide feedback/send packets as necessary.
						var targetPlayer = (ServerPlayer) entity;
						targetPlayer.initMenu(targetPlayer.inventoryMenu);
						var gameMode = targetPlayer.gameMode.getGameModeForPlayer();
						if (prevGameMode != gameMode) {
							targetPlayer.setGameMode(gameMode);
						}
						targetPlayer.connection.send(new ClientboundSetHealthPacket(targetPlayer.getHealth(),
								targetPlayer.getFoodData().getFoodLevel(),
								targetPlayer.getFoodData().getSaturationLevel()));
						targetPlayer.connection.send(new ClientboundSetExperiencePacket(
								targetPlayer.experienceProgress,
								targetPlayer.totalExperience,
								targetPlayer.experienceLevel));

						targetPlayer.onUpdateAbilities();
						targetPlayer.setPos(targetPlayer.getX(), targetPlayer.getY(), targetPlayer.getZ());
					}

					player.sendSystemMessage(Component.translatable(Constants.MESSAGE_SAVING_SUCCESSFUL)
							.withStyle(ChatFormatting.GREEN));
				} catch (Exception ex) {
					player.sendSystemMessage(Component.translatable(Constants.MESSAGE_SAVING_FAILED_INVALID_NBT)
							.withStyle(ChatFormatting.RED));

					NBTEdit.getInstance().getLogger().error("Player " + player.getName().getString() +
							" edited the tag of entity " + packet.entityUuid + " and caused an exception!");
					NBTEdit.getInstance().getLogger().error("NBT data: " + packet.compoundTag.getAsString());
					NBTEdit.getInstance().getLogger().error(new RuntimeException(ex).toString());
				}
			} else {
				NBTEdit.getInstance().getLogger().info("Player " + player.getName() +
						" tried to edit a non-existent entity " + packet.entityUuid + ".");
				player.sendSystemMessage(Component.translatable(Constants.MESSAGE_SAVING_FAILED_ENTITY_NOT_EXISTS)
						.withStyle(ChatFormatting.RED));
			}
		});
	}
}
