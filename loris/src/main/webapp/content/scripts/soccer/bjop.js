/**
 * Created with IntelliJ IDEA.
 * User: siguang
 * Date: 14-2-27
 * Time: 下午4:23
 * 百家欧赔
 */

jQuery('td[id^=avg_]').mouseenter(function() {
    setDiv(jQuery(this));
});

jQuery('td[id^=avg_]').mouseleave(function() {
    jQuery('#div_display').css('display', 'none');
});

function setDiv(obj_this) {
    
    var obj = jQuery("#div_display");
    var obj_local = jQuery("#avg_lost");
    

    var offset = obj_local.offset();
    var left = offset.left + obj_local.width();
    var top = offset.top + obj_local.height();
    
    obj.css("top", top);
    obj.css("left", left);
    obj.css("display", "block");
    
}


(function() {
    var d;      //数据集合
    var i;      //数据索引

    //插入弹层
    var c = $(
        '<div class="data-layer data-layer-op" style="display:none;">'
            + '	<div class="datalayer-con">'
            + '		<h2>'
            + '			<img style="position:absolute; left:10px; top:10px;" src="../content/images/oploading.gif" /><span></span>'
            + '		</h2>'
            + '		<ul>'
            + '		</ul>'
            + '	</div>'
            + '</div>'
    ).appendTo(document.body)[0];

    // 将鼠标移到一条数据时将这条数据的信息插入到弹层中
    var data = function() {
        if(d[i]) {
            var e = $(c).find('ul')[0];
            var h = '';
            for(var v = d[i].value, j = 0; v[j]; j++) {
                h += '<li>'
                    +  '	<span class="time-1">'	+ $a(v[j]) + '</span>'
                    +  '	<span class="time-2">'	+ $b(v[j]) + '</span>'
                    +  '	<i>'					+ $c(v[j]) + '</i>'
                    +  '	<i>'					+ $d(v[j]) + '</i>'
                    +  '	<i>'					+ $e(v[j]) + '</i>'
                    +  '	<i>'					+ $f(v[j]) + '</i>'
                    +  '	<i class="spe">'		+ $g(v[j]) + '</i>'
                    +  '	<i>'					+ $h(v[j]) + '</i>'
                    +  '</li>';
            }
            if(j == 10) {
                h += '<li>'
                  +  '<span style="width:auto; color:#090; font-weight:bold;">点击指数查看更多变化</span>'
                  +  '</li>';
            }
            e.innerHTML = h + e.innerHTML;
        }
    };

    //  data
    var $a = function(v) {
        return v['wdate'].substring(5,16);
    };

    var $b = function(v) {
        //初始化变量
        var t = Math.floor((parseInt(matchData) - v['wdate1']) / 6e4);
        var h = t > 0 ? Math.floor(t / 60) : 0;
        var m = t > 0 ? t % 60 : 0;
        return '赛前' + (h ? h + '时' : '') + (m + '分');
    };

    var $c = function(v) {
        return '<span style="color:' + (v['winch'] == 1 ? '#f00' : v['winch'] == 2 ? '#0e66c1' : '') + ';">' + v['win'] + (v['winch'] == 1 ? '↑' : v['winch'] == 2 ? '↓' : '→') + '</span>';
    };

    var $d = function(v) {
        return '<span style="color:' + (v['samech'] == 1 ? '#f00' : v['samech'] == 2 ? '#0e66c1' : '') + ';">' + v['same'] + (v['samech'] == 1 ? '↑' : v['samech'] == 2 ? '↓' : '→') + '</span>';
    };

    var $e = function(v) {
        return '<span style="color:' + (v['lostch'] == 1 ? '#f00' : v['lostch'] == 2 ? '#0e66c1' : '') + ';">' + v['lost'] + (v['lostch'] == 1 ? '↑' : v['lostch'] == 2 ? '↓' : '→') + '</span>';
    };

    var $f = function(v) {
        return v['awin'];
    };

    var $g = function(v) {
        return v['arq'];
    };

    var $h = function(v) {
        return v['alost'];
    };


    var request = function() {
        //确保只请求一次
        request = function() {};
        $.ajax({
            'type'     : 'get',
            'dataType' : 'json',
            'url'      : '/json/'+ mid +'/bjop',
            'async'    : true,
            'success'  : success
        });
    };

    var success = function(j) {
        d = {};
        //重新组合对象
        for(var i = 0, j = j['company5List']; j[i]; i++) {
            var id   = j[i]['attribute']['cid'];
            var name = j[i]['attribute']['name'];
            for(var value = [], _i = 0, _j = j[i]['c']; _j[_i]; _i++) {
                value[_i] = _j[_i]['attribute'];
            }
            d[id] = {
                name  : name,
                value : value
            };
        }

        //隐藏加载图片
        $(c).find('img').css('display', 'none');

        //执行数据
        data();
    };

    var innerHTML = function(t) {
        var p = t.parentNode;
        //插入标题
        $(c).find('span').html($(p).find('td:eq(1)').html() + ' 指数变化');

        //插入初指
        $(c).find('ul').html(
            '<li class="fb">'
                + '	<span class="time-1">' + p.getAttribute('firsttime').substring(5,16) + '</span>'
                + '	<span class="time-2">初</span>'
                + '	<i>' + $(p).find('td:eq(2)').html() + '</i>'
                + '	<i>' + $(p).find('td:eq(3)').html() + '</i>'
                + '	<i>' + $(p).find('td:eq(4)').html() + '</i>'
                + '</li>'
        );

        //判断执行
        if(d) {
            data();
        }
        else {
            request();
        }
    };

    var cssText = function(e) {
        c.style.display = 'block';
        var $1 = $(c).width();
        var $2 = $(c).height();
        var $3 = $(document).width();
        var $4 = $(document).height();
        var $5 = $(window).scrollLeft() + e.clientX + 20;
        var $6 = $(window).scrollLeft() + e.clientX - $1 - 20;
        var $7 = $(window).scrollTop() + e.clientY + 10;
        var $8 = $(window).scrollTop() + e.clientY - $2 - 10;

        c.style.left = ($1 + $5 < $3 ? $5 : $6) + 'px';
        c.style.top  = ($2 + $7 < $4 ? $7 : $8) + 'px';
    };

    $('.bf-tab-02').mouseover(function(e) {
        var t = e.target;

        //判断查找
        if(t.tagName == 'A') {
            t = t.parentNode;
        }

        //判断显示
        if(t.getAttribute('cid')) {
            //保存数据索引
            i = t.getAttribute('cid');

            //显示数据层
            innerHTML(t);
            cssText(e);
            e.stopPropagation();
        }
    });

    $(document).mouseover(function(e) {
        c.style.display = 'none';
    });
})();


initUpdateTime = function() {
	var mtime = parseInt(matchData);
    // 指数弹层中的赛前时间
    $('#data-body tr').each(function(i, e) {
        var t = Math.floor((mtime - parseInt(e.getAttribute('lasttime'))) / 6e4);
        var h = t > 0 ? Math.floor(t / 60) : 0;
        var m = t > 0 ? t % 60 : 0;

        $(e).find('em:eq(0)')
            .attr('class', t > 120 ? 'gengxin-4' : t > 30 ? 'gengxin-3' : t > 5 ? 'gengxin-2' : 'gengxin-1')
            .attr('title', '更新时间：赛前' + (h ? h + '时' : '') + (m + '分'))
        ;
    });
};


initHeader = function() {
    //变量
    var com_count = $('#com-count')[0].innerHTML;

    //头部
    var tr = $('#data-body tbody tr').each(function(i, tr) {
        var td = $(tr).find('td');
        tr.$a = i;
        tr.$b = Number(td[2] .getAttribute('data'));
        tr.$c = Number(td[3] .getAttribute('data'));
        tr.$d = Number(td[4] .getAttribute('data'));
        tr.$e = Number(td[5] .getAttribute('data'));
        tr.$f = Number(td[6] .getAttribute('data'));
        tr.$g = Number(td[7] .getAttribute('data'));
        tr.$h = Number(td[9] .getAttribute('data'));
        tr.$i = Number(td[10].getAttribute('data'));
        tr.$j = Number(td[11].getAttribute('data'));
        tr.$k = Number(td[12].getAttribute('data'));
        tr.$l = Number(td[13].getAttribute('data'));
        tr.$m = Number(td[14].getAttribute('data'));
        tr.$n = Number(td[15].getAttribute('data'));

        //公司类型
        tr.$com_type = td[1].getAttribute('data');
    });

    var sort = function(n/* String name */, r/* Boolean reverse */) {
        if(r) {
            tr.sort(function(a, b) {return a[n] - b[n]});
        } else {
            tr.sort(function(a, b) {return b[n] - a[n]});
        }

        for(var e = $('#data-body tbody')[0], i = 0; tr[i]; i++) {
            e.appendChild(tr[i]);
        }

    };

    // 筛选数据
    var sift = function() {
        var select = document.getElementById('com-type');
        var input  = document.getElementsByName('data-sift');

        $b1 = Number(input[0].value  || -Infinity); $b2 = Number(input[1].value  || Infinity);
        $c1 = Number(input[2].value  || -Infinity); $c2 = Number(input[3].value  || Infinity);
        $d1 = Number(input[4].value  || -Infinity); $d2 = Number(input[5].value  || Infinity);
        $e1 = Number(input[6].value  || -Infinity); $e2 = Number(input[7].value  || Infinity);
        $f1 = Number(input[8].value  || -Infinity); $f2 = Number(input[9].value  || Infinity);
        $g1 = Number(input[10].value || -Infinity); $g2 = Number(input[11].value || Infinity);
        $h1 = Number(input[12].value || -Infinity); $h2 = Number(input[13].value || Infinity);
        $i1 = Number(input[14].value || -Infinity); $i2 = Number(input[15].value || Infinity);
        $j1 = Number(input[16].value || -Infinity); $j2 = Number(input[17].value || Infinity);
        $k1 = Number(input[18].value || -Infinity); $k2 = Number(input[19].value || Infinity);
        $l1 = Number(input[20].value || -Infinity); $l2 = Number(input[21].value || Infinity);
        $m1 = Number(input[22].value || -Infinity); $m2 = Number(input[23].value || Infinity);

        for(var e, c = 0, i = 0; e = tr[i]; i++) {
            //公司筛选
            if(select.value != 'x' && e.$com_type.indexOf(select.value) < 0) {e.style.display = 'none'; c++; continue}

            //高级筛选
            if((!isNaN($b1) && $b1 > e.$b) || (!isNaN($b2) && $b2 < e.$b)) {e.style.display = 'none'; c++; continue}
            if((!isNaN($c1) && $c1 > e.$c) || (!isNaN($c2) && $c2 < e.$c)) {e.style.display = 'none'; c++; continue}
            if((!isNaN($d1) && $d1 > e.$d) || (!isNaN($d2) && $d2 < e.$d)) {e.style.display = 'none'; c++; continue}
            if((!isNaN($e1) && $e1 > e.$e) || (!isNaN($e2) && $e2 < e.$e)) {e.style.display = 'none'; c++; continue}
            if((!isNaN($f1) && $f1 > e.$f) || (!isNaN($f2) && $f2 < e.$f)) {e.style.display = 'none'; c++; continue}
            if((!isNaN($g1) && $g1 > e.$g) || (!isNaN($g2) && $g2 < e.$g)) {e.style.display = 'none'; c++; continue}
            if((!isNaN($h1) && $h1 > e.$h) || (!isNaN($h2) && $h2 < e.$h)) {e.style.display = 'none'; c++; continue}
            if((!isNaN($i1) && $i1 > e.$i) || (!isNaN($i2) && $i2 < e.$i)) {e.style.display = 'none'; c++; continue}
            if((!isNaN($j1) && $j1 > e.$j) || (!isNaN($j2) && $j2 < e.$j)) {e.style.display = 'none'; c++; continue}
            if((!isNaN($k1) && $k1 > e.$k) || (!isNaN($k2) && $k2 < e.$k)) {e.style.display = 'none'; c++; continue}
            if((!isNaN($l1) && $l1 > e.$l) || (!isNaN($l2) && $l2 < e.$l)) {e.style.display = 'none'; c++; continue}
            if((!isNaN($m1) && $m1 > e.$m) || (!isNaN($m2) && $m2 < e.$m)) {e.style.display = 'none'; c++; continue}
            e.style.display = '';
        }
        $('#com-count')[0].innerHTML = com_count - c;
    };


    // 处理表头
    $('#data-header').click(function(e) {
        var t = e.target;
        var b = document.body;

        if(t.className.indexOf('pm') > -1) {
            $('tr.tabtit a.pm').each(function(i, e) {
                if(e !== t) {e.className = 'pm'}
            });

            if(t.className == 'pm') {
                t.className = 'pm up';
            } else if(t.className == 'pm up') {
                t.className = 'pm down';
            } else if(t.className == 'pm down') {
                t.className = 'pm up';
            }
            sort(t.getAttribute('data'), t.className.indexOf('up') > -1);
        }

        if(t.value == '筛 选') {
            sift();
            position();
            scroll();
            OP.init();          // 生成
        }

        if(t.innerHTML == '筛选') {
            $('tr.shaixuan').css('display', function(i, v) {
                if(v != 'none') {
                    $('span.sx-tag-h').attr('class', 'sx-tag');
                    return 'none';
                } else {
                    $('span.sx-tag').attr('class', 'sx-tag-h');
                    return '';
                }
            });
        }

        if(t.innerHTML == '关闭') {
            $('span.sx-tag-h').attr('class', 'sx-tag');
            $('tr.shaixuan').css('display', 'none');
        }

        if(t.innerHTML == "初始") {
            $('button.showBtn').css('display', 'none');
            b.className = b.className.replace(/#1/g, '');
            position();
            scroll();
        }

        if(t.innerHTML == "隐藏") {
            $('button.showBtn').css('display', '');
            b.className += ' #1';
            position();
            scroll();
        }
    });

    // 选择公司
    $('#com-type').change(function(e) {
        sift();
        position();
        scroll();
        dobusiness();
        OP.init();          // 生成
    });

    /* 底部内容控制 */
    var dh = $('#data-header')[0];
    var db = $('#data-body')[0];
    var df = $('#data-footer')[0];
    var y1 = 0;
    var y2 = 0;

    // 返回top位置
    var position = function() {
        y1 = $(db).offset().top - $(dh).height() - 60;
        y2 = $(db).offset().top + $(db).height() - $(df).height() - $(window).height();
    };

    // 头、尾加浮动样式
    var scroll = function() {
        if($('#checkbox-scroll')[0].checked) {
            dh.className = ($(window).scrollTop() > y1) ? 'header-fix' : 'data-header';
            df.className = ($(window).scrollTop() < y2) ? 'footer-fix' : 'data-footer';
        }
        else {
            dh.className = 'data-header';
            df.className = 'data-footer';
        }
    };


    $(df).click(function(e) {
        var t = e.target;
        // 显示选择处理
        if(t.value == "显示选择") {
            var c = 0;
            $(db).find('tr').each(function(i, e) {
                if(!$(e).find('input')[0].checked) {
                    e.style.display = 'none'; c++;
                }
            });
            $('#com-count')[0].innerHTML = com_count - c;
            position();
            scroll();
            dobusiness();

            OP.init();          // 生成
        }

        // 处理恢复数据
        if(t.innerHTML == "恢复") {
            $(db).find('tr').each(function(i, e) {
                e.style.display = '';
            });
            $('#com-count')[0].innerHTML = com_count;

            position();
            scroll();
            dobusiness();
            OP.init();          // 生成
        }

        // 全选处理
        if(t.innerHTML == "全选") {
            $(db).find('input').each(function(i, e) {e.checked = true});
        }

        // 反选处理
        if(t.innerHTML == "反选") {
            $(db).find('input').each(function(i, e) {e.checked = !e.checked});
        }

        // 浮动处理
        if(t.value == '头尾浮动') {
            position();
            scroll();
        }
    });

    position();
    scroll();
    $(window).scroll(scroll).resize(function() {position(); scroll()});
};

// 计算均值
var dobusiness = function(){
    //建立数组
    var arr = new Array();
    var ar = new Array();

    //初始化数组
    for(var i=0;i<4;i++){
        arr[i]= new Array();
        for(var j=0;j<=15;j++)
            arr[i][j]=0;
    }

    //公司总数
    var count = $("#com-count").text();

    for(var i=2;i<=7;i++){
        ar[i]= new Array();
        for(var j=0;j<count;j++)
            ar[i][j]=0;
    }

    //计数
    var sum =0;

    //遍历数据存入数组
    $("#data-body table tr").each(function(i,e){
        //显示的tr
        if($(e).is(":visible")){

            $(e).find("td").each(function(j,ee){
                //需要统计的td
                if(j!=8&&j<=15&&j>=2){
                    //得到值
                    var v = parseFloat($(ee).text());
                    //均值累计
                    arr[0][j] += v;
                    //最大
                    if(v>arr[1][j])
                        arr[1][j]=v;
                    //最小
                    if(arr[2][j]==0 || v<arr[2][j]){
                        arr[2][j]=v;
                    }
                    //储存胜水位
                    if(j>=2 && j<=7)
                        ar[j][sum] = v;
                }
            });
            sum++;
        }
    });

    //页面显示
    $("#data-footer table tr").each(function(i,e){
        if(i<=2){
            $(e).find("td").each(function(j,ee){
                if(j!=8 && j<=15 && j>=2){
                    if(i==0)
                        arr[i][j]/=sum==0?1:sum;
                    $(ee).text(arr[i][j].toFixed(2));
                }
            });
        }
    });

	//计算方差		
	var init_fc_win = fcsj(sum,ar[2],arr[0][2]);//初始胜方差
	var init_fc_draw = fcsj(sum,ar[3],arr[0][3]);//初始平方差
	var init_fc_lost = fcsj(sum,ar[4],arr[0][4]);//初始负方差
	
	var t1 = fcsj(sum,ar[5],arr[0][5]);
	var t2 = fcsj(sum,ar[6],arr[0][6]);
	var t3 = fcsj(sum,ar[7],arr[0][7]);

    //页面显示
    $(".otherodds").text("离散度% "+ Math.sqrt(t1).toFixed(2) +" "+ Math.sqrt(t2).toFixed(2)+ " "+ Math.sqrt(t3).toFixed(2) +"  | 中足网方差% "+t1+" "+ t2 +" "+ t3);

    var fcObj = [
        {init_win:init_fc_win, init_draw:init_fc_draw, init_lost:init_fc_lost},
        {new_win:t1, new_draw:t2, new_lost:t3}
    ]
    return fcObj;       // 返回初始和最新方差对象
};

// 方差计算
function fcsj(sum,ar,arr){
    var tota=0;
    for(var i=0;i<sum;i++){
        tota += Math.pow((ar[i]-arr),2);
    }
    tota /= sum == 0 ? 1 : sum;
    return (tota*100).toFixed(2);
};


//----------------------------------------------------------------------------------
// 创建欧赔对象
var OP = OP || {};
OP = {

    // 返回一组数据中有重复的数和重复的次数
    dataLogic : function(oArr){
        var oDataJson={};
        var oFinallyArr = [];

        for(var i=0; i<oArr.length; i++){
            var ret = oArr[i];
            if(!oDataJson[ret]){
                oDataJson[ret]=1;
            }
            else {
                oDataJson[ret]++;
            }
        };

        for(var key in oDataJson){
            oFinallyArr.push([parseFloat(key), oDataJson[key], key+","+oDataJson[key]]);        // 转成一个二维数姐
        };
        return oFinallyArr;
    },

    // 获取显示的td
    getVisibleData : function(){
        this.clearChars();
        var oIS = [];      // 初始赔率-胜
        var oIP = [];      // 初始赔率-平
        var oIF = [];      // 初始赔率-负

        var oNS = [];      // 最新赔率-胜
        var oNP = [];      // 最新赔率-平
        var oNF = [];      // 最新赔率-负

        $("#data-body tr:visible").each(function(){
            oIS.push($(this).find("td").eq(2).attr("data"));
            oIP.push($(this).find("td").eq(3).attr("data"));
            oIF.push($(this).find("td").eq(4).attr("data"));

            oNS.push($(this).find("td").eq(5).attr("data"))
            oNP.push($(this).find("td").eq(6).attr("data"))
            oNF.push($(this).find("td").eq(7).attr("data"))
        })

        /* 数据模型
        var oDefault = {
            grid : {        // 网格
                drawGridlines : false,
                shadow        : false
            },
            seriesDefaults : {      //线
                renderer : $.jqplot.BarRenderer,
                rendererOptions : {
                    varyBarColor : true,
                    barWidth     : 3,
                    shadowOffset : 0.5
                }
            },
            seriesColors : ["#cccccc"],       //线颜色
            highlighter : {         //荧光笔
                show            : true,
                tooltipLocation : 'n',
                sizeAdjust      : 4,
                yvalues         : 3,
                formatString    : '<span style="display:none;">%s,%s,</span>%s'
            }
        }
        if(oIS.length>0 && oIP.length>0 && oIF.length>0){
            jQuery.jqplot("initOdds1", [this.dataLogic(oIS)],  $.extend(oDefault, {seriesColors : ["#C70000"]}));
            jQuery.jqplot("initOdds2", [this.dataLogic(oIP)],  $.extend(oDefault, {seriesColors : ["#333333"]}));
            jQuery.jqplot("initOdds3", [this.dataLogic(oIF)],  $.extend(oDefault, {seriesColors : ["#0E66C1"]}));
        }
        if(oNS.length>0 && oNP.length>0 && oNF.length>0){

            jQuery.jqplot("newsOdds1", [this.dataLogic(oNS)], $.extend(oDefault, {seriesColors : ["#C70000"]}));
            jQuery.jqplot("newsOdds2", [this.dataLogic(oNP)], $.extend(oDefault, {seriesColors : ["#333333"]}));
            jQuery.jqplot("newsOdds3", [this.dataLogic(oNF)], $.extend(oDefault, {seriesColors : ["#0E66C1"]}));
        } */
    },

    // 清除图表
    clearChars:function(){
        $("#initOdds1").empty();
        $("#initOdds2").empty();
        $("#initOdds3").empty();
        $("#newsOdds1").empty();
        $("#newsOdds2").empty();
        $("#newsOdds3").empty();
    },

    // 获取表值
    getOddsVal: function(){
        var oInitCharsBox = $("#initCharsBox .var-ps-2");
        var oNewCharsBox = $("#newCharsBox .var-ps-2");

        var opath0 = $("#data-footer tr:eq(0)");
        var opath1 = $("#data-footer tr:eq(1)");
        var opath2 = $("#data-footer tr:eq(2)");

        /* initOdds：初赔数据
        * 0：胜  1：平  2：负
        * [平均, 最大， 最小， 方差]
        * */
        var getDobusiness = dobusiness();    // 获取初始、最新方差

        var initOdds = {
            0:[$("td", opath0).eq(2).text(), $("td", opath1).eq(2).text(), $("td", opath2).eq(2).text(), getDobusiness[0].init_win],
            1:[$("td", opath0).eq(3).text(), $("td", opath1).eq(3).text(), $("td", opath2).eq(3).text(), getDobusiness[0].init_draw],
            2:[$("td", opath0).eq(4).text(), $("td", opath1).eq(4).text(), $("td", opath2).eq(4).text(), getDobusiness[0].init_lost]
        }
        var newOdds ={
            0:[$("td", opath0).eq(5).text(), $("td", opath1).eq(5).text(), $("td", opath2).eq(5).text(), getDobusiness[1].new_win],
            1:[$("td", opath0).eq(6).text(), $("td", opath1).eq(6).text(), $("td", opath2).eq(6).text(), getDobusiness[1].new_draw],
            2:[$("td", opath0).eq(7).text(), $("td", opath1).eq(7).text(), $("td", opath2).eq(7).text(), getDobusiness[1].new_lost]
        }

        oInitCharsBox.each(function(i){
            $(this).empty().html('<span class="red">最大:'+ initOdds[i][1] +'</span><span class="green">最小:'+ initOdds[i][2] +'</span><span>平均:'+ initOdds[i][0] +'</span><span>方差%:'+ initOdds[i][3] +'</span>');
        })

        oNewCharsBox.each(function(i){
            $(this).empty().html('<span class="red">最大:'+ newOdds[i][1] +'</span><span class="green">最小:'+ newOdds[i][2] +'</span><span>平均:'+ newOdds[i][0] +'</span><span>方差%:'+ newOdds[i][3] +'</span>');
        })
    },

    init: function(){
        this.getVisibleData();
        this.getOddsVal();
    }
}