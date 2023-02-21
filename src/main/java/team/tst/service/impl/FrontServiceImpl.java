package team.tst.service.impl;

import team.tst.annotations.Inject;
import team.tst.annotations.Service;
import team.tst.service.FrontService;
import team.tst.service.MusicService;
import team.tst.service.PictureService;

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
