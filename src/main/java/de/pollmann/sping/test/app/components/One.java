package de.pollmann.sping.test.app.components;

import de.pollmann.sping.test.app.AbstractComponent;
import de.pollmann.sping.test.app.MyComponent;
import de.pollmann.sping.test.app.Registry;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

@Log4j2
@Component
@MyComponent(componentName = "ONE")
public class One extends AbstractComponent {

    private final Registry registry;

    private int i;

    public One(@NonNull Registry registry) {
        this.registry = registry;
    }

    @Override
    public void loop() throws InterruptedException {
        i = 0;
        sleeping();
    }

    private void sleeping() {
        try {
            iWantToSleep();
        } catch (InterruptedException interruptedException) {
            sleeping();
        }
    }

    private void iWantToSleep() throws InterruptedException {
        i++;
        long sleep = 10 - i;
        if (sleep > 0) {
            Thread.sleep(sleep * 1000);
        }
    }

    @Override
    public String toString() {
        return "One{" +
                "componentName='" + componentName + '\'' +
                ", objectIdentifier=" + objectIdentifier +
                ", registry=" + registry +
                '}';
    }
}
