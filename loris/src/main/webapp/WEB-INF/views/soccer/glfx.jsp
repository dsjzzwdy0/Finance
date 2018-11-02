<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%
	String issue = request.getParameter("issue");
	String lid = request.getParameter("lid");
%>

<html xmlns="http://www.w3.org/1999/xhtml">
<!--  head meta, include css, title, etc. -->
<%@include file="./shared/basic.jsp"%>

<style type="text/css">
html, body {
	width: 100%;
	height: 100%;
	margin: 0;
}

.headerPanel {
	height: 120px;
}

.wrapper {
	width: 90%;
	height: 800px;
	margin: auto;
}

#main {
	height: 100%;
}
</style> 
<body>

<div class="headerPanel"></div> 
<div class="wrapper"> 
   	<div id="main"></div> 
</div>

<script type="text/javascript" src="../content/scripts/echarts/dist/echarts.js"></script> 
<script type="text/javascript" src="../content/scripts/echarts/theme/vintage.js"></script> 
<script type="text/javascript" src="../content/scripts/echarts/dist/extension/dataTool.js"></script> 
<script type="text/javascript" src="../content/scripts/echarts/lib/dat.gui.min.js"></script>

<script>

var myChart = echarts.init(document.getElementById('main'), 'vintage', {});
myChart.showLoading();
$.getJSON('../soccerdata/getGraph?issue=<%=issue%>&lid=<%=lid%>', function (json) 
{
	json = json.data;
    myChart.hideLoading();
    myChart.setOption(option = 
    {
    	tooltip: {
    		formatter: function (params, ticket, callback) {
    		    return params.data.name + '<br/>' + params.data.attr;
    		}
    	},
    	title : {
            text : '意甲联赛2017－2018赛季第12轮数据分析' 
        },
        animationDurationUpdate : 1500, 
        animationEasingUpdate : 'quinticInOut', 
        series : [ 
        {
        	type : 'graph', 
            layout : 'circular', // progressiveThreshold: 700, force
            data : json.nodes.map(function (node) 
            {
            	
                return {
                    //x : node.x, 
                    //y : node.y, 
                    id: node.id, 
                    name: node.name, 
                    attr: node.attrs.join("<br>"),
                    symbolSize : node.size, 
                    /*itemStyle : {
                        normal : {
                            color : node.color 
                        }
                    }*/
                };
            }),
            links : json.links.map(function (edge) 
            {
                return {
                    id: edge.id,
                	source : edge.source, 
                    target : edge.target,
                    name: edge.source + ' -> ' + edge.target,
                    attr: edge.attrs.join("<br>"),
                    lineStyle:{
                    	width: edge.size / 2
                    },
                    label:{
                    	normal: {
                        	show: true
                    	}
                    }
                };
            }),
            label : {
            	normal: {
                    show: true,
                    textStyle:{
                    	fontSize: 15
                    }
                }
            },
            roam : true, 
            focusNodeAdjacency : true, 
            lineStyle : {
                normal : {
                    width : 0.5, 
                    curveness : 0.3, 
                    opacity : 0.7 
                }
            },
            edgeLabel: {
                normal: {
                    textStyle: {
                        fontSize: 10
                    }
                }
            }
        } ] 
    }, true);
});

myChart.on('click', clickOnItem);

function clickOnItem(param) {    
    if (typeof param.seriesIndex == 'undefined') {    
        return;    
    }    
    if (param.type == 'click') {    
        alert(param.name);
    }    
} 
</script>

</body>
</html>