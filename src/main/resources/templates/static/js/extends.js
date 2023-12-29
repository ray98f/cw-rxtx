//这里是对easyui进行一些自定义以适应框架

//表单校验时候的提示，比较通用的，框架里应到了些远程校验是否已经存在的，这个可以自己定义提示
$.fn.validatebox.defaults.missingMessage="该项不能为空";
$.fn.validatebox.defaults.rules.email.message="请输入正确的Email地址";
$.fn.validatebox.defaults.rules.url.message="请输入正确的网址";
$.fn.validatebox.defaults.rules.length.message="请输入{0}到{1}之间的值";
$.fn.validatebox.defaults.rules.remote.message="数据远程校验失败，请填写符合要求的内容";

//添加是否重复校验规则，校验方式同remote，仅便于显示与remote区分开的提示信息,对于一般的远程校验用remote即可
$.extend($.fn.validatebox.defaults.rules, {
  isRepeat: {
  validator: function(value, param){
    var d = {};
    d[param[1]]=value;
    return "true"==$.ajax({url:param[0],dataType:"json",data:d,async:false,cache:false,type:"post"}).responseText;
  },
  message: '该内容已经存在'
  }
  });
$.extend($.fn.validatebox.defaults.rules, {
  isPasswordValid: {
  validator: function(value, param){
    var d = {};
    d[param[1]]=value;
    return "true"==$.ajax({url:param[0],dataType:"json",data:d,async:false,cache:false,type:"post"}).responseText;
  },
  message: '旧密码不正确'
  }
  });
$.extend($.fn.validatebox.defaults.rules, {
  equals: {
      validator: function(value,param){
          return value == $(param[0]).val();
      },
      message: '两次输入的内容不相同'
  }
});
//最小长度校验 
$.extend($.fn.validatebox.defaults.rules, {
  minLength: {
      validator: function(value, param){
          return value.length >= param[0];
      },
      message: '请至少输入{0}位字符'
  }
});

//解决后台返回的json分页结构和easyui的不匹配
$.fn.datagrid.defaults.loadFilter=function(data){
  var newdata = {};
  newdata['total'] = data.totalCount;
  newdata['rows'] = data.items;
  return newdata;
 }
/*$.fn.treegrid.defaults.loadFilter=function(data){
	  var newdata = {};
	  newdata['total'] = data.totalCount;
	  newdata['rows'] = data.items;
	  return newdata;
	 }*/
var func_deep = 1;//解决递归深度问题
//grid的一些默认设置
$.fn.datagrid.defaults.singleSelect=false;
//$.fn.datagrid.defaults.onUncheck=function(index,row){
//$(this).datagrid('unselectRow',index);
//
//};
//$.fn.datagrid.defaults.onUnselect=function(index,row){
//$(this).datagrid('uncheckRow',index);
//
//};

//$.fn.datagrid.defaults.onCheck=function(index,row){
//
//var rows = $(this).datagrid('getRows');
//for(var i=0;i<rows.length;i++){
//if(rows[i].id!=row.id){
//$(this).datagrid('uncheckRow',i);
//}
//}
//if(func_deep++==1)
//$(this).datagrid('selectRow',index);
//func_deep=1;
//
//};
//$.fn.datagrid.defaults.onSelect=function(index,row){
//if(func_deep++==1)
//$(this).datagrid('checkRow',index);
//func_deep=1;
//
//};
//$.fn.datagrid.defaults.onLoadSuccess=function(){$(this).datagrid('clearSelections');}
$.fn.datagrid.defaults.pageNumber=1;
$.fn.datagrid.defaults.pageSize=10;
$.fn.datagrid.defaults.nowrap=true;
$.fn.datagrid.defaults.fitColumns=true;
$.fn.datagrid.defaults.showPageList=false;
$.fn.datagrid.defaults.pagination=true;
$.fn.datagrid.defaults.width=$("#body").width();
$.fn.datagrid.defaults.idField='id';
$.fn.datagrid.defaults.selectOnCheck=true;
$.fn.datagrid.defaults.checkOnSelect=true;
$.fn.datagrid.defaults.toolbar='#tb';
$.fn.datagrid.defaults.scrollbarSize=0;

$.fn.treegrid.defaults.pageNumber=1;
$.fn.treegrid.defaults.pageSize=10;
$.fn.treegrid.defaults.nowrap=true;
$.fn.treegrid.defaults.fitColumns=true;
$.fn.treegrid.defaults.showPageList=false;
$.fn.treegrid.defaults.pagination=true;
$.fn.treegrid.defaults.width=$("#body").width();
$.fn.treegrid.defaults.idField='id';
$.fn.treegrid.defaults.selectOnCheck=true;
$.fn.treegrid.defaults.checkOnSelect=true;
$.fn.treegrid.defaults.toolbar='#tb';

//翻页工具栏文字
if ($.fn.pagination) {
  $.fn.pagination.defaults.showPageList = false;
      $.fn.pagination.defaults.beforePageText = "第 <span id='currentPage'>1</span> 页  转到",
      $.fn.pagination.defaults.afterPageText = "页&nbsp; <a style='border:0;text-decoration:none; font-size:15px;font-weight:bold;color:#8DB2E3' href='javascript:void(0);' onclick='jumpPage()'>GO</a>&nbsp; 共 {pages} 页";
  $.fn.pagination.defaults.displayMsg = "当前{from}-{to} 条  共{total}条记录";
}
if ($.fn.datagrid) {
  $.fn.datagrid.defaults.loadMsg = '正在加载...';
}
function jumpPage(){
//设置键盘事件 并将其设置为按下回车
var e=$.Event("keydown");
e.keyCode=13;
$("input.pagination-num:visible").trigger(e);//模拟页码框按下回车
}

// window窗体默认属性
$.fn.window.defaults.resizable = true;
$.fn.window.defaults.noheader = false;
$.fn.window.defaults.collapsible = false;
$.fn.window.defaults.minimizable = false;
$.fn.window.defaults.maximizable = false;
$.fn.window.defaults.shadow = false;
$.fn.window.defaults.modal = true;
$.fn.window.defaults.title = ' ';
$.fn.window.defaults.loadingMessage = '正在加载...';

// 信息框按钮文字
if ($.messager) {
  $.messager.defaults.ok = '确定';
  $.messager.defaults.cancel = '取消';
}

//日期控件
$.fn.datebox.defaults.formatter = function(date){
  var y = date.getFullYear();
  var m = date.getMonth()+1;
  var d = date.getDate();
  return y+'-'+m+'-'+d;
  };
$.fn.datebox.defaults.missingMessage = "该项不能为空";

//combobox
$.fn.combobox.defaults.missingMessage = "该项不能为空";
//$.fn.combobox.defaults.editable =false ;
//$.fn.combobox.defaults.delay =600 ;
//$.fn.combobox.defaults.onChange=function(nv,ov){var tt=$(this).combobox('getText');if(tt=="") return;var flag = true;$.each($(this).combobox('getData'),function(i,v){if(nv==v.value||v.name.startWith(tt)){flag=false;return;}});if(flag){$(this).combobox('clear');$.messager.alert('提示','无匹配数据，请从下拉列表中选择！');}}
$.fn.combobox.defaults.formatter=function (row) {
    var opts = $(this).combobox('options');
    if(opts.multiple){
    	return '<input type="checkbox" class="combobox-checkbox">&nbsp;' + row[opts.textField]
    }else{
    	return '<input type="radio" name="'+($(this).attr('comboname'))+'" class="combobox-checkbox">&nbsp;' + row[opts.textField]
    }
    
};
$.fn.combobox.defaults.onLoadSuccess=function () {
    var opts = $(this).combobox('options');
    var target = this;
    var values = $(target).combobox('getValues');
    $.map(values, function (value) {
        var el = opts.finder.getEl(target,value);
        if(el)
        	el.find('input.combobox-checkbox')._propAttr('checked', true);
    })
};
$.fn.combobox.defaults.onSelect=function (row) {
    //console.log(row);
    var opts = $(this).combobox('options');
    var el = opts.finder.getEl(this, row[opts.valueField]);
    el.find('input.combobox-checkbox')._propAttr('checked', true);
};
$.fn.combobox.defaults.onUnselect=function (row) {
    var opts = $(this).combobox('options');
    var el = opts.finder.getEl(this, row[opts.valueField]);
    el.find('input.combobox-checkbox')._propAttr('checked', false);
};

$.fn.tabs.methods.loading = function(jq, msg) {
  return jq.each(function () {
    var panel = $(this).tabs("getSelected");  
    if (msg == undefined) {
        msg = "页面加载中，请稍候...";  
    }  
    $("<div class=\"datagrid-mask\"></div>").css({ display: "block", background:"#FFFFFF", opacity:"1", width: panel.width(), height: panel.height(), top:panel[0].offsetTop?panel[0].offsetTop:"30px" }).appendTo(panel);  
    $("<div class=\"datagrid-mask-msg\"></div>").html(msg).appendTo(panel).css({ display: "block", left: (panel.width() - $("div.datagrid-mask-msg", panel).outerWidth()) / 2, top: (panel.height() - $("div.datagrid-mask-msg", panel).outerHeight()) / 2 });  
  });  
};

$.fn.tabs.methods.loaded = function(jq) {
  return jq.each(function () {  
    var panel = $(this).tabs("getSelected");  
    panel.find("div.datagrid-mask-msg").remove();
    panel.find("div.datagrid-mask").fadeOut(300);
    setTimeout(function(){
      panel.find("div.datagrid-mask").remove();
    },300);
  });
};
  
/**
 * Created with JetBrains WebStorm.
 * User: cao.guanghui
 * Date: 13-6-26
 * Time: 下午11:27
 * To change this template use File | Settings | File Templates.
 */
$.extend($.fn.datagrid.methods, {
    /**
     * 开打提示功能（基于1.3.3+版本）
     * @param {} jq
     * @param {} params 提示消息框的样式
     * @return {}
     */
    doCellTip:function (jq, params) {
        function showTip(showParams, td, e, dg) {
            //无文本，不提示。
        	if ($(td).text() == "") return;
            params = params || {};
            var options = dg.data('datagrid');
            var styler = 'style="';
            if(showParams.width){
                styler = styler + "width:" + showParams.width + ";";
            }
            if(showParams.maxWidth){
                styler = styler + "max-width:" + showParams.maxWidth + ";";
            }
            if(showParams.minWidth){
                styler = styler + "min-width:" + showParams.minWidth + ";";
            }
            styler = styler + '"';
            showParams.content = '<div class="tipcontent" ' + styler + '>' + showParams.content + '</div>';
            $(td).tooltip({
                content:showParams.content,
                trackMouse:true,
                position:params.position,
                onHide:function () {
                    $(this).tooltip('destroy');
                },
                onShow:function () {
                    var tip = $(this).tooltip('tip');
                    if(showParams.tipStyler){
                        tip.css(showParams.tipStyler);
                    }
                    if(showParams.contentStyler){
                        tip.find('div.tipcontent').css(showParams.contentStyler);
                    }
                }
            }).tooltip('show');
        };
        return jq.each(function () {
            var grid = $(this);
            var options = $(this).data('datagrid');
            if (!options.tooltip) {
                var panel = grid.datagrid('getPanel').panel('panel');
                panel.find('.datagrid-body').each(function () {
                    var delegateEle = $(this).find('> div.datagrid-body-inner').length ? $(this).find('> div.datagrid-body-inner')[0] : this;
                    $(delegateEle).undelegate('td', 'mouseover').undelegate('td', 'mouseout').undelegate('td', 'mousemove').delegate('td[field]', {
                        'mouseover':function (e) {
                            //if($(this).attr('field')===undefined) return;
                            var that = this;
                            var setField = null;
                            if(params.specialShowFields && params.specialShowFields.sort){
                                for(var i=0; i<params.specialShowFields.length; i++){
                                    if(params.specialShowFields[i].field == $(this).attr('field')){
                                        setField = params.specialShowFields[i];
                                    }
                                }
                            }
                            if(setField==null){
                                options.factContent = $(this).find('>div').clone().css({'margin-left':'-5000px', 'width':'auto', 'display':'inline', 'position':'absolute'}).appendTo('body');
                                var factContentWidth = options.factContent.width();
                                params.content = $(this).text();
                                if (params.onlyShowInterrupt) {
                                    if (factContentWidth > $(this).width()) {
                                        showTip(params, this, e, grid);
                                    }
                                } else {
                                    showTip(params, this, e, grid);
                                }
                            }else{
                                panel.find('.datagrid-body').each(function(){
                                    var trs = $(this).find('tr[datagrid-row-index="' + $(that).parent().attr('datagrid-row-index') + '"]');
                                    trs.each(function(){
                                        var td = $(this).find('> td[field="' + setField.showField + '"]');
                                        if(td.length){
                                            params.content = td.text();
                                        }
                                    });
                                });
                                showTip(params, this, e, grid);
                            }
                        },
                        'mouseout':function (e) {
                            if (options.factContent) {
                                options.factContent.remove();
                                options.factContent = null;
                            }
                        }
                    });
                });
            }
        });
    },
    /**
     * 关闭消息提示功能（基于1.3.3版本）
     * @param {} jq
     * @return {}
     */
    cancelCellTip:function (jq) {
        return jq.each(function () {
            var data = $(this).data('datagrid');
            if (data.factContent) {
                data.factContent.remove();
                data.factContent = null;
            }
            var panel = $(this).datagrid('getPanel').panel('panel');
            panel.find('.datagrid-body').undelegate('td', 'mouseover').undelegate('td', 'mouseout').undelegate('td', 'mousemove')
        });
    }
});