package de.cheaterpaul.elytraautoflight;

import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.client.util.InputMappings;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import org.lwjgl.glfw.GLFW;

import java.util.LinkedList;

public class ClientTicker {

    private boolean autoFlight;

    private Vector3d previousPosition;
    private double currentVelocity;

    public boolean isDescending;
    public boolean pullUp;
    public boolean pullDown;

    private boolean lastPressed = false;

    public boolean showHud;

    public String[] hudString;


    private Minecraft mc;

    private static KeyBinding keyBinding;

    public LinkedList<GraphDataPoint> graph = new LinkedList<>();

    public static void registerKeyBinding(FMLClientSetupEvent event){
        keyBinding = new KeyBinding("key.elytraautoflight.toggle", InputMappings.Type.KEYSYM, GLFW.GLFW_KEY_R, "text.elytraautoflight.title");
        ClientRegistry.registerKeyBinding(keyBinding);
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
                    mc.player.xRot -= ElytraConfig.CONFIG.pullUpSpeed.get();

                    if (mc.player.xRot <= ElytraConfig.CONFIG.pullUpAngle.get())
                        mc.player.xRot = ElytraConfig.CONFIG.pullUpAngle.get().floatValue();
                }

                if (pullDown) {
                    mc.player.xRot += ElytraConfig.CONFIG.pullDownSpeed.get();

                    if (mc.player.xRot >= ElytraConfig.CONFIG.pullDownAngle.get())
                        mc.player.xRot = ElytraConfig.CONFIG.pullDownAngle.get().floatValue();
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
        Vector3d newPosition = mc.player.position();

        if (previousPosition == null)
            previousPosition = newPosition;

        Vector3d difference = new Vector3d(newPosition.x - previousPosition.x, newPosition.y - previousPosition.y, newPosition.z - previousPosition.z);

        previousPosition = newPosition;

        currentVelocity = difference.length() > 0 ? difference.length() : currentVelocity;
    }
}
