package cx.rain.mc.nbtedit.utility;

import cx.rain.mc.nbtedit.NBTEditPlatform;
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
            if (result.getType() == HitResult.Type.ENTITY) {
                var entity = ((EntityHitResult) result).getEntity();
                NBTEditPlatform.getNetworking().clientOpenGuiRequest(entity, false);
            } else if (result.getType() == HitResult.Type.BLOCK) {
                NBTEditPlatform.getNetworking().clientOpenGuiRequest(((BlockHitResult) result).getBlockPos());
            } else if (!player.getMainHandItem().isEmpty()) {
                NBTEditPlatform.getNetworking().clientOpenGuiRequest(player.getMainHandItem());
            } else {
                player.createCommandSourceStack().sendFailure(Component
                        .translatable(ModConstants.MESSAGE_NOTHING_TO_EDIT)
                        .withStyle(ChatFormatting.RED));
            }
        }
    }
}
