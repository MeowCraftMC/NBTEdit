package cx.rain.mc.nbtedit.forge.networking.packet;

import cx.rain.mc.nbtedit.NBTEdit;
import cx.rain.mc.nbtedit.utility.Constants;
import io.netty.buffer.ByteBuf;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundSetExperiencePacket;
import net.minecraft.network.protocol.game.ClientboundSetHealthPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.GameType;
import net.minecraftforge.event.network.CustomPayloadEvent;

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

	public C2SEntitySavingPacket(ByteBuf byteBuf) {
		var buf = new FriendlyByteBuf(byteBuf);
		entityUuid = buf.readUUID();
		entityId = buf.readInt();
		compoundTag = buf.readNbt();
		isSelf = buf.readBoolean();
	}

	public C2SEntitySavingPacket(UUID uuid, int id, CompoundTag tag, boolean self) {
		entityUuid = uuid;
		compoundTag = tag;
		entityId = id;
		isSelf = self;
	}

	public void toBytes(ByteBuf byteBuf) {
		var buf = new FriendlyByteBuf(byteBuf);
		buf.writeUUID(entityUuid);
		buf.writeInt(entityId);
		buf.writeNbt(compoundTag);
		buf.writeBoolean(isSelf);
	}

	public void serverHandleOnMain(CustomPayloadEvent.Context context) {
		var player = context.getSender();
		var server = player.getServer();
		var level = player.serverLevel();
		server.execute(() -> {
			var entity = level.getEntity(entityUuid);
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
					entity.load(compoundTag);
					NBTEdit.getInstance().getLogger().info("Player " + player.getName().getString() +
							" edited the tag of Entity with UUID " + entityUuid + " .");
					NBTEdit.getInstance().getLogger().debug("New NBT of entity " + entityUuid +
							" is " + compoundTag.getAsString());

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
							" edited the tag of entity " + entityUuid + " and caused an exception!");
					NBTEdit.getInstance().getLogger().error("NBT data: " + compoundTag.getAsString());
					NBTEdit.getInstance().getLogger().error(new RuntimeException(ex).toString());
				}
			} else {
				NBTEdit.getInstance().getLogger().info("Player " + player.getName() +
						" tried to edit a non-existent entity " + entityUuid + ".");
				player.sendSystemMessage(Component.translatable(Constants.MESSAGE_SAVING_FAILED_ENTITY_NOT_EXISTS)
						.withStyle(ChatFormatting.RED));
			}
		});
	}
}
