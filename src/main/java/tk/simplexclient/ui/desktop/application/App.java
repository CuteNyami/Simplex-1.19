package tk.simplexclient.ui.desktop.application;

import com.mojang.blaze3d.vertex.PoseStack;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import tk.simplexclient.SimplexClient;
import tk.simplexclient.renderer.Renderer;

import java.awt.*;

/**
 * Create a new application that can be used on the desktop
 */
public class App {

    @Getter
    private final String name;

    @Getter
    private final EnumAppPosition position;

    @Getter
    private final EnumAppButtons buttons;

    @Getter
    private final int width, height;

    @Getter
    private final ResourceLocation icon;

    private Minecraft minecraft;

    private final int guiWidth, guiHeight;

    private final Renderer renderer;

    @Setter
    public boolean shadow = true;

    @Getter
    @Setter
    private Color color = new Color(56, 56, 56);

    @Getter
    private int[] pos = new int[]{};

    /**
     * Initializes all the needed variables for the application
     *
     * @param name     The title for the application
     * @param position The position for the application
     * @param buttons  The buttons for the application
     * @param width    The width for the application
     * @param height   The height for the application
     * @param icon     The icon for the application
     */
    public App(String name, EnumAppPosition position, EnumAppButtons buttons, int width, int height, ResourceLocation icon) {
        this.name = name;
        this.position = position;
        this.buttons = buttons;
        this.width = width;
        this.height = height;
        this.icon = icon;
        this.minecraft = Minecraft.getInstance();
        this.guiWidth = minecraft.getWindow().getGuiScaledWidth();
        this.guiHeight = minecraft.getWindow().getGuiScaledHeight();
        this.renderer = SimplexClient.getInstance().getRenderer();
        this.setPosition();
    }

    /**
     * Initializes all the needed variables for the application
     *
     * @param name     The title for the application
     * @param position The position for the application
     * @param buttons  The buttons for the application
     * @param width    The width for the application
     * @param height   The height for the application
     */
    public App(String name, EnumAppPosition position, EnumAppButtons buttons, int width, int height) {
        this.name = name;
        this.position = position;
        this.buttons = buttons;
        this.width = width;
        this.height = height;
        this.icon = new ResourceLocation("simplex/textures/icons/applications/default.png");
        this.minecraft = Minecraft.getInstance();
        this.guiWidth = minecraft.getWindow().getGuiScaledWidth();
        this.guiHeight = minecraft.getWindow().getGuiScaledHeight();
        this.renderer = SimplexClient.getInstance().getRenderer();
        this.setPosition();
    }

    /**
     * Renders the application background with the buttons,
     * and it renders the application content
     *
     * @param poseStack PoseStack that Minecraft is using
     * @param mouseX    Mouse x position
     * @param mouseY    Mouse y position
     */
    public void renderApplication(PoseStack poseStack, int mouseX, int mouseY) {
        renderer.start();
        {
            if (shadow) {
                renderer.drawRoundedRectWithShadow((float) getPos()[0], (float) getPos()[1], width, height, 5, 1, color);
            } else {
                renderer.drawRoundedRectangle((float) getPos()[0], (float) getPos()[1], width, height, 5, color);
            }
            //renderer.drawRectangle((float) getPos()[0], (float) getPos()[1], width, 15, Color.WHITE);
            renderer.drawCircle((float) getPos()[0] + 10, (float) getPos()[1] + 7.5f, 2.5f, new Color(255, 80, 80));
            renderer.drawCircle((float) getPos()[0] + 17.5f, (float) getPos()[1] + 7.5f, 2.5f, new Color(255, 188, 0));
            renderer.drawCircle((float) getPos()[0] + 25, (float) getPos()[1] + 7.5f, 2.5f, new Color(0, 205, 32));

            float[] titleSize = renderer.getStringWidth(name, 6.5f, "inter");
            renderer.drawStringScaled(name,
                    (float) getPos()[0] + (float) (width / 2) - (titleSize[0] / 2),
                    (float) getPos()[1] + 7.5f - (titleSize[1] / 2),
                    6.5f, new Color(232, 232, 232), "inter");
        }
        renderer.end();
        this.render(poseStack, mouseX, mouseY);
    }

    /**
     * Renders the content of the application
     *
     * @param poseStack PoseStack that Minecraft is using
     * @param mouseX    Mouse x position
     * @param mouseY    Mouse y position
     */
    public void render(PoseStack poseStack, int mouseX, int mouseY) {
    }

    /**
     * Get {@link EnumAppPosition} as int array
     *
     * @return The position as int array
     */
    protected void setPosition() {
        switch (position) {
            case CENTER -> pos = new int[]{(guiWidth / 2) - (width / 2), (guiHeight / 2) - (height / 2)};
            case CENTER_LEFT -> pos = new int[]{0, (guiHeight / 2) - (height / 2)};
            case CENTER_RIGHT -> pos = new int[]{guiWidth - width, (guiHeight / 2) - (height / 2)};
            case CENTER_TOP -> pos = new int[]{(guiWidth / 2) - (width / 2), 0};
            case CENTER_BOTTOM -> pos = new int[]{(guiWidth / 2) - (width / 2), guiHeight - height};
            case TOP_LEFT -> pos = new int[]{0, 0};
            case TOP_RIGHT -> pos = new int[]{guiWidth - width, 0};
            case BOTTOM_LEFT -> pos = new int[]{0, guiHeight - height};
            case BOTTOM_RIGHT -> pos = new int[]{guiWidth - width, guiHeight - height};
        }
    }

    public void setPos(int x, int y) {
        this.pos = new int[]{x, y};
    }
}
