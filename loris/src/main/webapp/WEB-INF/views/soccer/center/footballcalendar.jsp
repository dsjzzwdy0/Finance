<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<div class="cal_tit">
	<span class="s_1">足球日历</span> <span class="s_2" id="today_btn">今天</span>
	<span class="s_3">
		<a href="javascript:void(0);" id="preD"></a>
		<a href="javascript:void(0);" id="nextD"></a>
	</span>
	<span class="s_4" id="infoD">2018年03月19 星期一</span>
	<span class="s_7" id="changeViewBtn" style="float: right; margin-right: 15px;">
		<em class="cur"	data_type="one">周</em> 
		<em data_type="four" class="">4周</em> 
		<em	data_type="month" class="">月</em>
	</span>
</div>

<div class="cal_main">
	<div class="cal_main_left">
		<div class="calender">
			<h1>
				<span class="leftaward" id="pre"></span>
				<span class="rightaward" id="next"></span>
				<span id="info">2018年03月</span>
			</h1>
			<ul id="calendar">
				<li date="2018-02-25" issue="80204" zcissue="18027" class="off"><span>25</span></li>
				<li date="2018-02-26" issue="80204" zcissue="0" class="off"><span>26</span></li>
				<li date="2018-02-27" issue="80301" zcissue="18028" class="off"><span>27</span></li>
				<li date="2018-02-28" issue="80301" zcissue="0" class="off"><span>28</span></li>
				<li date="2018-03-01" issue="80301" zcissue="0"><span>1</span></li>
				<li date="2018-03-02" issue="80301" zcissue="18029"><span>2</span></li>
				<li date="2018-03-03" issue="80301" zcissue="18030"><span>3</span></li>
				<li date="2018-03-04" issue="80301" zcissue="18031"><span>4</span></li>
				<li date="2018-03-05" issue="80301" zcissue="0"><span>5</span></li>
				<li date="2018-03-06" issue="80302" zcissue="18032"><span>6</span></li>
				<li date="2018-03-07" issue="80302" zcissue="0"><span>7</span></li>
				<li date="2018-03-08" issue="80302" zcissue="0"><span>8</span></li>
				<li date="2018-03-09" issue="80302" zcissue="18033"><span>9</span></li>
				<li date="2018-03-10" issue="80302" zcissue="18034"><span>10</span></li>
				<li date="2018-03-11" issue="80302" zcissue="18035"><span>11</span></li>
				<li date="2018-03-12" issue="80302" zcissue="0"><span>12</span></li>
				<li date="2018-03-13" issue="80303" zcissue="18036"><span>13</span></li>
				<li date="2018-03-14" issue="80303" zcissue="0"><span>14</span></li>
				<li date="2018-03-15" issue="80303" zcissue="0"><span>15</span></li>
				<li date="2018-03-16" issue="80303" zcissue="18037"><span>16</span></li>
				<li date="2018-03-17" issue="80303" zcissue="18038"><span>17</span></li>
				<li date="2018-03-18" issue="80303" zcissue="18039"><span>18</span></li>
				<li date="2018-03-19" issue="80303" zcissue="0" class="cur on"><span>19</span></li>
				<li date="2018-03-20" issue="0" zcissue="0"><span>20</span></li>
				<li date="2018-03-21" issue="0" zcissue="0"><span>21</span></li>
				<li date="2018-03-22" issue="0" zcissue="0"><span>22</span></li>
				<li date="2018-03-23" issue="0" zcissue="0"><span>23</span></li>
				<li date="2018-03-24" issue="0" zcissue="0"><span>24</span></li>
				<li date="2018-03-25" issue="0" zcissue="0"><span>25</span></li>
				<li date="2018-03-26" issue="0" zcissue="0"><span>26</span></li>
				<li date="2018-03-27" issue="0" zcissue="0"><span>27</span></li>
				<li date="2018-03-28" issue="0" zcissue="0"><span>28</span></li>
				<li date="2018-03-29" issue="0" zcissue="0"><span>29</span></li>
				<li date="2018-03-30" issue="0" zcissue="0"><span>30</span></li>
				<li date="2018-03-31" issue="0" zcissue="0"><span>31</span></li>
				<li date="2018-04-01" issue="0" zcissue="0" class="off"><span>1</span></li>
				<li date="2018-04-02" issue="0" zcissue="0" class="off"><span>2</span></li>
				<li date="2018-04-03" issue="0" zcissue="0" class="off"><span>3</span></li>
				<li date="2018-04-04" issue="0" zcissue="0" class="off"><span>4</span></li>
				<li date="2018-04-05" issue="0" zcissue="0" class="off"><span>5</span></li>
				<li date="2018-04-06" issue="0" zcissue="0" class="off"><span>6</span></li>
				<li date="2018-04-07" issue="0" zcissue="0" class="off"><span>7</span></li>
			</ul>
			<div class="clear"></div>
		</div>
	</div>
	<div class="cal_main_right">
		<table class="tab4" width="100%" cellspacing="0" cellpadding="0"
			border="0">
			<tbody id="calendarMap">
				<tr>
					<td id="2018-03-16" issue="80303" zcissue="18037">
						<p class="s_11">
							<i>星期五</i><br>
							<b class="font3">03月16日</b><br>
							<span class="match_num">411场比赛</span>
						</p>
						<p id="p-2018-03-16" class="s_12">
							411场比赛<br>竞彩32场<br>单场54场<br>足彩18037期停售
						</p>
					</td>
					<td id="2018-03-17" issue="80303" zcissue="18038"><p
							class="s_11">
							<i>星期六</i><br>
							<b class="font3">03月17日</b><br>
							<span class="match_num">1373场比赛</span>
						</p>
						<p id="p-2018-03-17" class="s_12">
							1373场比赛<br>竞彩107场<br>单场157场<br>足彩18038期停售
						</p>
					</td>
					<td id="2018-03-18" issue="80303" zcissue="18039"><p
							class="s_11">
							<i>星期日</i><br>
							<b class="font3">03月18日</b><br>
							<span class="match_num">1061场比赛</span>
						</p>
						<p id="p-2018-03-18" class="s_12">
							1061场比赛<br>竞彩66场<br>单场114场<br>足彩18039期停售
						</p>
					</td>
					<td id="2018-03-19" class="on" issue="80303" zcissue="0"><p
							class="s_11">
							<i>星期一</i><br>
							<b class="font3">03月19日</b><br>
							<span class="match_num">185场比赛</span>
						</p>
						<p id="p-2018-03-19" class="s_12">
							185场比赛<br>竞彩6场<br>单场14场
						</p>
					</td>
					<td id="2018-03-20" issue="0" zcissue="0"><p class="s_11">
							<i>星期二</i><br>
							<b class="font3">03月20日</b><br>
							<span class="match_num">142场比赛</span>
						</p>
						<p id="p-2018-03-20" class="s_12">
							142场比赛<br>竞彩0场<br>单场0场
						</p>
					</td>
					<td id="2018-03-21" issue="0" zcissue="0"><p class="s_11">
							<i>星期三</i><br>
							<b class="font3">03月21日</b><br>
							<span class="match_num">228场比赛</span>
						</p>
						<p id="p-2018-03-21" class="s_12">
							228场比赛<br>竞彩0场<br>单场0场
						</p>
					</td>
					<td id="2018-03-22" issue="0" zcissue="0"><p class="s_11">
							<i>星期四</i><br>
							<b class="font3">03月22日</b><br>
							<span class="match_num">120场比赛</span>
						</p>
						<p id="p-2018-03-22" class="s_12">
							120场比赛<br>竞彩0场<br>单场0场
						</p>
					</td>
				</tr>
			</tbody>
		</table>
	</div>
	<div class="clear"></div>
</div>
