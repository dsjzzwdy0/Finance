<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="UTF-8"%>  
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>  
<%  
    String path = request.getContextPath();  
    String basePath = request.getScheme() + "://"  
            + request.getServerName() + ":" + request.getServerPort()  
            + path + "/";  
%>
<table id="gridTable">
	<thead>
		<tr>
			<th>编号</th>
			<th>代码</th>
			<th>交易日期</th>
			<th>股票名称</th>
			<th>小额总额</th>
		</tr>
	</thead>
	<tbody>
		<c:forEach var="v" items="${page}">  
		    <tr bgcolor="#FFFFFF" id="${v.id}">  
		        <td><div align="center">  
		                <input type="checkbox" value="${v.id}" class="subBox" name="aaa"  />  
		            </div></td>  
		        <td><div align="center">${v.symbol}</div></td>  
		        <td><div align="center">${v.day}</div></td>  
		        <td><div align="center">${v.name}</div></td>  
		        <td><div align="center">${v.smalltotal}</div></td>
		    </tr>  
		</c:forEach>
	</tbody>
</table>