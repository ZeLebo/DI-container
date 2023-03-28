package team.config;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DefaultBeanDefinition implements BeanDefinition {
    private String beanName;
    private String beanClassName;
    private String scope;
    private String beanPackage;
    private Object bean;
    private Class implementation;
    private String postConstructMethod;
    private static final Map<String, String> injectsMap = new ConcurrentHashMap<>();

    @Override
    public String getBeanName() {
        return this.beanName;
    }

    @Override
    public void setBeanName(String beanName) {
        this.beanName = beanName;
    }

    @Override
    public void setImplementation(Class implementation) {
        this.implementation = implementation;
    }

    @Override
    public Class getImplementation() {
        return this.implementation;
    }

    @Override
    public void putInject(String field, String reference) {
        injectsMap.put(field, reference);
    }

    @Override
    public String getInject(String field) {
        return injectsMap.get(field);
    }

    @Override
    public void setPostConstructMethod(String postConstructMethod) {
        this.postConstructMethod = postConstructMethod;
    }


    @Override
    public String getPostConstructMethod() {
        return this.postConstructMethod;
    }


    @Override
    public void setBeanPackage(String beanPackage) {
        this.beanPackage = beanPackage;
    }

    @Override
    public String getBeanPackage() {
        return this.beanPackage;
    }

    @Override
    public Object getBean() {
        return this.bean;
    }

    @Override
    public void setBean(Object bean) {
        this.bean = bean;
    }

    @Override
    public String getBeanClassName() {
        return this.beanClassName;
    }

    @Override
    public void setBeanClassName(String beanClassName) {
        this.beanClassName = beanClassName;
    }

    @Override
    public String getScope() {
        return this.scope;
    }

    @Override
    public void setScope(String scope) {
        this.scope = scope;
    }

    @Override
    public boolean isSingleton() {
        return this.scope.equalsIgnoreCase("singleton");
    }

    @Override
    public boolean isPrototype() {
        return this.scope.equalsIgnoreCase("prototype");
    }

    @Override
    public boolean isThread() {
        return this.scope.equalsIgnoreCase("thread");
    }
}
