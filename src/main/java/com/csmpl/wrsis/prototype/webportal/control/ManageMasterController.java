package com.csmpl.wrsis.prototype.webportal.control;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.csmpl.adminconsole.webportal.controller.WrsisPortalAbstractController;

@Controller
public class ManageMasterController extends WrsisPortalAbstractController {

	@RequestMapping(value = { "/adduser" })
	public String adduser() {

		return "adduser";
	}

	@RequestMapping(value = { "/viewuser" })
	public String viewuser() {

		return "viewuser";
	}

	@RequestMapping(value = { "/edituser" })
	public String edituser() {

		return "edituser";
	}

	@RequestMapping(value = { "/adddesignation" })
	public String adddesignation() {

		return "adddesignation";
	}

	@RequestMapping(value = { "/viewdesignation" })
	public String viewdesignation() {

		return "viewdesignation";
	}

	@RequestMapping(value = { "/editdesignation" })
	public String editdesignation() {

		return "editdesignation";
	}

	@RequestMapping(value = { "/viewPublishedRecommendation" })
	public String viewPublishedRecommendation() {

		return "viewPublishedRecommendation";
	}

	@RequestMapping(value = { "/addApprovalAuthority" })
	public String addApprovalAuthority() {

		return "addApprovalAuthority";
	}

	@RequestMapping(value = { "/addTimeLimit" })
	public String addTimeLimit() {

		return "addTimeLimit";
	}

	@RequestMapping(value = { "/editTimeLimit" })
	public String editTimeLimit() {

		return "editTimeLimit";
	}

	@RequestMapping(value = { "/viewTimeLimit" })
	public String viewTimeLimit() {

		return "viewTimeLimit";
	}

	@RequestMapping(value = { "/changePassword" })
	public String changePassword() {

		return "changePassword";
	}

}
