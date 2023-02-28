package team.configurator;

import org.reflections.Reflections;

import java.util.Set;

public class JavaBeanConfigurator implements BeanConfigurator {

//    Convenient tool for scanning packages from lib org.reflection
    final Reflections scanner;

    public JavaBeanConfigurator(String packageToScan) {
        this.scanner = new Reflections(packageToScan);
    }

    @Override
    public <T> Class<? extends T> getImplementationClass(Class<T> interfaceClass) {
        Set<Class<? extends T>> implementetionClasses = scanner.getSubTypesOf(interfaceClass);
        if(implementetionClasses.size() !=1) {
            throw  new RuntimeException("0 or more than 1 implementations");
        }
        return implementetionClasses.stream().findFirst().get();
    }
}
