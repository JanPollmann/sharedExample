package de.pollmann.sping.test.app;

import lombok.Getter;
import lombok.extern.log4j.Log4j2;

@Log4j2
public abstract class UniqueClasses {

    private static int ID = 0;

    @Getter
    protected final int objectIdentifier;

    protected UniqueClasses() {
        objectIdentifier = ID++;
        log.info("Created {}: {}", getClass().getSimpleName(), objectIdentifier);
    }

    @Override
    public abstract String toString();

}
