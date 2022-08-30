package de.cheaterpaul.autoelytraflight;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.lwjgl.glfw.GLFW;

import java.util.LinkedList;

public class ClientTicker {

    private boolean autoFlight;

    private Vec3 previousPosition;
    private double currentVelocity;

    public boolean isDescending;
    public boolean pullUp;
    public boolean pullDown;

    private boolean lastPressed = false;

    public boolean showHud;

    public String[] hudString;


    private final Minecraft mc;

    private static final KeyMapping keyBinding = new KeyMapping("key.autoelytraflight.toggle", InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_R, "text.autoelytraflight.title");

    public LinkedList<GraphDataPoint> graph = new LinkedList<>();

    public static void registerKeyBinding(RegisterKeyMappingsEvent event){
        event.register(keyBinding);
    }

    public ClientTicker() {
        this.mc = Minecraft.getInstance();
    }

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if (mc.player != null) {

            if (mc.player.isFallFlying())
                showHud = true;
            else {
                showHud = false;
                autoFlight = false;
            }

            if (!lastPressed && keyBinding.isDown()) {

                if (mc.player != null) {
                    if (mc.player.isFallFlying()) {
                        // If the player is flying an elytra, we start the auto flight
                        autoFlight = !autoFlight;
                        if (autoFlight) isDescending = true;
                    }
                }
            }
            lastPressed = keyBinding.isDown();


            if (autoFlight) {

                if (isDescending) {
                    pullUp = false;
                    pullDown = true;
                    if (currentVelocity >= ElytraConfig.CONFIG.pullDownMaxVelocity.get()) {
                        isDescending = false;
                        pullDown = false;
                        pullUp = true;
                    }
                } else {
                    pullUp = true;
                    pullDown = false;
                    if (currentVelocity <= ElytraConfig.CONFIG.pullUpMinVelocity.get()) {
                        isDescending = true;
                        pullDown = true;
                        pullUp = false;
                    }
                }

                if (pullUp) {
                    mc.player.setXRot((float) (mc.player.getXRot()- ElytraConfig.CONFIG.pullUpSpeed.get()));

                    if (mc.player.getXRot() <= ElytraConfig.CONFIG.pullUpAngle.get())
                        mc.player.setXRot(ElytraConfig.CONFIG.pullUpAngle.get().floatValue());
                }

                if (pullDown) {
                    mc.player.setXRot((float) (mc.player.getXRot() + ElytraConfig.CONFIG.pullDownSpeed.get()));

                    if (mc.player.getXRot() >= ElytraConfig.CONFIG.pullDownAngle.get())
                        mc.player.setXRot(ElytraConfig.CONFIG.pullDownAngle.get().floatValue());
                }
            } else {
                pullUp = false;
                pullDown = false;
            }


            if (showHud) {
                // TODO only if flying?
                computeVelocity();

                double altitude = mc.player.position().y;

                if (hudString == null) hudString = new String[3];

                hudString[0] = "Auto flight : " + (autoFlight ? "Enabled" : "Disabled");
                hudString[1] = "Altitude : " + String.format("%.2f", altitude);
                hudString[2] = "Speed : " + String.format("%.2f", currentVelocity * 20) + " m/s";

                GraphDataPoint newDataPoint;
                if (graph.size() > 0)
                    newDataPoint = new GraphDataPoint(mc.player.position(), graph.getLast().realPosition);
                else newDataPoint = new GraphDataPoint(mc.player.position());
                newDataPoint.pullUp = pullUp;
                newDataPoint.pullDown = pullDown;
                newDataPoint.velocity = currentVelocity;

                addLastDataPoint(newDataPoint);
            } else {
                clearGraph();
            }

        }
    }

    private double totalHorizontalDelta = 0;
    private void addLastDataPoint(GraphDataPoint p) {
        graph.addLast(p);
        totalHorizontalDelta += p.horizontalDelta;

        while (totalHorizontalDelta > ElytraConfig.CONFIG.guiGraphRealWidth.get()) removeFirstDataPoint();
    }

    private void removeFirstDataPoint() {
        graph.removeFirst();
        totalHorizontalDelta -= graph.getFirst().horizontalDelta;
    }

    private void clearGraph(){
        graph.clear();
        totalHorizontalDelta = 0;
    }

    private void computeVelocity()
    {
        Vec3 newPosition = mc.player.position();

        if (previousPosition == null)
            previousPosition = newPosition;

        Vec3 difference = new Vec3(newPosition.x - previousPosition.x, newPosition.y - previousPosition.y, newPosition.z - previousPosition.z);

        previousPosition = newPosition;

        currentVelocity = difference.length() > 0 ? difference.length() : currentVelocity;
    }
}
