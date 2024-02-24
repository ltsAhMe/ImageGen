package image;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Arrays;

public class genImage {
    public final static int NULL = -999999999;



    public static int[] ThreadGetJob(int all,int Threadnum){
        int[] done = new int[Threadnum];
        int shoud = (int)(all/Threadnum);
        for (int i=0;i<done.length;i++){
            if (i== done.length-1){
                done[i]=all;
            }else {
                done[i]=(shoud*i)+shoud;
            }
        }
        return done;
    }

   static volatile int okNum=0;
    public static BufferedImage Threadblur(BufferedImage image,int size,int Threadnum){

        BufferedImage done = new BufferedImage(image.getWidth(),image.getHeight(),image.getType());
        int[] Job = ThreadGetJob(image.getWidth(),Threadnum);
        for (int CreateThread=0;CreateThread<Threadnum;CreateThread++){
            int finalCreateThread = CreateThread;
            new Thread(){
                @Override
                public void run() {
                    super.run();
                    int[] canbe = new int[(int) Math.pow(9,size)];
                    byte[] canBool = new byte[canbe.length];
                    for (int x = (finalCreateThread *Job[0]); x<Job[finalCreateThread]; x++){
                        for (int y=0;y<image.getHeight();y++){
                            Arrays.fill(canbe,NULL);
                            Arrays.fill(canBool,Byte.MAX_VALUE);
                            for (int sx = -size; sx <= size; sx++) {
                                for (int sy = -size; sy <= size; sy++) {
                                    if (x + sx >= 0 && x + sx < image.getWidth() && y + sy >= 0 && y + sy < image.getHeight()) {
                                        int sxx = Math.abs(sx);
                                        int syy = Math.abs(sy);
                                        if (sxx > syy) {
                                            a(canBool, (byte) sxx);
                                        } else {
                                            a(canBool, (byte) syy);
                                        }
                                        a(canbe, image.getRGB((x + sx),(y + sy)));
                                    }
                                }
                            }
//                            System.out.println("x:"+x+" y:"+y);
                            done.setRGB(x,y,getColorBlur(canbe,canBool));
                        }
                    }
                    okNum++;
                }
            }.start();
        }
        while (okNum!=Threadnum){
        }
        return done;
    }

    public static BufferedImage blur(BufferedImage image,int size){
        BufferedImage done = new BufferedImage(image.getWidth(),image.getHeight(),image.getType());
        for (int x=0;x<image.getWidth();x++){
            for (int y=0;y<image.getHeight();y++){
                int[] canbe = new int[(int) Math.pow(9,size)];
                byte[] canBool = new byte[canbe.length];
                Arrays.fill(canbe,NULL);
                Arrays.fill(canBool,Byte.MAX_VALUE);
                for (int sx = -size; sx <= size; sx++) {
                    for (int sy = -size; sy <= size; sy++) {
                        if (x + sx >= 0 && x + sx < image.getWidth() && y + sy >= 0 && y + sy < image.getHeight()) {
                            int sxx = Math.abs(sx);
                            int syy = Math.abs(sy);
                            if (sxx > syy) {
                                a(canBool, (byte) sxx);
                            } else {
                                a(canBool, (byte) syy);
                            }
                            a(canbe, image.getRGB((x + sx),(y + sy)));
                        }
                    }
                }
//                System.out.println("x:"+x+" y:"+y);
                done.setRGB(x,y,getColorBlur(canbe,canBool));
            }
        }
        System.out.println("blur");
        return done;
    }
    public static BufferedImage bloom(BufferedImage image,float scale,int size,int start){
        BufferedImage done = new BufferedImage(image.getWidth(),image.getHeight(),image.getType());
        Graphics2D graphics2D = (Graphics2D) done.getGraphics();
        graphics2D.drawImage(image,0,0,null);
        graphics2D.drawImage(Threadblur(reSize(Colorcut(image,start,255,1),scale),size,5),0,0,image.getWidth(),image.getHeight(),null);
        graphics2D.dispose();
        return done;
    }
    public static BufferedImage reSize(BufferedImage image,float scale){
        if (scale==1){
            return image;
        }
        return toBufferedImage(image.getScaledInstance((int) ((float)image.getWidth()*scale), (int) ((float)image.getHeight()*scale),Image.SCALE_FAST));
    }


    public static BufferedImage Colorcut(BufferedImage image,int start,int end,int mode){
        BufferedImage done = new BufferedImage(image.getWidth(),image.getHeight(),BufferedImage.TYPE_INT_ARGB);
        for(int x=0;x<image.getWidth();x++){
            for (int y=0;y<image.getHeight();y++){
               int The = image.getRGB(x,y);
               int shoude=-1;
               switch (mode){
                   case 0:
                       shoude=(The >> 24)&0xFF;
                       break;
                   case 1:
                       shoude=(The >> 16)&0xFF;
                       break;
                   case 2:
                       shoude=(The >> 8)&0xFF;
                       break;
                   case 3:
                       shoude=The&0xFF;
                       break;
               }
               if (shoude>=start&&shoude<=end){
                   done.setRGB(x,y,The);
               }
            }
        }
        return done;
    }

    public static int getColorBlur(int[] sb, byte[] state) {
        int sumA = 0;
        int sumR = 0;
        int sumG = 0;
        int sumB = 0;
        int totalWeight = 0;

        for (int i = 0; i < sb.length; i++) {
            int pixel = sb[i];
            byte weight = state[i];

            if (pixel != NULL) {
                int alpha = (pixel >> 24) & 0xFF;
                int red = (pixel >> 16) & 0xFF;
                int green = (pixel >> 8) & 0xFF;
                int blue = pixel & 0xFF;

                int weightedAlpha = alpha * weight;
                int weightedRed = red * weight;
                int weightedGreen = green * weight;
                int weightedBlue = blue * weight;

                sumA += weightedAlpha;
                sumR += weightedRed;
                sumG += weightedGreen;
                sumB += weightedBlue;
                totalWeight += weight;
            }
        }

        int averageA =  (sumA / totalWeight);
        int averageR =  (sumR / totalWeight);
        int averageG = (sumG / totalWeight);
        int averageB = (sumB / totalWeight);

        return (averageA << 24) | (averageR << 16) | (averageG << 8) | averageB;
    }
    public static int getnull(int[] s){
        for (int i=0;i<s.length;i++){
            if (s[i]==NULL){
                return i;
            }
        }
        return -1;
    }


    public static int getnull(byte[] s){
        for (int i=0;i<s.length;i++){
            if (s[i]==Byte.MAX_VALUE){
                return i;
            }
        }
        return Byte.MAX_VALUE;
    }
    public static void a(int[] s,int i){
        s[getnull(s)]=i;
    }
    public static void a(byte[] s,byte check){
        s[getnull(s)] = check;
    }
//    public static BufferedImage toBufferedImage(Image image) {
//        BufferedImage Done = new BufferedImage(image.getWidth(null),image.getHeight(null),BufferedImage.TYPE_INT_ARGB);
//        Graphics2D g2d = (Graphics2D) Done.getGraphics();
//        g2d.drawImage(Done,0,0,null);
//        g2d.dispose();
//        return Done;
//    }
//public static BufferedImage toBufferedImage(Image image) {
//    if (image instanceof BufferedImage) {
//        return (BufferedImage) image;
//    }
//
//    int width = image.getWidth(null);
//    int height = image.getHeight(null);
//    boolean hasAlpha = false;
//
//    GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
//    GraphicsDevice gs = ge.getDefaultScreenDevice();
//    GraphicsConfiguration gc = gs.getDefaultConfiguration();
//
//    int transparency = Transparency.OPAQUE;
//    if (hasAlpha) {
//        transparency = Transparency.TRANSLUCENT;
//    }
//
//    BufferedImage bimage = gc.createCompatibleImage(width, height, transparency);
//    Graphics g = bimage.createGraphics();
//    g.drawImage(image, 0, 0, null);
//    g.dispose();
//    return bimage;
//}
    public static BufferedImage toBufferedImage(Image image) {
        if (image instanceof BufferedImage) {
            return (BufferedImage) image;
        }
        int width = image.getWidth(null);
        int height = image.getHeight(null);
        BufferedImage bimage = new BufferedImage(width,height,BufferedImage.TYPE_INT_ARGB);
        Graphics g = bimage.createGraphics();
        g.drawImage(image, 0, 0, null);
        g.dispose();
        return bimage;
    }
}


