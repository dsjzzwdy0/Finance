/**
 * 
 */

$(document).ready(function(){
	$("#sub_1").click(function(){
		$("#form_1").submit();
	});
	$("#sub_2").click(function(){
		$("#form_2").submit();
	});
	
	$("#liansai").hover(
		function(){
			$("#liansailist").show();
		},function(){
			$("#liansailist").hide();
		}
	);
	$("#beisai").hover(
		function(){
			$("#beisailist").show();
		},function(){
			$("#beisailist").hide();
		}
	);
	$("#liansailist").hover(
		function(){$(this).show();},function(){$(this).hide();}
	);
	$("#beisailist").hover(
		function(){$(this).show();},function(){$(this).hide();}
	);

	$(".box em").click(
		function(){
			var li = $(this);
			if(li.hasClass("em_2")) return;
			// $("#selectTeam2").hide();
			$("#selectTeam").find('span').html('请选择球队');  
			$("#selectTeam2").find('span').html('请选择球队');  
			$(".box em").removeClass("em_2");
			$(".box em").addClass("em_1");
			var inx=li.addClass("em_2").html();
			getSchedule(inx);
		}
	);
	

	$(".nav li").click(
		function(){
			var li = $(this);
			if(li.hasClass("on")) return;
			$(".nav li").removeClass("on");
			var inx=$(".nav li").index(li.addClass("on"));
			$(".coretab").hide();
			getRank(inx);
		}
	);
	//$(".box li:eq(0)").click();
	$(".nav li:eq(0)").click();
	
	$("#selectYear").change(function()
	{
		window.location.href = "http://saishi.zgzcw.com:80/soccer/league/"+'34'+"/"+$("#selectYear").val() + '';
	});
	$(".div-select span").click(function(){
		$(this).siblings('ul').show();
	});
	$('.div-select').mouseleave(function(){
		$(this).find('ul').hide();
	})
	$("#selectTeam li").click(function()
	{		var thisHtml = $(this).html();
			var thisVal = $(this).attr('value');
			$(this).parents('#selectTeam').attr('value',thisVal);
			$(this).parent().siblings('span').html(thisHtml);
			$(this).parent().hide();
			$(".box em").removeClass("em_2");
			$(".box em").addClass("em_1");
			// $("#selectTeam2").show();
	       if($("#fjsai li.cur").index('#fjsai li')=="1" || $("#fjsai li.cur").index('#fjsai li')=="2"){
	    	   getMatchGamefj($("#selectTeam").attr('value'),$("#selectTeam2").attr('value'));
	       }else{
			
			   if ($("#selectTeam").attr('value')!="0"&&$("#selectTeam2").attr('value')=="0"&&$("#fjsai li.cur").index('#fjsai li')=="0"){	  	
					getMatchGame($("#selectTeam").attr('value'));
			   	};
			   	if ($("#selectTeam").attr('value')!="0"&&$("#selectTeam2").attr('value')=="0"&&$("#fjsai li.cur").index('#fjsai li')=="0"){	  	
					getMatchGame($("#selectTeam").attr('value'));
			   	};	
			   if ($("#selectTeam").attr('value')=="0"&&$("#selectTeam2").attr('value')&&$("#fjsai li.cur").index('#fjsai li')=="0"){	  	
					getMatchGame($("#selectTeam2").attr('value'));
			   	};		
				  	
			   if ($("#selectTeam").attr('value')!="0"&&$("#selectTeam2").attr('value')!="0"&&$("#fjsai li.cur").index('#fjsai li')=="0"){		   		
					getMatchGame2($("#selectTeam").attr('value'),$("#selectTeam2").attr('value'));
			   	};
	       }
	});

	$("#selectTeam2 li").click(function()
	{	var thisHtml = $(this).html();
		var thisVal = $(this).attr('value');
		$(this).parents('#selectTeam2').attr('value',thisVal);
		$(this).parent().siblings('span').html(thisHtml);
		$(this).parent().hide();
	 	if($("#fjsai li.cur").index('#fjsai li')=="1" || $("#fjsai li.cur").index('#fjsai li')=="2"){
	    	   getMatchGamefj($("#selectTeam").attr('value'),$("#selectTeam2").attr('value'));
	       }else{
		   if ($("#selectTeam").attr('value')=="0"&&$("#selectTeam2").attr('value')!="0"&&$("#fjsai li.cur").index('#fjsai li')=="0"){
				getMatchGame($("#selectTeam2").attr('value'));
		   }
		   if ($("#selectTeam").attr('value')!="0"&&$("#selectTeam2").attr('value')=="0"&&$("#fjsai li.cur").index('#fjsai li')=="0"){
				getMatchGame($("#selectTeam").attr('value'));
		   }
		   if ($("#selectTeam").attr('value')!="0"&&$("#selectTeam2").attr('value')!="0"&&$("#fjsai li.cur").index('#fjsai li')=="0"){
				getMatchGame2($("#selectTeam").attr('value'),$("#selectTeam2").attr('value'));
		   }
	      }
	});

	$("#fjsai li").click(function(){
		$(this).addClass('cur').siblings('#fjsai li').removeClass('cur');
        getMatchGamefj($("#selectTeam").attr('value'),$("#selectTeam2").attr('value'));   
    });
});

function getMatchGamefj(par1,par2){
	var season = $("#selectYear").val();
	var leagueId = '34';
	if(par1 == '0'){
		p1 = '';
	}
	p1 = par1;
	if(par2 =='0'){
		p2 = '';
	}
	p2 = par2;
	if($("#fjsai li.cur").index('#fjsai li')=="2")
	{
	    $.ajax({
	    	type: "POST",
			url: "http://saishi.zgzcw.com:80/summary/liansaifjAction.action",
			data: "source_league_id="+leagueId+"&season="+season+"&hostTeamId="+p1+"&guestTeamId="+p2+"&seasonType=2",
			dataType: "html",
			success: function(isExists){
				if (!!isExists) {	
				    $("#matchInfofj").html(isExists);
				    $(".league_right_021").hide();
				    $(".league_right_03").hide();
				    $("#matchInfo").hide();
					$("#matchInfofj").show();
					$("#hideList").hide();
				};
			}
		});
	}
	else if($("#fjsai li.cur").index('#fjsai li')=="1")
	{
		$.ajax({
			type: "POST",
			url: "http://saishi.zgzcw.com:80/summary/liansaifjAction.action",
			data: "source_league_id="+leagueId+"&season="+season+"&hostTeamId="+p1+"&guestTeamId="+p2+"&seasonType=1",
			dataType: "html",
			success: function(isExists){							
				if (!!isExists) {	
				    $("#matchInfofj").html(isExists);
				    $(".league_right_021").hide();
				    $(".league_right_03").hide();
				    $("#matchInfo").hide();
					$("#matchInfofj").show();
					$("#hideList").hide();
				};
			}
		});	
	}
	else if($("#fjsai li.cur").index('#fjsai li') =="0")
	{
		$("#matchInfofj").hide();
		$("#matchInfo").show();
		$(".league_right_021").show();
		$(".league_right_03").show();
	}
}

function getRank(inx){
	$(".coretab:eq("+inx+")").show();
	if(inx == 3){
		$("#corehead1").hide();
		$("#corehead2").show();
	}
	else{
		$("#corehead1").show();
		$("#corehead2").hide();
	}
	$(".coretab tr").unbind().hover(
		function(){$(this).addClass("hover");},function(){$(this).removeClass("hover");}
	);
}
function getSchedule(lunci){
	$(".scheduletab tr").unbind().hover(
		function(){$(this).addClass("hover");},function(){$(this).removeClass("hover");}
	);

	if($("#tab_"+lunci).length>0){
		$("#matchInfo > table").hide();
		$("#tab_"+lunci).show();
		return;
	}
 	
	var season = '2017-2018';
	var leagueId = '34';
	var seasonType = '';
	$.ajax({
		type: "POST",
		url: "http://saishi.zgzcw.com:80/summary/liansaiAjax.action",
		data: "source_league_id="+leagueId+"&currentRound="+lunci+"&season="+season+"&seasonType="+seasonType,//+"&guestTeamId="+guestTeamId,
		dataType: "html",
		success: function(isExists){	
			////var stm=isExists.split("@");
			if (isExists != "") 
			{
				$("#matchInfo > table").hide();
				$("#matchInfo").append(isExists);
		    };
		}
	});
}

function getMatchGame(matchid){
	$("#selectTeam li").show();
	$("#selectTeam2 li").show();

	var season = $("#selectYear").val();
	var leagueId = '34';
	var seasonType = '';

	$.ajax({
		type: "POST",
		url: "http://saishi.zgzcw.com:80/summary/liansaiAjax.action",
		data: "source_league_id="+leagueId+"&season="+season+"&hostTeamId="+matchid+"&seasonType="+seasonType,//+"&guestTeamId="+guestTeamId,
		dataType: "html",
		success: function(isExists)
		{							
			//var stm=isExists.split("@");
			if (isExists != "") 
			{			
				$("#matchInfo > table").hide();
				$("#matchInfo").append(isExists);
					
				if($("#selectTeam").attr('value')!="0"){
					$("#selectTeam2 li[value='"+matchid+"']").hide();
				}
				if($("#selectTeam2").attr('value')!="0"){
					$("#selectTeam li[value='"+matchid+"']").hide();
				}
				/* $("#selectTeam2").empty();
				$("#selectTeam2").prepend("<option value='0'>选择球队</option>");		
				$("#selectTeam2").append(stm[1]); */	
		    };
		}
	});

}

function getMatchGame2(mid,sid){
	$("#selectTeam li").show();
	$("#selectTeam2 li").show();
	var season = $("#selectYear").val();
	var leagueId = '34';
	var seasonType = '';

	$.ajax({
		type: "POST",
		url: "http://saishi.zgzcw.com:80/summary/liansaiAjax.action",
		data: "source_league_id="+leagueId+"&season="+season+"&hostTeamId="+mid+"&guestTeamId="+sid+"&seasonType="+seasonType,
		dataType: "html",
		success: function(isExists){
			//var stm=isExists.split("@");
			if (isExists!="") {
				$("#matchInfo > table").hide();
				$("#matchInfo").append(isExists);					
				$("#selectTeam2 li[value='"+mid+"']").hide();					
				$("#selectTeam li[value='"+sid+"']").hide();
		    };
		}
	});
}


$(function() {
	$('.team_lian span').mouseover(function() {
		$(this).siblings('.leagueSelBox').show();
	});
	$('.hoverSelect span').mouseover(function() {
		$(this).siblings('.select_options').show();
	});

	// $('.xuanze a').bind('click',function(e){
	// 	$(this).parents('.leagueSelBox').hide();
	// 	e.stopPropagation();
	// 	e.preventDefault();
	// })
	$('.team_lian').mouseleave(function() {
		$(this).find('.leagueSelBox').hide();
	});
	$('.gamesSelect dd').mouseover(function() {
		$(this).addClass('crt');
	}).mouseleave(function() {
		$(this).removeClass('crt');
	})
	//选项卡切换
	function tabsChange() {
		var chosenVar = $('.chooseHead').find('.crt').attr('id');
		$('.gamesSelect').find('.' + chosenVar + '_content').show()
				.siblings('.rlea').hide();
	}
	tabsChange();
	$('.chooseHead li').click(function() {
		$(this).addClass('crt').siblings('li').removeClass('crt');
		tabsChange();
	})
})


$(document).ready(function(){
	//选项卡切换
	function tabsChange(){
		var chosenVar = $('#tabs1').find('.cur').attr('value');
		$('.table_out').find('#tabs1_main_'+chosenVar).show().siblings('.tabs1_main_ul').hide();
	}
	tabsChange();
	$('#tabs1 li').click(function(){
		$(this).addClass('cur').siblings('li').removeClass('cur');
		tabsChange();
	})

	$("#sub_1").click(function(){
		$("#form_1").submit();
	});
	$("#sub_2").click(function(){
		$("#form_2").submit();
	});
	
	$("#liansai").hover(
		function(){
			$("#liansailist").show();
		},function(){
			$("#liansailist").hide();
		}
	);
	$("#beisai").hover(
		function(){
			$("#beisailist").show();
		},function(){
			$("#beisailist").hide();
		}
	);
	$("#liansailist").hover(
		function(){$(this).show();},function(){$(this).hide();}
	);
	$("#beisailist").hover(
		function(){$(this).show();},function(){$(this).hide();}
	);

	$(".box em").click(
		function(){
			var li = $(this);
			if(li.hasClass("em_2")) return;
			$("#selectTeam2").hide();
			$("#selectTeam").get(0).selectedIndex=0;  
			$("#selectTeam2").get(0).selectedIndex=0;  
			$(".box em").removeClass("em_2");
			$(".box em").addClass("em_1");
			var inx=li.addClass("em_2").html();
			getSchedule(inx);
		}
	);


	$(".nav li").click(
		function(){
			var li = $(this);
			if(li.hasClass("on")) return;
			$(".nav li").removeClass("on");
			var inx=$(".nav li").index(li.addClass("on"));
			$(".coretab").hide();
			getRank(inx);
		}
	);
	//$(".box li:eq(0)").click();
	$(".nav li:eq(0)").click();
	
	$("#selectYear").change(function()
	{
		window.location.href = "http://saishi.zgzcw.com:80/soccer/league/"+'34'+"/"+$("#selectYear").val() + '';
	});

	$("#selectTeam").change(function()
	{
			$(".box em").removeClass("em_2");
			$(".box em").addClass("em_1");
			$("#selectTeam2").show();
	       if($("#fjsai").val()=="1"){
	    	   getMatchGamefj($("#selectTeam").val(),$("#selectTeam2").val());
	       }else{
			
		   if ($("#selectTeam").val()!="0"&&$("#selectTeam2").val()=="0"&&$("#fjsai").val()=="0"){	  	
				getMatchGame($("#selectTeam").val());
		   	};
		   	if ($("#selectTeam").val()!="0"&&$("#selectTeam2").val()=="0"&&$("#fjsai").val()=="0"){	  	
				getMatchGame($("#selectTeam").val());
		   	};	
		   if ($("#selectTeam").val()=="0"&&$("#selectTeam2").val()!="0"&&$("#fjsai").val()=="0"){	  	
				getMatchGame($("#selectTeam2").val());
		   	};		
			  	
		   if ($("#selectTeam").val()!="0"&&$("#selectTeam2").val()!="0"&&$("#fjsai").val()=="0"){		   		
				getMatchGame2($("#selectTeam").val(),$("#selectTeam2").val());
		   	};
	       }

	});

	$("#selectTeam2").change(function()
	{
		if($("#fjsai").val()=="1"){
			getMatchGamefj($("#selectTeam").val(),$("#selectTeam2").val());
	    }
		else
	    {
		   if ($("#selectTeam").val()=="0"&&$("#selectTeam2").val()!="0"&&$("#fjsai").val()=="0"){
				getMatchGame($("#selectTeam2").val());
		   }
		   if ($("#selectTeam").val()!="0"&&$("#selectTeam2").val()=="0"&&$("#fjsai").val()=="0"){
				getMatchGame($("#selectTeam").val());
		   }
		   if ($("#selectTeam").val()!="0"&&$("#selectTeam2").val()!="0"&&$("#fjsai").val()=="0"){
				getMatchGame2($("#selectTeam").val(),$("#selectTeam2").val());
		   }
	    }
	});

	$("#fjsai").change(function(){
		getMatchGamefj($("#selectTeam").val(),$("#selectTeam2").val());   
    });

});