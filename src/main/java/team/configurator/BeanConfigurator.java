package team.configurator;

import org.reflections.Reflections;
import team.config.DefaultBeanDefinition;
import team.factory.BeanFactory;

import java.lang.reflect.InvocationTargetException;

public interface BeanConfigurator {
    <T> Class<? extends T> getImplementationClass(Class<T> interfaceClass);
    Reflections getScanner();

    void setBeanFactory(BeanFactory beanFactory);

    <T> DefaultBeanDefinition generateBean(Class<T> tClass) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException;
}
