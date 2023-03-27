package team.configurator.metadata;

import lombok.Getter;
import lombok.SneakyThrows;
import org.reflections.Reflections;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import team.config.BeanDefinition;
import team.config.DefaultBeanDefinition;
import team.configurator.BeanConfigurator;
import team.factory.BeanFactory;

import javax.xml.parsers.DocumentBuilderFactory;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@SuppressWarnings("unused")
public class XMLBeanConfigurator implements BeanConfigurator {
    @Getter
    private Reflections scanner;
    private BeanFactory beanFactory;
    private String FILENAME = "beans.xml";
    // contains the map of ClassInterface to Class of implementations
    private final Map<Class, Class> interfaceToImplementation;
    // map for mapping naming (id in XML) to BeanDefinition
    private static final Map<String, BeanDefinition> beansToBeanDefinitions = new ConcurrentHashMap<>();
    // Map <BeanId> -> Map<field name, beanIdToResolve>
    private static final Map<String, Map<String, String>> injectsMap = new ConcurrentHashMap<>();

    private static final String BASE_PACKAGE_TAG = "base-package";
    private static final String BEAN_TAG = "bean";
    private static final String BEAN_NAME_ATTRIBUTE = "id";
    private static final String BEAN_CLASS_NAME_ATTRIBUTE = "class";
    private static final String SCOPE_ATTRIBUTE = "scope";
    private static final String PRIMARY_ATTRIBUTE = "primary";
    private static final String FACTORY_BEAN_ATTRIBUTE = "factory-bean";
    private static final String FACTORY_METHOD_ATTRIBUTE = "factory-method";
    private static final String INIT_METHOD_ATTRIBUTE = "init-method";
    private static final String DESTROY_METHOD_ATTRIBUTE = "destroy-method";
    private static final String CONSTRUCTOR_ARGUMENT_TAG = "constructor-arg";
    private static final String PROPERTY_TAG = "property";
    private static final String NAME_ATTRIBUTE = "name";
    private static final String REF_ATTRIBUTE = "ref";
    private static final String FIELD_ATTRIBUTE = "field";

    public XMLBeanConfigurator(String filename) {
        this.FILENAME = filename;
        this.interfaceToImplementation = new ConcurrentHashMap<>();
//        this.interfaceToImplementation.put(ServiceB.class, ServiceBImpl.class);
        this.parseXML();
    }

    public XMLBeanConfigurator(Map<Class, Class> interfaceToImplementation) {
        this.interfaceToImplementation = interfaceToImplementation;
    }

    public XMLBeanConfigurator(String filename, Map<Class, Class> interfaceToImplementation) {
        this.FILENAME = filename;
        this.interfaceToImplementation = interfaceToImplementation;
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }

    @SneakyThrows
    public void parseXML() {
        Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(this.FILENAME);
        parseBeans(document.getElementsByTagName(BEAN_TAG));
    }

    public void parseBeans(NodeList nodeBeans) {
        if (nodeBeans.getLength() == 0) {
            throw new RuntimeException("No beans found in " + FILENAME);
        }
        for (int i = 0; i < nodeBeans.getLength(); i++) {
            Element bean = (Element) nodeBeans.item(i);
            BeanDefinition beanDefinition = new DefaultBeanDefinition();
            if (bean.hasAttribute(BEAN_CLASS_NAME_ATTRIBUTE)) {
                beanDefinition.setBeanClassName(bean.getAttribute(BEAN_CLASS_NAME_ATTRIBUTE));
            }
            if (bean.hasAttribute(SCOPE_ATTRIBUTE)) {
                beanDefinition.setScope(bean.getAttribute("scope"));
            }

            if (!bean.hasAttribute("id")) {
                throw new RuntimeException("Some beans don't have id");
            }

            beansToBeanDefinitions.put(bean.getAttribute("id"), beanDefinition);
        }
        parseImplementations(nodeBeans);
        parseInjects(nodeBeans);
    }

    @SneakyThrows
    public void parseImplementations(NodeList nodeBeans) {
        if (nodeBeans.getLength() == 0) {
            throw new RuntimeException("No beans found in " + FILENAME);
        }

        for (int i = 0; i < nodeBeans.getLength(); i++) {
            Element bean = (Element) nodeBeans.item(i);
            if (bean.hasAttribute(REF_ATTRIBUTE)) {
                interfaceToImplementation.put(
                        Class.forName(bean.getAttribute(BEAN_CLASS_NAME_ATTRIBUTE)),
                        Class.forName(beansToBeanDefinitions.get(bean.getAttribute(REF_ATTRIBUTE)).getBeanClassName())
                );
            }
        }
    }

    @SneakyThrows
    public void parseInjects(NodeList nodeBeans) {
        // following all beans
        for (int i = 0; i < nodeBeans.getLength(); i++) {
            Element bean = (Element) nodeBeans.item(i);
            NodeList injects = bean.getElementsByTagName(CONSTRUCTOR_ARGUMENT_TAG);
            if (injects.getLength() == 0) {
                continue;
            }
            // create a map of all inject to stuff
            Map<String, String> fieldBeanPair = new ConcurrentHashMap<>();
            for (int j = 0; j < injects.getLength(); j++) {
                Element beanToInject = (Element) injects.item(j);
                String fieldName = beanToInject.getAttribute(FIELD_ATTRIBUTE);
                String beanImpl = beanToInject.getAttribute(REF_ATTRIBUTE);
                fieldBeanPair.put(fieldName, beanImpl);
            }
            injectsMap.put(
                    bean.getAttribute(BEAN_CLASS_NAME_ATTRIBUTE),
                    fieldBeanPair
            );

        }
    }

    @Override
    public <T> Class getImplementationClass(Class<T> interfaceClass) {
        // return class from interfaceToImplementation map
        // if class doesn't exist in map -> throw exception
        if (interfaceToImplementation.containsKey(interfaceClass)) {
            return interfaceToImplementation.get(interfaceClass);
        } else {
            throw new RuntimeException("No implementation for " + interfaceClass.getName());
        }

    }


    @SneakyThrows
    @Override
    public <T> DefaultBeanDefinition generateBean(Class<T> tClass) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        if (tClass.isInterface())  {
            tClass = (Class<T>) this.getImplementationClass(tClass);
        }
        T bean = tClass.getDeclaredConstructor().newInstance();

        // inject all the dependencies
        Map<String, String> toInject = injectsMap.get(tClass.getName());
        if (toInject != null) {
            // have something to inject
            for (Field field: tClass.getDeclaredFields()) {
                String beanId = toInject.get(field.getName());
                if (beanId != null) {
                    // need to inject in this field
                    field.setAccessible(true);
                    field.set(bean, this.beanFactory.getBean(
                            Class.forName(beansToBeanDefinitions.get(beanId).getBeanClassName())
                    ));
                }
            }
        }

        DefaultBeanDefinition tmp = new DefaultBeanDefinition();
        tmp.setBean(bean);
        tmp.setBeanClassName(bean.getClass().getName());
        tClass.getMethods();

        // get bean's scope
        for (Map.Entry<String, BeanDefinition> entry : beansToBeanDefinitions.entrySet()) {
            if (entry.getValue().getBeanClassName().equals(tClass.getName())) {
                tmp.setScope(entry.getValue().getScope());
            }
        }
        if (tmp.getScope() == null) {
            tmp.setScope("singleton");
        }

        return tmp;
    }
}
