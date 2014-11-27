NOTE: this file is in Markdown format and is best viewed on GitHub at the
following location: https://github.com/aclissold/Proxima/blob/master/README.md

Proxima
=======

Proxima is a new social media image sharing app with a twist. Take a photo to
add it to the public map. Proxima users can then view and comment on it—but only
once they've gotten close enough! Have a blast exploring campus, the park,
or wherever, discovering hundreds of memories worth thousands of words.

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
4. Now that you're on the **Configure AVD** screen, most default settings
    should be appropriate, but you'll want to use hardware acceleration and
    need to enable emulated front and back cameras (using a webcam may not
    work properly). Ensure your settings match the screenshot below exactly
    and click **Finish**.

![AVD Configuration](https://raw.githubusercontent.com/aclissold/Proxima/master/art/screenshots/avd-configuration.png)

### Getting the Code Ready

If you don't already have the source code, run the following command:

    git clone https://github.com/aclissold/Proxima.git

If you don't have git, a zip can also be obtained
[here](https://github.com/aclissold/Proxima/releases).

Finally, take the `debug.keystore` file and move it to
`~/.android/debug.keystore` on your computer.

* Linux / OS X: `mv debug.keystore ~/.android/debug.keystore`
* Windows: Stick it in `C:\Users\<your username>\.android\debug.keystore`.

### Build & Run

If you're opening the project for the first time, you'll need to select
**Import Project…** from within Android Studio to load the newly
cloned/downloaded source code.

After opening the project, simply **Debug**
![Debug](https://developer.android.com/images/tools/as-debugbutton.png)
(or **Run**) the project, starting up the Android Virtual Device you configured
in the previous step. If the Camera app does not show an emulated camera image,
restart the emulator.

Finally, the app will not work properly if you don't emulate location. The
easiest way to do this: from the command line, simply run the provided
`./geo.sh` shell script to emulate a default location. You may have to run it
twice.

Running Unit Tests
------------------

### Creating a Test Configuration

Unfortunately, Android Studio stores the configuration needed to perform unit
tests in a file that needs to be excluded in `.gitignore` because it differs
for each team member, necessitating this extra step before running the tests.

Within Android Studio,

1. From the system menu, select **Run** → **Edit Configurations…**
2. Click the plus symbol (+) followed by Android Tests to add a test
    configuration
3. Name it "test" or something similar next to **Name:**
4. Set the module to "app"
5. Test "All in package"
6. Set the package to test to `com.proxima.test`
7. If desired, set target device to "Show chooser dialog". You can also specify
    it to only use the AVD you created previously.
8. Click "OK".

### Running the Test Configuration

Simply toggle the configuration dropdown between "app" and "test" when
you want to run the app or the tests, respectively. Then **Run** or **Debug**
as before.

![Test](https://raw.githubusercontent.com/aclissold/Proxima/master/art/screenshots/test.png)

### Helpful Links

* [`android.test` JavaDocs](http://developer.android.com/reference/android/test/package-summary.html)
* [Android Unit Testing Fundamentals](http://developer.android.com/tools/testing/testing_android.html)
* [Unit Testing With Android Studio](http://rexstjohn.com/unit-testing-with-android-studio/)

Screenshots
-----------

TODO
