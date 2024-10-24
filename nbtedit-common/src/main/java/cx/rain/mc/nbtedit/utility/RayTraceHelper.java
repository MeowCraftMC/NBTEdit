package cx.rain.mc.nbtedit.utility;

import cx.rain.mc.nbtedit.NBTEditPlatform;
import cx.rain.mc.nbtedit.networking.packet.c2s.BlockEntityRaytraceResultPacket;
import cx.rain.mc.nbtedit.networking.packet.c2s.EntityRaytraceResultPacket;
import cx.rain.mc.nbtedit.networking.packet.c2s.ItemStackRaytraceResultPacket;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;

public class RayTraceHelper {
    public static void doRayTrace() {
        var player = Minecraft.getInstance().player;
        var result = Minecraft.getInstance().hitResult;

        if (result != null) {
            if (result.getType() == HitResult.Type.BLOCK) {
                var block = (BlockHitResult) result;
                NBTEditPlatform.getNetworking().sendToServer(new BlockEntityRaytraceResultPacket(block.getBlockPos()));
            } else if (result.getType() == HitResult.Type.ENTITY) {
                var entity = ((EntityHitResult) result).getEntity();
                NBTEditPlatform.getNetworking().sendToServer(new EntityRaytraceResultPacket(entity.getUUID(), entity.getId()));
            } else if (!player.getMainHandItem().isEmpty()) {
                NBTEditPlatform.getNetworking().sendToServer(new ItemStackRaytraceResultPacket(player.getMainHandItem()));
            } else {
                player.displayClientMessage(Component
                        .translatable(ModConstants.MESSAGE_NOTHING_TO_EDIT)
                        .withStyle(ChatFormatting.RED), false);
            }
        }
    }
}
