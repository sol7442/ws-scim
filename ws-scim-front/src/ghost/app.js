var options = {
		columns: [
			{data: null},
			{data: null},
			{data:"companyID"},
			{data:"id"},
			{data:"Name"},
			{data:"title"},
			{data: null},
			{data: null}
		],
		columnDefs:[
			{
				render: function(data, type, row) {
					return ws.store().target;
				},
				targets :1
			},{
				render: function(data, type, row) {
					return ws.store().endTime;
				},
				targets :6
			},{
				render: function(data, type, row) {
					return "";
				},
				targets :7
			}
		]
	};

ws.query("/common/", function(resp){$("nav").append(resp); $(".gnb-ghost").addClass("active");});
ws.query("data.txt?v=" + ws.ver(), function(resp){
	var $dt = ws.dataTable($("#dataTable"), $.extend(options, {data:ws.store().ghost})),
		$groupsCombo = ws.groupsCombo()
			.on("change", function(){
				console.log(this.value + "그룹 JSON 데이터 가져오기");
		}),
		$btnExtract = $("<button>").addClass("btn btn-info btn-sm").attr("id","btnExtract").text("고아계정추출");

	$("div.toolbar").prepend($groupsCombo, $btnExtract);
	$(".excel").click(function(){
		ws.convert(ws.store().ghost, "GhostAccount", true);
	});
});