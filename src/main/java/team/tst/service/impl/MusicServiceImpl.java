package team.tst.service.impl;

import team.tst.annotations.Service;
import team.tst.service.MusicService;

import java.util.concurrent.TimeUnit;

@Service
public class MusicServiceImpl implements MusicService {
    @Override
    public void musicLoading() throws InterruptedException {
        TimeUnit.SECONDS.sleep(1);
        System.out.println("Music is being listened by all the humanity");
    }
}
