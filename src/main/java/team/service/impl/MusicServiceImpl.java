package team.service.impl;

import team.annotations.Inject;
import team.annotations.Service;
import team.service.FrontService;
import team.service.MusicService;

import java.util.concurrent.TimeUnit;

@Service
public class MusicServiceImpl implements MusicService {
    @Inject
    public FrontService front;

    @Override
    public void musicLoading() throws InterruptedException {
        TimeUnit.SECONDS.sleep(1);
        System.out.println("Music is being listened by all the humanity");
        front.test();
    }
}
