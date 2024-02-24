package commend;

import image.genImage;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import static image.genImage.*;

public class uncode {
    public final static int help = 2;
    public final static int Bloom = 0;
    public final static int ReSize = 1;

    public final static int blur = 2;

    public final static int cut = 3;

    public final static int ThreadBlur = 4;

    public static void Review(String[] args){
        BufferedImage image = null;
        int MagicNumber = 0;
        if (args.length<1){
            System.out.println("if you dont know how to use,我认为你可以当个库使用 比如那个genImage 所以其实这些都可以删了");
        }
        //code
        switch (args[1]){
            case "bloom":
                MagicNumber = Bloom;
                break;
            case "ReSize":
                MagicNumber=ReSize;
                break;
            case "blur":
                MagicNumber = blur;
                break;
            case "cut":
                MagicNumber =  cut;
                break;
            case "help":
                MagicNumber = help;
                break;
            case "TBlur":
                MagicNumber = ThreadBlur;
                break;
            default:
                MagicNumber = Integer.MAX_VALUE;
        }

        try {
            image = ImageIO.read(new File(args[0]));
        } catch (IOException e) {
            System.err.println("not find file :(");
        }
        saveImage(Gen(image,MagicNumber,cut(args,2, args.length)));
    }
    public static void saveImage(BufferedImage image){
        File Donefile = new File("done.png");

        try {
            ImageIO.write(image,"png",Donefile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public static BufferedImage Gen(BufferedImage image,int Magic,String[] args){
        BufferedImage done = null;

        switch (Magic){
            case Bloom:
                done = bloom(image,Float.parseFloat(args[0]), Integer.parseInt(args[1]),Integer.parseInt(args[2]));
                break;
            case ReSize:
                done = reSize(image,Float.parseFloat(args[0]));
                break;
            case blur:
                done=blur(image,Integer.parseInt(args[0]));
                break;
            case cut:
                done =Colorcut(image,Integer.parseInt(args[0]),Integer.parseInt(args[1]),Integer.parseInt(args[2]));
                break;
            case ThreadBlur:
                done = Threadblur(image,Integer.parseInt(args[0]),Integer.parseInt(args[1]));
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + Magic);
        }

        return done;
    }
    public static String[] cut(String[] sb,int start,int end){
        String[] done = new String[end - start];
        for (int i=0;i<done.length;i++){
            done[i]=sb[i+start];
        }
        return done;
    }
}
