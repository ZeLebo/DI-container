package team.service.impl;

import team.service.ServiceB;

public class ServiceBAnotherImpl implements ServiceB {
    @Override
    public void jobB() {
        System.out.println("Another variant of work by ServiceB has been chosen");
    }
}
