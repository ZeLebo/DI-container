package team.factory;

import lombok.Getter;
import team.config.DefaultBeanDefinition;
import team.configurator.BeanConfigurator;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/*
    BeanFactory manages the beans themselves
 */
public class BeanFactory {
    private final Map<Class, DefaultBeanDefinition> singletonBeanMap = new ConcurrentHashMap<>();
    private final Map<Class, Map<Thread, DefaultBeanDefinition>> threadBeanMap = new ConcurrentHashMap<>();
    private final Map<Class, DefaultBeanDefinition> providedBeanMap = new ConcurrentHashMap<>();


    @Getter
    private final BeanConfigurator beanConfigurator;

    public BeanFactory(BeanConfigurator beanConfigurator) {
        this.beanConfigurator = beanConfigurator;
    }

    // check the bean in the map and generate if not exist
    public <T> T getBean(Class<T> tClass) {
        // check for the bean in map
        if (singletonBeanMap.containsKey(tClass)) {
            return (T) singletonBeanMap.get(tClass).getBean();
        }

        DefaultBeanDefinition bean = null;
        try {
            bean = beanConfigurator.generateBean(tClass);
        } catch (InstantiationException | InvocationTargetException | IllegalAccessException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
        switch (bean.getScope()) {
            case "singleton":
                singletonBeanMap.put(tClass, bean);
            case "thread":
                threadBeanMap.put(tClass, Map.of(Thread.currentThread(), bean));
            case "provided":
                providedBeanMap.put(tClass, bean);
        }

        return (T) bean.getBean();
    }

}
