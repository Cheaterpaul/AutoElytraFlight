package de.cheaterpaul.elytraautoflight;

import com.electronwill.nightconfig.core.ConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;
import org.apache.commons.lang3.tuple.Pair;

public class ElytraConfig {

    public static final ElytraConfig CONFIG;
    private static final ForgeConfigSpec configSpec;

    static {
        final Pair<ElytraConfig, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(ElytraConfig::new);
        CONFIG = specPair.getKey();
        configSpec = specPair.getValue();
    }

    public static void init() {
        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, configSpec);
    }

    public ForgeConfigSpec.IntValue guiX;
    public ForgeConfigSpec.IntValue guiY;
    public ForgeConfigSpec.IntValue guiWidth;
    public ForgeConfigSpec.IntValue guiHeight;
    public ForgeConfigSpec.IntValue guiGraphRealWidth;
    public ForgeConfigSpec.BooleanValue showGraph;

    public ForgeConfigSpec.DoubleValue pullUpAngle;
    public ForgeConfigSpec.DoubleValue pullDownAngle;
    public ForgeConfigSpec.DoubleValue pullUpMinVelocity;
    public ForgeConfigSpec.DoubleValue pullDownMaxVelocity;
    public ForgeConfigSpec.DoubleValue pullUpSpeed;
    public ForgeConfigSpec.DoubleValue pullDownSpeed;

    public ElytraConfig(ForgeConfigSpec.Builder builder) {
        builder.push("In-Flight Gui");
        this.guiX = builder.comment("Graph X").defineInRange("guiX", 5, 0, Integer.MAX_VALUE);
        this.guiY = builder.comment("Graph Y").defineInRange("guiY", 5, 0, Integer.MAX_VALUE);
        this.guiWidth = builder.comment("Graph width").defineInRange("guiWidth", 150, 0, Integer.MAX_VALUE);
        this.guiHeight = builder.comment("Graph height").defineInRange("guiHeight", 50, 0, Integer.MAX_VALUE);
        this.guiGraphRealWidth = builder.comment("Graph horizontal distance").defineInRange("guiGraphRealWidth", 2000, 0, Integer.MAX_VALUE);
        this.showGraph = builder.comment("Display graph").define("showGraph", true);
        builder.pop();
        builder.push("Flight Profile");
        this.pullUpAngle = builder.comment("Going up angle").defineInRange("pullUpAngle", -46.633514, -Double.MAX_VALUE, Double.MAX_VALUE);
        this.pullDownAngle = builder.comment("Going down angle").defineInRange("pullDownAngle", 37.19872, -Double.MAX_VALUE, Double.MAX_VALUE);
        this.pullUpMinVelocity = builder.comment("Min velocity when going up").defineInRange("pullUpMinVelocity", 1.9102669, Double.MIN_VALUE, Double.MAX_VALUE);
        this.pullDownMaxVelocity = builder.comment("Max velocity when going down").defineInRange("pullDownMaxVelocity", 2.3250866, Double.MIN_VALUE, Double.MAX_VALUE);
        this.pullUpSpeed = builder.comment("Speed to pull up").defineInRange("pullUpSpeed", 2.1605124 * 3, Double.MIN_VALUE, Double.MAX_VALUE);
        this.pullDownSpeed = builder.comment("speed to pull down").defineInRange("pullDownSpeed", 0.20545267 * 3, Double.MIN_VALUE, Double.MAX_VALUE);
        builder.pop();
    }
}
