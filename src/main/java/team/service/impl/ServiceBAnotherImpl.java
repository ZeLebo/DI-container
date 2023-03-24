package team.service.impl;

import team.service.ServiceB;

public class ServiceBAnotherImpl implements ServiceB {
    @Override
    public void jobB() {
        System.out.println("The needed work has been done ANOTHER");
    }
}
