var options = {
		columns: [
			{data: null},
			{data: "target.name"},
			{data: "target.ip"},
			{data: "provisioning.type"},
			{data: "target.manager"},
			{data: "provisioning.attributes"},
		],
		columnDefs:[]
	};

ws.query("/common/", function(resp){$("nav").append(resp); $(".gnb-provisioning").addClass("active");});
ws.query("data.txt", function(resp){
	var $dt = ws.dataTable($("#dataTable"), $.extend(options, {data:ws.store().data}));

	$(".excel").click(function(){
		ws.convert(ws.store().data, "Deactivate", true);
	});
});