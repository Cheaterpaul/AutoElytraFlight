package de.cheaterpaul.autoelytraflight;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;

@Mod(value = "autoelytraflight", dist = Dist.CLIENT)
public class AutoElytraFlightClient {

    public AutoElytraFlightClient(IEventBus modEventBus, ModContainer container) {
        ElytraConfig.init(container);
        modEventBus.addListener(ClientTicker::registerKeyBinding);
        modEventBus.addListener(InGameHud::registerOverlay);
    }
}
