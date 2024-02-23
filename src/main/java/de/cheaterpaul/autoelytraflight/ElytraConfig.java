package de.cheaterpaul.autoelytraflight;


import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.common.ModConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

public class ElytraConfig {

    public static final ElytraConfig CONFIG;
    private static final ModConfigSpec configSpec;

    static {
        final Pair<ElytraConfig, ModConfigSpec> specPair = new ModConfigSpec.Builder().configure(ElytraConfig::new);
        CONFIG = specPair.getKey();
        configSpec = specPair.getValue();
    }

    public static void init() {
        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, configSpec);
    }

    public ModConfigSpec.IntValue guiX;
    public ModConfigSpec.IntValue guiY;
    public ModConfigSpec.IntValue guiWidth;
    public ModConfigSpec.IntValue guiHeight;
    public ModConfigSpec.IntValue guiGraphRealWidth;
    public ModConfigSpec.BooleanValue showGraph;

    public ModConfigSpec.DoubleValue pullUpAngle;
    public ModConfigSpec.DoubleValue pullDownAngle;
    public ModConfigSpec.DoubleValue pullUpMinVelocity;
    public ModConfigSpec.DoubleValue pullDownMaxVelocity;
    public ModConfigSpec.DoubleValue pullUpSpeed;
    public ModConfigSpec.DoubleValue pullDownSpeed;

    public ElytraConfig(ModConfigSpec.Builder builder) {
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
