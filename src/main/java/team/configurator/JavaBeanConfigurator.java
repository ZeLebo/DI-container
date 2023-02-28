package team.configurator;

import org.reflections.Reflections;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class JavaBeanConfigurator implements BeanConfigurator {

    //    Convenient tool for scanning packages from lib org.reflection
    private final Reflections scanner;
    private final Map<Class, Class> interfaceToImplementation;


    public JavaBeanConfigurator(String packageToScan, Map<Class, Class> interfaceToImplementation) {
        this.scanner = new Reflections(packageToScan);
        this.interfaceToImplementation = new ConcurrentHashMap<>(interfaceToImplementation);
    }

    @Override
    public <T> Class<? extends T> getImplementationClass(Class<T> interfaceClass) {
        return interfaceToImplementation.computeIfAbsent(interfaceClass, tClass -> {
            Set<Class<? extends T>> implementetionClasses = scanner.getSubTypesOf(interfaceClass);
            if (implementetionClasses.size() != 1) {
                throw new RuntimeException("0 or more than 1 implementations");
            }
            return implementetionClasses.stream().findFirst().get();
        });
    }
}
