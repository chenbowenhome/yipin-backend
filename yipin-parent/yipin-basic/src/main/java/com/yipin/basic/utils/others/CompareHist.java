package com.yipin.basic.utils.others;

import java.util.Arrays;
import java.util.List;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfFloat;
import org.opencv.core.MatOfInt;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.core.TermCriteria;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.ml.Ml;
import org.opencv.ml.SVM;
import org.opencv.ml.TrainData;

public class CompareHist
{
	private static final int SAMPLE_NUMBER = 18;
	
	public CompareHist()
	{
		
	}
	
	public static double compare(String srcFilename,String desFilename)
	{
		Mat srcMat = Imgcodecs.imread(srcFilename);
		Mat desMat = Imgcodecs.imread(desFilename);
		Mat hsvMat1 = new Mat();
		Mat hsvMat2 = new Mat();
		Imgproc.cvtColor( srcMat, hsvMat1, Imgproc.COLOR_BGR2HSV );
		Imgproc.cvtColor( desMat, hsvMat2, Imgproc.COLOR_BGR2HSV );
		// hue varies from 0 to 179, saturation from 0 to 255
		float[] ranges = { 0, 180, 0, 256 };
		// Use the 0-th and 1-st channels
		int[] channels = { 0, 1 };
		int hBins = 50, sBins = 60;
        int[] histSize = { hBins, sBins };
		List<Mat> hsvMat1List = Arrays.asList(hsvMat1);
		List<Mat> hsvMat2List = Arrays.asList(hsvMat2);
		Mat histMat1 = new Mat();
		Mat histMat2 = new Mat();
        Imgproc.calcHist(hsvMat1List, new MatOfInt(channels), new Mat(), histMat1, new MatOfInt(histSize), new MatOfFloat(ranges), false);
        Imgproc.calcHist(hsvMat2List, new MatOfInt(channels), new Mat(), histMat2, new MatOfInt(histSize), new MatOfFloat(ranges), false);
        Core.normalize(histMat1, histMat1, 0, 1, Core.NORM_MINMAX);
        Core.normalize(histMat2, histMat2, 0, 1, Core.NORM_MINMAX);
		double target = compare(histMat1, histMat2);
		return target;
	}
	
	public static double compare(Mat srcMat,Mat desMat)
	{
		srcMat.convertTo(srcMat, CvType.CV_32F);
        desMat.convertTo(desMat, CvType.CV_32F);
        //CV_COMP_CORREL = 0,
        //CV_COMP_CHISQR = 1,
        //CV_COMP_INTERSECT = 2,
        //CV_COMP_BHATTACHARYYA = 3,
        //CV_COMP_HELLINGER = 3,
        //CV_COMP_CHISQR_ALT = 4,
        //CV_COMP_KL_DIV = 5,
        double target = Imgproc.compareHist(srcMat, desMat, Imgproc.CV_COMP_CORREL);

		return target;
	}
	
	public static Scalar getMSSIM(String srcFilename,String desFilename)
	{
	    Mat i1 = Imgcodecs.imread(srcFilename);
	    Mat i2 = Imgcodecs.imread(desFilename);
	    /*
	    if(i1.size().width > i2.size().width)
	    {
	    	Imgproc.resize(i1, i1, i2.size(), 0, 0, Imgproc.INTER_AREA);
	    }
	    else
	    {
	    	Imgproc.resize(i1, i1, i2.size(), 0, 0, Imgproc.INTER_CUBIC);
	    }
	    //
	    if(i1.size().width > i2.size().width)
	    {
	    	Imgproc.resize(i2, i2, i1.size(), 0, 0, Imgproc.INTER_CUBIC);
	    }
	    else
	    {
	    	Imgproc.resize(i2, i2, i1.size(), 0, 0, Imgproc.INTER_AREA);
	    }
	    */
	    if(i1.size().width > i2.size().width)
	    {
	    	Imgproc.resize(i1, i1, i2.size(), 0, 0, Imgproc.INTER_AREA);
	    }
	    else
	    {
	    	Imgproc.resize(i2, i2, i1.size(), 0, 0, Imgproc.INTER_AREA);
	    }
	    double C1 = 6.5025, C2 = 58.5225;
	    int d = CvType.CV_32F;
	    Mat I1=new Mat(), I2=new Mat();
	    i1.convertTo(I1, d);
	    i2.convertTo(I2, d);
	    
	    Mat I2_2 = I2.mul(I2);
	    Mat I1_2 = I1.mul(I1);
	    Mat I1_I2 = I1.mul(I2);
	    
	    Mat mu1=new Mat(), mu2=new Mat();
	    Imgproc.GaussianBlur(I1, mu1, new Size(11, 11), 1.5);
	    Imgproc.GaussianBlur(I2, mu2, new Size(11, 11), 1.5);
	    
	    Mat mu1_2 = mu1.mul(mu1);
	    Mat mu2_2 = mu2.mul(mu2);
	    Mat mu1_mu2 = mu1.mul(mu2);
	    
	    Mat sigma1_2=new Mat(), sigma2_2=new Mat(), sigma12=new Mat();
	    Imgproc.GaussianBlur(I1_2, sigma1_2, new Size(11, 11), 1.5);
	    Core.subtract(sigma1_2, mu1_2, sigma1_2);
	    Imgproc.GaussianBlur(I2_2, sigma2_2, new Size(11, 11), 1.5);
	    Core.subtract(sigma2_2, mu2_2, sigma2_2);
	    Imgproc.GaussianBlur(I1_I2, sigma12, new Size(11, 11), 1.5);
	    Core.subtract(sigma12, mu1_mu2, sigma12);

	    Mat t1=new Mat(), t2=new Mat(), t3;
	    Core.multiply(mu1_mu2, new Scalar(2), t1);
	    Core.add(t1, new Scalar(C1), t1);
	    
	    Core.multiply(sigma12, new Scalar(2), t2);
	    Core.add(t2, new Scalar(C2), t2);

	    t3 = t1.mul(t2);
	    Core.add(mu1_2, mu1_2, t1);
	    Core.add(t1, new Scalar(C1), t1);

	    Core.add(sigma1_2, sigma2_2, t2);
	    Core.add(t2, new Scalar(C2), t2);

	    t1 = t1.mul(t2);
	    Mat ssim_map=new Mat();
	    Core.divide(t3, t1, ssim_map);
	    Scalar mssim = Core.mean(ssim_map);
	    return mssim;
	}
	
	public static int[]  colorStat(String filename, Mat inData)
	{
		int[] outData = new int[inData.cols()];
		Mat mat = Imgcodecs.imread(filename);
		mat.convertTo(mat, CvType.CV_8UC3);
		Imgproc.GaussianBlur(mat, mat, new Size(11, 11), 1.5);

		int m = mat.rows();
		int n = mat.cols();
		int len = inData.cols();
		double dist = 0;
		double minDist = 0;
		int index = 0;
		for(int i = 0;i < m;i++)
		{
			for(int j = 0;j < n;j++)
			{
				double[] color = mat.get(i, j);
				for(int k = 0;k < len;k++)
				{
					double[] colorStd = inData.get(0, k);
					for(int l = 0;l < mat.channels();l++)
					{
						dist += (color[l] - colorStd[l]) * (color[l] - colorStd[l]);
					}
					//System.out.println("dist[" + k + "] = " + dist);
					if(k == 0)
					{
						index = k;
						minDist = dist;
					}
					if(dist < minDist)
					{
						index = k;
						minDist = dist;
					}
					dist = 0;
				}
				outData[index] += 1;
			}
		}
		return outData;
	}
	
	public static int[]  colorStat2(String filename, Mat inData)
	{
		int[] outData = new int[inData.cols()];
		Mat mat = Imgcodecs.imread(filename);
		Imgproc.cvtColor( mat, mat, Imgproc.COLOR_BGR2HSV );
		mat.convertTo(mat, CvType.CV_8UC3);
		Imgproc.cvtColor( inData, inData, Imgproc.COLOR_BGR2HSV );

		int m = mat.rows();
		int n = mat.cols();
		int len = inData.cols();
		double dist = 0;
		double minDist = 0;
		int index = 0;
		for(int i = 0;i < m;i++)
		{
			for(int j = 0;j < n;j++)
			{
				double[] color = mat.get(i, j);
				for(int k = 0;k < len;k++)
				{
					double[] colorStd = inData.get(0, k);
					for(int l = 0;l < mat.channels();l++)
					{
						dist += (color[l] - colorStd[l]) * (color[l] - colorStd[l]);
					}
					//System.out.println("dist[" + k + "] = " + dist);
					if(k == 0)
					{
						index = k;
						minDist = dist;
					}
					if(dist < minDist)
					{
						index = k;
						minDist = dist;
					}
					dist = 0;
				}
				outData[index] += 1;
			}
		}
		return outData;
	}
	
	public static void train(String savePath)
	{
		//用于存放所有样本矩阵
		Mat trainingDataMat = null;
		
		//标记：正样本用 0 表示，负样本用 1 表示。
		//图片命名：
		//正样本： 0.jpg  1.jpg  2.jpg  3.jpg
		//负样本：4.jpg  5.jpg  6.jpg  7.jpg   ...   17.jpg
        int labels[] = {0,0,0,0,
                           1,1,1,1,1,1,1,
                           1,1,1,1,1,1,1};

        //存放标记的Mat,每个图片都要给一个标记。SAMPLE_NUMBER 是自己定义的图片数量
        Mat labelsMat = new Mat(SAMPLE_NUMBER,1,CvType.CV_32SC1);
        labelsMat.put(0, 0, labels);

        //这里的意思是，trainingDataMat 存放18张图片的矩阵，trainingDataMat 的每一行存放一张图片的矩阵。
        for(int i = 0;i<SAMPLE_NUMBER;i++)
        {
        	String path = "D:\\xunlian\\a\\" + i + ".jpg" ;
            Mat src = Imgcodecs.imread(path);

            //创建一个行数为18(正负样本总数量为18),列数为 rows*cols 的矩阵
            if(trainingDataMat == null) {
                trainingDataMat = new Mat(SAMPLE_NUMBER, src.rows()*src.cols(),CvType.CV_32FC1);// CV_32FC1 是规定的训练用的图片格式。
            }

            //转成灰度图并检测边缘
            //这里是为了过滤不需要的特征，减少训练时间。实际处理按情况论。
            Imgproc.cvtColor(src, src, Imgproc.COLOR_BGR2GRAY);
            Mat dst = new Mat(src.rows(),src.cols(),src.type());//此时的 dst 是8u1c。
            Imgproc.Canny(src, dst, 130, 250);

            //转成数组再添加。
            //失败案例:这里我试图用 get(row,col,data)方法获取数组，但是结果和这个结果不一样，原因未知。
            float[] arr =new float[dst.rows()*dst.cols()];
            int l=0;
            for (int j=0;j<dst.rows();j++){
                for(int k=0;k<dst.cols();k++) {
                    double[] a=dst.get(j, k);
                    arr[l]=(float)a[0];
                    l++;
                }
            } 
            trainingDataMat.put(i, 0, arr);
        }

        SVM svm = SVM.create();
        // 配置SVM训练器参数
        TermCriteria criteria = new TermCriteria(TermCriteria.EPS + TermCriteria.MAX_ITER, 1000, 0);
        svm.setTermCriteria(criteria);// 指定
        svm.setKernel(SVM.LINEAR);// 使用预先定义的内核初始化
        svm.setType(SVM.C_SVC); // SVM的类型,默认是：SVM.C_SVC
        svm.setGamma(0.5);// 核函数的参数
        svm.setNu(0.5);// SVM优化问题参数
        svm.setC(1);// SVM优化问题的参数C

        TrainData td = TrainData.create(trainingDataMat, Ml.ROW_SAMPLE, labelsMat);// 类封装的训练数据
        boolean success = svm.train(td.getSamples(), Ml.ROW_SAMPLE, td.getResponses());// 训练统计模型
        System.out.println("Svm training result: " + success);
        svm.save(savePath);// 保存模型
	}
	
	public static void predict(String savePath)
	{
		Mat src = Imgcodecs.imread("D:\\xunlian\\a\\0.jpg");//图片大小要和样本一致
		Imgproc.cvtColor(src, src, Imgproc.COLOR_BGR2GRAY);
		Mat dst = new Mat();
		Imgproc.Canny(src, dst, 40, 200);
        SVM svm = SVM.load(savePath);//加载训练得到的 xml
        
        Mat samples = new Mat(1, src.cols() * src.rows(), CvType.CV_32FC1);
        
        //转换 src 图像的 cvtype
        //失败案例：我试图用 src.convertTo(src, CvType.CV_32FC1); 转换，但是失败了，原因未知。猜测: 内部的数据类型没有转换？
        float[] dataArr = new float[src.cols() * src.rows()];
        for(int i =0,f = 0 ;i<src.rows();i++)
        {
        	for(int j = 0;j<src.cols();j++)
        	{
        		float pixel = (float)src.get(i, j)[0];
        		dataArr[f] = pixel;
        		f++;
        	}
        }
        samples.put(0, 0, dataArr);
        
        //预测用的方法，返回定义的标识。
        //      int labels[]  = {9,9,9,9,
        //                 1,1,1,1,1,1,1,
        //                 1,1,1,1,1,1,1};
        //      如果训练时使用这个标识，那么符合的图像会返回9.0
        float flag = svm.predict(samples);
        
        System.out.println("预测结果："+flag);
        if(flag == 0)
        {
            System.out.println("目标符合");
        }
        else if(flag == 1)
        {
        	System.out.println("目标不符合");
        }
	}
}
