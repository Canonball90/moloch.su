package net.spartanb312.base.core.concurrent;

import net.spartanb312.base.core.concurrent.blocking.BlockingContent;
import net.spartanb312.base.core.concurrent.blocking.BlockingTask;
import net.spartanb312.base.core.concurrent.repeat.DelayUnit;
import net.spartanb312.base.core.concurrent.repeat.RepeatManager;
import net.spartanb312.base.core.concurrent.repeat.RepeatUnit;
import net.spartanb312.base.core.concurrent.task.*;
import net.spartanb312.base.core.concurrent.thread.BackgroundMainThread;
import net.spartanb312.base.core.concurrent.utils.Syncer;
import net.spartanb312.base.utils.ChatUtil;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.*;
import java.util.function.IntSupplier;

/**
 * Created by B_312 on 05/01/2021
 */
public class ConcurrentTaskManager {

    //---- Instance Stuff ----//
    public static ConcurrentTaskManager instance = new ConcurrentTaskManager();

    public static final int workingThreads = Runtime.getRuntime().availableProcessors();

    public BackgroundMainThread backgroundMainThread = new BackgroundMainThread();

    public ThreadPoolExecutor executor = new ThreadPoolExecutor(workingThreads, Integer.MAX_VALUE, 1000, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>());

    ConcurrentTaskManager() {
        RepeatManager.init();
        backgroundMainThread.start();
    }

    public static long runTiming(VoidTask task) {
        long startTime = System.currentTimeMillis();
        task.invoke();
        return System.currentTimeMillis() - startTime;
    }

    //---- TaskPool Runner ----//
    public static void runBlocking(BlockingTask task) {
        BlockingContent content = new BlockingContent();
        task.invoke(content);
        content.await();
    }

    public static void runBlockingTasks(VoidTask... tasks) {
        runBlockingTasks(Arrays.asList(tasks));
    }

    public static void runBlockingTasks(List<VoidTask> tasks) {
        Syncer syncer = new Syncer(tasks.size());
        tasks.forEach(it -> instance.executor.execute(new VoidRunnable(it, syncer)));
        syncer.await();
    }

    public static <T> void runParameterBlocking(List<MultiParameterTask<T>> tasks, T[] parameters) {
        Syncer syncer = new Syncer(tasks.size());
        tasks.forEach(it -> instance.executor.execute(new TaskRunnable<>(it, syncer, parameters)));
        syncer.await();
    }

    public static void launch(VoidTask task) {
        instance.executor.execute(new VoidRunnable(task));
    }

    public static <T> void launch(Syncer syncer, T parameters, Task<T> task) {
        instance.executor.execute(new TaskRunnable<>(task, syncer, parameters));
    }

    public static <T> void launch(T parameter, Task<T> task) {
        instance.executor.execute(new TaskRunnable<>(parameter, task));
    }

    public static <T> void launch(Syncer syncer, T[] parameters, MultiParameterTask<T> task) {
        instance.executor.execute(new TaskRunnable<>(task, syncer, parameters));
    }

    public static <T> void launch(T[] parameters, MultiParameterTask<T> task) {
        instance.executor.execute(new TaskRunnable<>(parameters, task));
    }

    public static void launch(Syncer syncer, VoidTask task) {
        instance.executor.execute(new VoidRunnable(task, syncer));
    }

    /**
     *
     * @param task calculation task to complete
     * @param timeout milliseconds to wait for calculation to complete
     * @return calculation result
     */
    public static Object calculate(VoidTask task, int timeout) {
        try {
            return instance.executor.submit(new VoidRunnable(task)).get(timeout, TimeUnit.MILLISECONDS);
        } catch (ExecutionException | InterruptedException | TimeoutException e) {
            ChatUtil.printErrorChatMessage("Failed to calculate task");
            e.printStackTrace();
        }
        return null;
    }

    //---- Delay Runner ----//
    public static void addDelayTask(int delay, VoidTask task) {
        RepeatManager.instance.delayUnits.add(new DelayUnit(System.currentTimeMillis() + delay, task));
    }

    public static void addDelayTask(DelayUnit delayUnit) {
        RepeatManager.instance.delayUnits.add(delayUnit);
    }

    //---- Repeat Runner ----//
    public static void runRepeat(RepeatUnit unit) {
        registerRepeatUnit(unit);
    }

    public static void runRepeat(int delay, VoidTask task) {
        RepeatUnit unit = new RepeatUnit(delay, task);
        registerRepeatUnit(unit);
    }

    public static void runRepeat(int delay, int times, VoidTask task) {
        RepeatUnit unit = new RepeatUnit(delay, times, task);
        registerRepeatUnit(unit);
    }

    public static void runRepeat(IntSupplier delay, VoidTask task) {
        RepeatUnit unit = new RepeatUnit(delay, task);
        registerRepeatUnit(unit);
    }

    public static void runRepeat(IntSupplier delay, int times, VoidTask task) {
        RepeatUnit unit = new RepeatUnit(delay, times, task);
        registerRepeatUnit(unit);
    }

    public static void registerRepeatUnit(RepeatUnit repeatUnit) {
        RepeatManager.instance.repeatUnits.add(repeatUnit);
    }

    public static void unregisterRepeatUnit(RepeatUnit repeatUnit) {
        RepeatManager.instance.repeatUnits.remove(repeatUnit);
    }

    public static void repeat(int times, VoidTask task) {
        for (int i = 0; i < times; i++) {
            task.invoke();
        }
    }

    public static void stop() {
        try {
            instance.executor.shutdown();
        } catch (Exception ignore) {
            System.out.println("[TaskManager]TaskManager shut down!");
        }
    }

    //---- Background Stuff ----//
    public static void updateBackground() {
        RepeatManager.update();
    }
}
