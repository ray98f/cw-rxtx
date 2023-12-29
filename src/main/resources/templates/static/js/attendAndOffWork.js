var zaoCross=null;
$(function(){	
	$.ajax({
		url:ctx+"/driverAttendt/getZaoCwCrData.action",
		type:"post",
		dataType:"json",
		success:function(data){
			zaoCross=data;
		}
	});
	$(document).bind("contextmenu",function(e){
			return false;
	});
	$("#cancel").on("click", function(){
		var userId = $("#userId").val();
		var zby = $("#clsId").val();
		var lineId = $("#lineId").val();
		$.ajax({
			url:ctx+"/driverAttendt/checkExam.action",
			data:{userId:userId,zby:zby,lineId:lineId},
			type:"post",
			dataType:"json",
			success:function(data){
				if(data.flag){
					cancel();
				}else{
					$parent.messager.alert('提示',"每日一问未完成，无法退勤！");
					//alert("每日一问未完成，无法退勤！");
					return ;
				}
			}
		});
	});
	
	$("#cancelAttend").on("click", function(){
		cancelAttend();
	});
	$("#attend").on("click", function(){
		save();
	});	
	
	$("#cross").combobox({
		onSelect:function(record){
			if(zaoCross !=null){
				for(var i=0;i<zaoCross.length;i++){
					if(record.value==zaoCross[i].cwcrid){
						$("#attendTime").val(zaoCross[i].attenTime);
						break;
					}
				}
			}
		}
	});
	
});

function cancel(){
	var account=$("#account").val();
	var crDetailId=$("#crDetailId").val();
	$('#dataForm').form({
		url:ctx+"/driverAttendt/Offwork.action",
		onSubmit:function(){
			return $(this).form('validate');
		},
		success:function(data){
			var data = eval('('+data+')');
			$parent.messager.alert('提示',data.Message);
			if(data.ActionResult){
				$("#password").val("");
				$("#parent_win").window("close");
				 var tab_ = $parent("#tabs").tabs("getSelected");
			     tab_.find("iframe")[0].contentWindow.viewNow();
			     addTab(ctx+'/driverInfo/addDriverInfo.action?account='+account+"&crDetailId="+crDetailId,'新增司机报单');
			}
		}
	}).submit();
}

function cancelAttend(){
	$('#dataForm').form({
		url:ctx+"/driverAttendt/cancelAttend.action",
		onSubmit:function(){
			return $(this).form('validate');
		},
		success:function(data){
			var data = eval('('+data+')');
			$parent.messager.alert('提示',data.Message);
			if(data.ActionResult){
				$("#password").val("");
				$("#parent_win").window("close");
				 var tab_ = $parent("#tabs").tabs("getSelected");
			     tab_.find("iframe")[0].contentWindow.viewNow();
			}
		}
	}).submit();
}
/*
function save(){
	url:ctx+"/driverAttendt/attend.action",
	$('#dataForm').form({
		onSubmit:function(){
			return $(this).form('validate');
		},
		success:function(data){
			var data = eval('('+data+')');
			$parent.messager.alert('提示',data.Message);
			if(data.ActionResult){
				$("#password").val("");
				$("#parent_win").window("close");
				 var tab_ = $parent("#tabs").tabs("getSelected");
			     tab_.find("iframe")[0].contentWindow.viewNow();
			}
		}
	}).submit();
}*/
/*document.onkeydown = function () {
    if (window.event && window.event.keyCode == 13) {
        window.event.returnValue = false;
    }
}*/