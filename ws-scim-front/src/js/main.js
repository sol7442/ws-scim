var bearerToken = "Bearer eyJ0eXAiOiJKV1QiLCJpc3N1ZURhdGUiOjE0OTYxOTk5NjEyMzMsImFsZyI6IkhTMjU2In0.eyJzdWIiOiJTQ0lNL1VTRVIiLCJpc3MiOiJTQ0lNIiwiaWF0IjoxNDk2MTk5OTYxLCJleHAiOjE1Mjc3MzU5NjEsInByb3ZpZGVyIjoidHJ1ZSIsInNlcnZlcklwIjoiMTI3LjAuMC4xIiwiY29scGFydCI6IkEwMDEiLCJhcHBDb2RlIjoiRGVtb0hSIn0.utQJYMdIRCJUMM5On3-G6Ay0mKBZ2aWnLKvcQ68kCl0";
var ws = function($){
	var ds = null,
		dt = null;
	return {
		store: function(){
			return typeof ds != 'object' ? $.parseJSON(ds) : ds;
		},
		query: function(url, f){
			$.ajax({
					url: url,
					beforeSend:function(xhr){
						$('.wrap-loading').removeClass('display-none');
						xhr.setRequestHeader("Authorization", bearerToken);
						xhr.setRequestHeader("Content-type","application/scim+json");
					}
				})
				.done(function(data){ds=data; f(data);});
		},
		exec: function(url, f) {
			$.ajax({
				url:url,
				beforeSend:function(xhr){
						xhr.setRequestHeader("Authorization", bearerToken);
						xhr.setRequestHeader("Content-type","application/scim+json");
				}
			}).done(function(data){
				if (typeof f === 'function') f((typeof data != 'object') ? $.parseJSON(data) : data);
			});
		},
		del: function(url, el, f){
			$.ajax({
				url:url,
				type:'POST',
				data:el.data("id"),
				beforeSend:function(xhr){
						xhr.setRequestHeader("Authorization", bearerToken);
						xhr.setRequestHeader("Content-type","application/scim+json");
				}
			}).done(function(res){
				if (typeof f === 'function') f(res);
			});
		},
		post: function(url, frm, f){
			$.ajax({
				url:url,
				type:'POST',
				data:frm.serialize(),
				beforeSend:function(xhr){
						xhr.setRequestHeader("Authorization", bearerToken);
						xhr.setRequestHeader("Content-type","application/x-www-form-urlencoded; charset=UTF-8");
				}
			}).done(function(res){
				if (typeof f === 'function') f(res);
			});
		},
		__query: function(url, f){
			$.ajax({
				url: url,
				beforeSend:function(xhr){
						xhr.setRequestHeader("Authorization", bearerToken);
						xhr.setRequestHeader("Content-type","application/scim+json");
				}
			}).done(function(data){
				if (typeof f === "function") f(data);
			});
		},
		dataTable: function(target, opts){
			$('.wrap-loading').addClass('display-none');
			dt = target.DataTable($.extend(true, ws.defaults.dataTableOptions, opts));
			dt.on('order.dt search.dt', function(e){
				var max = arguments[1].aiDisplay.length;
				dt.column(0, {search: 'applied', order: 'applied'}).nodes().each(function(cell, i) {
					cell.innerHTML = max - i;
				});
			}).draw();
			var $excel = $("<button class='btn btn-success btn-sm btn-convert-excel excel'><i class='glyphicon glyphicon-download-alt'></i> CSV </button>");
			$("div.toolbar").append($excel);
			return dt;
		},
		groupsCombo: function(ary){
			var $el = $("<select>").attr("id","selGroups").attr("name","selGroups").addClass("form-control input-sm"),
				groups = ["Groupware","BuddyOne"];
			$("<option>").val("").text("그룹선택").appendTo($el);
			for (var i=0; i<groups.length; i++) {
				$("<option>").val(groups[i]).text(groups[i]).appendTo($el);
			}
			return $el;
		},
		open: function(url, f){
			var modal = $(".modal");
			modal.empty();
			ws.__query(url, function(resp){
				modal.html(resp);
				if (typeof f === "function") f(resp);
				modal.modal({show: true, backdrop: 'static'});
			});
			return modal;
		},
		ver: function(){
			return (new Date().getTime().toString());
		},
		convert: function(JSONData, ReportTitle, ShowLabel) {
			var arrData = typeof JSONData != 'object' ? JSON.parse(JSONData) : JSONData;
			var CSV = '';
			//CSV += ReportTitle + '\r\n';
			var row = "";
			if (ShowLabel) {
				for (var key in arrData[0]) {
					row += key + ',' ;
				}
				row = row.slice(0, -1) + "\r\n";
			}

			for (var i = 0; i < arrData.length; i++) {
				for (var index in arrData[i]) {
				var obj = "";
					if (typeof arrData[i][index] === "object" && arrData[i][index] !== null) {
						for (var j=0; j<arrData[i][index].length; j++) {
							if (typeof arrData[i][index][j] === "string") {
								obj += arrData[i][index][j] + ",";
							} else {
								obj += "{";
								for (var key in arrData[i][index][j]) {
									obj += (key + ":" + arrData[i][index][j][key] + ",");
								}
								obj = obj.slice(0, -1);
								obj += "}, ";
							}
						}
							obj = obj.slice(0, -2);
							row += '"' + obj + '",';
					} else {
						row += '"' + arrData[i][index] + '",';
					}
				}
				row.slice(0, row.length-1);
				row += '\r\n';
			}
			if (row == '') {
				alert("Invalid data");
				return;
			}

			var fileName = "WessionIM_";
			fileName += ReportTitle.replace(/ /g,"_");

			var uri = 'data:text/csv;charset=UTF-8,\uFEFF' + encodeURI(row);

			var link = document.createElement("a");
			link.href = uri;
			link.style = "visibility:hidden";
			link.download = fileName + ".csv";

			document.body.appendChild(link);
			link.click();
			document.body.removeChild(link);
		},
		active_toggle: function() {
			var flg = ($(this).val() === 'true'),
				$true = $("#ptrue").closest("label"),
				$false = $("#pfalse").closest("label");
			if (flg) {
				$true.removeClass("btn-default").addClass("btn-info");
				$false.removeClass("btn-danger").addClass("btn-default");
			} else {
				$true.removeClass("btn-info").addClass("btn-default");
				$false.removeClass("btn-default").addClass("btn-danger");
			}
		},
		control_email: function (){
			var $wrap = $(this).closest(".col-sm-7");
			if ($(this).hasClass("btn-add-email")) {
					$(this).attr("title", "remove email").removeClass("btn-info btn-add-email").addClass("btn-remove-email btn-danger").find("i").removeClass("glyphicon-plus").addClass("glyphicon-minus");
					var el = $("<div class='input-group addon'><input type='text' name='pemails' class='form-control input-sm'><div class='input-group-btn'><button class='btn btn-info btn-sm btn-add-email' type='button' title='add email'><i class='glyphicon glyphicon-plus'></i></button></div></div></div>");
					el.appendTo($wrap);
					el.find("input").focus();
			} else {
				if (confirm("이메일을 삭제하시겠습니까?")) {
					$(this).closest(".input-group").remove();
				}
			}
		},
		control_ims: function (){
			var $wrap = $(this).closest(".col-sm-7");
			if ($(this).hasClass("btn-add-ims")) {
					$(this).attr("title", "remove ims").removeClass("btn-info btn-add-ims").addClass("btn-remove-ims btn-danger").find("i").removeClass("glyphicon-plus").addClass("glyphicon-minus");
					var el = $("<div class='input-group addon'><input type='text' name='pims' class='form-control input-sm'><div class='input-group-btn'><button class='btn btn-info btn-sm btn-add-ims' type='button' title='add ims'><i class='glyphicon glyphicon-plus'></i></button></div></div></div>");
					el.appendTo($wrap);
					el.find("input").focus();
			} else {
				if (confirm("이메일을 삭제하시겠습니까?")) {
					$(this).closest(".input-group").remove();
				}
			}
		},
		control_phonenumber: function (){
			var $wrap = $(this).closest(".col-sm-7");
			if ($(this).hasClass("btn-add-phonenumber")) {
					$(this).attr("title", "remove phoneNumber").removeClass("btn-info btn-add-phonenumber").addClass("btn-remove-phonenumber btn-danger").find("i").removeClass("glyphicon-plus").addClass("glyphicon-minus");
					var el = $("<div class='input-group addon'><input type='text' name='pphonenumber' class='form-control input-sm'><div class='input-group-btn'><button class='btn btn-info btn-sm btn-add-phonenumber' type='button' title='add phoneNumber'><i class='glyphicon glyphicon-plus'></i></button></div></div></div>");
					el.appendTo($wrap);
					el.find("input").focus();
			} else {
				if (confirm("전화번호를 삭제하시겠습니까?")) {
					$(this).closest(".input-group").remove();
				}
			}
		},
		control_scim: function (){
			var win = window.open();
			win.document.open().write(JSON.stringify($(this).data()));
			win.document.close();
			win.focus();
		},
		info: function(){
			console.log(this.defaults);
		}
	}
}(jQuery);

ws.defaults = {
	context: "body",
	dataTableOptions: {
		processing:		true,
		lengthChange:	false,
		info:			false,
		ordering:		false,
		pageLength:		20,
		pagingType:		"full_numbers",
		language: {
			processing:			"data loading ...",
			loadingRecords:		"data loading ...",
			search:				"",
			searchPlaceholder:	"검색",
			paginate: {
				first:			"<i class='glyphicon glyphicon-step-backward'></i>",
				previous:		"<i class='glyphicon glyphicon-triangle-left'></i>",
				next:			"<i class='glyphicon glyphicon-triangle-right'></i>",
				last:			"<i class='glyphicon glyphicon-step-forward'></i>"
			}
		},
		dom: '<"toolbar">frtip'
	}
};