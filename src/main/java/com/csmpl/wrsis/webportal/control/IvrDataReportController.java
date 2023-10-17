package com.csmpl.wrsis.webportal.control;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.csmpl.adminconsole.webportal.controller.WrsisPortalAbstractController;
import com.csmpl.adminconsole.webportal.util.WrsisPortalConstant;
import com.csmpl.wrsis.webportal.bean.IvrDataReportBean;
import com.csmpl.wrsis.webportal.datatable.IvrSurveyDataTable;
import com.csmpl.wrsis.webportal.service.IVRDataService;

@Controller
public class IvrDataReportController extends WrsisPortalAbstractController {

	public static final Logger LOG = LoggerFactory.getLogger(IvrDataReportController.class);

	@Autowired
	IVRDataService ivrDataService;

	@RequestMapping(value = { "/viewIvrSurveyData" })
	public String viewIvrSurveyData(Model model) {

		try {
			List<IvrDataReportBean> ivrData = ivrDataService.getIvrData();
			model.addAttribute("ivrData", ivrData);
		} catch (Exception e) {
			LOG.error("IvrDataReportController::viewIvrSurveyData():" + e);
		}
		return "viewIvrSurveyData";
	}

	@ResponseBody
	@RequestMapping(value = "/getIvrSurveyData", method = RequestMethod.GET)
	public IvrSurveyDataTable getIvrSurveyData(HttpServletRequest request) {
		try {
			String start = request.getParameter(WrsisPortalConstant.START);
			String length = request.getParameter(WrsisPortalConstant.LENGTH);
		

			List<IvrDataReportBean> ivr = ivrDataService.viewIvrSurveydata(Integer.valueOf(start),
					Integer.valueOf(length));
			for (int i = 0; i < ivr.size(); i++) {
				IvrDataReportBean ivrBean = ivr.get(i);
				ivrBean.setSl((i + 1));
				ivr.add(ivrBean);
			}

			Integer count = ivrDataService.countIvrData();

			IvrSurveyDataTable u = new IvrSurveyDataTable();
			u.setData(ivr);
			u.setRecordsFiltered(count);
			u.setRecordsTotal(count);

			return u;
		} catch (Exception e) {
			LOG.error("IvrDataReportController::getIvrSurveyData():" + e);
			return null;
		}
	}

}
