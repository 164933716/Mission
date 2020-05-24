var terrainExaggeration = 1.0;
var terrainProvider = new Cesium.CesiumTerrainProvider({url:"http://47.110.155.250:8003/Map/Terrain"});
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

// var mapboxLayer = new Cesium.MapboxStyleImageryProvider({
//     url : 'https://api.mapbox.com/styles/v1/',
//     username:'wingwuyf',
//     styleId: 'cjajfiz60artm2rnxr8wiecli',
//     accessToken: 'pk.eyJ1Ijoid2luZ3d1eWYiLCJhIjoiY2s3b282bm90MDJ1dDNtbGlibGowMTRoeSJ9.hDrbU176NROA7KYhmBkmEg',
//     scaleFactor: true
// });

//var mapboxLayer = new Cesium.UrlTemplateImageryProvider({url:"https://map-cache.oss-cn-hangzhou.aliyuncs.com/v4/mapbox.mapbox-streets-v8,mapbox.mapbox-terrain-v2/{z}/{x}/{y}.png?sku=1011hS4giuW7i&access_token=pk.eyJ1Ijoid2ViZXJ0YW8iLCJhIjoiY2pibTdmaWc2MTZqaDJybzFzcm93bGE2eiJ9.cwSE9DYCYP0dIeY4Hhp6Kg"});
//var mapboxLayer = new Cesium.UrlTemplateImageryProvider({url:"https://api.mapbox.com/styles/v1/wingwuyf/cjajfiz60artm2rnxr8wiecli/tiles/512/{z}/{x}/{y}@2x?access_token=pk.eyJ1Ijoid2luZ3d1eWYiLCJhIjoiY2s3b282bm90MDJ1dDNtbGlibGowMTRoeSJ9.hDrbU176NROA7KYhmBkmEg"});

var googleLayer = new Cesium.UrlTemplateImageryProvider({url:"http://mt2.google.cn/vt/lyrs=s@167000000&hl=zh-CN&gl=cn&x={x}&y={y}&z={z}&s=Galil"});

viewer.imageryLayers.removeAll();
//viewer.imageryLayers.addImageryProvider(mapboxLayer);

viewer.imageryLayers.addImageryProvider(new Cesium.UrlTemplateImageryProvider({url:"http://47.110.155.250:8003/Map/Image/{z}/{x}/{y}.png"}));

viewer._cesiumWidget._creditContainer.style.display = "none";

viewer.scene.globe.depthTestAgainstTerrain = true;

//viewer.scene.screenSpaceCameraController.minimumZoomDistance = 10; //相机的高度的最小值
//viewer.scene.screenSpaceCameraController.maximumZoomDistance = 80000; //22000000; //相机高度的最大值
//viewer.scene.screenSpaceCameraController._minimumZoomRate = 1000; //30000; //相机缩小时的速率
//viewer.scene.screenSpaceCameraController._maximumZoomRate = 1000000; //5906376272000; //相机放大时的速率
//viewer.scene.screenSpaceCameraController.maximumMovementRatio = 0.01;

viewer.scene.skyBox.show = false;
viewer.scene.backgroundColor = new Cesium.Color(48/255, 57/255, 28/255);
viewer.scene.sun.show = false; 
viewer.scene.moon.show = false;
//viewer.scene.globe.show = false;
viewer.scene.skyAtmosphere.show = false;

var west = 108.5506111775;
var south = 27.6176314387;
var east = 108.8606161775;
var north = 28.0951804387;
var center = {x : (west + east) / 2, y : (south + north) / 2};

//var position = Cesium.Cartographic.toCartesian(new Cesium.Cartographic.fromDegrees(108.690813, 27.918094, 0));
//var distance = 7500;
//var clippingPlanes = new Cesium.ClippingPlaneCollection({
//    modelMatrix : Cesium.Transforms.eastNorthUpToFixedFrame(position),
//    planes : [
//        new Cesium.ClippingPlane(new Cesium.Cartesian3( 1.0,  0.0, 0.0), distance),
//        new Cesium.ClippingPlane(new Cesium.Cartesian3(-1.0,  0.0, 0.0), distance),
//        new Cesium.ClippingPlane(new Cesium.Cartesian3( 0.0,  1.0, 0.0), distance),
//        new Cesium.ClippingPlane(new Cesium.Cartesian3( 0.0, -1.0, 0.0), distance)
//    ],
//    unionClippingRegions: true
//});
//viewer.scene.globe.clippingPlanes = clippingPlanes;
//viewer.scene.globe.backFaceCulling = false;
//viewer.scene.globe.showSkirts = false;

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


function recenter(location) {

    var position = Cesium.Cartesian3.fromDegrees(location.longitude, location.latitude, location.height);

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

    promise = Cesium.sampleTerrain(terrainProvider, 14, [Cesium.Cartographic.fromDegrees(location.longitude, location.latitude)]);
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

updateUserLocation({longitude: 108.690813, latitude: 27.918094, height: 2416.7601452398626});

recenter(userLocation);

var userTourPostitions = [
    // {
    //     "name": "我的位置",
    //     "latitude": 27.918094,
    //     "longitude": 108.690813
    // },
    {
        "latitude": 27.917657686039245,
        "longitude": 108.69037617890788
    },
    {
        "latitude": 27.91692192666803,
        "longitude": 108.69131740982795
    },
    {
        "latitude": 27.9165244381843,
        "longitude": 108.69121086652304
    },
    {
        "latitude": 27.91623926720558,
        "longitude": 108.69150739963742
    },
    {
        "latitude": 27.91549104263241,
        "longitude": 108.69217944202228
    },
    {
        "latitude": 27.91520140130523,
        "longitude": 108.69191774037297
    },
    {
        "latitude": 27.914740210355973,
        "longitude": 108.69218819645468
    },
    {
        "latitude": 27.914097411500606,
        "longitude": 108.69209152516927
    },
    {
        "latitude": 27.913517942581727,
        "longitude": 108.69209618178225
    },
    {
        "latitude": 27.91342779055449,
        "longitude": 108.69211555329224
    },
    {
        "latitude": 27.91321153744783,
        "longitude": 108.69242531118748
    },
    {
        "latitude": 27.91312566950453,
        "longitude": 108.69271197228235
    },
    {
        "latitude": 27.912906063636527,
        "longitude": 108.69293865620207
    },
    {
        "latitude": 27.91254061265008,
        "longitude": 108.69355221152794
    },
    {
        "latitude": 27.912522544991727,
        "longitude": 108.69362243325165
    },
    {
        "latitude": 27.91242028577075,
        "longitude": 108.6937926790221
    },
    {
        "latitude": 27.912257863110106,
        "longitude": 108.69368725330429
    },
    {
        "latitude": 27.91209991079792,
        "longitude": 108.69377386630566
    },
    {
        "latitude": 27.912075137616885,
        "longitude": 108.69395901323763
    },
    {
        "latitude": 27.912613814606082,
        "longitude": 108.69401302994817
    },
    {
        "latitude": 27.91262890203213,
        "longitude": 108.6940923786333
    },
    {
        "latitude": 27.9124597738488,
        "longitude": 108.6941987356737
    },
    {
        "latitude": 27.91248566461695,
        "longitude": 108.69426839860384
    },
    {
        "latitude": 27.91223700148397,
        "longitude": 108.69442374321275
    },
    {
        "latitude": 27.912044031442196,
        "longitude": 108.69474970612116
    },
    {
        "latitude": 27.91189446103337,
        "longitude": 108.69485140654858
    },
    {
        "latitude": 27.91179406445758,
        "longitude": 108.69524498347741
    },
    {
        "latitude": 27.91138689021886,
        "longitude": 108.69525895331634
    },
    {
        "latitude": 27.911458229529668,
        "longitude": 108.69541522924786
    },
    {
        "latitude": 27.91137441049608,
        "longitude": 108.69561993395433
    },
    {
        "latitude": 27.911426750825942,
        "longitude": 108.69568978314898
    },
    {
        "latitude": 27.91138875286405,
        "longitude": 108.69599115914086
    },
    {
        "latitude": 27.911441093193915,
        "longitude": 108.69598687505693
    },
    {
        "latitude": 27.911441093193915,
        "longitude": 108.69622827387366
    },
    {
        "latitude": 27.911512432504722,
        "longitude": 108.69640932298621
    },
    {
        "latitude": 27.911591222396297,
        "longitude": 108.6965398944141
    },
    {
        "latitude": 27.91156402777651,
        "longitude": 108.69675540246267
    },
    {
        "latitude": 27.911575762441213,
        "longitude": 108.69694464721407
    },
    {
        "latitude": 27.91145580809092,
        "longitude": 108.69708490439695
    },
    {
        "latitude": 27.91128500352692,
        "longitude": 108.69691335477486
    },
    {
        "latitude": 27.911240672571378,
        "longitude": 108.6969722143629
    },
    {
        "latitude": 27.911157784860382,
        "longitude": 108.69687591560653
    },
    {
        "latitude": 27.91104416350374,
        "longitude": 108.6969800374727
    },
    {
        "latitude": 27.910768492015492,
        "longitude": 108.69699195840192
    },
    {
        "latitude": 27.910699201614392,
        "longitude": 108.69695563682069
    },
    {
        "latitude": 27.910645929961934,
        "longitude": 108.69688131727759
    },
    {
        "latitude": 27.910596942393415,
        "longitude": 108.69659093089233
    },
    {
        "latitude": 27.910566581276804,
        "longitude": 108.69616513020169
    },
    {
        "latitude": 27.910468047346207,
        "longitude": 108.69596862113406
    },
    {
        "latitude": 27.91025980361387,
        "longitude": 108.69577173953738
    },
    {
        "latitude": 27.910168161470477,
        "longitude": 108.69558901404416
    },
    {
        "latitude": 27.91015158392828,
        "longitude": 108.69533103768522
    },
    {
        "latitude": 27.910294635078937,
        "longitude": 108.69487450334894
    },
    {
        "latitude": 27.910319221995454,
        "longitude": 108.69441592010295
    },
    {
        "latitude": 27.910382729286447,
        "longitude": 108.69391299488315
    },
    // {
    //     "name": "金顶",
    //     "latitude": 27.9112084171,
    //     "longitude": 108.6940465145
    // }
];

function clickPoi() {
    viewer.clock.stopTime = viewer.clock.startTime;

    //window.Android.fetchRoute(JSON.stringify(userLocation), JSON.stringify(pois[this.value]));
    getUserTourHeights(JSON.stringify(userTourPostitions));
    updateUserTour(userTourPostitions);
}

function updatePoiLocation(pois) {
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

    promise = Cesium.sampleTerrain(terrainProvider, 14, positions);
    Cesium.when(promise, function (updatedPositions) {
        if (updatedPositions.length > 0) {
            viewer.scene.preRender.addEventListener(function(l, t) {
                for (var i = 0; i < updatedPositions.length; i++) {
                    var htmlOverlay = document.getElementById('htmlOverlay' + i);
                    var scratch = new Cesium.Cartesian2();

                    pois[htmlOverlay.value].height = updatedPositions[htmlOverlay.value].height;

                    var position = Cesium.Cartesian3.fromDegrees(Cesium.Math.toDegrees(updatedPositions[htmlOverlay.value].longitude), Cesium.Math.toDegrees(updatedPositions[htmlOverlay.value].latitude), updatedPositions[htmlOverlay.value].height * terrainExaggeration);
                    var canvasPosition = viewer.scene.cartesianToCanvasCoordinates(position, scratch);
                    if (Cesium.defined(canvasPosition)) {
                        htmlOverlay.style.top = canvasPosition.y - htmlOverlay.offsetHeight + 'px';
                        htmlOverlay.style.left = canvasPosition.x - htmlOverlay.offsetWidth / 2 + 'px';

                        htmlOverlay.style.zIndex = Math.ceil(canvasPosition.y);
                    }
                }
            });
        }
    });
}

var pois = [
    {name: "金顶", longitude: 108.6940465145, latitude: 27.9112084171, thumbnail : "http://tiles.pano.vizen.cn/6A96E59B1701491990DB44C603664DFB/sphere/thumb.jpg"},
];

updatePoiLocation(pois);

var userTour = null;

function updateUserTour(positions) {

    var positionArray = [];
    for (var i = 0; i < positions.length; i++) {
        // positionArray.push(positions[i].longitude);
        // positionArray.push(positions[i].latitude);
        // positionArray.push(0.0);
        positionArray.push(Cesium.Cartographic.fromDegrees(positions[i].longitude, positions[i].latitude));
    }
    if (userTour) {
        viewer.entities.remove(userTour);
        userTour = null;
    }
    promise = Cesium.sampleTerrain(terrainProvider, 14, positionArray);
    Cesium.when(promise, function (updatedPositions) {
        var positionsVolume = [];
        for (var i = 0; i < positions.length; i++) {
            positionsVolume.push(positions[i].longitude);
            positionsVolume.push(positions[i].latitude);
            positionsVolume.push(updatedPositions[i].height + 5);
        }

        userTour = viewer.entities.add({
            polylineVolume: {
                positions : Cesium.Cartesian3.fromDegreesArrayHeights(positionsVolume),
                shape: computeCircle(3.0),
                material: Cesium.Color.RED.withAlpha(0.8)
            }
            // polyline : {
            //     positions : Cesium.Cartesian3.fromDegreesArrayHeights(positionArray),
            //     clampToGround : true,
            //     width : 3,
            //     material : new Cesium.PolylineOutlineMaterialProperty({
            //         color : Cesium.Color.SPRINGGREEN,
            //         outlineWidth : 1,
            //         outlineColor : Cesium.Color.SPRINGGREEN
            //     }),
            //     depthFailMaterial : new Cesium.PolylineOutlineMaterialProperty({
            //         color : Cesium.Color.SPRINGGREEN,
            //         outlineWidth : 1,
            //         outlineColor : Cesium.Color.SPRINGGREEN
            //     })
            // }
        });

        viewer.flyTo(userTour);

    });
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

    for (var i = 0; i < road.features.length; i++) {
        var positions = [];
        for (var j = 0; j < road.features[i].geometry.coordinates.length; j++) {
            positions.push(road.features[i].geometry.coordinates[j][0]);
            positions.push(road.features[i].geometry.coordinates[j][1]);
            positions.push(road.features[i].geometry.coordinates[j][2]);
        }
    
        viewer.entities.add({
            name : 'ssroad',
            polyline : {
                positions : Cesium.Cartesian3.fromDegreesArrayHeights(positions),
                width : 3,
                clampToGround : true,
                material : new Cesium.ImageMaterialProperty({
                    color: Cesium.Color.GRAY.withAlpha(0.5)
                })
            }
        });

        var promise = Cesium.GeoJsonDataSource.load(
            './sroad.geojson', { clampToGround: true } 
        );
        promise.then(function (dataSource) {
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
}

addRoadBackgroundLayer({
    "type": "FeatureCollection",
    "features": [
            { "type": "Feature", "properties": { "FID_": "0", "Entity": "LWPolyline", "Layer": "景区石阶小路", "Color": "4", "Linetype": "CONTINUOUS", "Elevation": "0.00000000000e+000", "LineWt": "25", "RefName": "" }, "geometry": { "type": "LineString", "coordinates": [ [ 108.696955788146582, 27.910699212574428, 0.0 ], [ 108.696991964126937, 27.910768650495708, 0.0 ], [ 108.696987243503543, 27.910866512701915, 0.0 ], [ 108.696980163829011, 27.911044307292187, 0.0 ], [ 108.696875922165944, 27.911157869516593, 0.0 ], [ 108.696972314543274, 27.911240777467583, 0.0 ], [ 108.69691340836232, 27.911285101571849, 0.0 ], [ 108.697085015224985, 27.911455834111774, 0.0 ], [ 108.69694470031547, 27.911575781721091, 0.0 ], [ 108.696755544439895, 27.911564087900743, 0.0 ], [ 108.696539973616282, 27.911591355835686, 0.0 ], [ 108.696409364932379, 27.911512545882065, 0.0 ], [ 108.696228441105205, 27.911441218076835, 0.0 ], [ 108.695986975324871, 27.911441157355615, 0.0 ], [ 108.695991252219386, 27.911388880269289, 0.0 ], [ 108.695689833730285, 27.911426831509647, 0.0 ], [ 108.695619998373544, 27.911374585934247, 0.0 ], [ 108.6954153435195, 27.911458383751601, 0.0 ], [ 108.695258961518633, 27.91138696681789, 0.0 ], [ 108.695245080037566, 27.911794108033778, 0.0 ], [ 108.694851499923161, 27.91189455975498, 0.0 ], [ 108.694749839743864, 27.912044114942834, 0.0 ], [ 108.694423923111245, 27.912237085581573, 0.0 ], [ 108.694268519546355, 27.912485818745374, 0.0 ], [ 108.694198781388863, 27.912459920781441, 0.0 ], [ 108.694092439261723, 27.912628926832308, 0.0 ], [ 108.694013160617189, 27.912613851003172, 0.0 ], [ 108.693973105411615, 27.912271599494201, 0.0 ], [ 108.693959083130622, 27.912075308317384, 0.0 ], [ 108.693773988184603, 27.912099945898742, 0.0 ], [ 108.693687394346441, 27.912258023871185, 0.0 ], [ 108.69375123304593, 27.91235752096285, 0.0 ], [ 108.693792804808467, 27.912420341130726, 0.0 ], [ 108.693622436518993, 27.912522651369926, 0.0 ] ] } },
            { "type": "Feature", "properties": { "FID_": "0", "Entity": "LWPolyline", "Layer": "景区石阶小路", "Color": "4", "Linetype": "CONTINUOUS", "Elevation": "2.21332400000e+003", "LineWt": "25", "RefName": "" }, "geometry": { "type": "LineString", "coordinates": [ [ 108.692382238407234, 27.910501594532231, 2213.323974609375 ], [ 108.692344859039707, 27.910336046536703, 2213.323974609375 ], [ 108.692606004934021, 27.91024311703055, 2213.323974609375 ], [ 108.692676122524773, 27.910414879946774, 2213.323974609375 ], [ 108.692382238407234, 27.910501594532231, 2213.323974609375 ] ] } },
            { "type": "Feature", "properties": { "FID_": "0", "Entity": "LWPolyline", "Layer": "景区石阶小路", "Color": "4", "Linetype": "CONTINUOUS", "Elevation": "2.21285300000e+003", "LineWt": "25", "RefName": "" }, "geometry": { "type": "LineString", "coordinates": [ [ 108.692517995215823, 27.910269917499001, 2212.85302734375 ], [ 108.692379685518631, 27.909802776879456, 2212.85302734375 ], [ 108.692118078150216, 27.909288809860076, 2212.85302734375 ], [ 108.691507095651403, 27.90907620677876, 2212.85302734375 ], [ 108.691391316257111, 27.908862072160002, 2212.85302734375 ], [ 108.69109271548426, 27.908663428084218, 2212.85302734375 ], [ 108.691026167888623, 27.908055690364858, 2212.85302734375 ], [ 108.690791815280875, 27.907642602739756, 2212.85302734375 ], [ 108.690586055395514, 27.907355376523473, 2212.85302734375 ], [ 108.690502454410733, 27.907222066302158, 2212.85302734375 ], [ 108.690433669908529, 27.907065797952832, 2212.85302734375 ], [ 108.690355994837958, 27.906854701753105, 2212.85302734375 ], [ 108.690241766110063, 27.905930727471421, 2212.85302734375 ], [ 108.689033335377076, 27.904947559524427, 2212.85302734375 ], [ 108.687471052183042, 27.90350276812794, 2212.85302734375 ], [ 108.687161092793758, 27.903198974825944, 2212.85302734375 ] ] } },
            { "type": "Feature", "properties": { "FID_": "0", "Entity": "LWPolyline", "Layer": "景区石阶小路", "Color": "4", "Linetype": "CONTINUOUS", "Elevation": "2.10171100000e+003", "LineWt": "25", "RefName": "" }, "geometry": { "type": "LineString", "coordinates": [ [ 108.696955788146582, 27.910699212574428, 2101.7109375 ], [ 108.696881426936827, 27.910646115254863, 2101.7109375 ], [ 108.696590942014012, 27.910597107142859, 2101.7109375 ], [ 108.696165243074645, 27.910566738936467, 2101.7109375 ], [ 108.695968787071536, 27.910468057633906, 2101.7109375 ], [ 108.695771833447481, 27.910259868543655, 2101.7109375 ], [ 108.695589176990438, 27.910168200461793, 2101.7109375 ], [ 108.695331082276624, 27.910151745512625, 2101.7109375 ], [ 108.694874588155628, 27.910294754225724, 2101.7109375 ], [ 108.6944160057798, 27.910319400792048, 2101.7109375 ], [ 108.693631250856228, 27.910418419110396, 2101.7109375 ], [ 108.693135364912322, 27.91029927825106, 2101.7109375 ], [ 108.69282394335292, 27.910305447414981, 2101.7109375 ] ] } },
            { "type": "Feature", "properties": { "FID_": "0", "Entity": "LWPolyline", "Layer": "景区石阶小路", "Color": "4", "Linetype": "CONTINUOUS", "Elevation": "2.31342400000e+003", "LineWt": "25", "RefName": "" }, "geometry": { "type": "LineString", "coordinates": [ [ 108.697625793148902, 27.925940665296864, 2313.424072265625 ], [ 108.697046750560204, 27.925145917051342, 2313.424072265625 ], [ 108.696088894024797, 27.923039244259115, 2313.424072265625 ], [ 108.695724257900594, 27.921085773914704, 2313.424072265625 ], [ 108.695208959010145, 27.917861959295806, 2313.424072265625 ], [ 108.694657749993269, 27.91657125923118, 2313.424072265625 ], [ 108.694488985793328, 27.916079956518779, 2313.424072265625 ], [ 108.694236542388452, 27.914164805269134, 2313.424072265625 ], [ 108.694128844785126, 27.913886547144251, 2313.424072265625 ], [ 108.69391626588623, 27.913602149082486, 2313.424072265625 ], [ 108.693754651811958, 27.913160276315129, 2313.424072265625 ], [ 108.693763859428032, 27.913062315209313, 2313.424072265625 ], [ 108.693715438643366, 27.912824129462241, 2313.424072265625 ], [ 108.693622436518993, 27.912522651369926, 2313.424072265625 ], [ 108.693552308264003, 27.912540654696041, 2313.424072265625 ], [ 108.692938744023678, 27.91290614815367, 2313.424072265625 ], [ 108.692712053120275, 27.913125686130197, 2313.424072265625 ], [ 108.692425317847025, 27.913211552784318, 2313.424072265625 ], [ 108.69265119891908, 27.913021021091865, 2313.424072265625 ], [ 108.692703848844317, 27.912852168933007, 2313.424072265625 ], [ 108.693209227207618, 27.912097718876534, 2313.424072265625 ], [ 108.693199991591499, 27.911880856837797, 2313.424072265625 ], [ 108.692971782990128, 27.911664483306552, 2313.424072265625 ], [ 108.692753881568947, 27.911214834349472, 2313.424072265625 ], [ 108.692676494661967, 27.910908313977291, 2313.424072265625 ], [ 108.692441182552201, 27.910679802430995, 2313.424072265625 ], [ 108.692407516966455, 27.910499301774546, 2313.424072265625 ], [ 108.692517995215823, 27.910269917499001, 2313.424072265625 ], [ 108.692379685518631, 27.909802776879456, 2313.424072265625 ], [ 108.692118078150216, 27.909288809860076, 2313.424072265625 ], [ 108.691507095651403, 27.90907620677876, 2313.424072265625 ], [ 108.691391316257111, 27.908862072160002, 2313.424072265625 ], [ 108.69109271548426, 27.908663428084218, 2313.424072265625 ], [ 108.691026167888623, 27.908055690364858, 2313.424072265625 ], [ 108.690791815280875, 27.907642602739756, 2313.424072265625 ], [ 108.690586055395514, 27.907355376523473, 2313.424072265625 ], [ 108.690502454410733, 27.907222066302158, 2313.424072265625 ], [ 108.690433669908529, 27.907065797952832, 2313.424072265625 ], [ 108.690355994837958, 27.906854701753105, 2313.424072265625 ], [ 108.690241766110063, 27.905930727471421, 2313.424072265625 ], [ 108.689033335377076, 27.904947559524427, 2313.424072265625 ], [ 108.687471052183042, 27.90350276812794, 2313.424072265625 ], [ 108.687161092793758, 27.903198974825944, 2313.424072265625 ] ] } },
            { "type": "Feature", "properties": { "FID_": "0", "Entity": "LWPolyline", "Layer": "景区石阶小路", "Color": "4", "Linetype": "CONTINUOUS", "Elevation": "2.23459300000e+003", "LineWt": "25", "RefName": "" }, "geometry": { "type": "LineString", "coordinates": [ [ 108.692425317847025, 27.913211552784318, 2234.593017578125 ], [ 108.69211572405095, 27.913427843081401, 2234.593017578125 ], [ 108.692096266260066, 27.913517978037078, 2234.593017578125 ], [ 108.692091597858735, 27.914097567163168, 2234.593017578125 ], [ 108.692188233807244, 27.914740263160624, 2234.593017578125 ], [ 108.691917912926428, 27.915201489778553, 2234.593017578125 ], [ 108.692179476147743, 27.915491085859113, 2234.593017578125 ], [ 108.691507569651904, 27.91623934818119, 2234.593017578125 ], [ 108.691210979981264, 27.916524529255692, 2234.593017578125 ], [ 108.691317430331338, 27.916922006185104, 2234.593017578125 ], [ 108.690293561527923, 27.917722398921686, 2234.593017578125 ], [ 108.689744539560365, 27.918027728564336, 2234.593017578125 ], [ 108.689093208620392, 27.918253258412115, 2234.593017578125 ], [ 108.68834395222801, 27.91874917867214, 2234.593017578125 ], [ 108.687654087456281, 27.918758915398662, 2234.593017578125 ], [ 108.687409531652932, 27.918719440350301, 2234.593017578125 ], [ 108.686705673164255, 27.919269907201191, 2234.593017578125 ], [ 108.686305917803935, 27.919680027358876, 2234.593017578125 ], [ 108.684682106620855, 27.919490866139977, 2234.593017578125 ], [ 108.683464018590584, 27.919067324315797, 2234.593017578125 ], [ 108.682924951293103, 27.918458868958908, 2234.593017578125 ], [ 108.680548917473459, 27.917702312847076, 2234.593017578125 ], [ 108.679951842839742, 27.917691980106483, 2234.593017578125 ], [ 108.678361480609709, 27.917681416870675, 2234.593017578125 ], [ 108.677535288699715, 27.917478772904264, 2234.593017578125 ], [ 108.675794604900332, 27.916720603155422, 2234.593017578125 ], [ 108.674869593686623, 27.916465739313807, 2234.593017578125 ], [ 108.67350271556549, 27.916196337681573, 2234.593017578125 ], [ 108.672867166605769, 27.916049455002788, 2234.593017578125 ], [ 108.672299906274418, 27.91582081613549, 2234.593017578125 ], [ 108.671648913617673, 27.915350787341158, 2234.593017578125 ], [ 108.670811892121648, 27.914712144667277, 2234.593017578125 ], [ 108.670622989107429, 27.914630029777673, 2234.593017578125 ], [ 108.669943110621475, 27.914631182773856, 2234.593017578125 ], [ 108.669603958134246, 27.914735520957475, 2234.593017578125 ], [ 108.66902216537963, 27.91469341154313, 2234.593017578125 ], [ 108.668810305429702, 27.914637619222564, 2234.593017578125 ], [ 108.66813274140813, 27.914270165398396, 2234.593017578125 ], [ 108.667526845849935, 27.913948597632412, 2234.593017578125 ], [ 108.667300784385787, 27.913842781837332, 2234.593017578125 ], [ 108.666917070648708, 27.913777180485738, 2234.593017578125 ], [ 108.666416048629785, 27.913313844394732, 2234.593017578125 ], [ 108.666164953503312, 27.913139072228706, 2234.593017578125 ], [ 108.665275278921825, 27.912853526586009, 2234.593017578125 ], [ 108.664982774813168, 27.912787109108741, 2234.593017578125 ], [ 108.664190277178974, 27.912486360530185, 2234.593017578125 ], [ 108.663961586105899, 27.912559282252495, 2234.593017578125 ], [ 108.663608038579767, 27.912553614273399, 2234.593017578125 ], [ 108.663254230223473, 27.912583427434239, 2234.593017578125 ], [ 108.663012978492134, 27.91249082504509, 2234.593017578125 ], [ 108.662718478158297, 27.912531432231201, 2234.593017578125 ], [ 108.662447428090317, 27.912474642545359, 2234.593017578125 ], [ 108.662199313611382, 27.912530916020081, 2234.593017578125 ], [ 108.661762302286704, 27.912621204588575, 2234.593017578125 ], [ 108.66133970352162, 27.912397545619989, 2234.593017578125 ], [ 108.660691987101188, 27.912304896488717, 2234.593017578125 ], [ 108.660247101284568, 27.912404963530488, 2234.593017578125 ], [ 108.659991068060876, 27.912472198592408, 2234.593017578125 ], [ 108.65924789074883, 27.912594590484709, 2234.593017578125 ], [ 108.658516140882583, 27.912561311688812, 2234.593017578125 ], [ 108.65751923763834, 27.912203010845715, 2234.593017578125 ], [ 108.657208805444952, 27.912046706054038, 2234.593017578125 ], [ 108.656434343191734, 27.911876930397415, 2234.593017578125 ], [ 108.656088832650582, 27.911795496708141, 2234.593017578125 ], [ 108.656070838201728, 27.911776199236741, 2234.593017578125 ] ] } },
            { "type": "Feature", "properties": { "FID_": "0", "Entity": "LWPolyline", "Layer": "景区石阶小路", "Color": "4", "Linetype": "CONTINUOUS", "Elevation": "2.10174600000e+003", "LineWt": "25", "RefName": "" }, "geometry": { "type": "LineString", "coordinates": [ [ 108.696947710000529, 27.91070355769963, 2101.74609375 ], [ 108.697242772903593, 27.910652253967136, 2101.74609375 ], [ 108.697968808041509, 27.910489165690031, 2101.74609375 ], [ 108.698879355871938, 27.909529565721108, 2101.74609375 ], [ 108.699084617804459, 27.908952134614207, 2101.74609375 ], [ 108.699874493309181, 27.90819802975437, 2101.74609375 ], [ 108.700642065354046, 27.907792668384353, 2101.74609375 ], [ 108.701122906094696, 27.907702701691711, 2101.74609375 ], [ 108.702190775739297, 27.907306269972985, 2101.74609375 ], [ 108.702238463619153, 27.906802890896348, 2101.74609375 ], [ 108.702164158375524, 27.90567196726203, 2101.74609375 ], [ 108.703119119232042, 27.904332473681126, 2101.74609375 ], [ 108.703866621072493, 27.903622687801256, 2101.74609375 ], [ 108.70379811882357, 27.902499134400042, 2101.74609375 ], [ 108.703553204910861, 27.902162507114063, 2101.74609375 ], [ 108.703752936399383, 27.901721957688974, 2101.74609375 ], [ 108.70386744036918, 27.901058898164116, 2101.74609375 ], [ 108.704351463294103, 27.899765701323336, 2101.74609375 ], [ 108.704678240493294, 27.899391157933941, 2101.74609375 ], [ 108.706365897228636, 27.899406083419333, 2101.74609375 ], [ 108.70676634704634, 27.899221085451156, 2101.74609375 ], [ 108.708080087238471, 27.899661708913136, 2101.74609375 ], [ 108.708590964403214, 27.899472672927637, 2101.74609375 ], [ 108.709125212311292, 27.899053422811228, 2101.74609375 ], [ 108.709252527529046, 27.898889882911934, 2101.74609375 ], [ 108.710006037766547, 27.898959579226478, 2101.74609375 ], [ 108.710398955039025, 27.898871206897102, 2101.74609375 ], [ 108.711269033297796, 27.898717024996145, 2101.74609375 ], [ 108.711724256554675, 27.898730473932275, 2101.74609375 ], [ 108.712411388497301, 27.898523648063968, 2101.74609375 ], [ 108.713129102862979, 27.897995205025349, 2101.74609375 ], [ 108.713874757232574, 27.897261932000085, 2101.74609375 ], [ 108.714385803766731, 27.897338482479331, 2101.74609375 ], [ 108.714719944625102, 27.897352008061574, 2101.74609375 ], [ 108.715030652445009, 27.897312758341194, 2101.74609375 ], [ 108.715312197477601, 27.897137214064639, 2101.74609375 ], [ 108.715726140432864, 27.897070816191405, 2101.74609375 ], [ 108.716163911603033, 27.897066303122848, 2101.74609375 ], [ 108.716898978716785, 27.897270021545083, 2101.74609375 ], [ 108.717781270198785, 27.897652366336288, 2101.74609375 ], [ 108.718732627914491, 27.897823383260619, 2101.74609375 ], [ 108.718954438965838, 27.897609766078645, 2101.74609375 ], [ 108.719940101167865, 27.897321215319181, 2101.74609375 ], [ 108.720598172195707, 27.897202047343903, 2101.74609375 ], [ 108.720946041075678, 27.897181295755001, 2101.74609375 ], [ 108.721350050397689, 27.896878064662541, 2101.74609375 ], [ 108.722309383354514, 27.896844794057309, 2101.74609375 ], [ 108.722870174916963, 27.896749424161214, 2101.74609375 ], [ 108.721838299267361, 27.896581447435054, 2101.74609375 ], [ 108.721232019529779, 27.896341961501843, 2101.74609375 ], [ 108.720783376762441, 27.896087932337306, 2101.74609375 ], [ 108.720791459917564, 27.895507007478241, 2101.74609375 ], [ 108.722024901485838, 27.894783518287792, 2101.74609375 ], [ 108.721713690136085, 27.894340641590976, 2101.74609375 ] ] } }
        ]
    }
);

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

// addAreaBackgroundLayer();

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

// addSceneLayer();

var handler = new Cesium.ScreenSpaceEventHandler(viewer.scene.canvas);

handler.setInputAction(function(click) { // 点击事件
    viewer.clock.stopTime = viewer.clock.startTime; //viewer.clock.stopTime 赋值为 viewer.clock.startTime 即可停止旋转

    var pickElement = viewer.scene.pick(click.position);
    if (pickElement && pickElement.id){

    }
}, Cesium.ScreenSpaceEventType.LEFT_DOWN);

//position_A绕position_B逆时针旋转angle度（角度）得到新点
function rotatedPointByAngleXY(position_A, position_B, angle) {
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
    var scene = viewer.scene;
    var ellipsoid = scene.globe.ellipsoid;
    var canvas = scene.canvas;
    let center_position =  viewer.scene.camera.pickEllipsoid(new Cesium.Cartesian2(canvas.width / 2.0, canvas.height / 2.0), ellipsoid);

    //viewer.scene.camera.rotate(center_position, Cesium.Math.toRadians(angle));

    let new_position = rotatedPointByAngleXY(viewer.camera.position, center_position, angle);

    var heading = viewer.camera.heading + Cesium.Math.toRadians(angle);
    var pitch = viewer.camera.pitch;
    var roll = viewer.camera.roll;

    viewer.scene.camera.flyTo({
        destination : new_position,
        orientation: {
            heading : heading,
            pitch : pitch,
            roll : roll
        }
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
    var scene = viewer.scene;
    var ellipsoid = scene.globe.ellipsoid;
    var canvas = scene.canvas;
    let center_position =  viewer.scene.camera.pickEllipsoid(new Cesium.Cartesian2(canvas.width / 2.0, canvas.height / 2.0), ellipsoid);

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
        }
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
//
//function optMap(action, data) {
//    if (action === "rotateByLeft") {
//        let angle = parseInt(data);
//        rotateLeftRight(angle);
//    }
//    else if (action === "rotateByRight") {
//        let angle = parseInt(data);
//        rotateLeftRight(-angle);
//    }
//    else if (action === "rotateByUp") {
//        let angle = parseInt(data);
//        rotateUpDown(angle);
//    }
//    else if (action === "rotateByDown") {
//        let angle = parseInt(data);
//        rotateUpDown(-angle);
//    }
//    else if (action === "zoomIn") {
//        viewer.camera.zoomIn(viewer.camera.positionCartographic.height / 2);
//    }
//    else if (action === "zoomOut") {
//        viewer.camera.zoomOut(viewer.camera.positionCartographic.height * 2);
//    }
//    else if (action === "pointToNorth") {
//        rotateLeftRight(Cesium.Math.toDegrees(-viewer.camera.heading));
//    }
//}
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
    else if (opt.action === "opt.rotateByDown") {
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
    var userTourJSON = JSON.parse(userTour);
    var positions = [];
    for (var i = 0; i < userTourJSON.length; i++) {
        positions.push(Cesium.Cartographic.fromDegrees(userTourJSON[i].longitude, userTourJSON[i].latitude));
    }

    promise = Cesium.sampleTerrain(terrainProvider, 14, positions);
    Cesium.when(promise, function (updatedPositions) {
        if (updatedPositions.length > 0) {
            for (var i = 0; i <userTourJSON.length; i++) {
                userTourJSON[i].height = updatedPositions[i].height;
            }
            window.Android.putUserTourHeights(JSON.stringify(userTourJSON));
        }
    });
}

var options = {};
function resetView() {
    recenter(userLocation);
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