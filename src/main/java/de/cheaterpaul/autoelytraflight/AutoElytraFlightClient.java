package de.cheaterpaul.autoelytraflight;

import net.neoforged.bus.api.IEventBus;

public class AutoElytraFlightClient {

    public static void init(IEventBus modbus) {
        ElytraConfig.init();
        modbus.addListener(ClientTicker::registerKeyBinding);
        modbus.addListener(InGameHud::registerOverlay);
    }
}
