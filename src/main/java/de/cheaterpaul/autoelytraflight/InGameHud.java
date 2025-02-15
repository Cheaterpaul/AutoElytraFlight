package de.cheaterpaul.autoelytraflight;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.LayeredDraw;
import net.minecraft.client.renderer.CoreShaders;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.client.event.RegisterGuiLayersEvent;
import net.neoforged.neoforge.common.NeoForge;

public class InGameHud implements LayeredDraw.Layer {

	private final Minecraft minecraftClient;
	private final ClientTicker ticker;

	public static void registerOverlay(RegisterGuiLayersEvent event) {
		ClientTicker clientTicker = new ClientTicker();
		NeoForge.EVENT_BUS.register(clientTicker);
		event.registerAboveAll(ResourceLocation.fromNamespaceAndPath("autoelytraflight","elytra-statistics"), new InGameHud(clientTicker));
	}

	public InGameHud(ClientTicker ticker) {
		this.ticker = ticker;
		this.minecraftClient = Minecraft.getInstance();
	}

	@Override
	public void render(GuiGraphics guiGraphics, DeltaTracker delta) {
			if (ticker.showHud) {

				if (ticker.hudString != null) {
					float stringX = ElytraConfig.CONFIG.guiX.get();
					float stringY = ElytraConfig.CONFIG.guiY.get() + ElytraConfig.CONFIG.guiHeight.get() + 2;

					for (int i = 0; i < ticker.hudString.length; i++) {
						guiGraphics.drawString(minecraftClient.font, ticker.hudString[i], stringX, stringY, 0xFFFFFF, true);
						stringY += minecraftClient.font.lineHeight + 1;

					}
				}

				if (ElytraConfig.CONFIG.showGraph.get()) {
					PoseStack pose = guiGraphics.pose();
					pose.pushPose();
					guiGraphics.fill(ElytraConfig.CONFIG.guiX.get(), ElytraConfig.CONFIG.guiY.get(), ElytraConfig.CONFIG.guiX.get() + ElytraConfig.CONFIG.guiWidth.get(), ElytraConfig.CONFIG.guiY.get() + ElytraConfig.CONFIG.guiHeight.get(), 0x22FFFFFF);

					double maxAltitude = 0;
					double minAltitude = 999;
					for (GraphDataPoint p : ticker.graph) {
						if (p.realPosition.y > maxAltitude) maxAltitude = p.realPosition.y;
						if (p.realPosition.y < minAltitude) minAltitude = p.realPosition.y;
					}

					if (maxAltitude > 0) {
						MultiBufferSource.BufferSource bufferSource = minecraftClient.renderBuffers().bufferSource();
						maxAltitude += 5;
						minAltitude -= 40;

						VertexConsumer consumer = beginDrawLineColor(bufferSource);

						double currentX = 0;
						double currentY;
						for (GraphDataPoint p : ticker.graph) {

							currentY = ((p.realPosition.y - minAltitude) * ElytraConfig.CONFIG.guiHeight.get() / (maxAltitude - minAltitude));

							double screenX = ElytraConfig.CONFIG.guiX.get() + currentX;
							double screenY = ElytraConfig.CONFIG.guiY.get() + ElytraConfig.CONFIG.guiHeight.get() - currentY;

							float speedRatio = (float) p.velocity / 3f;

							float r = 2 * (1 - speedRatio);
							float g = 2 * speedRatio;

							if (r > 1) r = 1;
							if (g > 1) g = 1;
							if (r < 0) r = 0;
							if (g < 0) g = 0;

							addLinePointColor(pose, consumer, (float) screenX, (float) screenY, 0, 1, r, g, 0);

							currentX += p.horizontalDelta * (ElytraConfig.CONFIG.guiWidth.get() - 1) / ElytraConfig.CONFIG.guiGraphRealWidth.get();
						}

						endDrawLine(bufferSource);

						VertexConsumer consumer1 = beginDrawLine(bufferSource, 0xFF000000);
						addLinePointColor(pose, consumer1, ElytraConfig.CONFIG.guiX.get(), ElytraConfig.CONFIG.guiY.get(), 0f, 1f,0f,0f, 0f);
						addLinePointColor(pose, consumer1, ElytraConfig.CONFIG.guiX.get(), ElytraConfig.CONFIG.guiY.get() + ElytraConfig.CONFIG.guiHeight.get(), 0, 1f,0f,0f, 0f);
						addLinePointColor(pose, consumer1, ElytraConfig.CONFIG.guiX.get() + ElytraConfig.CONFIG.guiWidth.get(), ElytraConfig.CONFIG.guiY.get() + ElytraConfig.CONFIG.guiHeight.get(), 0, 1f,0f,0f, 0f);
						addLinePointColor(pose, consumer1, ElytraConfig.CONFIG.guiX.get() + ElytraConfig.CONFIG.guiWidth.get(), ElytraConfig.CONFIG.guiY.get(), 0, 1f,0f,0f, 0f);
						addLinePointColor(pose, consumer1, ElytraConfig.CONFIG.guiX.get(), ElytraConfig.CONFIG.guiY.get(), 0, 1f,0f,0f, 0f);
						endDrawLine(bufferSource);
					}
					pose.popPose();
				}
			}
	}

	private VertexConsumer beginDrawLine(MultiBufferSource.BufferSource bufferSource, int color)
	{


		RenderSystem.setShader(CoreShaders.POSITION_COLOR);
		RenderSystem.enableBlend();
		return bufferSource.getBuffer(RenderType.debugLineStrip(1));
	}

	private VertexConsumer beginDrawLineColor(MultiBufferSource bufferSource)
	{
		RenderSystem.setShader(CoreShaders.POSITION_COLOR);
		RenderSystem.enableBlend();
		return bufferSource.getBuffer(RenderType.debugLineStrip(1));
	}

	private void addLinePointColor(PoseStack poseStack, VertexConsumer consumer, float x, float y, float z, float a, float r, float g, float b)
	{
		consumer.addVertex(poseStack.last().pose(), x, y, z).setColor(r,g,b,a);
	}

	private void endDrawLine(MultiBufferSource.BufferSource source)
	{
		source.endLastBatch();
		RenderSystem.disableBlend();
	}
}