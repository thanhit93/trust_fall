package com.anish.trust_fall;

import android.content.Context;
import android.location.Location;
import android.os.Looper;

import androidx.annotation.NonNull;

import com.anish.trust_fall.Emulator.EmulatorCheck;
import com.anish.trust_fall.ExternalStorage.ExternalStorageCheck;
import com.anish.trust_fall.MockLocation.MockLocationCheck;
import com.anish.trust_fall.Rooted.RootedCheck;

import io.flutter.plugin.common.BinaryMessenger;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;

public class MainMethodCallHandler implements MethodCallHandler {

    private final Context applicationContext;
    private final BinaryMessenger messenger;

    public MainMethodCallHandler(Context applicationContext,
                                 BinaryMessenger messenger) {
        this.applicationContext = applicationContext;
        this.messenger = messenger;
    }

    @Override
    public void onMethodCall(MethodCall call, @NonNull final Result result) {
        if (call.method.equals("getPlatformVersion")) {
            result.success("Android " + android.os.Build.VERSION.RELEASE);
        } else if (call.method.equals("isJailBroken")) {
            result.success(RootedCheck.isJailBroken(applicationContext));
        } else if (call.method.equals("canMockLocation")) {
            MockLocationCheck.LocationResult locationResult = new MockLocationCheck.LocationResult(){
                @Override
                public void gotLocation(final Location location){
                    android.os.Handler handler = new android.os.Handler(Looper.getMainLooper());
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            //Got the location!
                            if(location != null){
                                result.success(location.isFromMockProvider());
                            }else {
                                result.success(false);
                            }
                        }
                    });
                }
            };
            MockLocationCheck mockLocationCheck = new MockLocationCheck();
            mockLocationCheck.getLocation(applicationContext, locationResult);
        }else if (call.method.equals("isRealDevice")) {
            result.success(!EmulatorCheck.isEmulator());
        }else if (call.method.equals("isOnExternalStorage")) {
            result.success(ExternalStorageCheck.isOnExternalStorage(applicationContext));
        }
        else {
            result.notImplemented();
        }
    }

    void dispose() {
        // do nothing
    }
}
