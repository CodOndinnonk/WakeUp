package com.wakeup;


import android.hardware.Camera;

public class Light {
    private Camera camera;
    Camera.Parameters parameters;

    public Light(){
        camera = Camera.open();
        parameters = camera.getParameters();
    }


    public void onLight(){
        parameters.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
        camera.setParameters(parameters);
        camera.startPreview();
    }

    public void offLight(){
        parameters.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
        camera.setParameters(parameters);
        camera.startPreview();
    }


}
