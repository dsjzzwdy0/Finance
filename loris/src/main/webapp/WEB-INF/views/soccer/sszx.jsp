<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<link rel="stylesheet" type="text/css" href="../content/css/soccer/nsaishi.css" />
<link rel="stylesheet" type="text/css" href="../content/css/soccer/sszx.css" />

<!-- 主要内容 -->
<%@include file="./center/sszxdetail.jsp"%>


<script type="text/javascript" src="../content/scripts/soccer/xiaomishu.js"></script>
<script type="text/javascript" src="../content/scripts/soccer/search.js"></script>
<script type="text/javascript" src="../content/scripts/soccer/index_juhe.js"></script>
<script type="text/javascript" src="../content/scripts/soccer/tide_common_20110218.js"></script>

<script type="text/javascript">
jQuery(function() {
    var hTop = 0;
    if (jQuery("#fixTb").length) {
        hTop = jQuery("#fixTb").offset().top
    }
    jQuery(window).scroll(function() {
        var sTop = jQuery(this).scrollTop();
        var f = sTop > hTop ? true: false;
        if (f) {
            jQuery(".gotop").show();
        } else {
            jQuery(".gotop").hide();
        }
    })

    jQuery(document).delegate('a.gotop', 'click', function() {
        jQuery(this).hide();
        window.scrollTo(0, 0);
    })
})
</script>

<script>
$(function() {
    tabs("tabs1", "sscur", "a");
    tabs_tide({
        tab: '.player_num li',
        div_pre: 'player_main_js',
        cur: 'on',
        stop: '.focus_pic',
        action: 'mouseover',
        show: 1,
        auto: true,
        interval: 7
    });
    
    //地图滑过、点击效果
    $('.maps').on('mouseenter', '.area', function() {
	    var areaClass = $(this).attr('class');
	    areaClass = areaClass.split('_')[1];
	    if (!$(this).find('.' + areaClass).hasClass('active'))
	    {
	     	$(this).find('.' + areaClass).removeClass('dis_none');
	    }
    }).on('mouseleave', '.area', function() {
	    var areaClass = $(this).attr('class');
	    areaClass = areaClass.split('_')[1];
	    if (!$(this).find('.' + areaClass).hasClass('active'))
	    {
	       	$(this).find('.' + areaClass).addClass('dis_none');
	    }
    }).on('click', '.area', function(){
	    var _this = $(this), areaClass = _this.attr('class'), gameName, linkName;
	    gameName = areaClass.split('_')[1];
	    linkName = gameName + '_link';
	    $('#tabs1').find('.' + linkName).trigger('click');
    });
    
    //赛事选择
    $('#tabs1').on('click', 'a', function() {
	    var gameName = $(this).attr('class').split(' ')[0].split('_')[0];
	    var gameId = $(this).attr('id');
	    $(this).addClass('sscur').siblings('#tabs1 a').removeClass('sscur');
	    if (gameName == 'lqss') 
	    {
	        $('.maps').find('.areaBlock').removeClass('active').addClass('dis_none');
	    }
	    areaFun(gameName, gameId);
	});
	    
    // 赛事选择关联方法
    function areaFun(obj, IDName) {
        var areaName = $('.area_' + obj),
        detailName = $('.' + obj + '_detail');
        detailID = $('#div_' + IDName);
        areaName.find('.areaBlock').addClass('active').end().siblings('.area').find('.areaBlock').removeClass('active').addClass('dis_none');
        detailID.show().siblings('.gamesContent').hide();
    }
    //赛事滑过
    $('.lslogo').mouseover(function() {
        $(this).addClass('lscur');
        if ($(this).find('.kuang a').length <= 1) {
            $(this).find('.kuang').css({
                'width': 'auto'
            });
        } else if ($(this).find('.kuang a').length == 2) {
            $(this).find('.kuang').css({
                'width': '199'
            });
        } else if ($(this).find('.kuang a').length > 2) {
            $(this).find('.kuang').css({
                'width': '298'
            });
        } else {
            return;
        }
        $(this).find('.kuang').show();
    }).mouseleave(function() {
        $(this).removeClass('lscur');
        $(this).find('.kuang').hide();
    });
    $('.lslogo a.first-link').each(function() {
        var firstLink = $(this).siblings('.kuang').find('a:first').attr('href');
        $(this).attr({
            'target': '_blank',
            'href': firstLink
        });
    });
    //今日赛程交互
    var sw = $('.swindow_inner').children();
    var sw_len = sw.length;
    var i;
    $('.swindow_inner').css('width', 100 * sw_len) 
    
    if (sw_len <= 9) {
        $('.scrleft').css('visibility', 'hidden');
        $('.scrright').css('visibility', 'hidden');
    } else {
        $('.scrleft').css('visibility', 'visible');
        $('.scrright').css('visibility', 'visible');
        $('.scrright').addClass('scrrightActive');
        i = 9;
    }
    $('.scrArrow').on('click',
    function() {
        if ($(this).hasClass('scrleft')) {
            if (i > 9) {
                i--;
                $('.scrright').attr('class', 'scrArrow scrright fr scrrightActive');
                $('.swindow_inner').css('left', 100 * (9 - i));
                if (i == 9) {
                    $('.scrleft').removeClass('scrleftActive');
                }
            } else {
                $(this).removeClass('scrleftActive');
                return false;
            }
        } else if ($(this).hasClass('scrright')) {
            if (i < sw_len) {
                i++;
                $('.scrleft').attr('class', 'scrArrow scrleft fl scrleftActive');
                $('.swindow_inner').css('left', 100 * (9 - i));
                if (i == sw_len) {
                    $('.scrright').removeClass('scrrightActive');
                }
            } else {
                $(this).removeClass('scrrightActive');
                return false;
            }
        } else {
            return false;
        }
    })
})
  
</script>
