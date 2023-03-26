package team.configurator.metadata;

import org.reflections.Reflections;
import team.configurator.BeanConfigurator;

public class XMLBeanConfigurator implements BeanConfigurator {
    @Override
    public <T> Class<? extends T> getImplementationClass(Class<T> interfaceClass) {
        return null;
    }

    @Override
    public Reflections getScanner() {
        return null;
    }
}
