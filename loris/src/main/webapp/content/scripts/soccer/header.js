/**
 * Created with IntelliJ IDEA.
 * User: clone
 * Date: 13-7-17
 * Time: 上午10:08
 * To change this template use File | Settings | File Templates.
 */


var H = H ||{};

H.init = function(){
    this.bindEvent();
}

H.bindEvent = function(){
       var _this = this ;
       $("#hg_top, #scoreTop").on('mouseenter ',function(){
              $("#scoreTop").show()
       })

       $("#hg_top, #scoreTop").on('mouseleave',function(){
              $("#scoreTop").hide();
       })

       $("#scoreTop :radio").unbind().bind('click',function(){
             var t = $(this).attr('t');
             if(!t) return ;
             $("#s_"+t).show().siblings('.con').hide()
             $("#s_"+t).parent().find('div.con:first').show()
             _this.setJF($("#s_" + t));

       })

       $("#scoreTop div.con").delegate('dl','mouseenter',function(){
             if($(this).attr('class') == 'first') return ;
             $(this).addClass('hover');
       })

        $("#scoreTop div.con").delegate('dl','mouseleave',function(){
              if($(this).attr('class') == 'first') return ;
              $(this).removeClass('hover');
        })

        $(".play-con ul").delegate('li','mouseenter',function(){
            if($(this).parent().attr('class') == 'tit') return ;
            $(this).addClass('hover');
        })

        $(".play-con ul").delegate('li','mouseleave',function(){
            if($(this).parent().attr('class') == 'tit') return ;
            $(this).removeClass('hover');
        })


        $("#lc_head h2").unbind().bind('mouseenter',function(){
                $(this).addClass('hover');
                if($(this).data("data")) return ;
                _this.loadMatchInfos(this);

        })

        $("#lc_head").delegate('h2','mouseleave',function(){
                 if($(this).hasClass('cur')) return ;
                 $(this).removeClass('hover');
        })

        $("#lc_headr > li").unbind().bind('mouseenter',function(){
                $(this).addClass('hover');
                if($(this).data("data")) return ;
                _this.loadMatchInfos(this);
        })

        $("#lc_headr").delegate('li','mouseleave',function(){
                if($(this).hasClass('cur')) return ;
                $(this).removeClass('hover');
        })


       $("#head_pre,#hjc_pre,#hbd_pre,#hzc_pre").unbind().bind('click',function(){
                var t = $(this).attr('type');
                _this.preNext(1,t)
       });

       $("#head_next,#hjc_next,#hbd_next,#hzc_next").unbind().bind('click',function(){
                var t = $(this).attr('type');
                _this.preNext(0,t)
       });
}

H.setJF = function(obj){
     var dl = obj.find('[id=cur]').clone();
     $("#hg_top").find('dl:gt(0)').remove();
     $("#hg_top").find('dl.first').after(dl);
}

H.loadMatchInfos = function(obj){
    var _t = obj
    var pid = $(obj).attr('pid'),t = $(obj).attr('t'), p = $(obj).attr('p'),lc = $(obj).attr('lc'),d = $(obj).attr('d'),type= $(obj).attr('type');
    var url = '/dynamic/'+pid+'/'+p+'/'+t+'/'+d;
    var data = {lc:lc};
    $.ajax({
        url:url,
        type:'post',
        data:data,
        success:function(result){
            if(!result) return ;
            $(_t).find('div.lunci-scroll').append(result);
            $(_t).data('data',1)
            var ul_height = $("#"+type+'_'+lc).height();
            if(ul_height < 385) $(_t).find('div.lunci-scroll').height(ul_height)
        },error:function(){}
    })
}


H.preNext = function(m,type){
        var now = $("#now_"+type).html(),ul;
        if(m){
            ul = $("#"+type+"_"+now).next('ul')
        }else{
            ul = $("#"+type+"_"+now).prev('ul')
        }
        if(!ul || !ul.length) return ;
        if(!ul.attr('id')) return ;
        var id = ul.attr('id').split('_')[1];
        ul.show()
        $("#now_"+type).html(id)
        $("#"+type+"_"+now).hide();
       var ul_height = ul.height();
       if(ul_height < 385){
           $(ul).parent().height(ul_height)
       }else{
           $(ul).parent().height(385)
       }

}

$(function(){
     H.init();
})