var $parent = self.parent.$;
$(function() {
//IE8下indexOf
  if (!Array.prototype.indexOf) {
    Array.prototype.indexOf = function(elt) {
      var len = this.length >>> 0;
      var from = Number(arguments[1]) || 0;
      from = (from < 0) ? Math.ceil(from) : Math.floor(from);
      if (from < 0)
        from += len;
      for (; from < len; from++) {
        if (from in this && this[from] === elt)
          return from;
      }
      return -1;
    };
  }
  // IE8下Object.keys()
  if (!Object.keys) {
    Object.keys = (function() {
      'use strict';
      var hasOwnProperty = Object.prototype.hasOwnProperty, hasDontEnumBug = !({
        toString : null
      }).propertyIsEnumerable('toString'), dontEnums = [ 'toString',
          'toLocaleString', 'valueOf', 'hasOwnProperty', 'isPrototypeOf',
          'propertyIsEnumerable', 'constructor' ], dontEnumsLength = dontEnums.length;

      return function(obj) {
        if (typeof obj !== 'object'
            && (typeof obj !== 'function' || obj === null)) {
          throw new TypeError('Object.keys called on non-object');
        }

        var result = [], prop, i;

        for (prop in obj) {
          if (hasOwnProperty.call(obj, prop)) {
            result.push(prop);
          }
        }

        if (hasDontEnumBug) {
          for (i = 0; i < dontEnumsLength; i++) {
            if (hasOwnProperty.call(obj, dontEnums[i])) {
              result.push(dontEnums[i]);
            }
          }
        }
        return result;
      };
    }());
  }
  
  // 隐藏显示查询条件区域
  $('#openOrClose').on("click", function() {
    $('#conditon').toggle(80);
  });

  // 页面中需要定义一个变量 值为grid容器id

  window.onresize = debounce(domresize,600,false);
  
});
var debounce = function(func, threshold, execAsap) {
  var timeout;
  return function debounced() {
    var obj = this, args = arguments;
    function delayed() {
      if (!execAsap)
        func.apply(obj, args);
      timeout = null;
    }
    ;
    if (timeout)
      clearTimeout(timeout);
    else if (execAsap)
      func.apply(obj, args);
    timeout = setTimeout(delayed, threshold || 100);
  };
} 
function domresize() {
  if(typeof grid!='undefined'){
    try {
     var mt = $('#search_area').css("margin-top").replace("px","")-0,
           mb = $('#search_area').css("margin-bottom").replace("px","")-0;
     $('#'+grid).datagrid('resize',{
         height:$("#body").height()-$('#search_area').outerHeight()-mt-mb,
         width:$("#body").width()
     });
    }catch(e){
/*      if(console){
        console.log(e);
      }*/
    }
  }
  /*
   * 计划填写页面随窗口大小变化更新高度
   */
  var tab = $parent('#tabs').tabs('getSelected');
  if(tab){
    var nowFrame = $("iframe:first-child",tab);
    if(nowFrame){
      if($(nowFrame).contents().find("body#planFillPage").length>0){
          $(nowFrame).contents().find("#planFillfield").height(tab[0].offsetHeight-60);
      }
    }
  }
 }
  
function openDialog(_url, _title, _edit, width, height) {
  if (_edit == 1) {
    $parent("#parent_win").window({
      width : (width ? width : 600),
      height : (height ? height : 390),
      href : _url,
      /* title:_title, 不要弹出框的标题了 */
      title : ' ',
      onClose : function() {
        if (typeof grid != 'undefined')
          // $('#'+grid).datagrid('reload');
          refreshGrid();
      }
    });
  } else {
    openWindow(_url,width,height,_title);
  }
}
/*
 * function openWindow(_url, width, height) { $parent("#parent_win").window({
 * width : (width ? width : 600), height : (height ? height : 400), href : _url,
 * title : ' ' }); }
 */
/**
 * winGrid指的是window里面的gridid指定后会随着win的大小而自适应
 */
function openWindow(_url, width, height,title,winGrid) {
  width = width ? width : 600;
  height = height ? height : 400;
  $parent("#parent_win").window({
    width : width,
    height : height,
    onResize:function(width,height){
      if (typeof winGrid != 'undefined'){
        $parent("#"+winGrid).datagrid('resize',{width:width-2});
      }
    },
    top : (window.screen.height - height) * 0.5 - 60,
    left : (window.screen.width - width) * 0.5,
    title:(title?title:" "),
    href : _url
  });
}

function directoryMapping(_type) {
  var mappingData = "";
  $.ajax({
    type : "post",
    url : ctx + "/common/getMappingByType.action",
    data : {
      type : _type

    },
    async : false,
    dataType : 'json',
    success : function(data) {

      mappingData = data;
    }
  });

  return mappingData;
}
function roleMapping() {
  var mappingData = "";
  $.ajax({
    type : "post",
    url : ctx + "/role/getRole.action",
    async : false,
    dataType : 'json',
    success : function(data) {
      mappingData = data;
    }
  });
  return mappingData;
}
function mappingByUrl(url) {
  var mappingData = "";
  $.ajax({
    type : "post",
    url : url,
    async : false,
    dataType : 'json',
    success : function(data) {
      mappingData = data;
    }
  });
  return mappingData;
}
function doFluidLayout() {
  $
      .each(
          $(".easyui-combobox,.easyui-datebox,.easyui-numberspinner,.easyui-combotree"),
          function(i, v) {
            var per = new RegExp("width:\\s*(\\d{1,3})%", 'i').exec($(v).attr(
                'style'));
            if (per && per.length > 1) {
              per = per[1];
              if ("" == $(v).attr("data-options")
                  || typeof ($(v).attr("data-options")) == 'undefined')
                $(v).attr("data-options", "1:1");
              $(v).attr(
                  "data-options",
                  "width:" + $(v.parentNode).width() * per / 100 + ","
                      + $(v).attr("data-options"));

            }
          });

}

// 添加tab页签
function addTabs2(url, title, icon) {
  var $ = $parent;
  if (!$('#tabs').tabs('exists', title)) {
    $('#tabs')
        .tabs(
            'add',
            {
              title : title,
              content : '<iframe name="mainframe" src="'
                  + url
                  + '" frameBorder="0" border="0" scrolling="yes" style="overflow-y:scroll;width: 100%;height:100%;min-width:1149px;_height:600px;_width:1149px;min-height:600px;"/>',
              closable : true
            });
  } else {
    // 重新点击菜单，刷新tab页
    var tab = $('#tabs').tabs('getTab', title);
    $("#tabs").tabs('update', {
      tab : tab,
      options : {
        title : tab.panel('options').title,
        content : tab.panel('options').content
      }
    });
    $('#tabs').tabs('select', title);
  }
}

// 添加或者更新tab标签(addTabs2只支持页面刷新，addTab支持url重新定向)
function addTab(url, title) {
  var $tabs = $parent("#tabs");
  var content = '<iframe name="mainframe" src="'
      + url
      + '" frameBorder="0" border="0" scrolling="yes" style="overflow-y:scroll;width: 100%;height:100%;min-width:1149px;_height:600px;_width:1149px;min-height:600px;"/>';
  var haveTabs = $tabs.tabs('exists', title);
  if (!haveTabs || title=="新增司机报单") {
    // 新增
    $tabs.tabs('add', {
      title : title,
      content : content,
      closable : true
    })
  } else {
    var $tab = $tabs.tabs('getTab', title);
    // 更新
    $tabs.tabs('update', {
      tab : $tab,
      options : {
        title : title,
        content : content,
        closable : true
      }
    })
    // 聚焦
    $tabs.tabs('select', title);
  }
}

// 权限检查
function ifHasPermission(_title) {
  return true;
}

String.prototype.startWith = function(str) {
  var reg = new RegExp("^" + str);
  return reg.test(this);
}

function startWait(msg) {
  if (!msg)
    msg = "数据加载中...";
  $parent.messager.progress({
    title : '请稍等',
    msg : msg
  });
}
function stopWait() {
  $parent.messager.progress('close');
}

//--窗口遮罩--
function ajaxLoading($div) {
    var mask = {
        display : "block", width : "100%", "z-index" : 9010, height : $(document.body).height() > $(window).height() ? $(document.body).height() : $(window).height()
    }
    var msg = {
        display : "block", "z-index" : 9011, left : ($(document.body).outerWidth(true) - 139) / 2, top : ($(window).height() - 45) / 2 ,height:"auto"
    }
    var text = "正在处理, 请稍候...";
    $("<div class=\"datagrid-mask\"><\/div>").css(mask).appendTo("body");
    $("<div class=\"datagrid-mask-msg\"><\/div>").html(text).appendTo("body").css(msg);
}
//--移掉窗口遮罩--
function ajaxLoadEnd() {
    $(".datagrid-mask").remove();
    $(".datagrid-mask-msg").remove();
}

// 工作流启动并提交
function startAndPrepareSubmitWorkFlow(id, url) {
  startWait();
  $
      .ajax({
        type : 'GET',
        url : url,
        async : true,
        dataType : 'json',
        success : function(data) {
          stopWait();
          if (data.status <= 0) {
            $parent.messager.alert("提示", "启动失败");
            return;
          }
          var taskId = data.taskId;
          data = data.candidates;
          data.taskId = taskId;
          if ((typeof data.grouplist == 'undefined' && typeof data.accountlist == 'undefined')
              || (Object.keys(data.grouplist).length == 0 && data.accountlist.length == 0)) {
            $.ajax({
              type : "GET",
              url : ctx + "/task/submit.action",
              data : {
                'taskId' : taskId,
                'candidates' : '',
                'transition' : '',
                'comments' : ''
              },
              async : false,
              success : function(data) {
                if ('success' == data) {
                  $parent.messager.alert('提示', '启动并提交成功', 'info', function() {
                    refreshGrid();
                  });
                } else {
                  $parent.messager.alert('提示', '启动提交失败', 'error');
                }
              }
            });
          } else {
            renderUserChooser(data);
          }
        }
      })
}
var workFlowCandiCache = [];
var workFlowIsSend = false;
function renderUserChooser(datas) {
  var userChooseTable = $("<table style='width:100%;margin-bottom:10px;padding:5px;' class='table'><tr class='success' style='height:32px;'><td colspan='4' style='text-align:center;background:#f5f8fa'>选择下一环节处理人员</td></tr></table>");
  var accountList = datas.accountlist;
  var groupList = datas.grouplist;
  var tmpHTML = "<tr class='common' style='height:32px;text-align:center;'><td class='prompt'>候选人</td><td colspan='3'>";
  for ( var groupName in groupList) {

    $
        .each(
            groupList[groupName],
            function(i, v) {
              var va = v.split(';')[0];
              var vn = v.split(';')[1];
              if (workFlowCandiCache.indexOf(va) <= -1) {
                tmpHTML += "<span style='margin-right:15px;display:flex;float:left;'><input type='checkbox' class='checkbox' name='candidatecheck' value='"
                    + va + "'/>&nbsp;&nbsp;" + vn + "</span>";
                workFlowCandiCache.push(va);
              }
            });
  }

  $
      .each(
          accountList,
          function(i, v) {
            var va = v.split(';')[0];
            var vn = v.split(';')[1];
            if (workFlowCandiCache.indexOf(va) <= -1) {
              tmpHTML += "<span style='margin-right:15px;display:flex;float:left;'><input type='checkbox' class='checkbox' name='candidatecheck' value='"
                  + va + "'/>&nbsp;&nbsp;" + vn + "</span>";
              workFlowCandiCache.push(va);
            }
          });

  userChooseTable.append($(tmpHTML));

  $("#chooseUser").append(userChooseTable);
  $("#chooseUser").append(
      $("<div style='text-align:center;'><button onclick='dosubmit(\""
          + datas.taskId + "\");' >确定</button></div>"));
  $('#chooseUser').window({
    width : 600,
    height : 'auto',
    modal : true,
    onBeforeClose : function() {
      if (workFlowIsSend)
        return true;
      return window.confirm('确定要关闭吗?该申请已经启动，如果您此时关闭，该条申请将出现在您的待办列表里，是否继续关闭?');
    },
    onClose : function() {
      workFlowIsSend = false;
      $('#chooseUser').empty();
      workFlowCandiCache = [];
      refreshGrid();
    }
  });
}
function dosubmit(taskId) {
  if (!taskId) {
    $parent.messager.alert("提示", "参数错误");
    return;
  }
  var candidatesCheck = $("input[name='candidatecheck']");
  var candidates = "";
  $.each(candidatesCheck, function(i, v) {
    if (v.checked) {
      candidates += (v.value + ",");
    }
  });
  if (candidates == "") {
    $parent.messager.alert("提示", "请选择要提交到的用户");
    return;
  } else {
    candidates = candidates.substring(0, candidates.length - 1);
  }
  $.ajax({
    type : "POST",
    url : ctx + "/task/submit.action",
    data : {
      'taskId' : taskId,
      'candidates' : candidates,
      'transition' : '通过',
      'comments' : ''
    },
    async : false,
    success : function(data) {
      if ('success' == data) {
        $parent.messager.alert('提示', '提交成功', 'info', function() {
          refreshGrid(grid);
          workFlowIsSend = true;
          $('#chooseUser').window('close');
        });

      } else {
        $parent.messager.alert('提示', '提交失败', 'error');
      }
    }
  });
}
// 关闭当前tab
function closeCurrentTab() {
  var tabs = $("#tabs");
  if (tabs)
    tabs = window.parent.$("#tabs");
  var tab = tabs.tabs('getSelected');
  var index = tabs.tabs('getTabIndex', tab);

  tabs.tabs('close', index);
}

function tabLoaded() {
  $parent("#tabs").tabs("loaded");
}

// 添加或者更新tab标签(支持url重新定向,用于加载数据较多的界面,关闭tab的loading mask界面)
function addTabLoading(url, title) {
  var $tabs = $parent("#tabs");
  var content = '<iframe name="mainframe" onload="tabLoaded()" src="'
      + url
      + '" frameBorder="0" border="0" scrolling="yes" style="overflow-y:scroll;width: 100%;height:100%;min-width:1149px;_height:600px;_width:1149px;min-height:600px;"/>';
  var haveTabs = $tabs.tabs('exists', title);
  var currentTab = $tabs.tabs('getSelected');
  var currentTabTitle = currentTab.panel('options').title;
  var targetTab;
  if (!haveTabs) {
    // 新增
    $tabs.tabs('add', {
      title : title,
      content : content,
      closable : true,
      cache : false
    })
    targetTab = $tabs.tabs('getTab', title);
  } else {
    targetTab = $tabs.tabs('getTab', title);
    // 更新
    $tabs.tabs('update', {
      tab : targetTab,
      options : {
        title : title,
        content : content,
        closable : true,
        cache : false
      }
    })
    // 聚焦
    $tabs.tabs('select', title);
  }
  if(!targetTab.sourceTitle){
    targetTab.sourceTitle = currentTabTitle;
  }
  $tabs.tabs("loading");
}

/** 
 * 使用方法: 
 * 开启:MaskUtil.mask(); 
 * 关闭:MaskUtil.unmask();
 *
 * MaskUtil.mask('其它提示文字...'); 
 */  
var MaskUtil = (function(){
    var $mask,$maskMsg;
    var defMsg = '正在加载数据，请稍候...';  
      
    function init(){
        if(!$mask){
            $mask = $("<div class=\"datagrid-mask mymask\"></div>").appendTo("body");  
        }  
        if(!$maskMsg){
            $maskMsg = $("<div class=\"datagrid-mask-msg mymask\">"+defMsg+"</div>")  
                .appendTo("body").css({'font-size':'12px'});  
        }  
        $mask.css({width:"100%",height:$(document).height()});  
        var scrollTop = $(document.body).scrollTop();  
        $maskMsg.css({
            left:($(document.body).outerWidth(true)-190)/2  
            ,top:(($(window).height()-45)/2)+scrollTop  
        });   
    }
      
    return {
        mask:function(msg){
            init();
            $mask.show();  
            $maskMsg.html(msg||defMsg).show();  
        }
        ,unmask:function(){  
            $mask.hide();  
            $maskMsg.hide();  
        }
    }
}());