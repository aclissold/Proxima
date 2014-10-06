package com.siteshot.siteshot;

import android.app.Fragment;
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

    private PictureCallback capturedIt = new PictureCallback() {

        @Override
        public void onPictureTaken(byte[] data, Camera camera) {

            Bitmap bitmap = BitmapFactory.decodeByteArray(data , 0, data .length);
            if(bitmap==null){
                Toast.makeText(getActivity().getApplicationContext(), "not taken", Toast.LENGTH_SHORT).show();
            }
            else
            {
                Toast.makeText(getActivity().getApplicationContext(), "taken", Toast.LENGTH_SHORT).show();
            }
            cameraObject.release();
            cameraObject = isCameraAvailiable();
            showCamera = new ShowCamera(getActivity(), cameraObject);
            FrameLayout preview = (FrameLayout) getView().findViewById(R.id.camera_preview);
            preview.addView(showCamera);


        }
    };

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