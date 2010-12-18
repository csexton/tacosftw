// dt.js

$(document).ready(function() {
	// create the map
	var options = {
		zoom: 12,
		mapTypeId: google.maps.MapTypeId.ROADMAP
	};
	var map = new google.maps.Map(document.getElementById('map'), options);
	
	// this is our map bounds
	var latLngBounds = new google.maps.LatLngBounds();
	
	// add a marker for the restaurant
	var address = '5723 Lee Highway, Arlington, VA 22207';
	var geocoder = new google.maps.Geocoder();
	geocoder.geocode({'address': address}, function (results, status) {
		if (status == google.maps.GeocoderStatus.OK) {
			latLngBounds.extend(results[0].geometry.location);
			map.setCenter(results[0].geometry.location);
			var html = '<div><b>District Taco Restaurant</b></div><div>5723 Lee Highway</div><div>Arlington, VA 22207</div>';
			var infowindow = new google.maps.InfoWindow({
				content: html
			});
			var marker = new google.maps.Marker({
				map: map,
				position: results[0].geometry.location,
				icon: new google.maps.MarkerImage('/images/marker.png', new google.maps.Size(48, 31), new google.maps.Point(0, 0), new google.maps.Point(24, 15))
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
				var html = '<div><b>' + location['description'] + '</b></div><div>' + this['body'] + '</div>';
				var infowindow = new google.maps.InfoWindow({
					content:  html
				});
				var marker = new google.maps.Marker({
					map: map,
					position: location,
					icon: new google.maps.MarkerImage('/images/sombrero.png', new google.maps.Size(32, 20), new google.maps.Point(0, 0), new google.maps.Point(16, 10))
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
	});
});
