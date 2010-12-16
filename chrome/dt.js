// dt.js

$(document).ready(function() {
	$.getJSON('http://carte.districttaco.com/status.json', function(data) {
		$.each(data['statuses'], function() {
			$('#status').text(this['body']);
		})
	});
});
