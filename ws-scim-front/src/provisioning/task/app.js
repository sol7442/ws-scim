var options = {
		columns: [
			{data: null},
			{data: null},
			{data: null},
			{data: null},
			{data: null},
			{data: null},
			{data: null}
		],
		columnDefs:[]
	};

ws.query("/common/", function(resp){$("nav").append(resp); $(".gnb-provisioning").addClass("active");});
ws.query("data.txt", function(resp){
	var $dt = ws.dataTable($("#dataTable"), $.extend(options, {data:ws.store()})),
		$groupsCombo = ws.groupsCombo()
			.on("change", function(){
				console.log(this.value + "그룹 JSON 데이터 가져오기");
		})

	$("div.toolbar").prepend($groupsCombo);
	$(".excel").click(function(){

		ws.convert(JSON.stringify(ws.store()), "Provisioning_Sheduler", true);
	});
});