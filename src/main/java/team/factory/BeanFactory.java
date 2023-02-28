package team.factory;

import lombok.SneakyThrows;
import team.config.Configuration;
import team.config.JavaConfiguration;
import team.configurator.BeanConfigurator;
import team.configurator.JavaBeanConfigurator;


//The BeanFactory is the actual container which instantiates, configures, and manages a number of beans.
public class BeanFactory {
//    singleton
    private static final BeanFactory BEAN_FACTORY = new BeanFactory();
    private final BeanConfigurator beanConfigurator;
    private final Configuration configuration;

//    for now we have config package, configurations will be stored as xml
    private BeanFactory() {
        this.configuration = new JavaConfiguration();
        this.beanConfigurator = new JavaBeanConfigurator(configuration.GetPackageToScan());
    }

    public static BeanFactory getInstance(){
        return BEAN_FACTORY;
    }
//    Add exceptions checking later
    @SneakyThrows
    public <T> T getBean(Class<T> tClass) {
//         check if tClass is interface then find suitable implementation
//         else tClass is just a class then only create it
        Class<? extends T> implementationClass = tClass;
        if (implementationClass.isInterface()) {
            implementationClass = beanConfigurator.getImplementationClass(implementationClass);
        }
        return implementationClass.getDeclaredConstructor().newInstance();
    }
}
