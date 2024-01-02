const $menu = $('#menuLateral');
obtenerMenuFromSession();
function obtenerMenuFromSession(){
	var respuesta = JSON.parse(sessionStorage.getItem("_menu"));
	if(respuesta){
		for(var i in respuesta){
			for(var k  in respuesta[i].subMenu){
				var id = respuesta[i].subMenu[k].idMenuSuperior+ "_"+ respuesta[i].subMenu[k].idMenu;
				//console.log("id", id);
				var element = $("span#"+id+".menucantidad");
				element.attr('title', respuesta[i].subMenu[k].cantidad + " correspondencias");
    			element.attr("data-original-title", element.attr('title')).attr('title', '');
    			if(respuesta[i].subMenu[k].cantidad > 99){
    				element.text("99+")
    			} else {
    				element.text(respuesta[i].subMenu[k].cantidad);
    			}
			}   			
			/*var element = $("span#"+respuesta[i].url+".menucantidad");
			element.attr('title', respuesta[i].cantidad + " correspondencias");
			element.attr("data-original-title", element.attr('title')).attr('title', '');
			if(respuesta[i].cantidad > 99){
				element.text("99+")
			} else {
				element.text(respuesta[i].cantidad);
			}	*/
		}
	}
}

$(".sideMenu").click(function(){
    $("#menuLateral").removeClass("oculto");
    //inicio Comentado por el ticket 9000004710
    /*$("#menuLoading").show();
    obtenercantidadCorrespondencias()
    	.then(function(respuesta){
    		sessionStorage.setItem("_menu",JSON.stringify(respuesta));
    		for(var i in respuesta){
    			for(var k  in respuesta[i].subMenu){
    				var id = respuesta[i].subMenu[k].idMenuSuperior+ "_"+ respuesta[i].subMenu[k].idMenu;
    				console.log("id", id);
    				var element = $("span#"+id+".menucantidad");
    				element.attr('title', respuesta[i].subMenu[k].cantidad + " correspondencias");
        			element.attr("data-original-title", element.attr('title')).attr('title', '');
        			if(respuesta[i].subMenu[k].cantidad > 99){
        				element.text("99+")
        			} else {
        				element.text(respuesta[i].subMenu[k].cantidad);
        			}
    			}   			
    			/*inicio comentario var element = $("span#"+respuesta[i].url+".menucantidad");
    			element.attr('title', respuesta[i].cantidad + " correspondencias");
    			element.attr("data-original-title", element.attr('title')).attr('title', '');
    			if(respuesta[i].cantidad > 99){
    				element.text("99+")
    			} else {
    				element.text(respuesta[i].cantidad);
    			}	fin comentario
			}
    		eventoToolTip();
    		$("#menuLoading").hide();
    	}).catch(function(error){
    		if(error.status == 403){
    			location.reload();
    		}else {
    			$("#menuLoading").hide();
    		}
    		
    	});*/
    //fin Comentado por el ticket 9000004710
});

$(".closeMenu").click(function(){
    $("#menuLateral").addClass("oculto");
});
$(".menuItem").click(function(e){
	var _url = e.currentTarget.dataset.url;
	if(_url != undefined){
		$("#modalProcesando").modal('show');
		var url = window.location.origin;
		var path = window.location.pathname.trim().split("/");
		for(var i = 1; i <= path.length ; i++){ 
			if(path[i] == 'app'){
				break;
			}else{
				url = url + "/" + path[i];
			}
		}
		console.log("URL:" + _url);
		url = url + '/' + _url;
		window.location.replace(url);
	}else{
		var t = $(this);
		var id = t.attr('data-id');
		var idPadre = t.attr('data-padre');
		console.log("Id ocultar hijo:" + id);
		console.log("Id ocultar padre:" + idPadre);
		ocultarAcordeon(id);
	}
});

$(".menuItem").hover(function(){
	eventoToolTip();
});

$(document).mouseup(function (e) {
	/*if(!$menu.is(e.target)){
		console.log("Dentro");
		//$("#menuLateral").addClass("oculto");
	}else{
		console.log("Fuera");
	}*/
	var container = $("#menuLateral");
	var class_ = container.attr('class');
	if(class_ != undefined){
		var numClass = class_.split('oculto');
		if(numClass.length<2){
			// if the target of the click isn't the container nor a descendant of the container
		    if (!container.is(e.target) && container.has(e.target).length === 0) 
		    {
		        //container.hide();
		        container.addClass("oculto");
		    }
		}
	}
});

function obtenercantidadCorrespondencias(){
	var url = window.location.origin;
	var path = window.location.pathname.trim().split("/");
	for(var i = 1; i <= path.length ; i++){ 
		if(path[i] == 'app'){
			break;
		}else{
			url = url + "/" + path[i];
		}
	}
	return $.ajax({
			type	:	'GET',
			url		: 	url + '/app/correspondencias/recuperar-menu',
			cache	:	false,
			beforeSend: function(xhr) {
		        xhr.setRequestHeader("Accept", "application/json");
		        xhr.setRequestHeader("Content-Type", "application/json");
		    }
		});
} 

function eventoToolTip(){
	if(navigator.userAgent.match(/Android/i)
			 || navigator.userAgent.match(/webOS/i)
			 || navigator.userAgent.match(/iPhone/i)
			 || navigator.userAgent.match(/iPad/i)
			 || navigator.userAgent.match(/iPod/i)
			 || navigator.userAgent.match(/BlackBerry/i)
			 || navigator.userAgent.match(/Windows Phone/i)){
		
	}else {
		$('[data-toggle="tooltip"]').tooltip('update');
	}
}

function ocultarAcordeon(id){
	console.log("ID ACORDEON OCULTO:" + id);
	if($("[data-padre="+id+"]").attr("style") == "display:none!important;" || $("[data-padre="+id+"]").attr("style") == "display: none !important;"){
		console.log("Mostrando");
		$("[data-padre="+id+"]").attr("style", "display:block!important;");
	}else{
		console.log("Mostrando");
		$("[data-padre="+id+"]").attr("style", "display:none!important;");
	}
	
}