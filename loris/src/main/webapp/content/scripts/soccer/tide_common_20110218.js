// JavaScript Document
/*******
tab:切换的标签
div_pre:'tab' 切换的div前缀，默认为tab div规则 <div id="tab1"></div><div id="tab2"></div>
cur:'cur' 当前样式
action:'click' 单击切换，mouseover:悬停切换
show:第几个
*******/
function tabs_tide(o){
		o=jQuery.extend({
			tab:null,
			cur:'cur',
			prev:null,
			next:null,
			stop:null,
			auto: false,
			action:'click',
			div_pre:'tab',
			show:null,
			interval:1
		},o||{});

	var $obj=jQuery(o.tab);//所用标签
	var len=$obj.length;//个数
	var divs="";
	var c=1;
	var interval;
	for(n=1;n<=len;n++){n==1?divs+="#"+o.div_pre+n:divs+=",#"+o.div_pre+n;};
	function showDiv($num){
		$obj.removeClass(o.cur);
		jQuery(divs).hide();
		jQuery($obj.get($num-1)).addClass(o.cur);
		jQuery("#"+o.div_pre+$num).show();	
	}

	if(o.action=='click'){
		$obj.each(function(i){		
			jQuery(this).click(function(){
				c=i+1;showDiv(c);	
			});
		});
	}else{
		$obj.each(function(i){		
			jQuery(this).hover(function(){
				c=i+1;showDiv(c);
			},function(){});
		});
	}

	if(o.show){
		c=o.show;showDiv(c);
	}

	if(o.next){
		jQuery(o.next).click(function(){
				c++;c>len?c=1:c;showDiv(c);	
			});
	}

	if(o.prev){
		jQuery(o.prev).click(function(){
				c--;c<=0?c=len:c;showDiv(c);		
			});
	}

	if(o.auto){
		interval=setInterval(function() {
					c++;c>len?c=1:c;showDiv(c);	
				}, o.interval*1000);
	}
	
	if(o.auto){
		if(o.stop){
				jQuery(o.stop).mouseover(function(){
				 clearInterval(interval);
				}).mouseout(function(){
					  interval=setInterval(function() {
						c++;c>len?c=1:c;showDiv(c);	
					}, o.interval*1000);
				});
			}
	}
}


		$(function(){
			var name = $('#ad');  //滚动广告的ID
			function showad(time){
					setTimeout(function(){
						$(name).show();},time);
				}
			function hidead(time){
					setTimeout(function(){
						$(name).hide();},time);
				}
			$('#close').click(function(){
					$(name).hide();
				});
			showad(100); //页面加载完毕多久后显示广告
			//hidead(31000); //页面加载完毕多久后关闭广告
			function scrollad(){
					var offset = $(window).height() - $(name).height() + $(document).scrollTop();
					$(name).animate({top:offset},{duration:800,queue:false});
				}	
			scrollad();
			$(window).scroll(scrollad);
		});
		
/******标签切换*******/
//"id"为需要切换样式的层的id,与切换相对应的内容id命名规则为id_main_i."cur"为当前层的样式名字."s"为需要切换样式的每个容器的标签,如p、span、li等.
function tabs(id,cur,s){
	var content="_main_";
	if ( jQuery("#"+id).length){
	function closeContent(id,length){
		for(var i=1;i<=length;i++){
		jQuery("#"+id+content+i).hide();
			}	
		}
	var length=jQuery("#"+id+"  "+s).length;
	 jQuery("#"+id+"  "+s).each(function(i){
		jQuery(this).click(function(){
			 jQuery("#"+id+"  "+s).removeClass(cur);   
			 closeContent(id,length);
			 jQuery(this).addClass(cur);
			 jQuery("#"+id+content+(i+1)).show();
		},function(){
		});						 
	});
	}//end length
}

//购彩流程
var timeout = null;
function sets(id,cur,num)
{
	timeout =setTimeout('tab_swicth("'+id+'","'+cur+'",'+num+')',250);
}

function tab_swicth(id,cur,num)
{
	jQuery("#"+id+" li").removeClass(cur);
	jQuery(jQuery("#"+id+" li").get(num-1)).addClass(cur);
	jQuery(".tab .nav_main").hide();
	jQuery("#"+id+"_main_"+num).show();
}

//开奖公告
function show_detail(id,num)
{
	jQuery("#tabs1_main_"+id+" li .tabs1_list2").hide();
	jQuery("#tabs1_main_"+id+" li .tabs1_list3").hide();
	jQuery("#tabs1_main_"+id+" #tabs1_main_"+id+"_"+num+" .tabs1_list2").show();
	jQuery("#tabs1_main_"+id+" #tabs1_main_"+id+"_"+num+" .tabs1_list3").show();
}

//快速投注
function click_tabs(id,cur,s){
	var content="_main_";
	if ( jQuery("#"+id).length){
	function closeContent(id,length){
		for(var i=1;i<=length;i++){
		jQuery("#"+id+content+i).hide();
			}	
		}
	var length=jQuery("#"+id+"  "+s).length;
	 jQuery("#"+id+"  "+s).each(function(i){
		jQuery(this).click(function(){
			 jQuery("#"+id+"  "+s).removeClass(cur);   
			 closeContent(id,length);
			 jQuery(this).addClass(cur);
			 jQuery("#"+id+content+(i+1)).show();
		},function(){
		});						 
	});
	}//end length
}
var pagenum=2
var pagedown=0;

//彩博名人
function page_up(id)
{
	jQuery("#"+id+" #up_down_up").attr("num",1);
	jQuery("#"+id+" #up_down_down").attr("num",0);
}
function page_down(id)
{
	jQuery("#"+id+" #up_down_up").attr("num",0);
	jQuery("#"+id+" #up_down_down").attr("num",1);
}
var pagenum=1;
var pagenum2=1;
$(function(){
if(jQuery("#box5_con_list").jCarouselLite){
jQuery("#box5_con_list").jCarouselLite({
      btnNext: "#up_down_ul #up_down_down",
      btnPrev: "#up_down_ul #up_down_up",
      visible:2,
	  circular: false,
	 // auto:800,
	  speed:900,
	  scroll:2,
	  vertical:true,
	  beforeStart: function(a) { 
	    setValue("up_down_ul",pagenum,1);

		 }
});}

if(jQuery("#box5_con_list2").jCarouselLite){
jQuery("#box5_con_list2").jCarouselLite({
      btnNext: "#up_down_ul2 #up_down_down",
      btnPrev: "#up_down_ul2 #up_down_up",
      visible:2,
	  circular: false,
	 // auto:800,
	  speed:900,
	  scroll:2,
	  vertical:true,
	  beforeStart: function(a) { 
	    setValue("up_down_ul2",pagenum2,2);

		 }
});}
});

function setValue(id,page,type)
{
  if(jQuery("#"+id+" #up_down_up").attr("num")==1)
{
    if(page>1)
    page--;
}
else
{
    if(page<3)
    page++;
}
		
	 if(page==1)
	   {
			jQuery("#"+id+" #up_down_up").removeClass();
			jQuery("#"+id+" #up_down_up").addClass("up_1");
			jQuery("#"+id+" #up_down_down").removeClass();
			jQuery("#"+id+" #up_down_down").addClass("down");
	   }
	   else if(page==2)
	   {
			jQuery("#"+id+" #up_down_up").removeClass("up_1");
			jQuery("#"+id+" #up_down_up").addClass("up");
			jQuery("#"+id+" #up_down_down").removeClass("down_1");
			jQuery("#"+id+" #up_down_down").addClass("down");
	   }
	   else if(page==3)
	   {
			jQuery("#"+id+" #up_down_down").removeClass("down");
			jQuery("#"+id+" #up_down_down").addClass("down_1");
	   }
if(type==1)
pagenum=page
else
pagenum2=page;
}
//特色栏目推荐
$(function(){
	$("#menuhdid").mouseover(function() {
    	$("#specintro").show();
    	$("#menuhdid").removeClass('menuhdid');
    	$("#menuhdid").addClass('menuhd_on');
    });
    
	$("#menuhdid").mouseout(function() {
		$("#specintro").hide();
		$("#menuhdid").removeClass('menuhd_on');
    $("#menuhdid").addClass('menuhdid');
    });
});

//乐语客服 - 第二个客服链接
function clickSecondDoyooLink(){
	var p = "";
	var con = doyoo.icon.config;
	if (con.mode == 0) {
		if (con.target != "" && con.target != "0") {
			p += "n=" + con.target;
		}
	} else if (con.target != null && con.target != "0") {
		p += "g=" + con.target;
	}
	doyoo.util.openChat(p);
}