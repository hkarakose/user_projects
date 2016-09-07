package com.kodfarki.opencv;

import org.opencv.core.Mat;
import org.opencv.videoio.VideoCapture;

import javax.swing.*;
import java.awt.*;

/**
 * User: Halil Karakose
 * Date: 13/07/16
 * Time: 22:21
 */
public class Main03VideoRead extends OpenCV {

    private JFrame frame;
    private JLabel imageLabel;

    public static void main(String[] args) throws InterruptedException {
        Main03VideoRead app = new Main03VideoRead();
        app.initGUI();
        app.runMainLoop(args);
    }

    private void initGUI() {
        frame = new JFrame("Video Playback Example");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 400);
        imageLabel = new JLabel();
        frame.add(imageLabel);
        frame.setVisible(true);
    }

    private void runMainLoop(String[] args) throws InterruptedException {
        ImageProcessor imageProcessor = new ImageProcessor();
        Mat webcamMatImage = new Mat();
        Image tempImage;
        VideoCapture capture = new VideoCapture("/Users/halil/Projects/user_projects/untitled/src/main/resources/tree.avi");
        if (capture.isOpened()) {
            while (true) {
                capture.read(webcamMatImage);
                if (!webcamMatImage.empty()) {
                    tempImage = imageProcessor.toBufferedImage(webcamMatImage);
                    ImageIcon imageIcon = new ImageIcon(tempImage, "Video playback");
                    imageLabel.setIcon(imageIcon);
                    frame.pack();  //this will resize the window to fit the image
                    Thread.sleep(50);
                } else {
                    System.out.println(" Frame not captured or video has finished");
                    break;
                }
            }
        } else {
            System.out.println("Couldn't open video file.");
        }

    }
}
