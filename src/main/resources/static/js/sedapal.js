
$(function() {
	
	var Dachshund = Bloodhound.noConflict();
	var typeahead = jQuery.fn.typeahead.noConflict();
	jQuery.fn._typeahead = typeahead;
	
	var ficheros = new Bloodhound({
		  limit: 10,
		  datumTokenizer: Bloodhound.tokenizers.obj.whitespace('idficha'),
		  queryTokenizer: Bloodhound.tokenizers.whitespace,					  
		  remote: {
		    url : $("#hdnBuscarUser").attr("href") + '%QUERY',
		    wildcard: '%QUERY'
		  }
		});
	
	$(".fichaUsuario").typeahead({
		  hint: false,
		  highlight: true,
		  minLength: 3
		},
		{
		  name: 'idficha',
		  display: 'idficha',
		  source: ficheros,
		  templates: {
			    empty: [
			      '<div class="empty-message">',
			        'no se encontro ficha de registro',
			      '</div>'
			    ].join('\n'),
			    suggestion: function(data) {
			        return '<p>' + data.idficha + ' – <strong>' + data.nombreCompleto + '</strong></p>';
			    }
			  }
		});
	
	$('.fichaUsuario').bind('typeahead:select', function(ev, data) {
		  console.log('Selection: ' + data + ' ' + data.nombreCompleto + ' ' + data.ceco.centroCosto);
		  
		  $(".fichaNombres").val(data.nombreCompleto);
			$(".centroCostoDeno").val(data.ceco.denominacion);
			$(".centroCosto").val(data.ceco.centrocosto);
			$(".gerencia").val(data.ceco.gerencia);
          $(".proyecto").val(data.ceco.codigoDocumentario);
		  
		});
	
	// <![CDATA[
	$(".inputNumber").keydown(
		function(event) {
			if (event.keyCode == 46 || event.keyCode == 8
					|| event.keyCode == 9 || event.keyCode == 37
					|| event.keyCode == 38 || event.keyCode == 39
					|| event.keyCode == 40 || event.keyCode == 13) {
				// let it happen, don't do anything
			} else if ((event.shiftKey || (event.keyCode < 48 || event.keyCode > 57)) 
					&& (event.keyCode < 96 || event.keyCode > 105)) {
				event.preventDefault();
			}
		});
	// ]]>
	
//	$('.nocopy').bind("cut copy paste",function(e) {
//	        e.preventDefault();
//	  });
	
	$('#datetimepicker').datetimepicker({
        locale: 'es',
        format: 'L'
    });
	
	$(".showImg").click(function(event) {
		$("#viewImg").attr('src', '');
    	$("#showImgModal").modal({
			backdrop: "static",
			show : true
		});
    	
    	var url = $("#hdnShowImgn").attr("href");
    		url += this.id;
		$("#viewImg").attr('src', url);
		var i = $(this).index();
		$("#titleModalLabel span").text("Imagen "+ i);
    });
	
	
	
//	$(".fichaUsuario").blur(function(e){
//		e.preventDefault();
//		
//		var urlAction = $("#hdnBuscarUser").attr("href");
//			urlAction += $(this).val();
//		
//		console.log(urlAction);
//		
//		$.getJSON(urlAction)
//			.done(function(data){
//				$(".fichaNombres").val(data.nombreCompleto);
//				$(".fichaUsuario").val(data.idficha);
//				$(".centroCostoDeno").val(data.ceco.denominacion);
//				$(".centroCosto").val(data.ceco.centroCosto);
//				$(".gerencia").val(data.ceco.gerencia);
//                $(".proyecto").val(data.ceco.codigoDocumentario);
//			})
//			.error(function(ev){
//				$(".fichaNombres").val('');
//				$(".fichaUsuario").val('');
//				$(".centroCostoDeno").val('');
//				$(".centroCosto").val('');
//				$(".gerencia").val('');
//                $(".proyecto").val('');
//				  $("#msgAjax").stop().addClass("error").css( "opacity", 1 ).text("Error codigo de fichero incorrecto..")
//				    .fadeIn( 50 ).fadeOut( 3000 );
//				$(".fichaUsuario").select();
//				$(".fichaUsuario").focus();
//			});
//	});
	
//	$(".centroCosto").blur(function(e){
//		e.preventDefault();
//		
//		var urlAction = $("#hdnBuscarCeco").attr("href");
//			urlAction += $(this).val();
//		$.getJSON(urlAction)
//			.done(function(data){
//				$(".centroCostoDeno").val(data.denominacion);
//				$(".centroCosto").val(data.centrocosto);
//                $(".gerencia").val(data.gerencia);
//                $(".proyecto").val(data.unidad_organica);
//			})
//			.error(function(ev){
//				console.log("error")
//				$(".centroCostoDeno").val('');
//				  $("#msgAjaxCC").stop().addClass("error").css( "opacity", 1 ).text("Error centro de costo incorrecto...")
//				    .fadeIn( 50 ).fadeOut( 3000 );
//				  $(".centroCosto").select();
////				  $(".centroCosto").focus();
//			});
//	});
	
//	$(".emplazamiento").blur(function(e){
//		e.preventDefault();
//		
//		var urlAction = $("#hdnBuscarEmplz").attr("href");
//			urlAction += $(this).val();
//		$.getJSON(urlAction)
//			.done(function(data){
//				$(".emplazamientoDeno").val(data.vinculado);
//				$(".emplazamiento").val(data.codigo);
//			})
//			.error(function(ev){
//				$(".emplazamientoDeno").val('');
//				$("#msgAjaxEmp").stop().addClass("error").css( "opacity", 1 ).text("Error codigo de emplazamiento...")
//				    .fadeIn( 50 ).fadeOut( 3000 );
//				$(".emplazamiento").select();
////				$(".emplazamiento").focus();
//			});
//	});
	
	$(".cmbEmplazamiento").change(function(e){
		e.preventDefault();
		
		$(".emplazamientoDeno").val('');
		var txtCmb = $('option:selected', $(this)).text();
		var raya = txtCmb.split('-');
		var divi = raya.split('/');
		$(".emplazamientoDeno").val($.trim(divi[1]));
		$("#txtDenoEmplazamientos").val($.trim(divi[1]));
	});
	
	$(".cmbCecos").change(function(e){
		e.preventDefault();
		
		$(".centroCostoDeno").val('');
		var txtCmb = $('option:selected', $(this)).text();
		var arr = txtCmb.split('-');
		$(".centroCostoDeno").val($.trim(arr[1]));
		$("#txtDenominacionCeco").val($.trim(arr[1]));
		
//		var urlAction = $("#hdnBuscarCeco").attr("href");
//			urlAction += $(this).val();
//		$.getJSON(urlAction)
//			.done(function(data){
//				$(".centroCostoDeno").val(data.denominacion);
////				$(".centroCosto").val(data.centrocosto);
//	            $(".gerencia").val(data.gerencia);
//	            $(".proyecto").val(data.unidad_organica);
//			})
	});
	
	$(".cmbclase").change(function(e){
		e.preventDefault();
		var txtCmb = $('option:selected', $(this)).text();
		console.log('text==>' + txtCmb)
		$("#denominacionClase").val(txtCmb);
	});
	
	$(".zero6").change(function(){
		var field = $(this);
		var start = field.val().length;
		field.val(leftPadTextof6(start, field.val()));
	});
	
});


var idValues=[];

function validateIdRemoves(activoAEliminar) {
	idValues = $.grep(idValues, function(value) {
		  return value != activoAEliminar;
		});
}

function validateIdRemovesAdd(activoAEliminar){
	if(!isValidCodeInArray(activoAEliminar))
		idValues.push(activoAEliminar)
}

function isValidCodeInArray(code){
    return ($.inArray(code, idValues) > -1);
}

function validMasterCheckbox(){
	var cntActivo = $('.removeActivo:checkbox', 'table > tbody' ).length;
	var cntActivoCheck = $('.removeActivo:checked', 'table > tbody').length
	if (cntActivo == cntActivoCheck )
		$("#selectAll").prop('checked', true);
	else
		$("#selectAll").prop('checked', false);
	
	if(cntActivoCheck == 0)
		$(".removeActivosbtn").addClass("hidden");
}

function leftPadTextof6(ini, text){
	var start = 6-ini;
	return loopleftPadText('0', start)+text;
}

// <![CDATA[
function loopleftPadText(text, countEnd){
	var strConcate = '';
	var i = 0;
	while( (i++) < countEnd ){
		strConcate += text;
	}
	return strConcate;
}
// ]]>