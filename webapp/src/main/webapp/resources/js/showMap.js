
function initMap() {

    var options  = {
        zoom:8,
        center: {lat : ${place.latitude}, long : ${place.longitude}}
    };

    var map = new google.maps.Map(document.getElementById('map"${place.id")'), options);

    var marker = new google.maps.Marker({
        position: {lat:${place.latitude} , lng:${place.longitude} },
        map: map
    })


}