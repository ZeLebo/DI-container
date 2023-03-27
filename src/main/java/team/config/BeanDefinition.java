package team.config;

@SuppressWarnings("unused")
public interface BeanDefinition {
    String getBeanName();
    void SetBeanName(String beanName);

    void SetBeanPackage(String beanPackage);

    String GetBeanPackage();

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
