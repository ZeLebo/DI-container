package team.config;

import java.util.Map;

public interface Configuration {

    Map<Class, Class> getInterfaceToImplementations();

    void addInterfaceToImplementations(Class interfaceClass, Class implementation);
}
