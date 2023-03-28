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
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class XMLBeanConfigurator implements BeanConfigurator {
    @Getter
    private Reflections scanner;
    private BeanFactory beanFactory;
    private String FILENAME;
    // map for mapping naming (id in XML) to BeanDefinition
    private static final Map<String, BeanDefinition> beansToBeanDefinitions = new ConcurrentHashMap<>();
    // Map <BeanId> -> Map<field name, beanIdToResolve>
    private static final Map<String, String> nameToReference = new ConcurrentHashMap<>();

    private static final String BASE_PACKAGE_TAG = "base-package";
    private static final String BEAN_TAG = "bean";
    private static final String BEAN_NAME_ATTRIBUTE = "id";
    private static final String BEAN_CLASS_NAME_ATTRIBUTE = "class";
    private static final String SCOPE_ATTRIBUTE = "scope";
    private static final String CONSTRUCTOR_ARGUMENT_TAG = "constructor-arg";
    private static final String REF_ATTRIBUTE = "ref";
    private static final String FIELD_ATTRIBUTE = "field";

    public XMLBeanConfigurator(String filename) {
        this.FILENAME = filename;
        this.parseXML();
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
                beanDefinition.setScope(bean.getAttribute(SCOPE_ATTRIBUTE));
            } else {
                beanDefinition.setScope("singleton");
            }

            if (!bean.hasAttribute(BEAN_NAME_ATTRIBUTE)) {
                throw new RuntimeException("Some beans don't have id");
            }

            nameToReference.put(
                    bean.getAttribute(BEAN_NAME_ATTRIBUTE),
                    bean.getAttribute(BEAN_CLASS_NAME_ATTRIBUTE)
            );
            beansToBeanDefinitions.put(bean.getAttribute(BEAN_CLASS_NAME_ATTRIBUTE), beanDefinition);
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
                String path = nameToReference.get(bean.getAttribute(REF_ATTRIBUTE));
                // put info about implementation
                BeanDefinition beanDefinition = beansToBeanDefinitions.get(bean.getAttribute(BEAN_CLASS_NAME_ATTRIBUTE));
                beanDefinition.setImplementation(
                        Class.forName(beansToBeanDefinitions.get(path).getBeanClassName())
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
            for (int j = 0; j < injects.getLength(); j++) {
                Element beanToInject = (Element) injects.item(j);
                String fieldName = beanToInject.getAttribute(FIELD_ATTRIBUTE);
                String beanImpl = beanToInject.getAttribute(REF_ATTRIBUTE);
                String path = nameToReference.get(bean.getAttribute(BEAN_NAME_ATTRIBUTE));
                BeanDefinition beanDefinition = beansToBeanDefinitions.get(path);
                beanDefinition.putInject(fieldName, beanImpl);
            }

        }
    }

    @Override
    public <T> Class getImplementationClass(Class<T> interfaceClass) {
        return (Class <T>) beansToBeanDefinitions.get(interfaceClass.getName()).getImplementation();
    }


    @SneakyThrows
    @Override
    public <T> BeanDefinition generateBean(Class<T> tClass) {
        Class clz = tClass;
        if (tClass.isInterface())  {
            tClass = (Class <T>) beansToBeanDefinitions.get(tClass.getName()).getImplementation();
        }
        T bean = tClass.getDeclaredConstructor().newInstance();


        //inject all dependencies from beanDefinition
        BeanDefinition beanDefinition = beansToBeanDefinitions.get(tClass.getName());
        // need to put the bean to map from here
        this.beanFactory.addBean(clz, beanDefinition);

        for (Field field : tClass.getDeclaredFields()) {
            String ref = beanDefinition.getInject(field.getName());
            if (ref == null) {
                continue;
            }

            ref = nameToReference.get(ref);

            field.setAccessible(true);
            field.set(
                    bean,
                    this.beanFactory.getBean(Class.forName(ref)));
        }
        if (beanDefinition.getScope() == null) {
            beanDefinition.setScope("singleton");
        }
        DefaultBeanDefinition tmp = new DefaultBeanDefinition();
        tmp.setBean(bean);
        tmp.setBeanClassName(bean.getClass().getName());
        beanDefinition.setBean(bean);

        return beanDefinition;
    }
}
