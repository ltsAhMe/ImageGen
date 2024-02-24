import TimerTest.newTimerTest;
import commend.uncode;
import image.genImage;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        BufferedImage image;
        try {
            image = ImageIO.read(new File("C:\\Users\\ltsAhMe\\Desktop\\allCr\\imageTest\\v2-86848ef3286546c5e800b57756079473_b.jpg"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        newTimerTest sb = new newTimerTest(){
            @Override
            public void Run() {
                genImage.Threadblur(image,5,5);
            }
        };
        sb.getFps(30);
//        uncode.Review(args);
    }
}
