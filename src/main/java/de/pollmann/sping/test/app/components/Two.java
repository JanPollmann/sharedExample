package de.pollmann.sping.test.app.components;

import de.pollmann.sping.test.app.AbstractComponent;
import de.pollmann.sping.test.app.MyComponent;
import de.pollmann.sping.test.app.Registry;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

@Log4j2
@Component
@MyComponent(componentName = "TWO")
@AllArgsConstructor
public class Two extends AbstractComponent {

    private final Registry registry;

    @Override
    public void loop() throws InterruptedException {
        Thread.sleep(2000);
    }

    @Override
    public String toString() {
        return "Two{" +
                "componentName='" + componentName + '\'' +
                ", objectIdentifier=" + objectIdentifier +
                ", registry=" + registry +
                '}';
    }
}
