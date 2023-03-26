package team.configurator.metadata;

import lombok.Getter;
import org.reflections.Reflections;
import team.annotations.Service;
import team.configurator.BeanConfigurator;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class JavaBeanConfigurator implements BeanConfigurator {
    @Getter
    private Reflections scanner;
    private final Map<Class, Class> interfaceToImplementation;

    public JavaBeanConfigurator(Map<Class, Class> implementations) {
        this.interfaceToImplementation = implementations;
    }

    public JavaBeanConfigurator(String packageToScan) {
        this.scanner = new Reflections(packageToScan);
        this.interfaceToImplementation = new ConcurrentHashMap<>();
    }

    public void setPackageToScan(String packageToScan) {
        this.scanner = new Reflections(packageToScan);
    }

    public JavaBeanConfigurator(String packageToScan, Map<Class, Class> interfaceToImplementation) {
        this.scanner = new Reflections(packageToScan);
        this.interfaceToImplementation = new ConcurrentHashMap<>(interfaceToImplementation);
    }

    @Override
    public <T> Class<? extends T> getImplementationClass(Class<T> interfaceClass) {
        return interfaceToImplementation.computeIfAbsent(interfaceClass, tClass -> {
            Set<Class<? extends T>> implementationClasses = scanner.getSubTypesOf(interfaceClass);
            // if many classes are implement interface choose one with @Service
            if (implementationClasses.size() != 1) {
                // stream api
                for (Class cls : implementationClasses) {
                    if (cls.isAnnotationPresent(Service.class)) {
                        return cls;
                    }
                }

                throw new RuntimeException("0 or more than 1 implementation");
            }
            return implementationClasses.stream().findFirst().get();
        });
    }
}
