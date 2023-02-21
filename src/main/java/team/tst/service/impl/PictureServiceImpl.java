package team.tst.service.impl;

import team.tst.annotations.Service;
import team.tst.service.PictureService;

import java.util.concurrent.TimeUnit;

@Service
public class PictureServiceImpl implements PictureService {

    @Override
    public void pictureLoading() throws InterruptedException {
        TimeUnit.SECONDS.sleep(1);
        System.out.println("The pictures have been loaded to all the computers all over the world");
    }
}
