package cx.rain.mc.nbtedit.utility;

import cx.rain.mc.nbtedit.NBTEdit;
import net.minecraft.client.Minecraft;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;

public class RayTraceHelper {
    public static void doRayTrace() {
        var result = Minecraft.getInstance().hitResult;

        if (result != null) {
            if (result.getType() == HitResult.Type.ENTITY) {
                var entity = ((EntityHitResult) result).getEntity();
                NBTEdit.getInstance().getNetworkManager().clientOpenGuiRequest(entity, false);
            } else if (result.getType() == HitResult.Type.BLOCK) {
                NBTEdit.getInstance().getNetworkManager().clientOpenGuiRequest(((BlockHitResult) result).getBlockPos());
            } else {
                NBTEdit.getInstance().getNetworkManager().clientOpenGuiRequest();
            }
        }
    }
}
