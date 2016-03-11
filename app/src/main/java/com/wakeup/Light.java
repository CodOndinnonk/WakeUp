package com.wakeup;


import android.hardware.Camera;

import java.security.Policy;

public class Light {
    private Camera camera;
    Camera.Parameters parameters;
    boolean activeFlash;


    public Light(){
        camera = Camera.open();
        parameters = camera.getParameters();
    }

    public void onFlash() {// не работает
        if (activeFlash) {
            String mode = parameters.getFlashMode();
            if (mode.equals(Camera.Parameters.FLASH_MODE_OFF)){
                mode = Camera.Parameters.FLASH_MODE_TORCH;}
            else {
                mode = Camera.Parameters.FLASH_MODE_OFF;
            }
            parameters.setFlashMode(mode);
            camera.setParameters(parameters);
            try {
                Thread.sleep(500L);

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
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
        activeFlash = false;
    }


}
