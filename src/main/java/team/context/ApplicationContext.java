package team.context;

import lombok.Setter;
import team.configurator.BeanConfigurator;
import team.configurator.metadata.JavaBeanConfigurator;
import team.configurator.metadata.XMLBeanConfigurator;
import team.factory.BeanFactory;

public class ApplicationContext {
    @Setter
    private BeanFactory beanFactory;

    public ApplicationContext(String packageToScan) {
        BeanConfigurator beanConfigurator = null;
        if (packageToScan.endsWith(".xml")) {
            beanConfigurator = new XMLBeanConfigurator(packageToScan);
        } else {
            beanConfigurator = new JavaBeanConfigurator(packageToScan);

        }
        BeanFactory beanFactory = new BeanFactory(beanConfigurator);
        beanConfigurator.setBeanFactory(beanFactory);
        this.setBeanFactory(beanFactory);
    }

    // get the bean from map, if the bean doesn't exist -> create and return
    public <T> T getBean(Class<T> tClass) {
        return beanFactory.getBean(tClass);
    }
}



