var options = {
		columns: [
			{data: null},
			{data:"externalId"},
			{data:"userName"},
			{data:"meta.created"},
			{data:"meta.lastModified"},
			{data:"groups.length"},
			{data: null},
			{data: null}
		],
		drawCallback: function(){
			$('[data-toggle="popover"]').popover();
		},
		columnDefs:[{
				render: function (data, type, row) {
					if (typeof(row.groups) != 'undefined' && row.groups) {
						if (row.groups.length > 1) {
							var groups = [];
							$.each(row.groups, function(i, n){
								groups.push(n.display);
							});
							return "<a tabindex='0' role='button' data-toggle='popover' data-trigger='focus'  data-html='true' data-content='"+groups.join("<br>")+"'>" + row.groups[0].display + " (" + data + ") </a>"
						} else if (row.groups.length == 1) {
							return row.groups[0].display;
						} else {
							return "";
						}
					} else {
						return "";
					}
				},
				targets : 5
			}, {
				render: function(data, type, row) {
					return "<button class='btn btn-primary btn-xs btn-detail' type='button'>상세보기</button>";
				},
				targets : 6
			},{
				render: function(data, type, row) {
					return "";
				},
				targets:7
			}
		]
	};
ws.query("../common/", function(resp){$("nav").append(resp); $(".gnb-manager").addClass("active");});
//ws.query("data.txt", function(resp){
//**********>>> wession.com:5000/scim/v2.0/Users?attributes=meta,externalId,userName,groups&count=40
ws.query("/scim/v2.0/Users?attributes=active,meta,title,externalId,userName,displayname,groups&count=5000", function(resp){
//	console.log(ws.store());

	var $dt = ws.dataTable($("#dataTable"), $.extend(options, {data:ws.store().Resources})),
		$groupsCombo = ws.groupsCombo()
						.on("change", function(){
							$dt.column(5).search(this.value).draw();
						});
	$("div.toolbar").prepend($groupsCombo);
	$(".excel").click(function(){
		ws.convert(ws.store().Resources, "Resource", true);
	});
	$("#dataTable tbody").on("click", ".btn-detail", function(){
		var $id = $dt.cell($(this).closest("td")[0]).data().id;
		ws.exec("/scim/v2.0/Users/" + $id, function(data){
			if (!data.groups) data.groups=[];
			if (!data.phoneNumbers) data.phoneNumbers=[];
			if (!data.ims) data.ims=[];
			if (!data.emails) data.emails=[];

			var modal = ws.open('form.html', function(){
					$("#form-container")[0].innerHTML = tmpl("tmpl-manager", data);
					$(".modal-header").append(data.userName);
				});
			modal.on("show.bs.modal",function(){
				$("#pversion, #pcreated, #plastmodified, #pexternalid, #pusername").prop("readonly", true);
				$(".btn-del").data("id", $id);
				$(".btn-view-scim").data(data);
				$("input:radio[name=active]").on("change", ws.active_toggle);
				var enumGroups = [];
				if (data.groups !== undefined) {
					if (data.groups.length > 0){
						for (var i=0; i<data.groups.length; i++) {
							enumGroups.push(data.groups[i].display);
						}
						$("#pgroups").val(enumGroups.join(","));
					}
				} else {
					$("#pgroups").val("");
				}
			});
		});
	});
	$(".modal").on("click", ".btn-view-scim", ws.control_scim);
	$(".modal").on("click", ".btn-add-email, .btn-remove-email", ws.control_email);
	$(".modal").on("click", ".btn-add-ims, .btn-remove-ims", ws.control_ims);
	$(".modal").on("click", ".btn-add-phonenumber, .btn-remove-phonenumber", ws.control_phonenumber);
	$(".modal").on("click", ".btn-save", function(){
		if (confirm("저장하시겠습니까?\n\n변경된 내용은 다음 동기화 진행시 원복될 수 있습니다.")){
			ws.post("/admin/update", $('#resource'), function(res){
				var data = (typeof res != 'object')? $.parseJSON(res):res;
				if (data.message) {alert(data.message);}
				return false;
			});
		}
	});
	$(".modal").on("click", ".btn-del", function(){
		if (confirm("계정을 삭제하시겠습니까?\n\n삭제된 계정은 다음 동기화시 재생성될 수 있습니다.")) {
			ws.del("/admin/update", $(this), function(res){
				var data = (typeof res != 'object')? $.parseJSON(res):res;
				if (data.message) {alert(data.message);}
				return false;
			});
		}
	});
});