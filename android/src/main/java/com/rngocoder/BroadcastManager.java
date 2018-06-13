package com.rngocoder;

import android.content.Context;
import com.wowza.gocoder.sdk.api.WowzaGoCoder;
import com.wowza.gocoder.sdk.api.broadcast.WZBroadcast;
import com.wowza.gocoder.sdk.api.broadcast.WZBroadcastConfig;
import com.wowza.gocoder.sdk.api.configuration.WZMediaConfig;
import com.wowza.gocoder.sdk.api.configuration.WowzaConfig;
import com.wowza.gocoder.sdk.api.devices.WZAudioDevice;
import com.wowza.gocoder.sdk.api.devices.WZCameraView;
import com.wowza.gocoder.sdk.api.errors.WZStreamingError;
import com.wowza.gocoder.sdk.api.logging.WZLog;


import com.wowza.gocoder.sdk.api.status.WZStatusCallback;

/**
 * Edited by countaight 6/13/18
 */


public class BroadcastManager {
    private static WZBroadcastConfig mWZBroadcastConfig = null;

    public static WZBroadcast initBroadcast(Context localContext, String hostAddress, String applicationName, String broadcastName, String sdkLicenseKey, String username, String password, int sizePreset, WZCameraView cameraView, WZAudioDevice audioDevice){
        WZBroadcast goCoderBroadcast = new WZBroadcast();
        mWZBroadcastConfig = new WZBroadcastConfig();
        // Update the active config to the defaults for 720p video

        mWZBroadcastConfig.setAudioBitRate(22400);
        mWZBroadcastConfig.setVideoFramerate(12);
        mWZBroadcastConfig.set(getSizePresetWithInt(sizePreset));

        // Set the address for the Wowza Streaming Engine server or Wowza Cloud
        mWZBroadcastConfig.setHostAddress(hostAddress);
        mWZBroadcastConfig.setUsername(username);
        mWZBroadcastConfig.setPassword(password);
        mWZBroadcastConfig.setApplicationName(applicationName);
        // Set the name of the stream
        mWZBroadcastConfig.setStreamName(broadcastName);

        mWZBroadcastConfig.setVideoBroadcaster(cameraView);
        mWZBroadcastConfig.setAudioBroadcaster(audioDevice);

        // Update the active config
        return goCoderBroadcast;

    }
    public static void startBroadcast(WZBroadcast goCoderBroadcast, WZStatusCallback callback){
        if (!goCoderBroadcast.getStatus().isRunning()) {
            // Validate the current broadcast config
            WZStreamingError configValidationError = mWZBroadcastConfig.validateForBroadcast();
            if (configValidationError != null) {
                WZLog.error(configValidationError);
            } else {
                // Start the live stream
                goCoderBroadcast.startBroadcast(mWZBroadcastConfig, callback);
            }
        }
    }
    public static void stopBroadcast(WZBroadcast goCoderBroadcast, WZStatusCallback callback){
        if (goCoderBroadcast.getStatus().isRunning()) {
            // Stop the live strea
            goCoderBroadcast.endBroadcast(callback);
        }
    }
    public static void invertCamera(WZCameraView cameraView){
        cameraView.switchCamera();
    }
    public static void turnFlash(WZCameraView cameraView, boolean on){
        cameraView.getCamera().setTorchOn(on);
    }
    public static void mute(WZAudioDevice audioDevice, boolean muted){
        audioDevice.setMuted(muted);
    }
    public static void changeStreamName(String broadcastName){
        mWZBroadcastConfig.setStreamName(broadcastName);
    }

    private static WZMediaConfig getSizePresetWithInt(int sizePreset){
        switch (sizePreset){
            case 0: //FRAME_SIZE_176x144
                return WZMediaConfig.FRAME_SIZE_176x144;
            case 1: //FRAME_SIZE_320x240
                return WZMediaConfig.FRAME_SIZE_320x240;
            case 2: //FRAME_SIZE_352x288
                return WZMediaConfig.FRAME_SIZE_352x288;
            case 3: //FRAME_SIZE_640x480
                return WZMediaConfig.FRAME_SIZE_640x480;
            case 4: //FRAME_SIZE_960x540
                return WZMediaConfig.FRAME_SIZE_960x540;
            case 5: //FRAME_SIZE_1280x720
                return WZMediaConfig.FRAME_SIZE_1280x720;
            case 6: //FRAME_SIZE_1440x1080
                return WZMediaConfig.FRAME_SIZE_1440x1080;
            case 7: //FRAME_SIZE_1920x1080
                return WZMediaConfig.FRAME_SIZE_1920x1080;
            case 8: //FRAME_SIZE_3840x2160
                return  WZMediaConfig.FRAME_SIZE_3840x2160;
            default:
                return WZMediaConfig.FRAME_SIZE_640x480;
        }
    }
}