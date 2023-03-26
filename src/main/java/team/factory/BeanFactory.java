package team.factory;

import lombok.Getter;
import lombok.SneakyThrows;
import team.annotations.Inject;
import team.config.Configuration;
import team.config.JavaConfiguration;
import team.configurator.BeanConfigurator;
import team.configurator.JavaBeanConfigurator;
import team.context.ApplicationContext;

import java.lang.reflect.Field;
import java.util.Arrays;

/*
    BeanFactory manages the beans themselves
 */
public class BeanFactory {
    @Getter
    private final BeanConfigurator beanConfigurator;
    private final ApplicationContext applicationContext;

    // for now, we have config package, configurations will be stored as xml
    public BeanFactory(ApplicationContext applicationContext) {
        Configuration configuration = new JavaConfiguration();
        this.beanConfigurator = new JavaBeanConfigurator(configuration.GetPackageToScan(), configuration.getInterfaceToImplementetions());
        this.applicationContext = applicationContext;
    }
    
    @SneakyThrows
    public <T> T getBean(Class<T> tClass) {
    // check if tClass is interface then find suitable implementation
    // else tClass is just a class then only create it
        Class<? extends T> implementationClass = tClass;
        if (implementationClass.isInterface()) {
            implementationClass = beanConfigurator.getImplementationClass(implementationClass);
        }
        T bean = implementationClass.getDeclaredConstructor().newInstance();

        // inject all the fields with annotation @inject
        for(Field field : Arrays.stream(implementationClass.getDeclaredFields()).filter(field -> field.isAnnotationPresent(Inject.class)).toList()) {
            field.setAccessible(true);
            field.set(bean, applicationContext.getBean(field.getType()));
        }

        return bean;
    }
}
