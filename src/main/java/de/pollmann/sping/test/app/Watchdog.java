package de.pollmann.sping.test.app;

import lombok.extern.log4j.Log4j2;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Log4j2
public class Watchdog {

    private final ScheduledExecutorService watchdog = Executors.newScheduledThreadPool(1, r -> {
        Thread thread = new Thread(r, "Watchdog");
        thread.setDaemon(true);
        return thread;
    });
    private final Thread toWatch;
    private final Watcher watcher;

    private volatile String componentName;
    private int interruptAttempts;

    public Watchdog(Thread toWatch) {
        this.toWatch = toWatch;
        watcher = new Watcher(Duration.ofMillis(1000), this::timeout);
        watchdog.scheduleAtFixedRate(watcher::watch, 500, 500, TimeUnit.MILLISECONDS);
    }

    private void timeout() {
        interruptAttempts++;
        log.fatal("Component '{}' timed out - Interrupt main Thread{}", componentName, interruptAttempts > 1 ? String.format(" (%s)", interruptAttempts) : "");
        toWatch.interrupt();
    }

    public void start(String componentName) {
        this.componentName = componentName;
        interruptAttempts = 0;
        watcher.start();
    }

    public void stop() {
        watcher.stop();
        componentName = "";
    }

    public boolean targetReached() {
        return watcher.targetReached();
    }

    public List<Runnable> shutdownNow() {
        return watchdog.shutdownNow();
    }

    private static class Watcher {

        private final long targetInNs;
        private final Runnable onTargetReached;

        private long limitInNs = Long.MAX_VALUE;

        public Watcher(Duration targetDuration, Runnable onTargetReached) {
            this.targetInNs = targetDuration.toNanos();
            this.onTargetReached = onTargetReached;
        }

        public void start() {
            limitInNs = System.nanoTime() + targetInNs;
        }

        public void stop() {
            limitInNs = Long.MAX_VALUE;
        }

        public void watch() {
            if (targetReached()) {
                onTargetReached.run();
            }
        }

        private boolean targetReached() {
            return System.nanoTime() >= limitInNs;
        }

    }

}
