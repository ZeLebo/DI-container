package team.context;

import lombok.Setter;
import team.configurator.BeanConfigurator;
import team.configurator.metadata.JavaBeanConfigurator;
import team.configurator.metadata.XMLBeanConfigurator;
import team.factory.BeanFactory;

import java.io.File;
import java.lang.reflect.InvocationTargetException;

public class ApplicationContext {
    @Setter
    private BeanFactory beanFactory;

    public ApplicationContext(String packageToScan) {
        BeanConfigurator beanConfigurator = null;
        if (packageToScan.endsWith(".xml")) {
            File file = new File(packageToScan);
            if (!file.exists()) {
                throw new RuntimeException("File " + packageToScan + " doesn't exist");
            }
            if (!file.isFile()) {
                throw new RuntimeException(packageToScan + " is not a file");
            }
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
        try {
            return beanFactory.getBean(tClass);
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



