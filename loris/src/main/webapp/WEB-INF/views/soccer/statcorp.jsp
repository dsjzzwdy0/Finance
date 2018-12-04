<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@page import="com.loris.soccer.analysis.util.IssueMatchUtil" %>
<%@page import="com.baomidou.mybatisplus.toolkit.StringUtils" %>

<link rel="stylesheet" type="text/css" href="../content/css/soccer/datacenter.css" />
<%
    String issue = request.getParameter("issue");
	String type = request.getParameter("type");
	
	if(StringUtils.isEmpty(issue))
	{
		issue = IssueMatchUtil.getCurrentIssue();
	}
	if(StringUtils.isEmpty(type))
	{
		type = "bd";
	}
%>

<div id="content" class="container_wrapper">
	<%@include file="./analysis/toolbar.jsp"%>

	<div id="main" class="main_wrapper">		
		<table class="gridTable table table-hover table-striped" id="gridTable" data-pagination="false"
			data-show-refresh="false" data-show-toggle="false"
			data-showColumns="false">
		</table>
	</div>
</div>

<script type="text/javascript">
var url = "../soccerdata/getCorpStatItems"

function getCorpStatItems(request)
{
	$.ajax({
		type: "GET",
		url: url,
		contentType : "application/json;charset=utf-8",
		dataType : "json",
        success : function (msg)
        {
			var corps = msg.data;
			request.success({
                row : corps
            });
            $('#gridTable').bootstrapTable('load', corps);
        },
		error:function(){
			layer.msg("错误");
		}
    });
}

function initTable() 
{
	//先销毁表格  
	$('#gridTable').bootstrapTable('destroy');
	$("#gridTable").bootstrapTable({ 
		ajax: getCorpStatItems,
		striped: false, //表格显示条纹 
		pagination: false, //启动分页 
		search: false, //是否启用查询 
		showColumns: false, //显示下拉框勾选要显示的列 
		showRefresh: false, //显示刷新按钮 
		sidePagination: "server", //表示服务端请求 
		//toolbar: "#toolbar",
		//设置为undefined可以获取pageNumber，pageSize，searchText，sortName，sortOrder 
		//设置为limit可以获取limit, offset, search, sort, order 
		queryParamsType : "undefined",		
		columns: [
		[{
	       	field: 'name',
	       	title: '序号',
	       	sortable: true,
	       	rowspan: 2,
	       	formatter: function(value, row, index)
     		{
     			return index + 1;
     		}
	    },
		{
        	field: 'name',
         	title: '公司名称',
         	sortable: true,
         	rowspan: 2,
     	},
     	{
        	field: 'baseOpVar',
        	sortable: true,
        	title: '全部比赛',
        	colspan: 6,
        	formatter: function(value, row, index)
     		{
     			return value.num;
     		}
    	},
     	{
     		field: 'winOpVar',
     		sortable: true,
        	title: '胜比赛',
        	colspan: 6,
        	formatter: function(value, row, index)
     		{
     			return value.num;
     		}
     	},
     	{
     		field: 'drawOpVar',
     		sortable: true,
        	title: '平比赛',
        	colspan: 6,
        	formatter: function(value, row, index)
     		{
     			return value.num;
     		}
     	},
     	{
     		field: 'loseOpVar',
     		sortable: true,
        	title: '负比赛',
        	colspan: 6,
        	formatter: function(value, row, index)
     		{
     			return value.num;
     		}
     	}],
     	[{
     		field: 'baseOpVar',
            sortable: true,
            title: '胜差',
            formatter: function(value, row, index)
         	{
            	return formatVars(value.vars, 0);
         	}
     	},
     	{
     		field: 'baseOpVar',
            sortable: true,
            title: '平差',
            formatter: function(value, row, index)
         	{
            	return formatVars(value.vars, 1);
         	}
     	},
     	{
     		field: 'baseOpVar',
            sortable: true,
            title: '负差',
            formatter: function(value, row, index)
         	{
            	return formatVars(value.vars, 2);
         	}
     	},
     	{
     		field: 'baseOpVar',
            sortable: true,
            title: '胜方差',
            formatter: function(value, row, index)
         	{
            	return formatVars(value.vars, 3);
         	}
     	},
     	{
     		field: 'baseOpVar',
            sortable: true,
            title: '平方差',
            formatter: function(value, row, index)
         	{
            	return formatVars(value.vars, 4);
         	}
     	},
     	{
     		field: 'baseOpVar',
            sortable: true,
            title: '负方差',
            formatter: function(value, row, index)
         	{
            	return formatVars(value.vars, 5);
         	}
     	},
     	{
     		field: 'winOpVar',
            sortable: true,
            title: '胜差',
            formatter: function(value, row, index)
         	{
            	return formatVars(value.vars, 0);
         	}
     	},
     	{
     		field: 'winOpVar',
            sortable: true,
            title: '平差',
            formatter: function(value, row, index)
         	{
            	return formatVars(value.vars, 1);
         	}
     	},
     	{
     		field: 'winOpVar',
            sortable: true,
            title: '负差',
            formatter: function(value, row, index)
         	{
            	return formatVars(value.vars, 2);
         	}
     	},
     	{
     		field: 'winOpVar',
            sortable: true,
            title: '胜方差',
            formatter: function(value, row, index)
         	{
            	return formatVars(value.vars, 3);
         	}
     	},
     	{
     		field: 'winOpVar',
            sortable: true,
            title: '平方差',
            formatter: function(value, row, index)
         	{
            	return formatVars(value.vars, 4);
         	}
     	},
     	{
     		field: 'winOpVar',
            sortable: true,
            title: '负方差',
            formatter: function(value, row, index)
         	{
            	return formatVars(value.vars, 5);
         	}
     	},
     	{
     		field: 'drawOpVar',
            sortable: true,
            title: '胜差',
            formatter: function(value, row, index)
         	{
            	return formatVars(value.vars, 0);
         	}
     	},
     	{
     		field: 'drawOpVar',
            sortable: true,
            title: '平差',
            formatter: function(value, row, index)
         	{
            	return formatVars(value.vars, 1);
         	}
     	},
     	{
     		field: 'drawOpVar',
            sortable: true,
            title: '负差',
            formatter: function(value, row, index)
         	{
            	return formatVars(value.vars, 2);
         	}
     	},
     	{
     		field: 'drawOpVar',
            sortable: true,
            title: '胜方差',
            formatter: function(value, row, index)
         	{
            	return formatVars(value.vars, 3);
         	}
     	},
     	{
     		field: 'drawOpVar',
            sortable: true,
            title: '平方差',
            formatter: function(value, row, index)
         	{
            	return formatVars(value.vars, 4);
         	}
     	},
     	{
     		field: 'drawOpVar',
            sortable: true,
            title: '负方差',
            formatter: function(value, row, index)
         	{
            	return formatVars(value.vars, 5);
         	}
     	},
     	{
     		field: 'loseOpVar',
            sortable: true,
            title: '胜差',
            formatter: function(value, row, index)
         	{
            	return formatVars(value.vars, 0);
         	}
     	},
     	{
     		field: 'loseOpVar',
            sortable: true,
            title: '平差',
            formatter: function(value, row, index)
         	{
            	return formatVars(value.vars, 1);
         	}
     	},
     	{
     		field: 'loseOpVar',
            sortable: true,
            title: '负差',
            formatter: function(value, row, index)
         	{
         		return formatVars(value.vars, 2);
         	}
     	},
     	{
     		field: 'loseOpVar',
            sortable: true,
            title: '胜方差',
            formatter: function(value, row, index)
         	{
            	return formatVars(value.vars, 3);
         	}
     	},
     	{
     		field: 'loseOpVar',
            sortable: true,
            title: '平方差',
            formatter: function(value, row, index)
         	{
            	return formatVars(value.vars, 4);
         	}
     	},
     	{
     		field: 'loseOpVar',
            sortable: true,
            title: '负方差',
            formatter: function(value, row, index)
         	{
         		return formatVars(value.vars, 5);
         	}
     	}]
		],
		onLoadSuccess: function(){ //加载成功时执行 
		    layer.msg("加载成功");
			//$("#cusTable").TabStyle();
		}, 
		onLoadError: function(){ //加载失败时执行 
			layer.msg("加载数据失败", {time : 1500, icon : 2}); 
		} 
	});	
}

function formatVars(vars, index)
{
	if(!$.isNullOrEmpty(vars))
	{
		return vars[index].toFixed(2);
	}
	else
	{
		return '';
	}
}

$(document).ready(function() {
	//调用函数，初始化表格  
	initTable();
});
</script>