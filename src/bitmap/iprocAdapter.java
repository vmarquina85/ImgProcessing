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
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

/**
 *
 * @author victor.marquina
 */
public class iprocAdapter implements iprocInterfase {

    public static final int NO_CONV = 0;
    public static final int TIPO_CONV_0 = 1;
    public static final int TIPO_CONV_45 = 2;
    public static final int TIPO_CONV_90 = 3;
    public static final int TIPO_CONV_135 = 4;
    public static final int[][] MATRIZ0 = {{1, 1}, {0, 0}};
    public static final int[][] MATRIZ45 = {{0, 1}, {1, 0}};
    public static final int[][] MATRIZ90 = {{1, 0}, {1, 0}};
    public static final int[][] MATRIZ135 = {{1, 0}, {0, 1}};

    @Override
    public BufferedImage binarizarImagen(BufferedImage f) throws IOException {
        BufferedImage bn = new BufferedImage(f.getWidth(), f.getHeight(), BufferedImage.TYPE_BYTE_BINARY);
        for (int i = 0; i < f.getWidth(); i++) {
            for (int j = 0; j < f.getHeight(); j++) {
                bn.setRGB(i, j, f.getRGB(i, j));
            }
        }
        return bn;
    }

    @Override
    public int[][] doZhangSuenThinning(int[][] givenImage, boolean changeGivenImage) {
        int[][] binaryImage;
        if (changeGivenImage) {
            binaryImage = givenImage;
        } else {
            binaryImage = givenImage.clone();
        }
        int a, b;
        List<Point> pointsToChange = new LinkedList();
        boolean hasChange;
        do {
            hasChange = false;
            for (int y = 1; y + 1 < binaryImage.length; y++) {
                for (int x = 1; x + 1 < binaryImage[y].length; x++) {
                    a = getA(binaryImage, y, x);
                    b = getB(binaryImage, y, x);
                    if (binaryImage[y][x] == 1 && 2 <= b && b <= 6 && a == 1
                            && (binaryImage[y - 1][x] * binaryImage[y][x + 1] * binaryImage[y + 1][x] == 0)
                            && (binaryImage[y][x + 1] * binaryImage[y + 1][x] * binaryImage[y][x - 1] == 0)) {
                        pointsToChange.add(new Point(x, y));
//binaryImage[y][x] = 0;
                        hasChange = true;
                    }
                }
            }
            for (Point point : pointsToChange) {
                binaryImage[point.getY()][point.getX()] = 0;
            }
            pointsToChange.clear();
            for (int y = 1; y + 1 < binaryImage.length; y++) {
                for (int x = 1; x + 1 < binaryImage[y].length; x++) {
                    a = getA(binaryImage, y, x);
                    b = getB(binaryImage, y, x);
                    if (binaryImage[y][x] == 1 && 2 <= b && b <= 6 && a == 1
                            && (binaryImage[y - 1][x] * binaryImage[y][x + 1] * binaryImage[y][x - 1] == 0)
                            && (binaryImage[y - 1][x] * binaryImage[y + 1][x] * binaryImage[y][x - 1] == 0)) {
                        pointsToChange.add(new Point(x, y));
                        hasChange = true;
                    }
                }
            }
            for (Point point : pointsToChange) {
                binaryImage[point.getY()][point.getX()] = 0;
            }
            pointsToChange.clear();
        } while (hasChange);
        return binaryImage;
    }

    @Override
    public int getA(int[][] binaryImage, int y, int x) {
        int count = 0;
//p2 p3
        if (y - 1 >= 0 && x + 1 < binaryImage[y].length && binaryImage[y - 1][x] == 0 && binaryImage[y - 1][x + 1] == 1) {
            count++;
        }
//p3 p4
        if (y - 1 >= 0 && x + 1 < binaryImage[y].length && binaryImage[y - 1][x + 1] == 0 && binaryImage[y][x + 1] == 1) {
            count++;
        }
//p4 p5
        if (y + 1 < binaryImage.length && x + 1 < binaryImage[y].length && binaryImage[y][x + 1] == 0 && binaryImage[y + 1][x + 1] == 1) {
            count++;
        }
//p5 p6
        if (y + 1 < binaryImage.length && x + 1 < binaryImage[y].length && binaryImage[y + 1][x + 1] == 0 && binaryImage[y + 1][x] == 1) {
            count++;
        }
//p6 p7
        if (y + 1 < binaryImage.length && x - 1 >= 0 && binaryImage[y + 1][x] == 0 && binaryImage[y + 1][x - 1] == 1) {
            count++;
        }
//p7 p8
        if (y + 1 < binaryImage.length && x - 1 >= 0 && binaryImage[y + 1][x - 1] == 0 && binaryImage[y][x - 1] == 1) {
            count++;
        }
//p8 p9
        if (y - 1 >= 0 && x - 1 >= 0 && binaryImage[y][x - 1] == 0 && binaryImage[y - 1][x - 1] == 1) {
            count++;
        }
//p9 p2
        if (y - 1 >= 0 && x - 1 >= 0 && binaryImage[y - 1][x - 1] == 0 && binaryImage[y - 1][x] == 1) {
            count++;
        }
        return count;
    }

    @Override
    public int getB(int[][] binaryImage, int y, int x) {
        return binaryImage[y - 1][x] + binaryImage[y - 1][x + 1] + binaryImage[y][x + 1]
                + binaryImage[y + 1][x + 1] + binaryImage[y + 1][x] + binaryImage[y + 1][x - 1]
                + binaryImage[y][x - 1] + binaryImage[y - 1][x - 1];
    }

    @Override
    public int[][] createImageData(BufferedImage bn) {
        int[][] imageData = new int[bn.getHeight()][bn.getWidth()];
        Color c;
        for (int y = 0; y < imageData.length; y++) {
            for (int x = 0; x < imageData[y].length; x++) {

                if (bn.getRGB(x, y) == Color.BLACK.getRGB()) {
                    imageData[y][x] = 1;
                } else if (bn.getRGB(x, y) == Color.WHITE.getRGB()) {
                    imageData[y][x] = 0;

                }
            }
        }
        return imageData;
    }

    @Override
    public BufferedImage UpdateBufferedImage(int[][] binaryImage, BufferedImage bin, int angulo) {
        for (int y = 0; y < binaryImage.length; y++) {

            for (int x = 0; x < binaryImage[y].length; x++) {
                if (angulo == iprocAdapter.TIPO_CONV_0) {
                    if (binaryImage[y][x] == 1) {
                        bin.setRGB(x, y, Color.BLACK.getRGB());

                    } else if (binaryImage[y][x] == 0) {
                        bin.setRGB(x, y, Color.WHITE.getRGB());
                    } else {
                        bin.setRGB(x, y, Color.CYAN.getRGB());
                    }
                }
                if (angulo == iprocAdapter.TIPO_CONV_90) {
                    if (binaryImage[y][x] == 1) {
                        bin.setRGB(x, y, Color.BLACK.getRGB());

                    } else if (binaryImage[y][x] == 0) {
                        bin.setRGB(x, y, Color.WHITE.getRGB());
                    } else {
                        bin.setRGB(x, y, Color.BLUE.getRGB());
                    }
                }
                if (angulo == iprocAdapter.TIPO_CONV_45) {
                    if (binaryImage[y][x] == 1) {
                        bin.setRGB(x, y, Color.BLACK.getRGB());

                    } else if (binaryImage[y][x] == 0) {
                        bin.setRGB(x, y, Color.WHITE.getRGB());
                    } else {
                        bin.setRGB(x, y, Color.RED.getRGB());
                    }
                }
                if (angulo == iprocAdapter.TIPO_CONV_135) {
                    if (binaryImage[y][x] == 1) {
                        bin.setRGB(x, y, Color.BLACK.getRGB());

                    } else if (binaryImage[y][x] == 0) {
                        bin.setRGB(x, y, Color.WHITE.getRGB());
                    } else {
                        bin.setRGB(x, y, Color.GREEN.getRGB());
                    }
                }
                if (angulo == iprocAdapter.NO_CONV) {
                    if (binaryImage[y][x] == 1) {
                        bin.setRGB(x, y, Color.BLACK.getRGB());

                    } else if (binaryImage[y][x] == 0) {
                        bin.setRGB(x, y, Color.WHITE.getRGB());
                    }
                }

            }
        }
        return bin;
    }

    @Override
    public void aplicarConvolucion(BufferedImage Esqueletizado, int[][] Kernel) {
        iprocAdapter adapter = new iprocAdapter();
        int[][] ImagenMatriz = adapter.createImageData(Esqueletizado);

        int fila = ImagenMatriz.length;
        int col = ImagenMatriz[0].length;

        int[][] resultante = new int[fila][col];

        for (int i = 0; i < (ImagenMatriz.length) - 1; i++) {
            for (int j = 0; j < (ImagenMatriz[i].length) - 1; j++) {
                int resultPixel = ImagenMatriz[i][j] * Kernel[0][0] + ImagenMatriz[i][j + 1] * Kernel[0][1] + ImagenMatriz[i + 1][j] * Kernel[1][0] + ImagenMatriz[i + 1][j + 1] * Kernel[1][1];

                resultante[i][j] = resultPixel;

            }
        }

        BufferedImage res = new BufferedImage(Esqueletizado.getWidth(), Esqueletizado.getHeight(), BufferedImage.TYPE_INT_RGB);
        if (Kernel == iprocAdapter.MATRIZ0) {
            BufferedImage resultado = adapter.UpdateBufferedImage(resultante, res, iprocAdapter.TIPO_CONV_0);
            File img4 = new File("d:/firma/conv0.jpg");
            try {
                ImageIO.write(resultado, "jpg", img4);
            } catch (IOException ex) {
                Logger.getLogger(iprocAdapter.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        if (Kernel == iprocAdapter.MATRIZ135) {
            BufferedImage resultado = adapter.UpdateBufferedImage(resultante, res, iprocAdapter.TIPO_CONV_135);
            File img4 = new File("d:/firma/conv135.jpg");
            try {
                ImageIO.write(resultado, "jpg", img4);
            } catch (IOException ex) {
                Logger.getLogger(iprocAdapter.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        if (Kernel == iprocAdapter.MATRIZ45) {
            BufferedImage resultado = adapter.UpdateBufferedImage(resultante, res, iprocAdapter.TIPO_CONV_45);
            File img4 = new File("d:/firma/conv45.jpg");
            try {
                ImageIO.write(resultado, "jpg", img4);
            } catch (IOException ex) {
                Logger.getLogger(iprocAdapter.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        if (Kernel == iprocAdapter.MATRIZ90) {
            BufferedImage resultado = adapter.UpdateBufferedImage(resultante, res, iprocAdapter.TIPO_CONV_90);
            File img4 = new File("d:/firma/conv90.jpg");
            try {
                ImageIO.write(resultado, "jpg", img4);
            } catch (IOException ex) {
                Logger.getLogger(iprocAdapter.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

}
