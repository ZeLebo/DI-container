package team.postprocessor;

public class LoggingPostProcessor implements BeanPostProcessor {
    @Override
    public void process(Object bean) {
        System.out.println(String.format("Log: bean has been created: %s", bean.getClass()));
    }
}
