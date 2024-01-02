var utilitario_textLarge = {
		showChar: 100,
		ellipsestext: "...",
		moretext: "más",
		lesstext: "menos",
		inicializar: function(){
			var ref = this;
			$(".more").each(function() {
				var content = $(this).html();

				if(content.length > ref.showChar) {

					var c = content.substr(0, ref.showChar);
					var h = content.substr(ref.showChar-1, content.length - ref.showChar);

					var html = c + '<span class="moreellipses">' + ref.ellipsestext + '&nbsp;</span><span class="morecontent"><span>' + h + '</span>&nbsp;&nbsp;<a href="" class="morelink">' + ref.moretext + '</a></span>';
				
					$(this).html(html);
				}
			});
			
			$(".morelink").click(function(){
				if($(this).hasClass("less")) {
					$(this).removeClass("less");
					$(this).html(ref.moretext);
				} else {
					$(this).addClass("less");
					$(this).html(ref.lesstext);
				}
				$(this).parent().prev().toggle();
				$(this).prev().toggle();
				return false;
			});
		}
};

setTimeout(function() {
	utilitario_textLarge.inicializar();
}, 100);

function LISTA_DATA(datos){
	this.datos = datos || [];
}

LISTA_DATA.prototype.agregarItem = function(item){
	if(!item){
		return;
	}
	var encontrado = false;
	for(var i in this.datos){
		if(this.datos[i].id == item.id){
			encontrado = true;
			break;
		}
	}
	if(!encontrado){
		this.datos.push(item);
	}
};

LISTA_DATA.prototype.agregarLista = function(lista){
	for(var i in lista){
		this.agregarItem(lista[i]);
	}
};

LISTA_DATA.prototype.buscarPorId = function(id){
	for(var i in this.datos){
		if(id == this.datos[i].id){
			return this.datos[i];
		}
	}
	return null;
};