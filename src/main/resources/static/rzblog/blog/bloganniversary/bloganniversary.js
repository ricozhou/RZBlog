var prefix = ctx + "blog/bloganniversary"

$(function() {
	var columns = [
			{
				checkbox : true
			},
			{
				field : 'blogAnniversaryId',
				align : 'center',
				title : 'ID'
			},
			{
				field : 'anniversaryDate',
				align : 'center',
				title : '日期',
				formatter : function(value, row, index) {
					return '<span class="label label-primary">' + value
							+ '</span>';
				}
			},
			{
				field : 'anniversaryName',
				align : 'center',
				title : '纪念日名'
			},
			{
				field : 'anniversaryDesc',
				align : 'center',
				visible : false,
				title : '纪念日描述'
			},
			{
				field : 'anniversaryWebsiteOperate',
				align : 'center',
				title : '整站操作',
				formatter : function(value, row, index) {
					if (value == '0') {
						return '<span class="label label-primary">无操作</span>';
					} else if (value == '1') {
						return '<span class="label label-danger">整站置灰</span>';
					}
				}
			},
			{
				field : 'anniversaryWebsiteShow',
				align : 'center',
				title : '详情显示',
				formatter : function(value, row, index) {
					if (value == '0') {
						return '<span class="label label-primary">不显示</span>';
					} else if (value == '1') {
						return '<span class="label label-success">弹窗显示</span>';
					} else if (value == '2') {
						return '<span class="label label-info">顶部显示</span>';
					} else if (value == '3') {
						return '<span class="label label-warning">侧边显示</span>';
					}
				}
			},
			{
				field : 'status',
				align : 'center',
				title : '状态',
				formatter : function(value, row, index) {
					if (value == '0') {
						return '<span class="label label-success">可用</span>';
					} else if (value == '1') {
						return '<span class="label label-danger">禁用</span>';
					}
				}
			},
			{
				field : 'createBy',
				align : 'center',
				visible : false,
				title : '创建者'
			},
			{
				field : 'updateBy',
				align : 'center',
				visible : false,
				title : '更新者'
			},
			{
				field : 'createTime',
				align : 'center',
				visible : false,
				title : '创建时间'
			},
			{
				field : 'updateTime',
				align : 'center',
				visible : false,
				title : '更新时间'
			},
			{
				field : 'remark',
				align : 'center',
				visible : false,
				title : '备注'
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
									+ row.blogAnniversaryId
									+ '\')"><i class="fa fa-edit"></i>编辑</a> ');
					actions.push('<a class="btn btn-warning btn-xs '
							+ removeFlag
							+ '" href="#" title="删除" onclick="remove(\''
							+ row.blogAnniversaryId
							+ '\')"><i class="fa fa-remove"></i>删除</a>');
					return actions.join('');
				}
			} ];
	var url = prefix + "/list";
	$.initTable(columns, url, '请输入日期、纪念日、描述');
});

/* 纪念日管理-新增 */
function add() {
	var url = prefix + '/add';
	layer_showAuto("新增纪念日", url);
}

/* 纪念日管理-修改 */
function edit(blogAnniversaryId) {
	var url = prefix + '/edit/' + blogAnniversaryId;
	layer_showAuto("修改纪念日", url);
}

// 单条删除
function remove(id) {
	$.modalConfirm("确定要删除选中纪念日管理吗？", function() {
		_ajax(prefix + "/remove/" + id, "", "post");
	})
}

// 批量删除
function batchRemove() {
	var rows = $.getSelections("blogAnniversaryId");
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
