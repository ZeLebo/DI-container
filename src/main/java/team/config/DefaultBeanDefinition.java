package team.config;

import java.lang.reflect.InvocationTargetException;

@SuppressWarnings("unused")
public class DefaultBeanDefinition implements BeanDefinition {
    private String beanName;
    private String beanClassName;
    private String scope;
    private String beanPackage;
    private Object bean;

    @Override
    public String getBeanName() {
        return this.beanName;
    }

    @Override
    public void SetBeanName(String beanName) {
        this.beanName = beanName;
    }

    @Override
    public void SetBeanPackage(String beanPackage) {
        this.beanPackage = beanPackage;
    }

    @Override
    public String GetBeanPackage() {
        return this.beanPackage;
    }

    @Override
    public Object getBean() {
        if (this.bean == null) {
            Class <?> clazz = null;
            try {
                clazz = Class.forName(this.beanPackage);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException("Class with this package hasn't been found");
            }
            try {
                return clazz.getDeclaredConstructor().newInstance();
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                throw new RuntimeException("Cannot create new bean for class" + this.beanClassName);
            }
        }
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
