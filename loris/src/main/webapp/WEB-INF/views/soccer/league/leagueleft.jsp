<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!--  左侧页面 -->
<div class="left">
	<div class="left_head">
		<dl class="head_dl">
			<dt><img src="../content/images/yijia/20130120113554.jpg" width="100" height="100" border="0"></dt>
			<dd>[${league.name}]</dd>
		</dl>
		<div class="team_out">
			<div class="team_lian">
				<span>赛事选择</span>
				<div class="leagueSelBox clearfix" style="">
					<div class="xuanze">
						<div class="ssxz"></div>
						<!-- <a href=""><span class="guanbi">关闭</span></a> -->
					</div>
					<ul class="left2 chooseHead">
						<li class="nav01" id="nav01" data-id="国际赛事"><em class="icon1"></em>
							国际赛事</li>
						<li class="nav02 crt" id="nav02" data-id="欧洲赛事"><em
							class="icon2"></em> 欧洲赛事</li>
						<li class="nav03" id="nav03" data-id="美洲赛事"><em class="icon3"></em>
							美洲赛事</li>
						<li class="nav04" id="nav04" data-id="亚洲赛事"><em class="icon4"></em>
							亚洲赛事</li>
						<li class="nav05" id="nav05" data-id="非洲赛事"><em class="icon5"></em>
							非洲赛事</li>
					</ul>
					<div class="gamesSelect fl" style="width: 470px;">
						<div class="right2 rlea nav01_content" style="display: none;">
							<dl class="league clearfix">
								<dd data-id="世界杯">
									<a title="世界杯" href="http://saishi.zgzcw.com/soccer/cup/75">世界杯</a>
								</dd>
								<dd data-id="世欧预">
									<a title="世欧预" href="http://saishi.zgzcw.com/soccer/cup/650">世欧预</a>
								</dd>
								<dd data-id="世南预">
									<a title="世南预" href="http://saishi.zgzcw.com/soccer/cup/652">世南预</a>
								</dd>
								<dd data-id="世北预">
									<a title="世北预" href="http://saishi.zgzcw.com/soccer/cup/653">世北预</a>
								</dd>
								<dd>
									<a href="http://saishi.zgzcw.com/soccer/fifaRank"
										target="_blank">FIFA</a>
								</dd>
								<div class="clear"></div>
							</dl>
						</div>
						<div class="right2 rlea nav02_content">
							<dl class="league clearfix">
								<dd>
									<a href="http://saishi.zgzcw.com/soccer/league/36/"
										target="_blank">英超</a>
								</dd>
								<dd>
									<a href="http://saishi.zgzcw.com/soccer/league/37/"
										target="_blank">英冠</a>
								</dd>
								<dd>
									<a href="http://saishi.zgzcw.com/soccer/league/39/"
										target="_blank">英甲</a>
								</dd>
								<dd>
									<a href="http://saishi.zgzcw.com/soccer/league/34/"
										target="_blank">意甲</a>
								</dd>
								<dd>
									<a href="http://saishi.zgzcw.com/soccer/league/40/"
										target="_blank">意乙</a>
								</dd>
								<dd>
									<a href="http://saishi.zgzcw.com/soccer/league/8/"
										target="_blank">德甲</a>
								</dd>
								<dd>
									<a href="http://saishi.zgzcw.com/soccer/league/9/"
										target="_blank">德乙</a>
								</dd>
								<dd>
									<a href="http://saishi.zgzcw.com/soccer/league/31/"
										target="_blank">西甲</a>
								</dd>
								<dd>
									<a href="http://saishi.zgzcw.com/soccer/league/33/"
										target="_blank">西乙</a>
								</dd>
								<dd>
									<a href="http://saishi.zgzcw.com/soccer/league/11/"
										target="_blank">法甲</a>
								</dd>
								<dd>
									<a href="http://saishi.zgzcw.com/soccer/league/12/"
										target="_blank">法乙</a>
								</dd>
								<dd>
									<a href="http://saishi.zgzcw.com/soccer/league/16/"
										target="_blank">荷甲</a>
								</dd>
								<dd>
									<a href="http://saishi.zgzcw.com/soccer/league/17/"
										target="_blank">荷乙</a>
								</dd>
								<dd>
									<a href="http://saishi.zgzcw.com/soccer/league/26/"
										target="_blank">瑞超</a>
								</dd>
								<dd>
									<a href="http://saishi.zgzcw.com/soccer/league/122/"
										target="_blank">瑞甲</a>
								</dd>
								<dd>
									<a href="http://saishi.zgzcw.com/soccer/league/22/"
										target="_blank">挪超</a>
								</dd>
								<dd>
									<a href="http://saishi.zgzcw.com/soccer/league/23/"
										target="_blank">葡超</a>
								</dd>
								<dd>
									<a href="http://saishi.zgzcw.com/soccer/league/157/"
										target="_blank">葡甲</a>
								</dd>
								<dd>
									<a href="http://saishi.zgzcw.com/soccer/league/30/"
										target="_blank">土超</a>
								</dd>
								<dd>
									<a href="http://saishi.zgzcw.com/soccer/league/10/"
										target="_blank">俄超</a>
								</dd>
								<dd>
									<a href="http://saishi.zgzcw.com/soccer/league/13/"
										target="_blank">芬超</a>
								</dd>
								<dd>
									<a href="http://saishi.zgzcw.com/soccer/league/212/"
										target="_blank">芬甲</a>
								</dd>
								<dd>
									<a href="http://saishi.zgzcw.com/soccer/league/29/"
										target="_blank">苏超</a>
								</dd>
								<dd>
									<a href="http://saishi.zgzcw.com/soccer/league/5/"
										target="_blank">比甲</a>
								</dd>
								<dd>
									<a href="http://saishi.zgzcw.com/soccer/league/1/"
										target="_blank">爱超</a>
								</dd>
								<dd>
									<a href="http://saishi.zgzcw.com/soccer/league/135/"
										target="_blank">威超</a>
								</dd>
								<dd>
									<a href="http://saishi.zgzcw.com/soccer/league/235/"
										target="_blank">俄甲</a>
								</dd>
								<dd>
									<a href="http://saishi.zgzcw.com/soccer/league/123/"
										target="_blank">挪甲</a>
								</dd>
								<dd>
									<a href="http://saishi.zgzcw.com/soccer/league/118/"
										target="_blank">以超</a>
								</dd>
								<dd>
									<a href="http://saishi.zgzcw.com/soccer/league/119/"
										target="_blank">乌超</a>
								</dd>
								<dd>
									<a href="http://saishi.zgzcw.com/soccer/league/7/"
										target="_blank">丹超</a>
								</dd>
								<dd>
									<a href="http://saishi.zgzcw.com/soccer/league/32"
										target="_blank">希超</a>
								</dd>
								<dd>
									<a href="http://saishi.zgzcw.com/soccer/cup/103"
										target="_blank">欧冠杯</a>
								</dd>
								<dd>
									<a href="http://saishi.zgzcw.com/soccer/cup/113"
										target="_blank">欧罗巴</a>
								</dd>
								<dd>
									<a href="http://saishi.zgzcw.com/soccer/cup/67" target="_blank">欧洲杯</a>
								</dd>
								<div class="clear"></div>
							</dl>
						</div>
						<div class="right2 rlea nav03_content" style="display: none;">
							<dl class="league clearfix">
								<dd>
									<a href="http://saishi.zgzcw.com/soccer/league/2"
										target="_blank">阿甲 </a>
								</dd>
								<dd>
									<a href="http://saishi.zgzcw.com/soccer/league/423"
										target="_blank">阿乙 </a>
								</dd>
								<dd>
									<a href="http://saishi.zgzcw.com/soccer/league/358"
										target="_blank">巴乙 </a>
								</dd>
								<dd>
									<a href="http://saishi.zgzcw.com/soccer/league/4"
										target="_blank">巴西甲</a>
								</dd>
								<dd>
									<a href="http://saishi.zgzcw.com/soccer/league/21"
										target="_blank">美职联</a>
								</dd>
								<dd>
									<a href="http://saishi.zgzcw.com/soccer/league/415"
										target="_blank">智利甲</a>
								</dd>
								<dd>
									<a href="http://saishi.zgzcw.com/soccer/league/611"
										target="_blank">智利乙</a>
								</dd>
								<dd>
									<a href="http://saishi.zgzcw.com/soccer/league/250"
										target="_blank">哥伦甲</a>
								</dd>
								<dd>
									<a href="http://saishi.zgzcw.com/soccer/league/354"
										target="_blank">巴拉甲</a>
								</dd>
								<dd>
									<a href="http://saishi.zgzcw.com/soccer/league/240"
										target="_blank">乌拉甲</a>
								</dd>
								<dd>
									<a href="http://saishi.zgzcw.com/soccer/league/140"
										target="_blank">墨西联</a>
								</dd>
								<dd>
									<a href="http://saishi.zgzcw.com/soccer/league/242"
										target="_blank">秘鲁甲</a>
								</dd>
								<dd>
									<a href="http://saishi.zgzcw.com/soccer/league/593"
										target="_blank">玻利甲</a>
								</dd>
								<dd>
									<a href="http://saishi.zgzcw.com/soccer/cup/224"
										target="_blank">美洲杯</a>
								</dd>
								<dd>
									<a href="http://saishi.zgzcw.com/soccer/cup/89" target="_blank">自由杯</a>
								</dd>
								<dd>
									<a href="http://saishi.zgzcw.com/soccer/cup/232"
										target="_blank">美金杯</a>
								</dd>
								<dd>
									<a href="http://saishi.zgzcw.com/soccer/cup/263"
										target="_blank">南球杯</a>
								</dd>
								<div class="clear"></div>
							</dl>
						</div>
						<div class="right2 rlea nav04_content" style="display: none;">
							<dl class="league clearfix">
								<dd>
									<a href="http://saishi.zgzcw.com/soccer/league/60"
										target="_blank">中超 </a>
								</dd>
								<dd>
									<a href="http://saishi.zgzcw.com/soccer/league/61"
										target="_blank">中甲 </a>
								</dd>
								<dd>
									<a href="http://saishi.zgzcw.com/soccer/league/25"
										target="_blank">日职 </a>
								</dd>
								<dd>
									<a href="http://saishi.zgzcw.com/soccer/league/284"
										target="_blank">日乙 </a>
								</dd>
								<dd>
									<a href="http://saishi.zgzcw.com/soccer/league/15"
										target="_blank">韩职联</a>
								</dd>
								<dd>
									<a href="http://saishi.zgzcw.com/soccer/league/273"
										target="_blank">澳超 </a>
								</dd>
								<dd>
									<a href="http://saishi.zgzcw.com/soccer/league/279"
										target="_blank">伊朗超</a>
								</dd>
								<dd>
									<a href="http://saishi.zgzcw.com/soccer/league/292"
										target="_blank">沙特联</a>
								</dd>
								<dd>
									<a href="http://saishi.zgzcw.com/soccer/league/332"
										target="_blank">阿曼联</a>
								</dd>
								<dd>
									<a href="http://saishi.zgzcw.com/soccer/league/1078"
										target="_blank">约旦甲</a>
								</dd>
								<dd>
									<a href="http://saishi.zgzcw.com/soccer/league/766"
										target="_blank">越南联</a>
								</dd>
								<dd>
									<a href="http://saishi.zgzcw.com/soccer/league/772"
										target="_blank">乌兹超</a>
								</dd>
								<dd>
									<a href="http://saishi.zgzcw.com/soccer/league/1122"
										target="_blank">印尼超</a>
								</dd>
								<dd>
									<a href="http://saishi.zgzcw.com/soccer/cup/87" target="_blank">足协杯</a>
								</dd>
								<dd>
									<a href="http://saishi.zgzcw.com/soccer/cup/162"
										target="_blank">日皇杯</a>
								</dd>
								<dd>
									<a href="http://saishi.zgzcw.com/soccer/cup/72" target="_blank">日联杯</a>
								</dd>
								<div class="clear"></div>
							</dl>
						</div>
						<div class="right2 rlea nav05_content" style="display: none;">
							<dl class="league clearfix">
								<dd data-id="阿尔甲">
									<a title="阿尔甲" href="http://saishi.zgzcw.com/soccer/league/193">阿尔甲</a>
								</dd>
								<dd data-id="利比亚甲">
									<a title="利比亚甲"
										href="http://saishi.zgzcw.com/soccer/league/324">利比亚甲</a>
								</dd>
								<dd data-id="南非超">
									<a title="南非超" href="http://saishi.zgzcw.com/soccer/league/308">南非超</a>
								</dd>
								<dd data-id="尼日超">
									<a title="尼日超" href="http://saishi.zgzcw.com/soccer/league/949">尼日超</a>
								</dd>
								<div class="clear"></div>
							</dl>
						</div>
					</div>
				</div>
			</div>
			<div class="div-select hoverSelect">
				<span>赛季选择</span>
				<ul class="select_options">
					<c:forEach var="r" items="${seasons}">
						<a href="league?lid=34&season=${r.season}">
							<li>${r.season}</li>
						</a>
					</c:forEach>
				</ul>
			</div>
		</div>
	</div>
	<div class="tongji_list">
		<div>赛事统计</div>
		<ul>
			<li class="cur"><a
				href="http://saishi.zgzcw.com/soccer/league/34"> 赛程信息</a></li>
			<li><a
				href="http://saishi.zgzcw.com/soccer/league/34/2016-2017/ssb">射手榜</a></li>
			<li><a
				href="http://saishi.zgzcw.com/soccer/league/34/2016-2017/jqds">总进球单双</a></li>
			<li><a
				href="http://saishi.zgzcw.com/soccer/league/34/2016-2017/sxds">上下单双</a></li>
			<li><ahref="http://saishi.zgzcw.com/soccer/league/34/2016-2017/sxpl">上下盘路榜</a></li>
		</ul>
	</div>
	<div class="tongji_list">
		<div>球队列表</div>
		<ul>
			<c:forEach var="t" items="${teams}">		
			<a href="http://saishi.zgzcw.com/soccer/team/${t.tid }" target="_blank"
				class="a1" title="${t.name }">
				<li>${t.name}</li>
			</a>
			</c:forEach>
		</ul>
	</div>
</div>