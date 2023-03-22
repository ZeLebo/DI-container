package team.context;

import lombok.Setter;
import lombok.SneakyThrows;
import team.factory.BeanFactory;
import team.postprocessor.BeanPostProcessor;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ApplicationContext {
    @Setter
    private BeanFactory beanFactory;
    private final Map<Class, Object> beanMap = new ConcurrentHashMap<>();

    public ApplicationContext() {
    }

    public <T> T getBean(Class<T> tClass) {
        if (beanMap.containsKey((tClass))) {
            return (T) beanMap.get(tClass);
        }

        T bean = beanFactory.getBean(tClass);
        this.callPostProcessors(bean);

        beanMap.put(tClass, bean);

        return bean;
    }

    @SneakyThrows
    private void callPostProcessors(Object bean) {
        for(Class processor : beanFactory.getBeanConfigurator().getScanner().getSubTypesOf(BeanPostProcessor.class)) {
            BeanPostProcessor postProcessor = (BeanPostProcessor) processor.getDeclaredConstructor().newInstance();
            postProcessor.process(bean);
        }
    }
}



