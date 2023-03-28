package team.configurator.metadata;

import lombok.Getter;
import org.reflections.Reflections;
import team.annotations.*;
import team.annotations.Thread;
import team.config.BeanDefinition;
import team.config.DefaultBeanDefinition;
import team.configurator.BeanConfigurator;
import team.factory.BeanFactory;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class JavaBeanConfigurator implements BeanConfigurator {
    @Getter
    private Reflections scanner;
    private final Map<Class, Class> interfaceToImplementation;
    private static final Map<String, BeanDefinition> beansToBeanDefinitions = new ConcurrentHashMap<>();
    private BeanFactory beanFactory;

    @Override
    public void setBeanFactory(BeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }

    public JavaBeanConfigurator(String packageToScan) {
        this.scanner = new Reflections(packageToScan);
        this.interfaceToImplementation = new ConcurrentHashMap<>();
    }


    @Override
    public <T> BeanDefinition generateBean(Class<T> tClass) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        Class tmp = tClass;
        if (tClass.isInterface()) {
            tClass = (Class<T>) this.getImplementationClass(tClass);
        }
        T bean = tClass.getDeclaredConstructor().newInstance();
        BeanDefinition beanDefinition = new DefaultBeanDefinition();
        beanDefinition.setBeanClassName(bean.getClass().getName());
        beanDefinition.setScope("singleton");
        if (tClass.isAnnotationPresent(Thread.class)) {
            beanDefinition.setScope("thread");
        } else if (tClass.isAnnotationPresent(Provided.class)) {
            beanDefinition.setScope("provided");
        }

        // to be able to connect it somewhere
        beanDefinition.setBean(bean);
        this.beanFactory.addBean(tmp, beanDefinition);


        // inject all the fields with annotation @inject
        for (Field field : Arrays.stream(tClass.getDeclaredFields()).filter(field -> field.isAnnotationPresent(Inject.class)).toList()) {
            field.setAccessible(true);
            field.set(bean, this.beanFactory.getBean(field.getType()));
        }

        beanDefinition.setBean(bean);

        this.callPostProcessor(bean);

        return beanDefinition;

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

    @Override
    // Class<? extends T>
    public <T> Class getImplementationClass(Class<T> interfaceClass) {
        return interfaceToImplementation.computeIfAbsent(interfaceClass, tClass -> {
            Set<Class<? extends T>> implementationClasses = scanner.getSubTypesOf(interfaceClass);
            // if many classes are implement interface choose one with @Service
            if (implementationClasses.size() != 1) {
                return implementationClasses.stream()
                        .filter(aClass -> aClass.isAnnotationPresent(Service.class))
                        .findFirst()
                        .orElseThrow(() -> new RuntimeException("No or more than one implementation for " + interfaceClass.getName()));
            }
            return implementationClasses.stream().findFirst().get();
        });
    }
}
