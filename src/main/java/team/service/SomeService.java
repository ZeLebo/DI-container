package team.service;

import team.annotations.Inject;
import team.annotations.PostConstruct;

public class SomeService {

    @Inject
    private ServiceB serviceB;

    @PostConstruct
    public void postConstruct() {
        System.out.println("post construct!");
    }

    public void prepare() {
        System.out.println("start...");
    }
}
