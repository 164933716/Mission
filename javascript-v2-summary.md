# Overview

On February 18, 2020, [Map JS API v1.4.15][1] released an update;
On March 31, 2020, [Map JS API v2.0][2] was released, welcome to use.

[1]: https://lbs.amap.com//lbs.amap.com/api/javascript-api/changelog
[2]: https://lbs.amap.com/api/jsapi-v2/summary

## Introduction

AMap JS API is a set of map application programming interface developed in JavaScript language, integrated design of mobile terminal and PC terminal, a set of API is compatible with many system platforms. Currently JS API is free and open to use.

JS API provides 2D and 3D map modes to meet the needs of most developers for map display, map customization, layer loading, point marker addition, and vector graphics drawing. It also provides POI search, route planning, and geocoding. , Administrative area query, positioning and many other open service interfaces.

JS API has a complete document body. For system learning maps and service interface calls, please go to [Tutorial][3]. For a quick understanding of all core capabilities of JS API, please go to [Sample Center][4]. For detailed interface documentation, please go to [Reference Manual][5]. If you encounter any problems, please refer to [FAQ][6] or [Submit Work Order][7].

[3]: https://lbs.amap.com/api/javascript-api/guide/abc/prepare
[4]: https://lbs.amap.com/demo-center/js-api
[5]: https://lbs.amap.com/api/javascript-api/reference/core
[6]: https://lbs.amap.com/faq/web/javascript-api
[7]: https://lbs.amap.com/dev/ticket/type/javascript

## Function overview
### Layer

    Official layer
        Standard vector layer TileLayer
        Satellite layer TileLayer.Satellite
        Road network data TileLayer.RoadNet
        Real-time traffic data TileLayer.Traffic
        Building Block Layer Buildings
        Indoor Map IndoorMap
        3D stereo graphics layer Object3DLayer
        
    Three-party standard layer
        WMS layer TileLayer.WMS
        WMTS layer TileLayer.WMTS
        XYZ tile layer TileLayer
        
        
    Show own data
        Image layer ImageLayer
        Video layer VideoLayer
        Canvas layer CanvasLayer
        Any tile layer TileLayer.Flexible
        Fully custom layer CustomLayer
        Heatmap Heatmap
        
### Point mark

    Point mark
        General point marker Marker
        Text mark
        CircleMarker
        Flexible point marking ElasticMarker
        
    Mass dot mark
        MassMarks
        Point aggregation MarkerCluster
        
### Information Form

    InfoWindow
    Advanced InfoWindow
    
### Vector Graphics

    Circle
    Polyline
    Polygon
    Ellipse
    RecTangle
    Curve BesizerCurve
    
### 3D stereo graphics

    Mesh
    MeshAcceptLights
    Prism
    Wide line MeshLine
    
### Map control

    Scale
    Zoom ToolBar ToolBar
    Hawkeye OverView
    Layer switching MapType
    Location button Geolocation
    
### Coordinate transformation

    Coordinate system transformation
        Latitude and longitude <-> map container coordinates
        Latitude and longitude <-> map plane coordinates
        Latitude and longitude <-> 3D world coordinates
        
    Map coordinate conversion
        GPS(WGS84) -> Gaode (GCJ02, Mars)
        Baidu -> Gaode
        MapBar -> Gaode
        
### Right-click menu

ContexMenu

### Service interface

    search for
        Input prompt Autocomplete
        POI Search PlaceSearch
        
    Route planning
        Driving
        Truck
        Walking
        Riding
        Bus Transfer
        
    District query
        DistrictSearch
        
    Geocoding
        Geocoder
        
    Positioning
        Initial positioning Map
        City Positioning CitySearch
        Browser location Geolocation
        
    other service
        Weather
        Bus station StationSearch
        Bus line LineSearch

### Support library

    Spatial geometry calculation
        GeometryUtil

    Basic library
        Universal Support Library Util
        DOM support library DomUtil
        Browser information Browser


## Features

### Multi-view mode

    2D view vertical viewing angle
    3D view 360 degree rotation perspective
    
### Perfect function

    map
        Cover all common map functions
        
    service
        Strong data support of Gaode map
    
### Interactive control

    Pan
        Mouse/single finger drag
        Up, down, left and right buttons
        
    Zoom
        Mouse wheel, double click, two fingers
        
    3D viewing angle control
        Ctrl+mouse drag
        Rotate with two fingers / slide with two fingers
        
### Excellent performance

    Draw on demand
        Load on demand, viewport crop

    Advanced drawing technology
        SVG, Canvas, CSS3, WebGL
        
### Flexible and open

    Custom map style and content
    Custom raster layer
    Custom layer
    Custom point mark, information form
    
### Visual Features

    Zoom animation
    Drag inertia
    Stepless zoom (3D)
    Perfect resolution of HD screen definition
    
### Good compatibility
    
    Mac, Windows (including touch screen)
    Chrome, FireFox
    IE 6+, Edge
    Safari 5
    360、It's a good thing.
    Ios, Android, WinPhone
    Safari for iOS 6+
    Android Browser 2.3+
    Chrome for Mobile
    FireFox for Mobile
    UC, WeChat, QQ
    iOS WebView, Android WebView

## Function introduction and experience 

  * Map
    ```javascript
    var map = new AMap.Map('container', {
            resizeEnable: true,
            zoom:11,
            center: [116.397428, 39.90923]
     });
    ```
  * Point mark
    ```javascript
    var marker, map = new AMap.Map("container", {
            resizeEnable: true,
            center: [116.397428, 39.90923],
            zoom: 13
        });
     AMap.event.addDomListener(document.getElementById('addMarker'), 'click', function() {
            addMarker();
        }, false);
     AMap.event.addDomListener(document.getElementById('updateMarker'), 'click', function() {
            marker && updateMarker();
        }, false);
     AMap.event.addDomListener(document.getElementById('clearMarker'), 'click', function() {
            if (marker) {
                marker.setMap(null);
                marker = null;
            }
        }, false);
     // Instantiation point marker
        function addMarker() {
            if (marker) {
                return;
            }
            marker = new AMap.Marker({
                icon: "https://webapi.amap.com/theme/v1.3/markers/n/mark_b.png",
                position: [116.405467, 39.907761]
            });
            marker.setMap(map);
        }
    function updateMarker() {
            // Custom point tag content
            var markerContent = document.createElement("div");
    
            // Click the icon in the marker
            var markerImg = document.createElement("img");
            markerImg.className = "markerlnglat";
            markerImg.src = "https://webapi.amap.com/theme/v1.3/markers/n/mark_r.png";
            markerContent.appendChild(markerImg);
      // Point the text in the marker
            var markerSpan = document.createElement("span");
            markerSpan.className = 'marker';
            markerSpan.innerHTML = "Hi，I changed my equipment!";
            markerContent.appendChild(markerSpan);
            marker.setContent(markerContent); //Update point mark content
            marker.setPosition([116.391467, 39.927761]); //Update point marker position
        }
    ```
  * Layer
    ```javascript
    var map = new AMap.Map('container', {
            resizeEnable: true,
            center: [116.397428, 39.90923],
            zoom: 13
        });
        //Real-time traffic layer
        var trafficLayer = new AMap.TileLayer.Traffic({
            zIndex: 10
        });
        trafficLayer.setMap(map);
     var isVisible = true;
        AMap.event.addDomListener(document.getElementById('control'), 'click', function() {
            if (isVisible) {
                trafficLayer.hide();
                isVisible = false;
            } else {
                trafficLayer.show();
                isVisible = true;
            }
        }, false);
    ```
  * search for
    ```javascript
    var map = new AMap.Map("container", {
        resizeEnable: true
    });
    AMap.service(["AMap.PlaceSearch"], function() {
        var placeSearch = new AMap.PlaceSearch({ //Construct location query class
            pageSize: 5,
            pageIndex: 1,
            city: "010", //city
            map: map,
            panel: "panel"
        });
        //Keyword query
        placeSearch.search('Fang Heng');
    });
    ```
  * route plan
    ```javascript
    var map = new AMap.Map("container", {
        resizeEnable: true,
        center: [116.397428, 39.90923],//Map center point
        zoom: 13 //The zoom level of the map display
    });
    //Construct route navigation class
    var driving = new AMap.Driving({
        map: map,
        panel: "panel"
    }); 
    // Plan driving and navigation routes according to the longitude and latitude of the start and end points
    driving.search(new AMap.LngLat(116.379028, 39.865042), new AMap.LngLat(116.427281, 39.903719));
    ```
  * Geocoding
    ```javascript
        function geocoder() {
        var geocoder = new AMap.Geocoder({
            city: "010", //City, default: "National"
            radius: 1000 //Range, default: 500
        });
        //Geocoding, return geocoding results
        geocoder.getLocation("Suzhou Street, Haidian District, Beijing", function(status, result) {
            if (status === 'complete' && result.info === 'OK') {
                geocoder_CallBack(result);
            }
        });
    }
    
    function addMarker(i, d) {
        var marker = new AMap.Marker({
            map: map,
            position: [d.location.getLng(), d.location.getLat()]
        });
        var infoWindow = new AMap.InfoWindow({
            content: d.formattedAddress,
            offset: {
                x: 0,
                y: -30
            }
        });
        marker.on("mouseover", function(e) {
            infoWindow.open(map, marker.getPosition());
        });
    }
    //Geocoding return result display
    function geocoder_CallBack(data) {
        var resultStr = "";
        //Geocoding result array
        var geocode = data.geocodes;
        for (var i = 0; i < geocode.length; i++) {
            //Splicing output html
            resultStr += "<span style=\"font-size: 12px;padding:0px 0 4px 2px; border-bottom:1px solid #C1FFC1;\">" + "<b>address</b>：" + geocode[i].formattedAddress + "" + "&nbsp;&nbsp;<b>The geocoding result is:</b><b>&nbsp;&nbsp;&nbsp;&nbsp;coordinate</b>：" + geocode[i].location.getLng() + ", " + geocode[i].location.getLat() + "" + "<b>&nbsp;&nbsp;&nbsp;&nbsp;Match level</b>：" + geocode[i].level + "</span>";
            addMarker(i, geocode[i]);
        }
        map.setFitView();
        document.getElementById("result").innerHTML = resultStr;
    }
    ```
