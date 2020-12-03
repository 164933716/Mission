
# Quick start

After completing the page preparation work according to the "Preparation" chapter, you can actually start the map development work. This article takes you to quickly understand: the most basic usage methods of maps, layers, point markers, vector graphics, information forms, and events.

## HELLO, AMAP!

Simply creating a map requires only one line of code. The `container` in the construction parameter is the `id` of the map container added in the preparation phase:

```javascript
var map = new AMap.Map('container');
```
When creating, you can set the center point, level, display mode, custom style and other attributes of the map:
```javascript
var map = new AMap.Map('container', {
	zoom:11,//level
	center: [116.397428, 39.90923],//Center point coordinates 
	viewMode:'3D'//Use 3D view
});
```
## Layer

By default, the map only displays the standard basemap. If you need to overlay other layers, you can add layers through the `map.add` method:
To
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
map.add(trafficLayer);//Add a layer to the map
```
You can also set multiple layers for the map through the `layers` property when the map is initialized:
To
```javascript
var map = new AMap.Map('container', {
	center: [116.397428, 39.90923],
	layers: [//Use multiple layers
	new AMap.TileLayer.Satellite(),
	new AMap.TileLayer.RoadNet() ],
	zooms: [4,18],//Set the map level range
	zoom: 13
});
```

Map JS API provides multiple official layers such as standard, satellite, road network, road conditions, buildings, etc. It also provides an interface to load standard layers such as third-party WMS, WMTS, XYZ, etc. It also provides general images, Canvas , Video, heat and other capabilities as layers, [View layer related tutorial](https://lbs.amap.com/api/javascript-api/guide/layers/official-layers).

## Point mark and vector graphics
JS API provides the ability to draw overlays on the map, such as Point Marker, Text Marker, and Circle Marker.

The method of adding a point marker is very simple, such as adding a default style Marker:
```javascript
var marker = new AMap.Marker({
	position:[116.39, 39.9]//position
})
map.add(marker);//Add to the map
```
The removal method is as follows:
```javascript
map.remove(marker)
```
[View point marker related tutorial](https://lbs.amap.com/api/javascript-api/guide/overlays/marker)

It also provides the ability to draw vector graphics such as Circle, Polyline, Polygon, Ellipse, Rectangle, Besizer Curve, BesizerCurve, such as adding a polyline:
```javascript
  var lineArr = [
        [116.368904, 39.913423],
        [116.382122, 39.901176],
        [116.387271, 39.912501],
        [116.398258, 39.904600]
    ];
    var polyline = new AMap.Polyline({
        path: lineArr, //Set the line cover path
        strokeColor: "#3366FF", //Line color
        strokeWeight: 5, //line width
        strokeStyle: "solid", //line style
    });
    map.add(polyline);
```
[View the vector graphics tutorial](https://lbs.amap.com/api/javascript-api/guide/overlays/vector-overlay)

## Event function and information form
The instances of Map, point markers, and vector graphics provided by JS API all support events, and mouse or touch operations will trigger corresponding events. We have a simple understanding of [Event System](https://lbs.amap.com/api/javascript-api/guide/events/map_overlay) and [Information Form](https: //lbs.amap.com/api/javascript-api/guide/overlays/infowindow) basic usage:
```javascript
var infoWindow = new AMap.InfoWindow({ //Create information window
	isCustom: true, //Use custom form
	content:'<div>Information form</div>', //The content of the information form can be any html fragment
	offset: new AMap.Pixel(16, -45)
});
var onMarkerClick = function(e) {
	infoWindow.open(map, e.target.getPosition());//Open the information window
//e.target is the Marker that was clicked
}
var marker = new AMap.Marker({
	position: [116.481181, 39.989792]
})
map.add(marker);
marker.on('click',onMarkerClick);//Binding the click event
```