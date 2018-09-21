var options = {
		columns: [
			{data: null},
			{data: "target.name"},
			{data: "order"},
			{data: "process.startTime"},
			{data: "process.elapsed"},
			{data: "process.add"},
			{data: "process.delete"},
			{data: "process.modify"},
			{data: "process.ghost"},
			{data: "process.deactivate"},
			{data: null}
		],
		columnDefs:[
			{
				targets: 3,
				render: function(data, type, row) {
					return data + '<br>' + (row.process.endTime);
				}
			},{
				targets: 5,
				render: function(data, type, row) {
					var ary = [];
					if (row.add.length > 0) {
						for (var i=0 ;i<row.add.length ;i++ ) {
							ary.push(row.add[i].display);
						}
					}
					return "<a tabindex='0' role='button' data-toggle='popover' data-trigger='focus'  data-html='true' data-content='"+ary.join("<br>")+"'>" + data + "</a>";
				}
			},{
				targets: 6,
				render: function(data, type, row) {
					var ary = [];
					if (row.delete.length > 0) {
						for (var i=0 ;i<row.delete.length ;i++ ) {
							ary.push(row.delete[i].display);
						}
					}
					return "<a tabindex='0' role='button' data-toggle='popover' data-trigger='focus'  data-html='true' data-content='"+ary.join("<br>")+"'>" + data + "</a>";
				}
			},{
				targets: 7,
				render: function(data, type, row) {
					var ary = [];
					if (row.modify.length > 0) {
						for (var i=0 ;i<row.modify.length ;i++ ) {
							ary.push(row.modify[i].display);
						}
					}
					return "<a tabindex='0' role='button' data-toggle='popover' data-trigger='focus'  data-html='true' data-content='"+ary.join("<br>")+"'>" + data + "</a>";
				}
			},{
				targets: 8,
				render: function(data, type, row){
					var ary = [];
					if (row.ghost.length > 0) {
						for (var i=0 ;i<row.ghost.length ;i++ ) {
							ary.push(row.ghost[i].display);
						}
					}
					return "<a tabindex='0' role='button' data-toggle='popover' data-trigger='focus'  data-html='true' data-content='"+ary.join("<br>")+"'>" + data + '(' + ((row.process['ghost-diff'] > 0)? ((row.process['ghost-diff'] === 0)? '-':'▲'):'▽') + row.process['ghost-diff']  + ')' + "</a>";
				}
			},{
				targets: 9,
				render: function(data, type, row){
					var ary = [];
					if (row.deactivate.length > 0) {
						for (var i=0 ;i<row.deactivate.length ;i++ ) {
							ary.push(row.deactivate[i].display);
						}
					}
					return "<a tabindex='0' role='button' data-toggle='popover' data-trigger='focus'  data-html='true' data-content='"+ary.join("<br>")+"'>" + data + "(" + ((row.process['deactivate-diff'] > 0)? ((row.process['deactivate-diff'] === 0)? '-':'▲'):'▽') + row.process['ghost-diff']  + ")</a>";
				}
			},{
				targets: 10,
				render: function(data, type, row) {
					var ary = [];
					if (row.process.record.length > 0) {
						for (var i=0 ;i<row.process.record.length ;i++ ) {
							ary.push(row.process.record[i].order + ' : ' + row.process.record[i].count + '(' + ((row.process.record[i]['count-diff'] > 0) ? '▲':'▽') + Math.abs(row.process.record[i]['count-diff']) + ')');
						}
					}
					return "<a tabindex='0' role='button' data-toggle='popover' data-trigger='focus'  data-html='true' data-content='"+ary.join("<br>")+"'>" +
						row.process.record[0].order + " : " + row.process.record[0].count + "(" + ((row.process.record[0]['count-diff'] > 0) ? '▲':'▽') + Math.abs(row.process.record[0]['count-diff']) + ")</a>";
				}
			}
		]
	};

ws.query("/common/", function(resp){$("nav").append(resp); $(".gnb-provisioning").addClass("active");});
ws.query("data.txt?v="+ws.ver(), function(resp){
	var $dt = ws.dataTable($("#dataTable"), $.extend(options, {data:ws.store()})),
		$groupsCombo = ws.groupsCombo()
			.on("change", function(){
				console.log(this.value + "그룹 JSON 데이터 가져오기");
		}),
		$btnExtract = $("<button>").addClass("btn btn-info btn-sm").attr("id","btnExtract").text("로그조회");

	$("div.toolbar").prepend($groupsCombo, $btnExtract);
	$(".excel").click(function(){

		ws.convert(JSON.stringify(ws.store()), "ProvisioningLog", true);
	});
	$('[data-toggle="popover"]').popover();
});