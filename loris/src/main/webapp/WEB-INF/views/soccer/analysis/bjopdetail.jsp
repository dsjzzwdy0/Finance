<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<div class="minibars">
	<span class="location">当前位置：</span> <a href="http://www.zgzcw.com/">中国足彩网</a>
	<span class="dotline">&gt;</span> <a
		href="http://saishi.zgzcw.com/soccer">赛事中心</a> <span class="dotline">&gt;</span>
	<a href="http://saishi.zgzcw.com/soccer/league/34/2017-2018">${match.leaguename }&nbsp;${match.season}</a>
	<span class="dotline">&gt;</span> &nbsp;${match.homename } VS
	${match.clientname }
</div>

<div class="bfyc-header marb10">
	<ul>
		<li class="cur"><a href="http://fenxi.zgzcw.com/2286943/bjop">百家欧赔</a></li>
		<li class=""><a href="http://fenxi.zgzcw.com/2286943/ypdb">亚盘对比</a></li>
		<li class=""><a href="http://fenxi.zgzcw.com/2286943/dxdb">大小对比</a></li>
		<li class=""><a href="http://fenxi.zgzcw.com/2286943/bfyc"
			name="anchor-top">八方预测</a></li>
		<li id="bsls" class=""><a
			href="http://fenxi.zgzcw.com/2286943/bsls">比赛历史</a></li>
		<li class="" style="position: relative;"><s
			style="position: absolute; background: url(http://public.zgzcw.com/shared/new.png) no-repeat; display: block; width: 20px; height: 22px; top: -20px; right: -10px;"></s>
			<a href="http://fenxi.zgzcw.com/2286943/qlpl">球路盘路</a></li>
		<li class=""><a href="http://fenxi.zgzcw.com/2286943/sfzs">胜负走势</a></li>
		<li class=""><a href="http://fenxi.zgzcw.com/2286943/zjtz">战绩特征</a></li>
		<li class=""><a href="http://fenxi.zgzcw.com/2286943/zrtj">阵容统计</a></li>
		<li id="cmyc" class=""><a
			href="http://fenxi.zgzcw.com/2286943/cmyc">彩民预测</a></li>
		<li class=""><a href="http://fenxi.zgzcw.com/2286943/qbz">情报站</a>
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
				<dl>
					<dt>12</dt>
					<dd class="team">
						<a href="http://saishi.zgzcw.com/soccer/team/185" target="_blank">博洛尼亚</a>
					</dd>
					<dd class="sai">26</dd>
					<dd class="win">10</dd>
					<dd class="draw">3</dd>
					<dd class="lost">13</dd>
					<dd class="sorce">33</dd>
				</dl>
				<dl>
					<dt>13</dt>
					<dd class="team">
						<a href="http://saishi.zgzcw.com/soccer/team/552" target="_blank">热那亚</a>
					</dd>
					<dd class="sai">26</dd>
					<dd class="win">8</dd>
					<dd class="draw">6</dd>
					<dd class="lost">12</dd>
					<dd class="sorce">30</dd>
				</dl>
				<div class="dropdown"></div>
			</div>
			<div class="paiming-beisai" id="scoreTop" style="display: none;">
				<div class="paiming-beisai-radio">
					<label><input name="score" checked="checked" t="T"
						type="radio"> 总</label> <label><input name="score" t="H"
						type="radio"> 主场</label> <label><input name="score" t="G"
						type="radio"> 客场</label>
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
				<div class="con" id="s_T" style="display:">
					<dl id="" class="">
						<dt>1</dt>
						<dd class="team">
							<a href="http://saishi.zgzcw.com/soccer/team/1419"
								target="_blank">那不勒斯</a>
						</dd>
						<dd class="sai">25</dd>
						<dd class="win">21</dd>
						<dd class="draw">3</dd>
						<dd class="lost">1</dd>
						<dd class="sorce">66</dd>
					</dl>
					<dl id="" class="">
						<dt>2</dt>
						<dd class="team">
							<a href="http://saishi.zgzcw.com/soccer/team/166" target="_blank">尤文图斯</a>
						</dd>
						<dd class="sai">25</dd>
						<dd class="win">21</dd>
						<dd class="draw">2</dd>
						<dd class="lost">2</dd>
						<dd class="sorce">65</dd>
					</dl>
					<dl id="">
						<dt>3</dt>
						<dd class="team">
							<a href="http://saishi.zgzcw.com/soccer/team/182" target="_blank">拉齐奥</a>
						</dd>
						<dd class="sai">26</dd>
						<dd class="win">16</dd>
						<dd class="draw">4</dd>
						<dd class="lost">6</dd>
						<dd class="sorce">52</dd>
					</dl>
					<dl id="">
						<dt>4</dt>
						<dd class="team">
							<a href="http://saishi.zgzcw.com/soccer/team/152" target="_blank">国际米兰</a>
						</dd>
						<dd class="sai">26</dd>
						<dd class="win">14</dd>
						<dd class="draw">9</dd>
						<dd class="lost">3</dd>
						<dd class="sorce">51</dd>
					</dl>
					<dl id="">
						<dt>5</dt>
						<dd class="team">
							<a href="http://saishi.zgzcw.com/soccer/team/174" target="_blank">罗马</a>
						</dd>
						<dd class="sai">25</dd>
						<dd class="win">15</dd>
						<dd class="draw">5</dd>
						<dd class="lost">5</dd>
						<dd class="sorce">50</dd>
					</dl>
					<dl id="">
						<dt>6</dt>
						<dd class="team">
							<a href="http://saishi.zgzcw.com/soccer/team/179" target="_blank">桑普</a>
						</dd>
						<dd class="sai">26</dd>
						<dd class="win">13</dd>
						<dd class="draw">5</dd>
						<dd class="lost">8</dd>
						<dd class="sorce">44</dd>
					</dl>
					<dl id="">
						<dt>7</dt>
						<dd class="team">
							<a href="http://saishi.zgzcw.com/soccer/team/150" target="_blank">AC米兰</a>
						</dd>
						<dd class="sai">25</dd>
						<dd class="win">12</dd>
						<dd class="draw">5</dd>
						<dd class="lost">8</dd>
						<dd class="sorce">41</dd>
					</dl>
					<dl id="">
						<dt>8</dt>
						<dd class="team">
							<a href="http://saishi.zgzcw.com/soccer/team/154" target="_blank">亚特兰大</a>
						</dd>
						<dd class="sai">25</dd>
						<dd class="win">10</dd>
						<dd class="draw">8</dd>
						<dd class="lost">7</dd>
						<dd class="sorce">38</dd>
					</dl>
					<dl id="">
						<dt>9</dt>
						<dd class="team">
							<a href="http://saishi.zgzcw.com/soccer/team/558" target="_blank">都灵</a>
						</dd>
						<dd class="sai">26</dd>
						<dd class="win">8</dd>
						<dd class="draw">12</dd>
						<dd class="lost">6</dd>
						<dd class="sorce">36</dd>
					</dl>
					<dl id="">
						<dt>10</dt>
						<dd class="team">
							<a href="http://saishi.zgzcw.com/soccer/team/176" target="_blank">佛罗伦萨</a>
						</dd>
						<dd class="sai">26</dd>
						<dd class="win">9</dd>
						<dd class="draw">8</dd>
						<dd class="lost">9</dd>
						<dd class="sorce">35</dd>
					</dl>
					<dl id="">
						<dt>11</dt>
						<dd class="team">
							<a href="http://saishi.zgzcw.com/soccer/team/187" target="_blank">乌迪内斯</a>
						</dd>
						<dd class="sai">26</dd>
						<dd class="win">10</dd>
						<dd class="draw">3</dd>
						<dd class="lost">13</dd>
						<dd class="sorce">33</dd>
					</dl>
					<dl id="cur">
						<dt>12</dt>
						<dd class="team">
							<a href="http://saishi.zgzcw.com/soccer/team/185" target="_blank">博洛尼亚</a>
						</dd>
						<dd class="sai">26</dd>
						<dd class="win">10</dd>
						<dd class="draw">3</dd>
						<dd class="lost">13</dd>
						<dd class="sorce">33</dd>
					</dl>
					<dl id="cur">
						<dt>13</dt>
						<dd class="team">
							<a href="http://saishi.zgzcw.com/soccer/team/552" target="_blank">热那亚</a>
						</dd>
						<dd class="sai">26</dd>
						<dd class="win">8</dd>
						<dd class="draw">6</dd>
						<dd class="lost">12</dd>
						<dd class="sorce">30</dd>
					</dl>
					<dl id="">
						<dt>14</dt>
						<dd class="team">
							<a href="http://saishi.zgzcw.com/soccer/team/183" target="_blank">卡利亚里</a>
						</dd>
						<dd class="sai">25</dd>
						<dd class="win">7</dd>
						<dd class="draw">4</dd>
						<dd class="lost">14</dd>
						<dd class="sorce">25</dd>
					</dl>
					<dl id="">
						<dt>15</dt>
						<dd class="team">
							<a href="http://saishi.zgzcw.com/soccer/team/195" target="_blank">切沃</a>
						</dd>
						<dd class="sai">26</dd>
						<dd class="win">6</dd>
						<dd class="draw">7</dd>
						<dd class="lost">13</dd>
						<dd class="sorce">25</dd>
					</dl>
					<dl id="">
						<dt>16</dt>
						<dd class="team">
							<a href="http://saishi.zgzcw.com/soccer/team/2960"
								target="_blank">萨索洛</a>
						</dd>
						<dd class="sai">26</dd>
						<dd class="win">6</dd>
						<dd class="draw">5</dd>
						<dd class="lost">15</dd>
						<dd class="sorce">23</dd>
					</dl>
					<dl id="">
						<dt>17</dt>
						<dd class="team">
							<a href="http://saishi.zgzcw.com/soccer/team/554" target="_blank">克罗托内</a>
						</dd>
						<dd class="sai">26</dd>
						<dd class="win">5</dd>
						<dd class="draw">6</dd>
						<dd class="lost">15</dd>
						<dd class="sorce">21</dd>
					</dl>
					<dl id="">
						<dt>18</dt>
						<dd class="team">
							<a href="http://saishi.zgzcw.com/soccer/team/1408"
								target="_blank">费拉拉</a>
						</dd>
						<dd class="sai">26</dd>
						<dd class="win">4</dd>
						<dd class="draw">8</dd>
						<dd class="lost">14</dd>
						<dd class="sorce">20</dd>
					</dl>
					<dl id="">
						<dt>19</dt>
						<dd class="team">
							<a href="http://saishi.zgzcw.com/soccer/team/559" target="_blank">维罗纳</a>
						</dd>
						<dd class="sai">26</dd>
						<dd class="win">5</dd>
						<dd class="draw">4</dd>
						<dd class="lost">17</dd>
						<dd class="sorce">19</dd>
					</dl>
					<dl id="">
						<dt>20</dt>
						<dd class="team">
							<a href="http://saishi.zgzcw.com/soccer/team/1420"
								target="_blank">贝内文托</a>
						</dd>
						<dd class="sai">26</dd>
						<dd class="win">3</dd>
						<dd class="draw">1</dd>
						<dd class="lost">22</dd>
						<dd class="sorce">10</dd>
					</dl>
				</div>
				<div class="con" id="s_H" style="display: none">
					<dl id="">
						<dt>1</dt>
						<dd class="team">
							<a href="http://saishi.zgzcw.com/soccer/team/1419"
								target="_blank">那不勒斯</a>
						</dd>
						<dd class="sai">13</dd>
						<dd class="win">10</dd>
						<dd class="draw">2</dd>
						<dd class="lost">1</dd>
						<dd class="sorce">32</dd>
					</dl>
					<dl id="">
						<dt>2</dt>
						<dd class="team">
							<a href="http://saishi.zgzcw.com/soccer/team/179" target="_blank">桑普</a>
						</dd>
						<dd class="sai">14</dd>
						<dd class="win">10</dd>
						<dd class="draw">2</dd>
						<dd class="lost">2</dd>
						<dd class="sorce">32</dd>
					</dl>
					<dl id="">
						<dt>3</dt>
						<dd class="team">
							<a href="http://saishi.zgzcw.com/soccer/team/166" target="_blank">尤文图斯</a>
						</dd>
						<dd class="sai">12</dd>
						<dd class="win">10</dd>
						<dd class="draw">1</dd>
						<dd class="lost">1</dd>
						<dd class="sorce">31</dd>
					</dl>
					<dl id="">
						<dt>4</dt>
						<dd class="team">
							<a href="http://saishi.zgzcw.com/soccer/team/152" target="_blank">国际米兰</a>
						</dd>
						<dd class="sai">14</dd>
						<dd class="win">9</dd>
						<dd class="draw">4</dd>
						<dd class="lost">1</dd>
						<dd class="sorce">31</dd>
					</dl>
					<dl id="">
						<dt>5</dt>
						<dd class="team">
							<a href="http://saishi.zgzcw.com/soccer/team/174" target="_blank">罗马</a>
						</dd>
						<dd class="sai">13</dd>
						<dd class="win">8</dd>
						<dd class="draw">1</dd>
						<dd class="lost">4</dd>
						<dd class="sorce">25</dd>
					</dl>
					<dl id="">
						<dt>6</dt>
						<dd class="team">
							<a href="http://saishi.zgzcw.com/soccer/team/182" target="_blank">拉齐奥</a>
						</dd>
						<dd class="sai">12</dd>
						<dd class="win">7</dd>
						<dd class="draw">2</dd>
						<dd class="lost">3</dd>
						<dd class="sorce">23</dd>
					</dl>
					<dl id="">
						<dt>7</dt>
						<dd class="team">
							<a href="http://saishi.zgzcw.com/soccer/team/150" target="_blank">AC米兰</a>
						</dd>
						<dd class="sai">12</dd>
						<dd class="win">7</dd>
						<dd class="draw">2</dd>
						<dd class="lost">3</dd>
						<dd class="sorce">23</dd>
					</dl>
					<dl id="">
						<dt>8</dt>
						<dd class="team">
							<a href="http://saishi.zgzcw.com/soccer/team/154" target="_blank">亚特兰大</a>
						</dd>
						<dd class="sai">13</dd>
						<dd class="win">6</dd>
						<dd class="draw">4</dd>
						<dd class="lost">3</dd>
						<dd class="sorce">22</dd>
					</dl>
					<dl id="">
						<dt>9</dt>
						<dd class="team">
							<a href="http://saishi.zgzcw.com/soccer/team/558" target="_blank">都灵</a>
						</dd>
						<dd class="sai">13</dd>
						<dd class="win">5</dd>
						<dd class="draw">5</dd>
						<dd class="lost">3</dd>
						<dd class="sorce">20</dd>
					</dl>
					<dl id="">
						<dt>10</dt>
						<dd class="team">
							<a href="http://saishi.zgzcw.com/soccer/team/176" target="_blank">佛罗伦萨</a>
						</dd>
						<dd class="sai">13</dd>
						<dd class="win">5</dd>
						<dd class="draw">4</dd>
						<dd class="lost">4</dd>
						<dd class="sorce">19</dd>
					</dl>
					<dl id="cur">
						<dt>11</dt>
						<dd class="team">
							<a href="http://saishi.zgzcw.com/soccer/team/185" target="_blank">博洛尼亚</a>
						</dd>
						<dd class="sai">14</dd>
						<dd class="win">5</dd>
						<dd class="draw">3</dd>
						<dd class="lost">6</dd>
						<dd class="sorce">18</dd>
					</dl>
					<dl id="">
						<dt>12</dt>
						<dd class="team">
							<a href="http://saishi.zgzcw.com/soccer/team/187" target="_blank">乌迪内斯</a>
						</dd>
						<dd class="sai">13</dd>
						<dd class="win">5</dd>
						<dd class="draw">2</dd>
						<dd class="lost">6</dd>
						<dd class="sorce">17</dd>
					</dl>
					<dl id="">
						<dt>13</dt>
						<dd class="team">
							<a href="http://saishi.zgzcw.com/soccer/team/195" target="_blank">切沃</a>
						</dd>
						<dd class="sai">13</dd>
						<dd class="win">4</dd>
						<dd class="draw">4</dd>
						<dd class="lost">5</dd>
						<dd class="sorce">16</dd>
					</dl>
					<dl id="">
						<dt>14</dt>
						<dd class="team">
							<a href="http://saishi.zgzcw.com/soccer/team/183" target="_blank">卡利亚里</a>
						</dd>
						<dd class="sai">12</dd>
						<dd class="win">4</dd>
						<dd class="draw">1</dd>
						<dd class="lost">7</dd>
						<dd class="sorce">13</dd>
					</dl>
					<dl id="">
						<dt>15</dt>
						<dd class="team">
							<a href="http://saishi.zgzcw.com/soccer/team/554" target="_blank">克罗托内</a>
						</dd>
						<dd class="sai">13</dd>
						<dd class="win">3</dd>
						<dd class="draw">4</dd>
						<dd class="lost">6</dd>
						<dd class="sorce">13</dd>
					</dl>
					<dl id="cur">
						<dt>16</dt>
						<dd class="team">
							<a href="http://saishi.zgzcw.com/soccer/team/552" target="_blank">热那亚</a>
						</dd>
						<dd class="sai">12</dd>
						<dd class="win">3</dd>
						<dd class="draw">2</dd>
						<dd class="lost">7</dd>
						<dd class="sorce">11</dd>
					</dl>
					<dl id="">
						<dt>17</dt>
						<dd class="team">
							<a href="http://saishi.zgzcw.com/soccer/team/1408"
								target="_blank">费拉拉</a>
						</dd>
						<dd class="sai">12</dd>
						<dd class="win">2</dd>
						<dd class="draw">5</dd>
						<dd class="lost">5</dd>
						<dd class="sorce">11</dd>
					</dl>
					<dl id="">
						<dt>18</dt>
						<dd class="team">
							<a href="http://saishi.zgzcw.com/soccer/team/2960"
								target="_blank">萨索洛</a>
						</dd>
						<dd class="sai">13</dd>
						<dd class="win">2</dd>
						<dd class="draw">4</dd>
						<dd class="lost">7</dd>
						<dd class="sorce">10</dd>
					</dl>
					<dl id="">
						<dt>19</dt>
						<dd class="team">
							<a href="http://saishi.zgzcw.com/soccer/team/559" target="_blank">维罗纳</a>
						</dd>
						<dd class="sai">13</dd>
						<dd class="win">3</dd>
						<dd class="draw">1</dd>
						<dd class="lost">9</dd>
						<dd class="sorce">10</dd>
					</dl>
					<dl id="">
						<dt>20</dt>
						<dd class="team">
							<a href="http://saishi.zgzcw.com/soccer/team/1420"
								target="_blank">贝内文托</a>
						</dd>
						<dd class="sai">13</dd>
						<dd class="win">3</dd>
						<dd class="draw">1</dd>
						<dd class="lost">9</dd>
						<dd class="sorce">10</dd>
					</dl>
				</div>
				<div class="con" id="s_G" style="display: none">
					<dl id="">
						<dt>1</dt>
						<dd class="team">
							<a href="http://saishi.zgzcw.com/soccer/team/166" target="_blank">尤文图斯</a>
						</dd>
						<dd class="sai">13</dd>
						<dd class="win">11</dd>
						<dd class="draw">1</dd>
						<dd class="lost">1</dd>
						<dd class="sorce">34</dd>
					</dl>
					<dl id="">
						<dt>2</dt>
						<dd class="team">
							<a href="http://saishi.zgzcw.com/soccer/team/1419"
								target="_blank">那不勒斯</a>
						</dd>
						<dd class="sai">12</dd>
						<dd class="win">11</dd>
						<dd class="draw">1</dd>
						<dd class="lost">0</dd>
						<dd class="sorce">34</dd>
					</dl>
					<dl id="">
						<dt>3</dt>
						<dd class="team">
							<a href="http://saishi.zgzcw.com/soccer/team/182" target="_blank">拉齐奥</a>
						</dd>
						<dd class="sai">14</dd>
						<dd class="win">9</dd>
						<dd class="draw">2</dd>
						<dd class="lost">3</dd>
						<dd class="sorce">29</dd>
					</dl>
					<dl id="">
						<dt>4</dt>
						<dd class="team">
							<a href="http://saishi.zgzcw.com/soccer/team/174" target="_blank">罗马</a>
						</dd>
						<dd class="sai">12</dd>
						<dd class="win">7</dd>
						<dd class="draw">4</dd>
						<dd class="lost">1</dd>
						<dd class="sorce">25</dd>
					</dl>
					<dl id="">
						<dt>5</dt>
						<dd class="team">
							<a href="http://saishi.zgzcw.com/soccer/team/152" target="_blank">国际米兰</a>
						</dd>
						<dd class="sai">12</dd>
						<dd class="win">5</dd>
						<dd class="draw">5</dd>
						<dd class="lost">2</dd>
						<dd class="sorce">20</dd>
					</dl>
					<dl id="cur">
						<dt>6</dt>
						<dd class="team">
							<a href="http://saishi.zgzcw.com/soccer/team/552" target="_blank">热那亚</a>
						</dd>
						<dd class="sai">14</dd>
						<dd class="win">5</dd>
						<dd class="draw">4</dd>
						<dd class="lost">5</dd>
						<dd class="sorce">19</dd>
					</dl>
					<dl id="">
						<dt>7</dt>
						<dd class="team">
							<a href="http://saishi.zgzcw.com/soccer/team/150" target="_blank">AC米兰</a>
						</dd>
						<dd class="sai">13</dd>
						<dd class="win">5</dd>
						<dd class="draw">3</dd>
						<dd class="lost">5</dd>
						<dd class="sorce">18</dd>
					</dl>
					<dl id="">
						<dt>8</dt>
						<dd class="team">
							<a href="http://saishi.zgzcw.com/soccer/team/176" target="_blank">佛罗伦萨</a>
						</dd>
						<dd class="sai">13</dd>
						<dd class="win">4</dd>
						<dd class="draw">4</dd>
						<dd class="lost">5</dd>
						<dd class="sorce">16</dd>
					</dl>
					<dl id="">
						<dt>9</dt>
						<dd class="team">
							<a href="http://saishi.zgzcw.com/soccer/team/154" target="_blank">亚特兰大</a>
						</dd>
						<dd class="sai">12</dd>
						<dd class="win">4</dd>
						<dd class="draw">4</dd>
						<dd class="lost">4</dd>
						<dd class="sorce">16</dd>
					</dl>
					<dl id="">
						<dt>10</dt>
						<dd class="team">
							<a href="http://saishi.zgzcw.com/soccer/team/187" target="_blank">乌迪内斯</a>
						</dd>
						<dd class="sai">13</dd>
						<dd class="win">5</dd>
						<dd class="draw">1</dd>
						<dd class="lost">7</dd>
						<dd class="sorce">16</dd>
					</dl>
					<dl id="">
						<dt>11</dt>
						<dd class="team">
							<a href="http://saishi.zgzcw.com/soccer/team/558" target="_blank">都灵</a>
						</dd>
						<dd class="sai">13</dd>
						<dd class="win">3</dd>
						<dd class="draw">7</dd>
						<dd class="lost">3</dd>
						<dd class="sorce">16</dd>
					</dl>
					<dl id="cur">
						<dt>12</dt>
						<dd class="team">
							<a href="http://saishi.zgzcw.com/soccer/team/185" target="_blank">博洛尼亚</a>
						</dd>
						<dd class="sai">12</dd>
						<dd class="win">5</dd>
						<dd class="draw">0</dd>
						<dd class="lost">7</dd>
						<dd class="sorce">15</dd>
					</dl>
					<dl id="">
						<dt>13</dt>
						<dd class="team">
							<a href="http://saishi.zgzcw.com/soccer/team/2960"
								target="_blank">萨索洛</a>
						</dd>
						<dd class="sai">13</dd>
						<dd class="win">4</dd>
						<dd class="draw">1</dd>
						<dd class="lost">8</dd>
						<dd class="sorce">13</dd>
					</dl>
					<dl id="">
						<dt>14</dt>
						<dd class="team">
							<a href="http://saishi.zgzcw.com/soccer/team/179" target="_blank">桑普</a>
						</dd>
						<dd class="sai">12</dd>
						<dd class="win">3</dd>
						<dd class="draw">3</dd>
						<dd class="lost">6</dd>
						<dd class="sorce">12</dd>
					</dl>
					<dl id="">
						<dt>15</dt>
						<dd class="team">
							<a href="http://saishi.zgzcw.com/soccer/team/183" target="_blank">卡利亚里</a>
						</dd>
						<dd class="sai">13</dd>
						<dd class="win">3</dd>
						<dd class="draw">3</dd>
						<dd class="lost">7</dd>
						<dd class="sorce">12</dd>
					</dl>
					<dl id="">
						<dt>16</dt>
						<dd class="team">
							<a href="http://saishi.zgzcw.com/soccer/team/559" target="_blank">维罗纳</a>
						</dd>
						<dd class="sai">13</dd>
						<dd class="win">2</dd>
						<dd class="draw">3</dd>
						<dd class="lost">8</dd>
						<dd class="sorce">9</dd>
					</dl>
					<dl id="">
						<dt>17</dt>
						<dd class="team">
							<a href="http://saishi.zgzcw.com/soccer/team/1408"
								target="_blank">费拉拉</a>
						</dd>
						<dd class="sai">14</dd>
						<dd class="win">2</dd>
						<dd class="draw">3</dd>
						<dd class="lost">9</dd>
						<dd class="sorce">9</dd>
					</dl>
					<dl id="">
						<dt>18</dt>
						<dd class="team">
							<a href="http://saishi.zgzcw.com/soccer/team/195" target="_blank">切沃</a>
						</dd>
						<dd class="sai">13</dd>
						<dd class="win">2</dd>
						<dd class="draw">3</dd>
						<dd class="lost">8</dd>
						<dd class="sorce">9</dd>
					</dl>
					<dl id="">
						<dt>19</dt>
						<dd class="team">
							<a href="http://saishi.zgzcw.com/soccer/team/554" target="_blank">克罗托内</a>
						</dd>
						<dd class="sai">13</dd>
						<dd class="win">2</dd>
						<dd class="draw">2</dd>
						<dd class="lost">9</dd>
						<dd class="sorce">8</dd>
					</dl>
					<dl id="">
						<dt>20</dt>
						<dd class="team">
							<a href="http://saishi.zgzcw.com/soccer/team/1420"
								target="_blank">贝内文托</a>
						</dd>
						<dd class="sai">13</dd>
						<dd class="win">0</dd>
						<dd class="draw">0</dd>
						<dd class="lost">13</dd>
						<dd class="sorce">0</dd>
					</dl>
				</div>
			</div>
		</div>
	</div>
	<div class="bfyc-duizhen-m">
		<div class="logoVs">
			<div class="host-name">
				<a href="http://saishi.zgzcw.com/soccer/team/185" target="_blank">博洛尼亚</a><br>
				<span> [意甲]</span>
			</div>
			<div class="visit-name">
				<a href="http://saishi.zgzcw.com/soccer/team/552" target="_blank">热那亚</a>
				<br style="font-size: 22px;"> <span> [意甲]</span>
			</div>
			<div class="vs-score">
				<h1>
					<span class="h-s bold-r">2</span> <b>-</b> <span class="v-s bold-b">0</span>
				</h1>
				<p>(半场：0-0)</p>
			</div>
			<div class="host-logo">
				<a href="http://saishi.zgzcw.com/soccer/team/185" target="_blank">
					<img src="../content/images/yijia/2013120112638.jpg"> <em>主队</em>
				</a>
			</div>
			<div class="visit-logo">
				<a href="http://saishi.zgzcw.com/soccer/team/552" target="_blank">
					<img src="../content/images/yijia/2013120112712.jpg"><em>客队</em>
				</a>
			</div>

			<div class="team-add-info">
				<div class="team-add-info-zd">上赛季排名：15 本赛季排名：12</div>
				<div class="zhonglichang">&nbsp;&nbsp;</div>
				<div class="team-add-info-kd">上赛季排名：16 本赛季排名：13</div>
			</div>
			<div class="team-info">
				<div class="team-info-h">主队：10胜 3平 13负 33分</div>
				<div class="weather">
					<span class="weiyu" title="微雨"></span>
				</div>
				<div class="team-info-v">客队：8胜 6平 12负 30分</div>
			</div>

		</div>
	</div>
	<div class="bfyc-duizhen-r">
		<div class="date">
			<span>比赛时间：2018-02-25 01:00:00</span>
		</div>
		<!--     	<div class="changdi">球场：雷纳托·达拉亚球场</div> -->
		<div class="changdi">球场：雷纳托·达拉亚球场</div>
		<!--按轮次查询-->
		<div class="round" id="lc_head">
			<!--h2 class="hover" 为鼠标滑过的显示样式 -->
			<h2 pid="2286943" p="play" type="head" t="saicheng" lc="26" d="bjop">
				<p>
					意大利甲组联赛第<i id="now_head">26</i>轮
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
		<!--有两种玩法时-->
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

<div class="main">
	<!-- 头部 -->
	<div id="data-header" class="data-header">
		<table class="bf-tab-00" width="100%" border="0" cellspacing="0"
			cellpadding="0">
			<colgroup class="xvhao" span="1">
			</colgroup>
			<colgroup class="gongsi" span="1">
			</colgroup>
			<colgroup class="#1 chushi-0" span="1">
			</colgroup>
			<colgroup class="#2 zuixin-0" span="1">
			</colgroup>
			<colgroup class="#3 touzhu-0" span="1">
			</colgroup>
			<colgroup class="#4 kaili-0" span="1">
			</colgroup>
			<colgroup class="peifulv" span="1">
			</colgroup>
			<colgroup class="lishi" span="1">
			</colgroup>
			<tbody>
				<tr class="tabhead">
					<th><span class="sx-tag">筛选</span></th>
					<th><select id="com-type">
							<option value="x" selected="selected">百家欧赔</option>
							<option value="0">主流公司</option>
							<option value="1">交易公司</option>
							<option value="2">亚洲公司</option>
							<option value="5">普通公司</option>
					</select></th>
					<th class="#1"><button class="hiddenBtn">隐藏</button> 初始赔率</th>
					<th class="#2"><button class="showBtn" style="display: none;">初始</button>
						最新赔率</th>
					<th class="#3">最新概率(%)</th>
					<th class="#4">最新凯利指数</th>
					<th>赔付率</th>
					<th>历史</th>
				</tr>
			</tbody>
		</table>
		<table class="bf-tab-01" width="100%" border="0" cellspacing="0"
			cellpadding="0">
			<colgroup class="xvhao" span="1">
			</colgroup>
			<colgroup class="gongsi" span="1">
			</colgroup>
			<colgroup class="#1 chushi" span="1">
			</colgroup>
			<colgroup class="#1 chushi" span="1">
			</colgroup>
			<colgroup class="#1 chushi" span="1">
			</colgroup>
			<colgroup class="#2 zuixin" span="1">
			</colgroup>
			<colgroup class="#2 zuixin" span="1">
			</colgroup>
			<colgroup class="#2 zuixin" span="1">
			</colgroup>
			<colgroup class="gengxin" span="1">
			</colgroup>
			<colgroup class="#3 touzhu" span="1">
			</colgroup>
			<colgroup class="#3 touzhu" span="1">
			</colgroup>
			<colgroup class="#3 touzhu" span="1">
			</colgroup>
			<colgroup class="#4 kaili" span="1">
			</colgroup>
			<colgroup class="#4 kaili" span="1">
			</colgroup>
			<colgroup class="#4 kaili" span="1">
			</colgroup>
			<colgroup class="peifulv" span="1">
			</colgroup>
			<colgroup class="lishi" span="1">
			</colgroup>
			<tbody>
				<tr class="tabtit">
					<th class="border-r">序号</th>
					<th class="border-r border-l"><a href="javascript:;"
						class="pm" data="$a">公司</a></th>
					<th class="#1 border-l"><a href="javascript:;" class="pm"
						data="$b">胜</a></th>
					<th class="#1"><a href="javascript:;" class="pm" data="$c">平</a></th>
					<th class="#1 border-r"><a href="javascript:;" class="pm"
						data="$d">负</a></th>
					<th class="#2 border-l"><a href="javascript:;" class="pm"
						data="$e">胜</a></th>
					<th class="#2"><a href="javascript:;" class="pm" data="$f">平</a></th>
					<th class="#2"><a href="javascript:;" class="pm" data="$g">负</a></th>
					<th class="border-r">&nbsp;</th>
					<th class="#3 border-l"><a href="javascript:;" class="pm"
						data="$h">主</a></th>
					<th class="#3"><a href="javascript:;" class="pm" data="$i">平</a></th>
					<th class="#3 border-r"><a href="javascript:;" class="pm"
						data="$j">客</a></th>
					<th class="#4 border-l"><a href="javascript:;" class="pm"
						data="$k">主</a></th>
					<th class="#4"><a href="javascript:;" class="pm" data="$l">平</a></th>
					<th class="#4 border-r"><a href="javascript:;" class="pm"
						data="$m">客</a></th>
					<th class="border-l border-r"><a href="javascript:;"
						class="pm" data="$n">值</a></th>
					<th class="border-l">值</th>
				</tr>
				<tr class="shaixuan" style="display: none;">
					<th colspan="2" class="first border-r"><p class="fl">高级筛选
							&gt;&gt;</p>
						<p class="fr">
							最小<br> 最大
						</p></th>
					<th class="#1 border-l border-r"><input name="data-sift"
						type="text"> <br> <input name="data-sift" type="text"></th>
					<th class="#1"><input name="data-sift" type="text"> <br>
						<input name="data-sift" type="text"></th>
					<th class="#1 border-r"><input name="data-sift" type="text">
						<br> <input name="data-sift" type="text"></th>
					<th class="#2 border-l"><input name="data-sift" type="text">
						<br> <input name="data-sift" type="text"></th>
					<th class="#2"><input name="data-sift" type="text"> <br>
						<input name="data-sift" type="text"></th>
					<th class="#2"><input name="data-sift" type="text"> <br>
						<input name="data-sift" type="text"></th>
					<th class="border-r">&nbsp;</th>
					<th class="#3 border-l"><input name="data-sift" type="text">
						<br> <input name="data-sift" type="text"></th>
					<th class="#3"><input name="data-sift" type="text"> <br>
						<input name="data-sift" type="text"></th>
					<th class="#3 border-r"><input name="data-sift" type="text">
						<br> <input name="data-sift" type="text"></th>
					<th class="#4 border-l"><input name="data-sift" type="text">
						<br> <input name="data-sift" type="text"></th>
					<th class="#4"><input name="data-sift" type="text"> <br>
						<input name="data-sift" type="text"></th>
					<th class="#4 border-r"><input name="data-sift" type="text">
						<br> <input name="data-sift" type="text"></th>
					<th colspan="2" class="border-l"><span class="shaixuan-btn">
							<input value="筛 选" type="button"> <a href="javascript:;">关闭</a>
					</span></th>
				</tr>
			</tbody>
		</table>
	</div>
	<!-- 内容 -->
	<div id="data-body" class="data-main">
		<table class="bf-tab-02" width="100%" border="0" cellspacing="0"
			cellpadding="0">
			<colgroup class="xvhao" span="1"></colgroup>
			<colgroup class="gongsi" span="1">
			</colgroup>
			<colgroup class="#1 chushi" span="1">
			</colgroup>
			<colgroup class="#1 chushi" span="1">
			</colgroup>
			<colgroup class="#1 chushi" span="1">
			</colgroup>
			<colgroup class="#2 zuixin" span="1">
			</colgroup>
			<colgroup class="#2 zuixin" span="1">
			</colgroup>
			<colgroup class="#2 zuixin" span="1">
			</colgroup>
			<colgroup class="gengxin" span="1">
			</colgroup>
			<colgroup class="#3 touzhu" span="1">
			</colgroup>
			<colgroup class="#3 touzhu" span="1">
			</colgroup>
			<colgroup class="#3 touzhu" span="1">
			</colgroup>
			<colgroup class="#4 kaili" span="1">
			</colgroup>
			<colgroup class="#4 kaili" span="1">
			</colgroup>
			<colgroup class="#4 kaili" span="1">
			</colgroup>
			<colgroup class="peifulv" span="1">
			</colgroup>
			<colgroup class="lishi" span="1">
			</colgroup>
			<tbody>			
				<c:forEach var="op" items="${oddslist}">
					<tr class="" firsttime="2018-02-11 00:27:05" lasttime="1519497840000" ismore="1">
						<td class="border-r"><label class="sxinput">
							<input type="checkbox">${op.ordinary }</label>
						</td>
						<td data="0" class="border-r border-l"><font class="hot"></font>${op.gname }</td>
						<td data="2.43" class="#1 border-l	">${op.firstwinodds }</td>
						<td data="3.11" class="#1			">${op.firstdrawodds }</td>
						<td data="2.99" class="#1 border-r	">${op.firstloseodds }</td>
						<td cid="0" data="2.86" class="#2 border-l tdbg"><a
							href="http://fenxi.zgzcw.com/2286943/bjop/zhishu?company_id=0&amp;company=%E5%B9%B3%E5%9D%87%E6%AC%A7%E8%B5%94"
							target="_blank" class="">${op.winodds }</a></td>
						<td cid="0" data="2.92" class="#2 tdbg"><a
							href="http://fenxi.zgzcw.com/2286943/bjop/zhishu?company_id=0&amp;company=%E5%B9%B3%E5%9D%87%E6%AC%A7%E8%B5%94"
							target="_blank" class="blue">${op.drawodds }</a></td>
						<td cid="0" data="2.74" class="#2 tdbg"><a
							href="http://fenxi.zgzcw.com/2286943/bjop/zhishu?company_id=0&amp;company=%E5%B9%B3%E5%9D%87%E6%AC%A7%E8%B5%94"
							target="_blank" class="">${op.loseodds }</a></td>
						<td class="border-r tdbg"><em class="gengxin-1"
							title="更新时间：赛前0分"></em></td>
						<td data="${op.winprob }" class="#3 border-l">${op.winprob }</td>
						<td data="${op.drawprob}" class="#3			">${op.drawprob}</td>
						<td data="${op.loseprob}" class="#3 border-r">${op.loseprob}</td>
						<td data="${op.winkelly}" class="#4 border-l	">${op.winkelly}</td>
						<td data="${op.drawkelly}" class="#4			">${op.drawkelly}</td>
						<td data="${op.losekelly}" class="#4			">${op.losekelly}</td>
						<td data="${op.lossratio}" class="border-l border-r"><span class="">${op.lossratio}</span></td>
						<td class="border-l"><a rel="external nofollow"
							href="http://fenxi.zgzcw.com/2286943/bjop/zhu?company_id=0&amp;company=%E5%B9%B3%E5%9D%87%E6%AC%A7%E8%B5%94&amp;win=2.43&amp;same=3.11&amp;lost=2.99"
							target="_blank" class="bf-a1">主</a> <a rel="external nofollow"
							href="http://fenxi.zgzcw.com/2286943/bjop/ke?company_id=0&amp;company=%E5%B9%B3%E5%9D%87%E6%AC%A7%E8%B5%94&amp;win=2.43&amp;same=3.11&amp;lost=2.99"
							target="_blank" class="bf-a1">客</a> <a rel="external nofollow"
							href="http://fenxi.zgzcw.com/2286943/bjop/tong?company_id=0&amp;win=2.43&amp;same=3.11&amp;lost=2.99&amp;company=%E5%B9%B3%E5%9D%87%E6%AC%A7%E8%B5%94"
							target="_blank" class="bf-a1">同</a></td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
	</div>
	<!-- 底部 -->
	<div id="data-footer" class="footer-fix">
		<table class="bf-tab-03" width="100%" border="0" cellspacing="0"
			cellpadding="0">
			<colgroup class="foot-console" span="1">
			</colgroup>
			<colgroup class="foot-value" span="1">
			</colgroup>
			<colgroup class="#1 chushi" span="1">
			</colgroup>
			<colgroup class="#1 chushi" span="1">
			</colgroup>
			<colgroup class="#1 chushi" span="1">
			</colgroup>
			<colgroup class="#2 zuixin" span="1">
			</colgroup>
			<colgroup class="#2 zuixin" span="1">
			</colgroup>
			<colgroup class="#2 zuixin" span="1">
			</colgroup>
			<colgroup class="gengxin" span="1">
			</colgroup>
			<colgroup class="#3 touzhu" span="1">
			</colgroup>
			<colgroup class="#3 touzhu" span="1">
			</colgroup>
			<colgroup class="#3 touzhu" span="1">
			</colgroup>
			<colgroup class="#4 kaili" span="1">
			</colgroup>
			<colgroup class="#4 kaili" span="1">
			</colgroup>
			<colgroup class="#4 kaili" span="1">
			</colgroup>
			<colgroup class="peifulv" span="1">
			</colgroup>
			<colgroup class="lishi" span="1">
			</colgroup>
			<tbody>
				<tr>
					<td class="border-r"><input id="bjop_xsxz" class="bf-input1"
						value="显示选择" type="button"> <a href="javascript:;"
						class="bf-a1">全选</a> <a href="javascript:;" class="bf-a1">反选</a> <a
						href="javascript:;" class="bf-a2">恢复</a></td>
					<td class="border-r border-l">平均值</td>
					<td class="#1 border-l">2.43</td>
					<td class="#1">3.11</td>
					<td class="#1 border-r">2.99</td>
					<td class="#2 border-l tdbg" id="avg_win">2.86</td>
					<td class="#2 tdbg" id="avg_same">2.92</td>
					<td class="#2 tdbg" id="avg_lost">2.74</td>
					<td class="border-r tdbg">-</td>
					<td class="#3 border-l">33.14</td>
					<td class="#3">32.35</td>
					<td class="#3 border-r">34.52</td>
					<td class="#4 border-l">0.95</td>
					<td class="#4">0.95</td>
					<td class="#4 border-r">0.95</td>
					<td class="border-l border-r">0.95</td>
					<td class="border-l">&nbsp;</td>
				</tr>
				<tr>
					<td class="border-r"><span class="grey">共[<i
							id="com-count" class="red">210</i>]家公司
					</span></td>
					<td class="border-r border-l">最大值</td>
					<td class="#1 border-l">3.15</td>
					<td class="#1">3.68</td>
					<td class="#1 border-r">3.70</td>
					<td class="#2 border-l tdbg">3.30</td>
					<td class="#2 tdbg">3.20</td>
					<td class="#2 tdbg">3.05</td>
					<td class="border-r tdbg">-</td>
					<td class="#3 border-l">39.55</td>
					<td class="#3">34.48</td>
					<td class="#3 border-r">38.13</td>
					<td class="#4 border-l">1.09</td>
					<td class="#4">1.04</td>
					<td class="#4 border-r">1.05</td>
					<td class="border-l border-r">0.99</td>
					<td class="border-l">&nbsp;</td>
				</tr>
				<tr>
					<td class="border-r"><a
						href="http://fenxi.zgzcw.com/export/2286943/bjop" target="_blank"
						class="bf-input1">Excel下载</a></td>
					<td class="border-r border-l">最小值</td>
					<td class="#1 border-l">2.04</td>
					<td class="#1">2.65</td>
					<td class="#1 border-r">2.50</td>
					<td class="#2 border-l tdbg">2.20</td>
					<td class="#2 tdbg">2.47</td>
					<td class="#2 tdbg">2.27</td>
					<td class="border-r tdbg">-</td>
					<td class="#3 border-l">29.46</td>
					<td class="#3">29.67</td>
					<td class="#3 border-r">30.08</td>
					<td class="#4 border-l">0.73</td>
					<td class="#4">0.80</td>
					<td class="#4 border-r">0.78</td>
					<td class="border-l border-r">0.80</td>
					<td class="border-l">&nbsp;</td>
				</tr>
				<tr>
					<td colspan="17"><div class="data-f-ps">
							<span class="otherodds">离散度% 1.47 0.82 1.17 | 中足网方差% 2.16
								0.67 1.36</span> <label> <input id="checkbox-scroll"
								checked="checked" value="头尾浮动" type="checkbox"> 头尾浮动
							</label> <span> <em class="hot"></em>为主流公司
							</span> <span> <i class="bold-bla">黑粗最大</i> <i class="bold-g">绿粗最小</i>
								<i class="red">↑上升</i> <i class="blue">↓下降</i> <i class="bold-r">凯利指数红粗超过1</i>
							</span>
						</div></td>
				</tr>
			</tbody>
		</table>
	</div>
</div>

<!-- 图表 -->
<script type="text/javascript">
	var matchData = '1519491600000';
	var playId = '2286943';
	var dataType = 'bjop';
</script>
