package net.spartanb312.base.core.concurrent.repeat;

import net.spartanb312.base.client.ConfigManager;
import net.spartanb312.base.core.concurrent.ConcurrentTaskManager;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import static net.spartanb312.base.core.concurrent.ConcurrentTaskManager.launch;

/**
 * Created by B_312 on 05/01/2021
 */
public class RepeatManager {

    public static RepeatManager instance;

    public static void init() {
        if (instance == null) instance = new RepeatManager();
    }

    public final List<RepeatUnit> repeatUnits = new CopyOnWriteArrayList<>();
    public final List<DelayUnit> delayUnits = new CopyOnWriteArrayList<>();

    public static void update() {
        instance.delayUnits.removeIf(DelayUnit::invoke);
        instance.repeatUnits.removeIf(RepeatUnit::isDead);
        instance.repeatUnits.forEach(it -> {
            if (it.shouldRun()) {
                ConcurrentTaskManager.launch(it::run);
            }
        });
    }

}
