<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<div class="minibar">
	<span class="location">当前位置：</span>
	<a href="http://www.zgzcw.com/" target="_self">东方足球网</a> 
	<span class="dotline">&gt;</span>
	<a href="http://saishi.zgzcw.com:80/soccer/" target="_self">赛事中心</a>
	<span class="dotline">&gt;</span>
	<span class="indext">联赛资料</span>
</div>

<div class="league wapper">
	
	<!-- 左侧页面 -->
	<%@include file="leagueleft.jsp"%>
	
	<form id="form_1" method="post">
		<input name="seasonType" value="1" type="hidden">
	</form>
	<form id="form_2" method="post">
		<input name="seasonType" value="2" type="hidden">
	</form>

	<!-- 右侧页面 -->
	<div class="league_right">
		<div class="table_out">
			<div class="table_head">
				
				<span> ${season }${league.name }赛事 </span>

				<!-- 韩K赛 -->

				<ul id="fjsai">
					<li class="cur" style="margin-left: 80px;">常规赛</li>
					<li>附加赛</li>
				</ul>				
			</div>

			<div class="league_right_021">
				<div class="box luncib">
					<c:forEach var="r" items="${rounds}">
						<c:choose>
							<c:when test="${round eq r.rid }">
								<em class="em_2">${r.rid}</em>
							</c:when>
							<c:otherwise>
								<em class="em_1">${r.rid}</em>
						    </c:otherwise>
						</c:choose>						
					</c:forEach>
					<div class="clear"></div>
				</div>
			</div>
			<div class="league_right_022" id="matchInfo">
				<table id="tab_" class="zstab" width="100%" cellspacing="0"
					cellpadding="0" border="0">
					<thead>
						<tr>
							<th width="127">时间</th>
							<th width="165">主队</th>
							<th width="33">比分</th>
							<th width="165">客队</th>
							<th width="65">半场</th>
							<th width="153">让球</th>
							<th width="66">数据</th>
						</tr>
					</thead>
					<tbody>
						<c:forEach var="m" items="${matchs}">
							<tr>
								<td>${m.matchtime }</td>
								<td class="team_logo" style="float: right; padding-right: 20px;">
									<a href="http://saishi.zgzcw.com/soccer/team/174" target="_blank" title="">${m.homename}</a>
									<img src="getImage?id=${m.homeid}">
								</td>
								<td>${m.score }</td>
								<td class="team_logo" style="padding-left: 10px; float: left;">
									<img src="getImage?id=${m.clientid }">
									<a href="http://saishi.zgzcw.com/soccer/team/187" target="_blank" title="">${m.clientname}</a>
								</td>
								<td>0:0</td>
								<td>${m.handicap}</td>
								<td>
									<a href="ypdb?mid=${m.mid }" target="_blank" class="oyx">亚</a>
									<a href="bjop?mid=${m.mid }" target="_blank" class="oyx">欧</a>
									<a href="bfyc?mid=${m.mid }" target="_blank" class="oyx">析</a>
								</td>
							</tr>
						</c:forEach>						
					</tbody>
				</table>
			</div>
			<div class="league_right_022" id="matchInfofj"></div>
		</div>

		<!-- 积分榜--------------start -->
		<div id="hideList">
			<div class="table_out">
				<div class="table_head">
					<span>2016-2017意甲积分榜</span>
					<ul class="tabs1" id="tabs1">
						<li class="cur" value="1">总榜</li>
						<li value="2">主场</li>
						<li value="3">客场</li>
						<li value="4">近六轮</li>
						<li value="5">上半场</li>
						<li value="6">下半场</li>
					</ul>
				</div>

				<ul class="tabs1_main_ul" id="tabs1_main_1" style="display: block;">
					<table class="zstab" width="100%" cellspacing="0" cellpadding="0"
						border="0">
						<thead>
							<tr>
								<th width="50">排名</th>
								<th width="140">球队名称</th>
								<th width="40">赛</th>
								<th width="40">胜</th>
								<th width="40">平</th>
								<th width="40">负</th>
								<th width="40">得</th>
								<th width="40">失</th>
								<th width="40">净</th>
								<th width="50">均得</th>
								<th width="50">均失</th>
								<th width="50">胜率</th>
								<th width="50">平率</th>
								<th width="50">负率</th>
								<th width="40">积分</th>
							</tr>
						</thead>
						<tbody>
							<tr>
								<td class=""><b class="no1"
									style="background-color: #FF9966">1</b></td>
								<td class="team_logo"><img
									src="../content/images/yijia/20140324112025.jpg"> <a
									href="http://saishi.zgzcw.com/soccer/team/166" title="尤文图斯"
									target="_blank">尤文图斯</a></td>
								<td>38</td>
								<td>29</td>
								<td>4</td>
								<td>5</td>
								<td>77</td>
								<td>27</td>
								<td>50</td>
								<td>2.03</td>
								<td>0.71</td>
								<td>76%</td>
								<td>11%</td>
								<td>13%</td>
								<td>91</td>
							</tr>
							<tr class="shuangtr">
								<td class=""><b class="no1"
									style="background-color: #FF9966">2</b></td>
								<td class="team_logo"><img
									src="../content/images/yijia/20140806065357.jpg"> <a
									href="http://saishi.zgzcw.com/soccer/team/174" title="罗马"
									target="_blank">罗马</a></td>
								<td>38</td>
								<td>28</td>
								<td>3</td>
								<td>7</td>
								<td>90</td>
								<td>38</td>
								<td>52</td>
								<td>2.37</td>
								<td>1</td>
								<td>74%</td>
								<td>8%</td>
								<td>18%</td>
								<td>87</td>
							</tr>
							<tr>
								<td class=""><b class="no1"
									style="background-color: #a2e76f">3</b></td>
								<td class="team_logo"><img
									src="../content/images/yijia/2013120112732.jpg"> <a
									href="http://saishi.zgzcw.com/soccer/team/1419" title="那不勒斯"
									target="_blank">那不勒斯</a></td>
								<td>38</td>
								<td>26</td>
								<td>8</td>
								<td>4</td>
								<td>94</td>
								<td>39</td>
								<td>55</td>
								<td>2.47</td>
								<td>1.03</td>
								<td>68%</td>
								<td>21%</td>
								<td>11%</td>
								<td>86</td>
							</tr>
							<tr class="shuangtr">
								<td class=""><b class="no1"
									style="background-color: #00CCFF">4</b></td>
								<td class="team_logo"><img
									src="../content/images/yijia/2013120104409.jpg"> <a
									href="http://saishi.zgzcw.com/soccer/team/154" title="亚特兰大"
									target="_blank">亚特兰大</a></td>
								<td>38</td>
								<td>21</td>
								<td>9</td>
								<td>8</td>
								<td>62</td>
								<td>41</td>
								<td>21</td>
								<td>1.63</td>
								<td>1.08</td>
								<td>55%</td>
								<td>24%</td>
								<td>21%</td>
								<td>72</td>
							</tr>
							<tr>
								<td class=""><b class="no1"
									style="background-color: #00CCFF">5</b></td>
								<td class="team_logo"><img
									src="../content/images/yijia/2013120112625.jpg"> <a
									href="http://saishi.zgzcw.com/soccer/team/182" title="拉齐奥"
									target="_blank">拉齐奥</a></td>
								<td>38</td>
								<td>21</td>
								<td>7</td>
								<td>10</td>
								<td>74</td>
								<td>51</td>
								<td>23</td>
								<td>1.95</td>
								<td>1.34</td>
								<td>55%</td>
								<td>18%</td>
								<td>26%</td>
								<td>70</td>
							</tr>
							<tr class="shuangtr">
								<td class=""><b class="no1">6</b></td>
								<td class="team_logo"><img
									src="../content/images/yijia/2013120103644.jpg"> <a
									href="http://saishi.zgzcw.com/soccer/team/150" title="AC米兰"
									target="_blank">AC米兰</a></td>
								<td>38</td>
								<td>18</td>
								<td>9</td>
								<td>11</td>
								<td>57</td>
								<td>45</td>
								<td>12</td>
								<td>1.5</td>
								<td>1.18</td>
								<td>47%</td>
								<td>24%</td>
								<td>29%</td>
								<td>63</td>
							</tr>
							<tr>
								<td class=""><b class="no1">7</b></td>
								<td class="team_logo"><img
									src="../content/images/yijia/2013120103956.jpg"> <a
									href="http://saishi.zgzcw.com/soccer/team/152" title="国际米兰"
									target="_blank">国际米兰</a></td>
								<td>38</td>
								<td>19</td>
								<td>5</td>
								<td>14</td>
								<td>72</td>
								<td>49</td>
								<td>23</td>
								<td>1.89</td>
								<td>1.29</td>
								<td>50%</td>
								<td>13%</td>
								<td>37%</td>
								<td>62</td>
							</tr>
							<tr class="shuangtr">
								<td class=""><b class="no1">8</b></td>
								<td class="team_logo"><img
									src="../content/images/yijia/2013120112611.jpg"> <a
									href="http://saishi.zgzcw.com/soccer/team/176" title="佛罗伦萨"
									target="_blank">佛罗伦萨</a></td>
								<td>38</td>
								<td>16</td>
								<td>12</td>
								<td>10</td>
								<td>63</td>
								<td>57</td>
								<td>6</td>
								<td>1.66</td>
								<td>1.5</td>
								<td>42%</td>
								<td>32%</td>
								<td>26%</td>
								<td>60</td>
							</tr>
							<tr>
								<td class=""><b class="no1">9</b></td>
								<td class="team_logo"><img
									src="../content/images/yijia/201313093257.jpg"> <a
									href="http://saishi.zgzcw.com/soccer/team/558" title="都灵"
									target="_blank">都灵</a></td>
								<td>38</td>
								<td>13</td>
								<td>14</td>
								<td>11</td>
								<td>71</td>
								<td>66</td>
								<td>5</td>
								<td>1.87</td>
								<td>1.74</td>
								<td>34%</td>
								<td>37%</td>
								<td>29%</td>
								<td>53</td>
							</tr>
							<tr class="shuangtr">
								<td class=""><b class="no1">10</b></td>
								<td class="team_logo"><img
									src="../content/images/yijia/2013120112618.jpg"> <a
									href="http://saishi.zgzcw.com/soccer/team/179" title="桑普"
									target="_blank">桑普</a></td>
								<td>38</td>
								<td>12</td>
								<td>12</td>
								<td>14</td>
								<td>49</td>
								<td>55</td>
								<td>-6</td>
								<td>1.29</td>
								<td>1.45</td>
								<td>32%</td>
								<td>32%</td>
								<td>37%</td>
								<td>48</td>
							</tr>
							<tr>
								<td class=""><b class="no1">11</b></td>
								<td class="team_logo"><img
									src="../content/images/yijia/201313092945.jpg"> <a
									href="http://saishi.zgzcw.com/soccer/team/183" title="卡利亚里"
									target="_blank">卡利亚里</a></td>
								<td>38</td>
								<td>14</td>
								<td>5</td>
								<td>19</td>
								<td>55</td>
								<td>76</td>
								<td>-21</td>
								<td>1.45</td>
								<td>2</td>
								<td>37%</td>
								<td>13%</td>
								<td>50%</td>
								<td>47</td>
							</tr>
							<tr class="shuangtr">
								<td class=""><b class="no1">12</b></td>
								<td class="team_logo"><img
									src="../content/images/yijia/2013121192851.jpg"> <a
									href="http://saishi.zgzcw.com/soccer/team/2960" title="萨索洛"
									target="_blank">萨索洛</a></td>
								<td>38</td>
								<td>13</td>
								<td>7</td>
								<td>18</td>
								<td>58</td>
								<td>63</td>
								<td>-5</td>
								<td>1.53</td>
								<td>1.66</td>
								<td>34%</td>
								<td>18%</td>
								<td>47%</td>
								<td>46</td>
							</tr>
							<tr>
								<td class=""><b class="no1">13</b></td>
								<td class="team_logo"><img
									src="../content/images/yijia/201313092708.jpg"> <a
									href="http://saishi.zgzcw.com/soccer/team/187" title="乌迪内斯"
									target="_blank">乌迪内斯</a></td>
								<td>38</td>
								<td>12</td>
								<td>9</td>
								<td>17</td>
								<td>47</td>
								<td>56</td>
								<td>-9</td>
								<td>1.24</td>
								<td>1.47</td>
								<td>32%</td>
								<td>24%</td>
								<td>45%</td>
								<td>45</td>
							</tr>
							<tr class="shuangtr">
								<td class=""><b class="no1">14</b></td>
								<td class="team_logo"><img
									src="../content/images/yijia/2013120112658.jpg"> <a
									href="http://saishi.zgzcw.com/soccer/team/195" title="切沃"
									target="_blank">切沃</a></td>
								<td>38</td>
								<td>12</td>
								<td>7</td>
								<td>19</td>
								<td>43</td>
								<td>61</td>
								<td>-18</td>
								<td>1.13</td>
								<td>1.61</td>
								<td>32%</td>
								<td>18%</td>
								<td>50%</td>
								<td>43</td>
							</tr>
							<tr>
								<td class=""><b class="no1">15</b></td>
								<td class="team_logo"><img
									src="../content/images/yijia/2013120112638.jpg"> <a
									href="http://saishi.zgzcw.com/soccer/team/185" title="博洛尼亚"
									target="_blank">博洛尼亚</a></td>
								<td>38</td>
								<td>11</td>
								<td>8</td>
								<td>19</td>
								<td>40</td>
								<td>58</td>
								<td>-18</td>
								<td>1.05</td>
								<td>1.53</td>
								<td>29%</td>
								<td>21%</td>
								<td>50%</td>
								<td>41</td>
							</tr>
							<tr class="shuangtr">
								<td class=""><b class="no1">16</b></td>
								<td class="team_logo"><img
									src="../content/images/yijia/2013120112712.jpg"> <a
									href="http://saishi.zgzcw.com/soccer/team/552" title="热那亚"
									target="_blank">热那亚</a></td>
								<td>38</td>
								<td>9</td>
								<td>9</td>
								<td>20</td>
								<td>38</td>
								<td>64</td>
								<td>-26</td>
								<td>1</td>
								<td>1.68</td>
								<td>24%</td>
								<td>24%</td>
								<td>53%</td>
								<td>36</td>
							</tr>
							<tr>
								<td class=""><b class="no1">17</b></td>
								<td class="team_logo"><img
									src="../content/images/yijia/20130915170354.jpg"> <a
									href="http://saishi.zgzcw.com/soccer/team/554" title="克罗托内"
									target="_blank">克罗托内</a></td>
								<td>38</td>
								<td>9</td>
								<td>7</td>
								<td>22</td>
								<td>34</td>
								<td>58</td>
								<td>-24</td>
								<td>0.89</td>
								<td>1.53</td>
								<td>24%</td>
								<td>18%</td>
								<td>58%</td>
								<td>34</td>
							</tr>
							<tr class="shuangtr">
								<td class=""><b class="no1"
									style="background-color: #B1A7A7">18</b></td>
								<td class="team_logo"><img
									src="../content/images/yijia/2013121192718.jpg"> <a
									href="http://saishi.zgzcw.com/soccer/team/523" title="恩波利"
									target="_blank">恩波利</a></td>
								<td>38</td>
								<td>8</td>
								<td>8</td>
								<td>22</td>
								<td>29</td>
								<td>61</td>
								<td>-32</td>
								<td>0.76</td>
								<td>1.61</td>
								<td>21%</td>
								<td>21%</td>
								<td>58%</td>
								<td>32</td>
							</tr>
							<tr>
								<td class=""><b class="no1"
									style="background-color: #B1A7A7">19</b></td>
								<td class="team_logo"><img
									src="../content/images/yijia/2013120112541.jpg"> <a
									href="http://saishi.zgzcw.com/soccer/team/168" title="巴勒莫"
									target="_blank">巴勒莫</a></td>
								<td>38</td>
								<td>6</td>
								<td>8</td>
								<td>24</td>
								<td>33</td>
								<td>77</td>
								<td>-44</td>
								<td>0.87</td>
								<td>2.03</td>
								<td>16%</td>
								<td>21%</td>
								<td>63%</td>
								<td>26</td>
							</tr>
							<tr class="shuangtr">
								<td class=""><b class="no1"
									style="background-color: #B1A7A7">20</b></td>
								<td class="team_logo"><img
									src="../content/images/yijia/201313085709.gif"> <a
									href="http://saishi.zgzcw.com/soccer/team/560" title="佩斯卡拉"
									target="_blank">佩斯卡拉</a></td>
								<td>38</td>
								<td>3</td>
								<td>9</td>
								<td>26</td>
								<td>37</td>
								<td>81</td>
								<td>-44</td>
								<td>0.97</td>
								<td>2.13</td>
								<td>8%</td>
								<td>24%</td>
								<td>68%</td>
								<td>18</td>
							</tr>
						</tbody>
					</table>
					<div class="tuli"
						style="margin: 0; position: absolute; bottom: -20px; left: 22px;">
						<b style="background: #FF9966; margin: 5px;"></b> <span>欧冠杯小组赛资格</span>
						<b style="background: #a2e76f; margin: 5px;"></b> <span>欧冠杯附加</span>
						<b style="background: #00CCFF; margin: 5px;"></b> <span>欧罗巴联赛杯资格</span>
						<b style="background: #B1A7A7; margin: 5px;"></b> <span>降级球队</span>
					</div>
				</ul>
				<ul class="tabs1_main_ul" id="tabs1_main_2" style="display: none;">
					<table class="zstab" width="100%" cellspacing="0" cellpadding="0"
						border="0">
						<thead>
							<tr>
								<th width="50">排名</th>
								<th width="140">球队名称</th>
								<th width="40">赛</th>
								<th width="40">胜</th>
								<th width="40">平</th>
								<th width="40">负</th>
								<th width="40">得</th>
								<th width="40">失</th>
								<th width="40">净</th>
								<th width="50">均得</th>
								<th width="50">均失</th>
								<th width="50">胜率</th>
								<th width="50">平率</th>
								<th width="50">负率</th>
								<th width="40">积分</th>
							</tr>
						</thead>
						<tbody>
							<tr>
								<td width="70"><b class="no1">1</b></td>
								<td class="team_logo" width="90"><img
									src="../content/images/yijia/20140324112025.jpg"> <a
									href="http://saishi.zgzcw.com/soccer/team/166" title="尤文图斯"
									target="_blank">尤文图斯</a></td>
								<td width="40">19</td>
								<td width="40">18</td>
								<td width="40">1</td>
								<td width="40">0</td>
								<td width="40">48</td>
								<td width="40">9</td>
								<td width="40">39</td>
								<td width="50">2.53</td>
								<td width="50">0.47</td>
								<td width="50">95%</td>
								<td width="50">5%</td>
								<td width="50">0%</td>
								<td width="40">55</td>
								<!-- 	
					<td align="center" class="bor_r2"><a
						href="166"
						title="" class="a5" target="_blank">WWWWDW</a></td>
				 -->
							</tr>
							<tr class="shuangtr">
								<td width="70"><b class="no1">2</b></td>
								<td class="team_logo" width="90"><img
									src="../content/images/yijia/20140806065357.jpg"> <a
									href="http://saishi.zgzcw.com/soccer/team/174" title="罗马"
									target="_blank">罗马</a></td>
								<td width="40">19</td>
								<td width="40">16</td>
								<td width="40">1</td>
								<td width="40">2</td>
								<td width="40">50</td>
								<td width="40">18</td>
								<td width="40">32</td>
								<td width="50">2.63</td>
								<td width="50">0.95</td>
								<td width="50">84%</td>
								<td width="50">5%</td>
								<td width="50">11%</td>
								<td width="40">49</td>
								<!-- 	
					<td align="center" class="bor_r2"><a
						href="174"
						title="" class="a5" target="_blank">WWDLWW</a></td>
				 -->
							</tr>
							<tr>
								<td width="70"><b class="no1">3</b></td>
								<td class="team_logo" width="90"><img
									src="../content/images/yijia/2013120112732.jpg"> <a
									href="http://saishi.zgzcw.com/soccer/team/1419" title="那不勒斯"
									target="_blank">那不勒斯</a></td>
								<td width="40">19</td>
								<td width="40">13</td>
								<td width="40">4</td>
								<td width="40">2</td>
								<td width="40">44</td>
								<td width="40">19</td>
								<td width="40">25</td>
								<td width="50">2.32</td>
								<td width="50">1</td>
								<td width="50">68%</td>
								<td width="50">21%</td>
								<td width="50">11%</td>
								<td width="40">43</td>
								<!-- 	
					<td align="center" class="bor_r2"><a
						href="1419"
						title="" class="a5" target="_blank">LWDWWW</a></td>
				 -->
							</tr>
							<tr class="shuangtr">
								<td width="70"><b class="no1">4</b></td>
								<td class="team_logo" width="90"><img
									src="../content/images/yijia/2013120104409.jpg"> <a
									href="http://saishi.zgzcw.com/soccer/team/154" title="亚特兰大"
									target="_blank">亚特兰大</a></td>
								<td width="40">19</td>
								<td width="40">12</td>
								<td width="40">4</td>
								<td width="40">3</td>
								<td width="40">31</td>
								<td width="40">18</td>
								<td width="40">13</td>
								<td width="50">1.63</td>
								<td width="50">0.95</td>
								<td width="50">63%</td>
								<td width="50">21%</td>
								<td width="50">16%</td>
								<td width="40">40</td>
								<!-- 	
					<td align="center" class="bor_r2"><a
						href="154"
						title="" class="a5" target="_blank">WDWDDW</a></td>
				 -->
							</tr>
							<tr>
								<td width="70"><b class="no1">5</b></td>
								<td class="team_logo" width="90"><img
									src="../content/images/yijia/2013120112625.jpg"> <a
									href="http://saishi.zgzcw.com/soccer/team/182" title="拉齐奥"
									target="_blank">拉齐奥</a></td>
								<td width="40">19</td>
								<td width="40">12</td>
								<td width="40">2</td>
								<td width="40">5</td>
								<td width="40">40</td>
								<td width="40">23</td>
								<td width="40">17</td>
								<td width="50">2.11</td>
								<td width="50">1.21</td>
								<td width="50">63%</td>
								<td width="50">11%</td>
								<td width="50">26%</td>
								<td width="40">38</td>
								<!-- 	
					<td align="center" class="bor_r2"><a
						href="182"
						title="" class="a5" target="_blank">WWLWWL</a></td>
				 -->
							</tr>
							<tr class="shuangtr">
								<td width="70"><b class="no1">6</b></td>
								<td class="team_logo" width="90"><img
									src="../content/images/yijia/2013120103644.jpg"> <a
									href="http://saishi.zgzcw.com/soccer/team/150" title="AC米兰"
									target="_blank">AC米兰</a></td>
								<td width="40">19</td>
								<td width="40">12</td>
								<td width="40">2</td>
								<td width="40">5</td>
								<td width="40">32</td>
								<td width="40">20</td>
								<td width="40">12</td>
								<td width="50">1.68</td>
								<td width="50">1.05</td>
								<td width="50">63%</td>
								<td width="50">11%</td>
								<td width="50">26%</td>
								<td width="40">38</td>
								<!-- 	
					<td align="center" class="bor_r2"><a
						href="150"
						title="" class="a5" target="_blank">WWWLLW</a></td>
				 -->
							</tr>
							<tr>
								<td width="70"><b class="no1">7</b></td>
								<td class="team_logo" width="90"><img
									src="../content/images/yijia/2013120112611.jpg"> <a
									href="http://saishi.zgzcw.com/soccer/team/176" title="佛罗伦萨"
									target="_blank">佛罗伦萨</a></td>
								<td width="40">19</td>
								<td width="40">10</td>
								<td width="40">8</td>
								<td width="40">1</td>
								<td width="40">34</td>
								<td width="40">23</td>
								<td width="40">11</td>
								<td width="50">1.79</td>
								<td width="50">1.21</td>
								<td width="50">53%</td>
								<td width="50">42%</td>
								<td width="50">5%</td>
								<td width="40">38</td>
								<!-- 	
					<td align="center" class="bor_r2"><a
						href="176"
						title="" class="a5" target="_blank">WWLWWD</a></td>
				 -->
							</tr>
							<tr class="shuangtr">
								<td width="70"><b class="no1">8</b></td>
								<td class="team_logo" width="90"><img
									src="../content/images/yijia/2013120103956.jpg"> <a
									href="http://saishi.zgzcw.com/soccer/team/152" title="国际米兰"
									target="_blank">国际米兰</a></td>
								<td width="40">19</td>
								<td width="40">11</td>
								<td width="40">3</td>
								<td width="40">5</td>
								<td width="40">44</td>
								<td width="40">22</td>
								<td width="40">22</td>
								<td width="50">2.32</td>
								<td width="50">1.16</td>
								<td width="50">58%</td>
								<td width="50">16%</td>
								<td width="50">26%</td>
								<td width="40">36</td>
								<!-- 	
					<td align="center" class="bor_r2"><a
						href="152"
						title="" class="a5" target="_blank">WLDLLW</a></td>
				 -->
							</tr>
							<tr>
								<td width="70"><b class="no1">9</b></td>
								<td class="team_logo" width="90"><img
									src="../content/images/yijia/201313092945.jpg"> <a
									href="http://saishi.zgzcw.com/soccer/team/183" title="卡利亚里"
									target="_blank">卡利亚里</a></td>
								<td width="40">19</td>
								<td width="40">11</td>
								<td width="40">3</td>
								<td width="40">5</td>
								<td width="40">38</td>
								<td width="40">34</td>
								<td width="40">4</td>
								<td width="50">2</td>
								<td width="50">1.79</td>
								<td width="50">58%</td>
								<td width="50">16%</td>
								<td width="50">26%</td>
								<td width="40">36</td>
								<!-- 	
					<td align="center" class="bor_r2"><a
						href="183"
						title="" class="a5" target="_blank">DLWWWW</a></td>
				 -->
							</tr>
							<tr class="shuangtr">
								<td width="70"><b class="no1">10</b></td>
								<td class="team_logo" width="90"><img
									src="../content/images/yijia/201313093257.jpg"> <a
									href="http://saishi.zgzcw.com/soccer/team/558" title="都灵"
									target="_blank">都灵</a></td>
								<td width="40">19</td>
								<td width="40">9</td>
								<td width="40">8</td>
								<td width="40">2</td>
								<td width="40">43</td>
								<td width="40">31</td>
								<td width="40">12</td>
								<td width="50">2.26</td>
								<td width="50">1.63</td>
								<td width="50">47%</td>
								<td width="50">42%</td>
								<td width="50">11%</td>
								<td width="40">35</td>
								<!-- 	
					<td align="center" class="bor_r2"><a
						href="558"
						title="" class="a5" target="_blank">DDDDLW</a></td>
				 -->
							</tr>
							<tr>
								<td width="70"><b class="no1">11</b></td>
								<td class="team_logo" width="90"><img
									src="../content/images/yijia/2013120112618.jpg"> <a
									href="http://saishi.zgzcw.com/soccer/team/179" title="桑普"
									target="_blank">桑普</a></td>
								<td width="40">19</td>
								<td width="40">8</td>
								<td width="40">6</td>
								<td width="40">5</td>
								<td width="40">28</td>
								<td width="40">23</td>
								<td width="40">5</td>
								<td width="50">1.47</td>
								<td width="50">1.21</td>
								<td width="50">42%</td>
								<td width="50">32%</td>
								<td width="50">26%</td>
								<td width="40">30</td>
								<!-- 	
					<td align="center" class="bor_r2"><a
						href="179"
						title="" class="a5" target="_blank">WLDLDL</a></td>
				 -->
							</tr>
							<tr class="shuangtr">
								<td width="70"><b class="no1">12</b></td>
								<td class="team_logo" width="90"><img
									src="../content/images/yijia/201313092708.jpg"> <a
									href="http://saishi.zgzcw.com/soccer/team/187" title="乌迪内斯"
									target="_blank">乌迪内斯</a></td>
								<td width="40">19</td>
								<td width="40">8</td>
								<td width="40">5</td>
								<td width="40">6</td>
								<td width="40">30</td>
								<td width="40">23</td>
								<td width="40">7</td>
								<td width="50">1.58</td>
								<td width="50">1.21</td>
								<td width="50">42%</td>
								<td width="50">26%</td>
								<td width="50">32%</td>
								<td width="40">29</td>
								<!-- 	
					<td align="center" class="bor_r2"><a
						href="187"
						title="" class="a5" target="_blank">DWWWDD</a></td>
				 -->
							</tr>
							<tr>
								<td width="70"><b class="no1">13</b></td>
								<td class="team_logo" width="90"><img
									src="../content/images/yijia/2013120112638.jpg"> <a
									href="http://saishi.zgzcw.com/soccer/team/185" title="博洛尼亚"
									target="_blank">博洛尼亚</a></td>
								<td width="40">19</td>
								<td width="40">8</td>
								<td width="40">2</td>
								<td width="40">9</td>
								<td width="40">24</td>
								<td width="40">25</td>
								<td width="40">-1</td>
								<td width="50">1.26</td>
								<td width="50">1.32</td>
								<td width="50">42%</td>
								<td width="50">11%</td>
								<td width="50">47%</td>
								<td width="40">26</td>
								<!-- 	
					<td align="center" class="bor_r2"><a
						href="185"
						title="" class="a5" target="_blank">LWLWWL</a></td>
				 -->
							</tr>
							<tr class="shuangtr">
								<td width="70"><b class="no1">14</b></td>
								<td class="team_logo" width="90"><img
									src="../content/images/yijia/2013120112712.jpg"> <a
									href="http://saishi.zgzcw.com/soccer/team/552" title="热那亚"
									target="_blank">热那亚</a></td>
								<td width="40">19</td>
								<td width="40">6</td>
								<td width="40">7</td>
								<td width="40">6</td>
								<td width="40">24</td>
								<td width="40">24</td>
								<td width="40">0</td>
								<td width="50">1.26</td>
								<td width="50">1.26</td>
								<td width="50">32%</td>
								<td width="50">37%</td>
								<td width="50">32%</td>
								<td width="40">25</td>
								<!-- 	
					<td align="center" class="bor_r2"><a
						href="552"
						title="" class="a5" target="_blank">LLDLWW</a></td>
				 -->
							</tr>
							<tr>
								<td width="70"><b class="no1">15</b></td>
								<td class="team_logo" width="90"><img
									src="../content/images/yijia/2013121192851.jpg"> <a
									href="http://saishi.zgzcw.com/soccer/team/2960" title="萨索洛"
									target="_blank">萨索洛</a></td>
								<td width="40">19</td>
								<td width="40">7</td>
								<td width="40">3</td>
								<td width="40">9</td>
								<td width="40">27</td>
								<td width="40">28</td>
								<td width="40">-1</td>
								<td width="50">1.42</td>
								<td width="50">1.47</td>
								<td width="50">37%</td>
								<td width="50">16%</td>
								<td width="50">47%</td>
								<td width="40">24</td>
								<!-- 	
					<td align="center" class="bor_r2"><a
						href="2960"
						title="" class="a5" target="_blank">LLWDDW</a></td>
				 -->
							</tr>
							<tr class="shuangtr">
								<td width="70"><b class="no1">16</b></td>
								<td class="team_logo" width="90"><img
									src="../content/images/yijia/2013120112658.jpg"> <a
									href="http://saishi.zgzcw.com/soccer/team/195" title="切沃"
									target="_blank">切沃</a></td>
								<td width="40">19</td>
								<td width="40">6</td>
								<td width="40">5</td>
								<td width="40">8</td>
								<td width="40">25</td>
								<td width="40">30</td>
								<td width="40">-5</td>
								<td width="50">1.32</td>
								<td width="50">1.58</td>
								<td width="50">32%</td>
								<td width="50">26%</td>
								<td width="50">42%</td>
								<td width="40">23</td>
								<!-- 	
					<td align="center" class="bor_r2"><a
						href="195"
						title="" class="a5" target="_blank">WWLLDL</a></td>
				 -->
							</tr>
							<tr>
								<td width="70"><b class="no1">17</b></td>
								<td class="team_logo" width="90"><img
									src="../content/images/yijia/20130915170354.jpg"> <a
									href="http://saishi.zgzcw.com/soccer/team/554" title="克罗托内"
									target="_blank">克罗托内</a></td>
								<td width="40">19</td>
								<td width="40">6</td>
								<td width="40">4</td>
								<td width="40">9</td>
								<td width="40">21</td>
								<td width="40">25</td>
								<td width="40">-4</td>
								<td width="50">1.11</td>
								<td width="50">1.32</td>
								<td width="50">32%</td>
								<td width="50">21%</td>
								<td width="50">47%</td>
								<td width="40">22</td>
								<!-- 	
					<td align="center" class="bor_r2"><a
						href="554"
						title="" class="a5" target="_blank">DLWDWW</a></td>
				 -->
							</tr>
							<tr class="shuangtr">
								<td width="70"><b class="no1">18</b></td>
								<td class="team_logo" width="90"><img
									src="../content/images/yijia/2013121192718.jpg"> <a
									href="http://saishi.zgzcw.com/soccer/team/523" title="恩波利"
									target="_blank">恩波利</a></td>
								<td width="40">19</td>
								<td width="40">5</td>
								<td width="40">4</td>
								<td width="40">10</td>
								<td width="40">16</td>
								<td width="40">29</td>
								<td width="40">-13</td>
								<td width="50">0.84</td>
								<td width="50">1.53</td>
								<td width="50">26%</td>
								<td width="50">21%</td>
								<td width="50">53%</td>
								<td width="40">19</td>
								<!-- 	
					<td align="center" class="bor_r2"><a
						href="523"
						title="" class="a5" target="_blank">LLDLWL</a></td>
				 -->
							</tr>
							<tr>
								<td width="70"><b class="no1">19</b></td>
								<td class="team_logo" width="90"><img
									src="../content/images/yijia/2013120112541.jpg"> <a
									href="http://saishi.zgzcw.com/soccer/team/168" title="巴勒莫"
									target="_blank">巴勒莫</a></td>
								<td width="40">19</td>
								<td width="40">4</td>
								<td width="40">3</td>
								<td width="40">12</td>
								<td width="40">13</td>
								<td width="40">30</td>
								<td width="40">-17</td>
								<td width="50">0.68</td>
								<td width="50">1.58</td>
								<td width="50">21%</td>
								<td width="50">16%</td>
								<td width="50">63%</td>
								<td width="40">15</td>
								<!-- 	
					<td align="center" class="bor_r2"><a
						href="168"
						title="" class="a5" target="_blank">LLDWWW</a></td>
				 -->
							</tr>
							<tr class="shuangtr">
								<td width="70"><b class="no1">20</b></td>
								<td class="team_logo" width="90"><img
									src="../content/images/yijia/201313085709.gif"> <a
									href="http://saishi.zgzcw.com/soccer/team/560" title="佩斯卡拉"
									target="_blank">佩斯卡拉</a></td>
								<td width="40">19</td>
								<td width="40">2</td>
								<td width="40">5</td>
								<td width="40">12</td>
								<td width="40">19</td>
								<td width="40">38</td>
								<td width="40">-19</td>
								<td width="50">1</td>
								<td width="50">2</td>
								<td width="50">11%</td>
								<td width="50">26%</td>
								<td width="50">63%</td>
								<td width="40">11</td>
								<!-- 	
					<td align="center" class="bor_r2"><a
						href="560"
						title="" class="a5" target="_blank">LDLLLW</a></td>
				 -->
							</tr>
						</tbody>
					</table>
				</ul>
				<ul class="tabs1_main_ul" id="tabs1_main_3" style="display: none;">
					<table class="zstab" width="100%" cellspacing="0" cellpadding="0"
						border="0">
						<thead>
							<tr>
								<th width="50">排名</th>
								<th width="140">球队名称</th>
								<th width="40">赛</th>
								<th width="40">胜</th>
								<th width="40">平</th>
								<th width="40">负</th>
								<th width="40">得</th>
								<th width="40">失</th>
								<th width="40">净</th>
								<th width="50">均得</th>
								<th width="50">均失</th>
								<th width="50">胜率</th>
								<th width="50">平率</th>
								<th width="50">负率</th>
								<th width="40">积分</th>
							</tr>
						</thead>
						<tbody>
							<tr>
								<td width="70"><b class="no1">1</b></td>
								<td class="team_logo" width="90"><img
									src="../content/images/yijia/2013120112732.jpg"> <a
									href="http://saishi.zgzcw.com/soccer/team/1419" title="那不勒斯"
									target="_blank">那不勒斯</a></td>
								<td width="40">19</td>
								<td width="40">13</td>
								<td width="40">4</td>
								<td width="40">2</td>
								<td width="40">50</td>
								<td width="40">20</td>
								<td width="40">30</td>
								<td width="50">2.63</td>
								<td width="50">1.05</td>
								<td width="50">68%</td>
								<td width="50">21%</td>
								<td width="50">11%</td>
								<td width="40">43</td>
								<!-- 	
					<td align="center" class="bor_r2"><a
						href="1419"
						title="" class="a5" target="_blank">WWDWWW</a></td>
				 -->
							</tr>
							<tr class="shuangtr">
								<td width="70"><b class="no1">2</b></td>
								<td class="team_logo" width="90"><img
									src="../content/images/yijia/20140806065357.jpg"> <a
									href="http://saishi.zgzcw.com/soccer/team/174" title="罗马"
									target="_blank">罗马</a></td>
								<td width="40">19</td>
								<td width="40">12</td>
								<td width="40">2</td>
								<td width="40">5</td>
								<td width="40">40</td>
								<td width="40">20</td>
								<td width="40">20</td>
								<td width="50">2.11</td>
								<td width="50">1.05</td>
								<td width="50">63%</td>
								<td width="50">11%</td>
								<td width="50">26%</td>
								<td width="40">38</td>
								<!-- 	
					<td align="center" class="bor_r2"><a
						href="174"
						title="" class="a5" target="_blank">WWWWWW</a></td>
				 -->
							</tr>
							<tr>
								<td width="70"><b class="no1">3</b></td>
								<td class="team_logo" width="90"><img
									src="../content/images/yijia/20140324112025.jpg"> <a
									href="http://saishi.zgzcw.com/soccer/team/166" title="尤文图斯"
									target="_blank">尤文图斯</a></td>
								<td width="40">19</td>
								<td width="40">11</td>
								<td width="40">3</td>
								<td width="40">5</td>
								<td width="40">29</td>
								<td width="40">18</td>
								<td width="40">11</td>
								<td width="50">1.53</td>
								<td width="50">0.95</td>
								<td width="50">58%</td>
								<td width="50">16%</td>
								<td width="50">26%</td>
								<td width="40">36</td>
								<!-- 	
					<td align="center" class="bor_r2"><a
						href="166"
						title="" class="a5" target="_blank">WDWDLW</a></td>
				 -->
							</tr>
							<tr class="shuangtr">
								<td width="70"><b class="no1">4</b></td>
								<td class="team_logo" width="90"><img
									src="../content/images/yijia/2013120104409.jpg"> <a
									href="http://saishi.zgzcw.com/soccer/team/154" title="亚特兰大"
									target="_blank">亚特兰大</a></td>
								<td width="40">19</td>
								<td width="40">9</td>
								<td width="40">5</td>
								<td width="40">5</td>
								<td width="40">31</td>
								<td width="40">23</td>
								<td width="40">8</td>
								<td width="50">1.63</td>
								<td width="50">1.21</td>
								<td width="50">47%</td>
								<td width="50">26%</td>
								<td width="50">26%</td>
								<td width="40">32</td>
								<!-- 	
					<td align="center" class="bor_r2"><a
						href="154"
						title="" class="a5" target="_blank">WLWDDW</a></td>
				 -->
							</tr>
							<tr>
								<td width="70"><b class="no1">5</b></td>
								<td class="team_logo" width="90"><img
									src="../content/images/yijia/2013120112625.jpg"> <a
									href="http://saishi.zgzcw.com/soccer/team/182" title="拉齐奥"
									target="_blank">拉齐奥</a></td>
								<td width="40">19</td>
								<td width="40">9</td>
								<td width="40">5</td>
								<td width="40">5</td>
								<td width="40">34</td>
								<td width="40">28</td>
								<td width="40">6</td>
								<td width="50">1.79</td>
								<td width="50">1.47</td>
								<td width="50">47%</td>
								<td width="50">26%</td>
								<td width="50">26%</td>
								<td width="40">32</td>
								<!-- 	
					<td align="center" class="bor_r2"><a
						href="182"
						title="" class="a5" target="_blank">DWDWLL</a></td>
				 -->
							</tr>
							<tr class="shuangtr">
								<td width="70"><b class="no1">6</b></td>
								<td class="team_logo" width="90"><img
									src="../content/images/yijia/2013120103956.jpg"> <a
									href="http://saishi.zgzcw.com/soccer/team/152" title="国际米兰"
									target="_blank">国际米兰</a></td>
								<td width="40">19</td>
								<td width="40">8</td>
								<td width="40">2</td>
								<td width="40">9</td>
								<td width="40">28</td>
								<td width="40">27</td>
								<td width="40">1</td>
								<td width="50">1.47</td>
								<td width="50">1.42</td>
								<td width="50">42%</td>
								<td width="50">11%</td>
								<td width="50">47%</td>
								<td width="40">26</td>
								<!-- 	
					<td align="center" class="bor_r2"><a
						href="152"
						title="" class="a5" target="_blank">WDLLLW</a></td>
				 -->
							</tr>
							<tr>
								<td width="70"><b class="no1">7</b></td>
								<td class="team_logo" width="90"><img
									src="../content/images/yijia/2013120103644.jpg"> <a
									href="http://saishi.zgzcw.com/soccer/team/150" title="AC米兰"
									target="_blank">AC米兰</a></td>
								<td width="40">19</td>
								<td width="40">6</td>
								<td width="40">7</td>
								<td width="40">6</td>
								<td width="40">25</td>
								<td width="40">25</td>
								<td width="40">0</td>
								<td width="50">1.32</td>
								<td width="50">1.32</td>
								<td width="50">32%</td>
								<td width="50">37%</td>
								<td width="50">32%</td>
								<td width="40">25</td>
								<!-- 	
					<td align="center" class="bor_r2"><a
						href="150"
						title="" class="a5" target="_blank">LDDDDL</a></td>
				 -->
							</tr>
							<tr class="shuangtr">
								<td width="70"><b class="no1">8</b></td>
								<td class="team_logo" width="90"><img
									src="../content/images/yijia/2013121192851.jpg"> <a
									href="http://saishi.zgzcw.com/soccer/team/2960" title="萨索洛"
									target="_blank">萨索洛</a></td>
								<td width="40">19</td>
								<td width="40">6</td>
								<td width="40">4</td>
								<td width="40">9</td>
								<td width="40">31</td>
								<td width="40">35</td>
								<td width="40">-4</td>
								<td width="50">1.63</td>
								<td width="50">1.84</td>
								<td width="50">32%</td>
								<td width="50">21%</td>
								<td width="50">47%</td>
								<td width="40">22</td>
								<!-- 	
					<td align="center" class="bor_r2"><a
						href="2960"
						title="" class="a5" target="_blank">DLDWWL</a></td>
				 -->
							</tr>
							<tr>
								<td width="70"><b class="no1">9</b></td>
								<td class="team_logo" width="90"><img
									src="../content/images/yijia/2013120112611.jpg"> <a
									href="http://saishi.zgzcw.com/soccer/team/176" title="佛罗伦萨"
									target="_blank">佛罗伦萨</a></td>
								<td width="40">19</td>
								<td width="40">6</td>
								<td width="40">4</td>
								<td width="40">9</td>
								<td width="40">29</td>
								<td width="40">34</td>
								<td width="40">-5</td>
								<td width="50">1.53</td>
								<td width="50">1.79</td>
								<td width="50">32%</td>
								<td width="50">21%</td>
								<td width="50">47%</td>
								<td width="40">22</td>
								<!-- 	
					<td align="center" class="bor_r2"><a
						href="176"
						title="" class="a5" target="_blank">DWDLDL</a></td>
				 -->
							</tr>
							<tr class="shuangtr">
								<td width="70"><b class="no1">10</b></td>
								<td class="team_logo" width="90"><img
									src="../content/images/yijia/2013120112658.jpg"> <a
									href="http://saishi.zgzcw.com/soccer/team/195" title="切沃"
									target="_blank">切沃</a></td>
								<td width="40">19</td>
								<td width="40">6</td>
								<td width="40">2</td>
								<td width="40">11</td>
								<td width="40">18</td>
								<td width="40">31</td>
								<td width="40">-13</td>
								<td width="50">0.95</td>
								<td width="50">1.63</td>
								<td width="50">32%</td>
								<td width="50">11%</td>
								<td width="50">58%</td>
								<td width="40">20</td>
								<!-- 	
					<td align="center" class="bor_r2"><a
						href="195"
						title="" class="a5" target="_blank">LLLWDL</a></td>
				 -->
							</tr>
							<tr>
								<td width="70"><b class="no1">11</b></td>
								<td class="team_logo" width="90"><img
									src="../content/images/yijia/201313093257.jpg"> <a
									href="http://saishi.zgzcw.com/soccer/team/558" title="都灵"
									target="_blank">都灵</a></td>
								<td width="40">19</td>
								<td width="40">4</td>
								<td width="40">6</td>
								<td width="40">9</td>
								<td width="40">28</td>
								<td width="40">35</td>
								<td width="40">-7</td>
								<td width="50">1.47</td>
								<td width="50">1.84</td>
								<td width="50">21%</td>
								<td width="50">32%</td>
								<td width="50">47%</td>
								<td width="40">18</td>
								<!-- 	
					<td align="center" class="bor_r2"><a
						href="558"
						title="" class="a5" target="_blank">DLWWDL</a></td>
				 -->
							</tr>
							<tr class="shuangtr">
								<td width="70"><b class="no1">12</b></td>
								<td class="team_logo" width="90"><img
									src="../content/images/yijia/2013120112618.jpg"> <a
									href="http://saishi.zgzcw.com/soccer/team/179" title="桑普"
									target="_blank">桑普</a></td>
								<td width="40">19</td>
								<td width="40">4</td>
								<td width="40">6</td>
								<td width="40">9</td>
								<td width="40">21</td>
								<td width="40">32</td>
								<td width="40">-11</td>
								<td width="50">1.11</td>
								<td width="50">1.68</td>
								<td width="50">21%</td>
								<td width="50">32%</td>
								<td width="50">47%</td>
								<td width="40">18</td>
								<!-- 	
					<td align="center" class="bor_r2"><a
						href="179"
						title="" class="a5" target="_blank">WWLDLD</a></td>
				 -->
							</tr>
							<tr>
								<td width="70"><b class="no1">13</b></td>
								<td class="team_logo" width="90"><img
									src="../content/images/yijia/201313092708.jpg"> <a
									href="http://saishi.zgzcw.com/soccer/team/187" title="乌迪内斯"
									target="_blank">乌迪内斯</a></td>
								<td width="40">19</td>
								<td width="40">4</td>
								<td width="40">4</td>
								<td width="40">11</td>
								<td width="40">17</td>
								<td width="40">33</td>
								<td width="40">-16</td>
								<td width="50">0.89</td>
								<td width="50">1.74</td>
								<td width="50">21%</td>
								<td width="50">21%</td>
								<td width="50">58%</td>
								<td width="40">16</td>
								<!-- 	
					<td align="center" class="bor_r2"><a
						href="187"
						title="" class="a5" target="_blank">WDLLLL</a></td>
				 -->
							</tr>
							<tr class="shuangtr">
								<td width="70"><b class="no1">14</b></td>
								<td class="team_logo" width="90"><img
									src="../content/images/yijia/2013120112638.jpg"> <a
									href="http://saishi.zgzcw.com/soccer/team/185" title="博洛尼亚"
									target="_blank">博洛尼亚</a></td>
								<td width="40">19</td>
								<td width="40">3</td>
								<td width="40">6</td>
								<td width="40">10</td>
								<td width="40">16</td>
								<td width="40">33</td>
								<td width="40">-17</td>
								<td width="50">0.84</td>
								<td width="50">1.74</td>
								<td width="50">16%</td>
								<td width="50">32%</td>
								<td width="50">53%</td>
								<td width="40">15</td>
								<!-- 	
					<td align="center" class="bor_r2"><a
						href="185"
						title="" class="a5" target="_blank">WLDLLL</a></td>
				 -->
							</tr>
							<tr>
								<td width="70"><b class="no1">15</b></td>
								<td class="team_logo" width="90"><img
									src="../content/images/yijia/2013121192718.jpg"> <a
									href="http://saishi.zgzcw.com/soccer/team/523" title="恩波利"
									target="_blank">恩波利</a></td>
								<td width="40">19</td>
								<td width="40">3</td>
								<td width="40">4</td>
								<td width="40">12</td>
								<td width="40">13</td>
								<td width="40">32</td>
								<td width="40">-19</td>
								<td width="50">0.68</td>
								<td width="50">1.68</td>
								<td width="50">16%</td>
								<td width="50">21%</td>
								<td width="50">63%</td>
								<td width="40">13</td>
								<!-- 	
					<td align="center" class="bor_r2"><a
						href="523"
						title="" class="a5" target="_blank">LLWWLL</a></td>
				 -->
							</tr>
							<tr class="shuangtr">
								<td width="70"><b class="no1">16</b></td>
								<td class="team_logo" width="90"><img
									src="../content/images/yijia/20130915170354.jpg"> <a
									href="http://saishi.zgzcw.com/soccer/team/554" title="克罗托内"
									target="_blank">克罗托内</a></td>
								<td width="40">19</td>
								<td width="40">3</td>
								<td width="40">3</td>
								<td width="40">13</td>
								<td width="40">13</td>
								<td width="40">33</td>
								<td width="40">-20</td>
								<td width="50">0.68</td>
								<td width="50">1.74</td>
								<td width="50">16%</td>
								<td width="50">16%</td>
								<td width="50">68%</td>
								<td width="40">12</td>
								<!-- 	
					<td align="center" class="bor_r2"><a
						href="554"
						title="" class="a5" target="_blank">LWDWWL</a></td>
				 -->
							</tr>
							<tr>
								<td width="70"><b class="no1">17</b></td>
								<td class="team_logo" width="90"><img
									src="../content/images/yijia/201313092945.jpg"> <a
									href="http://saishi.zgzcw.com/soccer/team/183" title="卡利亚里"
									target="_blank">卡利亚里</a></td>
								<td width="40">19</td>
								<td width="40">3</td>
								<td width="40">2</td>
								<td width="40">14</td>
								<td width="40">17</td>
								<td width="40">42</td>
								<td width="40">-25</td>
								<td width="50">0.89</td>
								<td width="50">2.21</td>
								<td width="50">16%</td>
								<td width="50">11%</td>
								<td width="50">74%</td>
								<td width="40">11</td>
								<!-- 	
					<td align="center" class="bor_r2"><a
						href="183"
						title="" class="a5" target="_blank">WLWLLL</a></td>
				 -->
							</tr>
							<tr class="shuangtr">
								<td width="70"><b class="no1">18</b></td>
								<td class="team_logo" width="90"><img
									src="../content/images/yijia/2013120112712.jpg"> <a
									href="http://saishi.zgzcw.com/soccer/team/552" title="热那亚"
									target="_blank">热那亚</a></td>
								<td width="40">19</td>
								<td width="40">3</td>
								<td width="40">2</td>
								<td width="40">14</td>
								<td width="40">14</td>
								<td width="40">40</td>
								<td width="40">-26</td>
								<td width="50">0.74</td>
								<td width="50">2.11</td>
								<td width="50">16%</td>
								<td width="50">11%</td>
								<td width="50">74%</td>
								<td width="40">11</td>
								<!-- 	
					<td align="center" class="bor_r2"><a
						href="552"
						title="" class="a5" target="_blank">WLLLLL</a></td>
				 -->
							</tr>
							<tr>
								<td width="70"><b class="no1">19</b></td>
								<td class="team_logo" width="90"><img
									src="../content/images/yijia/2013120112541.jpg"> <a
									href="http://saishi.zgzcw.com/soccer/team/168" title="巴勒莫"
									target="_blank">巴勒莫</a></td>
								<td width="40">19</td>
								<td width="40">2</td>
								<td width="40">5</td>
								<td width="40">12</td>
								<td width="40">20</td>
								<td width="40">47</td>
								<td width="40">-27</td>
								<td width="50">1.05</td>
								<td width="50">2.47</td>
								<td width="50">11%</td>
								<td width="50">26%</td>
								<td width="50">63%</td>
								<td width="40">11</td>
								<!-- 	
					<td align="center" class="bor_r2"><a
						href="168"
						title="" class="a5" target="_blank">LLLLDL</a></td>
				 -->
							</tr>
							<tr class="shuangtr">
								<td width="70"><b class="no1">20</b></td>
								<td class="team_logo" width="90"><img
									src="../content/images/yijia/201313085709.gif"> <a
									href="http://saishi.zgzcw.com/soccer/team/560" title="佩斯卡拉"
									target="_blank">佩斯卡拉</a></td>
								<td width="40">19</td>
								<td width="40">1</td>
								<td width="40">4</td>
								<td width="40">14</td>
								<td width="40">18</td>
								<td width="40">43</td>
								<td width="40">-25</td>
								<td width="50">0.95</td>
								<td width="50">2.26</td>
								<td width="50">5%</td>
								<td width="50">21%</td>
								<td width="50">74%</td>
								<td width="40">7</td>
								<!-- 	
					<td align="center" class="bor_r2"><a
						href="560"
						title="" class="a5" target="_blank">LLDLLD</a></td>
				 -->
							</tr>
						</tbody>
					</table>
				</ul>
				<ul class="tabs1_main_ul" id="tabs1_main_4" style="display: none;">
					<table class="zstab" width="100%" cellspacing="0" cellpadding="0"
						border="0">
						<thead>
							<tr>
								<th width="50">排名</th>
								<th width="140">球队名称</th>
								<th width="40">赛</th>
								<th width="40">胜</th>
								<th width="40">平</th>
								<th width="40">负</th>
								<th width="40">得</th>
								<th width="40">失</th>
								<th width="40">净</th>
								<th width="50">均得</th>
								<th width="50">均失</th>
								<th width="50">胜率</th>
								<th width="50">平率</th>
								<th width="50">负率</th>
								<th width="40">积分</th>
							</tr>
						</thead>
						<tbody>
							<tr>
								<td width="70"><b class="no1">1</b></td>
								<td class="team_logo" width="90"><img
									src="../content/images/yijia/2013120112732.jpg"> <a
									href="http://saishi.zgzcw.com/soccer/team/1419" title="那不勒斯"
									target="_blank">那不勒斯</a></td>
								<td width="40">6</td>
								<td width="40">5</td>
								<td width="40">1</td>
								<td width="40">0</td>
								<td width="40">19</td>
								<td width="40">6</td>
								<td width="40">13</td>
								<td width="50">3.17</td>
								<td width="50">1</td>
								<td width="50">83%</td>
								<td width="50">17%</td>
								<td width="50">0%</td>
								<td width="40">16</td>
								<!-- 	 -->
							</tr>
							<tr class="shuangtr">
								<td width="70"><b class="no1">2</b></td>
								<td class="team_logo" width="90"><img
									src="../content/images/yijia/20140806065357.jpg"> <a
									href="http://saishi.zgzcw.com/soccer/team/174" title="罗马"
									target="_blank">罗马</a></td>
								<td width="40">6</td>
								<td width="40">5</td>
								<td width="40">0</td>
								<td width="40">1</td>
								<td width="40">20</td>
								<td width="40">11</td>
								<td width="40">9</td>
								<td width="50">3.33</td>
								<td width="50">1.83</td>
								<td width="50">83%</td>
								<td width="50">0%</td>
								<td width="50">17%</td>
								<td width="40">15</td>
								<!-- 	 -->
							</tr>
							<tr>
								<td width="70"><b class="no1">3</b></td>
								<td class="team_logo" width="90"><img
									src="../content/images/yijia/20130915170354.jpg"> <a
									href="http://saishi.zgzcw.com/soccer/team/554" title="克罗托内"
									target="_blank">克罗托内</a></td>
								<td width="40">6</td>
								<td width="40">4</td>
								<td width="40">1</td>
								<td width="40">1</td>
								<td width="40">8</td>
								<td width="40">6</td>
								<td width="40">2</td>
								<td width="50">1.33</td>
								<td width="50">1</td>
								<td width="50">67%</td>
								<td width="50">17%</td>
								<td width="50">17%</td>
								<td width="40">13</td>
								<!-- 	 -->
							</tr>
							<tr class="shuangtr">
								<td width="70"><b class="no1">4</b></td>
								<td class="team_logo" width="90"><img
									src="../content/images/yijia/2013120104409.jpg"> <a
									href="http://saishi.zgzcw.com/soccer/team/154" title="亚特兰大"
									target="_blank">亚特兰大</a></td>
								<td width="40">6</td>
								<td width="40">3</td>
								<td width="40">3</td>
								<td width="40">0</td>
								<td width="40">9</td>
								<td width="40">6</td>
								<td width="40">3</td>
								<td width="50">1.5</td>
								<td width="50">1</td>
								<td width="50">50%</td>
								<td width="50">50%</td>
								<td width="50">0%</td>
								<td width="40">12</td>
								<!-- 	 -->
							</tr>
							<tr>
								<td width="70"><b class="no1">5</b></td>
								<td class="team_logo" width="90"><img
									src="../content/images/yijia/20140324112025.jpg"> <a
									href="http://saishi.zgzcw.com/soccer/team/166" title="尤文图斯"
									target="_blank">尤文图斯</a></td>
								<td width="40">6</td>
								<td width="40">3</td>
								<td width="40">2</td>
								<td width="40">1</td>
								<td width="40">13</td>
								<td width="40">7</td>
								<td width="40">6</td>
								<td width="50">2.17</td>
								<td width="50">1.17</td>
								<td width="50">50%</td>
								<td width="50">33%</td>
								<td width="50">17%</td>
								<td width="40">11</td>
								<!-- 	 -->
							</tr>
							<tr class="shuangtr">
								<td width="70"><b class="no1">6</b></td>
								<td class="team_logo" width="90"><img
									src="../content/images/yijia/2013121192851.jpg"> <a
									href="http://saishi.zgzcw.com/soccer/team/2960" title="萨索洛"
									target="_blank">萨索洛</a></td>
								<td width="40">6</td>
								<td width="40">3</td>
								<td width="40">2</td>
								<td width="40">1</td>
								<td width="40">18</td>
								<td width="40">13</td>
								<td width="40">5</td>
								<td width="50">3</td>
								<td width="50">2.17</td>
								<td width="50">50%</td>
								<td width="50">33%</td>
								<td width="50">17%</td>
								<td width="40">11</td>
								<!-- 	 -->
							</tr>
							<tr>
								<td width="70"><b class="no1">7</b></td>
								<td class="team_logo" width="90"><img
									src="../content/images/yijia/2013120112541.jpg"> <a
									href="http://saishi.zgzcw.com/soccer/team/168" title="巴勒莫"
									target="_blank">巴勒莫</a></td>
								<td width="40">6</td>
								<td width="40">3</td>
								<td width="40">1</td>
								<td width="40">2</td>
								<td width="40">8</td>
								<td width="40">10</td>
								<td width="40">-2</td>
								<td width="50">1.33</td>
								<td width="50">1.67</td>
								<td width="50">50%</td>
								<td width="50">17%</td>
								<td width="50">33%</td>
								<td width="40">10</td>
								<!-- 	 -->
							</tr>
							<tr class="shuangtr">
								<td width="70"><b class="no1">8</b></td>
								<td class="team_logo" width="90"><img
									src="../content/images/yijia/2013120112625.jpg"> <a
									href="http://saishi.zgzcw.com/soccer/team/182" title="拉齐奥"
									target="_blank">拉齐奥</a></td>
								<td width="40">6</td>
								<td width="40">3</td>
								<td width="40">0</td>
								<td width="40">3</td>
								<td width="40">20</td>
								<td width="40">15</td>
								<td width="40">5</td>
								<td width="50">3.33</td>
								<td width="50">2.5</td>
								<td width="50">50%</td>
								<td width="50">0%</td>
								<td width="50">50%</td>
								<td width="40">9</td>
								<!-- 	 -->
							</tr>
							<tr>
								<td width="70"><b class="no1">9</b></td>
								<td class="team_logo" width="90"><img
									src="../content/images/yijia/201313092945.jpg"> <a
									href="http://saishi.zgzcw.com/soccer/team/183" title="卡利亚里"
									target="_blank">卡利亚里</a></td>
								<td width="40">6</td>
								<td width="40">3</td>
								<td width="40">0</td>
								<td width="40">3</td>
								<td width="40">10</td>
								<td width="40">14</td>
								<td width="40">-4</td>
								<td width="50">1.67</td>
								<td width="50">2.33</td>
								<td width="50">50%</td>
								<td width="50">0%</td>
								<td width="50">50%</td>
								<td width="40">9</td>
								<!-- 	 -->
							</tr>
							<tr class="shuangtr">
								<td width="70"><b class="no1">10</b></td>
								<td class="team_logo" width="90"><img
									src="../content/images/yijia/201313093257.jpg"> <a
									href="http://saishi.zgzcw.com/soccer/team/558" title="都灵"
									target="_blank">都灵</a></td>
								<td width="40">6</td>
								<td width="40">2</td>
								<td width="40">2</td>
								<td width="40">2</td>
								<td width="40">11</td>
								<td width="40">13</td>
								<td width="40">-2</td>
								<td width="50">1.83</td>
								<td width="50">2.17</td>
								<td width="50">33%</td>
								<td width="50">33%</td>
								<td width="50">33%</td>
								<td width="40">8</td>
								<!-- 	 -->
							</tr>
							<tr>
								<td width="70"><b class="no1">11</b></td>
								<td class="team_logo" width="90"><img
									src="../content/images/yijia/2013120112611.jpg"> <a
									href="http://saishi.zgzcw.com/soccer/team/176" title="佛罗伦萨"
									target="_blank">佛罗伦萨</a></td>
								<td width="40">6</td>
								<td width="40">2</td>
								<td width="40">2</td>
								<td width="40">2</td>
								<td width="40">13</td>
								<td width="40">16</td>
								<td width="40">-3</td>
								<td width="50">2.17</td>
								<td width="50">2.67</td>
								<td width="50">33%</td>
								<td width="50">33%</td>
								<td width="50">33%</td>
								<td width="40">8</td>
								<!-- 	 -->
							</tr>
							<tr class="shuangtr">
								<td width="70"><b class="no1">12</b></td>
								<td class="team_logo" width="90"><img
									src="../content/images/yijia/2013120103956.jpg"> <a
									href="http://saishi.zgzcw.com/soccer/team/152" title="国际米兰"
									target="_blank">国际米兰</a></td>
								<td width="40">6</td>
								<td width="40">2</td>
								<td width="40">0</td>
								<td width="40">4</td>
								<td width="40">13</td>
								<td width="40">12</td>
								<td width="40">1</td>
								<td width="50">2.17</td>
								<td width="50">2</td>
								<td width="50">33%</td>
								<td width="50">0%</td>
								<td width="50">67%</td>
								<td width="40">6</td>
								<!-- 	 -->
							</tr>
							<tr>
								<td width="70"><b class="no1">13</b></td>
								<td class="team_logo" width="90"><img
									src="../content/images/yijia/2013120112638.jpg"> <a
									href="http://saishi.zgzcw.com/soccer/team/185" title="博洛尼亚"
									target="_blank">博洛尼亚</a></td>
								<td width="40">6</td>
								<td width="40">2</td>
								<td width="40">0</td>
								<td width="40">4</td>
								<td width="40">11</td>
								<td width="40">12</td>
								<td width="40">-1</td>
								<td width="50">1.83</td>
								<td width="50">2</td>
								<td width="50">33%</td>
								<td width="50">0%</td>
								<td width="50">67%</td>
								<td width="40">6</td>
								<!-- 	 -->
							</tr>
							<tr class="shuangtr">
								<td width="70"><b class="no1">14</b></td>
								<td class="team_logo" width="90"><img
									src="../content/images/yijia/2013121192718.jpg"> <a
									href="http://saishi.zgzcw.com/soccer/team/523" title="恩波利"
									target="_blank">恩波利</a></td>
								<td width="40">6</td>
								<td width="40">2</td>
								<td width="40">0</td>
								<td width="40">4</td>
								<td width="40">9</td>
								<td width="40">11</td>
								<td width="40">-2</td>
								<td width="50">1.5</td>
								<td width="50">1.83</td>
								<td width="50">33%</td>
								<td width="50">0%</td>
								<td width="50">67%</td>
								<td width="40">6</td>
								<!-- 	 -->
							</tr>
							<tr>
								<td width="70"><b class="no1">15</b></td>
								<td class="team_logo" width="90"><img
									src="../content/images/yijia/2013120112712.jpg"> <a
									href="http://saishi.zgzcw.com/soccer/team/552" title="热那亚"
									target="_blank">热那亚</a></td>
								<td width="40">6</td>
								<td width="40">2</td>
								<td width="40">0</td>
								<td width="40">4</td>
								<td width="40">6</td>
								<td width="40">11</td>
								<td width="40">-5</td>
								<td width="50">1</td>
								<td width="50">1.83</td>
								<td width="50">33%</td>
								<td width="50">0%</td>
								<td width="50">67%</td>
								<td width="40">6</td>
								<!-- 	 -->
							</tr>
							<tr class="shuangtr">
								<td width="70"><b class="no1">16</b></td>
								<td class="team_logo" width="90"><img
									src="../content/images/yijia/2013120103644.jpg"> <a
									href="http://saishi.zgzcw.com/soccer/team/150" title="AC米兰"
									target="_blank">AC米兰</a></td>
								<td width="40">6</td>
								<td width="40">1</td>
								<td width="40">2</td>
								<td width="40">3</td>
								<td width="40">8</td>
								<td width="40">10</td>
								<td width="40">-2</td>
								<td width="50">1.33</td>
								<td width="50">1.67</td>
								<td width="50">17%</td>
								<td width="50">33%</td>
								<td width="50">50%</td>
								<td width="40">5</td>
								<!-- 	 -->
							</tr>
							<tr>
								<td width="70"><b class="no1">17</b></td>
								<td class="team_logo" width="90"><img
									src="../content/images/yijia/2013120112658.jpg"> <a
									href="http://saishi.zgzcw.com/soccer/team/195" title="切沃"
									target="_blank">切沃</a></td>
								<td width="40">6</td>
								<td width="40">1</td>
								<td width="40">2</td>
								<td width="40">3</td>
								<td width="40">8</td>
								<td width="40">12</td>
								<td width="40">-4</td>
								<td width="50">1.33</td>
								<td width="50">2</td>
								<td width="50">17%</td>
								<td width="50">33%</td>
								<td width="50">50%</td>
								<td width="40">5</td>
								<!-- 	 -->
							</tr>
							<tr class="shuangtr">
								<td width="70"><b class="no1">18</b></td>
								<td class="team_logo" width="90"><img
									src="../content/images/yijia/201313092708.jpg"> <a
									href="http://saishi.zgzcw.com/soccer/team/187" title="乌迪内斯"
									target="_blank">乌迪内斯</a></td>
								<td width="40">6</td>
								<td width="40">1</td>
								<td width="40">2</td>
								<td width="40">3</td>
								<td width="40">6</td>
								<td width="40">13</td>
								<td width="40">-7</td>
								<td width="50">1</td>
								<td width="50">2.17</td>
								<td width="50">17%</td>
								<td width="50">33%</td>
								<td width="50">50%</td>
								<td width="40">5</td>
								<!-- 	 -->
							</tr>
							<tr>
								<td width="70"><b class="no1">19</b></td>
								<td class="team_logo" width="90"><img
									src="../content/images/yijia/201313085709.gif"> <a
									href="http://saishi.zgzcw.com/soccer/team/560" title="佩斯卡拉"
									target="_blank">佩斯卡拉</a></td>
								<td width="40">6</td>
								<td width="40">1</td>
								<td width="40">1</td>
								<td width="40">4</td>
								<td width="40">6</td>
								<td width="40">11</td>
								<td width="40">-5</td>
								<td width="50">1</td>
								<td width="50">1.83</td>
								<td width="50">17%</td>
								<td width="50">17%</td>
								<td width="50">67%</td>
								<td width="40">4</td>
								<!-- 	 -->
							</tr>
							<tr class="shuangtr">
								<td width="70"><b class="no1">20</b></td>
								<td class="team_logo" width="90"><img
									src="../content/images/yijia/2013120112618.jpg"> <a
									href="http://saishi.zgzcw.com/soccer/team/179" title="桑普"
									target="_blank">桑普</a></td>
								<td width="40">6</td>
								<td width="40">0</td>
								<td width="40">3</td>
								<td width="40">3</td>
								<td width="40">9</td>
								<td width="40">16</td>
								<td width="40">-7</td>
								<td width="50">1.5</td>
								<td width="50">2.67</td>
								<td width="50">0%</td>
								<td width="50">50%</td>
								<td width="50">50%</td>
								<td width="40">3</td>
								<!-- 	 -->
							</tr>
						</tbody>
					</table>
				</ul>
				<ul class="tabs1_main_ul" id="tabs1_main_5" style="display: none;">
					<table class="zstab" width="100%" cellspacing="0" cellpadding="0"
						border="0">
						<thead>
							<tr>
								<th width="50">排名</th>
								<th width="140">球队名称</th>
								<th width="40">赛</th>
								<th width="40">胜</th>
								<th width="40">平</th>
								<th width="40">负</th>
								<th width="40">得</th>
								<th width="40">失</th>
								<th width="40">净</th>
								<th width="50">均得</th>
								<th width="50">均失</th>
								<th width="50">胜率</th>
								<th width="50">平率</th>
								<th width="50">负率</th>
								<th width="40">积分</th>
							</tr>
						</thead>
						<tbody>
							<tr>
								<td width="70"><b class="no1">1</b></td>
								<td class="team_logo" width="90"><img
									src="../content/images/yijia/2013120112732.jpg"> <a
									href="http://saishi.zgzcw.com/soccer/team/1419" title="那不勒斯"
									target="_blank">那不勒斯</a></td>
								<td width="40">38</td>
								<td width="40">21</td>
								<td width="40">10</td>
								<td width="40">7</td>
								<td width="40">37</td>
								<td width="40">10</td>
								<td width="40">27</td>
								<td width="50">0.97</td>
								<td width="50">0.26</td>
								<td width="50">55%</td>
								<td width="50">26%</td>
								<td width="50">18%</td>
								<td width="40">73</td>
								<!-- 	 -->
							</tr>
							<tr class="shuangtr">
								<td width="70"><b class="no1">2</b></td>
								<td class="team_logo" width="90"><img
									src="../content/images/yijia/20140324112025.jpg"> <a
									href="http://saishi.zgzcw.com/soccer/team/166" title="尤文图斯"
									target="_blank">尤文图斯</a></td>
								<td width="40">38</td>
								<td width="40">19</td>
								<td width="40">15</td>
								<td width="40">4</td>
								<td width="40">37</td>
								<td width="40">11</td>
								<td width="40">26</td>
								<td width="50">0.97</td>
								<td width="50">0.29</td>
								<td width="50">50%</td>
								<td width="50">39%</td>
								<td width="50">11%</td>
								<td width="40">72</td>
								<!-- 	 -->
							</tr>
							<tr>
								<td width="70"><b class="no1">3</b></td>
								<td class="team_logo" width="90"><img
									src="../content/images/yijia/20140806065357.jpg"> <a
									href="http://saishi.zgzcw.com/soccer/team/174" title="罗马"
									target="_blank">罗马</a></td>
								<td width="40">38</td>
								<td width="40">20</td>
								<td width="40">12</td>
								<td width="40">6</td>
								<td width="40">35</td>
								<td width="40">15</td>
								<td width="40">20</td>
								<td width="50">0.92</td>
								<td width="50">0.39</td>
								<td width="50">53%</td>
								<td width="50">32%</td>
								<td width="50">16%</td>
								<td width="40">72</td>
								<!-- 	 -->
							</tr>
							<tr class="shuangtr">
								<td width="70"><b class="no1">4</b></td>
								<td class="team_logo" width="90"><img
									src="../content/images/yijia/2013120104409.jpg"> <a
									href="http://saishi.zgzcw.com/soccer/team/154" title="亚特兰大"
									target="_blank">亚特兰大</a></td>
								<td width="40">38</td>
								<td width="40">18</td>
								<td width="40">11</td>
								<td width="40">9</td>
								<td width="40">32</td>
								<td width="40">20</td>
								<td width="40">12</td>
								<td width="50">0.84</td>
								<td width="50">0.53</td>
								<td width="50">47%</td>
								<td width="50">29%</td>
								<td width="50">24%</td>
								<td width="40">65</td>
								<!-- 	 -->
							</tr>
							<tr>
								<td width="70"><b class="no1">5</b></td>
								<td class="team_logo" width="90"><img
									src="../content/images/yijia/2013120112625.jpg"> <a
									href="http://saishi.zgzcw.com/soccer/team/182" title="拉齐奥"
									target="_blank">拉齐奥</a></td>
								<td width="40">38</td>
								<td width="40">12</td>
								<td width="40">19</td>
								<td width="40">7</td>
								<td width="40">34</td>
								<td width="40">17</td>
								<td width="40">17</td>
								<td width="50">0.89</td>
								<td width="50">0.45</td>
								<td width="50">32%</td>
								<td width="50">50%</td>
								<td width="50">18%</td>
								<td width="40">55</td>
								<!-- 	 -->
							</tr>
							<tr class="shuangtr">
								<td width="70"><b class="no1">6</b></td>
								<td class="team_logo" width="90"><img
									src="../content/images/yijia/201313093257.jpg"> <a
									href="http://saishi.zgzcw.com/soccer/team/558" title="都灵"
									target="_blank">都灵</a></td>
								<td width="40">38</td>
								<td width="40">13</td>
								<td width="40">15</td>
								<td width="40">10</td>
								<td width="40">28</td>
								<td width="40">23</td>
								<td width="40">5</td>
								<td width="50">0.74</td>
								<td width="50">0.61</td>
								<td width="50">34%</td>
								<td width="50">39%</td>
								<td width="50">26%</td>
								<td width="40">54</td>
								<!-- 	 -->
							</tr>
							<tr>
								<td width="70"><b class="no1">7</b></td>
								<td class="team_logo" width="90"><img
									src="../content/images/yijia/2013120103956.jpg"> <a
									href="http://saishi.zgzcw.com/soccer/team/152" title="国际米兰"
									target="_blank">国际米兰</a></td>
								<td width="40">38</td>
								<td width="40">13</td>
								<td width="40">14</td>
								<td width="40">11</td>
								<td width="40">30</td>
								<td width="40">21</td>
								<td width="40">9</td>
								<td width="50">0.79</td>
								<td width="50">0.55</td>
								<td width="50">34%</td>
								<td width="50">37%</td>
								<td width="50">29%</td>
								<td width="40">53</td>
								<!-- 	 -->
							</tr>
							<tr class="shuangtr">
								<td width="70"><b class="no1">8</b></td>
								<td class="team_logo" width="90"><img
									src="../content/images/yijia/201313092708.jpg"> <a
									href="http://saishi.zgzcw.com/soccer/team/187" title="乌迪内斯"
									target="_blank">乌迪内斯</a></td>
								<td width="40">38</td>
								<td width="40">11</td>
								<td width="40">17</td>
								<td width="40">10</td>
								<td width="40">19</td>
								<td width="40">20</td>
								<td width="40">-1</td>
								<td width="50">0.5</td>
								<td width="50">0.53</td>
								<td width="50">29%</td>
								<td width="50">45%</td>
								<td width="50">26%</td>
								<td width="40">50</td>
								<!-- 	 -->
							</tr>
							<tr>
								<td width="70"><b class="no1">9</b></td>
								<td class="team_logo" width="90"><img
									src="../content/images/yijia/20130915170354.jpg"> <a
									href="http://saishi.zgzcw.com/soccer/team/554" title="克罗托内"
									target="_blank">克罗托内</a></td>
								<td width="40">38</td>
								<td width="40">11</td>
								<td width="40">16</td>
								<td width="40">11</td>
								<td width="40">17</td>
								<td width="40">21</td>
								<td width="40">-4</td>
								<td width="50">0.45</td>
								<td width="50">0.55</td>
								<td width="50">29%</td>
								<td width="50">42%</td>
								<td width="50">29%</td>
								<td width="40">49</td>
								<!-- 	 -->
							</tr>
							<tr class="shuangtr">
								<td width="70"><b class="no1">10</b></td>
								<td class="team_logo" width="90"><img
									src="../content/images/yijia/2013121192851.jpg"> <a
									href="http://saishi.zgzcw.com/soccer/team/2960" title="萨索洛"
									target="_blank">萨索洛</a></td>
								<td width="40">38</td>
								<td width="40">12</td>
								<td width="40">12</td>
								<td width="40">14</td>
								<td width="40">27</td>
								<td width="40">31</td>
								<td width="40">-4</td>
								<td width="50">0.71</td>
								<td width="50">0.82</td>
								<td width="50">32%</td>
								<td width="50">32%</td>
								<td width="50">37%</td>
								<td width="40">48</td>
								<!-- 	 -->
							</tr>
							<tr>
								<td width="70"><b class="no1">11</b></td>
								<td class="team_logo" width="90"><img
									src="../content/images/yijia/2013120112611.jpg"> <a
									href="http://saishi.zgzcw.com/soccer/team/176" title="佛罗伦萨"
									target="_blank">佛罗伦萨</a></td>
								<td width="40">38</td>
								<td width="40">13</td>
								<td width="40">8</td>
								<td width="40">17</td>
								<td width="40">22</td>
								<td width="40">25</td>
								<td width="40">-3</td>
								<td width="50">0.58</td>
								<td width="50">0.66</td>
								<td width="50">34%</td>
								<td width="50">21%</td>
								<td width="50">45%</td>
								<td width="40">47</td>
								<!-- 	 -->
							</tr>
							<tr class="shuangtr">
								<td width="70"><b class="no1">12</b></td>
								<td class="team_logo" width="90"><img
									src="../content/images/yijia/2013120103644.jpg"> <a
									href="http://saishi.zgzcw.com/soccer/team/150" title="AC米兰"
									target="_blank">AC米兰</a></td>
								<td width="40">38</td>
								<td width="40">9</td>
								<td width="40">18</td>
								<td width="40">11</td>
								<td width="40">20</td>
								<td width="40">24</td>
								<td width="40">-4</td>
								<td width="50">0.53</td>
								<td width="50">0.63</td>
								<td width="50">24%</td>
								<td width="50">47%</td>
								<td width="50">29%</td>
								<td width="40">45</td>
								<!-- 	 -->
							</tr>
							<tr>
								<td width="70"><b class="no1">13</b></td>
								<td class="team_logo" width="90"><img
									src="../content/images/yijia/2013120112618.jpg"> <a
									href="http://saishi.zgzcw.com/soccer/team/179" title="桑普"
									target="_blank">桑普</a></td>
								<td width="40">38</td>
								<td width="40">10</td>
								<td width="40">14</td>
								<td width="40">14</td>
								<td width="40">18</td>
								<td width="40">29</td>
								<td width="40">-11</td>
								<td width="50">0.47</td>
								<td width="50">0.76</td>
								<td width="50">26%</td>
								<td width="50">37%</td>
								<td width="50">37%</td>
								<td width="40">44</td>
								<!-- 	 -->
							</tr>
							<tr class="shuangtr">
								<td width="70"><b class="no1">14</b></td>
								<td class="team_logo" width="90"><img
									src="../content/images/yijia/2013120112638.jpg"> <a
									href="http://saishi.zgzcw.com/soccer/team/185" title="博洛尼亚"
									target="_blank">博洛尼亚</a></td>
								<td width="40">38</td>
								<td width="40">8</td>
								<td width="40">18</td>
								<td width="40">12</td>
								<td width="40">17</td>
								<td width="40">23</td>
								<td width="40">-6</td>
								<td width="50">0.45</td>
								<td width="50">0.61</td>
								<td width="50">21%</td>
								<td width="50">47%</td>
								<td width="50">32%</td>
								<td width="40">42</td>
								<!-- 	 -->
							</tr>
							<tr>
								<td width="70"><b class="no1">15</b></td>
								<td class="team_logo" width="90"><img
									src="../content/images/yijia/201313092945.jpg"> <a
									href="http://saishi.zgzcw.com/soccer/team/183" title="卡利亚里"
									target="_blank">卡利亚里</a></td>
								<td width="40">38</td>
								<td width="40">10</td>
								<td width="40">12</td>
								<td width="40">16</td>
								<td width="40">23</td>
								<td width="40">36</td>
								<td width="40">-13</td>
								<td width="50">0.61</td>
								<td width="50">0.95</td>
								<td width="50">26%</td>
								<td width="50">32%</td>
								<td width="50">42%</td>
								<td width="40">42</td>
								<!-- 	 -->
							</tr>
							<tr class="shuangtr">
								<td width="70"><b class="no1">16</b></td>
								<td class="team_logo" width="90"><img
									src="../content/images/yijia/2013120112658.jpg"> <a
									href="http://saishi.zgzcw.com/soccer/team/195" title="切沃"
									target="_blank">切沃</a></td>
								<td width="40">38</td>
								<td width="40">7</td>
								<td width="40">18</td>
								<td width="40">13</td>
								<td width="40">15</td>
								<td width="40">26</td>
								<td width="40">-11</td>
								<td width="50">0.39</td>
								<td width="50">0.68</td>
								<td width="50">18%</td>
								<td width="50">47%</td>
								<td width="50">34%</td>
								<td width="40">39</td>
								<!-- 	 -->
							</tr>
							<tr>
								<td width="70"><b class="no1">17</b></td>
								<td class="team_logo" width="90"><img
									src="../content/images/yijia/2013120112541.jpg"> <a
									href="http://saishi.zgzcw.com/soccer/team/168" title="巴勒莫"
									target="_blank">巴勒莫</a></td>
								<td width="40">38</td>
								<td width="40">8</td>
								<td width="40">15</td>
								<td width="40">15</td>
								<td width="40">15</td>
								<td width="40">30</td>
								<td width="40">-15</td>
								<td width="50">0.39</td>
								<td width="50">0.79</td>
								<td width="50">21%</td>
								<td width="50">39%</td>
								<td width="50">39%</td>
								<td width="40">39</td>
								<!-- 	 -->
							</tr>
							<tr class="shuangtr">
								<td width="70"><b class="no1">18</b></td>
								<td class="team_logo" width="90"><img
									src="../content/images/yijia/2013120112712.jpg"> <a
									href="http://saishi.zgzcw.com/soccer/team/552" title="热那亚"
									target="_blank">热那亚</a></td>
								<td width="40">38</td>
								<td width="40">6</td>
								<td width="40">18</td>
								<td width="40">14</td>
								<td width="40">14</td>
								<td width="40">27</td>
								<td width="40">-13</td>
								<td width="50">0.37</td>
								<td width="50">0.71</td>
								<td width="50">16%</td>
								<td width="50">47%</td>
								<td width="50">37%</td>
								<td width="40">36</td>
								<!-- 	 -->
							</tr>
							<tr>
								<td width="70"><b class="no1">19</b></td>
								<td class="team_logo" width="90"><img
									src="../content/images/yijia/2013121192718.jpg"> <a
									href="http://saishi.zgzcw.com/soccer/team/523" title="恩波利"
									target="_blank">恩波利</a></td>
								<td width="40">38</td>
								<td width="40">5</td>
								<td width="40">20</td>
								<td width="40">13</td>
								<td width="40">14</td>
								<td width="40">27</td>
								<td width="40">-13</td>
								<td width="50">0.37</td>
								<td width="50">0.71</td>
								<td width="50">13%</td>
								<td width="50">53%</td>
								<td width="50">34%</td>
								<td width="40">35</td>
								<!-- 	 -->
							</tr>
							<tr class="shuangtr">
								<td width="70"><b class="no1">20</b></td>
								<td class="team_logo" width="90"><img
									src="../content/images/yijia/201313085709.gif"> <a
									href="http://saishi.zgzcw.com/soccer/team/560" title="佩斯卡拉"
									target="_blank">佩斯卡拉</a></td>
								<td width="40">38</td>
								<td width="40">5</td>
								<td width="40">16</td>
								<td width="40">17</td>
								<td width="40">15</td>
								<td width="40">33</td>
								<td width="40">-18</td>
								<td width="50">0.39</td>
								<td width="50">0.87</td>
								<td width="50">13%</td>
								<td width="50">42%</td>
								<td width="50">45%</td>
								<td width="40">31</td>
								<!-- 	 -->
							</tr>
						</tbody>
					</table>
				</ul>
				<ul class="tabs1_main_ul" id="tabs1_main_6" style="display: none;">
					<table class="zstab" width="100%" cellspacing="0" cellpadding="0"
						border="0">
						<thead>
							<tr>
								<th width="50">排名</th>
								<th width="140">球队名称</th>
								<th width="40">赛</th>
								<th width="40">胜</th>
								<th width="40">平</th>
								<th width="40">负</th>
								<th width="40">得</th>
								<th width="40">失</th>
								<th width="40">净</th>
								<th width="50">均得</th>
								<th width="50">均失</th>
								<th width="50">胜率</th>
								<th width="50">平率</th>
								<th width="50">负率</th>
								<th width="40">积分</th>
							</tr>
						</thead>
						<tbody>
							<tr>
								<td width="70"><b class="no1">1</b></td>
								<td class="team_logo" width="90"><img
									src="../content/images/yijia/20140806065357.jpg"> <a
									href="http://saishi.zgzcw.com/soccer/team/174" title="罗马"
									target="_blank">罗马</a></td>
								<td width="40">38</td>
								<td width="40">25</td>
								<td width="40">6</td>
								<td width="40">7</td>
								<td width="40">55</td>
								<td width="40">23</td>
								<td width="40">32</td>
								<td width="50">1.45</td>
								<td width="50">0.61</td>
								<td width="50">66%</td>
								<td width="50">16%</td>
								<td width="50">18%</td>
								<td width="40">81</td>
								<!-- 	 -->
							</tr>
							<tr class="shuangtr">
								<td width="70"><b class="no1">2</b></td>
								<td class="team_logo" width="90"><img
									src="../content/images/yijia/20140324112025.jpg"> <a
									href="http://saishi.zgzcw.com/soccer/team/166" title="尤文图斯"
									target="_blank">尤文图斯</a></td>
								<td width="40">38</td>
								<td width="40">23</td>
								<td width="40">11</td>
								<td width="40">4</td>
								<td width="40">40</td>
								<td width="40">16</td>
								<td width="40">24</td>
								<td width="50">1.05</td>
								<td width="50">0.42</td>
								<td width="50">61%</td>
								<td width="50">29%</td>
								<td width="50">11%</td>
								<td width="40">80</td>
								<!-- 	 -->
							</tr>
							<tr>
								<td width="70"><b class="no1">3</b></td>
								<td class="team_logo" width="90"><img
									src="../content/images/yijia/2013120112732.jpg"> <a
									href="http://saishi.zgzcw.com/soccer/team/1419" title="那不勒斯"
									target="_blank">那不勒斯</a></td>
								<td width="40">38</td>
								<td width="40">19</td>
								<td width="40">11</td>
								<td width="40">8</td>
								<td width="40">57</td>
								<td width="40">29</td>
								<td width="40">28</td>
								<td width="50">1.5</td>
								<td width="50">0.76</td>
								<td width="50">50%</td>
								<td width="50">29%</td>
								<td width="50">21%</td>
								<td width="40">68</td>
								<!-- 	 -->
							</tr>
							<tr class="shuangtr">
								<td width="70"><b class="no1">4</b></td>
								<td class="team_logo" width="90"><img
									src="../content/images/yijia/2013120103644.jpg"> <a
									href="http://saishi.zgzcw.com/soccer/team/150" title="AC米兰"
									target="_blank">AC米兰</a></td>
								<td width="40">38</td>
								<td width="40">18</td>
								<td width="40">12</td>
								<td width="40">8</td>
								<td width="40">37</td>
								<td width="40">21</td>
								<td width="40">16</td>
								<td width="50">0.97</td>
								<td width="50">0.55</td>
								<td width="50">47%</td>
								<td width="50">32%</td>
								<td width="50">21%</td>
								<td width="40">66</td>
								<!-- 	 -->
							</tr>
							<tr>
								<td width="70"><b class="no1">5</b></td>
								<td class="team_logo" width="90"><img
									src="../content/images/yijia/2013120103956.jpg"> <a
									href="http://saishi.zgzcw.com/soccer/team/152" title="国际米兰"
									target="_blank">国际米兰</a></td>
								<td width="40">38</td>
								<td width="40">17</td>
								<td width="40">13</td>
								<td width="40">8</td>
								<td width="40">42</td>
								<td width="40">28</td>
								<td width="40">14</td>
								<td width="50">1.11</td>
								<td width="50">0.74</td>
								<td width="50">45%</td>
								<td width="50">34%</td>
								<td width="50">21%</td>
								<td width="40">64</td>
								<!-- 	 -->
							</tr>
							<tr class="shuangtr">
								<td width="70"><b class="no1">6</b></td>
								<td class="team_logo" width="90"><img
									src="../content/images/yijia/2013120104409.jpg"> <a
									href="http://saishi.zgzcw.com/soccer/team/154" title="亚特兰大"
									target="_blank">亚特兰大</a></td>
								<td width="40">38</td>
								<td width="40">16</td>
								<td width="40">12</td>
								<td width="40">10</td>
								<td width="40">30</td>
								<td width="40">21</td>
								<td width="40">9</td>
								<td width="50">0.79</td>
								<td width="50">0.55</td>
								<td width="50">42%</td>
								<td width="50">32%</td>
								<td width="50">26%</td>
								<td width="40">60</td>
								<!-- 	 -->
							</tr>
							<tr>
								<td width="70"><b class="no1">7</b></td>
								<td class="team_logo" width="90"><img
									src="../content/images/yijia/2013120112611.jpg"> <a
									href="http://saishi.zgzcw.com/soccer/team/176" title="佛罗伦萨"
									target="_blank">佛罗伦萨</a></td>
								<td width="40">38</td>
								<td width="40">15</td>
								<td width="40">14</td>
								<td width="40">9</td>
								<td width="40">41</td>
								<td width="40">32</td>
								<td width="40">9</td>
								<td width="50">1.08</td>
								<td width="50">0.84</td>
								<td width="50">39%</td>
								<td width="50">37%</td>
								<td width="50">24%</td>
								<td width="40">59</td>
								<!-- 	 -->
							</tr>
							<tr class="shuangtr">
								<td width="70"><b class="no1">8</b></td>
								<td class="team_logo" width="90"><img
									src="../content/images/yijia/2013120112625.jpg"> <a
									href="http://saishi.zgzcw.com/soccer/team/182" title="拉齐奥"
									target="_blank">拉齐奥</a></td>
								<td width="40">38</td>
								<td width="40">16</td>
								<td width="40">9</td>
								<td width="40">13</td>
								<td width="40">40</td>
								<td width="40">34</td>
								<td width="40">6</td>
								<td width="50">1.05</td>
								<td width="50">0.89</td>
								<td width="50">42%</td>
								<td width="50">24%</td>
								<td width="50">34%</td>
								<td width="40">57</td>
								<!-- 	 -->
							</tr>
							<tr>
								<td width="70"><b class="no1">9</b></td>
								<td class="team_logo" width="90"><img
									src="../content/images/yijia/2013120112618.jpg"> <a
									href="http://saishi.zgzcw.com/soccer/team/179" title="桑普"
									target="_blank">桑普</a></td>
								<td width="40">38</td>
								<td width="40">15</td>
								<td width="40">12</td>
								<td width="40">11</td>
								<td width="40">31</td>
								<td width="40">26</td>
								<td width="40">5</td>
								<td width="50">0.82</td>
								<td width="50">0.68</td>
								<td width="50">39%</td>
								<td width="50">32%</td>
								<td width="50">29%</td>
								<td width="40">57</td>
								<!-- 	 -->
							</tr>
							<tr class="shuangtr">
								<td width="70"><b class="no1">10</b></td>
								<td class="team_logo" width="90"><img
									src="../content/images/yijia/2013121192851.jpg"> <a
									href="http://saishi.zgzcw.com/soccer/team/2960" title="萨索洛"
									target="_blank">萨索洛</a></td>
								<td width="40">38</td>
								<td width="40">13</td>
								<td width="40">12</td>
								<td width="40">13</td>
								<td width="40">31</td>
								<td width="40">32</td>
								<td width="40">-1</td>
								<td width="50">0.82</td>
								<td width="50">0.84</td>
								<td width="50">34%</td>
								<td width="50">32%</td>
								<td width="50">34%</td>
								<td width="40">51</td>
								<!-- 	 -->
							</tr>
							<tr>
								<td width="70"><b class="no1">11</b></td>
								<td class="team_logo" width="90"><img
									src="../content/images/yijia/201313092708.jpg"> <a
									href="http://saishi.zgzcw.com/soccer/team/187" title="乌迪内斯"
									target="_blank">乌迪内斯</a></td>
								<td width="40">38</td>
								<td width="40">14</td>
								<td width="40">9</td>
								<td width="40">15</td>
								<td width="40">28</td>
								<td width="40">36</td>
								<td width="40">-8</td>
								<td width="50">0.74</td>
								<td width="50">0.95</td>
								<td width="50">37%</td>
								<td width="50">24%</td>
								<td width="50">39%</td>
								<td width="40">51</td>
								<!-- 	 -->
							</tr>
							<tr class="shuangtr">
								<td width="70"><b class="no1">12</b></td>
								<td class="team_logo" width="90"><img
									src="../content/images/yijia/201313093257.jpg"> <a
									href="http://saishi.zgzcw.com/soccer/team/558" title="都灵"
									target="_blank">都灵</a></td>
								<td width="40">38</td>
								<td width="40">12</td>
								<td width="40">13</td>
								<td width="40">13</td>
								<td width="40">43</td>
								<td width="40">43</td>
								<td width="40">0</td>
								<td width="50">1.13</td>
								<td width="50">1.13</td>
								<td width="50">32%</td>
								<td width="50">34%</td>
								<td width="50">34%</td>
								<td width="40">49</td>
								<!-- 	 -->
							</tr>
							<tr>
								<td width="70"><b class="no1">13</b></td>
								<td class="team_logo" width="90"><img
									src="../content/images/yijia/2013120112658.jpg"> <a
									href="http://saishi.zgzcw.com/soccer/team/195" title="切沃"
									target="_blank">切沃</a></td>
								<td width="40">38</td>
								<td width="40">12</td>
								<td width="40">11</td>
								<td width="40">15</td>
								<td width="40">28</td>
								<td width="40">35</td>
								<td width="40">-7</td>
								<td width="50">0.74</td>
								<td width="50">0.92</td>
								<td width="50">32%</td>
								<td width="50">29%</td>
								<td width="50">39%</td>
								<td width="40">47</td>
								<!-- 	 -->
							</tr>
							<tr class="shuangtr">
								<td width="70"><b class="no1">14</b></td>
								<td class="team_logo" width="90"><img
									src="../content/images/yijia/201313092945.jpg"> <a
									href="http://saishi.zgzcw.com/soccer/team/183" title="卡利亚里"
									target="_blank">卡利亚里</a></td>
								<td width="40">38</td>
								<td width="40">10</td>
								<td width="40">12</td>
								<td width="40">16</td>
								<td width="40">32</td>
								<td width="40">40</td>
								<td width="40">-8</td>
								<td width="50">0.84</td>
								<td width="50">1.05</td>
								<td width="50">26%</td>
								<td width="50">32%</td>
								<td width="50">42%</td>
								<td width="40">42</td>
								<!-- 	 -->
							</tr>
							<tr>
								<td width="70"><b class="no1">15</b></td>
								<td class="team_logo" width="90"><img
									src="../content/images/yijia/2013120112638.jpg"> <a
									href="http://saishi.zgzcw.com/soccer/team/185" title="博洛尼亚"
									target="_blank">博洛尼亚</a></td>
								<td width="40">38</td>
								<td width="40">10</td>
								<td width="40">9</td>
								<td width="40">19</td>
								<td width="40">23</td>
								<td width="40">35</td>
								<td width="40">-12</td>
								<td width="50">0.61</td>
								<td width="50">0.92</td>
								<td width="50">26%</td>
								<td width="50">24%</td>
								<td width="50">50%</td>
								<td width="40">39</td>
								<!-- 	 -->
							</tr>
							<tr class="shuangtr">
								<td width="70"><b class="no1">16</b></td>
								<td class="team_logo" width="90"><img
									src="../content/images/yijia/2013121192718.jpg"> <a
									href="http://saishi.zgzcw.com/soccer/team/523" title="恩波利"
									target="_blank">恩波利</a></td>
								<td width="40">38</td>
								<td width="40">8</td>
								<td width="40">13</td>
								<td width="40">17</td>
								<td width="40">15</td>
								<td width="40">34</td>
								<td width="40">-19</td>
								<td width="50">0.39</td>
								<td width="50">0.89</td>
								<td width="50">21%</td>
								<td width="50">34%</td>
								<td width="50">45%</td>
								<td width="40">37</td>
								<!-- 	 -->
							</tr>
							<tr>
								<td width="70"><b class="no1">17</b></td>
								<td class="team_logo" width="90"><img
									src="../content/images/yijia/2013120112712.jpg"> <a
									href="http://saishi.zgzcw.com/soccer/team/552" title="热那亚"
									target="_blank">热那亚</a></td>
								<td width="40">38</td>
								<td width="40">7</td>
								<td width="40">13</td>
								<td width="40">18</td>
								<td width="40">24</td>
								<td width="40">37</td>
								<td width="40">-13</td>
								<td width="50">0.63</td>
								<td width="50">0.97</td>
								<td width="50">18%</td>
								<td width="50">34%</td>
								<td width="50">47%</td>
								<td width="40">34</td>
								<!-- 	 -->
							</tr>
							<tr class="shuangtr">
								<td width="70"><b class="no1">18</b></td>
								<td class="team_logo" width="90"><img
									src="../content/images/yijia/20130915170354.jpg"> <a
									href="http://saishi.zgzcw.com/soccer/team/554" title="克罗托内"
									target="_blank">克罗托内</a></td>
								<td width="40">38</td>
								<td width="40">9</td>
								<td width="40">6</td>
								<td width="40">23</td>
								<td width="40">17</td>
								<td width="40">37</td>
								<td width="40">-20</td>
								<td width="50">0.45</td>
								<td width="50">0.97</td>
								<td width="50">24%</td>
								<td width="50">16%</td>
								<td width="50">61%</td>
								<td width="40">33</td>
								<!-- 	 -->
							</tr>
							<tr>
								<td width="70"><b class="no1">19</b></td>
								<td class="team_logo" width="90"><img
									src="../content/images/yijia/201313085709.gif"> <a
									href="http://saishi.zgzcw.com/soccer/team/560" title="佩斯卡拉"
									target="_blank">佩斯卡拉</a></td>
								<td width="40">38</td>
								<td width="40">7</td>
								<td width="40">8</td>
								<td width="40">23</td>
								<td width="40">22</td>
								<td width="40">48</td>
								<td width="40">-26</td>
								<td width="50">0.58</td>
								<td width="50">1.26</td>
								<td width="50">18%</td>
								<td width="50">21%</td>
								<td width="50">61%</td>
								<td width="40">29</td>
								<!-- 	 -->
							</tr>
							<tr class="shuangtr">
								<td width="70"><b class="no1">20</b></td>
								<td class="team_logo" width="90"><img
									src="../content/images/yijia/2013120112541.jpg"> <a
									href="http://saishi.zgzcw.com/soccer/team/168" title="巴勒莫"
									target="_blank">巴勒莫</a></td>
								<td width="40">38</td>
								<td width="40">6</td>
								<td width="40">10</td>
								<td width="40">22</td>
								<td width="40">18</td>
								<td width="40">47</td>
								<td width="40">-29</td>
								<td width="50">0.47</td>
								<td width="50">1.24</td>
								<td width="50">16%</td>
								<td width="50">26%</td>
								<td width="50">58%</td>
								<td width="40">28</td>
								<!-- 	 -->
							</tr>
						</tbody>
					</table>
				</ul>
			</div>
		</div>
		<div style="color: #666; padding-top: 4px;">
			<font size="2px;">&nbsp;&nbsp;规则说明：两支(或更多)球队积分相同时，将应用如下同分解决规则：
				1. 净胜球 2. 进球数</font>
		</div>
		<!-- 积分榜--------------end -->
	</div>
</div>