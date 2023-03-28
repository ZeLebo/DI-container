package team.factory;

import lombok.Getter;
import team.config.BeanDefinition;
import team.config.DefaultBeanDefinition;
import team.configurator.BeanConfigurator;

import java.lang.reflect.InvocationTargetException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/*
    BeanFactory manages the beans themselves
 */
public class BeanFactory {
    // do I need here BeanDefinition?
    private final Map<Class, BeanDefinition> singletonBeanMap = new ConcurrentHashMap<>();
    private final Map<Class, Map<Thread, BeanDefinition>> threadBeanMap = new ConcurrentHashMap<>();
    private final Map<Class, BeanDefinition> providedBeanMap = new ConcurrentHashMap<>();
    private final Set<Class> queueForGenerate = new HashSet<>();


    @Getter
    private final BeanConfigurator beanConfigurator;

    public BeanFactory(BeanConfigurator beanConfigurator) {
        this.beanConfigurator = beanConfigurator;
    }

    public synchronized void addBean(Class clz, BeanDefinition beanDefinition) {
        switch (beanDefinition.getScope()) {
            case "singleton" -> singletonBeanMap.put(clz, beanDefinition);
            case "thread" -> threadBeanMap.put(clz, Map.of(Thread.currentThread(), beanDefinition));
            case "provided" -> providedBeanMap.put(clz, beanDefinition);
        }

    }

    // check the bean in the map and generate if not exist
    public synchronized <T> T getBean(Class<T> tClass) throws InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
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
            if (queueForGenerate.contains(tClass)) {
                throw new RuntimeException("Cyclic dependency");
            }
            queueForGenerate.add(tClass);
            bean = beanConfigurator.generateBean(tClass);
            queueForGenerate.remove(tClass);
        } catch (InstantiationException | InvocationTargetException | IllegalAccessException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }

        this.addBean(tClass, bean);

        return (T) bean.getBean();
    }

}
