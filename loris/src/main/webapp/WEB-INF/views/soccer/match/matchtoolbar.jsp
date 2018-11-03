<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<link rel="stylesheet" type="text/css" href="../content/css/soccer/bfyc.css" />

<div class="minibars">
	<span class="location">当前位置：</span>
<<<<<<< HEAD
	<a href="http://www.zgzcw.com/">东方足彩网</a>
=======
	<a href="http://www.zgzcw.com/">中国足彩网</a>
>>>>>>> 9b450c39b7c085402877e394d4583d6f2ceaf855
	<span class="dotline">&gt;</span>
	<a ref="http://saishi.zgzcw.com/soccer">赛事中心</a> 
	<span class="dotline">&gt;</span>
	<a href="league?lid=${match.lid}&season=${match.season}">${match.leaguename }&nbsp;${match.season}</a>
	<span class="dotline">&gt;</span> &nbsp;${match.homename } VS
	${match.clientname }
</div>

<div class="bfyc-header marb10">
	<ul>
		<li class="<c:if test="${page=='bjop'}">cur</c:if>"><a href="match?type=bjop&mid=${match.mid}">百家欧赔</a></li>
		<li class="<c:if test="${page=='ypdb'}">cur</c:if>"><a href="match?type=ypdb&mid=${match.mid}">亚盘对比</a></li>
		<li class="<c:if test="${page=='opvar'}">cur</c:if>"><a href="match?type=opvar&mid=${match.mid}">欧赔分析</a></li>
		<li class="<c:if test="${page=='dxdb'}">cur</c:if>"><a href="match?type=dxdb&mid=${match.mid}">大小对比</a></li>
		<li class="<c:if test="${page=='bfyc'}">cur</c:if>"><a href="match?type=bfyc&mid=${match.mid}" name="anchor-top">八方预测</a></li>
		<li id="bsls" class="<c:if test="${page=='bsls'}">cur</c:if>"><a href="match?type=bsls&mid=${match.mid}">比赛历史</a></li>
		<li class="" style="position: relative;">
			<s style="position: absolute; background: url(..content/images/new.png) no-repeat; display: block; width: 20px; height: 22px; top: -20px; right: -10px;">
			</s>
			<a href="match?type=qpql&mid=${match.mid}">球路盘路</a>
		</li>
		<li class=""><a href="match?type=sfzs&mid=${match.mid}">胜负走势</a></li>
		<li class=""><a href="match?type=zjtz&mid=${match.mid}">战绩特征</a></li>
		<li class=""><a href="match?type=zrtj&mid=${match.mid}">阵容统计</a></li>
		</li>
	</ul>
</div>

<div class="bfyc-duizhen marb10" style="display: block;">
	<div class="bfyc-duizhen-l">
		<div class="paiming-normal">
			<div class="nromal-con" id="hg_top">
				<dl class="first">
					<dt>排名</dt>
					<dd class="team">球队</dd>
					<dd class="sai">赛</dd>
					<dd class="win">胜</dd>
					<dd class="draw">平</dd>
					<dd class="lost">负</dd>
					<dd class="sorce">分</dd>
				</dl>
				<c:forEach var="r" items="${rank.ranks}">
					<c:if test="${r.type=='total'}">
						<dl>
							<dt>${r.rank }</dt>
							<dd class="team">
								<a href="http://saishi.zgzcw.com/soccer/team/185" target="_blank">${r.name }</a>
							</dd>
							<dd class="sai">${r.gamenum }</dd>
							<dd class="win">${r.winnum }</dd>
							<dd class="draw">${r.drawnum }</dd>
							<dd class="lost">${r.losenum }</dd>
							<dd class="sorce">${r.score }</dd>
						</dl>					
					</c:if>				
				</c:forEach>
				<div class="dropdown"></div>
			</div>
			<div class="paiming-beisai" id="scoreTop" style="display: none;">
				<div class="paiming-beisai-radio">
					<label><input name="score" checked="checked" t="T" type="radio"> 总</label>
					<label><input name="score" t="H" type="radio"> 主场</label>
					<label><input name="score" t="G" type="radio"> 客场</label>
				</div>
				<div class="con">
					<dl class="first">
						<dt>排名</dt>
						<dd class="team">球队</dd>
						<dd class="sai">赛</dd>
						<dd class="win">胜</dd>
						<dd class="draw">平</dd>
						<dd class="lost">负</dd>
						<dd class="sorce">分</dd>
					</dl>
				</div>
				<div class="con" id="s_T" style="display:block">
					<c:forEach var="r" items="${rank.ranks}">
						<c:if test="${r.type=='total'}">
							<dl>
								<dt>${r.rank }</dt>
								<dd class="team">
									<a href="http://saishi.zgzcw.com/soccer/team/185" target="_blank">${r.name }</a>
								</dd>
								<dd class="sai">${r.gamenum }</dd>
								<dd class="win">${r.winnum }</dd>
								<dd class="draw">${r.drawnum }</dd>
								<dd class="lost">${r.losenum }</dd>
								<dd class="sorce">${r.score }</dd>
							</dl>					
						</c:if>				
					</c:forEach>
				</div>
				<div class="con" id="s_H" style="display: none">
					<c:forEach var="r" items="${rank.ranks}">
						<c:if test="${r.type=='home'}">
							<dl>
								<dt>${r.rank }</dt>
								<dd class="team">
									<a href="http://saishi.zgzcw.com/soccer/team/185" target="_blank">${r.name }</a>
								</dd>
								<dd class="sai">${r.gamenum }</dd>
								<dd class="win">${r.winnum }</dd>
								<dd class="draw">${r.drawnum }</dd>
								<dd class="lost">${r.losenum }</dd>
								<dd class="sorce">${r.score }</dd>
							</dl>					
						</c:if>				
					</c:forEach>
				</div>
				<div class="con" id="s_G" style="display: none">
					<c:forEach var="r" items="${rank.ranks}">
						<c:if test="${r.type=='guest'}">
							<dl>
								<dt>${r.rank }</dt>
								<dd class="team">
									<a href="http://saishi.zgzcw.com/soccer/team/185" target="_blank">${r.name }</a>
								</dd>
								<dd class="sai">${r.gamenum }</dd>
								<dd class="win">${r.winnum }</dd>
								<dd class="draw">${r.drawnum }</dd>
								<dd class="lost">${r.losenum }</dd>
								<dd class="sorce">${r.score }</dd>
							</dl>					
						</c:if>				
					</c:forEach>		
					</div>
			</div>
		</div>
	</div>
	<div class="bfyc-duizhen-m">
		<div class="logoVs">
			<div class="host-name">
				<a href="http://saishi.zgzcw.com/soccer/team/185" target="_blank">${match.homename}</a><br>
				<span> [${match.leaguename }]</span>
			</div>
			<div class="visit-name">
				<a href="http://saishi.zgzcw.com/soccer/team/552" target="_blank">${match.clientname}</a>
				<br style="font-size: 22px;"> <span> [${match.leaguename}]</span>
			</div>
			<div class="vs-score">
				<h1>
					<c:choose>
						<c:when test="${match.homescore>match.clientscore}">
							<span class="h-s bold-r">${match.homescore }</span> <b>-</b> <span class="v-s bold-b">${match.clientscore}</span>
						</c:when>
						<c:when test="${match.homescore<match.clientscore}">
							<span class="h-s bold-r">${match.homescore }</span> <b>-</b> <span class="v-s bold-b">${match.clientscore}</span>
						</c:when>
						<c:when test="${match.homescore==match.clientscore}">
							<span class="h-s bold-r">${match.homescore }</span> <b>-</b> <span class="v-s bold-r">${match.clientscore}</span>
						</c:when>
						<c:otherwise>
							<span class="h-s bold-b"> </span> <b>-</b> <span class="v-s bold-b"> </span>
						</c:otherwise>  
					</c:choose>
					
				</h1>
				<p>(半场：${ match.halfscore})</p>
			</div>
			<div class="host-logo">
				<a href="http://saishi.zgzcw.com/soccer/team/${match.homeid }" target="_blank">
					<img src="getImage?type=team&id=${match.homeid }"> <em>主队</em>
				</a>
			</div>
			<div class="visit-logo">
				<a href="http://saishi.zgzcw.com/soccer/team/${match.clientid }" target="_blank">
					<img src="getImage?type=team&id=${match.clientid }"><em>客队</em>
				</a>
			</div>

			<div class="team-add-info">
				<div class="team-add-info-zd">本赛季排名：${rank.htotalrank.rank }</div>
				<div class="zhonglichang">&nbsp;&nbsp;</div>
				<div class="team-add-info-kd">本赛季排名：${rank.ctotalrank.rank }</div>
			</div>
			<div class="team-info">
				<div class="team-info-h">主队：${rank.htotalrank.winnum }胜 ${rank.htotalrank.drawnum }平 ${rank.htotalrank.losenum }负 ${rank.htotalrank.score }分</div>
				<div class="weather">
					<span class="weiyu" title="微雨"></span>
				</div>
				<div class="team-info-v">客队：${rank.ctotalrank.winnum }胜 ${rank.ctotalrank.drawnum }平 ${rank.ctotalrank.losenum }负 ${rank.ctotalrank.score }分</div>
			</div>

		</div>
	</div>
	<div class="bfyc-duizhen-r">
		<div class="date">
			<span>比赛时间：${match.matchtime}</span>
		</div>
		<div class="changdi"></div>
		<div class="round" id="lc_head">
			<h2 pid="2286943" p="play" type="head" t="saicheng" lc="26" d="bjop">
				<p>
					${match.leaguename }&nbsp;第<i id="now_head">${match.round }</i>轮
				</p>
				<div class="round-con">
					<div class="nextPre">
						<a href="javascript:void%200" id="head_pre" type="head">上一轮</a> <a
							href="javascript:void%200" id="head_next" type="head">下一轮</a>
					</div>
					<ul class="tit">
						<li><i class="index">序号</i><span class="tr">主队</span><i
							class="bifen">比分</i><span class="tl">客队</span></li>
					</ul>
					<div class="lunci-scroll"></div>
				</div>
			</h2>
		</div>
		<ul class="play-3" id="lc_headr">
			<!-- 鼠标滑过li标签增加hover样式 -->
			<li class="first" pid="2286943" p="play" t="jingcai" lc="2018-02-24"
				d="bjop" type="hjc">
				<p>竞彩</p>
				<div class="play-con">
					<div class="nextPre">
						<!--<a href="javascript:void 0" id="hjc_pre" type='hjc'>上一期</a>-->
						第<span id="now_hjc">2018-02-24</span>期
						<!--<a href="javascript:void 0" id="hjc_next" type='hjc'>下一期</a>-->
					</div>
					<ul class="tit">
						<li><i class="index">序号</i><span class="tr">主队</span><i
							class="bifen">比分</i><span class="tl">客队</span></li>
					</ul>
					<div class="lunci-scroll"></div>
				</div>
			</li>
			<li class="second" pid="2286943" p="play" t="baidan" lc="80204"
				d="bjop" type="hbd">
				<p>北单</p>
				<div class="play-con">
					<div class="nextPre">
						<!--<a href="javascript:void 0" id="hbd_pre" type='hbd'>上一期</a>-->
						第<span id="now_hbd">80204</span>期
						<!--<a href="javascript:void 0" id="hbd_next" type='hbd'>下一期</a>-->
					</div>
					<ul class="tit">
						<li><i class="index">序号</i><span class="tr">主队</span><i
							class="bifen">比分</i><span class="tl">客队</span></li>
					</ul>
					<div class="lunci-scroll"></div>
				</div>
			</li>
			<li class="third" pid="2286943" p="play" t="zucai" lc="2018026"
				d="bjop" type="hzc">
				<p>足彩</p>
				<div class="play-con">
					<div class="nextPre">
						<!--<a href="javascript:void 0" id="hzc_pre" type='hzc'>上一期</a>-->
						第<span id="now_hzc">2018026</span>期
						<!--<a href="javascript:void 0" id="hzc_next" type='hzc'>下一期</a>-->
					</div>
					<ul class="tit">
						<li><i class="index">序号</i><span class="tr">主队</span><i
							class="bifen">比分</i><span class="tl">客队</span></li>
					</ul>
					<div class="lunci-scroll"></div>
				</div>
			</li>
		</ul>
	</div>
</div>