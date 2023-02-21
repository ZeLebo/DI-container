package team.factory;

import team.configuration.Configuration;
import team.configuration.impl.JavaConfiguration;
import team.configurator.BeanConfigurator;
import team.configurator.impl.JavaBeanConfigurator;

import java.lang.reflect.InvocationTargetException;

public class BeanFactory {
    private static final BeanFactory BEAN_FACTORY = new BeanFactory();
    private final Configuration configuration;
    private final BeanConfigurator beanConfigurator;

    private BeanFactory() {
        this.configuration = new JavaConfiguration();
        this.beanConfigurator = new JavaBeanConfigurator(configuration.getPackageToScan());
    }

    public static BeanFactory getInstance() {
        return BEAN_FACTORY;
    }

    public <T> T getBean(Class<T> clazz) {
        Class<? extends T> implementationClass = clazz;
        if (implementationClass.isInterface()) {
            implementationClass = beanConfigurator.getImplementationClass(implementationClass);
        }

        try {
            return implementationClass.getDeclaredConstructor().newInstance();
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }
}
