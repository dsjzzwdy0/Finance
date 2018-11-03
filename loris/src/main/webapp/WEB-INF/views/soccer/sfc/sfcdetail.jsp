<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!--表头-->
<div class="head">
	<!--比赛筛选-->
	<div class="head_1" id="filterSh" hover="sai" hover-delay="0.3">
		<div class="head_1 hover1">
			<p class="float_l sias_p1">赛事</p>
			<span class="float_l icon hover1_1"></span>
		</div>
		<div class="saixuan hover1_2" id="filters">
			<div class="rangqiu">
				<strong>让球</strong>
				<ul>
					<c:forEach var="r" items="${rates}">
						<li><label> 
							<input type="checkbox" checked="checked" filter="rq" value="${r.key}" class="rqfilterObj"> 
							让${r.key}球 <a>[${r.value}场]</a>
						</label></li>
					</c:forEach>
				</ul>
			</div>
			<div class="clear"></div>
			<div class="rangqiu">
				<p class="saishi">赛事</p>
				<p id="filter5dLx" style="width: 49px; margin-left: 181px;">五大联赛</p>
				<p id="filterAlLs">全选</p>
				<p id="filterFxLx">反选</p>
				<p id="filterQqLx">全清</p>
				<ul id="filterls">
					<li><label>
						<input type="checkbox" checked="checked" value="17" filter="ss" class="rqfilterObj"> 英超 <a>[10场]</a>
					</label></li>
					<li><label>
						<input type="checkbox" checked="checked" value="14" filter="ss" class="rqfilterObj"> 未知 <a>[74场]</a>
					</label></li>
					<li><label> 
						<input type="checkbox" checked="checked" value="326" filter="ss" class="rqfilterObj"> 比利时杯 <a>[2场]</a>
					</label></li>
					<li><label>
						<input type="checkbox" checked="checked" value="333" filter="ss" class="rqfilterObj"> 法联杯 <a>[2场]</a>
					</label></li>
					
				</ul>
			</div>
			<div class="clear"></div>
			<div class="rangqiu">
				<strong>最低赔率</strong>
				<ul>
					<li>
						<input id="pl1" type="checkbox" checked="checked" filter="pl" value="0,1.5" class="rqfilterObj">
						<label for="pl1">1.50以下 <a>[17场]</a></label>
					</li>
					<li>
						<input id="pl2" type="checkbox" checked="checked" filter="pl" value="1.5,2" class="rqfilterObj">
						<label for="kr1">1.50~2.00 <a>[15场]</a>	</label>
					</li>
					<li>
						<input id="pl3" type="checkbox" checked="checked" filter="pl" value="2,0" class="rqfilterObj"> 
						<label for="pl3">2.00以上 <a>[20场]</a></label>
					</li>
				</ul>
			</div>
			<div class="clear"></div>
			<div class="yingcang" style="display: none">
				已隐藏<i>&nbsp;0&nbsp;</i>场
				<div class="huifu color">恢复</div>
			</div>
		</div>
	</div>
	<!--比赛筛选-->
	<div class="head_2" hover="ting" hover-delay="0.3" select="ts"
		id="selectts">
		<div class="head_2 hover1">
			<label select-title="">停售</label> <span
				class="icon hover1_12 t_hover"></span>
		</div>
		<div id="bisais">
			<div class="bisai bi" hover="bg_hover">开赛</div>
			<div class="bisai bi" hover="bg_hover" style="display: none;">停售</div>
		</div>
	</div>
	<ul class="head_3">
		<li class="zhu">主</li>
		<li class="ping">平</li>
		<li class="ke">客 <span id="ifzoushi" class="icon b1"
			title="奖金变化列表"></span>
		</li>
	</ul>
	<div class="head_7">分析</div>
	<ul class="head_4">
		<li class="zhu">让球主胜</li>
		<li class="ping">让球平</li>
		<li class="ke">让球主负</li>
	</ul>
	<div class="head_5" id="gengduo">更多</div>
	<div id="sellie" class="head_6" hover="nine_hover" hover-delay="0.3">
		<div class="wite">
			<label>99家平均欧赔</label> <span class="icon sanjiao"></span>
		</div>
		<div class="nine nine_cont">
			<div class="nine_1">
				<div class="ni_1">赔率盘口：</div>
				<div class="ni_2">
					<div class="dan_2" style="margin-top: 17px;">
						<div id="op" class="shu blue" hover="col_1">
							<p ttype="1" class="float_l">99家平均</p>
							<p class="float_l su_w">欧赔</p>
						</div>
						<ul class="xia_l" style="">
							<li hover="li_bg" sel="li_bg2" data-typeid="1" data-pid="24"
								sel-group="a" sel-essential="1" class="li_bg2">99家平均</li>
							<li hover="li_bg" sel="li_bg2" data-typeid="1" data-pid="14"
								sel-group="a" sel-essential="1">威廉希尔</li>
							<li hover="li_bg" sel="li_bg2" data-typeid="1" data-pid="82"
								sel-group="a" sel-essential="1">立博</li>
							<li hover="li_bg" sel="li_bg2" data-typeid="1" data-pid="27"
								sel-group="a" sel-essential="1">bet365</li>
							<li hover="li_bg" sel="li_bg2" data-typeid="1" data-pid="94"
								sel-group="a" sel-essential="1">bwin</li>
							<li hover="li_bg" sel="li_bg2" data-typeid="1" data-pid="84"
								sel-group="a" sel-essential="1">澳门彩票</li>
							<li hover="li_bg" sel="li_bg2" data-typeid="1" data-pid="322"
								sel-group="a" sel-essential="1">金宝博</li>
							<li hover="li_bg" sel="li_bg2" data-typeid="1" data-pid="35"
								sel-group="a" sel-essential="1">易胜博</li>
							<li hover="li_bg" sel="li_bg2" data-typeid="1" data-pid="250"
								sel-group="a" sel-essential="1">皇冠</li>
							<li hover="li_bg" sel="li_bg2" data-typeid="1" data-pid="280"
								sel-group="a" sel-essential="1">利记</li>
						</ul>
					</div>
					<div class="dan_2">
						<div id="yp" class="shu" hover="col_1">
							<p ttype="2" class="float_l"></p>
							<p class="float_l su_w">亚盘</p>
						</div>
						<ul class="xia_l" style="">
							<li style="color: #ccc;">99家平均</li>
							<li style="color: #ccc;">威廉希尔</li>
							<li hover="li_bg" sel="li_bg2" data-typeid="2" data-pid="82"
								sel-group="a" sel-essential="1">立博</li>
							<li hover="li_bg" sel="li_bg2" data-typeid="2" data-pid="27"
								sel-group="a" sel-essential="1">bet365</li>
							<li hover="li_bg" sel="li_bg2" data-typeid="2" data-pid="94"
								sel-group="a" sel-essential="1">bwin</li>
							<li hover="li_bg" sel="li_bg2" data-typeid="2" data-pid="84"
								sel-group="a" sel-essential="1">澳门彩票</li>
							<li hover="li_bg" sel="li_bg2" data-typeid="2" data-pid="322"
								sel-group="a" sel-essential="1">金宝博</li>
							<li hover="li_bg" sel="li_bg2" data-typeid="2" data-pid="35"
								sel-group="a" sel-essential="1">易胜博</li>
							<li hover="li_bg" sel="li_bg2" data-typeid="2" data-pid="250"
								sel-group="a" sel-essential="1">皇冠</li>
							<li hover="li_bg" sel="li_bg2" data-typeid="2" data-pid="280"
								sel-group="a" sel-essential="1">利记</li>
						</ul>
					</div>
				</div>
			</div>
			<div class="nine_1">
				<div class="ni_1">球队特征：</div>
				<ul class="ni_3">
					<li hover="col_1" sel="li_bg" data-typeid="3" data-pid="0"
						sel-group="a" sel-essential="1">连胜连败</li>
					<li hover="col_1" sel="li_bg" data-typeid="5" data-pid="0"
						sel-group="a" sel-essential="1">伤停状况</li>
				</ul>
			</div>
			<div class="nine_2">
				<div class="ni_1">本站特色：</div>
				<ul class="ni_3">
					<li hover="col_1" sel="li_bg" data-typeid="6" data-pid="0"
						sel-group="a" sel-essential="1">保存量</li>
					<li hover="col_1" sel="li_bg" data-typeid="7" data-pid="0"
						sel-group="a" sel-essential="1">保存人数</li>
					<li hover="col_1" sel="li_bg" data-typeid="8" data-pid="0"
						sel-group="a" sel-essential="1">澳客指数</li>
					<li hover="col_1" sel="li_bg" data-typeid="9" data-pid="0"
						sel-group="a" sel-essential="1">差异指数</li>
				</ul>
			</div>
		</div>
	</div>
</div>
<div class="tings">
	<label> 
		<input id="showEndMatch" type="checkbox" class="input">显示已停售比赛 <a style="color: #999">[27场]</a>
	</label>
</div>
<div class="scrollnotice_box" data-pagetype="jingcai">
	<div class="scrollnotice_container" style="top: -44px;">
		<ul class="scrollnotice">
			<li><a
				href="http://www.okooo.com/news/tags/other_161220190852415/"
				target="_blank">周二017比赛推迟&nbsp;2018-01-30 08:52 </a></li>
			<li><a
				href="http://www.okooo.com/news/tags/other_161220190852415/"
				target="_blank">周六021比赛取消&nbsp;2018-01-29 11:10 </a></li>
		</ul>
	</div>
</div>
<!--表头结束-->
<!--内容开始-->
<div class="cont" style="z-index: 20;">
	<div class="cont_1">
		<div class="shouqi" onclick="shouqi(this);">
			<span class="icon so_h"></span>
		</div>
		<div class="riqi">
			<div id="ChangeDate" class="time" data-time="1517241600">
				<a class="a5 a1"> <span class="float_l">${issue}&nbsp;${week}</span>
					<span class="float_l icon a2 a3"></span>
				</a>
				<ul class="time_sel block">
					<c:forEach var="u" items="${issues}">
						<li hover="li_bg_1"><a href="sfc?issue=${u}">${u}</a></li>
					</c:forEach>
					<!--
					<li hover="li_bg_1" class="li_bg_2"><a href="http://www.okooo.com/jingcai/">2018-01-30 星期二</a></li>
					<li hover="li_bg_1"><a href="http://www.okooo.com/jingcai/2018-01-29/">2018-01-29 星期一</a></li>
					<li hover="li_bg_1"><a href="http://www.okooo.com/jingcai/2018-01-28/">2018-01-28 星期日</a></li>
					<li hover="li_bg_1"><a href="http://www.okooo.com/jingcai/2018-01-27/">2018-01-27 星期六</a></li>
					<li hover="li_bg_1"><a href="http://www.okooo.com/jingcai/2018-01-26/">2018-01-26 星期五</a></li>
					<li hover="li_bg_1"><a href="http://www.okooo.com/jingcai/2018-01-25/">2018-01-25 星期四</a></li>
					<li hover="li_bg_1"><a href="http://www.okooo.com/jingcai/2018-01-24/">2018-01-24 星期三</a></li>
					<li hover="li_bg_1"><a href="http://www.okooo.com/jingcai/2018-01-23/">2018-01-23 星期二</a></li>
					  -->
				</ul>
			</div>
		</div>
		<!--div class="explain"><span>27</span>场可投注比赛</div-->
	</div>
	<div id="sfc_all_list" class="touzhu">
		<c:forEach var="v" items="${matches}">
			<div class="touzhu_1" 
				hover="hover2" 
				data-end="0" 
				id="match_${v.mid}"
				data-mid="${v.mid}" 
				data-morder="${v.ordinary}" 
				data-ordercn="周四003"
				data-rq="1" 
				filterdata="${v.rangqiu},${v.lid},${v.winodds}" 
				data-hname="${v.homename }" 
				data-aname="${v.clientname }"
				data-rev="N">
				<div class="liansai">
					<span class="xulie" hover="xulie_h" title="周四003">${v.ordinary}</span> 
					<a class="saiming aochao" style="background-color: #BF9BB8;"
						onclick="javascript:void(0);"
						href="glfx?issue=${v.issue }&lid=${v.lid}" title="${v.leaguename}"
						target="_blank">${v.leaguename} </a>
					<div class="shijian" mtime="${v.matchtime}" title="比赛时间:${v.matchtime}">${v.matchhour}</div>
				</div>
				<div class="shenpf ">
					<div style="position: relative; z-index: 0;">
						<div class="zhu 
						<c:choose>
							<c:when test="${!v.isopen}">weiks</c:when>
						</c:choose>
						" hover="hover1" choose="sel1" data-sp="3.40"
							data-wf="0" data-wz="0" data-xx="16" data-dg="0"
							data-mid="${v.mid}">
							<span class="icon zuo1 hover1_1 sel1_1"></span>
							<div class="zhud hover1_2 sel1_2 ">
								<div class="paim paim_sel">
									<p class="p2">[${v.homerank }]</p>
									<p class="p1">${v.homelid }</p>
								</div>
								<div class="zhum fff hui_colo" title="${v.homename }">${v.homename }</div>
								<div class="peilv fff hui_colo red_colo">${v.winodds }</div>
							</div>
							<span class="icon you1 hover1_3 sel1_3"></span>
						</div>
						<div class="ping 
						<c:choose>
							<c:when test="${!v.isopen}">weiks</c:when>
						</c:choose>
						" hover="hover11" choose="sel1" data-sp="${v.drawodds }"
							data-wf="0" data-wz="1" data-xx="15" data-dg="0"
							data-mid="${v.mid}">
							<span class="icon zuo1 hover1_1 sel1_1"></span>
							<div class="pingd hover1_2 sel1_2 ">
								<div class="peilv fff hui_colo red_colo">
								<c:choose>
									<c:when test="${v.isopen}">${v.drawodds }</c:when>
									<c:otherwise>未开售</c:otherwise>
								</c:choose>
								</div>
							</div>
							<span class="icon you1 hover1_3 sel1_3"></span>
						</div>
						<div class="fu 
						<c:choose>
							<c:when test="${!v.isopen}">weiks</c:when>
						</c:choose>
						" hover="hover11" choose="sel1" data-sp="${v.loseodds }"
							data-wf="0" data-wz="2" data-xx="14" data-dg="0"
							data-mid="${v.mid}">
							<span class="icon zuo1 hover1_1 sel1_1"></span>
							<div class="ked hover1_2 sel1_2 ">
								<div class="peilv fff hui_colo red_colo">${v.loseodds }</div>
								<div class="paim paim_sel">
									<p class="p2">[${v.clientrank }]</p>
									<p class="p1">${v.clientlid }</p>
								</div>
								<div class="zhum fff hui_colo " title="${v.clientname }">${v.clientname }</div>
							</div>
							<span class="icon you1 hover1_3 sel1_3"></span>
						</div>
						
						<c:choose>
							<c:when test="${!v.isopen}"><div class="spfweik"><div class="spfweik_1">未开售</div></div></c:when>
						</c:choose>
					</div>
				</div>
				<div class="fengxin1">
					<a class="ao" onclick="_gaq.push();"
						href="bjop?mid=${v.mid }"
						target="_blank">欧</a> 
					<a class="ao" onclick="_gaq.push();"
						href="../matchanalysis.html?lid=${v.lid}&issue=${v.issue}"
						target="_blank">析</a>
				</div>
				<div class="rangqiuspf ">
					<div style="position: relative; z-index: 0">
						<div class="zhu " hover="hover1" choose="sel1" data-sp="${v.rqwinodds }"
							data-wf="1" data-wz="0" data-xx="13" data-dg="0"
							data-mid="${v.mid}">
							<span class="icon zuo1 hover1_1 sel1_1"></span>
							<div class="zhud hover1_2 sel1_2 ">
								<div class="zhum fff hui_colo" title="${v.homename }">${v.homename }</div>
								<span class="rangqiuzhen"> ${v.rangqiu } </span>
								<div class="peilv fff hui_colo red_colo">${v.rqwinodds }</div>
							</div>
							<span class="icon you1 hover1_3 sel1_3"></span>
						</div>
						<div class="ping " hover="hover11" choose="sel1" data-sp="${v.rqdrawodds }"
							data-wf="1" data-wz="1" data-xx="11" data-dg="0"
							data-mid="${v.mid}">
							<span class="icon zuo1 hover1_1 sel1_1"></span>
							<div class="pingd hover1_2 sel1_2 ">
								<div class="peilv fff hui_colo red_colo">${v.rqdrawodds }</div>
							</div>
							<span class="icon you1 hover1_3 sel1_3"></span>
						</div>
						<div class="fu " hover="hover11" choose="sel1" data-sp="${v.rqloseodds }"
							data-wf="1" data-wz="2" data-xx="10" data-dg="0"
							data-mid="${v.mid}">
							<span class="icon zuo1 hover1_1 sel1_1"></span>
							<div class="zhud hover1_2 sel1_2 ">
								<div class="peilv fff hui_colo red_colo">${v.rqloseodds }</div>
								<div class="zhum fff hui_colo" title="${v.clientname }">${v.clientname }</div>
							</div>
							<span class="icon you1 hover1_3 sel1_3"></span>
						</div>
					</div>
				</div>
				<!--更多-->
				<div class="more " hover="hover1" rsel="gengd_weiks">
					<span class="icon zuo1 hover1_1 sel1_1"></span>
					<div class="more_bg hover1_2 sel1_2">
						<p class="p1 count"></p>
						<p class="icon p2 sel1_31"></p>
					</div>
					<span class="icon you1 hover1_3 sel1_3"></span>
				</div>
				<!--未开售-->
				<div class="more_weik" style="display: none">
					<div class="zi">未开售</div>
					<span class="icon"></span>
				</div>
				<div class="zhishu">
					<div class="zz1">
						<span class="zhishu_4">3.45</span>
						<span class="zhishu_4">2.91</span>
						<span class="zhishu_4">2.21</span>
					</div>
				</div>
			</div>
			<div class="clear"></div>
		</c:forEach>
	</div>
</div>


