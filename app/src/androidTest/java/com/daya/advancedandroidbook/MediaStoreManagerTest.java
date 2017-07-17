package com.daya.advancedandroidbook;

import android.database.Cursor;

import org.junit.Assert;
import org.junit.Test;

/**
 * Created by shnoble on 2017-07-17.
 */
public class MediaStoreManagerTest {


    @Test
    public static void getImage() throws Exception {
        MediaStoreManager mediaStoreManager = new MediaStoreManager();
        Cursor cursor = mediaStoreManager.getImage();
        Assert.assertNotNull(cursor);
    }
}