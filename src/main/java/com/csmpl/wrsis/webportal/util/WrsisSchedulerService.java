package com.csmpl.wrsis.webportal.util;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.csmpl.adminconsole.webportal.repository.UserRepository;
import com.csmpl.adminconsole.webportal.service.UserService;
import com.csmpl.adminconsole.webportal.util.WrsisPortalConstant;
import com.csmpl.wrsis.webportal.repository.LevelDetailsRepository;
import com.csmpl.wrsis.webportal.service.GisService;
import com.csmpl.wrsis.webportal.service.JobDelegationService;
import com.csmpl.wrsis.webportal.service.NotificationService;

@Component
//@Configuration
public class WrsisSchedulerService {

	private static final Logger LOG = LoggerFactory.getLogger(WrsisSchedulerService.class);

	@Autowired
	JobDelegationService jobDelegationService;

	@Autowired
	LevelDetailsRepository levelDetailsRepository;

	@Autowired
	UserService userService;

	@Autowired
	NotificationService notificationService;
	
	@Autowired
	GisService gisService;

	@Value("${schudeler.unblock.indicator}")
	private String unblockUserIndicator;

	@Scheduled(cron = "0 30 11 * * ?") // day 11:30 every day
	// @Scheduled(cron="*/5 * * * * *") //every 5 sec
	// @Scheduled(cron="0 15 0 * * ?") //night 12:15 every day
	public void tempTaskschedule() {
		LOG.info(WrsisPortalConstant.DATE + new Date());
		for (int i = 0; i < 5; i++) {

			LOG.info("WrsisSchedulerService::Scheduller start");
		}

	}

	// night 12:30 every day
	@Scheduled(cron = "0 30 0 * * ?")
	// @Scheduled(cron="0 42 16 * * ?") //test
	public void addJobDelegation() { // add job delegation
		LOG.info("WrsisSchedulerService::addJobDelegation():service started-" + new Date());

		int count = jobDelegationService.addJobDelegations();
		LOG.info("WrsisSchedulerService::addJobDelegation():Delegate count-" + count);

		LOG.info("WrsisSchedulerService::addJobDelegation():service closed-" + new Date());

	}

	// night 12:15 every day
	@Scheduled(cron = "0 15 0 * * ?")
	// @Scheduled(cron="0 15 17 * * ?") //test
	public void removeJobDelegation() { // remove job delegation
		LOG.info("WrsisSchedulerService::removeJobDelegation():service started-" + new Date());

		int count = jobDelegationService.removeJobDelegations();
		LOG.info("WrsisSchedulerService::removeJobDelegation():Delegation removed count-" + count);

		LOG.info("WrsisSchedulerService::removeJobDelegation():service closed-" + new Date());

	}

	@Autowired
	UserRepository userRepository;

	@Value("${schedular.unblock.user.duration}")
	private Integer unblockDuration;

	// every 30 minutes
	// Unblock user
	@Scheduled(cron = "${schedular.unblock.corn.time}")
	public void unblockUserSchedular() {
		if (unblockUserIndicator.equalsIgnoreCase("yes")) {
			userRepository.activeBlockUsersAfterOneDay(unblockDuration);
		}
	}

	// unlock user
	
	//Author : Raman Shrestha
	// night 12:30AM every day
	@Scheduled(cron = "0 30 0 * * ?")
	public void sendNotificationScheduler() { // send Notification
		LOG.info("WrsisSchedulerService::sendNotificationScheduler():service started-" + new Date());

		int count = notificationService.sendNotificationScheduler(new Date());
		LOG.info("WrsisSchedulerService::sendNotificationSchedular():Notification send count-" + count);

		LOG.info("WrsisSchedulerService::sendNotificationScheduler():service closed-" + new Date());

	}
	//Author : Raman Shrestha
	//Date : 03-09-2020
	// night 1:00 Am every day
		@Scheduled(cron = "0 0 1 * * ?")
		public void deleteForcastingFileScheduler() { // delete Forcasting File
			LOG.info("WrsisSchedulerService::deleteForcastingFileScheduler():service started-" + new Date());

			int count = gisService.deleteForcastingFile(new Date());
			LOG.info("WrsisSchedulerService::deleteForcastingFileScheduler(): count-" + count);

			LOG.info("WrsisSchedulerService::deleteForcastingFileScheduler():service closed-" + new Date());

		}
		
		
		//Author : Debendra Nayak
		//Date : 14-07-2021
		// night 11:00 pm every day
		//@Scheduled(cron = "0 30 11 * * ?") //test
		@Scheduled(cron = "0 0 23 * * ?") // night 11:00 pm every day
		public void saveIVRAPIDataScheduler() { // call ivr api
			
			Date date = new Date();
			
			LOG.info("WrsisSchedulerService::saveIVRAPIDataScheduler():scheduler started for ivr api call -" + date);

			String queResList = gisService.updateQuestionOptionData();
			LOG.info("WrsisSchedulerService::updateQuestionOptionData(): queResList-" + queResList);
			String status = gisService.updateIVRData(queResList,date);
			LOG.info("WrsisSchedulerService::updateIVRData(): status-" + status);

			LOG.info("WrsisSchedulerService::saveIVRAPIDataScheduler():scheduler closed for ivr api call -" + new Date());

		}
}
