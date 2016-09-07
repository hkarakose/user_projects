package com.kodfarki.opencv;

import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;

/**
 * User: Halil Karakose
 * Date: 13/07/16
 * Time: 17:08
 */
public class Main02 extends OpenCV{
    public static void main(String[] args) throws Exception {
        Mat mat = openFile("/Users/halil/Pictures/Wallpaper01.jpg");
        show(mat);
        filter(mat);
        show(mat);
    }

    /**
     * Set blue channel to 0 (zero)
     */
    public static void filter(Mat image) {
        int totalBytes = (int) (image.total() * image.elemSize());
        byte buffer[] = new byte[totalBytes];
        image.get(0, 0, buffer);
        for (int i = 0; i < totalBytes; i++) {
            if (i % 3 == 0) buffer[i] = 0;
        }
        image.put(0, 0, buffer);
    }
}
