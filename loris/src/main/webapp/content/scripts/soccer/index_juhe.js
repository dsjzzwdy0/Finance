
var tab_sszx = tab_sszx || {};

tab_sszx.tabs = 'oTab';

tab_sszx.init = function(){
     tab_sszx.bindEvent();
};

tab_sszx.bindEvent = function(){
           var _this = this;
           $('ul.leaguelist').find('li').delegate('span','mouseenter',function(){
                 $(this).find('.dropmenu').show();
				 $(this).find('span').toggleClass('status_1');
                
				 $(this).find('.dropmenu').delegate('li','mouseenter',function(){
                      $(this).toggleClass('li_2');
                      $(this).find('dl').show();
                 });
				 $(this).find('.dropmenu').delegate('li','mouseleave',function(){
					 $(this).toggleClass('li_2');
					 $(this).find('dl').hide();
				 });
           });
           $('ul.leaguelist').find('li').delegate('span','mouseleave',function(){
        	   $(this).find('.dropmenu').hide();
        	   $(this).find('span').toggleClass('status_1');
        	   $(this).find('.dropmenu').delegate('li','hover',function(){
        		   $(this).toggleClass('li_2');
        		   $(this).find('dl').toggle();
        	   });
           });

          $('#tabs1 li').click(function(){
                 _this.tabShow(this);
          });
};

tab_sszx.tabShow = function(obj){
          var type = $(obj).attr('id');
          if(type !==undefined || type !==null){
              $('#'+tab_sszx.tabs).attr('class','');
              $('#div_'+tab_sszx.tabs).hide();
          }
          tab_sszx.tabs = type;
          $('#'+type).attr('class','cur');
          $("#div_"+type).show();
};