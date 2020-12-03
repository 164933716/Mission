# Preparation

This chapter introduces some preparations before using AMap JS API to develop map applications.

## Register an account and apply for a Key


1. First, [register developer account](https://lbs.amap.com/dev/id/newuser), become a developer of AutoNavi Open Platform

2. After logging in, enter the "Application Management" page "Create New Application"

3. For the application [Add Key](https://lbs.amap.com/dev/key/app), please select "Web Client (JSAPI)" for "Service Platform"


## Prepare the page
1. Add the entry script tag of JS API on the page, and replace the "key value you applied for" with the key you just applied for;
```javascript
<script type="text/javascript" src="https://webapi.amap.com/maps?v=1.4.15&key=The key value you applied for"></script>
```
2. Add the `div` tag as the map container, and specify the `id` attribute for the `div`;
```javascript
<div id="container"></div>
```
3. Specify the height and width of the map container;
```javascript
#container {width:300px; height: 180px;}
```
4. When developing the mobile terminal, please add the `viewport` setting in the `head` to achieve the best drawing performance;
```javascript
<meta name="viewport" content="initial-scale=1.0, user-scalable=no">
```
5. After completing the above preparations, you can start the development work, check the quick start.

## Load JS API asynchronously
The previous section explained the most basic way to load the JS API synchronously. If you need to load it asynchronously, such as through `appendChild`, or through asynchronous methods such as `require`, you need to prepare a global callback first. The function is used as the callback function of JS API asynchronous loading, and its function name is added as the `callback` parameter after the reference address of the JS API. At this time, please note that the callback function should be declared before the script request is issued. The asynchronous loading method can only start to call the related interface of JSAPI after the callback. such as
```javascript
window.onLoad = function(){
var map = new AMap.Map('container');
}
var url ='https://webapi.amap.com/maps?v=1.4.15&key=The key value you applied for&callback=onLoad';
var jsapi = doc.createElement('script');
jsapi.charset ='utf-8';
jsapi.src = url;
document.head.appendChild(jsapi);
```
For more loading methods, please see "[JS API loading](https://lbs.amap.com/api/javascript-api/guide/abc/load)"

## HTTPS support

JSAPI 1.3 fully supports the `HTTPS` protocol. If you need to use a secure protocol, replace the `http` in the URL of the entry script of the JS API with `https`. With Chrome, iOS, etc. successively restricting location requests from non-secure domains, we recommend that you upgrade your website to the `HTTPS` protocol and use the `HTTPS` protocol to load the JS API.
To
```javascript
<script type="text/javascript" src="https://webapi.amap.com/maps?v=1.4.15&key=The key value you applied for"></script>
```
## Related documents

[Get started quickly](https://lbs.amap.com/api/javascript-api/guide/abc/quickstart?from=api-js_api-guide-abc-prepare)

[The composition of the map and the explanation of terms](https://lbs.amap.com/api/javascript-api/guide/abc/components?from=api-js_api-guide-abc-prepare)

[Basic class description](https://lbs.amap.com/api/javascript-api/guide/abc/basetype?from=api-js_api-guide-abc-prepare)

[Map JS API example](https://lbs.amap.com/demo-center/js-api?from=api-js_api-guide-abc-prepare)
