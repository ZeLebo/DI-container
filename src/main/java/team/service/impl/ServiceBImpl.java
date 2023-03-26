package team.service.impl;

import team.annotations.PostConstruct;
import team.service.ServiceB;

public class ServiceBImpl implements ServiceB {
    @Override
    public void jobB() {
        System.out.println("The needed work has been done");
    }

    @PostConstruct
    public void prepare() {
        System.out.println("The worker has been chosen");
    }
}
