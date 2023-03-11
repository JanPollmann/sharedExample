package de.pollmann.sping.test.app;

public abstract class AbstractComponent extends UniqueClasses {

    protected final String componentName = getClass().getAnnotation(MyComponent.class).componentName();

    protected AbstractComponent() {
        super();
    }

    public void load() {

    }

    public void loop() throws InterruptedException {

    }

}
