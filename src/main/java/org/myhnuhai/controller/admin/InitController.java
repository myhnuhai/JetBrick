package org.myhnuhai.controller.admin;

import javax.servlet.http.HttpSession;

import org.myhnuhai.service.admin.IInitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 初始化数据库控制器
 * @author myhnuhai
 *
 */
@Controller
@RequestMapping("/initController")
public class InitController {

	@Autowired
	private IInitService initService;

	/**
	 * 初始化数据库后转向到首页
	 * 
	 * @return
	 */
	@RequestMapping("/init.jetx")
	public String init(HttpSession session) {
		if (session != null) {
			session.invalidate();
		}
		initService.init();
		return "redirect:/";
	}

}
