package com.proxima.test;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Handler;
import android.test.ActivityInstrumentationTestCase2;

import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.proxima.activities.ProfileActivity;
import com.proxima.utils.PhotoUtils;

import java.io.IOException;
import java.util.Random;

/**
 * Created by aclissold on 10/6/14.
 */
public class PhotoUtilsTest extends ActivityInstrumentationTestCase2<ProfileActivity> {

    private ProfileActivity mProfileActivity;

    public PhotoUtilsTest() {
        super(ProfileActivity.class);
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
        mProfileActivity = getActivity();
    }

    public void testPhotoUpload() throws IOException {
        // Create the photo file.
        PhotoUtils.getInstance().createPhotoFile();
        String photoPath = PhotoUtils.getInstance().getCurrentPhotoPath();
        assertNotNull(photoPath);

        boolean rotateFlag = false;
        boolean selfFlag = false;
        // Write mock data to it.
        final Bitmap mockBitmap = createMockBitmap();

        // Upload it to Parse.
        PhotoUtils.getInstance().uploadPhoto(mockBitmap, new ParseGeoPoint(), null, rotateFlag, selfFlag);


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

    public void testDescriptionAssociated() throws IOException {
        // Create the photo file.
        PhotoUtils.getInstance().createPhotoFile();
        String photoPath = PhotoUtils.getInstance().getCurrentPhotoPath();
        assertNotNull(photoPath);

        boolean rotateFlag = false;
        boolean selfFlag = false;
        // Write mock data to it.
        final Bitmap mockBitmap = createMockBitmap();

        // Upload it to Parse.
        String description = "test description";
        PhotoUtils.getInstance().uploadPhoto(mockBitmap, new ParseGeoPoint(), description, rotateFlag, selfFlag);


        // Get it back from Parse.
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                ParseQuery query = new ParseQuery("UserPhoto");
                query.orderByDescending("createdAt");
                String foundDesc = null;
                try {
                    ParseObject object = query.getFirst();
                    foundDesc = object.getString("description");
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                assertNotNull(foundDesc);

                // Verify its contents.
                assertEquals("test description", foundDesc);
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
