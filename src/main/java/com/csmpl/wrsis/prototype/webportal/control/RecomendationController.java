package com.csmpl.wrsis.prototype.webportal.control;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.csmpl.adminconsole.webportal.controller.WrsisPortalAbstractController;

@Controller
public class RecomendationController extends WrsisPortalAbstractController {

	@RequestMapping(value = { "/viewmonitorrecommendationsdetails" })
	public String viewmonitorrecommendationsdetails() {

		return "viewmonitorrecommendationsdetails";
	}

	@RequestMapping(value = { "/viewrecommendationsdetails" })
	public String viewrecommendationsdetails() {

		return "viewrecommendationsdetails";
	}

	@RequestMapping(value = { "/zonemonitorrecommendations" })
	public String zonemonitorrecommendations() {

		return "zonemonitorrecommendations";
	}

}
