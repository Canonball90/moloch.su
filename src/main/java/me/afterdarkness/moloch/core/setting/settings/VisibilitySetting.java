package me.afterdarkness.moloch.core.setting.settings;

import net.spartanb312.base.core.setting.Setting;
import me.afterdarkness.moloch.core.common.Visibility;

public class VisibilitySetting extends Setting<Visibility> {
    public VisibilitySetting(String name, Visibility defaultValue) {
        super(name, defaultValue);
    }

    public void setOpposite(boolean visible) {
        value.setVisible(visible);
    }
}
