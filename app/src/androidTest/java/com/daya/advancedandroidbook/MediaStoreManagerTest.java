package com.daya.advancedandroidbook;

import android.content.Context;
import android.database.Cursor;
import android.support.test.InstrumentationRegistry;

import org.junit.Assert;
import org.junit.Test;

/**
 * Created by shnoble on 2017-07-17.
 */
public class MediaStoreManagerTest {

    @Test
    public void getImage() throws Exception {
        Context context = InstrumentationRegistry.getTargetContext();
        MediaStoreManager mediaStoreManager = new MediaStoreManager();
        Cursor cursor = mediaStoreManager.getImage(context);
        Assert.assertNotNull(cursor);
    }
}