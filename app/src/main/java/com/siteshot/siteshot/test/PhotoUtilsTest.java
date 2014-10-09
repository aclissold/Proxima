package com.siteshot.siteshot.test;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Handler;
import android.test.ActivityInstrumentationTestCase2;

import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.siteshot.siteshot.activities.ProfileActivity;
import com.siteshot.siteshot.utils.PhotoUtils;

import java.io.IOException;
import java.util.Random;

/**
 * Created by aclissold on 10/6/14.
 */
public class PhotoUtilsTest extends ActivityInstrumentationTestCase2<ProfileActivity> {

    private ProfileActivity mProfileActivity;
    private PhotoUtils mPhotoUtils;

    public PhotoUtilsTest() {
        super(ProfileActivity.class);
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
        mProfileActivity = getActivity();
        mPhotoUtils = new PhotoUtils();
    }

    public void testPhotoUpload() throws IOException {
        // Create the photo file.
        mPhotoUtils.createPhotoFile();
        String photoPath = mPhotoUtils.getCurrentPhotoPath();
        assertNotNull(photoPath);

        boolean rotateFlag = false;
        // Write mock data to it.
        final Bitmap mockBitmap = createMockBitmap();

        // Upload it to Parse.
        mPhotoUtils.uploadPhoto(mockBitmap, rotateFlag);

        // Get it back from Parse.
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                ParseQuery query = new ParseQuery("UserPhoto");
                query.orderByDescending("createdAt");
                ParseFile file = null;
                try {
                    ParseObject object = query.getFirst();
                    file = (ParseFile) object.get("photo");
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                assertNotNull(file);

                // Verify its contents.
                byte[] data = null;
                try {
                    data = file.getData();
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                assertNotNull(data);
                Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);

                assertEquals(mockBitmap, bitmap);
            }
        }, 2000);
    }

    private Bitmap createMockBitmap() {
        final int w = 25;
        final int h = 25;
        Bitmap bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);

        for (int x = 0; x < w; x++) {
            for (int y = 0; y < h; y++) {
                Random rnd = new Random();
                int color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
                bitmap.setPixel(x, y, color);
            }
        }

        return bitmap;
    }

}
