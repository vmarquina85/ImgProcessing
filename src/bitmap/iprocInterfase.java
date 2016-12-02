/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bitmap;

import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 *
 * @author victor.marquina
 */
public interface iprocInterfase {

    public BufferedImage binarizarImagen(BufferedImage bin)throws IOException;

    public int[][] doZhangSuenThinning(int[][] givenImage, boolean changeGivenImage);

    public int getA(int[][] binaryImage, int y, int x);
    public int getB(int[][] binaryImage, int y, int x);
    public int [][] createImageData(BufferedImage bin);
    public BufferedImage UpdateBufferedImage(int[][] binaryImage,BufferedImage bin, int angulo);
    public void aplicarConvolucion(BufferedImage Esqueletizado,int[][] Kernel);
    

    
    
}
