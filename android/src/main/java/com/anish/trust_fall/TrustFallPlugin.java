package com.anish.trust_fall;

import android.content.Context;

import androidx.annotation.NonNull;

import io.flutter.embedding.engine.FlutterEngine;
import io.flutter.embedding.engine.plugins.FlutterPlugin;
import io.flutter.plugin.common.BinaryMessenger;
import io.flutter.plugin.common.MethodChannel;

/** TrustFallPlugin */
public class TrustFallPlugin implements FlutterPlugin {
  /** Plugin registration. */
  private MethodChannel channel;
  private MainMethodCallHandler methodCallHandler;

  @Override
  public void onAttachedToEngine(@NonNull FlutterPluginBinding binding) {
    Context applicationContext = binding.getApplicationContext();
    BinaryMessenger messenger = binding.getBinaryMessenger();
    methodCallHandler = new MainMethodCallHandler(applicationContext, messenger);

    channel = new MethodChannel(messenger, "trust_fall");
    channel.setMethodCallHandler(methodCallHandler);
    binding.getFlutterEngine().addEngineLifecycleListener(new FlutterEngine.EngineLifecycleListener() {
      @Override
      public void onPreEngineRestart() {
        methodCallHandler.dispose();
      }

      @Override
      public void onEngineWillDestroy() {
      }
    });
  }

  @Override
  public void onDetachedFromEngine(@NonNull FlutterPluginBinding binding) {
    methodCallHandler.dispose();
    methodCallHandler = null;

    channel.setMethodCallHandler(null);
  }
}
