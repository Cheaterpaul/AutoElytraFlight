package de.cheaterpaul.autoelytraflight;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.GameRenderer;
import net.neoforged.neoforge.client.event.RegisterGuiOverlaysEvent;
import net.neoforged.neoforge.client.gui.overlay.ExtendedGui;
import net.neoforged.neoforge.client.gui.overlay.IGuiOverlay;
import net.neoforged.neoforge.common.NeoForge;

public class InGameHud implements IGuiOverlay {

	private final Minecraft minecraftClient;
	private final ClientTicker ticker;

	public static void registerOverlay(RegisterGuiOverlaysEvent event) {
		ClientTicker clientTicker = new ClientTicker();
		NeoForge.EVENT_BUS.register(clientTicker);
		event.registerAboveAll("elytra-statistics", new InGameHud(clientTicker));
	}

	public InGameHud(ClientTicker ticker) {
		this.ticker = ticker;
		this.minecraftClient = Minecraft.getInstance();
	}

	@Override
	public void render(ExtendedGui gui, GuiGraphics guiGraphics, float partialTick, int screenWidth, int screenHeight) {
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

					guiGraphics.fill(ElytraConfig.CONFIG.guiX.get(), ElytraConfig.CONFIG.guiY.get(), ElytraConfig.CONFIG.guiX.get() + ElytraConfig.CONFIG.guiWidth.get(), ElytraConfig.CONFIG.guiY.get() + ElytraConfig.CONFIG.guiHeight.get(), 0x22FFFFFF);

					double maxAltitude = 0;
					double minAltitude = 999;
					for (GraphDataPoint p : ticker.graph) {
						if (p.realPosition.y > maxAltitude) maxAltitude = p.realPosition.y;
						if (p.realPosition.y < minAltitude) minAltitude = p.realPosition.y;
					}

					if (maxAltitude > 0) {

						maxAltitude += 5;
						minAltitude -= 40;

						beginDrawLineColor();

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

							addLinePointColor(screenX, screenY, 0, 1, r, g, 0);

							currentX += p.horizontalDelta * (ElytraConfig.CONFIG.guiWidth.get() - 1) / ElytraConfig.CONFIG.guiGraphRealWidth.get();
						}

						endDrawLine();


						beginDrawLine(0xFF000000);
						addLinePoint(ElytraConfig.CONFIG.guiX.get(), ElytraConfig.CONFIG.guiY.get(), 0);
						addLinePoint(ElytraConfig.CONFIG.guiX.get(), ElytraConfig.CONFIG.guiY.get() + ElytraConfig.CONFIG.guiHeight.get(), 0);
						addLinePoint(ElytraConfig.CONFIG.guiX.get() + ElytraConfig.CONFIG.guiWidth.get(), ElytraConfig.CONFIG.guiY.get() + ElytraConfig.CONFIG.guiHeight.get(), 0);
						addLinePoint(ElytraConfig.CONFIG.guiX.get() + ElytraConfig.CONFIG.guiWidth.get(), ElytraConfig.CONFIG.guiY.get(), 0);
						addLinePoint(ElytraConfig.CONFIG.guiX.get(), ElytraConfig.CONFIG.guiY.get(), 0);
						endDrawLine();


					}
				}
			}
	}

	private Tesselator tessellator_1;
	private BufferBuilder bufferBuilder_1;
	private void beginDrawLine(int color)
	{
		float float_1 = (float)(color >> 24 & 255) / 255.0F;
		float float_2 = (float)(color >> 16 & 255) / 255.0F;
		float float_3 = (float)(color >> 8 & 255) / 255.0F;
		float float_4 = (float)(color & 255) / 255.0F;

		tessellator_1 = Tesselator.getInstance();
		bufferBuilder_1 = tessellator_1.getBuilder();
		RenderSystem.enableBlend();
		RenderSystem.lineWidth(1.0F);
		RenderSystem.setShaderColor(float_2, float_3, float_4, float_1);
		bufferBuilder_1.begin(VertexFormat.Mode.DEBUG_LINE_STRIP, DefaultVertexFormat.POSITION);
	}

	private void beginDrawLineColor()
	{
		RenderSystem.setShader(GameRenderer::getPositionColorShader);
		tessellator_1 = Tesselator.getInstance();
		bufferBuilder_1 = tessellator_1.getBuilder();
		RenderSystem.enableBlend();
		RenderSystem.lineWidth(1.0F);
		RenderSystem.setShaderFogColor(1,1,1,1);
		bufferBuilder_1.begin(VertexFormat.Mode.DEBUG_LINE_STRIP, DefaultVertexFormat.POSITION_COLOR);
	}

	private void addLinePoint(double x, double y, double z)
	{
		bufferBuilder_1.vertex(x, y, z).endVertex();
	}

	private void addLinePointColor(double x, double y, double z, float a, float r, float g, float b)
	{
		bufferBuilder_1.vertex(x, y, z).color(r,g,b,a).endVertex();
	}

	private void endDrawLine()
	{
		tessellator_1.end();
		RenderSystem.disableBlend();
	}

}
