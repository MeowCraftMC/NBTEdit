package cx.rain.mc.nbtedit.forge.networking.packet;

import cx.rain.mc.nbtedit.utility.RayTraceHelper;
import io.netty.buffer.ByteBuf;
import net.minecraftforge.event.network.CustomPayloadEvent;

public class S2CRayTracePacket {
	public S2CRayTracePacket() {
	}

	public S2CRayTracePacket(ByteBuf buf) {
	}

	public void toBytes(ByteBuf buf) {
	}

	public void clientHandleOnMain(CustomPayloadEvent.Context context) {
		RayTraceHelper.doRayTrace();
	}
}
