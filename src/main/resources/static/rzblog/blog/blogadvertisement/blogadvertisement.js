var prefix = ctx + "blog/blogadvertisement"

$(function() {
	var columns = [
			{
				checkbox : true
			},
			{
				field : 'blogAdId',
				align : 'center',
				title : 'ID'
			},
			{
				field : 'adName',
				align : 'center',
				title : '名称',
				formatter : function(value, row, index) {
					if (row.adUrl != null && row.adUrl != '') {
						return '<a href="' + row.adUrl + '" target="_Blank">'
								+ value + '</a>';
					}
					return value;
				}
			},
			{
				field : 'adCompany',
				align : 'center',
				visible : false,
				title : '广告商'
			},
			{
				field : 'adType',
				align : 'center',
				title : '类型'
			},
			{
				field : 'adCost',
				align : 'center',
				visible : false,
				title : '费用'
			},
			{
				field : 'adImage',
				align : 'center',
				title : '广告图片',
				width : '10%',
				formatter : function(value, row, index) {
					var link = row.adImage;
					if (link == null || link == '') {
						return '';
					} else {
						return '<a href="'
								+ link
								+ '" target="_Blank"><img width="100px" height="50px" src="'
								+ link + '" ></img></a>';
					}
				}
			},
			{
				field : 'adUrl',
				align : 'center',
				title : '广告链接',
				visible : false,
				formatter : function(value, row, index) {
					if (value != null && value != '') {
						return '<a href="' + value + '" target="_Blank">'
								+ value + '</a>';
					}
					return '';
				}
			},
			{
				field : 'adPlacement',
				align : 'center',
				visible : false,
				title : '投放位置'
			},
			{
				field : 'adStartIme',
				align : 'center',
				visible : false,
				title : '开始时间'
			},
			{
				field : 'adEndIme',
				align : 'center',
				visible : false,
				title : '结束时间'
			},
			{
				field : 'status',
				align : 'center',
				title : '状态',
				formatter : function(value, row, index) {
					if (value == "0") {
						return '<span class="label label-success">可用</span>';
					} else {
						return '<span class="label label-danger">禁用</span>';
					}
				}
			},
			{
				title : '操作',
				align : 'center',
				formatter : function(value, row, index) {
					var actions = [];
					actions
							.push('<a class="btn btn-primary btn-xs '
									+ editFlag
									+ '" href="#" title="编辑" mce_href="#" onclick="edit(\''
									+ row.blogAdId
									+ '\')"><i class="fa fa-edit"></i>编辑</a> ');
					actions.push('<a class="btn btn-warning btn-xs '
							+ removeFlag
							+ '" href="#" title="删除" onclick="remove(\''
							+ row.blogAdId
							+ '\')"><i class="fa fa-remove"></i>删除</a>');
					return actions.join('');
				}
			} ];
	var url = prefix + "/list";
	$.initTable(columns, url, '请输入广告名称');
});

/* 广告管理-新增 */
function add() {
	var url = prefix + '/add';
	layer_showAuto("新增广告", url);
}

/* 广告管理-修改 */
function edit(blogAdId) {
	var url = prefix + '/edit/' + blogAdId;
	layer_showAuto("修改广告", url);
}

// 单条删除
function remove(id) {
	$.modalConfirm("确定要删除选中广告吗？", function() {
		_ajax(prefix + "/remove/" + id, "", "post");
	})
}

// 批量删除
function batchRemove() {
	var rows = $.getSelections("blogAdId");
	if (rows.length == 0) {
		$.modalMsg("请选择要删除的数据", "warning");
		return;
	}
	$.modalConfirm("确认要删除选中的" + rows.length + "条数据吗?", function() {
		_ajax(prefix + '/batchRemove', {
			"ids" : rows
		}, "post");
	});
}
