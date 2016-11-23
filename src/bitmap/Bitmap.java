/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bitmap;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 *
 * @author victor.marquina
 */
public class Bitmap {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        File img = new File("d:/firma/firma3.jpg");
        BufferedImage f = ImageIO.read(img);
        iprocAdapter adapter = new iprocAdapter();
        
        BufferedImage bn = adapter.binarizarImagen(f);
        File img2 = new File("d:/firma/binfirma.jpg");
        ImageIO.write(bn, "jpg", img2);
        int[][] ImageData = adapter.createImageData(bn);
        int[][] givenImage = adapter.doZhangSuenThinning(ImageData, true);
        BufferedImage skeletonImage = adapter.UpdateBufferedImage(givenImage, bn);
        File img3 = new File("d:/firma/skeletonfirma.jpg");
        ImageIO.write(skeletonImage, "jpg", img3);

    }

}
