package com.kodfarki.ml;

import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.impl.model.file.FileDataModel;

import java.io.File;
import java.io.IOException;

class StringItemIdFileDataModel extends FileDataModel {

    //initialize migrator to covert String to unique long
    public ItemMemIDMigrator memIdMigtr;

    public StringItemIdFileDataModel(File dataFile, String regex) throws IOException {
        super(dataFile, regex);
    }

    @Override
    protected long readItemIDFromString(String value) {

        if (memIdMigtr == null) {
            memIdMigtr = new ItemMemIDMigrator();
        }

        // convert to long
        long retValue = memIdMigtr.toLongID(value);
        //store it to cache
        if (null == memIdMigtr.toStringID(retValue)) {
            try {
                memIdMigtr.singleInit(value);
            } catch (TasteException e) {
                e.printStackTrace();
            }
        }
        return retValue;
    }

    // convert long back to String
    String getItemIDAsString(long itemId) {
        return memIdMigtr.toStringID(itemId);
    }
}