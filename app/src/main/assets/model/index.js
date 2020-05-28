var terrainExaggeration = 1.0;
var terrainProvider = new Cesium.CesiumTerrainProvider({url:"https://sinomaps-mission.oss-accelerate.aliyuncs.com/Terrain",proxy : new Cesium.DefaultProxy('/proxy/')});//({url:"http://47.110.155.250:8003/Map/Terrain"});
var viewer = new Cesium.Viewer('cesiumContainer', {
    shadows: false,
    animation: false,
    homeButton: false,
    geocoder: false,
    baseLayerPicker: false,
    timeline: false,
    fullscreenButton: true,
    scene3DOnly: false,
    sceneModePicker: false,
    navigationInstructionsInitiallyVisible: false,
    navigationHelpButton: false,
    selectionIndicator: false,

    infoBox: false,
    selectionIndicator: false,
    shouldAnimate: false,

    terrainExaggeration: terrainExaggeration,
    terrainProvider: terrainProvider,
    
    imageryProvider : new Cesium.UrlTemplateImageryProvider({url:"http://47.110.155.250:8003/Global/Image/{z}/{x}/{y}.png"})
});

if (!viewer.scene.pickPositionSupported) {
    window.alert("This browser does not support pickPosition.");
}

// var mapboxLayer = new Cesium.MapboxStyleImageryProvider({
//     url : 'https://api.mapbox.com/styles/v1/',
//     username:'wingwuyf',
//     styleId: 'cjajfiz60artm2rnxr8wiecli',
//     accessToken: 'pk.eyJ1Ijoid2luZ3d1eWYiLCJhIjoiY2s3b282bm90MDJ1dDNtbGlibGowMTRoeSJ9.hDrbU176NROA7KYhmBkmEg',
//     scaleFactor: true
// });

var mapboxLayer = new Cesium.UrlTemplateImageryProvider({url:"https://map-cache.oss-cn-hangzhou.aliyuncs.com/styles/v1/wingwuyf/cjajfiz60artm2rnxr8wiecli/tiles/256/{z}/{x}/{y}?access_token=pk.eyJ1Ijoid2luZ3d1eWYiLCJhIjoiY2s3b282bm90MDJ1dDNtbGlibGowMTRoeSJ9.hDrbU176NROA7KYhmBkmEg"});

var googleLayer = new Cesium.UrlTemplateImageryProvider({url:"http://mt2.google.cn/vt/lyrs=s@167000000&hl=zh-CN&gl=cn&x={x}&y={y}&z={z}&s=Galil"});

viewer.imageryLayers.removeAll();
viewer.imageryLayers.addImageryProvider(mapboxLayer);

viewer.imageryLayers.addImageryProvider(new Cesium.UrlTemplateImageryProvider({url:"https://sinomaps-mission.oss-accelerate.aliyuncs.com/Image/{z}/{x}/{y}.png",proxy : new Cesium.DefaultProxy('/proxy/')}));//({url:"http://47.110.155.250:8003/Map/Image/{z}/{x}/{y}.png"}));

viewer._cesiumWidget._creditContainer.style.display = "none";

viewer.scene.globe.depthTestAgainstTerrain = true;

//viewer.scene.screenSpaceCameraController.minimumZoomDistance = 10; //相机的高度的最小值
//viewer.scene.screenSpaceCameraController.maximumZoomDistance = 80000; //22000000; //相机高度的最大值
//viewer.scene.screenSpaceCameraController._minimumZoomRate = 1000; //30000; //相机缩小时的速率
//viewer.scene.screenSpaceCameraController._maximumZoomRate = 1000000; //5906376272000; //相机放大时的速率
//viewer.scene.screenSpaceCameraController.maximumMovementRatio = 0.01;

// viewer.scene.skyBox.show = false;
// viewer.scene.backgroundColor = new Cesium.Color(48/255, 57/255, 28/255);
// viewer.scene.sun.show = false; 
// viewer.scene.moon.show = false;
// viewer.scene.skyAtmosphere.show = false;

var west = 108.5506111775;
var south = 27.6176314387;
var east = 108.8606161775;
var north = 28.0951804387;
var center = {x : (west + east) / 2, y : (south + north) / 2};

// var position = Cesium.Cartographic.toCartesian(new Cesium.Cartographic.fromDegrees(108.690813, 27.918094, 0));
// var distance = 7500;
// var clippingPlanes = new Cesium.ClippingPlaneCollection({
//    modelMatrix : Cesium.Transforms.eastNorthUpToFixedFrame(position),
//    planes : [
//        new Cesium.ClippingPlane(new Cesium.Cartesian3( 1.0,  0.0, 0.0), distance),
//        new Cesium.ClippingPlane(new Cesium.Cartesian3(-1.0,  0.0, 0.0), distance),
//        new Cesium.ClippingPlane(new Cesium.Cartesian3( 0.0,  1.0, 0.0), distance),
//        new Cesium.ClippingPlane(new Cesium.Cartesian3( 0.0, -1.0, 0.0), distance)
//    ],
//    unionClippingRegions: true
// });
// viewer.scene.globe.clippingPlanes = clippingPlanes;
// viewer.scene.globe.backFaceCulling = false;
// viewer.scene.globe.showSkirts = false;

// var request = new XMLHttpRequest();
// request.open("get", "./test.geojson");
// request.send(null);
// request.onload = function () {
//     if (request.status == 200) {
//         var geojson = JSON.parse(request.responseText);

//         var positions = [];
//         for (var j = 0; j < geojson.features[0].geometry.coordinates.length; j++) {
//             var coordinates = geojson.features[0].geometry.coordinates[j];
//             for (var k = 0; k < coordinates.length - 1; k++) {
//                 var coordinate = coordinates[k];

//                 positions.push(coordinate[0]);
//                 positions.push(coordinate[1]);
//             }
//         }

//         var points = Cesium.Cartesian3.fromDegreesArray(positions);
//         var pointsLength = points.length;
        
//         var clippingPlanes = [];
//         for (var i = 0; i < pointsLength; ++i) {
//             var nextIndex = (i + 1) % pointsLength;
//             var midPoint = Cesium.Cartesian3.add(points[i], points[nextIndex], new Cesium.Cartesian3());
//             midPoint = Cesium.Cartesian3.multiplyByScalar(midPoint, 0.5, midPoint);
            
//             var up = Cesium.Cartesian3.normalize(midPoint, new Cesium.Cartesian3());
//             var right = Cesium.Cartesian3.subtract(points[nextIndex], midPoint, new Cesium.Cartesian3());
//             right = Cesium.Cartesian3.normalize(right, right);
            
//             var normal = Cesium.Cartesian3.cross(right, up, new Cesium.Cartesian3());
//             normal = Cesium.Cartesian3.normalize(normal, normal);
            
//             var originCenteredPlane = new Cesium.Plane(normal, 0.0);
//             var distance = Cesium.Plane.getPointDistance(originCenteredPlane, midPoint);
            
//             clippingPlanes.push(new Cesium.ClippingPlane(normal, distance));
//         }

//         viewer.scene.globe.clippingPlanes = new Cesium.ClippingPlaneCollection({
//             planes : clippingPlanes,
//             edgeWidth: 1.0,
//             edgeColor: Cesium.Color.YELLOW,
//             // unionClippingRegions: true
//         });
//         viewer.scene.globe.backFaceCulling = false;
//         viewer.scene.globe.showSkirts = false;
//     }
// }


function recenter() {

    var position = Cesium.Cartesian3.fromDegrees(userLocation.longitude, userLocation.latitude, userLocation.height);

    var pitch = Cesium.Math.toRadians(-30);
    var angle = 360 / 10;
    var distance = 12000;
    var startTime = Cesium.JulianDate.fromDate(new Date());
    
    var stopTime = Cesium.JulianDate.addSeconds(startTime, 10, new Cesium.JulianDate());

    viewer.clock.startTime = startTime.clone();
    viewer.clock.stopTime = stopTime.clone();
    viewer.clock.currentTime = startTime.clone();
    viewer.clock.clockRange = Cesium.ClockRange.CLAMPED;
    viewer.clock.clockStep = Cesium.ClockStep.SYSTEM_CLOCK;
    var initialHeading = viewer.camera.heading;

    // 相机绕点旋转飞行
    var exec = function TimeExecution() {
            var delTime = Cesium.JulianDate.secondsDifference(viewer.clock.currentTime, viewer.clock.startTime);
            var heading = Cesium.Math.toRadians(delTime * angle) + initialHeading;
            viewer.scene.camera.setView({
                destination : position,
                orientation: {
                    heading : heading,
                    pitch : pitch,
                }
            });
            viewer.scene.camera.moveBackward(distance);
            if (Cesium.JulianDate.compare(viewer.clock.currentTime, viewer.clock.stopTime) >= 0) {
                viewer.clock.onTick.removeEventListener(exec);
            }
    };
    viewer.clock.onTick.addEventListener(exec);

    // // 相机原地旋转飞行
    // var exec = function TimeExecution() {
    //         var delTime = Cesium.JulianDate.secondsDifference(viewer.clock.currentTime, viewer.clock.startTime);
    //         var heading = Cesium.Math.toRadians(delTime * angle) + initialHeading;
    //         viewer.scene.camera.setView({
    //             orientation: {
    //                 heading : heading,
    //                 pitch : pitch,

    //             }
    //         });
    //         if (Cesium.JulianDate.compare(viewer.clock.currentTime, viewer.clock.stopTime) >= 0) {
    //             viewer.clock.onTick.removeEventListener(exec);
    //         }
    // };
    // viewer.clock.onTick.addEventListener(exec);
}

function getCurrentExtent() {
    var extent = {};

    var scene = viewer.scene;

    var ellipsoid = scene.globe.ellipsoid;
    var canvas = scene.canvas;

    var car3_lt = viewer.camera.pickEllipsoid(new Cesium.Cartesian2(0, 0), ellipsoid);

    var car3_rb = viewer.camera.pickEllipsoid(new Cesium.Cartesian2(canvas.width, canvas.height), ellipsoid);

    if (car3_lt && car3_rb) {
        var carto_lt = ellipsoid.cartesianToCartographic(car3_lt);
        var carto_rb = ellipsoid.cartesianToCartographic(car3_rb);
        
        extent.xmin = Cesium.Math.toDegrees(carto_lt.longitude);
        extent.ymax = Cesium.Math.toDegrees(carto_lt.latitude);
        extent.xmax = Cesium.Math.toDegrees(carto_rb.longitude);
        extent.ymin = Cesium.Math.toDegrees(carto_rb.latitude);
    }
    else if (!car3_lt && car3_rb) {
        var car3_lt2 = null;
        var yIndex = 0;

        do {
            yIndex <= canvas.height ? yIndex += 10 : canvas.height;
            car3_lt2 = viewer.camera.pickEllipsoid(new Cesium.Cartesian2(0, yIndex), ellipsoid);
        } while (!car3_lt2);
        
        var carto_lt2 = ellipsoid.cartesianToCartographic(car3_lt2);
        var carto_rb2 = ellipsoid.cartesianToCartographic(car3_rb);
        
        extent.xmin = Cesium.Math.toDegrees(carto_lt2.longitude);
        extent.ymax = Cesium.Math.toDegrees(carto_lt2.latitude);
        extent.xmax = Cesium.Math.toDegrees(carto_rb2.longitude);
        extent.ymin = Cesium.Math.toDegrees(carto_rb2.latitude);
    }

    extent.height = Math.ceil(viewer.camera.positionCartographic.height);

    return extent;
}

function sleep (time) {
    return new Promise((resolve) => setTimeout(resolve, time));
}

viewer.camera.setView({
    destination : Cesium.Rectangle.fromDegrees(west, south, east, north)
});

viewer.scene.camera.moveStart.addEventListener(function(){

});

viewer.scene.camera.moveEnd.addEventListener(function(){
    var outFlag = false;
    var extent = getCurrentExtent();
    var xmax = extent.xmax >= extent.xmin ? extent.xmax : extent.xmin;
    var xmin = extent.xmax >= extent.xmin ? extent.xmin : extent.xmax;
    var ymax = extent.ymax >= extent.ymin ? extent.ymax : extent.ymin;
    var ymin = extent.ymax >= extent.ymin ? extent.ymin : extent.ymax;
    
    if (xmax < west) {
        outFlag = true;
    }

    if (xmin > east) {
        outFlag = true;
    }

    if (ymax < south) {
        outFlag = true;
    }

    if (ymin > north) {
        outFlag = true;
    }

    if (outFlag) {
        // viewer.camera.setView({
        //    destination : Cesium.Rectangle.fromDegrees(west, south, east, north)
        // });
    }

});

viewer.camera.changed.addEventListener(function() {
    var heading = Math.round(Cesium.Math.toDegrees(viewer.camera.heading));
    var pitch = Math.round(Cesium.Math.toDegrees(viewer.camera.pitch));
});

var userLocation = {longitude: 108.7107853492, latitude: 27.8601391146, height: 1446.697};

var userEntity = viewer.entities.add({
    position : Cesium.Cartesian3.fromDegrees(userLocation.longitude, userLocation.latitude, userLocation.height),
    point : {
        show : false
    }
});

function updateUserLocation(location) {
    userLocation = location;

    promise = Cesium.sampleTerrainMostDetailed(terrainProvider, [Cesium.Cartographic.fromDegrees(location.longitude, location.latitude)]);
    Cesium.when(promise, function (updatedPositions) {
        if (updatedPositions.length > 0) {
            var htmlOverlay = document.getElementById('imgOverlay');
            var scratch = new Cesium.Cartesian2();

            viewer.scene.preRender.addEventListener(function() {
                userLocation.height = updatedPositions[0].height;

                userEntity.position = Cesium.Cartesian3.fromDegrees(userLocation.longitude, userLocation.latitude, userLocation.height);

                var position = Cesium.Cartesian3.fromDegrees(Cesium.Math.toDegrees(updatedPositions[0].longitude), Cesium.Math.toDegrees(updatedPositions[0].latitude), updatedPositions[0].height * terrainExaggeration);
                var canvasPosition = viewer.scene.cartesianToCanvasCoordinates(position, scratch);
                if (Cesium.defined(canvasPosition)) {
                    htmlOverlay.style.top = canvasPosition.y - htmlOverlay.offsetHeight / 2 + 'px';
                    htmlOverlay.style.left = canvasPosition.x - htmlOverlay.offsetWidth / 2 + 'px';
                }
            });
        }
    });
}

updateUserLocation(userLocation);

var userTourPostitions = [

    {
        "latitude": 27.917657686039245,
        "longitude": 108.69037617890788,
        "height" : 3000
    },
    {
        "latitude": 27.91692192666803,
        "longitude": 108.69131740982795,
        "height" : 3000
    },
    {
        "latitude": 27.9165244381843,
        "longitude": 108.69121086652304,
        "height" : 3000
    },
    {
        "latitude": 27.91623926720558,
        "longitude": 108.69150739963742,
        "height" : 3000
    },
    {
        "latitude": 27.91549104263241,
        "longitude": 108.69217944202228,
        "height" : 3000
    },
    {
        "latitude": 27.91520140130523,
        "longitude": 108.69191774037297,
        "height" : 3000
    },
    {
        "latitude": 27.914740210355973,
        "longitude": 108.69218819645468,
        "height" : 3000
    },
    {
        "latitude": 27.914097411500606,
        "longitude": 108.69209152516927,
        "height" : 3000
    },
    {
        "latitude": 27.913517942581727,
        "longitude": 108.69209618178225,
        "height" : 3000
    },
    {
        "latitude": 27.91342779055449,
        "longitude": 108.69211555329224,
        "height" : 3000
    },
    {
        "latitude": 27.91321153744783,
        "longitude": 108.69242531118748,
        "height" : 3000
    },
    {
        "latitude": 27.91312566950453,
        "longitude": 108.69271197228235,
        "height" : 3000
    },
    {
        "latitude": 27.912906063636527,
        "longitude": 108.69293865620207,
        "height" : 3000
    },
    {
        "latitude": 27.91254061265008,
        "longitude": 108.69355221152794,
        "height" : 3000
    },
    {
        "latitude": 27.912522544991727,
        "longitude": 108.69362243325165,
        "height" : 3000
    },
    {
        "latitude": 27.91242028577075,
        "longitude": 108.6937926790221,
        "height" : 3000
    },
    {
        "latitude": 27.912257863110106,
        "longitude": 108.69368725330429,
        "height" : 3000
    },
    {
        "latitude": 27.91209991079792,
        "longitude": 108.69377386630566,
        "height" : 3000
    },
    {
        "latitude": 27.912075137616885,
        "longitude": 108.69395901323763,
        "height" : 3000
    },
    {
        "latitude": 27.912613814606082,
        "longitude": 108.69401302994817,
        "height" : 3000
    },
    {
        "latitude": 27.91262890203213,
        "longitude": 108.6940923786333,
        "height" : 3000
    },
    {
        "latitude": 27.9124597738488,
        "longitude": 108.6941987356737,
        "height" : 3000
    },
    {
        "latitude": 27.91248566461695,
        "longitude": 108.69426839860384,
        "height" : 3000
    },
    {
        "latitude": 27.91223700148397,
        "longitude": 108.69442374321275,
        "height" : 3000
    },
    {
        "latitude": 27.912044031442196,
        "longitude": 108.69474970612116,
        "height" : 3000
    },
    {
        "latitude": 27.91189446103337,
        "longitude": 108.69485140654858,
        "height" : 3000
    },
    {
        "latitude": 27.91179406445758,
        "longitude": 108.69524498347741,
        "height" : 3000
    },
    {
        "latitude": 27.91138689021886,
        "longitude": 108.69525895331634,
        "height" : 3000
    },
    {
        "latitude": 27.911458229529668,
        "longitude": 108.69541522924786
    },
    {
        "latitude": 27.91137441049608,
        "longitude": 108.69561993395433,
        "height" : 3000
    },
    {
        "latitude": 27.911426750825942,
        "longitude": 108.69568978314898,
        "height" : 3000
    },
    {
        "latitude": 27.91138875286405,
        "longitude": 108.69599115914086,
        "height" : 3000
    },
    {
        "latitude": 27.911441093193915,
        "longitude": 108.69598687505693,
        "height" : 3000
    },
    {
        "latitude": 27.911441093193915,
        "longitude": 108.69622827387366,
        "height" : 3000
    },
    {
        "latitude": 27.911512432504722,
        "longitude": 108.69640932298621,
        "height" : 3000
    },
    {
        "latitude": 27.911591222396297,
        "longitude": 108.6965398944141,
        "height" : 3000
    },
    {
        "latitude": 27.91156402777651,
        "longitude": 108.69675540246267,
        "height" : 3000
    },
    {
        "latitude": 27.911575762441213,
        "longitude": 108.69694464721407,
        "height" : 3000
    },
    {
        "latitude": 27.91145580809092,
        "longitude": 108.69708490439695,
        "height" : 3000
    },
    {
        "latitude": 27.91128500352692,
        "longitude": 108.69691335477486,
        "height" : 3000
    },
    {
        "latitude": 27.911240672571378,
        "longitude": 108.6969722143629,
        "height" : 3000
    },
    {
        "latitude": 27.911157784860382,
        "longitude": 108.69687591560653,
        "height" : 3000
    },
    {
        "latitude": 27.91104416350374,
        "longitude": 108.6969800374727,
        "height" : 3000
    },
    {
        "latitude": 27.910768492015492,
        "longitude": 108.69699195840192,
        "height" : 3000
    },
    {
        "latitude": 27.910699201614392,
        "longitude": 108.69695563682069,
        "height" : 3000
    },
    {
        "latitude": 27.910645929961934,
        "longitude": 108.69688131727759,
        "height" : 3000
    },
    {
        "latitude": 27.910596942393415,
        "longitude": 108.69659093089233,
        "height" : 3000
    },
    {
        "latitude": 27.910566581276804,
        "longitude": 108.69616513020169,
        "height" : 3000
    },
    {
        "latitude": 27.910468047346207,
        "longitude": 108.69596862113406,
        "height" : 3000
    },
    {
        "latitude": 27.91025980361387,
        "longitude": 108.69577173953738,
        "height" : 3000
    },
    {
        "latitude": 27.910168161470477,
        "longitude": 108.69558901404416,
        "height" : 3000
    },
    {
        "latitude": 27.91015158392828,
        "longitude": 108.69533103768522,
        "height" : 3000
    },
    {
        "latitude": 27.910294635078937,
        "longitude": 108.69487450334894,
        "height" : 3000
    },
    {
        "latitude": 27.910319221995454,
        "longitude": 108.69441592010295,
        "height" : 3000
    },
    {
        "latitude": 27.910382729286447,
        "longitude": 108.69391299488315,
        "height" : 3000
    },
    // {
    //     "name": "金顶",
    //     "latitude": 27.9112084171,
    //     "longitude": 108.6940465145
    // }
];

function clickPoi() {
    viewer.clock.stopTime = viewer.clock.startTime;

    updateUserTour(userTourPostitions);
}

function updatePoiLocation(pois) {
    clearPoiLocation();

    var positions = [];
    for (var i = 0; i < pois.length; i++) {
        var htmlOverlay = document.createElement('div');
        htmlOverlay.id = 'htmlOverlay' + i;
        htmlOverlay.onclick = clickPoi;
        htmlOverlay.style = 'cursor: pointer;';
        htmlOverlay.value = i;
        htmlOverlay.className = 'imgContent';
        htmlOverlay.innerHTML = '<img style="position: relative; left: 50%; transform: translate(-23px, 4px); border:3px solid #fff; border-radius: 2px; height: 40px; width: 40px" src="'+ pois[i].thumbnail +'"/>';
        // htmlOverlay.innerHTML = '<div style="color: #fff; text-align: center;">' + pois[i].name + '</div>\
        // <img style="position: relative; left: 50%; transform: translate(-23px, 4px); border:3px solid #fff; border-radius: 2px; height: 40px; width: 40px" src="'+ pois[i].thumbnail +'"/>\
        // <div style="position: relative; width: 100%; height: 13px"><img style="transform: translate(13px, -3px);" src="tri-white.png" alt=""></div>';
        document.body.appendChild(htmlOverlay);

        positions.push(Cesium.Cartographic.fromDegrees(pois[i].longitude, pois[i].latitude));
    }

    promise = Cesium.sampleTerrainMostDetailed(terrainProvider, positions);
    Cesium.when(promise, function (updatedPositions) {
        if (updatedPositions.length > 0) {
            viewer.scene.preRender.addEventListener(function(l, t) {
                for (var i = 0; i < updatedPositions.length; i++) {
                    var htmlOverlay = document.getElementById('htmlOverlay' + i);
                    if (htmlOverlay) {
                        var scratch = new Cesium.Cartesian2();

                        pois[htmlOverlay.value].height = updatedPositions[htmlOverlay.value].height;

                        var position = Cesium.Cartesian3.fromDegrees(Cesium.Math.toDegrees(updatedPositions[htmlOverlay.value].longitude), Cesium.Math.toDegrees(updatedPositions[htmlOverlay.value].latitude), updatedPositions[htmlOverlay.value].height * terrainExaggeration);
                        var canvasPosition = viewer.scene.cartesianToCanvasCoordinates(position, scratch);
                        if (Cesium.defined(canvasPosition)) {
                            var x = canvasPosition.x;
                            var y = canvasPosition.y;
                            if (htmlOverlay.style.display === 'none') {
                                htmlOverlay.style.display = 'block';
                                x -= htmlOverlay.offsetWidth / 2;
                                y -= htmlOverlay.offsetHeight;
                                htmlOverlay.style.display = 'none';
                            }
                            else {
                                x -= htmlOverlay.offsetWidth / 2;
                                y -= htmlOverlay.offsetHeight;
                            }

                            if (y > 0 && x > 0) {
                                htmlOverlay.style.display = 'block';
                            }
                            else {
                                htmlOverlay.style.display = 'none';
                            }

                            htmlOverlay.style.top = canvasPosition.y - htmlOverlay.offsetHeight + 'px';
                            htmlOverlay.style.left = canvasPosition.x - htmlOverlay.offsetWidth / 2 + 'px';
                            htmlOverlay.style.zIndex = Math.ceil(canvasPosition.y);
                        }
                    }
                }
            });
            viewer.camera.flyTo({
                destination : Cesium.Cartesian3.fromDegrees(Cesium.Math.toDegrees(updatedPositions[0].longitude), Cesium.Math.toDegrees(updatedPositions[0].latitude), 12000)
            });
        }
    });
}

function clearPoiLocation() {
    var elements = document.getElementsByClassName('imgContent');
    for (i = 0; i < elements.length; i++) {
        elements[i].parentNode.removeChild(elements[i]);
    }
}

var pois = [
    {name: "金顶", longitude: 108.6940465145, latitude: 27.9112084171, thumbnail : "http://tiles.pano.vizen.cn/6A96E59B1701491990DB44C603664DFB/sphere/thumb.jpg"},
];

var userTour = null;

function updateUserTour(positions) {

    var positionArray = [];

    if (userTour) {
        viewer.entities.remove(userTour);
        userTour = null;
    }

    userTourPostitions = positions;

    var cartoPositions = [];
    for (var i = 0; i < userTourPostitions.length; i++) {
        cartoPositions.push(Cesium.Cartographic.fromDegrees(userTourPostitions[i].longitude, userTourPostitions[i].latitude));
    }
    promise = Cesium.sampleTerrainMostDetailed(terrainProvider, cartoPositions);
    Cesium.when(promise, function (updatedPositions) {
        if (updatedPositions.length > 0) {
            for (var i = 0; i < updatedPositions.length; i++) {
                userTourPostitions[i].height = updatedPositions[i].height + 1000;
            }
        }
        
        for (var i = 0; i < positions.length; i++) {
            positionArray.push(userTourPostitions[i].longitude);
            positionArray.push(userTourPostitions[i].latitude);
            positionArray.push(userTourPostitions[i].height);
        }
        userTour = viewer.entities.add({
            polyline : {
                positions : Cesium.Cartesian3.fromDegreesArrayHeights(positionArray),
                clampToGround : true,
                width : 3,
                material : new Cesium.PolylineOutlineMaterialProperty({
                    color : Cesium.Color.SPRINGGREEN,
                    outlineWidth : 1,
                    outlineColor : Cesium.Color.SPRINGGREEN
                }),
                depthFailMaterial : new Cesium.PolylineOutlineMaterialProperty({
                    color : Cesium.Color.SPRINGGREEN,
                    outlineWidth : 1,
                    outlineColor : Cesium.Color.SPRINGGREEN
                })
            }
        });
        viewer.flyTo(userTour);
    });


    // for (var i = 0; i < positions.length; i++) {
    //     positionArray.push(Cesium.Cartographic.fromDegrees(positions[i].longitude, positions[i].latitude));
    // }
    // promise = Cesium.sampleTerrainMostDetailed(terrainProvider, positionArray);
    // Cesium.when(promise, function (updatedPositions) {
    //     var positionsVolume = [];
    //     for (var i = 0; i < positions.length; i++) {
    //         positionsVolume.push(positions[i].longitude);
    //         positionsVolume.push(positions[i].latitude);
    //         positionsVolume.push(updatedPositions[i].height + 10);
    //     }

    //     userTour = viewer.entities.add({
    //         polylineVolume: {
    //             positions : Cesium.Cartesian3.fromDegreesArrayHeights(positionsVolume),
    //             shape: computeCircle(5.0),
    //             material: Cesium.Color.RED.withAlpha(0.8)
    //         }
    //     });
    //     viewer.flyTo(userTour);
    // });
}

function clearUserTour() {
    if (userTour) {
        viewer.entities.remove(userTour);
        userTour = null;
    }

    stopNavigation();
}

function startNavigation() {
    viewer.trackedEntity = userEntity;

    viewer.flyTo(userEntity);
}

function stopNavigation() {
    viewer.trackedEntity = undefined;
}

function computeCircle(radius) {
    var positions = [];
    for (var i = 0; i < 360; i++) {
        var radians = Cesium.Math.toRadians(i);
        positions.push(
            new Cesium.Cartesian2(
                radius * Math.cos(radians),
                radius * Math.sin(radians)
            )
        );
    }
    return positions;
}

function addRoadBackgroundLayer(road) {

    var promiseSSRoad = Cesium.GeoJsonDataSource.load(
        road.ssroad, { clampToGround: true } 
    );
    promiseSSRoad.then(function (dataSource) {
        dataSource.name = "ssroad";
        viewer.dataSources.add(dataSource);
        for (var i = 0; i < dataSource.entities.values.length; i++) {
            var entity = dataSource.entities.values[i];
            
            // entity.billboard.disableDepthTestDistance = Number.POSITIVE_INFINITY; //去掉地形遮挡
            entity.polyline.width = 3;
            entity.polyline.material = new Cesium.ImageMaterialProperty({
                color: Cesium.Color.GRAY.withAlpha(0.5)
            });
        }
    });

    var promiseSRoad = Cesium.GeoJsonDataSource.load(
        road.sroad, { clampToGround: true } 
    );
    promiseSRoad.then(function (dataSource) {
        dataSource.name = "sroad";
        viewer.dataSources.add(dataSource);
        for (var i = 0; i < dataSource.entities.values.length; i++) {
            var entity = dataSource.entities.values[i];
            
            // entity.billboard.disableDepthTestDistance = Number.POSITIVE_INFINITY; //去掉地形遮挡
            entity.polyline.width = 3;
            entity.polyline.material = new Cesium.ImageMaterialProperty({
                color: Cesium.Color.GRAY.withAlpha(0.5)
            });
        }
    });
}

function removeRoadBackgroundLayer(road) {
    let dataSourceSSRoad = viewer.dataSources.getByName("ssroad");
    viewer.dataSources.remove(dataSourceSSRoad[0]);

    let dataSourceSRoad = viewer.dataSources.getByName("sroad");
    viewer.dataSources.remove(dataSourceSRoad[0]);
}

function addAreaBackgroundLayer() {
    var parea = Cesium.GeoJsonDataSource.load(
        "./parea.geojson",
        {
            clampToGround: true   
        }
    );
    parea.then(function (dataSource) {
        viewer.dataSources.add(dataSource);
        for (var i = 0; i < dataSource.entities.values.length; i++) {
            var entity = dataSource.entities.values[i];
            
            // entity.billboard.disableDepthTestDistance = Number.POSITIVE_INFINITY; //去掉地形遮挡
            entity.polyline.width = 5;
            entity.polyline.material = new Cesium.ImageMaterialProperty({
                color: Cesium.Color.YELLOW.withAlpha(0.25)
            });
        }
    });

    var carea = Cesium.GeoJsonDataSource.load(
        "./carea.geojson",
        {
            clampToGround: true   
        }
    );
    carea.then(function (dataSource) {
        viewer.dataSources.add(dataSource);
        for (var i = 0; i < dataSource.entities.values.length; i++) {
            var entity = dataSource.entities.values[i];
            
            // entity.billboard.disableDepthTestDistance = Number.POSITIVE_INFINITY; //去掉地形遮挡
            entity.polyline.width = 5;
            entity.polyline.material = new Cesium.ImageMaterialProperty({
                color: Cesium.Color.GREEN.withAlpha(0.5)
            });
        }
    });
    
    return;

    viewer.dataSources.add(
        Cesium.GeoJsonDataSource.load(
            "./tarea.geojson",
            {
                stroke: Cesium.Color.HOTPINK,
                fill: new Cesium.Color(208/255, 227/255, 197/255, 0.5),
                strokeWidth: 3,
                clampToGround: true
            }
        )
    );
    viewer.dataSources.add(
        Cesium.GeoJsonDataSource.load(
            "./barea.geojson",
            {
                stroke: Cesium.Color.HOTPINK,
                fill: new Cesium.Color(177/255, 210/255, 167/255, 0.5),
                strokeWidth: 3,
                clampToGround: true
            }
        )
    );
}

function addSceneLayer() {
    var dataSource1 = Cesium.GeoJsonDataSource.load(
        "./scene.geojson",
        {
            clampToGround: true
        }
    );
    dataSource1.then(function (dataSource) {
        viewer.dataSources.add(dataSource);
        for (var i = 0; i < dataSource.entities.values.length; i++) {
            var entity = dataSource.entities.values[i];
            var name = entity.name;
        }
    });

    var dataSource2 = Cesium.GeoJsonDataSource.load(
        "./animal.geojson",
        {
            clampToGround: true
        }
    );
    viewer.dataSources.add(dataSource2);

    var dataSource3 = Cesium.GeoJsonDataSource.load(
        "./plant.geojson",
        {
            clampToGround: true
        }
    );
    viewer.dataSources.add(dataSource3);
}

var handler = new Cesium.ScreenSpaceEventHandler(viewer.scene.canvas);

handler.setInputAction(function(click) { // 点击事件
    viewer.clock.stopTime = viewer.clock.startTime; //viewer.clock.stopTime 赋值为 viewer.clock.startTime 即可停止旋转

    var pickElement = viewer.scene.pick(click.position);
    if (pickElement && pickElement.id){

    }
}, Cesium.ScreenSpaceEventType.LEFT_DOWN);

//position_A绕position_B逆时针旋转angle度（角度）得到新点
function rotatedPointByAngle(position_A, position_B, angle) {
    //以B点为原点建立局部坐标系（东方向为x轴,北方向为y轴,垂直于地面为z轴），得到一个局部坐标到世界坐标转换的变换矩阵
    var localToWorld_Matrix = Cesium.Transforms.eastNorthUpToFixedFrame(position_B);
    //求世界坐标到局部坐标的变换矩阵
    var worldToLocal_Matrix = Cesium.Matrix4.inverse(localToWorld_Matrix, new Cesium.Matrix4());
    //B点在局部坐标的位置，其实就是局部坐标原点
    var localPosition_B = Cesium.Matrix4.multiplyByPoint(worldToLocal_Matrix, position_B, new Cesium.Cartesian3());
    //A点在以B点为原点的局部的坐标位置
    var localPosition_A = Cesium.Matrix4.multiplyByPoint(worldToLocal_Matrix, position_A, new Cesium.Cartesian3());
    //根据数学公式A点逆时针旋转angle度后在局部坐标系中的x,y,z位置
    var new_x = localPosition_A.x * Math.cos(Cesium.Math.toRadians(angle)) + localPosition_A.y *   Math.sin(Cesium.Math.toRadians(angle));
    var new_y = localPosition_A.y * Math.cos(Cesium.Math.toRadians(angle)) - localPosition_A.x * Math.sin(Cesium.Math.toRadians(angle));
    var new_z = localPosition_A.z;
    //最后应用局部坐标到世界坐标的转换矩阵求得旋转后的A点世界坐标
    return Cesium.Matrix4.multiplyByPoint(localToWorld_Matrix, new Cesium.Cartesian3(new_x, new_y, new_z), new Cesium.Cartesian3());
}

function rotateLeftRight(angle) {
    var canvas = viewer.scene.canvas;
    let center_position = viewer.scene.pickPosition(new Cesium.Cartesian2(canvas.width / 2.0, canvas.height / 2.0));

    //var cartographic = Cesium.Cartographic.fromCartesian(center_position);
    
    //viewer.scene.camera.rotate(center_position, Cesium.Math.toRadians(angle));

    let new_position = rotatedPointByAngle(viewer.camera.position, center_position, angle);

    var heading = viewer.camera.heading + Cesium.Math.toRadians(angle);
    var pitch = viewer.camera.pitch;
    var roll = viewer.camera.roll;

    viewer.scene.camera.flyTo({
        destination : new_position,
        orientation: {
            heading : heading,
            pitch : pitch,
            roll : roll
        },
        duration: 0.5
    });
}

function rotatedPointByAngleYZ(position_A, position_B, angle) {
    //以B点为原点建立局部坐标系（东方向为x轴,北方向为y轴,垂直于地面为z轴），得到一个局部坐标到世界坐标转换的变换矩阵
    var localToWorld_Matrix = Cesium.Transforms.eastNorthUpToFixedFrame(position_B);
    //求世界坐标到局部坐标的变换矩阵
    var worldToLocal_Matrix = Cesium.Matrix4.inverse(localToWorld_Matrix, new Cesium.Matrix4());
    //B点在局部坐标的位置，其实就是局部坐标原点
    var localPosition_B = Cesium.Matrix4.multiplyByPoint(worldToLocal_Matrix, position_B, new Cesium.Cartesian3());
    //A点在以B点为原点的局部的坐标位置
    var localPosition_A = Cesium.Matrix4.multiplyByPoint(worldToLocal_Matrix, position_A, new Cesium.Cartesian3());
    //根据数学公式A点逆时针旋转angle度后在局部坐标系中的x,y,z位置
    var new_x = localPosition_A.x;
    var new_y = localPosition_A.y * Math.cos(Cesium.Math.toRadians(angle)) - localPosition_A.z * Math.sin(Cesium.Math.toRadians(angle));
    var new_z = localPosition_A.z * Math.cos(Cesium.Math.toRadians(angle)) + localPosition_A.y * Math.sin(Cesium.Math.toRadians(angle));
    //最后应用局部坐标到世界坐标的转换矩阵求得旋转后的A点世界坐标
    return Cesium.Matrix4.multiplyByPoint(localToWorld_Matrix, new Cesium.Cartesian3(new_x, new_y, new_z), new Cesium.Cartesian3());
}

function rotateUpDown(angle) {
    var canvas = viewer.scene.canvas;
    let center_position = viewer.scene.pickPosition(new Cesium.Cartesian2(canvas.width / 2.0, canvas.height / 2.0));

    let new_position = rotatedPointByAngleYZ(viewer.camera.position, center_position, angle);

    var heading = viewer.camera.heading;
    var pitch = viewer.camera.pitch + Cesium.Math.toRadians(angle);
    var roll = viewer.camera.roll;

    viewer.scene.camera.flyTo({
        destination : new_position,
        orientation: {
            heading : heading,
            pitch : pitch,
            roll : roll
        },
        duration: 0.5
    });
}

function changeMapMode() {
    var angle = Cesium.Math.toDegrees(viewer.camera.pitch);
    if (Math.round(Math.abs(angle)) === 90) {
        angle = 60;
    }
    else {
        angle = - (angle + 90);
    }

    rotateUpDown(angle);
}

document.addEventListener('keydown', function (e) {
    setKey(e);
}, false);

function optMap(opt) {
    if (opt.action === "rotateByLeft") {
        rotateLeftRight(opt.data);
    }
    else if (opt.action === "rotateByRight") {
        rotateLeftRight(-opt.data);
    }
    else if (opt.action === "rotateByUp") {
        rotateUpDown(opt.data);
    }
    else if (opt.action === "rotateByDown") {
        rotateUpDown(-opt.data);
    }
    else if (opt.action === "zoomIn") {
        viewer.camera.zoomIn(viewer.camera.positionCartographic.height / 2);
    }
    else if (opt.action === "zoomOut") {
        viewer.camera.zoomOut(viewer.camera.positionCartographic.height * 2);
    }
    else if (opt.action === "pointToNorth") {
        rotateLeftRight(Cesium.Math.toDegrees(-viewer.camera.heading));
    }
}

function setKey(event) {
    console.log(event.keyCode);
    if (event.keyCode === 77) {
        changeMapMode();
    }
    else if (event.keyCode === 78) {
        rotateLeftRight(Cesium.Math.toDegrees(-viewer.camera.heading));
    }
    else if (event.keyCode === 187) {
        viewer.camera.zoomIn(viewer.camera.positionCartographic.height / 2);
    }
    else if (event.keyCode === 189) {
        viewer.camera.zoomOut(viewer.camera.positionCartographic.height * 2);
    }
    else if (event.keyCode === 65) {
        addRoadBackgroundLayer({ "sroad" : "./sroad.geojson", "ssroad" : "./ssroad.geojson"});
    } 
    else if (event.keyCode === 68) {
        removeRoadBackgroundLayer();
    }
    else if (event.keyCode === 70) {
        // 飞行预览启动
        flyThroughStart(200, 45);
    }
    else if (event.keyCode === 32) {
        // 飞行预览暂停
        flyThroughPause();
    }
    else if (event.keyCode === 83) {
        // 飞行预览停止
        flyThroughStop();
    }
    else if (event.keyCode === 84) {
        updateUserTour(userTourPostitions);
        //updatePoiLocation(pois);
        //flyTo({"height":12000,"latitude":27.8601391146,"longitude":108.7107853492});
    }

    if (event.ctrlKey){
        if (event.keyCode === 39) {  // right arrow
            rotateLeftRight(15);
        } else if (event.keyCode === 37) {  // left arrow
            rotateLeftRight(-15);
        } else if (event.keyCode === 40) {  // up arrow
            rotateUpDown(15);
        } else if (event.keyCode === 38) {  // down arrow
            rotateUpDown(-15);
        }
    }
    else {
        var horizontalDegrees = 5.0;
        var verticalDegrees = 5.0;
        var scratchRectangle = new Cesium.Rectangle();
        Cesium.Camera.DEFAULT_VIEW_FACTOR = 0;

        var viewRect = viewer.camera.computeViewRectangle(viewer.scene.globe.ellipsoid, scratchRectangle);
        if (Cesium.defined(viewRect)) {
            horizontalDegrees *= Cesium.Math.toDegrees(viewRect.east - viewRect.west) / 360.0;
            verticalDegrees *= Cesium.Math.toDegrees(viewRect.north - viewRect.south) / 180.0;
        }

        if (event.keyCode === 39) {  // right arrow
            viewer.camera.rotateRight(Cesium.Math.toRadians(horizontalDegrees));
        } else if (event.keyCode === 37) {  // left arrow
            viewer.camera.rotateLeft(Cesium.Math.toRadians(horizontalDegrees));
        } else if (event.keyCode === 40) {  // up arrow
            viewer.camera.rotateUp(Cesium.Math.toRadians(verticalDegrees));
        } else if (event.keyCode === 38) {  // down arrow
            viewer.camera.rotateDown(Cesium.Math.toRadians(verticalDegrees));
        }
    }
}

handler.setInputAction(function(click) { // 点击事件
    viewer.clock.stopTime = viewer.clock.startTime; //viewer.clock.stopTime 赋值为 viewer.clock.startTime 即可停止旋转

    
}, Cesium.ScreenSpaceEventType.RIGHT_DOWN);

handler.setInputAction(function(wheelment) { // 滚轮事件

}, Cesium.ScreenSpaceEventType.WHEEL);

function getUserTourHeights(userTour) { // 异步调用
    var userTourJSON = userTour;
    var positions = [];
    for (var i = 0; i < userTourJSON.length; i++) {
        positions.push(Cesium.Cartographic.fromDegrees(userTourJSON[i].longitude, userTourJSON[i].latitude));
    }

    promise = Cesium.sampleTerrainMostDetailed(terrainProvider, positions);
    Cesium.when(promise, function (updatedPositions) {
        if (updatedPositions.length > 0) {
            for (var i = 0; i <userTourJSON.length; i++) {
                userTourJSON[i].height = updatedPositions[i].height;
            }
            if (window.Android) {
                window.Android.putUserTourHeights(JSON.stringify(userTourJSON));
            }
        }
    });
}

var options = {};
function resetView() {
    recenter();
}
// 用于在使用重置导航重置地图视图时设置默认视图控制。接受的值是Cesium.Cartographic 和 Cesium.Rectangle。
options.defaultResetView = resetView;
// 用于启用或禁用罗盘。true是启用罗盘，false是禁用罗盘。默认值为true。如果将选项设置为false，则罗盘将不会添加到地图中。
options.enableCompass = true;
// 用于启用或禁用缩放控件。true是启用，false是禁用。默认值为true。如果将选项设置为false，则缩放控件将不会添加到地图中。
options.enableZoomControls = true;
// 用于启用或禁用距离图例。true是启用，false是禁用。默认值为true。如果将选项设置为false，距离图例将不会添加到地图中。
options.enableDistanceLegend = true;
// 用于启用或禁用指南针外环。true是启用，false是禁用。默认值为true。如果将选项设置为false，则该环将可见但无效。
options.enableCompassOuterRing = true;

CesiumNavigation.umd(viewer, options);

viewer.scene.postRender.addEventListener(function () {
    var cameraParam = {
        position : {
            longitude : Cesium.Math.toDegrees(viewer.camera.positionCartographic.longitude),
            latitude : Cesium.Math.toDegrees(viewer.camera.positionCartographic.latitude),
            height : viewer.camera.positionCartographic.height
        },
        heading : Cesium.Math.toDegrees(viewer.camera.heading),
        pitch : viewer.camera.pitch,
        roll : viewer.camera.roll
    };
    if (window.Android) {
        window.Android.putCameraParam(JSON.stringify(cameraParam));
    }
});

function flyTo(position) {
    viewer.camera.flyTo({
        destination : Cesium.Cartesian3.fromDegrees(position.longitude, position.latitude, position.height)
    });
}

// fly through

var planeModelLoaded = false; // 飞行模型加载标记
var planePrimitive = null; // 飞行模型
var preUpdateListener = null;

function flyThroughPause() {
    viewer.clock.stopTime = viewer.clock.startTime;
    if (planeModelLoaded) {
        viewer.scene.primitives.remove(planePrimitive);
        planeModelLoaded = false;
    }
    if (preUpdateListener) {
        viewer.scene.preUpdate.removeEventListener(preUpdateListener);
        preUpdateListener = null;
    }
}

function flyThroughStop() {
    viewer.clock.stopTime = viewer.clock.startTime;
    if (planeModelLoaded) {
        viewer.scene.primitives.remove(planePrimitive);
        planeModelLoaded = false;
    }
    if (preUpdateListener) {
        viewer.scene.preUpdate.removeEventListener(preUpdateListener);
        preUpdateListener = null;
    }
    if (userTour) {
        viewer.flyTo(userTour);
    }
}

function flyThroughStart(height, angle) {
    if (!userTour || planeModelLoaded) {
        return;
    }

    var simplifiedPositions = simplify(userTourPostitions, 0.001, false);

    var cartoPositions = [];
    for (var i = 0; i < simplifiedPositions.length; i++) {
        cartoPositions.push(Cesium.Cartographic.fromDegrees(simplifiedPositions[i].longitude, simplifiedPositions[i].latitude));
    }
    promise = Cesium.sampleTerrainMostDetailed(terrainProvider, cartoPositions);
    Cesium.when(promise, function (updatedPositions) {
        if (updatedPositions.length > 0) {
            for (var i = 0; i < updatedPositions.length; i++) {
                simplifiedPositions[i].height = updatedPositions[i].height + height;
            }
        }

        var deltaRadians = Cesium.Math.toRadians(1.0);
            
        var scene = viewer.scene;
        var camera = viewer.camera;
        var controller = scene.screenSpaceCameraController;
        var r = 0;
        var center = new Cesium.Cartesian3();

        function getHeadingPitch(pointA, pointB){
            const transform = Cesium.Transforms.eastNorthUpToFixedFrame(pointA);
            const positionvector = Cesium.Cartesian3.subtract(pointB, pointA, new Cesium.Cartesian3());
            const vector = Cesium.Matrix4.multiplyByPointAsVector(Cesium.Matrix4.inverse(transform, new Cesium.Matrix4()), positionvector, new Cesium.Cartesian3());
            const direction = Cesium.Cartesian3.normalize(vector, new Cesium.Cartesian3());
            //heading
            const heading = Cesium.Math.TWO_PI-Cesium.Math.zeroToTwoPi(Math.atan2(direction.y, direction.x) - Cesium.Math.PI_OVER_TWO);
            //pitch
            const pitch = Cesium.Math.PI_OVER_TWO - Cesium.Math.acosClamped(direction.z);
            //distance
            const distance = Cesium.Cartesian3.distance(pointA, pointB);
            return {
                heading : heading,
                pitch : pitch,
                distance : distance
            };
        }
        var roamingPostions = [];
        for (var i = 0; i < simplifiedPositions.length; i++) {
            roamingPostions.push(simplifiedPositions[i].longitude);
            roamingPostions.push(simplifiedPositions[i].latitude);
            roamingPostions.push(simplifiedPositions[i].height);
        }
        var hPitches = [];
        var distanceSum = 0.0;
        for (var i = 3; i < roamingPostions.length; i += 3) {
            var originPosition = new Cesium.Cartographic(roamingPostions[i-3], roamingPostions[i-2]);
            var originHeight = 0;
            var targetPosition = new Cesium.Cartographic(roamingPostions[i+0], roamingPostions[i+1]);
            var targetHeight = 0;
            var origin = new Cesium.Cartesian3.fromDegrees(roamingPostions[i-3], roamingPostions[i-2], originHeight+roamingPostions[i-1]);
            var target = new Cesium.Cartesian3.fromDegrees(roamingPostions[i+0], roamingPostions[i+1], targetHeight+roamingPostions[i+2]);
            var hPitch = getHeadingPitch(origin, target);

            distanceSum += hPitch.distance;
            hPitch.distance = distanceSum;

            if (hPitches.length === 0) {
                hPitches.push(hPitch);
            }
            else {
                var heading = hPitch.heading;
                if (heading > hPitches[hPitches.length - 1].heading) {
                    for (var h = hPitches[hPitches.length - 1].heading; h < heading; h += deltaRadians) {
                        hPitches.push({
                            heading : h,
                            pitch : hPitch.pitch,
                            distance : hPitch.distance
                        });
                    }
                }
                else {
                    for (var h = hPitches[hPitches.length - 1].heading; h > heading; h -= deltaRadians) {
                        hPitches.push({
                            heading : h,
                            pitch : hPitch.pitch,
                            distance : hPitch.distance
                        });
                    }
                }
                hPitches.push(hPitch);
            }
        }

        var origin = new Cesium.Cartesian3.fromDegrees(simplifiedPositions[0].longitude, simplifiedPositions[0].latitude, simplifiedPositions[0].height);
        var target = new Cesium.Cartesian3.fromDegrees(simplifiedPositions[1].longitude, simplifiedPositions[1].latitude, simplifiedPositions[1].height);
        var hPitch = getHeadingPitch(origin, target);

        var hpRoll = new Cesium.HeadingPitchRoll();
        hpRoll.heading = hPitch.heading;
        hpRoll.pitch = hPitch.pitch;

        var hpRange = new Cesium.HeadingPitchRange();
        var speed = 10;
        

        var position = Cesium.Cartesian3.fromDegrees(simplifiedPositions[0].longitude, simplifiedPositions[0].latitude, simplifiedPositions[0].height);
        var speedVector = new Cesium.Cartesian3();
        var fixedFrameTransform = Cesium.Transforms.localFrameToFixedFrameGenerator('north', 'west');

        planePrimitive = scene.primitives.add(Cesium.Model.fromGltf({
            url : './Cesium_Air.glb',
            modelMatrix : Cesium.Transforms.headingPitchRollToFixedFrame(position, hpRoll, Cesium.Ellipsoid.WGS84, fixedFrameTransform),
            show : false,
            minimumPixelSize : 128
        }));

        planePrimitive.readyPromise.then(function(model) {
            // Play and loop all animations at half-speed
            model.activeAnimations.addAll({
                removeOnStop : true,
                multiplier : 0.5,
                loop : Cesium.ModelAnimationLoop.REPEAT
            });

            // Zoom to model
            r = 2.0 * Math.max(model.boundingSphere.radius, camera.frustum.near);
            controller.minimumZoomDistance = r * 0.5;
            Cesium.Matrix4.multiplyByPoint(model.modelMatrix, model.boundingSphere.center, center);
            var heading = Cesium.Math.toRadians(230.0);
            var pitch = Cesium.Math.toRadians(-20.0);
            hpRange.heading = heading;
            hpRange.pitch = pitch;
            hpRange.range = r * 25.0;
            camera.lookAt(center, hpRange);

            planeModelLoaded = true;
        });

        var distance = 0.0;
        var currentIndex = 0;
        var currentPosition = new Cesium.Cartesian3.fromDegrees(simplifiedPositions[0].longitude, simplifiedPositions[0].latitude, simplifiedPositions[0].height);
        
        preUpdateListener = function (scene, time) {
            if (!planeModelLoaded) {
                return;
            }
            
            speedVector = Cesium.Cartesian3.multiplyByScalar(Cesium.Cartesian3.UNIT_X, speed / 10, speedVector);
            position = Cesium.Matrix4.multiplyByPoint(planePrimitive.modelMatrix, speedVector, position);

            distance += Cesium.Cartesian3.distance(currentPosition, position);
            currentPosition = position.clone();

            Cesium.Transforms.headingPitchRollToFixedFrame(position, hpRoll, Cesium.Ellipsoid.WGS84, fixedFrameTransform, planePrimitive.modelMatrix);

            var i = 0;
            for (; i < hPitches.length; i++) {
                if (distance < hPitches[i].distance) {
                    break;
                }
            }
            if (i > currentIndex && i < hPitches.length) {
                hpRoll.heading = hPitches[i].heading;
                hpRoll.pitch = hPitches[i].pitch;
                currentIndex = i;
            }
            
            // Zoom to model
            Cesium.Matrix4.multiplyByPoint(planePrimitive.modelMatrix, planePrimitive.boundingSphere.center, center);
            hpRange.heading = hpRoll.heading;
            hpRange.pitch = Cesium.Math.toRadians(-angle);//hpRoll.pitch;
            camera.lookAt(center, hpRange);

            if (i >= hPitches.length) {
                flyThroughStop();
            }
        }

        viewer.scene.preUpdate.addEventListener(preUpdateListener);
    });
}