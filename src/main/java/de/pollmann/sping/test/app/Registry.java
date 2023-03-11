package de.pollmann.sping.test.app;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

@Log4j2
@Component
@AllArgsConstructor
public class Registry extends UniqueClasses {

    @Getter
    private final Configuration configuration;

    @Override
    public String toString() {
        return "Registry{" +
                "configuration=" + configuration +
                ", objectIdentifier=" + objectIdentifier +
                '}';
    }

}
