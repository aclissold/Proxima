package com.siteshot.siteshot;

import android.app.Fragment;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;




        import android.graphics.Bitmap;
        import android.graphics.BitmapFactory;
        import android.hardware.Camera;
        import android.hardware.Camera.PictureCallback;
        import android.os.Bundle;
        import android.app.Activity;
        import android.view.Menu;
        import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
        import android.widget.ImageView;
        import android.widget.Toast;


import com.siteshot.siteshot.utils.PhotoUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CameraFragment extends Fragment implements View.OnClickListener {

    private Camera cameraObject;
    private ShowCamera showCamera;
    private ImageView pic;


    public static Camera isCameraAvailiable(){
        Camera object = null;
        try {
            object = Camera.open();
        }
        catch (Exception e){
        }
        return object;
    }

    /**
     * Picture Callback for handling a picture capture and saving it out to a file.
     */
    private PictureCallback capturedIt = new PictureCallback() {
        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            File pictureFile = getOutputMediaFile();
            if (pictureFile == null){
                Toast.makeText(getActivity(), "Image retrieval failed.", Toast.LENGTH_SHORT).show();
                return;
            }
            try {
                FileOutputStream fos = new FileOutputStream(pictureFile);
                fos.write(data);
                fos.close();
// Restart the camera preview.
                //safeCameraOpenInView(mCameraView);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    };
    /**
     * Used to return the camera File output.
     * @return
     */
    private File getOutputMediaFile(){
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "UltimateCameraGuideApp");
        if (! mediaStorageDir.exists()){
            if (! mediaStorageDir.mkdirs()){
                Log.d("Camera Guide", "Required media storage does not exist");
                return null;
            }
        }
// Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File mediaFile;
        mediaFile = new File(mediaStorageDir.getPath() + File.separator + "IMG_"+ timeStamp + ".jpg");
        Toast.makeText(getActivity(), "Image saved", Toast.LENGTH_SHORT)
                .show();
        return mediaFile;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rV = inflater.inflate(R.layout.camera_fragment, container, false);
        Button upButton = (Button) rV.findViewById(R.id.button_capture);
        upButton.setOnClickListener(this);
        return rV;
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        pic = (ImageView)getActivity().findViewById(R.id.imageView1);
        cameraObject = isCameraAvailiable();
        showCamera = new ShowCamera(getActivity(), cameraObject);
        FrameLayout preview = (FrameLayout) getView().findViewById(R.id.camera_preview);
        preview.addView(showCamera);
    }

    @Override
    public void onClick(View v) {
            snapIt(v);



    }

    public void snapIt(View view){
        cameraObject.takePicture(null, null, capturedIt);
    }


}