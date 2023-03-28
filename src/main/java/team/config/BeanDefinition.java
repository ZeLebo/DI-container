package team.config;

import lombok.SneakyThrows;

@SuppressWarnings("unused")
public interface BeanDefinition {
    String getBeanName();
    void setBeanName(String beanName);

    void setImplementation(Class implementation);

    Class getImplementation();

    void putInject(String field, String reference);

    @SneakyThrows
    String getInject(String field);

    void setBeanPackage(String beanPackage);

    String getBeanPackage();

    Object getBean();

    void setBean(Object bean);

    String getBeanClassName();
    void setBeanClassName(String beanClassName);

    String getScope();
    void setScope(String scope);

    boolean isSingleton();
    boolean isPrototype();
    boolean isThread();
}
