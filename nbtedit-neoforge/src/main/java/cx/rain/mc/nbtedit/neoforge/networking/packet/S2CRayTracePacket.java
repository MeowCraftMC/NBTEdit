package cx.rain.mc.nbtedit.neoforge.networking.packet;

import cx.rain.mc.nbtedit.utility.RayTraceHelper;
import net.minecraft.network.FriendlyByteBuf;
import net.neoforged.neoforge.network.NetworkEvent;

public class S2CRayTracePacket {
	public S2CRayTracePacket() {
	}

	public S2CRayTracePacket(FriendlyByteBuf buf) {
	}

	public void toBytes(FriendlyByteBuf buf) {
	}

	public void clientHandleOnMain(NetworkEvent.Context context) {
		RayTraceHelper.doRayTrace();
	}
}
