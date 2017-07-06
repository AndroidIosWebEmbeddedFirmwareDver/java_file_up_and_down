package com.lys.test.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.lys.power.action.PowerAction;
import com.lys.sys.bo.Msg;

@Scope(value = "prototype")
@Controller("TestAction")
@RequestMapping(value="test")
public class TestAction extends PowerAction{
	/***
	 * 返回一个页面
	 * @return
	 */
	@RequestMapping(value="test1",method = RequestMethod.GET)
	public String test1(){ 
		model.addAttribute("mgs", new Msg(1).d("kesSSy",new ArrayList<>()).ok());
		return "test/test1";
	}
	/**
	 * 返回字符串
	 * @return
	 */
	@RequestMapping(value="test2",method = RequestMethod.GET)
	@ResponseBody
	public String test2(){ 
		return new Msg(1).d("key",new ArrayList<>()).ok();
	}
	/***
	 * 返回页面 + 存储request中的参数 ，类似 model，，，最好不使用这个方式
	 * @return
	 */
	@RequestMapping("test3") 
	public ModelAndView test3() {
		ModelAndView mav = new ModelAndView("test/test1");
//		mav.addObject("account", "account -1");
		mav.addObject("mgs", new Msg(1).d("key",new ArrayList<>()).ok());
		return mav;
	}
	/***
	 * 不返回参数， 直接通过 response 给 print出去
	 */
	@RequestMapping(value="test6" )
	public void test6(){ 
		 Map<String, String> map = new HashMap<String, String>(); 
	        map.put("key1", "valu\ne-1"); 
	        map.put("key2", "value-2"); 
	        map.put("key3", "value-3"); 
		this.createAjax(response,new Msg(1).d("map",map).ok());
		//该去封装了 
	}
	
}	 
