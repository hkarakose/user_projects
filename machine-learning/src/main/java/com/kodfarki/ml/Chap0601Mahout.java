package com.kodfarki.ml;

import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.impl.model.file.FileDataModel;
import org.apache.mahout.cf.taste.model.DataModel;

import java.io.File;
import java.io.IOException;

/**
 * User: Halil Karakose
 * Date: 10/07/16
 * Time: 16:13
 */
public class Chap0601Mahout {
    public static void main(String[] args) throws IOException, TasteException {
        StringItemIdFileDataModel model = new StringItemIdFileDataModel(
                new File("datasets/chap6/BX-Book-Ratings.csv"), ";");
        System.out.println("Total items: " + model.getNumItems() + "\nTotal users: " +model.getNumUsers());
    }
}
