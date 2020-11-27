$(document).ready(function(){
	$(".signup").hide();
  	$("#hidelogin").click(function(){
    	$(".login").hide();
    	$(".signup").show();
  	});
	$("#hidesignup").click(function(){
    	$(".login").show();
    	$(".signup").hide();
  	});
	 $("#signup_id").validate(
	 );
	$("#conf_password").keyup(isPasswordMatch);
    
});

function isPasswordMatch() {
    var password = $("#password").val();
    var conf_password = $("#conf_password").val();
    var bt = document.getElementById('btSubmit');

    if (password != conf_password) {
    	$("#divCheckPassword").html("Passwords do not match!");
    	bt.disabled = true;
    }
    else {
    	$("#divCheckPassword").html("Passwords match.");
    	bt.disabled = false;
    }
}