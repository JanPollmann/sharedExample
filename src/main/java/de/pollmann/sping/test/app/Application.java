package de.pollmann.sping.test.app;

import lombok.NonNull;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Log4j2
@Component
public class Application extends UniqueClasses {

    private final Registry registry;
    private final ApplicationContext applicationContext;
    private final Map<String, AbstractComponent> componentByName = new HashMap<>();

    private Watchdog watchdog;

    public Application(@NonNull Registry registry, @NonNull ApplicationContext applicationContext) {
        this.registry = registry;
        this.applicationContext = applicationContext;
    }

    public void run(String... args) throws Exception {
        Objects.requireNonNull(applicationContext);
        watchdog = new Watchdog(Thread.currentThread());
        log.info("Run application: {}", this);
        try {
            applicationLifecycle();
        } finally {
            watchdog.shutdownNow();
        }
    }

    private void applicationLifecycle() {
        componentScan();
        loadComponents();
        log.info("Loop components ...");
        for (int i = 0; i < 10; i++) {
            loop();
        }
    }

    private void loop() {
        for (var entry : componentByName.entrySet()) {
            String componentName = entry.getKey();
            AbstractComponent component = entry.getValue();
            watchdog.start(componentName);
            try {
                component.loop();
                if (Thread.interrupted() || watchdog.targetReached()) {
                    throw new ApplicationInterruptedException("Loop interrupted from Application itself");
                }
            } catch (ApplicationInterruptedException applicationInterruptedException) {
                log.fatal("Performance problem with component '{}':", componentName, applicationInterruptedException);
            } catch (InterruptedException interruptedException) {
                log.warn("Performance problem with component '{}', Interrupted from Application Watchdog:", componentName, interruptedException);
            } finally {
                watchdog.stop();
            }
        }
    }

    private void loadComponents() {
        log.info("Load components ...");
        componentByName.forEach((name, component) -> component.load());
    }

    private void componentScan() {
        var components = applicationContext.getBeansWithAnnotation(MyComponent.class);
        for (var component : components.entrySet()) {
            componentByName.put(component.getKey(), (AbstractComponent) component.getValue());
        }
        log.info("components: {}", componentByName);
    }

    @Override
    public String toString() {
        return "Application{" +
                "registry=" + registry +
                ", objectIdentifier=" + objectIdentifier +
                '}';
    }

    private static class ApplicationInterruptedException extends InterruptedException {
        public ApplicationInterruptedException(String message) {
            super(message);
        }
    }
}
