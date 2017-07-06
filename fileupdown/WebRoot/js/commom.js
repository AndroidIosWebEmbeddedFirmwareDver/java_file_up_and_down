//去掉两端空格
String.prototype.trim=function() {
    return this.replace(/(^\s*)|(\s*$)/g,'');
}

/***
 * 自定义加载事件
 */
function lys_load(url,params,objhtml){
	newMask();
	$.ajax({
		type : "POST",
		url : url,
		data : params+"&r="+new Date().getTime(),
		success : function(data, textStatus, jqXHR) {
			delMask();
			/**如果异常*/
			objhtml.html(data.m);
		},
		dataType : "json",
		error : function(XMLHttpRequest, textStatus, errorThrown) {
			delMask();
			objhtml.html(XMLHttpRequest.responseText);
		}
	});
}

/**
 * img onerror事件
 * @param imgurl
 * @return
 */
function nofind(imgurl){
	var img=event.srcElement;
	img.src=imgurl;
	img.onerror=null; //控制不要一直跳动
}
//初始化表单验证结果 --删除表单的验证信息
function initValidForm(obj){
	obj.find("span.Validform_checktip").empty().removeClass("Validform_wrong Validform_right");//移除表单中验证信息
	obj.find("select,input,textarea").removeClass("Validform_error");//移除表单中验证信息
}
//验证div中 是否 满足 验证条件
//参数：某个表单对象、验证成功后执行的函数
function checkFormValid(formObj,fun){
	var childSaveBtn=formObj.find("#child_SaveBtn");//查找form下的id="child_SaveBtn";
	if(childSaveBtn==null||childSaveBtn.length==0){
		formObj.append('<input type="button" id="child_SaveBtn" style="display:none;"/>');//为form元素结尾（仍然在内部）插入按钮
		childSaveBtn=formObj.find("#child_SaveBtn");
	}
	formObj.Validform({
		btnSubmit:"#child_SaveBtn",
		tiptype:function(msg,o,cssctl){
			if(o.obj.parent().next(".Validform_checktip").length!=0){
				var objtip=o.obj.parent().next(".Validform_checktip");
				cssctl(objtip,o.type);
				objtip.text(msg);
			}else{
				o.obj.parent().append("<span class='Validform_checktip' />");
				o.obj.parent().next().find(".Validform_checktip").remove();
				var objtip=o.obj.next(".Validform_checktip");
				cssctl(objtip,o.type);
				objtip.text(msg);
			}
			//msg：提示信息;        
			//o:{obj:*,type:*,curform:*}, obj指向的是当前验证的表单元素（或表单对象），type指示提示的状态，值为1、2、3、4， 1：正在检测/提交数据，2：通过验证，3：验证失败，4：提示ignore状态, curform为当前form对象;
			
			//cssctl:内置的提示信息样式控制函数，该函数需传入两个参数：显示提示信息的对象 和 当前提示的状态（既形参o中的type）; 
			/*if(!o.obj.is("form")){//验证表单元素时o.obj为该表单元素，全部验证通过提交表单时o.obj为该表单对象;        
				var oobjtip=o.obj.siblings(".Validform_checktip");             
				cssctl(objtip,o.type);            
				objtip.text(msg);         
				}  */     
			},
		showAllError:false,//true：提交表单时所有错误提示信息都会显示，false：一碰到验证不通过的就停止检测后面的元素，只显示该元素的错误信息;
		beforeSubmit:function(curform){
			//在验证成功后，表单提交前执行的函数，curform参数是当前表单对象。
			//这里明确return false的话表单将不会提交;	
			fun();
			return false;
		}
	});
	childSaveBtn.click();
}


//验证div中 是否 满足 验证条件
//参数：某个div对象、验证成功后执行的函数
//传入的div对象不能form标签
//此函数存在bug。尽量不使用
function checkDivValid(divObj,fun){
	//获取当前父类
	var formObj=divObj.find("form");//查找form
	if(formObj==null||formObj.length==0){ 
		divObj.wrapInner("<form></form>");//将获取的父类下的所有元素用form包裹
		formObj=divObj.find("form");//查找form
		
	}
	var childSaveBtn=formObj.find("#child_SaveBtn");//查找form下的id="child_SaveBtn";
	if(childSaveBtn==null||childSaveBtn.length==0){
		formObj.append('<input type="button" id="child_SaveBtn" style="display:none;"/>');//为form元素结尾（仍然在内部）插入按钮
		childSaveBtn=formObj.find("#child_SaveBtn");
	}
	formObj.Validform({
		btnSubmit:"#child_SaveBtn",
		tiptype:3,
		showAllError:false,//true：提交表单时所有错误提示信息都会显示，false：一碰到验证不通过的就停止检测后面的元素，只显示该元素的错误信息;
		beforeSubmit:function(curform){
			/*if(divObj.find('form').length>0){
				divObj.find('form').children().unwrap();//此处 的清除2次点击后  出现 了BUG。  所以， 暂时不清除 form
			}*/
			fun();
			return false;
		}
	});
	childSaveBtn.click();
}


//form == 表单jquery对象
//data == json数据对象
function formload(form,data){  
	for(var name in data){
		var val=data[name];
		var rr=$("input[name=\""+name+"\"][type=radio], input[name=\""+name+"\"][type=checkbox]",form);
		$.fn.prop?rr.prop("checked",false):rr.attr("checked",false);
		rr.each(function(){
			var f=$(this);
			var valarray =String(val).split(",");     
			if(in_array(f.val(),valarray)){
				$.fn.prop?f.prop("checked",true):f.attr("checked",true);
			}
			/*if(f.val()==String(val)){
				$.fn.prop?f.prop("checked",true):f.attr("checked",true);
			}*/
		});
		if(!rr.length){
			$("input[name=\""+name+"\"]",form).val(val);
			$("textarea[name=\""+name+"\"]",form).val(val);
			simulateClick($("select[name=\""+name+"\"]",form).val(val)[0]);
		}
	}
}
/***
 * 判断 一个字符串是否 在 字符串组
 * @param search 一个字符串
 * @param array 字符串数组
 * @returns
 */
function in_array(search,array){
    for(var i in array){
        if(array[i]==search){
            return true;
        }
    }
    return false;
}

//取消所有被选择节点
//参数： ztree对象
//
function cancelSelectedNode(treeObj){
	var nodes= treeObj.getCheckedNodes(true);
	for (var i = 0; i < nodes.length; i++) {
		treeObj.checkNode(nodes[i], false , false);
	}
}
//设置节点的禁用或启用状态
//参数： ztree对象
//节点集合
//true=是禁用  
function setChkDisableds(treeObj,nodes,flag){
	for (var i=0, l=nodes.length; i < l; i++) {
		treeObj.setChkDisabled(nodes[i], flag);
	}
}
//展开该节点所有父节点
function expandNodeps(treeObj,node,expandFlag, sonSign, focus){
	var parentnode=node.getParentNode();
    if (parentnode) {
		treeObj.expandNode(parentnode,expandFlag, sonSign, focus);
		expandNodeps(treeObj,parentnode,expandFlag, sonSign, focus);
    } 
}
//获取zTree节点下面的所有子节点
//参数： 节点对象 、 以逗号隔开的字符串
/* var str ='' ;
   str = getAllChildrenNodes(treeNode,str);
   alert(str); //所有叶子节点ID
 */
function getAllChildrenNodes(treeNode,result){
      if (treeNode.isParent) {
        var childrenNodes = treeNode.children;
        if (childrenNodes) {
            for (var i = 0; i < childrenNodes.length; i++) {
                result += ',' + childrenNodes[i].treeid;
                result = getAllChildrenNodes(childrenNodes[i], result);
            }
        }
    }
    return result;
}
//JS动态设置select 后 触发 onchange事件
//Chrome , Firfox 不支持fireEvent的方法
//可以使用dispatchEvent的方法替代
function simulateClick(el) {
	if(el){
		  var evt;
		  if (document.createEvent) { // DOM Level 2 standard
		    evt = document.createEvent("MouseEvents");
		    evt.initMouseEvent("change", true, true, window,
		      0, 0, 0, 0, 0, false, false, false, false, 0, null);
		    el.dispatchEvent(evt);
		  } else if (el.fireEvent) { // IE
		    el.fireEvent('onchange');
		  }
	}
}

//创建mask遮罩层
function newMask(){
		var newMask = document.createElement("div");
		newMask.id = "newMas_kId"; 
		_scrollWidth = Math.max(document.body.scrollWidth, document.documentElement.scrollWidth);
		_scrollHeight = Math.max(document.body.scrollHeight, document.documentElement.scrollHeight);
		newMask.innerHTML = "<div style='width: 80px; height: 80px;position: fixed;top:200px;left:50%;margin-left: -60px;z-index: 10050; '><img src='images/logo/loading.gif' width='100%'></div>" +
				"<div style='background:#666;filter:alpha(opacity=40);opacity:0.40;width:100%;height:100%;top:0px;left:0px;position:fixed;z-index:10050;'></div>";
		document.body.appendChild(newMask); 
	}
//删除mask遮罩层
function delMask(){
	$("div[id=newMas_kId]").remove();
}

