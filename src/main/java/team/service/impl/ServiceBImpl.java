package team.service.impl;

import team.annotations.PostConstruct;
import team.annotations.Service;
import team.annotations.Thread;
import team.service.ServiceB;


@Service
@Thread
public class ServiceBImpl implements ServiceB {
    private int cnt;
    @Override
    public void jobB() {
        this.cnt++;
        System.out.println("The needed work has been done" + cnt);
    }

    @PostConstruct
    public void prepare() {
        System.out.println("The worker has been chosen");
    }
}
