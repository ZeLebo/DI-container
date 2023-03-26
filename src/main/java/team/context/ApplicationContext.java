package team.context;

import lombok.Setter;
import lombok.SneakyThrows;
import team.annotations.PostConstruct;
import team.factory.BeanFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ApplicationContext {
    @Setter
    private BeanFactory beanFactory;
    // todo change something to beanDefinition (use it as proxy)
    private final Map<Class, Object> singletonBeanMap = new ConcurrentHashMap<>();
    private final Map<Class, Object> threadBeanMap = new ConcurrentHashMap<>();

    public ApplicationContext() {
        BeanFactory beanFactory = new BeanFactory(this);
        this.setBeanFactory(beanFactory);
    }

    // get the bean from map, if the bean doesn't exist -> create and return
    public <T> T getBean(Class<T> tClass) {
        if (singletonBeanMap.containsKey((tClass))) {
            return (T) singletonBeanMap.get(tClass);
        }

        T bean = beanFactory.getBean(tClass);
        this.callPostProcessors(bean);

        singletonBeanMap.put(tClass, bean);

        return bean;
    }

    @SneakyThrows
    private void callPostProcessors(Object bean) {
        for (var method : bean.getClass().getMethods()) {
            if (method.isAnnotationPresent(PostConstruct.class)) {
                method.invoke(bean);
            }
        }
    }
}



