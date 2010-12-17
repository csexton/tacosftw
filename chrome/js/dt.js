// dt.js

$(document).ready(function() {
	// create the map
	var options = {
		zoom: 6,
		mapTypeId: google.maps.MapTypeId.ROADMAP
	};
	var map = new google.maps.Map(document.getElementById('map'), options);
	
	// get the cart status
	$.getJSON('http://carte.districttaco.com/status.json', function(data) {
		$.each(data['statuses'], function() {
			var body = this['body'];
			var location = this['location'];
			if (location['latitude'] != 0 && location['longitude'] != 0) {
//				map.setCenter(new google.maps.LatLng(location['latitude'], location['longitude']));
//				alert('hi');
			}
		});
	});
});
