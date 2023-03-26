package team.factory;

import lombok.Getter;
import team.annotations.Inject;
import team.annotations.PostConstruct;
import team.config.Configuration;
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

    public BeanFactory(ApplicationContext applicationContext, String packageToScan) {
        this.applicationContext = applicationContext;
        this.beanConfigurator = new JavaBeanConfigurator(packageToScan);
    }

    // check the bean in the map and generate if not exist
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

        singletonBeanMap.put(tClass, bean);

        return bean;
    }

    private <T> T generateBean(Class<T> tClass) throws InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        if (tClass.isInterface()) {
            // find all the classes that are subtypes
            tClass = (Class<T>) beanConfigurator.getImplementationClass(tClass);
        }
        T bean = tClass.getDeclaredConstructor().newInstance();

        // put in the map

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
