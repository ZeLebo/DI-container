package team.configurator.metadata;

import lombok.Getter;
import org.reflections.Reflections;
import org.w3c.dom.Document;
import team.config.DefaultBeanDefinition;
import team.configurator.BeanConfigurator;
import team.factory.BeanFactory;

import javax.xml.parsers.DocumentBuilderFactory;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class XMLBeanConfigurator implements BeanConfigurator {
    @Getter
    private Reflections scanner;
    private String FILENAME = "beans.xml";
    private final Map<Class, Class> interfaceToImplementation;
    private BeanFactory beanFactory;

    private static final String BASE_PACKAGE_TAG = "base-package";
    private static final String BEAN_TAG = "bean";
    private static final String BEAN_NAME_ATTRIBUTE = "id";
    private static final String BEAN_CLASS_NAME_ATTRIBUTE = "class";
    private static final String SCOPE_ATTRIBUTE = "scope";
    private static final String LAZY_INIT_ATTRIBUTE = "lazy-init";
    private static final String PRIMARY_ATTRIBUTE = "primary";
    private static final String FACTORY_BEAN_ATTRIBUTE = "factory-bean";
    private static final String FACTORY_METHOD_ATTRIBUTE = "factory-method";
    private static final String INIT_METHOD_ATTRIBUTE = "init-method";
    private static final String DESTROY_METHOD_ATTRIBUTE = "destroy-method";
    private static final String CONSTRUCTOR_ARGUMENT_TAG = "constructor-arg";
    private static final String PROPERTY_TAG = "property";
    private static final String NAME_ATTRIBUTE = "name";
    private static final String REF_ATTRIBUTE = "ref";

    public XMLBeanConfigurator(String filename) {
        this.FILENAME = filename;
        this.interfaceToImplementation = new ConcurrentHashMap<>();
    }

    public XMLBeanConfigurator(Map<Class, Class> interfaceToImplementation) {
        this.interfaceToImplementation = interfaceToImplementation;
    }

    public XMLBeanConfigurator(String filename, Map<Class, Class> interfaceToImplementation) {
        this.FILENAME = filename;
        this.interfaceToImplementation = interfaceToImplementation;
    }

    public void parseXML() {
        // parse xml file and put classes in map
        try {
            Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(this.FILENAME);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public <T> Class<? extends T> getImplementationClass(Class<T> interfaceClass) {
        // return class from interfaceToImplementation map
        // if class doesn't exist in map -> throw exception
        if (interfaceToImplementation.containsKey(interfaceClass)) {
            return interfaceToImplementation.get(interfaceClass);
        } else {
            throw new RuntimeException("No implementation for " + interfaceClass.getName());
        }

    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }

    @Override
    public <T> DefaultBeanDefinition generateBean(Class<T> tClass) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        return null;
    }
}
