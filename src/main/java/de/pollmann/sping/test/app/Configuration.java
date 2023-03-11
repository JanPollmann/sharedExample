package de.pollmann.sping.test.app;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

@Log4j2
@Component
@AllArgsConstructor
public class Configuration extends UniqueClasses {
    @Override
    public String toString() {
        return "Configuration{" +
                "objectIdentifier=" + objectIdentifier +
                '}';
    }
}
