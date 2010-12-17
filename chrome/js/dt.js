// dt.js

$(document).ready(function() {
	// create the map
	var options = {
		zoom: 11,
		mapTypeId: google.maps.MapTypeId.ROADMAP
	};
	var map = new google.maps.Map(document.getElementById('map'), options);
	
	// get the cart status
	$.getJSON('http://carte.districttaco.com/status.json', function(data) {
		// center on the restaurant
		var address = '5723 N. Lee Highway, Arlington, VA 22207';
		var geocoder = new google.maps.Geocoder();
		geocoder.geocode({'address': address}, function (results, status) {
			if (status == google.maps.GeocoderStatus.OK) {
				map.setCenter(results[0].geometry.location);
				var infowindow = new google.maps.InfoWindow({
					content: '<b>District Taco Restaurant</b><br />5723 N. Lee Highway<br />Arlington, VA 22207'
				});
				var marker = new google.maps.Marker({
					map: map,
					position: results[0].geometry.location,
					icon: '/images/icon_48.png'
				});
				google.maps.event.addListener(marker, 'click', function() {
					infowindow.open(map, marker);
				});
			}
		});
		$.each(data['statuses'], function() {
			var body = this['body'];
			var location = this['location'];
			if (location['latitude'] != 0 && location['longitude'] != 0) {
			}
		});
		return false;
	});
});
