NOTE: this file is in Markdown format and is best viewed on GitHub at the
following location: https://github.com/aclissold/Proxima/blob/master/README.md

Proxima
=======

Proxima is blah blah.

Table of Contents
-----------------

* [Running the App](#running-the-app)  
  * [Prerequisites](#prerequisites)  
  * [Getting the Code Ready](#getting-the-code-ready)  
  * [Build & Run](#build-&-run)  
* [Running Unit Tests](#running-unit-tests)  
  * [Creating a Test Configuration](#creating-a-test-configuration)  
  * [Running the Test Configuration](#running-the-test-configuration)  
  * [Helpful Links](#helpful-links)  
* [Screenshots](#screenshots)  

Running the App
---------------

### Prerequisites

If you haven't already, you will need to
[download Android Studio](https://developer.android.com/sdk/installing/studio.html)
with the Android SDK.

After you've installed Android Studio, launch it and run the **SDK Manager**
![SDK Manager](https://developer.android.com/images/tools/sdk-manager-studio.png).

Note that you may have to generate a dummy project for this button to be
visible. These are the exact packages you'll need to install by clicking their
respective checkboxes and then **Install packages…**. If the exact
versions are not available, newer versions will probably suffice.

* **Tools**
  * Android SDK Tools rev. 23.0.5
  * Android SDK Platform-tools rev. 21
  * Android SDK Build-tools rev. 21.1.1
* **Android 5.0 (API 21)**
  * SDK Platform API 21 rev. 1  
  * Google APIs Intel x86 Atom (not Atom_64) System Image API 21 rev. 2
* **Extras**
  * Android Support Repository rev. 9
  * Android Support Library rev. 21.0.2
  * Intel x86 Emulator Accelerator (HAXM Installer) rev. 5.2

### Setting Up the Emulator

If you're not deploying to a physical device, here's how to configure the
emulator.

From within Android Studio, run the AVD Manager ![AVD Manager](https://developer.android.com/images/tools/avd-manager-studio.png).

Then, follow these steps.

1. **Create Virtual Device…**
2. Choose **Nexus 6** and click **Next**
3. Select Lollipop (API 21) targeting Google APIs (this is important or the
    Google map won't work) and click **Next**

### Getting the Code Ready

debug.keystore

### Build & Run

click play

Running Unit Tests
------------------

### Creating a Test Configuration

Within Android Studio,

1. Choose Run → Edit Configurations
2. Click the green `+` followed by Android Tests to add a test configuration
3. Name it "test" or something similar
4. Set the module to "app"
5. Test "All in package"
6. Set the package to test to `com.siteshot.siteshot.test`
7. If desired, set target device to "Show chooser dialog"
8. Click "OK".

### Running the Test Configuration

Simply toggle the configuration dropdown between "app" and "test" when
you want to run the app or the tests, respectively.

### Helpful Links

* [`android.test` JavaDocs](http://developer.android.com/reference/android/test/package-summary.html)
* [Android Unit Testing Fundamentals](http://developer.android.com/tools/testing/testing_android.html)
* [Unit Testing With Android Studio](http://rexstjohn.com/unit-testing-with-android-studio/)

Screenshots
-----------
