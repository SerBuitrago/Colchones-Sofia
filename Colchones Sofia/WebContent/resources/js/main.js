
/*******************************************************************************
 * MENU LEFT
 ******************************************************************************/
$(".sidebar-dropdown > a").click(function() {
	$(".sidebar-submenu").slideUp(200);
	if ($(this).parent().hasClass("active")) {
		$(".sidebar-dropdown").removeClass("active");
		$(this).parent().removeClass("active");
	} else {
		$(".sidebar-dropdown").removeClass("active");
		$(this).next(".sidebar-submenu").slideDown(200);
		$(this).parent().addClass("active");
	}
});

let CLOSE_SIDEBAR_NAVBAR_LEFT= [...document.getElementsByClassName("close-sidebar")];
if(CLOSE_SIDEBAR_NAVBAR_LEFT!= null && CLOSE_SIDEBAR_NAVBAR_LEFT.length >0){
	CLOSE_SIDEBAR_NAVBAR_LEFT.forEach((e,i)=>{
		e.addEventListener("click", (ev) => {
			$(".sofia-page-structure").addClass("sofia-page-structure__other"); 
			$(".page-wrapper").removeClass("toggled"); 
        });
	});
}

let SHOW_SIDEBAR_NAVBAR_LEFT= [...document.getElementsByClassName("show-sidebar")];
if(SHOW_SIDEBAR_NAVBAR_LEFT!= null && SHOW_SIDEBAR_NAVBAR_LEFT.length >0){
	SHOW_SIDEBAR_NAVBAR_LEFT.forEach((e,i)=>{
		e.addEventListener("click", (ev) => {
			$(".sofia-page-structure").removeClass("sofia-page-structure__other"); 
			$(".page-wrapper").addClass("toggled");
        });
	});
}


/*******************************************************************************
 * EFFECT TEXT
 ******************************************************************************/
/**
 * 
 * @returns
 */
$(document).ready(function () {
	let test= [...document.getElementsByClassName("sofia-container-text-image__h1")];
	if(test.length > 0){
	    var mouseX, mouseY;
	    var ww = $(window).width();
	    var wh = $(window).height();
	    var traX, traY;
	    $(document).mousemove(function (e) {
	        mouseX = e.pageX;
	        mouseY = e.pageY;
	        traX = ((14 * mouseX) / 600) + 50;
	        traY = ((14 * mouseY) / 600) + 50;
	        console.log(`Posicion: (${traX}, ${traY})`);
	        $(".sofia-container-text-image__h1").css({
	            "background-position": traX + "%" + traY + "%"
	        });
	    });
	}else{
		console.log("404");
	}
});

/*******************************************************************************
 * LOGIN
 ******************************************************************************/

/**
 * 
 * @returns
 */
function viewLoginForgot(){
	let login= [...document.getElementsByClassName("sofia-view-login__container-login")];
	let forgot= [...document.getElementsByClassName("sofia-view-login__container-forgot")];
	if(login.length > 0 && forgot.length > 0 ){
		login= login[0];
		forgot= forgot[0];
		login.classList.add("sofia-view-login__container-hidden");
		login.classList.remove("sofia-view-login__container-visible");
		forgot.classList.remove("sofia-view-login__container-hidden");
		forgot.classList.add("sofia-view-login__container-visible");
	}
}

/**
 * 
 * @returns
 */
function viewLoginLogin(){
	let login= [...document.getElementsByClassName("sofia-view-login__container-login")];
	let forgot= [...document.getElementsByClassName("sofia-view-login__container-forgot")];
	if(login.length > 0 && forgot.length > 0 ){
		login= login[0];
		forgot= forgot[0];
		login.classList.remove("sofia-view-login__container-hidden");
		login.classList.add("sofia-view-login__container-visible");
		forgot.classList.add("sofia-view-login__container-hidden");
		forgot.classList.remove("sofia-view-login__container-visible");
	}
}