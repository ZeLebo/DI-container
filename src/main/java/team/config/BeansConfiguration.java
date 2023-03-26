package team.config;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class BeansConfiguration implements Configuration {

    private final Map<Class, Class> implementations = new ConcurrentHashMap<>();

    // this map has to be collected in xml
    @Override
    public Map<Class, Class> getInterfaceToImplementations() {
        return this.implementations;
    }

    @Override
    public void addInterfaceToImplementations(Class interfaceClass, Class implementation) {
        this.implementations.put(interfaceClass, implementation);
    }
}
