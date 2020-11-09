# Amap Development Android Positioning SDK Overview

## What is positioning SDK

Android Positioning SDK is a simple set of LBS service positioning interface, you can use this set of positioning API to obtain positioning results, inverse geocoding (address text description), and geofence functions.

## Target readers

The Android Positioning SDK is provided to readers who have certain Android programming experience and understand object-oriented concepts.

## Function introduction and experience

   Basic positioning
   
    //Declare mlocationClient object
    public AMapLocationClient mlocationClient;
    //Declare mLocationOption object
    public AMapLocationClientOption mLocationOption = null;
    mlocationClient = new AMapLocationClient(this);
    //Initialize positioning parameters
    mLocationOption = new AMapLocationClientOption();
    //Set up location monitoring
    mlocationClient.setLocationListener(this);
    //Set positioning mode to high-precision mode, Battery_Saving is low-power mode, Device_Sensors is device-only mode
    mLocationOption.setLocationMode(AMapLocationMode.Hight_Accuracy);
    //Set the positioning interval, in milliseconds, the default is 2000ms
    mLocationOption.setInterval(2000);
    //Set positioning parameters
    mlocationClient.setLocationOption(mLocationOption);
    // This method initiates a positioning request at regular intervals. In order to reduce power consumption or network traffic consumption,
    // Pay attention to set the appropriate positioning time interval (the minimum interval supported is 1000ms), and call the stopLocation() method at the appropriate time to cancel the positioning request
    // After the positioning is over, call the onDestroy() method in the appropriate life cycle
    // In the case of a single positioning, no matter whether the positioning is successful or not, there is no need to call the stopLocation() method to remove the request. The positioning sdk will be removed internally
    //Start positioning
    mlocationClient.startLocation();
    @Override
    public void onLocationChanged(AMapLocation amapLocation) {
       if (amapLocation != null) {
            if (amapLocation.getErrorCode() == 0) {
            //Callback information for positioning success, set related messages
            amapLocation.getLocationType();//Get the current location result source, such as the network location result, see the location type table for details
            amapLocation.getLatitude();//Get latitude
            amapLocation.getLongitude();//Get longitude
            amapLocation.getAccuracy();//Get accuracy information
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = new Date(amapLocation.getTime());
            df.format(date);//location time
        } else {
            //Display error information ErrCode is the error code, errInfo is the error information, see the error code table for details.
            Log.e("AmapError","location Error, ErrCode:"
                + amapLocation.getErrorCode() + ", errInfo:"
                + amapLocation.getErrorInfo());
            }
        }
    }

  Inverse geocoding
    
    //Declare mLocationOption object
    public AMapLocationClientOption mLocationOption = null;
    mlocationClient = new AMapLocationClient(this);
    //Initialize positioning parameters
    mLocationOption = new AMapLocationClientOption();
    //Set the return address information, the default is true
    mLocationOption.setNeedAddress(true);
    //Set up location monitoring
    mlocationClient.setLocationListener(this);
    //Set the positioning mode to high-precision mode, Battery_Saving is low-power mode, Device_Sensors is device-only mode
    mLocationOption.setLocationMode(AMapLocationMode.Hight_Accuracy);
    //Set the positioning interval, in milliseconds, the default is 2000ms
    mLocationOption.setInterval(2000);
    //Set positioning parameters
    mlocationClient.setLocationOption(mLocationOption);
    //This method is to initiate a positioning request every fixed time. In order to reduce power consumption or network traffic consumption,
    //Pay attention to set the appropriate positioning time interval (the minimum interval supported is 1000ms), and call the stopLocation() method at the appropriate time to cancel the positioning request
    //After the positioning is over, call the onDestroy() method in the appropriate life cycle
    //In the case of a single positioning, no matter whether the positioning is successful or not, there is no need to call the stopLocation() method to remove the request. The positioning sdk will be removed internally
    //Start positioning
    mlocationClient.startLocation();
    @Override
    public void onLocationChanged(AMapLocation amapLocation) {
    if (amapLocation != null) {
            if (amapLocation.getErrorCode() == 0) {
            //Positioning success callback information, setting related messages
            amapLocation.getLocationType();//Get the current location result source, such as the network location result, see the location type table for details
            amapLocation.getLatitude();//Get latitude
            amapLocation.getLongitude();//Get longitude
            amapLocation.getAccuracy();//Get accuracy information
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = new Date(amapLocation.getTime());
            df.format(date);//location time
            amapLocation.getAddress();//Address, if isNeedAddress is set to false in option, there will be no such result. There will be address information in the network positioning result, and GPS positioning will not return address information.
            amapLocation.getCountry();//Country information
            amapLocation.getProvince();//Province information
            amapLocation.getCity();//City information
            amapLocation.getDistrict();//Urban area information
            amapLocation.getStreet();//Street information
            amapLocation.getStreetNum();//Street house number information
            amapLocation.getCityCode();//City code
            amapLocation.getAdCode();//Area code
            amapLocation.getAOIName();//Get the AOI information of the current anchor point
        } else {
            //Display error information ErrCode is the error code, errInfo is the error information, see the error code table for details.
            Log.e("AmapError","location Error, ErrCode:"
                + amapLocation.getErrorCode() + ", errInfo:"
                + amapLocation.getErrorInfo());
            }
        }
    }

[Download complete sample code](https://lbs.amap.com/api/android-location-sdk/download/)

  

## Account and Key application

There are three steps to register as a Amap developer:

The first step is to register a Amap developer;

The second step is to go to the console to create an application;

The third step is to obtain the Key.

See the figure below for specific steps

[Get API Key](/dev/key)

## Compatibility

AutoNavi positioning SDK supports Android 2.2 and above systems.