package team.factory;

import lombok.SneakyThrows;
import team.annotations.Inject;
import team.config.Configuration;
import team.config.JavaConfiguration;
import team.configurator.BeanConfigurator;
import team.configurator.JavaBeanConfigurator;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.stream.Collectors;

//The BeanFactory is the actual container which instantiates, configures, and manages a number of beans.
public class BeanFactory {
//    singleton
    private static final BeanFactory BEAN_FACTORY = new BeanFactory();
    private final BeanConfigurator beanConfigurator;
    private final Configuration configuration;

//    for now we have config package, configurations will be stored as xml
    private BeanFactory() {
        this.configuration = new JavaConfiguration();
        this.beanConfigurator = new JavaBeanConfigurator(configuration.GetPackageToScan(), configuration.getInterfaceToImplementetions());
    }

    public static BeanFactory getInstance(){
        return BEAN_FACTORY;
    }
//    Add exceptions checking later
    @SneakyThrows
    public <T> T getBean(Class<T> tClass) {
//         check if tClass is interface then find suitable implementation
//         else tClass is just a class then only create it
        Class<? extends T> implementationClass = tClass;
        if (implementationClass.isInterface()) {
            implementationClass = beanConfigurator.getImplementationClass(implementationClass);
        }
        T bean = implementationClass.getDeclaredConstructor().newInstance();

//        go through the beans fields and see which dependencies need to be implemented
        for(Field field : Arrays.stream(implementationClass.getDeclaredFields()).filter(field -> field.isAnnotationPresent(Inject.class)).collect(Collectors.toList())) {
            field.setAccessible(true);
            field.set(bean, BEAN_FACTORY.getBean(field.getType()));
        }

        return bean;
    }
}
