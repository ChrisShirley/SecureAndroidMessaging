package com.android.secure.messaging;

import org.junit.runners.model.InitializationError;
import org.robolectric.RobolectricTestRunner;

public class CustomRobolectricTestRunner extends RobolectricTestRunner {

  public CustomRobolectricTestRunner(Class<?> testClass) throws InitializationError {
    super(testClass);
    String buildVariant = BuildConfig.BUILD_TYPE + (BuildConfig.FLAVOR.isEmpty()? "" : "/" + BuildConfig.FLAVOR);
    System.setProperty("android.package", BuildConfig.APPLICATION_ID);
    System.setProperty("android.manifest", "build/intermediates/manifests/full/" + buildVariant + "/AndroidManifest.xml");
    System.setProperty("android.resources", "build/intermediates/res/" + buildVariant);
    System.setProperty("android.assets", "build/intermediates/assets/" + buildVariant);
  }
}