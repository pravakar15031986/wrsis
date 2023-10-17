package com.csmpl.wrsis.webportal.control;

import java.util.Base64;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.csmpl.adminconsole.webportal.captcha.AesEncryption;
import com.csmpl.adminconsole.webportal.config.MD5PasswordEncoder;
import com.csmpl.adminconsole.webportal.controller.WrsisPortalAbstractController;
import com.csmpl.adminconsole.webportal.entity.User;
import com.csmpl.adminconsole.webportal.service.LoginService;
import com.csmpl.adminconsole.webportal.util.WrsisPortalConstant;
import com.csmpl.wrsis.webportal.bean.PasswordBean;
import com.csmpl.wrsis.webportal.util.Validation;

@Controller
public class FirstTimeLoginController extends WrsisPortalAbstractController {

	private static final Logger LOG = LoggerFactory.getLogger(FirstTimeLoginController.class);

	@Autowired
	private LoginService loginService;

	@RequestMapping("firstTimeLogin")
	public String firstTimeLogin() {

		return "firstTimeLogin";
	}

	@PostMapping(value = { "/firstTimeChangePassword" })
	public String firstTimeChangePassword(Model model, @ModelAttribute("loginObj") PasswordBean loginReqDto,
			HttpServletRequest request, RedirectAttributes redirect) {
		HttpSession session = request.getSession();
		try {

			// captcha validation
			String securityAns = request.getParameter("securityAns");
			Object sObj = session.getAttribute("captcha_key_name");

			if (securityAns == null || securityAns.isEmpty()) {
				redirect.addFlashAttribute(WrsisPortalConstant.MESSAGE, "Please enter appropriate answer.");
				return "redirect:firstTimeLogin";
			}
			if (sObj == null) {
				redirect.addFlashAttribute(WrsisPortalConstant.MESSAGE, "Try after sometime");
				return "redirect:firstTimeLogin";
			}

			
			
			String sessionAns = AesEncryption.decrypt(sObj.toString());
			
			if (!sessionAns.equals(securityAns)) {
				redirect.addFlashAttribute(WrsisPortalConstant.MESSAGE, "Please enter appropriate answer.");
				return "redirect:firstTimeLogin";
			}

			// validation starts
			if (loginReqDto.getCurrentPassword() == null || loginReqDto.getCurrentPassword().isEmpty()) {
				redirect.addFlashAttribute(WrsisPortalConstant.ERROR_MSG, "Current password should not empty.");
				return "redirect:firstTimeLogin";
			}

			String currenPassword = new String(Base64.getDecoder().decode(loginReqDto.getCurrentPassword().getBytes()));

			if (loginReqDto.getCurrentPassword().length() != 0) {
				boolean isValidate = Validation.validateFirstBlankSpace(currenPassword);
				if (!isValidate) {
					redirect.addFlashAttribute(WrsisPortalConstant.ERROR_MSG, "Current password Should not Start with a Blank space.");
					return "redirect:firstTimeLogin";
				}
			}

			if (loginReqDto.getCurrentPassword().length() != 0) {
				boolean isValidate = Validation.validateLastBlankSpace(currenPassword);
				if (!isValidate) {
					redirect.addFlashAttribute(WrsisPortalConstant.ERROR_MSG, "Current password Should not End with a Blank space.");
					return "redirect:firstTimeLogin";
				}
			}
			if (loginReqDto.getCurrentPassword().length() != 0) {
				boolean isValidate = Validation.validateConsecutiveBlankSpacesInString(currenPassword);
				if (!isValidate) {
					redirect.addFlashAttribute(WrsisPortalConstant.ERROR_MSG,
							"Current password Should not Contain Consecutive Blank space.");
					return "redirect:firstTimeLogin";
				}
			}
			if (loginReqDto.getNewPassword() == null || loginReqDto.getNewPassword().isEmpty()) {
				redirect.addFlashAttribute(WrsisPortalConstant.ERROR_MSG, "New password should not empty.");
				return "redirect:firstTimeLogin";
			}
			String newPassword = new String(Base64.getDecoder().decode(loginReqDto.getNewPassword().getBytes()));

			if (loginReqDto.getNewPassword().length() != 0) {
				boolean isValidate = Validation.validateFirstBlankSpace(newPassword);
				if (!isValidate) {
					redirect.addFlashAttribute(WrsisPortalConstant.ERROR_MSG, "New password Should not Start with a Blank space.");
					return "redirect:firstTimeLogin";
				}
			}

			if (loginReqDto.getNewPassword().length() != 0) {
				boolean isValidate = Validation.validateLastBlankSpace(newPassword);
				if (!isValidate) {
					redirect.addFlashAttribute(WrsisPortalConstant.ERROR_MSG, "New password Should not End with a Blank space.");
					return "redirect:firstTimeLogin";
				}
			}
			if (loginReqDto.getNewPassword().length() != 0) {
				boolean isValidate = Validation.validateConsecutiveBlankSpacesInString(newPassword);
				if (!isValidate) {
					redirect.addFlashAttribute(WrsisPortalConstant.ERROR_MSG, "New password Should not Contain Consecutive Blank space.");
					return "redirect:firstTimeLogin";
				}
			}
			if (loginReqDto.getConfirmPassword() == null || loginReqDto.getConfirmPassword().isEmpty()) {
				redirect.addFlashAttribute(WrsisPortalConstant.ERROR_MSG, "Confirm password should not empty.");
				return "redirect:firstTimeLogin";
			}

			String confirmPassword = new String(
					Base64.getDecoder().decode(loginReqDto.getConfirmPassword().getBytes()));

			if (loginReqDto.getConfirmPassword().length() != 0) {
				boolean isValidate = Validation.validateFirstBlankSpace(confirmPassword);
				if (!isValidate) {
					redirect.addFlashAttribute(WrsisPortalConstant.ERROR_MSG, "Confirm password Should not Start with a Blank space.");
					return "redirect:firstTimeLogin";
				}
			}

			if (loginReqDto.getConfirmPassword().length() != 0) {
				boolean isValidate = Validation.validateLastBlankSpace(confirmPassword);
				if (!isValidate) {
					redirect.addFlashAttribute(WrsisPortalConstant.ERROR_MSG, "Confirm password Should not End with a Blank space.");
					return "redirect:firstTimeLogin";
				}
			}
			if (loginReqDto.getConfirmPassword().length() != 0) {
				boolean isValidate = Validation.validateConsecutiveBlankSpacesInString(confirmPassword);
				if (!isValidate) {
					redirect.addFlashAttribute(WrsisPortalConstant.ERROR_MSG,
							"Confirm password Should not Contain Consecutive Blank space.");
					return "redirect:firstTimeLogin";
				}
			}
			if (currenPassword.equals(newPassword)) {
				redirect.addFlashAttribute(WrsisPortalConstant.ERROR_MSG, "New password should not same as Current password.");
				return "redirect:firstTimeLogin";
			}
			if (!newPassword.equals(confirmPassword)) {
				redirect.addFlashAttribute(WrsisPortalConstant.ERROR_MSG, "Confirm password should be same as New password.");
				return "redirect:firstTimeLogin";
			}
			if (!Validation.validatePassword(newPassword)) {
				redirect.addFlashAttribute(WrsisPortalConstant.ERROR_MSG,
						"New Password must contain A-Z, a-z, 0-9, @ or # or $ or . of length 8-20 characters.");
				return "redirect:firstTimeLogin";
			}

			String userName = null;
			try {
				userName = session.getAttribute(WrsisPortalConstant.USER_NAME).toString();
			} catch (Exception e) {
				LOG.error("FirstTimeLoginController::firstTimeChangePassword():" + e);
				redirect.addFlashAttribute(WrsisPortalConstant.MESSAGE, "Password not changed, try again");
				return "redirect:login";
			}

			User userDetails = loginService.findByUserName(userName);
			if (userDetails.getFirstLogin() != 'Y') {
				return "redirect:login";
			}
			
			

			if (!new MD5PasswordEncoder().matches(currenPassword, userDetails.getPassword())) {
				redirect.addFlashAttribute(WrsisPortalConstant.ERROR_MSG, "You have entered invalid current password.");
				return "redirect:firstTimeLogin";
			} else {
				loginReqDto.setNewPassword(new MD5PasswordEncoder().encode(newPassword));
				loginReqDto.setUserId(userDetails.getUserId());
				loginReqDto.setUserName(userDetails.getUserName());

				String sts = loginService.updatePassword(loginReqDto);
				if (WrsisPortalConstant.SUCCESS.equalsIgnoreCase(sts)) {
					redirect.addFlashAttribute("successMessage",
							"Password Updated Successfully. Please Login with New Password");
					session.removeAttribute(WrsisPortalConstant.USER_NAME);
				} else {
					redirect.addFlashAttribute(WrsisPortalConstant.MESSAGE, "Password Update unsuccessful.");
				}
			}

		} catch (Exception e) {
			LOG.error("FirstTimeLoginController::firstTimeChangePassword():" + e);
		}
		return "redirect:/login";
	}

}
