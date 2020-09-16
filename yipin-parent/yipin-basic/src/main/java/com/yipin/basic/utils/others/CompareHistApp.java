package com.yipin.basic.utils.others;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Arrays;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;

/**
 * Hello world!
 *
 */
public class CompareHistApp 
{
    public static void main( String[] args )
    {
        System.out.println( "CompareHistApp" );
        System.out.println("Welcome to OpenCV " + Core.VERSION);
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        NumberFormat df = new DecimalFormat("#.##");
        double target = CompareHist.getMSSIM("student1.jpg", "teacher1.jpg").val[0];
        System.out.println("student1 and teacher1 = " + df.format(target));
        target = CompareHist.getMSSIM("student1.jpg", "teacher2.jpg").val[0];
        System.out.println("student1 and teacher2 = " + df.format(target));
        target = CompareHist.getMSSIM("student1.jpg", "teacher3.jpg").val[0];
        System.out.println("student1 and teacher3 = " + df.format(target));
        Mat colors = new Mat(1, 8, CvType.CV_8UC3);//RGBCMYKW
        byte[] colorsData = {0, 0, -1, 0, -1, 0, -1, 0, 0, -1, -1, 0, -1, 0, -1, 0, -1, -1, 0, 0, 0, -1, -1, -1};
        colors.put(0, 0, colorsData);
        int[] colorStats = CompareHist.colorStat("18.jpg", colors);
        System.out.println("student1 colorStats = " + Arrays.toString(colorStats));
        double[] colorStatsNormal = new double[colorStats.length];
        double total = 0;
        for(int i = 0;i < colorStats.length;i++)
        {
        	total += colorStats[i];
        }
        for(int i = 0;i < colorStatsNormal.length;i++)
        {
        	colorStatsNormal[i] = (int)(colorStats[i] * 10000.0 / total) / 100.0;
        }
        System.out.println("student1 colorStats% = " + Arrays.toString(colorStatsNormal));
    }
}
