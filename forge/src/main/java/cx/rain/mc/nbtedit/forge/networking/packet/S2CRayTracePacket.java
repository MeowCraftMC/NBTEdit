package cx.rain.mc.nbtedit.forge.networking.packet;

import cx.rain.mc.nbtedit.utility.RayTraceHelper;
import io.netty.buffer.ByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class S2CRayTracePacket {
	public S2CRayTracePacket() {
	}

	public S2CRayTracePacket(ByteBuf buf) {
	}

	public void toBytes(ByteBuf buf) {
	}

	public void clientHandle(Supplier<NetworkEvent.Context> context) {
		context.get().enqueueWork(RayTraceHelper::doRayTrace);
	}
}
