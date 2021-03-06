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
				icon: new google.maps.MarkerImage('/images/store_marker_48.png', new google.maps.Size(48, 48), new google.maps.Point(0, 0), new google.maps.Point(23, 47))
			});
			google.maps.event.addListener(marker, 'click', function() {
				infowindow.open(map, marker);
			});
		}
	});
	
	// get the cart status
	$.getJSON('http://carte.districttaco.com/status.json', function(data) {
		var haveStatus = false;
		$.each(data['statuses'], function() {
			var cartLocation = this['location'];
			if (cartLocation['latitude'] != 0 && cartLocation['longitude'] != 0) {
				haveStatus = true;
				var location = new google.maps.LatLng(cartLocation['latitude'], cartLocation['longitude']);
				latLngBounds.extend(location);
				var html = '<div><b>' + cartLocation['name'] + '</b></div><div>' + cartLocation['description'] + '</div><div><i>' + this['body'] + '</i></div>';
				var infowindow = new google.maps.InfoWindow({
					content:  html
				});
				var marker = new google.maps.Marker({
					map: map,
					position: location,
					icon: new google.maps.MarkerImage('/images/cart_marker_48.png', new google.maps.Size(48, 48), new google.maps.Point(0, 0), new google.maps.Point(23, 47))
				});
				google.maps.event.addListener(marker, 'click', function() {
					infowindow.open(map, marker);
				});
			}
		});

		// make sure the icon is up-to-date
		if (haveStatus)
			chrome.browserAction.setIcon({ path: '/images/icon.png' });
		else
			chrome.browserAction.setIcon({ path: '/images/bw19.png' });

		// now, calculate a point on the map that is the center of all the markers, center the map and make it show all points
		map.setCenter(latLngBounds.getCenter());
		map.fitBounds(latLngBounds);

		// I found the problem being it was too zoomed out to be useful, and the
		// origin al logic tried to check to make sure it wasn't too small -- so
		// I think a resonable default would be force it to 11
		map.setZoom(11);
	});
});
