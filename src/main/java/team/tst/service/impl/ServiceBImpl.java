package team.tst.service.impl;

import team.tst.annotations.Service;
import team.tst.service.ServiceB;

@Service
public class ServiceBImpl implements ServiceB {
    @Override
    public void jobB() {
        System.out.println("The needed work has been done");
    }
}
