package net.spartanb312.base.gui.renderers;

import net.spartanb312.base.module.modules.client.HUDEditor;
import net.spartanb312.base.client.ModuleManager;
import net.spartanb312.base.gui.Component;
import net.spartanb312.base.gui.Panel;
import net.spartanb312.base.gui.components.ModuleButton;
import net.spartanb312.base.hud.HUDModule;
import net.spartanb312.base.module.Category;
import net.spartanb312.base.module.modules.client.ClickGUI;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.spartanb312.base.utils.MathUtilFuckYou;
import net.spartanb312.base.utils.Timer;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import java.util.ArrayList;
import java.util.Collections;

import static org.lwjgl.input.Keyboard.KEY_DOWN;
import static org.lwjgl.input.Keyboard.KEY_UP;

public class  HUDEditorRenderer {

    public ArrayList<Panel> panels = new ArrayList<>();
    public final ArrayList<HUDModule> hudModules = new ArrayList<>();
    public static HUDEditorRenderer instance = new HUDEditorRenderer();
    public static boolean switchPanelFlag = false;
    public static Panel panelToSwitch;
    public static float scrolledY = 0;
    private final Timer scrollTimer = new Timer();
    public float scrollDelta;
    private float prevScrollY;
    public float scrollUpOrDown = 1.0f;

    public HUDEditorRenderer() {
        int startX = 5;
        for (Category category : Category.values()) {
            if (category == Category.HIDDEN || !category.isHUD) continue;
            panels.add(new Panel(category, startX, 20, 120, 16));
            startX += 124;
        }
        panels.forEach(it -> it.elements.forEach(moduleButton -> hudModules.add((HUDModule) moduleButton.module)));
    }

    public void drawScreen(int mouseX, int mouseY, float translateDelta, float partialTicks) {
        for (int i = panels.size() - 1; i >= 0; i--) {
            panels.get(i).drawShadowsAndGlow(mouseX, mouseY);
        }

        for (int i = panels.size() - 1; i >= 0; i--) {
            panels.get(i).drawScreen(mouseX, mouseY, translateDelta, partialTicks);
        }

        if (Component.shouldScroll) {
            int passedms = (int) scrollTimer.hasPassed();
            scrollTimer.reset();
            int dWheel = Mouse.getDWheel();

            scrollRollColor(passedms, dWheel);
            panels.forEach(component -> component.scrollPanel(passedms, dWheel));
        }
    }

    public void drawHUDElements(int mouseX, int mouseY, float partialTicks) {
        ScaledResolution resolution = new ScaledResolution(Minecraft.getMinecraft());

        for (int i = hudModules.size() - 1; i >= 0; i--) {
            HUDModule hudModule = hudModules.get(i);
            if (hudModule.x < 0) hudModule.x++;
            if (hudModule.y < 0) hudModule.y++;
            if (hudModule.x + hudModule.width > resolution.getScaledWidth()) hudModule.x--;
            if (hudModule.y + hudModule.height > resolution.getScaledHeight()) hudModule.y--;
            hudModule.renderInHUDEditor(mouseX, mouseY, partialTicks, resolution);
        }
    }

    public Panel getPanelByName(String name) {
        Panel getPane = null;
        if (panels != null)
            for (Panel panel : panels) {
                if (!panel.category.categoryName.equals(name)) {
                    continue;
                }
                getPane = panel;
            }
        return getPane;
    }

    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        for (Panel panel : panels) {
            panel.onMouseClicked(mouseX, mouseY, mouseButton);
            if (!panel.extended) continue;
            for (ModuleButton part : panel.elements) {
                part.onMouseClicked(mouseX, mouseY, mouseButton);
                if (!part.isExtended) continue;
                for (Component component : part.settings) {
                    if (!component.isVisible()) continue;
                    component.onMouseClicked(mouseX, mouseY, mouseButton);
                }
            }
        }

        if (switchPanelFlag && panelToSwitch != null) {
            panels.remove(panelToSwitch);
            panels.add(0, panelToSwitch);
            switchPanelFlag = false;
        }

        for (HUDModule hudModule : hudModules) {
            if (hudModule.onMouseClicked(mouseX, mouseY, mouseButton)) {
                Collections.swap(hudModules, 0, hudModules.indexOf(hudModule));
                return;
            }
        }
    }

    public void keyTyped(char typedChar, int keyCode) {
        if (keyCode == Keyboard.KEY_ESCAPE) {
            ModuleManager.getModule(HUDEditor.class).disable();
        }
        for (Panel panel : panels) {
            panel.keyTyped(typedChar, keyCode);
        }
    }

    public void mouseReleased(int mouseX, int mouseY, int state) {
        for (Panel panel : panels) {
            panel.mouseReleased(mouseX, mouseY, state);
        }
        for (HUDModule hudModule : hudModules) {
            hudModule.onMouseReleased(mouseX, mouseY, state);
        }
    }

    public void scrollRollColor(int passedms, int dWheel) {
        if (!Component.shouldScroll) return;

        if (dWheel < 0 || (ClickGUI.instance.arrowScroll.getValue() && Keyboard.isKeyDown(KEY_UP))) {
            scrollUpOrDown = -1.0f;
        }

        if (dWheel > 0 || (ClickGUI.instance.arrowScroll.getValue() && Keyboard.isKeyDown(KEY_DOWN))) {
            scrollUpOrDown = 1.0f;
        }

        if ((dWheel < 0 || (ClickGUI.instance.arrowScroll.getValue() && Keyboard.isKeyDown(KEY_UP))) || (dWheel > 0 || (ClickGUI.instance.arrowScroll.getValue() && Keyboard.isKeyDown(KEY_DOWN)))) {
            prevScrollY = scrolledY;
            scrollDelta = 0.0f;
        }

        if (passedms < 1000) {
            scrollDelta = MathUtilFuckYou.clamp(scrollDelta + ClickGUI.instance.scrollSpeed.getValue() * passedms / 5.0f, 0.0f, 300.0f);
        }

        scrolledY = MathUtilFuckYou.interpNonLinear(prevScrollY, prevScrollY + ClickGUI.instance.scrollAmount.getValue() * -1.0f * scrollUpOrDown, scrollDelta / 300.0f, ClickGUI.instance.scrollFactor.getValue() / 2.6667f);
    }
}
