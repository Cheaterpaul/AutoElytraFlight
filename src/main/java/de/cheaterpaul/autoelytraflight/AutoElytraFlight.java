package de.cheaterpaul.autoelytraflight;


import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.IExtensionPoint;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.loading.FMLEnvironment;

@Mod("autoelytraflight")
public class AutoElytraFlight {

    public AutoElytraFlight(IEventBus modbus) {
        if (FMLEnvironment.dist == Dist.CLIENT) {
            AutoElytraFlightClient.init(modbus);
        }
    }
}
