package de.cheaterpaul.elytraautoflight;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod("elytraautoflight")
public class ElytraAutoFlight {

    public ElytraAutoFlight() {
        DistExecutor.runWhenOn(Dist.CLIENT, () -> () -> {
            ElytraConfig.init();
            FMLJavaModLoadingContext.get().getModEventBus().addListener(ClientTicker::registerKeyBinding);
            ClientTicker ticker = new ClientTicker();
            MinecraftForge.EVENT_BUS.register(ticker);
            MinecraftForge.EVENT_BUS.register(new InGameHud(ticker));
        });
    }
}
