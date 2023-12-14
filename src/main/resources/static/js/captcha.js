function refreshCaptcha() {
var contextPath = $('#contextPath').val();
	$.ajax({

		type : 'GET',
		url : contextPath+'captcha',
		success : function(data) {
			
			$('#imageidcap').attr('src', contextPath+'captcha');
		}
	});
}


