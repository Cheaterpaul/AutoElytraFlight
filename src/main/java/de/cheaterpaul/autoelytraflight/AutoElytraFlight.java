package de.cheaterpaul.autoelytraflight;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
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
            ClientTicker ticker = new ClientTicker();
            MinecraftForge.EVENT_BUS.register(ticker);
            MinecraftForge.EVENT_BUS.register(new InGameHud(ticker));
        });
    }
}
