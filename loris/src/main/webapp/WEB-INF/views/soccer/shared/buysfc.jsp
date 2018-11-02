<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>

<!--购买-->
<div class="footer" id="touzhulan" style="position: fixed; top: 867px; left: 451.5px;">
	<div class="yixuan">
		<div id="yixuan">
			已选 <span id="changcishuliang">0</span> 场<i class="icon"
				style="display: none;"></i>
		</div>
		<!--购买弹出层-->
		<div id="goumai_ceng" class="goumai_ceng" style="display: none">
			<div class="gc_t">
				<div class="gc_t_1">对阵</div>
				<div class="gc_t_2">选项</div>
				<div class="gc_t_3">胆</div>
				<div id="clearall" class="gc_t_4">
					<a>全删</a>
				</div>
			</div>
			<div id="shedanrows"></div>
			<div id="danfanwei" class="dan" style="display: none;">
				<div class="dan_1">胆命中范围：</div>
				<div class="dan_2" id="danmin">
					<div class="shu">0</div>
					<div class="jia">
						<span class="icon dan_hover"></span>
						<ul class="xia_l li_xs">
						</ul>
					</div>
				</div>
				<div class="float_l ga">~</div>
				<div class="dan_2" id="danmax">
					<div class="shu">0</div>
					<div class="jia">
						<span class="icon dan_hover"></span>
						<ul class="xia_l li_xs">
						</ul>
					</div>
				</div>
			</div>
		</div>
	</div>
	<div class="conut">
		<div class="cont1">
			<div class="zi" id="nobisai">请在上方选择比赛</div>
			<div id="guoguanfangshi" style="display: none">
				<div class="fangshi">过关方式：</div>
				<ul class="cont1_1" id="chuans">
				</ul>
				<div id="guogfs" class="guogfs">
					<div id="guogfsbtn">
						<input name="" type="checkbox" value=""> <a hover="hover">更多方式</a>
						<span class="icon guogfs_h"></span>
					</div>
					<div class="guofs_zk" style="display: none;">
						<div class="zk_1">
							<a class="float_l link_blue"
								href="http://www.okooo.com/help/568f1aa57108c611fd9b0004/"
								target="_blank">过关方式对照表</a> <a class="float_r link_blue"
								href="javascript:void(0);"><u id="hideguogfs">隐藏</u></a>
						</div>
						<div id="more_3_1" class="zk_1" style="display: none;">
							<p>
								<input id="chuan3_3" type="checkbox" value="3_3"> <label
									for="chuan3_3">3串3</label>
							</p>
							<p>
								<input id="chuan3_4" type="checkbox" value="3_4"> <label
									for="chuan3_4">3串4</label>
							</p>
						</div>
						<div id="more_4_1" class="zk_1" style="display: none;">
							<p>
								<input id="chuan4_4" type="checkbox" value="4_4"> <label
									for="chuan4_4">4串4</label>
							</p>
							<p>
								<input id="chuan4_5" type="checkbox" value="4_5"> <label
									for="chuan4_5">4串5</label>
							</p>
							<p>
								<input id="chuan4_6" type="checkbox" value="4_6"> <label
									for="chuan4_6">4串6</label>
							</p>
							<p>
								<input id="chuan4_11" type="checkbox" value="4_11"> <label
									for="chuan4_11">4串11</label>
							</p>
						</div>
						<div id="more_5_1" class="zk_1" style="display: none;">
							<p>
								<input id="chuan5_5" type="checkbox" value="5_5"> <label
									for="chuan5_5">5串5</label>
							</p>
							<p>
								<input id="chuan5_6" type="checkbox" value="5_6"> <label
									for="chuan5_6">5串6</label>
							</p>
							<p>
								<input id="chuan5_10" type="checkbox" value="5_10"> <label
									for="chuan5_10">5串10</label>
							</p>
							<p>
								<input id="chuan5_16" type="checkbox" value="5_16"> <label
									for="chuan5_16">5串16</label>
							</p>
							<p>
								<input id="chuan5_20" type="checkbox" value="5_20"> <label
									for="chuan5_20">5串20</label>
							</p>
							<p>
								<input id="chuan5_26" type="checkbox" value="5_26"> <label
									for="chuan5_26">5串26</label>
							</p>
						</div>
						<div id="more_6_1" class="zk_1" style="display: none;">
							<p>
								<input id="chuan6_6" type="checkbox" value="6_6"> <label
									for="chuan6_6">6串6</label>
							</p>
							<p>
								<input id="chuan6_7" type="checkbox" value="6_7"> <label
									for="chuan6_7">6串7</label>
							</p>
							<p>
								<input id="chuan6_15" type="checkbox" value="6_15"> <label
									for="chuan6_15">6串15</label>
							</p>
							<p>
								<input id="chuan6_20" type="checkbox" value="6_20"> <label
									for="chuan6_20">6串20</label>
							</p>
							<p>
								<input id="chuan6_22" type="checkbox" value="6_22"> <label
									for="chuan6_22">6串22</label>
							</p>
							<p>
								<input id="chuan6_35" type="checkbox" value="6_35"> <label
									for="chuan6_35">6串35</label>
							</p>
							<p>
								<input id="chuan6_42" type="checkbox" value="6_42"> <label
									for="chuan6_42">6串42</label>
							</p>
							<p>
								<input id="chuan6_50" type="checkbox" value="6_50"> <label
									for="chuan6_50">6串50</label>
							</p>
							<p>
								<input id="chuan6_57" type="checkbox" value="6_57"> <label
									for="chuan6_57">6串57</label>
							</p>
						</div>
						<div id="more_7_1" class="zk_1" style="display: none;">
							<p>
								<input id="chuan7_7" type="checkbox" value="7_7"> <label
									for="chuan7_7">7串7</label>
							</p>
							<p>
								<input id="chuan7_8" type="checkbox" value="7_8"> <label
									for="chuan7_8">7串8</label>
							</p>
							<p>
								<input id="chuan7_21" type="checkbox" value="7_21"> <label
									for="chuan7_21">7串21</label>
							</p>
							<p>
								<input id="chuan7_35" type="checkbox" value="7_35"> <label
									for="chuan7_35">7串35</label>
							</p>
							<p>
								<input id="chuan7_120" type="checkbox" value="7_120"> <label
									for="chuan7_120">7串120</label>
							</p>
						</div>
						<div id="more_8_1" class="zk_1" style="display: none;">
							<p>
								<input id="chuan8_8" type="checkbox" value="8_8"> <label
									for="chuan8_8">8串8</label>
							</p>
							<p>
								<input id="chuan8_9" type="checkbox" value="8_9"> <label
									for="chuan8_9">8串9</label>
							</p>
							<p>
								<input id="chuan8_28" type="checkbox" value="8_28"> <label
									for="chuan8_28">8串28</label>
							</p>
							<p>
								<input id="chuan8_56" type="checkbox" value="8_56"> <label
									for="chuan8_56">8串56</label>
							</p>
							<p>
								<input id="chuan8_70" type="checkbox" value="8_70"> <label
									for="chuan8_70">8串70</label>
							</p>
							<p>
								<input id="chuan8_247" type="checkbox" value="8_247"> <label
									for="chuan8_247">8串247</label>
							</p>
						</div>
						<div class="ooo">
							<span class="bot"></span> <span class="top"></span>
						</div>
					</div>
				</div>
			</div>
			<div class="p"></div>
			<div id="beishus" class="beishu">
				<div class="beis_1">倍数：</div>
				<div class="beis_2">
					<div class="jian" hover="bg" sel="bg">
						<span class="icon"></span>
					</div>
					<div class="shu">
						<input id="beishu" value="1" type="text">
					</div>
					<div class="jia" hover="bg" sel="bg">
						<span class="icon"></span>
					</div>
				</div>
			</div>
			<div class="jiangjin">
				<p>投注金额： <span id="zongjin"> <a>0</a> 元 </span> </p>
				<p>最高奖金： <span id="jiangjin"> <a>0</a> 元 </span> </p>
			</div>
		</div>
	</div>
	<div class="wanfa">
		<p id="xuandanguolv" class="guolv" style="display: none">选单过滤</p>
		<p id="jiangjinyouhua" class="youhua" style="display: none">奖金优化</p>
	</div>
	<div id="determine" class="button goumai goumai_la"
		hover="goumai_hover"></div>
	<div id="pingcexiangqing" class="button goumai par_la"
		hover="par_hover" style="display: none"></div>
	<div id="pingce" class="pingce" style="display: none;"></div>
</div>