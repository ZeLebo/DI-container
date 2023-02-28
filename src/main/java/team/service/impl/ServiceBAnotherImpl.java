package team.service.impl;
import team.annotations.Service;
import team.service.ServiceB;

@Service
public class ServiceBAnotherImpl implements ServiceB {
    @Override
    public void jobB() {
        System.out.println("The needed work has been done ANOTHER");
    }
}
