package com.yipin.basic.utils.others;

import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ImageAlgorithms {
    private static Long angel;//角度

    /**
     * 构图算法
     **/
    public static List<String> shiYan(String path, String fileName) {
        Mat srcMat = Imgcodecs.imread(path);
        Mat t1 = new Mat();
        Mat tt1 = new Mat();
        //双边滤波
        Imgproc.bilateralFilter(srcMat, t1, 0, 100, 15);
        Imgproc.bilateralFilter(srcMat, tt1, 0, 10, 10);
        //tt1
        Imgproc.cvtColor(srcMat, tt1, Imgproc.COLOR_RGB2GRAY);
        Mat gaosi1 = new Mat();
        Mat gaosi2 = new Mat();
        //高斯滤波
        Imgproc.GaussianBlur(tt1, gaosi1, new Size(3, 3), 0);
        Imgproc.threshold(gaosi1, gaosi2, 0, 255, Imgproc.THRESH_BINARY + Imgproc.THRESH_OTSU);
        //canny
        Mat can1 = new Mat();
        //can1   第一个参数为低阈值 后一个为高阈值
        Imgproc.Canny(tt1, can1, 60, 150, 3);
        //霍夫直线
        Mat lines = new Mat();
        Imgproc.HoughLines(can1, lines, 1, Math.PI / 180, 20, 180, 10);
        Point pt1 = new Point();
        Point pt2 = new Point();
        // Mat houghLines = new Mat();
        //houghLines.create(can1.rows(), can1.cols(), CvType.CV_8UC1);
        double[] vec = lines.get(10, 0);
        double rho = vec[0];
        double theta = vec[1];
        boolean a = theta < (Math.PI / 4.);
        boolean b = theta > (3. * Math.PI / 4.0);
        double c = rho / Math.cos(theta);
        double x1 = c;

        if (a || b) {
            pt1.x = c;
            pt1.y = 0;
            pt2.x = ((rho - t1.height() * Math.sin(theta)) / Math.cos(theta));
            pt2.y = t1.height();
        } else {
            pt1.x = 0;
            pt1.y = rho / Math.sin(theta);
            pt2.x = t1.width();
            pt2.y = ((rho - t1.width() * Math.cos(theta)) / Math.sin(theta));
        }
        Imgproc.line(t1, pt1, pt2, new Scalar(255, 0, 0), 2);

        System.out.println("起始坐标=" + pt1 + pt2);
        double jiaodu;
        jiaodu = -(pt2.y - pt1.y) / (pt2.x - pt1.x);
        jiaodu = Math.atan(jiaodu) * (180 / Math.PI);

        angel = Math.round(jiaodu);

        Imgcodecs.imwrite("/root/art/images/other/" + "tt1" + fileName, tt1);
        Imgcodecs.imwrite("/root/art/images/other/" + "can1" + fileName, can1);
        Imgcodecs.imwrite("/root/art/images/other/" + "t1" + fileName, t1);
        ArrayList<String> imgUrls = new ArrayList<>();
        imgUrls.add("/images/other/" + "tt1" + fileName);
        imgUrls.add("/images/other/" + "can1" + fileName);
        imgUrls.add("/images/other/" + "t1" + fileName);
        return imgUrls;
    }

    /**
     * 视觉效果等级算法
     **/
    public static double viewRanking(String path) {
        int[] rgb = new int[3];
        File file = new File(path);
        BufferedImage bi = null;
        try {
            bi = ImageIO.read(file);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //获取图片的宽、高以及最小值
        int width = bi.getWidth();
        int height = bi.getHeight();
        int minx = bi.getMinX();
        int miny = bi.getMinY();
        int gray, gray1, gray2, gray3, gray4, gray5, gray6, gray7, gray8;
        List arry = new ArrayList();

        for (int i = minx + 1; i < width - 1; i++) {
            for (int j = miny + 1; j < height - 1; j++) {
                StringBuffer s = new StringBuffer();
                int s1;
                //获取rgb
                int p = bi.getRGB(i, j);
                int r = (p & 16711680) >> 16;
                int g = (p & 65280) >> 8;
                int b = (p & 255);
                //获取灰度值
                gray = (int) (r * 0.3 + g * 0.6 + b * 0.1);
                //左侧
                int p1 = bi.getRGB(i - 1, j);
                int r1 = (p1 & 16711680) >> 16;
                int g1 = (p1 & 65280) >> 8;
                int b1 = (p1 & 255);
                gray1 = (int) (r1 * 0.3 + g1 * 0.6 + b1 * 0.1);
                //左下角
                int p2 = bi.getRGB(i - 1, j + 1);
                int r2 = (p2 & 16711680) >> 16;
                int g2 = (p2 & 65280) >> 8;
                int b2 = (p2 & 255);
                gray2 = (int) (r2 * 0.3 + g2 * 0.6 + b2 * 0.1);
                //下方
                int p3 = bi.getRGB(i, j + 1);
                int r3 = (p3 & 16711680) >> 16;
                int g3 = (p3 & 65280) >> 8;
                int b3 = (p3 & 255);
                gray3 = (int) (r3 * 0.3 + g3 * 0.6 + b3 * 0.1);
                //右下角
                int p4 = bi.getRGB(i + 1, j + 1);
                int r4 = (p4 & 16711680) >> 16;
                int g4 = (p4 & 65280) >> 8;
                int b4 = (p4 & 255);
                gray4 = (int) (r4 * 0.3 + g4 * 0.6 + b4 * 0.1);
                //右侧
                int p5 = bi.getRGB(i + 1, j);
                int r5 = (p5 & 16711680) >> 16;
                int g5 = (p5 & 65280) >> 8;
                int b5 = (p5 & 255);
                gray5 = (int) (r5 * 0.3 + g5 * 0.6 + b5 * 0.1);
                //右上角
                int p6 = bi.getRGB(i + 1, j - 1);
                int r6 = (p6 & 16711680) >> 16;
                int g6 = (p6 & 65280) >> 8;
                int b6 = (p6 & 255);
                gray6 = (int) (r6 * 0.3 + g6 * 0.6 + b6 * 0.1);
                //上方
                int p7 = bi.getRGB(i, j - 1);
                int r7 = (p7 & 16711680) >> 16;
                int g7 = (p7 & 65280) >> 8;
                int b7 = (p7 & 255);
                gray7 = (int) (r7 * 0.3 + g7 * 0.6 + b7 * 0.1);
                //左上角
                int p8 = bi.getRGB(i - 1, j - 1);
                int r8 = (p8 & 16711680) >> 16;
                int g8 = (p8 & 65280) >> 8;
                int b8 = (p8 & 255);
                gray8 = (int) (r8 * 0.3 + g8 * 0.6 + b8 * 0.1);

                //若周围像素值大于中心像素值，则该像素点的位置被标记为1，否则为0
                if (gray1 > gray) {
                    s.append(1);
                } else {
                    s.append(0);
                }
                if (gray2 > gray) {
                    s.append(1);
                } else {
                    s.append(0);
                }
                if (gray3 > gray) {
                    s.append(1);
                } else {
                    s.append(0);
                }
                if (gray4 > gray) {
                    s.append(1);
                } else {
                    s.append(0);
                }
                if (gray5 > gray) {
                    s.append(1);
                } else {
                    s.append(0);
                }
                if (gray6 > gray) {
                    s.append(1);
                } else {
                    s.append(0);
                }
                if (gray7 > gray) {
                    s.append(1);
                } else {
                    s.append(0);
                }
                if (gray8 > gray) {
                    s.append(1);
                } else {
                    s.append(0);
                }
                //二进制转十进制
                s1 = Integer.parseInt(String.valueOf(s), 2);
                arry.add(s1);
            }
        }

        //读取arry数组的长度
        int quent = arry.size();
        List count = new ArrayList();
        List proportion = new ArrayList();
        List sum = new ArrayList();

        //记录相同数字出现的个数
        for (int p1 = 0; p1 < 256; p1++) {
            int frequent = Collections.frequency(arry, p1);
            count.add(frequent);

        }

        //计算 出现的频率 进而计算灰度熵
        double sum1 = 0;
        for (int p2 = 0; p2 < 256; p2++) {
            int p3 = (int) count.get(p2);
            double p4 = (double) p3 / quent;
            double p5 = 0;
            if (p4 == 0) {
                p5 = p5;
            } else {
                p5 = -p4 * ((Math.log(p4) / Math.log(2)));
            }
            sum.add(p5);
            proportion.add(p4);
        }
        //求灰度熵
        for (int q1 = 0; q1 < 256; q1++) {
            double sum2 = (double) sum.get(q1);
            sum1 = sum1 + sum2;
        }

        //结果归一化 数字7为纹理图熵的最大值

        return sum1 / 7;
    }

    public static Long getAngel() {
        return angel;
    }

}
