// dt.js

$(document).ready(function() {
	// create the map
	var options = {
		zoom: 11,
		mapTypeId: google.maps.MapTypeId.ROADMAP
	};
	var map = new google.maps.Map(document.getElementById('map'), options);
	
	// this is our map bounds
	var latLngBounds = new google.maps.LatLngBounds();
	
	// see if the browser supports location
	var userLocation = null;
	if (navigator.geolocation) {
		navigator.geolocation.getCurrentPosition(function(position) {
			userLocation = new google.maps.LatLng(position.coords.latitude, position.coords.longitude);
			latLngBounds.extend(userLocation);
			var marker = new google.maps.Marker({
				map: map,
				position: userLocation,
				animation: google.maps.Animation.DROP
			})
		});
	}
	
	// add a marker for the restaurant
	var address = '5723 Lee Highway, Arlington, VA 22207';
	var geocoder = new google.maps.Geocoder();
	geocoder.geocode({'address': address}, function (results, status) {
		if (status == google.maps.GeocoderStatus.OK) {
			latLngBounds.extend(results[0].geometry.location);
			var infowindow = new google.maps.InfoWindow({
				content: '<b>District Taco Restaurant</b><br />5723 Lee Highway<br />Arlington, VA 22207'
			});
			var marker = new google.maps.Marker({
				map: map,
				position: results[0].geometry.location,
				icon: new google.maps.MarkerImage('/images/icon_48.png', new google.maps.Size(48, 48), new google.maps.Point(0, 0), new google.maps.Point(24, 24))
			});
			google.maps.event.addListener(marker, 'click', function() {
				infowindow.open(map, marker);
			});
		}
	});
	
	// get the cart status
	$.getJSON('http://carte.districttaco.com/status.json', function(data) {
		$.each(data['statuses'], function() {
			var cartLocation = this['location'];
			if (cartLocation['latitude'] != 0 && cartLocation['longitude'] != 0) {
				var location = new google.maps.LatLng(cartLocation['latitude'], cartLocation['longitude']);
				latLngBounds.extend(location);
				var infowindow = new google.maps.InfoWindow({
					content: '<b>' + location['description'] + '</b><br />' + this['body']
				});
				var marker = new google.maps.Marker({
					map: map,
					position: location,
					icon: new google.maps.MarkerImage('/images/icon_48.png', new google.maps.Size(48, 48), new google.maps.Point(0, 0), new google.maps.Point(24, 24))
				});
				google.maps.event.addListener(marker, 'click', function() {
					infowindow.open(map, marker);
				});
			}
		});
		
		// now, calculate a point on the map that is the center of all the markers, center the map and make it show all points
		map.setCenter(latLngBounds.getCenter());
		map.fitBounds(latLngBounds);
		
		// make sure zoom area is not too small
		if (map.getZoom() > 12)
			map.setZoom(12);
			
		return false;
	});
});
