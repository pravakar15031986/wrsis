package com.csmpl.wrsis.webportal.control;

import java.io.File;
import java.io.FileInputStream;
import java.net.URLConnection;
import java.util.Base64;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.csmpl.adminconsole.webportal.captcha.AesEncryption;
import com.csmpl.adminconsole.webportal.config.MD5PasswordEncoder;
import com.csmpl.adminconsole.webportal.controller.WrsisPortalAbstractController;
import com.csmpl.adminconsole.webportal.entity.User;
import com.csmpl.adminconsole.webportal.service.LoginService;
import com.csmpl.adminconsole.webportal.util.WrsisPortalConstant;
import com.csmpl.wrsis.webportal.bean.PasswordBean;
import com.csmpl.wrsis.webportal.bean.ProfileBean;
import com.csmpl.wrsis.webportal.entity.MailSmsContentEntity;
import com.csmpl.wrsis.webportal.repository.MailSmsContentRepository;
import com.csmpl.wrsis.webportal.util.EmailUtil;
import com.csmpl.wrsis.webportal.util.SMSUtil;
import com.csmpl.wrsis.webportal.util.Validation;

@Controller
public class ChangePasswordController extends WrsisPortalAbstractController {

	public static final Logger LOG = LoggerFactory.getLogger(ChangePasswordController.class);

	@Autowired
	LoginService loginService;
	
	@Autowired 
	SMSUtil smsUtil;

	@Value("${mail.indicator}")
	private String mailFlag;

	@Value("${sms.indicator}")
	private String smsFlag;

	@Value("${spring.profiles.active}")
	private String activeProfile;

	@Autowired
	MailSmsContentRepository mailSmsContentRepository;

	@PostMapping("/updatePassword")
	public String updatePassword(Model model, @ModelAttribute("passwordObj") PasswordBean passwordbean,
			HttpServletRequest request, RedirectAttributes redirect) {
		String emailTxt = "";
		HttpSession session = request.getSession();
		try {

			if (passwordbean.getCurrentPassword() == null || passwordbean.getCurrentPassword().isEmpty()) {
				redirect.addFlashAttribute(WrsisPortalConstant.ERROR_MSG, "Please enter the Old Password");
				return "redirect:changePassword";
			}
			if (passwordbean.getCurrentPassword().length() != 0) {
				boolean isValidate = Validation.validateFirstBlankSpace(passwordbean.getCurrentPassword());
				if (!isValidate) {
					redirect.addFlashAttribute(WrsisPortalConstant.ERROR_MSG, "Old password should not start with a blank space");
					return "redirect:changePassword";
				}
			}

			if (passwordbean.getCurrentPassword().length() != 0) {
				boolean isValidate = Validation.validateLastBlankSpace(passwordbean.getCurrentPassword());
				if (!isValidate) {
					redirect.addFlashAttribute(WrsisPortalConstant.ERROR_MSG, "Old password should not end with a blank space");
					return "redirect:changePassword";
				}
			}
			if (passwordbean.getCurrentPassword().length() != 0) {
				boolean isValidate = Validation
						.validateConsecutiveBlankSpacesInString(passwordbean.getCurrentPassword());
				if (!isValidate) {
					redirect.addFlashAttribute(WrsisPortalConstant.ERROR_MSG, "Old password should not contain consecutive blank space");
					return "redirect:changePassword";
				}
			}
			if (passwordbean.getNewPassword() == null || passwordbean.getNewPassword().isEmpty()) {
				redirect.addFlashAttribute(WrsisPortalConstant.ERROR_MSG, "Please enter the New Password");
				return "redirect:changePassword";
			}
			if (passwordbean.getNewPassword().length() != 0) {
				boolean isValidate = Validation.validateFirstBlankSpace(passwordbean.getNewPassword());
				if (!isValidate) {
					redirect.addFlashAttribute(WrsisPortalConstant.ERROR_MSG, "New password should not start with a blank space");
					return "redirect:changePassword";
				}
			}

			if (passwordbean.getNewPassword().length() != 0) {
				boolean isValidate = Validation.validateLastBlankSpace(passwordbean.getNewPassword());
				if (!isValidate) {
					redirect.addFlashAttribute(WrsisPortalConstant.ERROR_MSG, "New password should not end with a blank space");
					return "redirect:changePassword";
				}
			}
			if (passwordbean.getNewPassword().length() != 0) {
				boolean isValidate = Validation.validateConsecutiveBlankSpacesInString(passwordbean.getNewPassword());
				if (!isValidate) {
					redirect.addFlashAttribute(WrsisPortalConstant.ERROR_MSG, "New password Should not Contain Consecutive Blank space");
					return "redirect:changePassword";
				}
			}
			if (passwordbean.getConfirmPassword() == null || passwordbean.getCurrentPassword().isEmpty()) {
				redirect.addFlashAttribute(WrsisPortalConstant.ERROR_MSG, "Please enter the Confirm Password");
				return "redirect:changePassword";
			}
			if (passwordbean.getConfirmPassword().length() != 0) {
				boolean isValidate = Validation.validateFirstBlankSpace(passwordbean.getConfirmPassword());
				if (!isValidate) {
					redirect.addFlashAttribute(WrsisPortalConstant.ERROR_MSG, "Confirm password should not start with a blank space");
					return "redirect:changePassword";
				}
			}

			if (passwordbean.getConfirmPassword().length() != 0) {
				boolean isValidate = Validation.validateLastBlankSpace(passwordbean.getConfirmPassword());
				if (!isValidate) {
					redirect.addFlashAttribute(WrsisPortalConstant.ERROR_MSG, "Confirm password should not end with a blank space");
					return "redirect:changePassword";
				}
			}
			if (passwordbean.getConfirmPassword().length() != 0) {
				boolean isValidate = Validation
						.validateConsecutiveBlankSpacesInString(passwordbean.getConfirmPassword());
				if (!isValidate) {
					redirect.addFlashAttribute(WrsisPortalConstant.ERROR_MSG, "Confirm password should not contain consecutive blank space");
					return "redirect:changePassword";
				}
			}
			if (passwordbean.getCurrentPassword().equals(passwordbean.getNewPassword())) {
				redirect.addFlashAttribute(WrsisPortalConstant.ERROR_MSG, "New Password should not same as Old Password");
				return "redirect:changePassword";
			}
			if (!passwordbean.getNewPassword().equals(passwordbean.getConfirmPassword())) {
				redirect.addFlashAttribute(WrsisPortalConstant.ERROR_MSG, "Confirm Password should be same as New Password");
				return "redirect:changePassword";
			}
			if (!Validation.validatePassword(passwordbean.getNewPassword())) {
				redirect.addFlashAttribute(WrsisPortalConstant.ERROR_MSG,
						"New Password must contain A-Z, a-z, 0-9, @ or # or $ or . of length 8-20 characters");
				return "redirect:changePassword";
			}

			String userName = null;
			try {
				userName = session.getAttribute(WrsisPortalConstant.USER_NAME).toString();
			} catch (Exception e) {
				LOG.error("ChangePasswordController::updatePassword():" + e);
				redirect.addFlashAttribute(WrsisPortalConstant.ERROR_MSG, "Password not changed, try again");
				return "redirect:/changePassword";
			}

			User userDetails = loginService.findByUserName(userName);
			String enteredPassword = passwordbean.getCurrentPassword();
			String existPassword = userDetails.getPassword();

			if (!new MD5PasswordEncoder().matches(enteredPassword, existPassword)) {
				redirect.addFlashAttribute(WrsisPortalConstant.ERROR_MSG, "You have entered invalid Old password");
				return "redirect:/changePassword";
			} else {
				passwordbean.setNewPassword(new MD5PasswordEncoder().encode(passwordbean.getNewPassword()));
				passwordbean.setUserId(userDetails.getUserId());
				passwordbean.setUserName(userDetails.getUserName());

				String sts = loginService.updatePassword(passwordbean);
				if (sts.equalsIgnoreCase(WrsisPortalConstant.SUCCESS)) {
					redirect.addFlashAttribute(WrsisPortalConstant.SUCCESS_MSG, "Password Updated Successfully");
					redirect.addFlashAttribute(WrsisPortalConstant.MESSAGE, "Password Updated Successfully. Login with New Password");

					// For sending sms
                    
					LOG.info("ChangePasswordController::updatePassword():smsFlag-" + smsFlag);
					if (WrsisPortalConstant.YES.equalsIgnoreCase(smsFlag))
					{
						LOG.info("ChangePasswordController::updatePassword(): Now sms going to send to this no.-" + userDetails.getMobile());
						if (userDetails.getMobile() != null && !"".equalsIgnoreCase(userDetails.getMobile())) {
							try {
								
							 final String newSMSTxt="Your password has been updated successfully";
								Thread t = new Thread() {
									@Override
									public void run() {
										LOG.info("ChangePasswordController::updatePassword(): Now SMSUtil->sendSms() call");
										smsUtil.sendSms(userDetails.getMobile(), newSMSTxt);
										}
								};
								t.start();
							} catch (Exception e) {
								LOG.error("ChangePasswordController::updatePassword():error on sending sms" + e);
							}
						}
					}
					// For Sending Email
					if (WrsisPortalConstant.YES.equalsIgnoreCase(mailFlag)) {
						final MailSmsContentEntity mailsms = mailSmsContentRepository.findByMscontentid(1);
						if (userDetails.getEmail() != null && !"".equalsIgnoreCase(userDetails.getEmail())) {
							try {
								emailTxt = mailsms.getMailcontent();
								final String newEmailTxt = emailTxt.replaceAll("@username", userName);
								Thread t = new Thread() {
									@Override
									public void run() {

										EmailUtil.sendAppcMail(userDetails.getEmail(), newEmailTxt,
												mailsms.getMailsubject());
									}
								};
								t.start();

							} catch (Exception e) {
								LOG.error("ChangePasswordController::updatePassword() sending email:" + e);

							}
						}
					}

				} else {
					redirect.addFlashAttribute(WrsisPortalConstant.ERROR_MSG, "Password Update unsuccessful");
					return "redirect:/changePassword";
				}
			}

			session.invalidate();
		} catch (Exception e) {
			LOG.error("ChangePasswordController::updatePassword() sending email:" + e);

			session.invalidate();
			redirect.addFlashAttribute(WrsisPortalConstant.MESSAGE, "Exception occurred !!");
		}

		return "redirect:/login";

	}

	@GetMapping(value = { "/logoutAfterPasswordChange" })
	public String logout(HttpSession session, Model model, HttpServletRequest request, RedirectAttributes redirect) {
		session.invalidate();
		redirect.addFlashAttribute(WrsisPortalConstant.MESSAGE, "Password changed successfully,Login again with new password");
		return "redirect:/" + request.getContextPath();
	}

	@RequestMapping(value = "/userProfile")
	public String userProfile(Model model, HttpServletRequest request, RedirectAttributes redirect) {

		try {
			HttpSession session = request.getSession();
			String userName = session.getAttribute(WrsisPortalConstant.USER_NAME).toString();

			ProfileBean userObj = new ProfileBean();
			User userDetails = loginService.findByUserName(userName);

			userObj.setUserId(userDetails.getUserId());
			userObj.setFullName(userDetails.getFullName());
			userObj.setMobile(userDetails.getMobile());
			userObj.setEmail(userDetails.getEmail());
			userObj.setProfilePhotoName(userDetails.getPhotoFileName());
			userObj.setGender(userDetails.getGender());

			model.addAttribute(WrsisPortalConstant.PROFILE_DETAILS, userObj);
		} catch (Exception e) {
			LOG.error("ChangePasswordController::userProfile():" + e);

		}

		return WrsisPortalConstant.USER_PROFILE;

	}

	@PostMapping("/updateProfile")
	public String updateProfile(@ModelAttribute(WrsisPortalConstant.PROFILE_DETAILS) ProfileBean profileDetails, Model model,
			HttpServletRequest request, RedirectAttributes redirect) {

		try {
			HttpSession session = request.getSession();

			if (profileDetails.getFullName() == null || profileDetails.getFullName().isEmpty()) {

				model.addAttribute(WrsisPortalConstant.ERROR_MSG, "Please enter the Name");
				return WrsisPortalConstant.USER_PROFILE;
			}
			if (profileDetails.getFullName().length() != 0) {
				boolean isValidate = Validation.validateFirstBlankSpace(profileDetails.getFullName());
				if (!isValidate) {
					model.addAttribute(WrsisPortalConstant.ERROR_MSG, "Name should not start with a blank space");
					return WrsisPortalConstant.USER_PROFILE;
				}
			}

			if (profileDetails.getFullName().length() != 0) {
				boolean isValidate = Validation.validateLastBlankSpace(profileDetails.getFullName());
				if (!isValidate) {
					model.addAttribute(WrsisPortalConstant.ERROR_MSG, "Name Should not End with a Blank space");
					return WrsisPortalConstant.USER_PROFILE;
				}
			}
			if (profileDetails.getFullName().length() != 0) {
				boolean isValidate = Validation.validateConsecutiveBlankSpacesInString(profileDetails.getFullName());
				if (!isValidate) {
					model.addAttribute(WrsisPortalConstant.ERROR_MSG, "Name should not contain consecutive blank space");
					return WrsisPortalConstant.USER_PROFILE;
				}
			}

			if (profileDetails.getFullName().length() != 0) {
				boolean isValidate = Validation.validateName(profileDetails.getFullName());
				if (!isValidate) {
					model.addAttribute(WrsisPortalConstant.ERROR_MSG, "Name accept only alphabates");
					return WrsisPortalConstant.USER_PROFILE;
				}
			}
			if (profileDetails.getMobile() == null || profileDetails.getMobile().isEmpty()) {

				model.addAttribute(WrsisPortalConstant.ERROR_MSG, "Please enter Mobile No.");
				return WrsisPortalConstant.USER_PROFILE;
			}
			if (profileDetails.getMobile().length() != 0) {
				boolean isValidate = Validation.validateFirstBlankSpace(profileDetails.getMobile());
				if (!isValidate) {
					model.addAttribute(WrsisPortalConstant.ERROR_MSG, "Mobile No. should not start with a blank space");
					return WrsisPortalConstant.USER_PROFILE;
				}
			}

			if (profileDetails.getMobile().length() != 0) {
				boolean isValidate = Validation.validateLastBlankSpace(profileDetails.getMobile());
				if (!isValidate) {
					model.addAttribute(WrsisPortalConstant.ERROR_MSG, "Mobile No. should not end with a blank space");
					return WrsisPortalConstant.USER_PROFILE;
				}
			}

			if (profileDetails.getMobile().length() != 0) {
				boolean isValidate = Validation.validateMobile(profileDetails.getMobile());
				if (!isValidate) {
					model.addAttribute(WrsisPortalConstant.ERROR_MSG,
							"Mobile No. accept only numbers and it should not be less than 6 digits");
					return WrsisPortalConstant.USER_PROFILE;
				}
			}
			if (profileDetails.getEmail() == null || profileDetails.getEmail().isEmpty()) {

				model.addAttribute(WrsisPortalConstant.ERROR_MSG, "Please enter Email");
				return WrsisPortalConstant.USER_PROFILE;
			}
			if (profileDetails.getEmail().length() != 0) {
				boolean isValidate = Validation.validateFirstBlankSpace(profileDetails.getEmail());
				if (!isValidate) {
					model.addAttribute(WrsisPortalConstant.ERROR_MSG, "Email should not start with a blank space");
					return WrsisPortalConstant.USER_PROFILE;
				}
			}

			if (profileDetails.getEmail().length() != 0) {
				boolean isValidate = Validation.validateLastBlankSpace(profileDetails.getEmail());
				if (!isValidate) {
					model.addAttribute(WrsisPortalConstant.ERROR_MSG, "Email should not end with a blank space");
					return WrsisPortalConstant.USER_PROFILE;
				}
			}
			if (profileDetails.getEmail().length() != 0) {
				boolean isValidate = Validation.validateConsecutiveBlankSpacesInString(profileDetails.getEmail());
				if (!isValidate) {
					model.addAttribute(WrsisPortalConstant.ERROR_MSG, "Email should not contain consecutive blank space");
					return WrsisPortalConstant.USER_PROFILE;
				}
			}

			if (profileDetails.getEmail().length() != 0) {
				boolean isValidate = Validation.validateEmail(profileDetails.getEmail());
				if (!isValidate) {
					model.addAttribute(WrsisPortalConstant.ERROR_MSG, "Invalid Email");
					return WrsisPortalConstant.USER_PROFILE;
				}
			}

			if (profileDetails.getProfileImg() != null && !profileDetails.getProfileImg().isEmpty()) {

				if (!Validation.validateProfilePhotoUpload(profileDetails.getProfileImg().getOriginalFilename())) {

					model.addAttribute(WrsisPortalConstant.ERROR_MSG, "Upload .jpg, .jpeg or .png file only ");
					return WrsisPortalConstant.USER_PROFILE;
				}

				long fileSize = profileDetails.getProfileImg().getSize();

				if (fileSize > 102400) {

					model.addAttribute(WrsisPortalConstant.ERROR_MSG, "Profile photo size should be less than 100KB");
					return WrsisPortalConstant.USER_PROFILE;
				}

			}

			int userId = (int) session.getAttribute(WrsisPortalConstant.USER_ID);

			profileDetails.setUserId(userId);
			model.addAttribute(WrsisPortalConstant.PROFILE_DETAILS, profileDetails);

			String sts = loginService.updateProfile(profileDetails);

			if (WrsisPortalConstant.SUCCESS.equalsIgnoreCase(sts)) {

				redirect.addFlashAttribute(WrsisPortalConstant.SUCCESS_MSG, "Profile updated successfully");
				addAttibuteInSession(request, profileDetails.getFullName(), WrsisPortalConstant.FULLNAME);

			} else if ("mobileNoExists".equalsIgnoreCase(sts)) {
				model.addAttribute(WrsisPortalConstant.PROFILE_DETAILS, profileDetails);
				model.addAttribute(WrsisPortalConstant.ERROR_MSG, "Mobile No. already registered for other user ");

				return WrsisPortalConstant.USER_PROFILE;

			} else {
				model.addAttribute(WrsisPortalConstant.PROFILE_DETAILS, profileDetails);
				model.addAttribute(WrsisPortalConstant.ERROR_MSG, "Profile update failed");
				return WrsisPortalConstant.USER_PROFILE;
			}
		} catch (Exception e) {
			LOG.error("ChangePasswordController::updateProfile():" + e);

		}

		return "redirect:userProfile";
	}

	@RequestMapping("viewProfilePhoto")
	public void viewProfilePhoto(HttpServletRequest request, HttpServletResponse response) {
		File file = null;
		try {
			String userName = request.getSession().getAttribute(WrsisPortalConstant.USER_NAME).toString();
			User userDetails = loginService.findByUserName(userName);
			if (userDetails.getPhotoFileName() != null && !userDetails.getPhotoFileName().isEmpty()) {
				file = new File(WrsisPortalConstant.wrsisPropertiesFileConstants.USER_PROFILE_IMG_UPLOAD_PATH
						+ File.separator + userDetails.getPhotoFileName());
				if (!file.exists()) {
					file = new File(request.getServletContext().getRealPath("/") + "wrsis" + File.separator + "images"
							+ File.separator + "blank_profile_img.png");
				}
			} else {
				file = new File(request.getServletContext().getRealPath("/") + File.separator + "wrsis" + File.separator
						+ "images" + File.separator + "blank_profile_img.png");
			}

			if (file != null) {

				byte[] bFile = new byte[(int) file.length()];
				FileInputStream fileInputStream = new FileInputStream(file);
				fileInputStream.read(bFile);
				fileInputStream.close();
				response.setContentType(URLConnection.guessContentTypeFromName(file.getName()));
				response.getOutputStream().write(bFile);
				response.getOutputStream().flush();
			}

		} catch (Exception e) {
			LOG.error("ChangePasswordController::viewProfilePhoto():" + e);

		}

	}

	@RequestMapping(value = "/forgotPassword")
	public String forgotPassword() {

		return "forgotPassword";

	}

	@PostMapping("/passwordReset")
	public String passwordReset(Model model, @ModelAttribute("paswdResetDetails") PasswordBean paswdResetDetails,
			HttpServletRequest request, RedirectAttributes redirect) {

		String emailTxt = "";
		HttpSession session = request.getSession();
		try {

			model.addAttribute("paswdResetDetails", paswdResetDetails);

			// captcha validation
			String securityAns = request.getParameter("securityAns");
			Object sObj = session.getAttribute("captcha_key_name");

			if (securityAns == null || securityAns.isEmpty()) {
				redirect.addFlashAttribute(WrsisPortalConstant.MESSAGE, "Please enter appropriate answer.");
				return "redirect:forgotPassword";
			}
			if (sObj == null) {
				redirect.addFlashAttribute(WrsisPortalConstant.MESSAGE, "Try after sometime");
				return "redirect:forgotPassword";
			}

			String sessionAns = AesEncryption.decrypt(sObj.toString());
			
			if (!sessionAns.equals(securityAns)) {
				redirect.addFlashAttribute(WrsisPortalConstant.MESSAGE, "Please enter appropriate answer.");
				return "redirect:forgotPassword";
			}

			// validation starts
			if (paswdResetDetails.getUserName() == null || paswdResetDetails.getUserName().isEmpty()) {
				model.addAttribute(WrsisPortalConstant.ERROR_MSG, "User ID should not empty");
				return "forgotPassword";

			}
			if (paswdResetDetails.getEmail() == null || paswdResetDetails.getEmail().isEmpty()) {
				model.addAttribute(WrsisPortalConstant.ERROR_MSG, "Email should not empty");
				return "forgotPassword";

			}
			if (!Validation.validateEmail(paswdResetDetails.getEmail())) {
				model.addAttribute(WrsisPortalConstant.ERROR_MSG, "Enter valid email");
				return "forgotPassword";
			}
			String decodeUserId = new String(Base64.getDecoder().decode(paswdResetDetails.getUserName().getBytes()));

			User userDetails = loginService.findByUserName(decodeUserId);

			if (userDetails == null) {
				model.addAttribute(WrsisPortalConstant.ERROR_MSG, "User ID does not exist");
				return "forgotPassword";

			}
			if (!paswdResetDetails.getEmail().equalsIgnoreCase(userDetails.getEmail())) {
				model.addAttribute(WrsisPortalConstant.ERROR_MSG, "Enter valid email");
				return "forgotPassword";
			}
			paswdResetDetails.setUserId(userDetails.getUserId());
			String sts = loginService.updateforgottenPassword(paswdResetDetails);

			model.addAttribute("paswdResetDetails", null);
			// Logic for sending the password to the email has to be done.

			if ("failure".equalsIgnoreCase(sts)) {
				model.addAttribute(WrsisPortalConstant.ERROR_MSG, "Password reset failed");
				return "forgotPassword";
			} else {

				if ("stag".equalsIgnoreCase(activeProfile) || "prod".equalsIgnoreCase(activeProfile))
					model.addAttribute("PswdMsg", WrsisPortalConstant.SUCCESS);
				else
					model.addAttribute("PswdMsg", sts);
				// For sending sms
				LOG.info("ChangePasswordController::passwordReset():smsFlag-" + smsFlag);
				if (WrsisPortalConstant.YES.equalsIgnoreCase(smsFlag))
				{
					LOG.info("ChangePasswordController::passwordReset(): Now sms going to send to this no.-" + userDetails.getMobile());
					if (userDetails.getMobile() != null && !"".equalsIgnoreCase(userDetails.getMobile())) {
						try {
							
						 final String newSMSTxt="Your password has been reset successfully,Login with new password "+sts;
							Thread t = new Thread() {
								@Override
								public void run() {
									LOG.info("ChangePasswordController::passwordReset(): Now SMSUtil->sendSms() call");
									smsUtil.sendSms(userDetails.getMobile(), newSMSTxt);
									
								}
							};
							t.start();
						} catch (Exception e) {
							LOG.error("ChangePasswordController::passwordReset():error on sending sms" + e);
						}
					}
				}
				// For Sending Email
				if (WrsisPortalConstant.YES.equalsIgnoreCase(mailFlag)) {
					final MailSmsContentEntity mailsms = mailSmsContentRepository.findByMscontentid(3);
					if (userDetails.getEmail() != null && !"".equalsIgnoreCase(userDetails.getEmail())) {
						try {
							emailTxt = mailsms.getMailcontent();
							final String newEmailTxt = emailTxt.replace("@username", userDetails.getUserName())
									.replace("@password", sts);
							Thread t = new Thread() {
								@Override
								public void run() {

									EmailUtil.sendAppcMail(userDetails.getEmail(), newEmailTxt,
											mailsms.getMailsubject());
								}
							};
							t.start();

						} catch (Exception e) {
							LOG.error("ChangePasswordController::passwordReset() email sending:" + e);
						}
					}
				}

			}

		} catch (Exception e) {
			LOG.error("ChangePasswordController::passwordReset():" + e);

			model.addAttribute(WrsisPortalConstant.ERROR_MSG, "You have entered an invalid User ID");
			return "forgotPassword";
		}
		return "login";

	}

	@ResponseBody
	@GetMapping("/validateMoblieByUser")
	public String validateMobileNumber(@RequestParam("mobile") String mobile, HttpServletRequest request) {
		int userId = 0;
		try {
			HttpSession session = request.getSession();
			userId = (Integer) session.getAttribute(WrsisPortalConstant.USER_ID);

		} catch (Exception e) {
			LOG.error("ChangePasswordController::validateMobileNumber():" + e);

		}

		return loginService.validateMobileNumberByUserId(mobile, userId);
	}
}
