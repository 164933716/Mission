
# Gaode Development Android Map SDK Getting Started Guide

This guide is a quick start guide for adding maps to Android apps.

## Step 1: Download and install Android Studio

Follow the guide [download](https://developer.android.com/sdk/index.html) and [install](http://developer.android.com/sdk/installing/index.html?pkg=studio)
Android Studio. (Note: The download address is the official Google website)

## Step 2: Obtain High German Key

[Click me to get Key>>](/dev/#/)

[Click me to check how to obtain the necessary data SHA1 and package name for Key registration>>](/faq/top/hot-questions/249)

## Step 3: Create project

Follow the steps below to create a new Empty Activity application project.

  1. Start Android Studio. If you see the Welcome to Android Studio dialog box, please select Start a new Android Studio project, otherwise, please click File in the Android Studio menu bar, then click New->New Project, and enter your application name and company domain as prompted And project location. Then click Next.
  2. Select the model required for your application. If you are not sure what you need, just choose Phone and Tablet. Then click Next.
  3. Select Empty Activity in the "Add an activity to Mobile" dialog box. Then click Next.
  4. Enter the Activity name, layout name, and title as prompted. Just use the default value. Then click Finish.

## Step 4: Download and install the map development package

Download the development package from [Official Website Download](/api/android-sdk/download/) as required and unzip it.

Take the map function of version 3.3.3 as an example. After decompression, you will get an AMap3DMap_3.3.3_20160726.jar file and an armeabi
Folder (the folder contains: libgdinamapv4sdk752.so, libgdinamapv4sdk752ex.so, libtbt3631.so, libwtbt144.so
Four so files).

For specific operation steps, please refer to the Android Stuido jar and so file installation steps in the development guide.

## Step 5: Hello AMap view code

Please check the following files in the Android Studio project.

1. Configure AndroidManifest.xml file

Configure Key in the application tag of AndroidManifest.xml:

[XML]
    
    <meta-data
        android:name="com.amap.api.v2.apikey"
        android:value="Your Key"/>
    

Configure permissions in AndroidManifest.xml:

[XML]
 
    //Basic permissions required for map packages and search packages
         
    <!--Allow the program to open the network socket-->
    <uses-permission android:name="android.permission.INTERNET" />
    <!--Allow the program to set the write permission of the built-in SD card-->
    <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
    <!--Allow the program to get the network status-->
    <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
    <!--Allow the program to access WiFi network information-->
    <uses-permission android:name="android.permission.ACCESS_WIFI_STATE" />
    <!--Allow the program to read and write the phone status and identity-->
    <uses-permission android:name="android.permission.READ_PHONE_STATE" />
    <!--Allow the program to access CellID or WiFi hotspot to get a rough location-->
    <uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION" />
    

2. Configure the XML layout file Add a map control in the layout xml file;

[XML]

    
    
    <com.amap.api.maps.MapView
        android:id="@+id/map"
        android:layout_width="match_parent"
        android:layout_height="match_parent">
    </com.amap.api.maps.MapView>

3. Map display

[Java]

    
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.basicmap_activity);//Set the corresponding XML layout file
        
        MapView mapView = (MapView) findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);// This method must be rewritten
        AMap aMap = mapView.getMap();
        
    }

## Step 6: Connect Android device

The easiest way to see how your app is actually running is to connect your Android device to your computer. Follow the instructions on your Android
Enable developer options on the device and configure your application and system to detect the device.

In addition, you can also use the Android emulator to run your app. Use Android Virtual Device (AVD) Manager
To configure one or more virtual devices, you can connect the devices with the Android emulator to build and run your application.

## Step 7: Build and run your application

In Android Studio, click the Run menu option (or the play button icon) to run your application.

When prompted to select a device, choose one of the following options:

Select the Android device connected to your computer. In addition, you can also select the Launch emulator radio button, and then select the virtual device you have configured before and click OK.
Android Studio will call Gradle to build your app, and then display the results on the device or emulator. It may take a few minutes for the app to open.

You can see that there is a map display in your project. If you do not see the map, please check whether you are connected to the Internet. If you see the map but cannot use related functions such as POI search and path planning, please confirm whether you have added your AutoNavi Key in AndroidManifest.xml.

## Next steps

You may want to watch some sample codes, you can go to [Related Download](/api/android-sdk/download/) to download the official sample Demo.