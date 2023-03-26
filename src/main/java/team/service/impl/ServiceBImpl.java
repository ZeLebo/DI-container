package team.service.impl;

import team.annotations.PostConstruct;
import team.annotations.Provided;
import team.annotations.Service;
import team.service.ServiceB;

@Service
@Provided
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
