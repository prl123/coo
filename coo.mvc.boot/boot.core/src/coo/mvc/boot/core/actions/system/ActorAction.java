package coo.mvc.boot.core.actions.system;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import coo.core.message.MessageSource;
import coo.core.security.annotations.Auth;
import coo.core.security.permission.AdminPermission;
import coo.mvc.boot.core.entity.Actor;
import coo.mvc.boot.core.service.SecurityService;
import coo.mvc.util.DwzResultUtils;

/**
 * 职务管理。
 */
@Controller
@RequestMapping("/system")
@Auth(AdminPermission.CODE)
public class ActorAction {
	@Resource
	private SecurityService securityService;
	@Resource
	private MessageSource messageSource;

	/**
	 * 新增职务。
	 * 
	 * @param userId
	 *            关联用户ID
	 * @param model
	 *            数据模型
	 */
	@RequestMapping("actor-add")
	public void add(String userId, Model model) {
		Actor actor = new Actor();
		actor.setUser(securityService.getUser(userId));
		model.addAttribute(actor);
		model.addAttribute("rootOrgan", securityService.getCurrentUser()
				.getSettings().getDefaultActor().getOrgan());
		model.addAttribute("roles", securityService.getAllRole());
	}

	/**
	 * 保存职务。
	 * 
	 * @param actor
	 *            职务
	 * @return 返回提示信息。
	 */
	@RequestMapping("actor-save")
	public ModelAndView save(Actor actor) {
		securityService.createActor(actor);
		return DwzResultUtils.close(
				messageSource.get("actor.add.success"), "user-edit");
	}

	/**
	 * 编辑职务。
	 * 
	 * @param actorId
	 *            职务ID
	 * @param model
	 *            数据模型
	 */
	@RequestMapping("actor-edit")
	public void edit(String actorId, Model model) {
		model.addAttribute(securityService.getActor(actorId));
		model.addAttribute("rootOrgan", securityService.getCurrentUser()
				.getSettings().getDefaultActor().getOrgan());
		model.addAttribute("roles", securityService.getAllRole());
	}

	/**
	 * 更新职务。
	 * 
	 * @param actor
	 *            职务
	 * @return 返回提示信息。
	 */
	@RequestMapping("actor-update")
	public ModelAndView update(Actor actor) {
		securityService.updateActor(actor);
		return DwzResultUtils.close(
				messageSource.get("actor.update.success"), "user-edit");
	}

	/**
	 * 删除职务。
	 * 
	 * @param actorId
	 *            职务ID
	 * @return 返回提示信息。
	 */
	@RequestMapping("actor-delete")
	public ModelAndView delete(String actorId) {
		securityService.deleteActor(actorId);
		return DwzResultUtils.refresh(
				messageSource.get("actor.delete.success"), "user-edit");
	}
}
