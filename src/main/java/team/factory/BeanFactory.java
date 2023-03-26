package team.factory;

import lombok.Getter;
import lombok.SneakyThrows;
import team.annotations.Inject;
import team.annotations.PostConstruct;
import team.config.Configuration;
import team.config.JavaConfiguration;
import team.configurator.BeanConfigurator;
import team.configurator.metadata.JavaBeanConfigurator;
import team.context.ApplicationContext;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/*
    BeanFactory manages the beans themselves
 */
public class BeanFactory {
    private final Map<Class, Object> singletonBeanMap = new ConcurrentHashMap<>();
    private final Map<Class, Object> threadBeanMap = new ConcurrentHashMap<>();


    @Getter
    private final BeanConfigurator beanConfigurator;
    private final ApplicationContext applicationContext;

    // for now, we have config package, configurations will be stored as xml
    public BeanFactory(ApplicationContext applicationContext) {
        Configuration configuration = new JavaConfiguration();
        this.beanConfigurator = new JavaBeanConfigurator(configuration.GetPackageToScan(), configuration.getInterfaceToImplementetions());
        this.applicationContext = applicationContext;
    }

    public BeanFactory(ApplicationContext applicationContext, Configuration configuration) {
        this.applicationContext = applicationContext;
        this.beanConfigurator = new JavaBeanConfigurator(configuration.GetPackageToScan(), configuration.getInterfaceToImplementetions());
    }
    
    public <T> T getBean(Class<T> tClass) {
        // check for the bean in map
        if (singletonBeanMap.containsKey(tClass)) {
            return (T) singletonBeanMap.get(tClass);
        }

        T bean = null;
        try {
            bean = generateBean(tClass);
        } catch (InstantiationException | InvocationTargetException | IllegalAccessException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
        this.callPostProcessor(bean);

        return bean;
    }

    private <T> T generateBean(Class<T> tClass) throws InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        if (tClass.isInterface()) {
            // find all the classes that are subtypes
            tClass = (Class<T>) beanConfigurator.getImplementationClass(tClass);
        }
        T bean = tClass.getDeclaredConstructor().newInstance();

        // inject all the fields with annotation @inject
        for (Field field : Arrays.stream(tClass.getDeclaredFields()).filter(field -> field.isAnnotationPresent(Inject.class)).toList()) {
            field.setAccessible(true);
            field.set(bean, applicationContext.getBean(field.getType()));
        }

        return bean;
    }

    private void callPostProcessor(Object bean) {
        for (Method method: bean.getClass().getMethods()) {
            if (method.isAnnotationPresent(PostConstruct.class)) {
                try {
                    method.invoke(bean);
                } catch (IllegalAccessException | InvocationTargetException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}
