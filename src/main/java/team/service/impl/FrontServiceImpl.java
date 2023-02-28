package team.service.impl;

import team.annotations.Inject;
import team.annotations.Service;
import team.service.FrontService;
import team.service.MusicService;
import team.service.PictureService;

import java.util.concurrent.TimeUnit;

@Service
public class FrontServiceImpl implements FrontService {
    @Inject
    private MusicService musicService;

    @Inject
    private PictureService pictureService;

    @Override
    public void siteLoading() {
        try {
            musicService.musicLoading();
            pictureService.pictureLoading();
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        System.out.println("Our BeAuTiFuL SiTe has been loaded");
    }
}
