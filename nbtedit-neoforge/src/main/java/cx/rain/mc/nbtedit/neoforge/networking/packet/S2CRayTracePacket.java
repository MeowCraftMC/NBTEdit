package cx.rain.mc.nbtedit.neoforge.networking.packet;

import cx.rain.mc.nbtedit.NBTEdit;
import cx.rain.mc.nbtedit.neoforge.networking.NBTEditNetworkingImpl;
import cx.rain.mc.nbtedit.utility.RayTraceHelper;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.PlayPayloadContext;
import org.jetbrains.annotations.NotNull;

public class S2CRayTracePacket implements CustomPacketPayload {
    public S2CRayTracePacket() {
    }

    public S2CRayTracePacket(FriendlyByteBuf buf) {
    }

    public static void handle(S2CRayTracePacket packet, PlayPayloadContext context) {
        context.workHandler().submitAsync(() -> {
            RayTraceHelper.doRayTrace();
        });
    }

    @Override
    public void write(FriendlyByteBuf buffer) {
    }

    @Override
    public @NotNull ResourceLocation id() {
        return NBTEditNetworkingImpl.S2C_RAY_TRACE_REQUEST_PACKET_ID;
    }
}
