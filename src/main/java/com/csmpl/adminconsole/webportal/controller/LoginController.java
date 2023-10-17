package com.csmpl.adminconsole.webportal.controller;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.csmpl.adminconsole.webportal.bean.LoginReqDto;
import com.csmpl.adminconsole.webportal.captcha.AesEncryption;
import com.csmpl.adminconsole.webportal.config.MD5PasswordEncoder;
import com.csmpl.adminconsole.webportal.entity.AdminRole;
import com.csmpl.adminconsole.webportal.entity.IpTrack;
import com.csmpl.adminconsole.webportal.repository.UserRepository;
import com.csmpl.adminconsole.webportal.service.DashboardService;
import com.csmpl.adminconsole.webportal.service.LoginService;
import com.csmpl.adminconsole.webportal.service.ManagePrimaryLinkService;
import com.csmpl.adminconsole.webportal.util.WrsisPortalConstant;
import com.csmpl.wrsis.webportal.util.DateUtil;

import nl.bitwalker.useragentutils.UserAgent;

@Controller
public class LoginController extends WrsisPortalAbstractController {

	private static final Logger LOG = LoggerFactory.getLogger(LoginController.class);

	@Autowired
	private LoginService loginService;

	@Autowired
	private ManagePrimaryLinkService primaryLinkservice;

	@GetMapping(value = { "/", "/site" })
	public String website(Model model) {

		model.addAttribute(WrsisPortalConstant.GIS_URL, GIS_URL);
		model.addAttribute(WrsisPortalConstant.WORKSPACE, GIS_WORKSPACE);

		return "login";
	}

	@GetMapping(value = { "/login" })
	public String login(HttpSession session, Model model) {

		model.addAttribute(WrsisPortalConstant.GIS_URL, GIS_URL);
		model.addAttribute(WrsisPortalConstant.WORKSPACE, GIS_WORKSPACE);

		return "login";
	}

	@GetMapping(value = { "/logout" })
	public String logout(HttpSession session, Model model, HttpServletRequest request) {

		try {

			Object obj = getAttibuteFromSession(request, "LOGIN_INFO");
			if (obj != null) {
				IpTrack ipinfo = (IpTrack) obj;
				ipinfo.setLogoutime(new Timestamp(new Date().getTime()));
				loginService.saveUserTrackInfo(ipinfo);
			}

			session.invalidate();

		} catch (Exception e) {
			LOG.error("LoginController::logout():" + e);
			session.invalidate();
		}
		return "redirect:/login";
	}

	//
	@GetMapping(value = { "error" })
	public String errorPage() {

		return "errorPage";
	}
	@GetMapping(value = { "aboutus" })
	public String aboutusPage() {

		return "aboutus";
	}
	
	
	/*
	 * This  method will render the page to GIS survey map .
	 * 
	 * 
	 * @author Shaktimaan Panda
	 * 
	 * @version 1.0
	 * 
	 * @since 08-01-2021
	 */
	
	
	@GetMapping(value = { "/surveymap" })
	public String gisMapPage(Model model) {

		model.addAttribute(WrsisPortalConstant.GIS_URL, GIS_URL);
		model.addAttribute(WrsisPortalConstant.WORKSPACE, GIS_WORKSPACE);
		
		return "surveymap";
	}
	
	@GetMapping(value = { "privacy" })
	public String privacyPage() {

		return "privacy";
	}

	@Autowired
	UserRepository userRepository;

	@SuppressWarnings("unused")
	@PostMapping(value = "/home")
	public String userHome(@ModelAttribute LoginReqDto loginReqDto, RedirectAttributes redircts,
			HttpServletRequest request, Model model) {
		String view = "";
		HttpSession session = request.getSession(false);
		try {

			if (loginReqDto.getUsername() == null || loginReqDto.getUsername().isEmpty()) {
				redircts.addFlashAttribute(WrsisPortalConstant.MESSAGE, "User ID should not left blank");
				return "redirect:login";
			}

			if (loginReqDto.getPassword() == null || loginReqDto.getPassword().isEmpty()) {
				redircts.addFlashAttribute(WrsisPortalConstant.MESSAGE, "Password should not left blank");
				return "redirect:login";
			}

			// captcha validation
			String securityAns = request.getParameter("securityAns");
			Object sObj = session.getAttribute("captcha_key_name");

			if (securityAns == null || securityAns.isEmpty()) {
				redircts.addFlashAttribute(WrsisPortalConstant.MESSAGE, "Please enter appropriate answer.");
				return "redirect:login";
			}
			if (sObj == null) {
				redircts.addFlashAttribute(WrsisPortalConstant.MESSAGE, "Try after sometime");
				return "redirect:login";
			}

			
			String sessionAns = AesEncryption.decrypt(sObj.toString());
			
			//
			if (!sessionAns.equals(securityAns)) {
				redircts.addFlashAttribute(WrsisPortalConstant.MESSAGE, "Please enter appropriate answer.");
				return "redirect:login";
			}

			String ipaddress = null;
			ipaddress = loginReqDto.getIpaddress();
			loginReqDto.setIpaddress(ipaddress);

			UserAgent userAgent = UserAgent.parseUserAgentString(request.getHeader("User-Agent"));
			loginReqDto.setBrowserName(userAgent.getBrowser().getName());
			loginReqDto.setBrowserVersion(userAgent.getBrowserVersion().getVersion());
			loginReqDto.setOsName(userAgent.getOperatingSystem().getName());
			loginReqDto.setDeviceType(userAgent.getOperatingSystem().getDeviceType().getName());
			loginReqDto.setManufactur(userAgent.getOperatingSystem().getManufacturer().getName());

			IpTrack ipinfo = new IpTrack();
			ipinfo.setIpaddress(loginReqDto.getIpaddress());
			ipinfo.setCreateOn(new Timestamp(new Date().getTime()));
			ipinfo.setLogintime(new Timestamp(new Date().getTime()));
			ipinfo.setBrowserName(loginReqDto.getBrowserName());
			ipinfo.setBrowserVersion(loginReqDto.getBrowserVersion());
			ipinfo.setOsName(loginReqDto.getOsName());
			ipinfo.setVchDeviceType(loginReqDto.getDeviceType());
			ipinfo.setDeviceBrand(loginReqDto.getManufactur());
			ipinfo.setLoginSuccess('N');

			String decodeUserId =new String(Base64.getDecoder().decode(loginReqDto.getUsername().getBytes()));
			com.csmpl.adminconsole.webportal.entity.User userDetails = loginService
					.findByUserName(decodeUserId.toLowerCase());

			if (userDetails == null) {
				loginService.saveUserTrackInfo(ipinfo);
				redircts.addFlashAttribute(WrsisPortalConstant.MESSAGE, "You have entered an invalid username or password.");
				return "redirect:login";

			}
			if (userDetails.getLoginFailAttempt() == 'Y') {
				redircts.addFlashAttribute(WrsisPortalConstant.MESSAGE,
						"Your account has already been blocked! Please contact your admin.");
				return "redirect:login";
			} else {

				AdminRole role = loginService.getRolByUserId(userDetails.getUserId());
				// UserType userType =
				// loginService.getUserTypeById(userDetails.getMemUserTypeId());//comment on
				// 05/11/2019
				String existPassword = userDetails.getPassword();
				String enteredPassword = new String(Base64.getDecoder().decode(loginReqDto.getPassword().getBytes()));

				if (!new MD5PasswordEncoder().matches(enteredPassword, existPassword)) {

					ipinfo.setCreatedBy(userDetails.getUserId());
					ipinfo.setFailReason("Password mismatch.");
					ipinfo = loginService.saveUserTrackInfo(ipinfo);
					Thread.sleep(500);
					if (ipinfo != null) {
						int failAttemptCount = loginService.countFailAttempt(userDetails.getUserId());
						if (failAttemptCount >= 5) {

							userDetails.setLoginFailAttempt('Y');
							userRepository.save(userDetails);
							redircts.addFlashAttribute(WrsisPortalConstant.MESSAGE,
									"Your account has been temporarily locked! Please contact your admin.");
							return "redirect:login";

						}

						redircts.addFlashAttribute(WrsisPortalConstant.MESSAGE,
								"You have entered an invalid User ID or Password.<br>You have made "
										+ ((failAttemptCount == 0) ? 1 : failAttemptCount)
										+ " unsuccessful attempt(s) out of 5 allowed attempts.");
						return "redirect:/login";
					}
				}

				if (userDetails.getFirstLogin() == 'Y' || userDetails.getFirstLogin() == 'y') {
					addAttibuteInSession(request, userDetails.getUserName(), WrsisPortalConstant.USER_NAME);
					return "redirect:firstTimeLogin";

				} else {
					ipinfo.setCreatedBy(userDetails.getUserId());
					ipinfo.setLoginSuccess('Y');
					IpTrack loginInfo = loginService.saveUserTrackInfo(ipinfo);
					addAttibuteInSession(request, loginInfo, "LOGIN_INFO");

					Map<com.csmpl.adminconsole.webportal.entity.GlobalLink, List<com.csmpl.adminconsole.webportal.entity.PrimaryLink>> leftMenu1 = primaryLinkservice
							.findUserLeftMenuByUserId(userDetails.getUserId());

					List<com.csmpl.adminconsole.webportal.entity.PrimaryLink> allPKs = leftMenu1.values().stream()
							.flatMap(Collection::stream).collect(Collectors.toList());
					List<String> userPermissions = new ArrayList<>();
					allPKs.forEach(e -> userPermissions.add(e.getFileName()));
					addAttibuteInSession(request, userPermissions, WrsisPortalConstant.USER_PERMISSIONS);

					addAttibuteInSession(request, userDetails, WrsisPortalConstant.USER);
					addAttibuteInSession(request, userDetails.getUserId(), WrsisPortalConstant.USER_ID);
					addAttibuteInSession(request, userDetails.getUserName(), WrsisPortalConstant.USER_NAME);
					addAttibuteInSession(request, userDetails.getIntLevelDetailId(), WrsisPortalConstant.LABEL_ID);
					addAttibuteInSession(request, userDetails.getFullName(), WrsisPortalConstant.FULLNAME);
					addAttibuteInSession(request, leftMenu1, WrsisPortalConstant.LEFT_MENU_PERMISSION);
					addAttibuteInSession(request, 0, WrsisPortalConstant.PRIMARY_LNK_ACTION);
					addAttibuteInSession(request, role.getAliasName(), WrsisPortalConstant.DESIGNATION);
					addAttibuteInSession(request, role, WrsisPortalConstant.ROLE);
					addAttibuteInSession(request, role.getRoleId(), WrsisPortalConstant.ROLE_ID);
					view = "redirect:/Home";
				}

			}

		} catch (Exception e1) {
			view = "redirect:/login";
			redircts.addFlashAttribute(WrsisPortalConstant.MESSAGE, "Error Occured !! Try after some time");
			LOG.error("LoginController::userHome():" + e1.getMessage());
		}
		return view;
	}

	@Autowired
	DashboardService dashboardService;

	@GetMapping(value = "Home") // welcome
	public String welcomePage1(Model model, HttpServletRequest request, HttpSession session) {

		try {

			
			List<String> yearlist = DateUtil.fetchYearList();
			model.addAttribute("year", yearlist);
			model.addAttribute("seasons", dashboardService.getActiveSeasonList());
		} catch (Exception e) {
			LOG.error("LoginController::welcomePage1():" + e);
		}
		return "homeDashBoard2"; // welcome
	}

	@GetMapping(value = "Home2") // welcome
	public String welcomePage2(Model model, HttpServletRequest request, HttpSession session) {

		try {

			JSONObject response = new JSONObject();
			Integer userId = (Integer) session.getAttribute(WrsisPortalConstant.USER_ID);
			response = dashboardService.getDashboardContent(userId);
			model.addAttribute("DashBoardJson", new String(Base64.getEncoder().encode(response.toString().getBytes())));

		} catch (Exception e) {
			LOG.error("LoginController::welcomePage2():" + e);
		}
		return "homeDashBoard"; // welcome
	}

	@Value("${map.forecasting.url}")
	private String GIS_URL;

	@Value("${gis.map.workspace}")
	private String GIS_WORKSPACE;

	@RequestMapping(value = "/viewForecastingMap", method = RequestMethod.GET)
	public String externalPageAccess(Model model) {
		model.addAttribute(WrsisPortalConstant.GIS_URL, GIS_URL);
		model.addAttribute(WrsisPortalConstant.WORKSPACE, GIS_WORKSPACE);

		return "externalPageAccess";
	}

	@RequestMapping(value = "/externalPageAccessValidityCheck", method = RequestMethod.GET)
	public @ResponseBody String externalPageAccessValidityCheck(Model model, HttpSession session) throws JSONException {
		JSONObject json = new JSONObject();
		Integer userId = (Integer) session.getAttribute(WrsisPortalConstant.USER_ID);
		if (userId == null) {
			json.put("Validity", false);
		} else {
			json.put("Validity", true);
		}
		return json.toString();

	}


	public String getClientIpAddr(HttpServletRequest request) {
		String ip = request.getHeader("X-Forwarded-For");
		if (ip == null || ip.length() == 0 || WrsisPortalConstant.UNKNOWN.equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || WrsisPortalConstant.UNKNOWN.equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || WrsisPortalConstant.UNKNOWN.equalsIgnoreCase(ip)) {
			ip = request.getHeader("HTTP_CLIENT_IP");
		}
		if (ip == null || ip.length() == 0 || WrsisPortalConstant.UNKNOWN.equalsIgnoreCase(ip)) {
			ip = request.getHeader("HTTP_X_FORWARDED_FOR");
		}
		if (ip == null || ip.length() == 0 || WrsisPortalConstant.UNKNOWN.equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		return ip;
	}

	@GetMapping("/homeDashBoard")
	public String homeDashBoard() {
		return "homeDashBoard";
	}

	@GetMapping("/homeDashBoardUser")
	public String homeDashBoardUser() {

		return "homeDashBoardUser";
	}

}
