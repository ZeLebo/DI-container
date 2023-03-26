package team.factory;

import lombok.Getter;
import team.config.BeanDefinition;
import team.config.DefaultBeanDefinition;
import team.configurator.BeanConfigurator;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/*
    BeanFactory manages the beans themselves
 */
public class BeanFactory {
    private final Map<Class, BeanDefinition> singletonBeanMap = new ConcurrentHashMap<>();
    private final Map<Class, Map<Thread, BeanDefinition>> threadBeanMap = new ConcurrentHashMap<>();
    private final Map<Class, BeanDefinition> providedBeanMap = new ConcurrentHashMap<>();


    @Getter
    private final BeanConfigurator beanConfigurator;

    public BeanFactory(BeanConfigurator beanConfigurator) {
        this.beanConfigurator = beanConfigurator;
    }

    // check the bean in the map and generate if not exist
    public <T> T getBean(Class<T> tClass) throws InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        // check for the bean in map
        if (singletonBeanMap.containsKey(tClass)) {
            return (T) singletonBeanMap.get(tClass).getBean();
        }
        if (threadBeanMap.containsKey(tClass)) {
            if (threadBeanMap.get(tClass).containsKey(Thread.currentThread())) {
                return (T) threadBeanMap.get(tClass).get(Thread.currentThread()).getBean();
            }
        }
        if (providedBeanMap.containsKey(tClass)) {
            return (T) providedBeanMap.get(tClass).getBean().getClass().getDeclaredConstructor().newInstance();
        }

        BeanDefinition bean = null;
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
