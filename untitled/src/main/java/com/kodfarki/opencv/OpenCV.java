package com.kodfarki.opencv;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;

/**
 * User: Halil Karakose
 * Date: 13/07/16
 * Time: 17:09
 */
public abstract class OpenCV {
    static {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }

    public static Mat openFile(String fileName) throws Exception {
        Mat newImage = Imgcodecs.imread(fileName);
        if (newImage.dataAddr() == 0) {
            throw new Exception("Couldn't open file " + fileName);
        }
        return newImage;
    }

    public static void show(Mat newImage) {
        ImageViewer imageViewer = new ImageViewer();
        imageViewer.show(newImage, "Loaded image");
    }
}
