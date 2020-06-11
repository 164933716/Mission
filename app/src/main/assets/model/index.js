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

var FJSImageryLayer = new Cesium.UrlTemplateImageryProvider({url:"https://sinomaps-mission.oss-accelerate.aliyuncs.com/Image/{z}/{x}/{y}.png",proxy : new Cesium.DefaultProxy('/proxy/')});

var mapboxLayer = new Cesium.UrlTemplateImageryProvider({url:"https://map-cache.oss-cn-hangzhou.aliyuncs.com/styles/v1/wingwuyf/cjajfiz60artm2rnxr8wiecli/tiles/256/{z}/{x}/{y}?access_token=pk.eyJ1Ijoid2luZ3d1eWYiLCJhIjoiY2s3b282bm90MDJ1dDNtbGlibGowMTRoeSJ9.hDrbU176NROA7KYhmBkmEg"});
var mapboxOutdoorsLayer = new Cesium.UrlTemplateImageryProvider({url:"https://map-cache.oss-cn-hangzhou.aliyuncs.com/styles/v1/mapbox/outdoors-v11/tiles/256/{z}/{x}/{y}?access_token=pk.eyJ1Ijoid2luZ3d1eWYiLCJhIjoiY2s3b282bm90MDJ1dDNtbGlibGowMTRoeSJ9.hDrbU176NROA7KYhmBkmEg"});
var googleLayer = new Cesium.UrlTemplateImageryProvider({url:"http://mt2.google.cn/vt/lyrs=s@167000000&hl=zh-CN&gl=cn&x={x}&y={y}&z={z}&s=Galil"});

viewer.imageryLayers.removeAll();
viewer.imageryLayers.addImageryProvider(mapboxLayer);
viewer.imageryLayers.addImageryProvider(FJSImageryLayer);

viewer._cesiumWidget._creditContainer.style.display = "none";

viewer.scene.globe.depthTestAgainstTerrain = true;

// viewer.scene.screenSpaceCameraController.minimumZoomDistance = 10; //相机的高度的最小值
// viewer.scene.screenSpaceCameraController.maximumZoomDistance = 80000; //22000000; //相机高度的最大值
// viewer.scene.screenSpaceCameraController._minimumZoomRate = 1000; //30000; //相机缩小时的速率
// viewer.scene.screenSpaceCameraController._maximumZoomRate = 1000000; //5906376272000; //相机放大时的速率
// viewer.scene.screenSpaceCameraController.maximumMovementRatio = 0.01;

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

var userLocation = {longitude: 108.7107853492, latitude: 27.8601391146, height: 1446.697};

var initialized = false;

function getInitialized() {
    return initialized;
}

function initOK() {
    if (window.Android) {
        window.Android.initOK();
    }

    if (typeof initOKInternal === "function") {
        initOKInternal();
    }

    initialized = true;
}

function init() {
    addAreaBackgroundLayer();

    viewer.camera.flyTo({
        destination : Cesium.Cartesian3.fromDegrees(userLocation.longitude, userLocation.latitude, 18000000),
        complete : function() {
            viewer.camera.flyTo({
                destination : Cesium.Cartesian3.fromDegrees(userLocation.longitude, userLocation.latitude, 20000),
                complete : function() {
                    if (initOK) {
                        rotateByUp(60.0, initOK);
                    }
                    else {
                        rotateByUp(60.0);
                    }
                }
            });
        }
    });

}


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

init();

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

var userTourPostitions = [];

var poiPreRenderListener = null;

function clickPoi() {
    viewer.clock.stopTime = viewer.clock.startTime;

    clearPoiDetail();

    var htmlOverlay = document.getElementById('infoOverlay');
    htmlOverlay.data = this.data;

    promise = Cesium.sampleTerrainMostDetailed(terrainProvider, [Cesium.Cartographic.fromDegrees(this.data.geometry.coordinates[0], this.data.geometry.coordinates[1])]);
    Cesium.when(promise, function (updatedPositions) {
        if (updatedPositions.length > 0) {
            poiPreRenderListener = function(l, t) {
                for (var i = 0; i < updatedPositions.length; i++) {
                    var htmlOverlay = document.getElementById('infoOverlay');
                    if (htmlOverlay) {
                        var offsetHeight = 0;
                        if (htmlOverlay.data.properties.class === 'POI') {
                            htmlOverlay.children[1].src = htmlOverlay.data.properties.thumbnail;
                            htmlOverlay.children[2].innerHTML = htmlOverlay.data.properties.名称;
                            offsetHeight = 46 + 10;
                        }
                        else {
                            htmlOverlay.children[1].src = 'img/' + htmlOverlay.data.properties.图片;
                            htmlOverlay.children[2].innerHTML = htmlOverlay.data.properties.名称;
                            htmlOverlay.children[3].innerHTML = htmlOverlay.data.properties.简介1;
                            offsetHeight = 32 + 5;
                        }
                        htmlOverlay.style.display = 'block';

                        var scratch = new Cesium.Cartesian2();

                        var position = Cesium.Cartesian3.fromDegrees(Cesium.Math.toDegrees(updatedPositions[0].longitude), Cesium.Math.toDegrees(updatedPositions[0].latitude), updatedPositions[0].height * terrainExaggeration);
                        var canvasPosition = viewer.scene.cartesianToCanvasCoordinates(position, scratch);
                        if (Cesium.defined(canvasPosition)) {
                            htmlOverlay.style.top = canvasPosition.y - htmlOverlay.offsetHeight - offsetHeight + 'px';
                            htmlOverlay.style.left = canvasPosition.x - htmlOverlay.offsetWidth / 2 + 'px';
                            htmlOverlay.style.zIndex = Math.ceil(canvasPosition.y);
                        }
                    }
                }
            };
            viewer.scene.preRender.addEventListener(poiPreRenderListener);
        }
    });
}

function clearPoiDetail() {
    if (poiPreRenderListener) {
        viewer.scene.preRender.removeEventListener(poiPreRenderListener);
        poiPreRenderListener = null;
    }
    var htmlOverlay = document.getElementById('infoOverlay');
    htmlOverlay.children[1].src = '';
    htmlOverlay.children[2].innerHTML = '';
    htmlOverlay.children[3].innerHTML = '';
    htmlOverlay.style.display = 'none';
}

function updatePoiLocation(geojson) {
    clearPoiLocation();

    var positions = [];
    for (var i = 0; i < geojson.features.length; i++) {
        var htmlOverlay = document.createElement('div');
        htmlOverlay.id = 'htmlOverlay' + i;
        htmlOverlay.onclick = clickPoi;
        htmlOverlay.style = 'cursor: pointer;';
        htmlOverlay.value = i;
        htmlOverlay.className = 'imgContent';
        geojson.features[i].properties.class = 'POI';
        htmlOverlay.data = geojson.features[i];
        htmlOverlay.innerHTML = '<img style="position: relative; left: 50%; transform: translate(-23px, 4px); border:3px solid #fff; border-radius: 2px; height: 40px; width: 40px" src="'+ geojson.features[i].properties.thumbnail +'"/>';
        // htmlOverlay.innerHTML = '<div style="color: #fff; text-align: center;">' + pois[i].name + '</div>\
        // <img style="position: relative; left: 50%; transform: translate(-23px, 4px); border:3px solid #fff; border-radius: 2px; height: 40px; width: 40px" src="'+ pois[i].thumbnail +'"/>\
        // <div style="position: relative; width: 100%; height: 13px"><img style="transform: translate(13px, -3px);" src="tri-white.png" alt=""></div>';
        document.body.appendChild(htmlOverlay);

        positions.push(Cesium.Cartographic.fromDegrees(geojson.features[i].geometry.coordinates[0], geojson.features[i].geometry.coordinates[1]));
    }

    promise = Cesium.sampleTerrainMostDetailed(terrainProvider, positions);
    Cesium.when(promise, function (updatedPositions) {
        if (updatedPositions.length > 0) {
            viewer.scene.preRender.addEventListener(function(l, t) {
                for (var i = 0; i < updatedPositions.length; i++) {
                    var htmlOverlay = document.getElementById('htmlOverlay' + i);
                    if (htmlOverlay) {
                        var scratch = new Cesium.Cartesian2();

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

var userTour = null;

function updateUserTour(positions) {
    if (!initialized) {
        return;
    }

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
                userTourPostitions[i].height = updatedPositions[i].height * terrainExaggeration;
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

    var promiseSRoadEast = Cesium.GeoJsonDataSource.load(
        road.sroadeast, { clampToGround: true } 
    );
    promiseSRoadEast.then(function (dataSource) {
        dataSource.name = "sroadeast";
        viewer.dataSources.add(dataSource);
        for (var i = 0; i < dataSource.entities.values.length; i++) {
            var entity = dataSource.entities.values[i];
            
            // entity.billboard.disableDepthTestDistance = Number.POSITIVE_INFINITY; //去掉地形遮挡
            entity.polyline.width = 3;
            entity.polyline.material = new Cesium.ImageMaterialProperty({
                color: Cesium.Color.GRAY.withAlpha(0.7)
            });
        }
    });

    var promiseSRoadWest = Cesium.GeoJsonDataSource.load(
        road.sroadwest, { clampToGround: true } 
    );
    promiseSRoadWest.then(function (dataSource) {
        dataSource.name = "sroadwest";
        viewer.dataSources.add(dataSource);
        for (var i = 0; i < dataSource.entities.values.length; i++) {
            var entity = dataSource.entities.values[i];
            
            // entity.billboard.disableDepthTestDistance = Number.POSITIVE_INFINITY; //去掉地形遮挡
            entity.polyline.width = 3;
            entity.polyline.material = new Cesium.ImageMaterialProperty({
                color: Cesium.Color.GRAY.withAlpha(0.7)
            });
        }
    });

    var promiseCableRoad = Cesium.GeoJsonDataSource.load(
        road.cableroad, { clampToGround: true } 
    );
    promiseCableRoad.then(function (dataSource) {
        dataSource.name = "cableroad";
        viewer.dataSources.add(dataSource);
        for (var i = 0; i < dataSource.entities.values.length; i++) {
            var entity = dataSource.entities.values[i];
            
            // entity.billboard.disableDepthTestDistance = Number.POSITIVE_INFINITY; //去掉地形遮挡
            entity.polyline.width = 3;
            entity.polyline.material = new Cesium.ImageMaterialProperty({
                color: Cesium.Color.BLUE.withAlpha(0.5)
            });
        }
    });

    // var promiseRoad = Cesium.GeoJsonDataSource.load(
    //     road.road, { clampToGround: true } 
    // );
    // promiseRoad.then(function (dataSource) {
    //     dataSource.name = "road";
    //     viewer.dataSources.add(dataSource);
    //     for (var i = 0; i < dataSource.entities.values.length; i++) {
    //         var entity = dataSource.entities.values[i];
            
    //         // entity.billboard.disableDepthTestDistance = Number.POSITIVE_INFINITY; //去掉地形遮挡
    //         entity.polyline.width = 3;
    //         entity.polyline.material = new Cesium.ImageMaterialProperty({
    //             color: Cesium.Color.GRAY.withAlpha(0.5)
    //         });
    //     }
    // });
}

function removeRoadBackgroundLayer(road) {
    let dataSourceSSRoad = viewer.dataSources.getByName("ssroad");
    viewer.dataSources.remove(dataSourceSSRoad[0]);

    let dataSourceSRoad = viewer.dataSources.getByName("sroadeast");
    viewer.dataSources.remove(dataSourceSRoad[0]);

    let dataSourceRoad = viewer.dataSources.getByName("sroadwest");
    viewer.dataSources.remove(dataSourceRoad[0]);

    let dataSourceCableRoad = viewer.dataSources.getByName("cableroad");
    viewer.dataSources.remove(dataSourceCableRoad[0]);
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
            entity.polyline.width = 3;
            entity.polyline.material = new Cesium.ImageMaterialProperty({
                color: Cesium.Color.YELLOW.withAlpha(0.25)
            });
        }
    });

    return;

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

function clickEntity() {
    if (window.Android) {
        window.Android.poiClick(JSON.stringify(this.data));
    }
}

function addAnimalLayer(geojson) {
    removeAnimalLayer();

    var positions = [];
    for (var i = 0; i < geojson.features.length; i++) {
        var htmlOverlay = document.createElement('div');
        htmlOverlay.id = 'Animal' + i;
        htmlOverlay.style = 'cursor: pointer;';
        htmlOverlay.onclick = clickPoi;
        htmlOverlay.value = i;
        geojson.features[i].properties.class = '动物';
        htmlOverlay.data = geojson.features[i];
        htmlOverlay.className = 'animalContent';
        htmlOverlay.innerHTML = //'<div style="font-size: 5px; color: #fff; text-align: center;">' + geojson.features[i].properties.名称 + '</div>\
        '<img style="position: relative; left: 50%; transform: translate(-16px, 4px); height: 32px; width: 32px" src="img/' + geojson.features[i].properties.thumbnail + '"/>';
        document.body.appendChild(htmlOverlay);

        positions.push(Cesium.Cartographic.fromDegrees(geojson.features[i].geometry.coordinates[0], geojson.features[i].geometry.coordinates[1]));
    }

    promise = Cesium.sampleTerrainMostDetailed(terrainProvider, positions);
    Cesium.when(promise, function (updatedPositions) {
        if (updatedPositions.length > 0) {
            viewer.scene.preRender.addEventListener(function(l, t) {
                for (var i = 0; i < updatedPositions.length; i++) {
                    var htmlOverlay = document.getElementById('Animal' + i); 
                    if (htmlOverlay) {
                        var scratch = new Cesium.Cartesian2();

                        var position = Cesium.Cartesian3.fromDegrees(Cesium.Math.toDegrees(updatedPositions[htmlOverlay.value].longitude), Cesium.Math.toDegrees(updatedPositions[htmlOverlay.value].latitude), updatedPositions[htmlOverlay.value].height * terrainExaggeration);
                        var canvasPosition = viewer.scene.cartesianToCanvasCoordinates(position, scratch);
                        if (Cesium.defined(canvasPosition)) {
                            var x = canvasPosition.x;
                            var y = canvasPosition.y;
                            if (htmlOverlay.style.display === 'none') {
                                htmlOverlay.style.display = 'block';
                                x -= htmlOverlay.offsetWidth / 2;
                                y -= htmlOverlay.offsetHeight + 10;
                                htmlOverlay.style.display = 'none';
                            }
                            else {
                                x -= htmlOverlay.offsetWidth / 2;
                                y -= htmlOverlay.offsetHeight + 10;
                            }

                            if (y > 0 && x > 0) {
                                htmlOverlay.style.display = 'block';
                            }
                            else {
                                htmlOverlay.style.display = 'none';
                            }

                            htmlOverlay.style.top = canvasPosition.y - htmlOverlay.offsetHeight + 10 + 'px';
                            htmlOverlay.style.left = canvasPosition.x - htmlOverlay.offsetWidth / 2 + 'px';
                            htmlOverlay.style.zIndex = Math.ceil(canvasPosition.y);
                        }
                    }
                }
            });
        }
    });
}

function removeAnimalLayer() {
    var elements = document.getElementsByClassName('animalContent');
    if (elements.length > 0) {
        var parentNode = elements[0].parentNode;
        while(elements.length > 0) {
            parentNode.removeChild(elements[0]);
        }
    }
}

var plantGeoJSON = {};

function addPlantLayer(geojson) {
    removePlantLayer();

    plantGeoJSON = geojson;

    var positions = [];
    for (var i = 0; i < geojson.features.length; i++) {
        var htmlOverlay = document.createElement('div');
        htmlOverlay.id = 'Plant' + i;
        htmlOverlay.style = 'cursor: pointer;';
        htmlOverlay.onclick = clickPoi;
        htmlOverlay.value = i;
        geojson.features[i].properties.class = '植物';
        htmlOverlay.data = geojson.features[i];
        htmlOverlay.className = 'plantContent';
        htmlOverlay.innerHTML = //'<div style="font-size: 5px; color: #fff; text-align: center;">' + geojson.features[i].properties.名称 + '</div>\
        '<img style="position: relative; left: 50%; transform: translate(-16px, 4px); height: 32px; width: 32px" src="img/' + geojson.features[i].properties.thumbnail + '"/>';
        document.body.appendChild(htmlOverlay);

        positions.push(Cesium.Cartographic.fromDegrees(geojson.features[i].geometry.coordinates[0], geojson.features[i].geometry.coordinates[1]));
    }

    promise = Cesium.sampleTerrainMostDetailed(terrainProvider, positions);
    Cesium.when(promise, function (updatedPositions) {
        if (updatedPositions.length > 0) {
            viewer.scene.preRender.addEventListener(function(l, t) {
                for (var i = 0; i < updatedPositions.length; i++) {
                    var htmlOverlay = document.getElementById('Plant' + i);
                    if (htmlOverlay) {
                        var scratch = new Cesium.Cartesian2();

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
        }
    });
}

function removePlantLayer() {
    var elements = document.getElementsByClassName('plantContent');
    if (elements.length > 0) {
        var parentNode = elements[0].parentNode;
        while(elements.length > 0) {
            parentNode.removeChild(elements[0]);
        }
    }
}

var naturalScienceGeoJSON = {};

function addNaturalScienceLayer(geojson) {
    removeNaturalScienceLayer();

    naturalScienceGeoJSON = geojson;

    var positions = [];
    for (var i = 0; i < geojson.features.length; i++) {
        var htmlOverlay = document.createElement('div');
        htmlOverlay.id = 'NaturalScience' + i;
        htmlOverlay.onclick = clickEntity;
        htmlOverlay.style = 'cursor: pointer;';
        htmlOverlay.value = i;
        geojson.features[i].properties.class = '自然科普';
        htmlOverlay.data = geojson.features[i];
        htmlOverlay.className = 'naturalScience';
        htmlOverlay.innerHTML = '<div style="font-size: 5px; color: ' + fontColor + '; text-align: center;">' + geojson.features[i].properties.NAME + '</div>\
        <img style="position: relative; left: 50%; transform: translate(-16px, 4px); height: 32px; width: 32px" src="img/icon_zirankepu_layer.png"/>';
        document.body.appendChild(htmlOverlay);

        positions.push(Cesium.Cartographic.fromDegrees(geojson.features[i].geometry.coordinates[0], geojson.features[i].geometry.coordinates[1]));
    }

    promise = Cesium.sampleTerrainMostDetailed(terrainProvider, positions);
    Cesium.when(promise, function (updatedPositions) {
        if (updatedPositions.length > 0) {
            viewer.scene.preRender.addEventListener(function(l, t) {
                for (var i = 0; i < updatedPositions.length; i++) {
                    var htmlOverlay = document.getElementById('NaturalScience' + i);
                    if (htmlOverlay) {
                        var scratch = new Cesium.Cartesian2();

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
        }
    });
}

function removeNaturalScienceLayer() {
    var elements = document.getElementsByClassName('naturalScience');
    if (elements.length > 0) {
        var parentNode = elements[0].parentNode;
        while(elements.length > 0) {
            parentNode.removeChild(elements[0]);
        }
    }
}

var sightseeingGeoJSON = {};

function addSightseeingLayer(geojson) {
    removeSightseeingLayer();

    sightseeingGeoJSON = geojson;

    var positions = [];
    for (var i = 0; i < geojson.features.length; i++) {
        var htmlOverlay = document.createElement('div');
        htmlOverlay.id = 'Sightseeing' + i;
        htmlOverlay.onclick = clickEntity;
        htmlOverlay.style = 'cursor: pointer;';
        htmlOverlay.value = i;
        geojson.features[i].properties.class = '观光旅游';
        htmlOverlay.data = geojson.features[i];
        htmlOverlay.className = 'sightseeing';
        htmlOverlay.innerHTML = '<div style="font-size: 5px; color: ' + fontColor + '; text-align: center;">' + geojson.features[i].properties.NAME + '</div>\
        <img style="position: relative; left: 50%; transform: translate(-16px, 4px); height: 32px; width: 32px" src="img/icon_guanguanglvyou_layer.png"/>';
        document.body.appendChild(htmlOverlay);

        positions.push(Cesium.Cartographic.fromDegrees(geojson.features[i].geometry.coordinates[0], geojson.features[i].geometry.coordinates[1]));
    }

    promise = Cesium.sampleTerrainMostDetailed(terrainProvider, positions);
    Cesium.when(promise, function (updatedPositions) {
        if (updatedPositions.length > 0) {
            viewer.scene.preRender.addEventListener(function(l, t) {
                for (var i = 0; i < updatedPositions.length; i++) {
                    var htmlOverlay = document.getElementById('Sightseeing' + i);
                    if (htmlOverlay) {
                        var scratch = new Cesium.Cartesian2();

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
        }
    });
}

function removeSightseeingLayer() {
    var elements = document.getElementsByClassName('sightseeing');
    if (elements.length > 0) {
        var parentNode = elements[0].parentNode;
        while(elements.length > 0) {
            parentNode.removeChild(elements[0]);
        }
    }
}

var specialTourismGeoJSON = {};

function addSpecialTourismLayer(geojson) {
    removeSpecialTourismLayer();

    specialTourismGeoJSON = geojson;

    var positions = [];
    for (var i = 0; i < geojson.features.length; i++) {
        var htmlOverlay = document.createElement('div');
        htmlOverlay.id = 'SpecialTourism' + i;
        htmlOverlay.onclick = clickEntity;
        htmlOverlay.style = 'cursor: pointer;';
        htmlOverlay.value = i;
        geojson.features[i].properties.class = '专项旅游';
        htmlOverlay.data = geojson.features[i];
        htmlOverlay.className = 'specialTourism';
        htmlOverlay.innerHTML = '<div style="font-size: 5px; color: ' + fontColor + '; text-align: center;">' + geojson.features[i].properties.NAME + '</div>\
        <img style="position: relative; left: 50%; transform: translate(-16px, 4px); height: 32px; width: 32px" src="img/icon_zhuanxianglvyou_layer.png"/>';
        document.body.appendChild(htmlOverlay);

        positions.push(Cesium.Cartographic.fromDegrees(geojson.features[i].geometry.coordinates[0], geojson.features[i].geometry.coordinates[1]));
    }

    promise = Cesium.sampleTerrainMostDetailed(terrainProvider, positions);
    Cesium.when(promise, function (updatedPositions) {
        if (updatedPositions.length > 0) {
            viewer.scene.preRender.addEventListener(function(l, t) {
                for (var i = 0; i < updatedPositions.length; i++) {
                    var htmlOverlay = document.getElementById('SpecialTourism' + i);
                    if (htmlOverlay) {
                        var scratch = new Cesium.Cartesian2();

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
        }
    });
}

function removeSpecialTourismLayer() {
    var elements = document.getElementsByClassName('specialTourism');
    if (elements.length > 0) {
        var parentNode = elements[0].parentNode;
        while(elements.length > 0) {
            parentNode.removeChild(elements[0]);
        }
    }
}

var mountainPeakGeoJSON = {};

function addMountainPeakLayer(geojson) {
    removeMountainPeakLayer();

    mountainPeakGeoJSON = geojson;

    var positions = [];
    for (var i = 0; i < geojson.features.length; i++) {
        var htmlOverlay = document.createElement('div');
        htmlOverlay.id = 'MountainPeak' + i;
        htmlOverlay.onclick = clickEntity;
        htmlOverlay.style = 'cursor: pointer;';
        htmlOverlay.value = i;
        geojson.features[i].properties.class = '山峰';
        htmlOverlay.data = geojson.features[i];
        htmlOverlay.className = 'mountainPeak';
        htmlOverlay.innerHTML = '<div style="font-size: 12px; color: ' + fontColor + '; text-align: center;">' + geojson.features[i].properties.NAME + '</div>';
        document.body.appendChild(htmlOverlay);

        positions.push(Cesium.Cartographic.fromDegrees(geojson.features[i].geometry.coordinates[0], geojson.features[i].geometry.coordinates[1]));
    }

    promise = Cesium.sampleTerrainMostDetailed(terrainProvider, positions);
    Cesium.when(promise, function (updatedPositions) {
        if (updatedPositions.length > 0) {
            viewer.scene.preRender.addEventListener(function(l, t) {
                for (var i = 0; i < updatedPositions.length; i++) {
                    var htmlOverlay = document.getElementById('MountainPeak' + i);
                    if (htmlOverlay) {
                        var scratch = new Cesium.Cartesian2();

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

                            htmlOverlay.style.top = canvasPosition.y - htmlOverlay.offsetHeight + 5 + 'px';
                            htmlOverlay.style.left = canvasPosition.x - htmlOverlay.offsetWidth / 2 + 'px';
                            htmlOverlay.style.zIndex = Math.ceil(canvasPosition.y);
                        }
                    }
                }
            });
        }
    });
}

function removeMountainPeakLayer() {
    var elements = document.getElementsByClassName('mountainPeak');
    if (elements.length > 0) {
        var parentNode = elements[0].parentNode;
        while(elements.length > 0) {
            parentNode.removeChild(elements[0]);
        }
    }
}

var villageGeoJSON = {};

function addVillageLayer(geojson) {
    removeVillageLayer();

    villageGeoJSON = geojson;

    var positions = [];
    for (var i = 0; i < geojson.features.length; i++) {
        var htmlOverlay = document.createElement('div');
        htmlOverlay.id = 'Village' + i;
        htmlOverlay.onclick = clickEntity;
        htmlOverlay.style = 'cursor: pointer;';
        htmlOverlay.value = i;
        geojson.features[i].properties.class = '村庄';
        htmlOverlay.data = geojson.features[i];
        htmlOverlay.className = 'village';
        htmlOverlay.innerHTML = '<div style="font-size: 8px; color: ' + fontColor + '; text-align: center;">' + geojson.features[i].properties.标准地名 + '</div>';
        document.body.appendChild(htmlOverlay);

        positions.push(Cesium.Cartographic.fromDegrees(geojson.features[i].geometry.coordinates[0], geojson.features[i].geometry.coordinates[1]));
    }

    promise = Cesium.sampleTerrainMostDetailed(terrainProvider, positions);
    Cesium.when(promise, function (updatedPositions) {
        if (updatedPositions.length > 0) {
            viewer.scene.preRender.addEventListener(function(l, t) {
                for (var i = 0; i < updatedPositions.length; i++) {
                    var htmlOverlay = document.getElementById('Village' + i);
                    if (htmlOverlay) {
                        var scratch = new Cesium.Cartesian2();

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

                            htmlOverlay.style.top = canvasPosition.y - htmlOverlay.offsetHeight + 5 + 'px';
                            htmlOverlay.style.left = canvasPosition.x - htmlOverlay.offsetWidth / 2 + 'px';
                            htmlOverlay.style.zIndex = Math.ceil(canvasPosition.y);
                        }
                    }
                }
            });
        }
    });
}

function removeVillageLayer() {
    var elements = document.getElementsByClassName('village');
    if (elements.length > 0) {
        var parentNode = elements[0].parentNode;
        while(elements.length > 0) {
            parentNode.removeChild(elements[0]);
        }
    }
}

var handler = new Cesium.ScreenSpaceEventHandler(viewer.scene.canvas);
var mouseOperation = 0;
handler.setInputAction(function(click) { // 点击事件
    viewer.clock.stopTime = viewer.clock.startTime; //viewer.clock.stopTime 赋值为 viewer.clock.startTime 即可停止旋转

    var pickElement = viewer.scene.pick(click.position);
    if (pickElement && pickElement.id){

    }

    mouseOperation = 1;
}, Cesium.ScreenSpaceEventType.LEFT_DOWN);

viewer.scene.camera.moveStart.addEventListener(function(){
    mouseOperation = 2;
});

handler.setInputAction(function(click) {
    if (mouseOperation !== 2) {
        clearPoiDetail();
    }
    mouseOperation = 0;
}, Cesium.ScreenSpaceEventType.LEFT_UP);

viewer.camera.moveEnd.addEventListener(function() { 
    // the camera stopped moving
    var elements = [];
    var animalElements = document.getElementsByClassName('animalContent');
    for (var i = 0; i < animalElements.length; i++) {
        elements.push(animalElements[i]);
    }
    var plantElements = document.getElementsByClassName('plantContent');
    for (var i = 0; i < plantElements.length; i++) {
        elements.push(plantElements[i]);
    }

    for (var i = 0; i < elements.length; i++) {
        for (var j = 0; j < elements.length; j++) {
            if (j === i)
                continue;
            
            
        }
    }
});

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
    var new_x = localPosition_A.x * Math.cos(Cesium.Math.toRadians(angle)) + localPosition_A.y * Math.sin(Cesium.Math.toRadians(angle));
    var new_y = localPosition_A.y * Math.cos(Cesium.Math.toRadians(angle)) - localPosition_A.x * Math.sin(Cesium.Math.toRadians(angle));
    var new_z = localPosition_A.z;
    //最后应用局部坐标到世界坐标的转换矩阵求得旋转后的A点世界坐标
    return Cesium.Matrix4.multiplyByPoint(localToWorld_Matrix, new Cesium.Cartesian3(new_x, new_y, new_z), new Cesium.Cartesian3());
}

function rotateLeftRight(angle) {
    var canvas = viewer.scene.canvas;
    let center_position = viewer.scene.pickPosition(new Cesium.Cartesian2(canvas.width / 2.0, canvas.height / 2.0));

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

function rotateByLeft(angle, callback) {
    var canvas = viewer.scene.canvas;
    let center_position = viewer.scene.pickPosition(new Cesium.Cartesian2(canvas.width / 2.0, canvas.height / 2.0));

    viewer.camera.lookAtTransform(Cesium.Transforms.eastNorthUpToFixedFrame(center_position));

    viewer.camera.rotateLeft(Cesium.Math.toRadians(angle));

    viewer.camera.lookAtTransform(Cesium.Matrix4.IDENTITY);
}

function rotateByRight(angle, callback) {
    var canvas = viewer.scene.canvas;
    let center_position = viewer.scene.pickPosition(new Cesium.Cartesian2(canvas.width / 2.0, canvas.height / 2.0));

    viewer.camera.lookAtTransform(Cesium.Transforms.eastNorthUpToFixedFrame(center_position));

    viewer.camera.rotateRight(Cesium.Math.toRadians(angle));

    viewer.camera.lookAtTransform(Cesium.Matrix4.IDENTITY);
}

function rotateByUp(angle, callback) {
    let cameraPitchAngle = Cesium.Math.toDegrees(viewer.camera.pitch);
    if ((cameraPitchAngle + angle) > 0) {
        return;
    }

    var canvas = viewer.scene.canvas;
    let center_position = viewer.scene.pickPosition(new Cesium.Cartesian2(canvas.width / 2.0, canvas.height / 2.0));

    viewer.camera.lookAtTransform(Cesium.Transforms.eastNorthUpToFixedFrame(center_position));

    viewer.camera.rotateUp(Cesium.Math.toRadians(angle));

    viewer.camera.lookAtTransform(Cesium.Matrix4.IDENTITY);

    if (callback) {
        callback();
    }
}

function rotateByDown(angle, callback) {
    var canvas = viewer.scene.canvas;
    let center_position = viewer.scene.pickPosition(new Cesium.Cartesian2(canvas.width / 2.0, canvas.height / 2.0));

    viewer.camera.lookAtTransform(Cesium.Transforms.eastNorthUpToFixedFrame(center_position));

    viewer.camera.rotateDown(Cesium.Math.toRadians(angle));

    viewer.camera.lookAtTransform(Cesium.Matrix4.IDENTITY);
    
    if (callback) {
        callback();
    }
}

var fontColor = '#fff';

function changeMapMode(mode) {
    var angle = Cesium.Math.toDegrees(viewer.camera.pitch);
    if (Math.round(Math.abs(angle)) === 90) {
        rotateByUp(60);
    }
    else {
        angle += 90;
        rotateByDown(angle);
    }

    if (mode === '2D') {
        viewer.imageryLayers.removeAll();
        viewer.imageryLayers.addImageryProvider(mapboxOutdoorsLayer);

        viewer.terrainProvider = new Cesium.EllipsoidTerrainProvider({});
        terrainExaggeration = 0.0;

        fontColor = '#000';
    }
    else {
        viewer.imageryLayers.removeAll();
        viewer.imageryLayers.addImageryProvider(mapboxLayer);
        viewer.imageryLayers.addImageryProvider(FJSImageryLayer);

        viewer.terrainProvider = terrainProvider;
        terrainExaggeration = 1.0;

        fontColor = '#fff';
    }

    let typeList = ['naturalScience','sightseeing','specialTourism','mountainPeak','village'];
    let funcList = [addNaturalScienceLayer,addSightseeingLayer,addSpecialTourismLayer,addMountainPeakLayer,addVillageLayer];
    let dataList = [naturalScienceGeoJSON,sightseeingGeoJSON,specialTourismGeoJSON,mountainPeakGeoJSON,villageGeoJSON];
    for (let i = 0; i < typeList.length; i++) {
        let elements = document.getElementsByClassName(typeList[i]);
        if (elements.length > 0) {
            funcList[i](dataList[i]);
        }
        // for (let j = 0; j < elements.length; j++) {
        //     elements[j].style.color = fontColor;
        // }
    }
}

document.addEventListener('keydown', function (e) {
    setKey(e);
}, false);

function optMap(opt) {
    if (opt.action === "rotateByLeft") {
        rotateByLeft(opt.data);
    }
    else if (opt.action === "rotateByRight") {
        rotateByRight(opt.data);
    }
    else if (opt.action === "rotateByUp") {
        rotateByUp(opt.data);
    }
    else if (opt.action === "rotateByDown") {
        rotateByDown(opt.data);
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
        if (fontColor === '#000') {
            changeMapMode('3D');
        }
        else {
            changeMapMode('2D');
        }
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
        addRoadBackgroundLayer({ "sroadeast" : "./sroadeast.geojson", "sroadwest" : "./sroadwest.geojson", "ssroad" : "./ssroad.geojson"});
        
        var request1 = new XMLHttpRequest();
        request1.open("get", "./动物.geojson");
        request1.send(null);
        request1.onload = function () {
            if (request1.status == 200) {
                var geojson = JSON.parse(request1.responseText);
                addAnimalLayer(geojson);
            }
        }

        var request2 = new XMLHttpRequest();
        request2.open("get", "./植物.geojson");
        request2.send(null);
        request2.onload = function () {
            if (request2.status == 200) {
                var geojson = JSON.parse(request2.responseText);
                addPlantLayer(geojson);
            }
        }

        var request3 = new XMLHttpRequest();
        request3.open("get", "./自然科普.geojson");
        request3.send(null);
        request3.onload = function () {
            if (request3.status == 200) {
                var geojson = JSON.parse(request3.responseText);
                addNaturalScienceLayer(geojson);
            }
        }

        var request4 = new XMLHttpRequest();
        request4.open("get", "./观光旅游.geojson");
        request4.send(null);
        request4.onload = function () {
            if (request4.status == 200) {
                var geojson = JSON.parse(request4.responseText);
                addSightseeingLayer(geojson);
            }
        }

        var request5 = new XMLHttpRequest();
        request5.open("get", "./专项旅游.geojson");
        request5.send(null);
        request5.onload = function () {
            if (request5.status == 200) {
                var geojson = JSON.parse(request5.responseText);
                addSpecialTourismLayer(geojson);
            }
        }

        var request6 = new XMLHttpRequest();
        request6.open("get", "./山峰.geojson");
        request6.send(null);
        request6.onload = function () {
            if (request6.status == 200) {
                var geojson = JSON.parse(request6.responseText);
                addMountainPeakLayer(geojson);
            }
        }

        var request7 = new XMLHttpRequest();
        request7.open("get", "./村庄.geojson");
        request7.send(null);
        request7.onload = function () {
            if (request7.status == 200) {
                var geojson = JSON.parse(request7.responseText);
                addVillageLayer(geojson);
            }
        }
    } 
    else if (event.keyCode === 68) {
        removeRoadBackgroundLayer();

        removeAnimalLayer();

        removePlantLayer();

        removeNaturalScienceLayer();

        removeSightseeingLayer();

        removeSpecialTourismLayer();

        removeMountainPeakLayer();

        removeVillageLayer();
    }
    else if (event.keyCode === 70) {
        // 飞行预览启动
        flyThroughStart2();//flyThroughStart(200, 45);
    }
    else if (event.keyCode === 32) {
        // 飞行预览暂停
        flyThroughPause2();//flyThroughPause();
    }
    else if (event.keyCode === 83) {
        // 飞行预览停止
        flyThroughStop2();//flyThroughStop();
    }
    else if (event.keyCode === 84) {
        // var positions = [];
        // var request1 = new XMLHttpRequest();
        // request1.open("get", "./route.geojson");
        // request1.send(null);
        // request1.onload = function () {
        //     if (request1.status == 200) {
        //         var geojson = JSON.parse(request1.responseText);
        //         for (var i = 0; i < geojson.features[0].geometry.coordinates.length; i++) {
        //             var coordinate = geojson.features[0].geometry.coordinates[i];
        //             var item = {};
        //             item.longitude = coordinate[0];
        //             item.latitude = coordinate[1];
        //             item.height = coordinate[2];
        //             positions.push(item);
        //         }
        //         updateUserTour(positions);
        //     }
        // }
        updatePoiLocation({"type":"FeatureCollection","features":[{"type":"Feature","geometry":{"type":"Point","coordinates":[108.6566137,27.9127753,1932.822074053631]},"properties":{"名称":"万米睡佛","thumbnail":"http://tiles.pano.vizen.cn/32C820DA834943B095E52C3F0D402ADE/sphere/thumb.jpg"}}]});
        //flyTo({"height":12000,"latitude":27.8601391146,"longitude":108.7107853492});
    }

    if (event.ctrlKey){
        if (event.keyCode === 39) {  // right arrow
            rotateByLeft(15);
        } else if (event.keyCode === 37) {  // left arrow
            rotateByRight(15);
        } else if (event.keyCode === 40) {  // up arrow
            rotateByUp(15);
        } else if (event.keyCode === 38) {  // down arrow
            rotateByDown(15);
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

    clearPoiDetail();

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

// var options = {};
// function resetView() {
//     recenter();
// }
// // 用于在使用重置导航重置地图视图时设置默认视图控制。接受的值是Cesium.Cartographic 和 Cesium.Rectangle。
// options.defaultResetView = resetView;
// // 用于启用或禁用罗盘。true是启用罗盘，false是禁用罗盘。默认值为true。如果将选项设置为false，则罗盘将不会添加到地图中。
// options.enableCompass = true;
// // 用于启用或禁用缩放控件。true是启用，false是禁用。默认值为true。如果将选项设置为false，则缩放控件将不会添加到地图中。
// options.enableZoomControls = true;
// // 用于启用或禁用距离图例。true是启用，false是禁用。默认值为true。如果将选项设置为false，距离图例将不会添加到地图中。
// options.enableDistanceLegend = true;
// // 用于启用或禁用指南针外环。true是启用，false是禁用。默认值为true。如果将选项设置为false，则该环将可见但无效。
// options.enableCompassOuterRing = true;

// CesiumNavigation.umd(viewer, options);

viewer.scene.postRender.addEventListener(function () {
    var cameraParam = {
        position : {
            longitude : Cesium.Math.toDegrees(viewer.camera.positionCartographic.longitude),
            latitude : Cesium.Math.toDegrees(viewer.camera.positionCartographic.latitude),
            height : viewer.camera.positionCartographic.height
        },
        heading : Cesium.Math.toDegrees(viewer.camera.heading),
        pitch : Cesium.Math.toDegrees(viewer.camera.pitch),
        roll : viewer.camera.roll
    };
    if (window.Android) {
        window.Android.putCameraParam(JSON.stringify(cameraParam));
    }
    if (typeof compass === 'function') {
        compass(cameraParam);
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
    viewer.camera.lookAtTransform(Cesium.Matrix4.IDENTITY);
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
    viewer.camera.lookAtTransform(Cesium.Matrix4.IDENTITY);

    if (userTour) {
        viewer.flyTo(userTour);
    }
    if (window.Android) {
        window.Android.flyThroughStoped();
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


var flyThroughState = 0;

function flyThroughPause2() {
    if (flyThroughState !== 1) {
        return;
    }

    flyThroughState = 2;

    viewer.clock.stopTime = viewer.clock.startTime;

    if (preUpdateListener) {
        viewer.scene.preUpdate.removeEventListener(preUpdateListener);
    }
    viewer.camera.lookAtTransform(Cesium.Matrix4.IDENTITY);
}

function flyThroughStop2() {
    if (flyThroughState === 0) {
        return;
    }

    flyThroughState = 0;

    viewer.clock.stopTime = viewer.clock.startTime;
    var element = document.getElementById('flyThrough');
    if (element) {
        element.parentNode.removeChild(element);
    }
    if (planeModelLoaded) {
        viewer.scene.primitives.remove(planePrimitive);
        planeModelLoaded = false;
    }
    if (preUpdateListener) {
        viewer.scene.preUpdate.removeEventListener(preUpdateListener);
    }
    viewer.camera.lookAtTransform(Cesium.Matrix4.IDENTITY);

    if (userTour) {
        viewer.flyTo(userTour);
    }
    if (window.Android) {
        window.Android.flyThroughStoped();
    }
    if (typeof flyThroughStoped === 'function') {
        flyThroughStoped();
    }
}

function flyThroughStart2() {
    if (flyThroughState === 2) {
        flyThroughState = 1;
        viewer.scene.preUpdate.addEventListener(preUpdateListener);
        return;
    }

    if (!userTour || flyThroughState === 1) {
        return;
    }

    flyThroughState = 1;

    var cartoPositions = [];
    for (var i = 0; i < userTourPostitions.length; i++) {
        cartoPositions.push(Cesium.Cartographic.fromDegrees(userTourPostitions[i].longitude, userTourPostitions[i].latitude));
    }
    promise = Cesium.sampleTerrainMostDetailed(terrainProvider, cartoPositions);
    Cesium.when(promise, function (updatedPositions) {
        if (updatedPositions.length > 0) {
            for (var i = 0; i < updatedPositions.length; i++) {
                userTourPostitions[i].height = updatedPositions[i].height * terrainExaggeration;
            }
        }

        var simplifiedPositions = simplify(userTourPostitions, 0.001, false);

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
        for (var i = 0; i < userTourPostitions.length; i++) {
            roamingPostions.push(userTourPostitions[i].longitude);
            roamingPostions.push(userTourPostitions[i].latitude);
            roamingPostions.push(userTourPostitions[i].height);
        }
        var hPitches = [];
        var distanceSum = 0.0;
        for (var i = 3, j = 0, k = 0; i < roamingPostions.length; i += 3) {
            var origin = new Cesium.Cartesian3.fromDegrees(roamingPostions[i-3], roamingPostions[i-2], roamingPostions[i-1]);
            var target = new Cesium.Cartesian3.fromDegrees(roamingPostions[i+0], roamingPostions[i+1], roamingPostions[i+2]);
            var hPitch = getHeadingPitch(origin, target);

            hPitch.transportation = userTourPostitions[k++].transportation;

            distanceSum += hPitch.distance;
            hPitch.distance = distanceSum;

            var simplifiedOrigin = new Cesium.Cartesian3.fromDegrees(simplifiedPositions[j].longitude, simplifiedPositions[j].latitude, simplifiedPositions[j].height);
            var simplifiedTarget = new Cesium.Cartesian3.fromDegrees(simplifiedPositions[j+1].longitude, simplifiedPositions[j+1].latitude, simplifiedPositions[j+1].height);
            var simplifiedHPitch = getHeadingPitch(simplifiedOrigin, simplifiedTarget);

            hPitch.simplifiedHPitch = simplifiedHPitch;

            hPitches.push(hPitch);

            if (simplifiedPositions[j+1].longitude === roamingPostions[i-3] && simplifiedPositions[j+1].latitude === roamingPostions[i-2]) {
                j++;
            }
        }

        var origin = new Cesium.Cartesian3.fromDegrees(userTourPostitions[0].longitude, userTourPostitions[0].latitude, userTourPostitions[0].height);
        var target = new Cesium.Cartesian3.fromDegrees(userTourPostitions[1].longitude, userTourPostitions[1].latitude, userTourPostitions[1].height);
        var hPitch = getHeadingPitch(origin, target);

        var hpRoll = new Cesium.HeadingPitchRoll();
        hpRoll.heading = hPitch.heading;
        hpRoll.pitch = hPitch.pitch;

        var hpRange = new Cesium.HeadingPitchRange();
        var speed = 10 * 3;
        
        var position = Cesium.Cartesian3.fromDegrees(userTourPostitions[0].longitude, userTourPostitions[0].latitude, userTourPostitions[0].height);
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

            planeModelLoaded = true;
        });

        var distance = 0.0;
        var currentIndex = 0;
        var currentPosition = new Cesium.Cartesian3.fromDegrees(userTourPostitions[0].longitude, userTourPostitions[0].latitude, userTourPostitions[0].height);
        var lastPosition = new Cesium.Cartesian3.fromDegrees(userTourPostitions[userTourPostitions.length - 1].longitude, userTourPostitions[userTourPostitions.length - 1].latitude, userTourPostitions[userTourPostitions.length - 1].height);
        var simplifiedHPitch = getHeadingPitch(currentPosition, lastPosition);
        var heading = simplifiedHPitch.heading;
        var pitch = Cesium.Math.toRadians(-30.0);
        var range = 4000.0;
        viewer.camera.lookAt(currentPosition, new Cesium.HeadingPitchRange(heading, pitch, range));
        
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
                    if (typeof transportationCB === 'function') {
                        transportationCB(hPitches[i].transportation)
                    }
    
                    if (window.Android) {
                        window.Android.transportationCB(hPitches[i].transportation )
                    }

                    break;
                }
            }
            if (i > currentIndex && i < hPitches.length) {
                hpRoll.heading = hPitches[i].heading;
                hpRoll.pitch = hPitches[i].pitch;

                simplifiedHPitch = hPitches[i].simplifiedHPitch;

                currentIndex = i;
            }
            
            // Zoom to model
            Cesium.Matrix4.multiplyByPoint(planePrimitive.modelMatrix, planePrimitive.boundingSphere.center, center);

            if (i >= hPitches.length) {
                flyThroughStop2();
            }
        }

        viewer.scene.preUpdate.addEventListener(preUpdateListener);

        //
        var element = document.getElementById('flyThrough');
        if (element) {
            element.parentNode.removeChild(element);
        }

        var htmlFlyThroughOverlay = document.createElement('img');
        htmlFlyThroughOverlay.id = 'flyThrough';
        htmlFlyThroughOverlay.style = 'position: absolute; top: 177.697px; left: 258.29px; width: 32px; pointer-events: none;';
        htmlFlyThroughOverlay.src = 'img/mylocation2.png';
        document.body.appendChild(htmlFlyThroughOverlay);

        promise = Cesium.sampleTerrainMostDetailed(terrainProvider, [currentPosition]);
        Cesium.when(promise, function (updatedPositions) {
            if (updatedPositions.length > 0) {
                viewer.scene.preRender.addEventListener(function() {
                    var htmlFlyThroughOverlay = document.getElementById('flyThrough');
                    if (htmlFlyThroughOverlay) {
                        var scratch = new Cesium.Cartesian2();

                        viewer.camera.lookAt(currentPosition, new Cesium.HeadingPitchRange(simplifiedHPitch.heading, pitch, range));

                        var canvasPosition = viewer.scene.cartesianToCanvasCoordinates(currentPosition, scratch);
                        if (Cesium.defined(canvasPosition)) {
                            htmlFlyThroughOverlay.style.top = canvasPosition.y - htmlFlyThroughOverlay.offsetHeight / 2 + 'px';
                            htmlFlyThroughOverlay.style.left = canvasPosition.x - htmlFlyThroughOverlay.offsetWidth / 2 + 'px';
                        }
                    }
                });
            }
        });
    });
}