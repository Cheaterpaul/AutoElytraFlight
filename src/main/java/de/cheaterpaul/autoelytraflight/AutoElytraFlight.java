package de.cheaterpaul.autoelytraflight;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.IExtensionPoint;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod("autoelytraflight")
public class AutoElytraFlight {

    public AutoElytraFlight() {
        ModLoadingContext.get().registerExtensionPoint(IExtensionPoint.DisplayTest.class, () -> new IExtensionPoint.DisplayTest(() -> "", (incoming, isNetwork) -> true));
        DistExecutor.runWhenOn(Dist.CLIENT, () -> () -> {
            ElytraConfig.init();
            FMLJavaModLoadingContext.get().getModEventBus().addListener(ClientTicker::registerKeyBinding);
            FMLJavaModLoadingContext.get().getModEventBus().addListener(InGameHud::registerOverlay);
        });
    }
}
