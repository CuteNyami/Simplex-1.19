package tk.simplexclient.renderer;

import lombok.Getter;
import net.minecraft.client.Minecraft;
import org.lwjgl.BufferUtils;
import org.lwjgl.nanovg.NVGPaint;
import org.lwjgl.nanovg.NanoVG;
import tk.simplexclient.SimplexClient;
import tk.simplexclient.utils.IOUtil;

import java.awt.Color;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;

import static org.lwjgl.system.MemoryUtil.*;

public class Renderer {

    @Getter
    private final long vg;

    private static final FloatBuffer bounds = BufferUtils.createFloatBuffer(4);


    final ByteBuffer RobotoRegular = IOUtil.loadResource("Roboto-Regular.ttf", 1024);

    public Renderer(long vg) {
        this.vg = vg;
        NanoVG.nvgCreateFontMem(vg, "roboto", RobotoRegular, 0);
    }

    public void start() {
        SimplexClient.getInstance().getGlState().backupGlState();
        NanoVG.nvgBeginFrame(vg, (float) Minecraft.getInstance().getWindow().getGuiScaledWidth(), Minecraft.getInstance().getWindow().getGuiScaledHeight(), (float) Minecraft.getInstance().getWindow().getGuiScale());
    }

    public void end() {
        NanoVG.nvgEndFrame(vg);
        SimplexClient.getInstance().getGlState().restoreGlState();
    }

    public void drawRoundedRectangle(float x, float y, float width, float height, float radius, Color color) {
        NanoVG.nvgBeginPath(vg);
        NanoVG.nvgRoundedRect(vg, x, y, width, height, radius);
        NanoVG.nvgFillColor(vg, new tk.simplexclient.renderer.Color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).toNVGColor());
        NanoVG.nvgFill(vg);
    }

    public void drawRectangle(float x, float y, float width, float height, Color color) {
        NanoVG.nvgBeginPath(vg);
        NanoVG.nvgRect(vg, x, y, width, height);
        NanoVG.nvgFillColor(vg, new tk.simplexclient.renderer.Color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).toNVGColor());
        NanoVG.nvgFill(vg);
    }

    public void drawRoundedRectangle(int x, int y, float width, float height, float radius, Color color) {
        NanoVG.nvgBeginPath(vg);
        NanoVG.nvgRoundedRect(vg, x, y, width, height, radius);
        NanoVG.nvgFillColor(vg, new tk.simplexclient.renderer.Color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).toNVGColor());
        NanoVG.nvgFill(vg);
    }

    public void drawRectangle(int x, int y, float width, float height, Color color) {
        NanoVG.nvgBeginPath(vg);
        NanoVG.nvgRect(vg, x, y, width, height);
        NanoVG.nvgFillColor(vg, new tk.simplexclient.renderer.Color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).toNVGColor());
        NanoVG.nvgFill(vg);
    }

    public void drawRoundedOutline(float x, float y, float width, float height, float radius, float outlineWidth, Color color) {
        try (NVGPaint paint = NVGPaint.calloc()) {
            NanoVG.nvgBoxGradient(vg, x, y, width - outlineWidth, height - outlineWidth, radius, 0, new tk.simplexclient.renderer.Color(0,0,0, 0).toNVGColor(), new tk.simplexclient.renderer.Color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).toNVGColor(), paint);
            NanoVG.nvgBeginPath(vg);
            NanoVG.nvgPathWinding(vg, NanoVG.NVG_SOLID);
            NanoVG.nvgRoundedRect(vg,
                    x - (outlineWidth / 2),
                    y - (outlineWidth / 2),
                    width,
                    height,
                    radius);
            NanoVG.nvgFillPaint(vg, paint);
            NanoVG.nvgFill(vg);
        }

    }

    public void drawOutline(float x, float y, float width, float height, Color color) {
        NanoVG.nvgBeginPath(vg);
        NanoVG.nvgRect(vg, x, y, width, height);
        NanoVG.nvgFillColor(vg, new tk.simplexclient.renderer.Color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).toNVGColor());
        NanoVG.nvgFill(vg);
    }

    public void drawRoundedRectWithShadow(float x, float y, float width, float height, float radius, Color color) {
        float hOffset = 1;
        float vOffset = 1;
        float blur = 16;
        float spread = -4;

        try (NVGPaint shadowPaint = NVGPaint.calloc()) {
            NanoVG.nvgBoxGradient(vg,
                    x + hOffset - spread,
                    y + vOffset - spread,
                    width + 2 * spread,
                    height + 2 * spread,
                    radius + spread,
                    blur,
                    new tk.simplexclient.renderer.Color(0, 0, 0).toNVGColor(),
                    new tk.simplexclient.renderer.Color(0, 0, 0).toNVGColor(),
                    shadowPaint);
            NanoVG.nvgBeginPath(vg);
            NanoVG.nvgPathWinding(vg, NanoVG.NVG_SOLID);
            NanoVG.nvgRoundedRect(vg, x + hOffset - spread - blur, y + vOffset - spread - blur, width + 2 * spread + 2 * blur, height + 2 * spread + 2 * blur, radius + spread);
            NanoVG.nvgPathWinding(vg, NanoVG.NVG_HOLE);
            NanoVG.nvgRoundedRect(vg, x, y, width, height, radius);
            NanoVG.nvgFillPaint(vg, shadowPaint);
            NanoVG.nvgFillColor(vg, new tk.simplexclient.renderer.Color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).toNVGColor());
            NanoVG.nvgFill(vg);
        }
    }

    public void drawString(String text, float x, float y, Color color) {
        ByteBuffer byteBuffer = memUTF8(text, false);
        memFree(byteBuffer);

        NanoVG.nvgFontSize(vg, 8.0f);
        NanoVG.nvgFontFace(vg, "roboto");
        NanoVG.nvgTextMetrics(vg, null, null, BufferUtils.createFloatBuffer(1));

        long start = memAddress(byteBuffer);
        long end = start + byteBuffer.remaining();

        NanoVG.nvgTextBounds(vg, x, y, byteBuffer, bounds);
        NanoVG.nvgFillColor(vg, new tk.simplexclient.renderer.Color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).toNVGColor());
        NanoVG.nnvgText(vg, x, y + (bounds.get(3) - bounds.get(1)) - 2, start, end);
    }

    public float[] getStringWidth(String text) {
        ByteBuffer byteBuffer = memUTF8(text, false);
        memFree(byteBuffer);

        NanoVG.nvgFontSize(vg, 8.0f);
        NanoVG.nvgFontFace(vg, "roboto");
        NanoVG.nvgTextMetrics(vg, null, null, BufferUtils.createFloatBuffer(1));
        NanoVG.nvgTextBounds(vg, 0, 0, byteBuffer, bounds);

        return new float[]{bounds.get(2) - bounds.get(0), (bounds.get(3) - bounds.get(1)) - 2};
    }
}