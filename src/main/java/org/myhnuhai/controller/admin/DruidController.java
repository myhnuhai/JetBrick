package org.myhnuhai.controller.admin;

import org.myhnuhai.controller.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 数据源控制器
 * 
 * @author myhnuhai
 * 
 */
@Controller
@RequestMapping("/druidController")
public class DruidController extends BaseController {

	/**
	 * 转向到数据源监控页面
	 * 
	 * @return
	 */
	@RequestMapping("/druid.html")
	public String druid() {
		return "redirect:/druid/index.html";
	}

}
