var terrainProvider = new Cesium.CesiumTerrainProvider({url:"http://47.110.155.250:8003/Map/Terrain"});
var viewer = new Cesium.Viewer('cesiumContainer', {
    shadows: true,
    animation: false,
    homeButton: false,
    geocoder: false,
    baseLayerPicker: false,
    timeline: false,
    fullscreenButton: true,
    scene3DOnly: true,
    sceneModePicker: false,
    navigationInstructionsInitiallyVisible: false,
    navigationHelpButton: false,
    selectionIndicator: false,

    infoBox: false,
    selectionIndicator: false,
    shouldAnimate: false,

    terrainProvider: terrainProvider,
    imageryProvider : new Cesium.UrlTemplateImageryProvider({url:"http://47.110.155.250:8003/Map/Image/{z}/{x}/{y}.png"})
});

viewer._cesiumWidget._creditContainer.style.display = "none";

viewer.scene.globe.depthTestAgainstTerrain = true;

viewer.scene.screenSpaceCameraController.minimumZoomDistance = 10; //相机的高度的最小值
viewer.scene.screenSpaceCameraController.maximumZoomDistance = 80000; //22000000; //相机高度的最大值
viewer.scene.screenSpaceCameraController._minimumZoomRate = 1000; //30000; //相机缩小时的速率
viewer.scene.screenSpaceCameraController._maximumZoomRate = 1000000; //5906376272000; //相机放大时的速率
viewer.scene.screenSpaceCameraController.maximumMovementRatio = 0.01;

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

    /*
    // 相机原地旋转飞行
    var exec = function TimeExecution() {
            var delTime = Cesium.JulianDate.secondsDifference(viewer.clock.currentTime, viewer.clock.startTime);
            var heading = Cesium.Math.toRadians(delTime * angle) + initialHeading;
            viewer.scene.camera.setView({
                orientation: {
                    heading : heading,
                    pitch : pitch,

                }
            });
            if (Cesium.JulianDate.compare(viewer.clock.currentTime, viewer.clock.stopTime) >= 0) {
                viewer.clock.onTick.removeEventListener(exec);
            }
    };
    viewer.clock.onTick.addEventListener(exec);
    */
}

/* 获取camera高度  */
function getHeight() {
    if (viewer) {
        var scene = viewer.scene;
        var ellipsoid = scene.globe.ellipsoid;
        var height = ellipsoid.cartesianToCartographic(viewer.camera.position).height;
        return height;
    }
}

/* 获取camera中心点坐标 */
function getCenterPosition() {
    var result = viewer.camera.pickEllipsoid(new Cesium.Cartesian2(viewer.canvas.clientWidth / 2, viewer.canvas.clientHeight / 2));
    var curPosition = Cesium.Ellipsoid.WGS84.cartesianToCartographic(result);
    var lon = curPosition.longitude * 180 / Math.PI;
    var lat = curPosition.latitude * 180 / Math.PI;
    var height = getHeight();
    
    return {
        lon: lon,
        lat: lat,
        height: height
    };
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

var west = 108.5506111775;
var south = 27.6176314387;
var east = 108.8606161775;
var north = 28.0951804387;
var center = {x : (west + east) / 2, y : (south + north) / 2};

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
        viewer.camera.setView({
            destination : Cesium.Rectangle.fromDegrees(west, south, east, north)
        });
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

                var position = Cesium.Cartesian3.fromDegrees(Cesium.Math.toDegrees(updatedPositions[0].longitude), Cesium.Math.toDegrees(updatedPositions[0].latitude), updatedPositions[0].height);
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

function clickPoi() {
    viewer.clock.stopTime = viewer.clock.startTime;

    window.Android.fetchRoute(JSON.stringify(userLocation), JSON.stringify(pois[this.value]));

//    updateUserTour([
//        {
//            "name": "我的位置",
//            "latitude": 27.918094,
//            "longitude": 108.690813
//        },
//        {
//            "latitude": 27.917657686039245,
//            "longitude": 108.69037617890788
//        },
//        {
//            "latitude": 27.91692192666803,
//            "longitude": 108.69131740982795
//        },
//        {
//            "latitude": 27.9165244381843,
//            "longitude": 108.69121086652304
//        },
//        {
//            "latitude": 27.91623926720558,
//            "longitude": 108.69150739963742
//        },
//        {
//            "latitude": 27.91549104263241,
//            "longitude": 108.69217944202228
//        },
//        {
//            "latitude": 27.91520140130523,
//            "longitude": 108.69191774037297
//        },
//        {
//            "latitude": 27.914740210355973,
//            "longitude": 108.69218819645468
//        },
//        {
//            "latitude": 27.914097411500606,
//            "longitude": 108.69209152516927
//        },
//        {
//            "latitude": 27.913517942581727,
//            "longitude": 108.69209618178225
//        },
//        {
//            "latitude": 27.91342779055449,
//            "longitude": 108.69211555329224
//        },
//        {
//            "latitude": 27.91321153744783,
//            "longitude": 108.69242531118748
//        },
//        {
//            "latitude": 27.91312566950453,
//            "longitude": 108.69271197228235
//        },
//        {
//            "latitude": 27.912906063636527,
//            "longitude": 108.69293865620207
//        },
//        {
//            "latitude": 27.91254061265008,
//            "longitude": 108.69355221152794
//        },
//        {
//            "latitude": 27.912522544991727,
//            "longitude": 108.69362243325165
//        },
//        {
//            "latitude": 27.91242028577075,
//            "longitude": 108.6937926790221
//        },
//        {
//            "latitude": 27.912257863110106,
//            "longitude": 108.69368725330429
//        },
//        {
//            "latitude": 27.91209991079792,
//            "longitude": 108.69377386630566
//        },
//        {
//            "latitude": 27.912075137616885,
//            "longitude": 108.69395901323763
//        },
//        {
//            "latitude": 27.912613814606082,
//            "longitude": 108.69401302994817
//        },
//        {
//            "latitude": 27.91262890203213,
//            "longitude": 108.6940923786333
//        },
//        {
//            "latitude": 27.9124597738488,
//            "longitude": 108.6941987356737
//        },
//        {
//            "latitude": 27.91248566461695,
//            "longitude": 108.69426839860384
//        },
//        {
//            "latitude": 27.91223700148397,
//            "longitude": 108.69442374321275
//        },
//        {
//            "latitude": 27.912044031442196,
//            "longitude": 108.69474970612116
//        },
//        {
//            "latitude": 27.91189446103337,
//            "longitude": 108.69485140654858
//        },
//        {
//            "latitude": 27.91179406445758,
//            "longitude": 108.69524498347741
//        },
//        {
//            "latitude": 27.91138689021886,
//            "longitude": 108.69525895331634
//        },
//        {
//            "latitude": 27.911458229529668,
//            "longitude": 108.69541522924786
//        },
//        {
//            "latitude": 27.91137441049608,
//            "longitude": 108.69561993395433
//        },
//        {
//            "latitude": 27.911426750825942,
//            "longitude": 108.69568978314898
//        },
//        {
//            "latitude": 27.91138875286405,
//            "longitude": 108.69599115914086
//        },
//        {
//            "latitude": 27.911441093193915,
//            "longitude": 108.69598687505693
//        },
//        {
//            "latitude": 27.911441093193915,
//            "longitude": 108.69622827387366
//        },
//        {
//            "latitude": 27.911512432504722,
//            "longitude": 108.69640932298621
//        },
//        {
//            "latitude": 27.911591222396297,
//            "longitude": 108.6965398944141
//        },
//        {
//            "latitude": 27.91156402777651,
//            "longitude": 108.69675540246267
//        },
//        {
//            "latitude": 27.911575762441213,
//            "longitude": 108.69694464721407
//        },
//        {
//            "latitude": 27.91145580809092,
//            "longitude": 108.69708490439695
//        },
//        {
//            "latitude": 27.91128500352692,
//            "longitude": 108.69691335477486
//        },
//        {
//            "latitude": 27.911240672571378,
//            "longitude": 108.6969722143629
//        },
//        {
//            "latitude": 27.911157784860382,
//            "longitude": 108.69687591560653
//        },
//        {
//            "latitude": 27.91104416350374,
//            "longitude": 108.6969800374727
//        },
//        {
//            "latitude": 27.910768492015492,
//            "longitude": 108.69699195840192
//        },
//        {
//            "latitude": 27.910699201614392,
//            "longitude": 108.69695563682069
//        },
//        {
//            "latitude": 27.910645929961934,
//            "longitude": 108.69688131727759
//        },
//        {
//            "latitude": 27.910596942393415,
//            "longitude": 108.69659093089233
//        },
//        {
//            "latitude": 27.910566581276804,
//            "longitude": 108.69616513020169
//        },
//        {
//            "latitude": 27.910468047346207,
//            "longitude": 108.69596862113406
//        },
//        {
//            "latitude": 27.91025980361387,
//            "longitude": 108.69577173953738
//        },
//        {
//            "latitude": 27.910168161470477,
//            "longitude": 108.69558901404416
//        },
//        {
//            "latitude": 27.91015158392828,
//            "longitude": 108.69533103768522
//        },
//        {
//            "latitude": 27.910294635078937,
//            "longitude": 108.69487450334894
//        },
//        {
//            "latitude": 27.910319221995454,
//            "longitude": 108.69441592010295
//        },
//        {
//            "latitude": 27.910382729286447,
//            "longitude": 108.69391299488315
//        },
//        {
//            "name": "金顶",
//            "latitude": 27.9112084171,
//            "longitude": 108.6940465145
//        }
//    ]);
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
        htmlOverlay.innerHTML = '<div style="color: #fff; text-align: center;">' + pois[i].name + '</div>\
        <img style="position: relative; left: 50%; transform: translate(-23px, 4px); border:3px solid #fff; border-radius: 2px; height: 40px; width: 40px" src="'+ pois[i].thumbnail +'"/>\
        <div style="position: relative; width: 100%; height: 13px"><img style="transform: translate(13px, -3px);" src="tri-white.png" alt=""></div>';
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

                    var position = Cesium.Cartesian3.fromDegrees(Cesium.Math.toDegrees(updatedPositions[htmlOverlay.value].longitude), Cesium.Math.toDegrees(updatedPositions[htmlOverlay.value].latitude), updatedPositions[htmlOverlay.value].height);
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
    {name: "九龙池", longitude: 108.6883161949, latitude: 27.9028590332, thumbnail : "http://tiles.pano.vizen.cn/6A96E59B1701491990DB44C603664DFB/sphere/thumb.jpg"},
    {name: "鱼坳", longitude: 108.7227589833, latitude: 27.8964451440, thumbnail : "http://tiles.pano.vizen.cn/6A96E59B1701491990DB44C603664DFB/sphere/thumb.jpg"},
    {name: "烂茶顶", longitude: 108.6977088697, latitude: 27.9260234287, thumbnail : "http://tiles.pano.vizen.cn/6A96E59B1701491990DB44C603664DFB/sphere/thumb.jpg"}
];

updatePoiLocation(pois);

var userTour = null;

function updateUserTour(positions) {
    var xmin = 180.0;
    var ymin = 90.0;
    var xmax = -180.0;
    var ymax = -90.0;

    var positionArray = [];
    for (var i = 0; i < positions.length; i++) {
        positionArray.push(positions[i].longitude);
        positionArray.push(positions[i].latitude);
        positionArray.push(0.0);

        if (xmin > positions[i].longitude) {
            xmin = positions[i].longitude;
        }
        if (xmax < positions[i].longitude) {
            xmax = positions[i].longitude;
        }
        if (ymin > positions[i].latitude) {
            ymin = positions[i].latitude;
        }
        if (ymax < positions[i].latitude) {
            ymax = positions[i].latitude;
        }
    }
    if (userTour) {
        viewer.entities.remove(userTour);
        userTour = null;
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
}

function stopNavigation() {
    viewer.trackedEntity = undefined;
}

function addRoadBackgroundLayer(road) {

    for (var i = 0; i < road.features.length; i++) {
        var positions = [];
        for (var j = 0; j < road.features[i].geometry.coordinates.length; j++) {
            positions.push(road.features[i].geometry.coordinates[j][0]);
            positions.push(road.features[i].geometry.coordinates[j][1]);
            positions.push(road.features[i].geometry.coordinates[j][2]);
        }
    
        var redLine = viewer.entities.add({
            name : '景区石阶小路',
            polyline : {
                positions : Cesium.Cartesian3.fromDegreesArrayHeights(positions),
                width : 5,
                clampToGround : true,
                material : new Cesium.ImageMaterialProperty({
                    color: Cesium.Color.WHITE.withAlpha(0.25),
                    outlineWidth : 1,
                    outlineColor : Cesium.Color.WHITE.withAlpha(0.25)
                })
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

var handler = new Cesium.ScreenSpaceEventHandler(viewer.scene.canvas);

handler.setInputAction(function(click) { // 点击事件
    viewer.clock.stopTime = viewer.clock.startTime; //viewer.clock.stopTime 赋值为 viewer.clock.startTime 即可停止旋转

    var pickElement = viewer.scene.pick(click.position);
    if (pickElement && pickElement.id){

    }
}, Cesium.ScreenSpaceEventType.LEFT_DOWN);

handler.setInputAction(function(click) { // 点击事件
    viewer.clock.stopTime = viewer.clock.startTime; //viewer.clock.stopTime 赋值为 viewer.clock.startTime 即可停止旋转

    //clearUserTour();

    var pickElement = viewer.scene.pick(click.position);
    if (pickElement && pickElement.id){

    }
}, Cesium.ScreenSpaceEventType.RIGHT_DOWN);

handler.setInputAction(function(wheelment) { // 滚轮事件

}, Cesium.ScreenSpaceEventType.WHEEL);

viewer.scene.camera.moveEnd.addEventListener(function(){ // 相机移动结束事件
    height = Math.ceil(viewer.camera.positionCartographic.height); // 相机高度

});