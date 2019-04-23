<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%--
TODO: CAMBIAR HREF/ACTION A C:URL
Autocomplete -> Cargar datos a la db (place id) -> Inverse geocoding -> place data
var a = autocomplete.getPlace().geometry.location
a.lng
a.lat
--%>

<html>
<head>
    <title>Create Trip</title>
    <link type="text/css" rel="stylesheet" href="/resources/css/ac-map.css">
</head>
<body>
    <a href="/" >back to index</a>
    <h3>Create Trip</h3>
            <div class="pac-card" id="pac-card">
                <div>
                    <div id="title">
                        Search
                    </div>
                   <div id="type-selector" class="pac-controls">
                        <input type="radio" name="type" id="changetype-all" checked="checked">
                        <label for="changetype-all">All</label>

                        <input type="radio" name="type" id="changetype-establishment">
                        <label for="changetype-establishment">Establishments</label>

                        <input type="radio" name="type" id="changetype-address">
                        <label for="changetype-address">Addresses</label>

                        <input type="radio" name="type" id="changetype-geocode">
                        <label for="changetype-geocode">Geocodes</label>
                    </div>
                    <div id="strict-bounds-selector" class="pac-controls">
                        <input type="checkbox" id="use-strict-bounds" value="">
                        <label for="use-strict-bounds">Strict Bounds</label>
                    </div>
                </div>
                <div id="pac-container">
                    <input id="pac-input" type="text"
                           placeholder="Enter a location">
                </div>
            </div>
            <div id="map"></div>
            <div id="infowindow-content">
                <img src="" width="16" height="16" id="place-icon">
                <span id="place-name"  class="title"></span><br>
                <span id="place-address"></span>
            </div>

    <script src="https://maps.googleapis.com/maps/api/js?key=AIzaSyBsr7zi-a1oz7nNE4ZmsQBiFXkD3hEUHdM&libraries=places&callback=initMap"
            async defer></script>
    <script type="text/javascript" href="resources/js/ac-map.js"></script>
</body>
</html>
