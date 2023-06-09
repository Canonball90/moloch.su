package me.afterdarkness.moloch.core.common;


import net.spartanb312.base.module.modules.client.ClickGUI;
import net.spartanb312.base.utils.ColorUtil;

public class Color {

    private int color;
    private int red;
    private int green;
    private int blue;
    private int alpha;
    private boolean syncGlobal;
    private boolean rainbow;
    private float rainbowSpeed;
    private float rainbowSaturation;
    private float rainbowBrightness;

    public Color(int color, boolean syncGlobal, boolean rainbow, float rainbowSpeed, float rainbowSaturation, float rainbowBrightness) {
        this.color = color;
        this.syncGlobal = syncGlobal;
        this.rainbow = rainbow;
        this.rainbowSpeed = rainbowSpeed;
        this.rainbowSaturation = rainbowSaturation;
        this.rainbowBrightness = rainbowBrightness;
        this.red = ColorUtil.getRed(color);
        this.green = ColorUtil.getGreen(color);
        this.blue = ColorUtil.getBlue(color);
        this.alpha = ColorUtil.getAlpha(color);
    }

    public Color(int color) {
        this.color = color;
        this.syncGlobal = false;
        this.rainbow = false;
        this.rainbowSpeed = 1.0f;
        this.rainbowSaturation = 0.75f;
        this.rainbowBrightness = 0.9f;
        this.red = ColorUtil.getRed(color);
        this.green = ColorUtil.getGreen(color);
        this.blue = ColorUtil.getBlue(color);
        this.alpha = ColorUtil.getAlpha(color);
    }

    private void updateColor() {
        if (syncGlobal) {
            setColor(ClickGUI.instance.globalColor.getValue().getColor());
        }
        else {
            if (rainbow) {
                java.awt.Color lgbtq = new java.awt.Color(ColorUtil.staticRainbow(rainbowSpeed, rainbowSaturation, rainbowBrightness));
                setColor(new java.awt.Color(lgbtq.getRed(), lgbtq.getGreen(), lgbtq.getBlue(), alpha).getRGB());
            }
            else {
                setColor(new java.awt.Color(red, green, blue, alpha).getRGB());
            }
        }
    }

    public void setColor(int color) {
        this.color = color;
        setRed(ColorUtil.getRed(color));
        setGreen(ColorUtil.getGreen(color));
        setBlue(ColorUtil.getBlue(color));
        setAlpha(ColorUtil.getAlpha(color));
    }

    public int getColor() {
        updateColor();
        return this.color;
    }

    public java.awt.Color getColorColor() {
        updateColor();
        return new java.awt.Color(this.color);
    }

    public void setSyncGlobal(boolean syncGlobal) {
        this.syncGlobal = syncGlobal;
    }

    public boolean getSyncGlobal() {
        return this.syncGlobal;
    }

    public void setRainbow(boolean rainbow) {
        this.rainbow = rainbow;
    }

    public boolean getRainbow() {
        return this.rainbow;
    }

    public void setRainbowSpeed(float rainbowSpeed) {
        this.rainbowSpeed = rainbowSpeed;
    }

    public float getRainbowSpeed() {
        return this.rainbowSpeed;
    }

    public void setRainbowSaturation(float rainbowSaturation) {
        this.rainbowSaturation = rainbowSaturation;
    }

    public float getRainbowSaturation() {
        return this.rainbowSaturation;
    }

    public void setRainbowBrightness(float rainbowBrightness) {
        this.rainbowBrightness = rainbowBrightness;
    }

    public float getRainbowBrightness() {
        return this.rainbowBrightness;
    }

    public void setRed(int red) {
        this.red = red;
    }

    public int getRed() {
        updateColor();
        return this.red;
    }

    public void setGreen(int green) {
        this.green = green;
    }

    public int getGreen() {
        updateColor();
        return this.green;
    }

    public void setBlue(int blue) {
        this.blue = blue;
    }

    public int getBlue() {
        updateColor();
        return this.blue;
    }

    public void setAlpha(int alpha) {
        this.alpha = alpha;
    }

    public int getAlpha() {
        return this.alpha;
    }

}
