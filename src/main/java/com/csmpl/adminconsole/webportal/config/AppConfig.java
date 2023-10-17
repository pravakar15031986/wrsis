package  com.csmpl.adminconsole.webportal.config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.csmpl.adminconsole.webportal.bean.PrimaryLinkVo;
import com.csmpl.adminconsole.webportal.util.WrsisPortalConstant;
import com.csmpl.wrsis.webportal.util.WrsisPortalURLs;



@Configuration
@PropertySource("classpath:application.properties")
public class AppConfig implements WebMvcConfigurer{
	
	  private final Map<String,String> mapUrls = new HashMap();
      AppConfig(){
		
		mapUrls.put(WrsisPortalURLs.Dashboard, "Home");
		
		mapUrls.put(WrsisPortalURLs.RECOMMENDATION, "addadvisory");
		mapUrls.put(WrsisPortalURLs.RECOMMENDATION1, "addadvisory");
		mapUrls.put(WrsisPortalURLs.RECOMMENDATION1A, "addadvisory");
		
		mapUrls.put(WrsisPortalURLs.RECOMMENDATION2, "recommendation");
		mapUrls.put(WrsisPortalURLs.RECOMMENDATION2A, "recommendation");
		mapUrls.put(WrsisPortalURLs.RECOMMENDATION3, "recommendation");
		mapUrls.put(WrsisPortalURLs.RECOMMENDATION3A, "recommendation");
		mapUrls.put(WrsisPortalURLs.RECOMMENDATION4, "recommendation");
		mapUrls.put(WrsisPortalURLs.RECOMMENDATION5, "moarecommendation");
		mapUrls.put(WrsisPortalURLs.RECOMMENDATION5A, "moarecommendation");
		
		mapUrls.put(WrsisPortalURLs.RECOMMENDATION11, "addadvisory");
		mapUrls.put(WrsisPortalURLs.RECOMMENDATION12, "viewRecommendationByBoa");
		mapUrls.put(WrsisPortalURLs.RECOMMENDATION13, "viewRecommendationByBoa");
		mapUrls.put(WrsisPortalURLs.RECOMMENDATION14, "viewRecommendationByBoa"); 
		mapUrls.put(WrsisPortalURLs.RECOMMENDATION15, "woredarecommendation");
		mapUrls.put(WrsisPortalURLs.RECOMMENDATION16, "moaRecommendationStatus");
		mapUrls.put(WrsisPortalURLs.RECOMMENDATION17, "moaRecommendationStatus");
		mapUrls.put(WrsisPortalURLs.RECOMMENDATION18, "viewPublishedAdvisory");
		mapUrls.put(WrsisPortalURLs.RECOMMENDATION19, "viewPublishedRecommendation");
		
		
		mapUrls.put(WrsisPortalURLs.RECOMMENDATION6, "woredarecommendation");
		mapUrls.put(WrsisPortalURLs.RECOMMENDATION7, "woredarecommendation");
		
		mapUrls.put(WrsisPortalURLs.RECOMMENDATION8, "viewImplementationDetails");
		
		mapUrls.put(WrsisPortalURLs.RECOMMENDATION9, "addadvisory");
		mapUrls.put(WrsisPortalURLs.RECOMMENDATION10, "addadvisory");
		mapUrls.put(WrsisPortalURLs.RECOMMENDATION20, "woredaExpertRecommendation");
		
		mapUrls.put(WrsisPortalURLs.RECOMMENDATION21, "advisorypending");
		mapUrls.put(WrsisPortalURLs.RECOMMENDATION21A, "advisorypending");
		mapUrls.put(WrsisPortalURLs.RECOMMENDATION21B, "advisorypending");
		mapUrls.put(WrsisPortalURLs.RECOMMENDATION21C, "advisorypending");
		mapUrls.put(WrsisPortalURLs.RECOMMENDATION21D, "advisorypending");
		
		
		mapUrls.put(WrsisPortalURLs.RECOMMENDATION22, "pendingrecommendation");
		mapUrls.put(WrsisPortalURLs.RECOMMENDATION22A, "pendingrecommendation");
		mapUrls.put(WrsisPortalURLs.RECOMMENDATION22B, "pendingrecommendation");
		mapUrls.put(WrsisPortalURLs.RECOMMENDATION22C, "pendingrecommendation");
		mapUrls.put(WrsisPortalURLs.RECOMMENDATION22D, "pendingrecommendation");
		
		
		
		
		mapUrls.put(WrsisPortalURLs.Implementation2, "woredaExpertImplementation");
		mapUrls.put(WrsisPortalURLs.Implementation3, "woredaExpertImplementation");
		mapUrls.put(WrsisPortalURLs.Implementation3A, "woredaExpertImplementation");
		mapUrls.put(WrsisPortalURLs.Implementation4, "woredaExpertImplementation");
		mapUrls.put(WrsisPortalURLs.Implementation5, "woredaExpertImplementation");
		mapUrls.put(WrsisPortalURLs.Implementation6, "woredaExpertImplementation");
		
		
		mapUrls.put(WrsisPortalURLs.Implementation7, "viewMonitorImplementation");
		mapUrls.put(WrsisPortalURLs.Implementation8, "viewMonitorImplementation");
		mapUrls.put(WrsisPortalURLs.Implementation9, "viewMonitorImplementation");
		mapUrls.put(WrsisPortalURLs.Implementation10, "viewMonitorImplementation");
		mapUrls.put(WrsisPortalURLs.Implementation11, "viewMonitorImplementation");
		mapUrls.put(WrsisPortalURLs.Implementation12, "viewMonitorImplementation");
		mapUrls.put(WrsisPortalURLs.Implementation13, "viewMonitorImplementation");
		mapUrls.put(WrsisPortalURLs.Implementation14, "viewMonitorImplementation");
		mapUrls.put(WrsisPortalURLs.Implementation15, "viewMonitorImplementation");
		mapUrls.put(WrsisPortalURLs.Implementation16, "viewMonitorImplementation");
		
		mapUrls.put(WrsisPortalURLs.Implementation17, "viewRecommendationResult");
		mapUrls.put(WrsisPortalURLs.Implementation18, "viewPublishedRecommendationSurveyData");
		mapUrls.put(WrsisPortalURLs.Implementation19, "viewPublishedRecommendationSurveyData");
		
		

		mapUrls.put(WrsisPortalURLs.MIS_REPORT, "prevalenceReport");
		mapUrls.put(WrsisPortalURLs.MIS_REPORTA, "prevalenceReport");
		
		mapUrls.put(WrsisPortalURLs.MIS_REPORT1, "incidence-Severity-RustReport");
		mapUrls.put(WrsisPortalURLs.MIS_REPORT1A, "incidence-Severity-RustReport");
		
		mapUrls.put(WrsisPortalURLs.MIS_REPORT2, "reaction-ToRustReport");
		mapUrls.put(WrsisPortalURLs.MIS_REPORT2A, "reaction-ToRustReport");
		
		mapUrls.put(WrsisPortalURLs.MIS_REPORT3, "wheatRustReport");
		
		mapUrls.put(WrsisPortalURLs.MIS_REPORT4, "implementationSummaryReport");
		mapUrls.put(WrsisPortalURLs.MIS_REPORT4A, "implementationSummaryReport");
		
		mapUrls.put(WrsisPortalURLs.MIS_REPORT24, "implementationReport");
		mapUrls.put(WrsisPortalURLs.MIS_REPORT24A, "implementationReport");
		
		
		mapUrls.put(WrsisPortalURLs.MIS_REPORT5, "stemRustRaces-DetectedCropping");
		mapUrls.put(WrsisPortalURLs.MIS_REPORT5A, "stemRustRaces-DetectedCropping");
		
		mapUrls.put(WrsisPortalURLs.MIS_REPORT6, "stemRustRaces-IdentifiedCropping");
		mapUrls.put(WrsisPortalURLs.MIS_REPORT6A, "stemRustRaces-IdentifiedCropping");
		mapUrls.put(WrsisPortalURLs.MIS_REPORT6B, "stemRustRaces-IdentifiedCropping");
		
		
		mapUrls.put(WrsisPortalURLs.MIS_REPORT7, "virulence-SpectrumPgtRaces");
		mapUrls.put(WrsisPortalURLs.MIS_REPORT7A, "virulence-SpectrumPgtRaces");
		mapUrls.put(WrsisPortalURLs.MIS_REPORT8, "regionAndRustTypeReport");
		mapUrls.put(WrsisPortalURLs.MIS_REPORT9, "surveyAndIvrReport"); 
		mapUrls.put(WrsisPortalURLs.MIS_REPORT10, "rustTypeChartReport");
		
		mapUrls.put(WrsisPortalURLs.MIS_REPORT11, "surveyPublishedReport");
		mapUrls.put(WrsisPortalURLs.MIS_REPORT11A, "surveyPublishedReport");
		mapUrls.put(WrsisPortalURLs.MIS_REPORT12, "surveyPublishedReport");
		mapUrls.put(WrsisPortalURLs.MIS_REPORT12A, "surveyPublishedReport");
		mapUrls.put(WrsisPortalURLs.MIS_REPORT12B, "surveyPublishedReport");
		mapUrls.put(WrsisPortalURLs.MIS_REPORT12C, "surveyPublishedReport");
		mapUrls.put(WrsisPortalURLs.MIS_REPORT12D, "surveyPublishedReport");
		mapUrls.put(WrsisPortalURLs.MIS_REPORT12E, "surveyPublishedReport");
		
		mapUrls.put(WrsisPortalURLs.MIS_REPORT13, "viewPublishedAdvisory");
		mapUrls.put(WrsisPortalURLs.MIS_REPORT14, "viewPublishedRecommendation");
		mapUrls.put(WrsisPortalURLs.MIS_REPORT15, "addadvisory");
		mapUrls.put(WrsisPortalURLs.MIS_REPORT16, "reportSurveyReportByGlobalRust");
		mapUrls.put(WrsisPortalURLs.MIS_REPORT17, "addadvisory");
		mapUrls.put(WrsisPortalURLs.MIS_REPORT18, "addadvisory");
		mapUrls.put(WrsisPortalURLs.MIS_REPORT19, "raceAnalysisReport");
		mapUrls.put(WrsisPortalURLs.MIS_REPORT23, "reportSurveyReportByGlobalRust");
		mapUrls.put(WrsisPortalURLs.MIS_REPORT23A, "reportSurveyReportByGlobalRust");
		
		mapUrls.put(WrsisPortalURLs.MIS_REPORT20, "mapdetails");
		mapUrls.put(WrsisPortalURLs.MIS_REPORT21, "IVRMapDetails");
		mapUrls.put(WrsisPortalURLs.MIS_REPORT22, "IncidentMapDetails");
		mapUrls.put(WrsisPortalURLs.MIS_REPORT22A, "viewForecastingMap");
		
		mapUrls.put(WrsisPortalURLs.MIS_REPORT25, "rustSurveyAndRecommentationSurveyChart");
		mapUrls.put(WrsisPortalURLs.MIS_REPORT26, "rustSurveyOtherDisease");
		mapUrls.put(WrsisPortalURLs.MIS_REPORT26A, "rustSurveyOtherDisease");
		
		mapUrls.put(WrsisPortalURLs.Race_Analysis, "stem-rust-race-analysis");
		mapUrls.put(WrsisPortalURLs.Race_AnalysisA, "stem-rust-race-analysis");
		mapUrls.put(WrsisPortalURLs.Race_AnalysisB, "stem-rust-race-analysis");
		
		mapUrls.put(WrsisPortalURLs.Race_Analysis1, "stem-rust-race-analysis");
		mapUrls.put(WrsisPortalURLs.Race_Analysis2, "stem-rust-race-analysis");
		mapUrls.put(WrsisPortalURLs.Race_Analysis2A, "stem-rust-race-analysis");
		
		mapUrls.put(WrsisPortalURLs.Race_Analysis3, "stem-rust-race-analysis");
		mapUrls.put(WrsisPortalURLs.Race_Analysis3A, "stem-rust-race-analysis");
		
		mapUrls.put(WrsisPortalURLs.Race_Analysis4, "stem-rust-race-analysis");
		mapUrls.put(WrsisPortalURLs.Race_Analysis4A, "stem-rust-race-analysis");
		
		
		mapUrls.put(WrsisPortalURLs.Race_Analysis5, "stem-rust-race-analysis");
		mapUrls.put(WrsisPortalURLs.Race_Analysis5A, "stem-rust-race-analysis");
		mapUrls.put(WrsisPortalURLs.Race_Analysis5B, "stem-rust-race-analysis");
		
		mapUrls.put(WrsisPortalURLs.Race_Analysis6, "dumpedExternalSamples");
		mapUrls.put(WrsisPortalURLs.Race_Analysis6A, "dumpedExternalSamples");
		
		
		mapUrls.put(WrsisPortalURLs.Race_Analysis29, "allocationforrestanalysis");
		mapUrls.put(WrsisPortalURLs.Race_Analysis30, "viewRaceAnalysisStatus");
		mapUrls.put(WrsisPortalURLs.Race_Analysis30A, "viewRaceAnalysisStatus");
		
		mapUrls.put(WrsisPortalURLs.Race_Analysis31, "allocationforrestanalysis");
		mapUrls.put(WrsisPortalURLs.Race_Analysis31A, "allocationforrestanalysis");
		mapUrls.put(WrsisPortalURLs.Race_Analysis32, "allocationforrestanalysis");
		
		
		mapUrls.put(WrsisPortalURLs.Race_Analysis33, "externalTaggedSamples");
		mapUrls.put(WrsisPortalURLs.Race_Analysis33A, "externalTaggedSamples");
		mapUrls.put(WrsisPortalURLs.Race_Analysis34, "externalTaggedSamples");
		mapUrls.put(WrsisPortalURLs.Race_Analysis35, "externalTaggedSamples");
		
		
		//Race_Analysis33
		
		
		mapUrls.put(WrsisPortalURLs.MANAGE_MASTER, "addTypeofRust");
		mapUrls.put(WrsisPortalURLs.MANAGE_MASTER1, "addTypeofRust");
		mapUrls.put(WrsisPortalURLs.MANAGE_MASTER2, "addTypeofRust");
		
		mapUrls.put(WrsisPortalURLs.MANAGE_MASTER3, "addgrowthstage");
		mapUrls.put(WrsisPortalURLs.MANAGE_MASTER4, "addgrowthstage");
		mapUrls.put(WrsisPortalURLs.MANAGE_MASTER5, "addgrowthstage");
		
		mapUrls.put(WrsisPortalURLs.MANAGE_MASTER6, "addSampleType");
		mapUrls.put(WrsisPortalURLs.MANAGE_MASTER7, "addSampleType");
		mapUrls.put(WrsisPortalURLs.MANAGE_MASTER8, "addSampleType");
		
		mapUrls.put(WrsisPortalURLs.MANAGE_MASTER9, "addtypeoffungicide");
		mapUrls.put(WrsisPortalURLs.MANAGE_MASTER10, "addtypeoffungicide");
		mapUrls.put(WrsisPortalURLs.MANAGE_MASTER11, "addtypeoffungicide");
		
		mapUrls.put(WrsisPortalURLs.MANAGE_MASTER12, "addregion");
		mapUrls.put(WrsisPortalURLs.MANAGE_MASTER13, "addregion");
		mapUrls.put(WrsisPortalURLs.MANAGE_MASTER14, "addregion");
		
		mapUrls.put(WrsisPortalURLs.MANAGE_MASTER15, "addzone");
		mapUrls.put(WrsisPortalURLs.MANAGE_MASTER16, "addzone");
		mapUrls.put(WrsisPortalURLs.MANAGE_MASTER17, "addzone");
		
		mapUrls.put(WrsisPortalURLs.MANAGE_MASTER18, "addworeda");
		mapUrls.put(WrsisPortalURLs.MANAGE_MASTER19, "addworeda");
		mapUrls.put(WrsisPortalURLs.MANAGE_MASTER20, "addworeda");
		
		mapUrls.put(WrsisPortalURLs.MANAGE_MASTER21, "addkebele");
		mapUrls.put(WrsisPortalURLs.MANAGE_MASTER22, "addkebele");
		mapUrls.put(WrsisPortalURLs.MANAGE_MASTER23, "addkebele");
		
		mapUrls.put(WrsisPortalURLs.MANAGE_MASTER24, "wheatMaster");
		mapUrls.put(WrsisPortalURLs.MANAGE_MASTER25, "wheatMaster");
		mapUrls.put(WrsisPortalURLs.MANAGE_MASTER26, "wheatMaster");
		
		mapUrls.put(WrsisPortalURLs.MANAGE_MASTER27, "wheatDifferentialLineMaster");
		mapUrls.put(WrsisPortalURLs.MANAGE_MASTER28, "wheatDifferentialLineMaster");
		mapUrls.put(WrsisPortalURLs.MANAGE_MASTER29, "wheatDifferentialLineMaster");

		mapUrls.put(WrsisPortalURLs.MANAGE_MASTER30, "raceIdentificationChart");
		mapUrls.put(WrsisPortalURLs.MANAGE_MASTER31, "raceIdentificationChart");
		mapUrls.put(WrsisPortalURLs.MANAGE_MASTER32, "raceIdentificationChart");
		
		mapUrls.put(WrsisPortalURLs.MANAGE_MASTER33, "configureResearchCenterMaster");
		mapUrls.put(WrsisPortalURLs.MANAGE_MASTER34, "configureResearchCenterMaster");
		mapUrls.put(WrsisPortalURLs.MANAGE_MASTER35, "configureResearchCenterMaster");
		
		mapUrls.put(WrsisPortalURLs.MANAGE_MASTER36, "seasonMaster");
		mapUrls.put(WrsisPortalURLs.MANAGE_MASTER37, "seasonMaster");
		mapUrls.put(WrsisPortalURLs.MANAGE_MASTER38, "seasonMaster");
		
		mapUrls.put(WrsisPortalURLs.MANAGE_MASTER39, "deseaseMaster");
		mapUrls.put(WrsisPortalURLs.MANAGE_MASTER40, "deseaseMaster");
		mapUrls.put(WrsisPortalURLs.MANAGE_MASTER41, "deseaseMaster");
		
		mapUrls.put(WrsisPortalURLs.MANAGE_MASTER42, "questionnaireMaster");
		mapUrls.put(WrsisPortalURLs.MANAGE_MASTER43, "questionnaireMaster");
		mapUrls.put(WrsisPortalURLs.MANAGE_MASTER44, "questionnaireMaster");
		
		mapUrls.put(WrsisPortalURLs.MANAGE_MASTER45, "addUserUserMaster");
		mapUrls.put(WrsisPortalURLs.MANAGE_MASTER46, "addUserUserMaster");
		mapUrls.put(WrsisPortalURLs.MANAGE_MASTER47, "addUserUserMaster");
		
		mapUrls.put(WrsisPortalURLs.MANAGE_MASTER48, "adddesignation");
		mapUrls.put(WrsisPortalURLs.MANAGE_MASTER49, "adddesignation");
		mapUrls.put(WrsisPortalURLs.MANAGE_MASTER50, "adddesignation");
		
		mapUrls.put(WrsisPortalURLs.MANAGE_MASTER52, "addOrganization");
		mapUrls.put(WrsisPortalURLs.MANAGE_MASTER53, "addOrganization");
		mapUrls.put(WrsisPortalURLs.MANAGE_MASTER54, "addOrganization");
		
		mapUrls.put(WrsisPortalURLs.MANAGE_MASTER55, "addCommittee");
		mapUrls.put(WrsisPortalURLs.MANAGE_MASTER56, "addCommittee");
		mapUrls.put(WrsisPortalURLs.MANAGE_MASTER57, "addCommittee");
		
		mapUrls.put(WrsisPortalURLs.MANAGE_MASTER58, "demographicTagging");
		mapUrls.put(WrsisPortalURLs.MANAGE_MASTER59, "demographicTagging");
		mapUrls.put(WrsisPortalURLs.MANAGE_MASTER72, "demographicTagging");
		
		mapUrls.put(WrsisPortalURLs.MANAGE_MASTER60, "addApprovalMaster");
		mapUrls.put(WrsisPortalURLs.MANAGE_MASTER61, "addApprovalMaster");
		mapUrls.put(WrsisPortalURLs.MANAGE_MASTER62, "addApprovalMaster");
		
		mapUrls.put(WrsisPortalURLs.MANAGE_MASTER63, "addApprovalAuthority");
		
		mapUrls.put(WrsisPortalURLs.MANAGE_MASTER64, "addJobDeligation");
		mapUrls.put(WrsisPortalURLs.MANAGE_MASTER65, "addJobDeligation");
		mapUrls.put(WrsisPortalURLs.MANAGE_MASTER66, "addJobDeligation");
		
		
		//Not mapped-->
		 mapUrls.put(WrsisPortalURLs.MANAGE_MASTER67, "addVariety");
		 mapUrls.put(WrsisPortalURLs.MANAGE_MASTER68, "addVariety");
		 mapUrls.put(WrsisPortalURLs.MANAGE_MASTER69, "addVariety");
		 mapUrls.put(WrsisPortalURLs.MANAGE_MASTER70, "changePassword");
		 
		 mapUrls.put(WrsisPortalURLs.MANAGE_MASTER71, "userProfile");
		 

		 
		mapUrls.put(WrsisPortalURLs.MANAGE_MASTER73, "raceScoringMaster");
		mapUrls.put(WrsisPortalURLs.MANAGE_MASTER74, "raceScoringMaster");
		mapUrls.put(WrsisPortalURLs.MANAGE_MASTER75, "raceScoringMaster");
		
		mapUrls.put(WrsisPortalURLs.MANAGE_MASTER76, "addYellowRacePhenotype");
		mapUrls.put(WrsisPortalURLs.MANAGE_MASTER77, "addYellowRacePhenotype");
		mapUrls.put(WrsisPortalURLs.MANAGE_MASTER78, "addYellowRacePhenotype");
		
		
			
		//addVariety
		
		
		//Not mapped-->
		
		mapUrls.put(WrsisPortalURLs.MANAGE_SURVEY, "addSurveyDetails");
		mapUrls.put(WrsisPortalURLs.MANAGE_SURVEY1, "addSurveyDetails");
		mapUrls.put(WrsisPortalURLs.MANAGE_SURVEY2, "addSurveyDetails");
		mapUrls.put(WrsisPortalURLs.MANAGE_SURVEY2A, "addSurveyDetails");
		mapUrls.put(WrsisPortalURLs.MANAGE_SURVEY3, "addSurveyDetails");
		mapUrls.put(WrsisPortalURLs.MANAGE_SURVEY3A, "addSurveyDetails");
		mapUrls.put(WrsisPortalURLs.MANAGE_SURVEY21, "addSurveyDetails");
		mapUrls.put(WrsisPortalURLs.MANAGE_SURVEY22, "addSurveyDetails");
		
		
		
		mapUrls.put(WrsisPortalURLs.MANAGE_SURVEY4, "uploadSurveyDataXcel");
		mapUrls.put(WrsisPortalURLs.MANAGE_SURVEY23, "uploadSurveyDataXcel");
		mapUrls.put(WrsisPortalURLs.MANAGE_SURVEY24, "uploadSurveyDataXcel");
		mapUrls.put(WrsisPortalURLs.MANAGE_SURVEY7, "uploadSurveyDataXcel");
		mapUrls.put(WrsisPortalURLs.MANAGE_SURVEY4A, "uploadSurveyDataXcel");
		
		mapUrls.put(WrsisPortalURLs.MANAGE_SURVEY25, "rustSurveyData");
		mapUrls.put(WrsisPortalURLs.MANAGE_SURVEY25A, "rustSurveyData");
		mapUrls.put(WrsisPortalURLs.MANAGE_SURVEY26, "rustSurveyData");
		
		
		mapUrls.put(WrsisPortalURLs.MANAGE_SURVEY6, "pendingsurvey");
		mapUrls.put(WrsisPortalURLs.MANAGE_SURVEY6A, "pendingsurvey");
		mapUrls.put(WrsisPortalURLs.MANAGE_SURVEY6B, "pendingsurvey");
		mapUrls.put(WrsisPortalURLs.MANAGE_SURVEY6C, "pendingsurvey");
		mapUrls.put(WrsisPortalURLs.MANAGE_SURVEY8, "pendingsurvey");
		mapUrls.put(WrsisPortalURLs.MANAGE_SURVEY9, "pendingsurvey");
		
		
		
		mapUrls.put(WrsisPortalURLs.MANAGE_SURVEY12, "uploadIvrData");
		mapUrls.put(WrsisPortalURLs.MANAGE_SURVEY13, "uploadIvrData");
		mapUrls.put(WrsisPortalURLs.MANAGE_SURVEY14, "uploadIvrData");
		mapUrls.put(WrsisPortalURLs.MANAGE_SURVEY14A, "uploadIvrData");
		
		mapUrls.put(WrsisPortalURLs.MANAGE_SURVEY5, "wrSurveySummaryReports");
		mapUrls.put(WrsisPortalURLs.MANAGE_SURVEY5A, "wrSurveySummaryReports");
		
		mapUrls.put(WrsisPortalURLs.MANAGE_SURVEY15, "rustIncidentView");
		mapUrls.put(WrsisPortalURLs.MANAGE_SURVEY16, "rustIncidentView");
		
		mapUrls.put(WrsisPortalURLs.MANAGE_SURVEY17, "rustSurveyData");

		mapUrls.put(WrsisPortalURLs.MANAGE_SURVEY11, "addSurveyDetails");
		
		mapUrls.put(WrsisPortalURLs.MANAGE_SURVEY19, "viewRustIncident");
		
		mapUrls.put(WrsisPortalURLs.MANAGE_SURVEY20, "uploadIvrData");
		
		mapUrls.put(WrsisPortalURLs.MANAGE_SURVEY27, "addNotification");
		mapUrls.put(WrsisPortalURLs.MANAGE_SURVEY27A, "addNotification");
		mapUrls.put(WrsisPortalURLs.MANAGE_SURVEY27B, "addNotification");
		mapUrls.put(WrsisPortalURLs.MANAGE_SURVEY27C, "addNotification");
		mapUrls.put(WrsisPortalURLs.MANAGE_SURVEY27D, "addNotification");
		
		mapUrls.put(WrsisPortalURLs.MANAGE_SURVEY14B, "ivrSurveyAPIData");
		
		
		mapUrls.put(WrsisPortalURLs.MANAGE_LINK, "addGlobalLink");
		mapUrls.put(WrsisPortalURLs.MANAGE_LINK1, "addGlobalLink");
		mapUrls.put(WrsisPortalURLs.MANAGE_LINK2, "addGlobalLink");
		mapUrls.put(WrsisPortalURLs.MANAGE_LINK3, "addPrimaryLink");
		mapUrls.put(WrsisPortalURLs.MANAGE_LINK4, "addPrimaryLink");
		mapUrls.put(WrsisPortalURLs.MANAGE_LINK5, "addPrimaryLink");
		mapUrls.put(WrsisPortalURLs.MANAGE_LINK6, "addFunctionMaster");
		mapUrls.put(WrsisPortalURLs.MANAGE_LINK7, "addFunctionMaster");
		mapUrls.put(WrsisPortalURLs.MANAGE_LINK8, "addFunctionMaster");
		mapUrls.put(WrsisPortalURLs.MANAGE_LINK9, "addRoleMaster");
		mapUrls.put(WrsisPortalURLs.MANAGE_LINK10, "addRoleMaster");
		mapUrls.put(WrsisPortalURLs.MANAGE_LINK11, "addRoleMaster");
		
	    mapUrls.put(WrsisPortalURLs.MANAGE_LINK12, "addUserPermission");
	    mapUrls.put(WrsisPortalURLs.MANAGE_LINK13, "addUserPermission");
	    mapUrls.put(WrsisPortalURLs.MANAGE_LINK14, "addUserPermission");
	    
	    mapUrls.put(WrsisPortalURLs.LOG_REPORTS, "viewSucessWebLoginDetails");
	    mapUrls.put(WrsisPortalURLs.LOG_REPORTS1, "viewSucessWebLoginDetails");
	    mapUrls.put(WrsisPortalURLs.LOG_REPORTS2, "viewMobileLogDetails");
	    
	    mapUrls.put(WrsisPortalURLs.LOG_REPORTS3, "viewGisFileLog");
	    mapUrls.put(WrsisPortalURLs.LOG_REPORTS3A, "viewGisFileLog");
	
	    mapUrls.put(WrsisPortalURLs.LOG_REPORTS4, "viewQueryBuilderLog");
	    mapUrls.put(WrsisPortalURLs.LOG_REPORTS5, "viewIvrApiLogData");
	    mapUrls.put(WrsisPortalURLs.LOG_REPORTS6, "gisForcastingFile");
		 
	}
	
	
	@Bean
	public static PropertySourcesPlaceholderConfigurer propertyConfigInDev() {
		return new PropertySourcesPlaceholderConfigurer();
	}
    
	@Bean
	public MessageSource messageSource() {
		ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
		messageSource.addBasenames("classpath:messages");
		messageSource.setDefaultEncoding("UTF-8");
		return messageSource;
	}
	
	    @Bean
	    public LocalValidatorFactoryBean validator() {
	        LocalValidatorFactoryBean bean = new LocalValidatorFactoryBean();
	        bean.setValidationMessageSource(messageSource());
	        return bean;
	    }

	 @Override
	public void addInterceptors(InterceptorRegistry registry) {
		
		registry.addInterceptor(handlerInterceptor()).excludePathPatterns("/","/login","/home","/forgotPassword","/firstTimeLogin","/viewProfilePhoto","/firstTimeChangePassword","/passwordReset",
				"getUserByRoleAndOrgId/","/logout","/getMinistryList","/getFungicideDetails", "/privacy","/aboutus","/surveymap","/downLoadLatestApk",
				"/webapp/**", "/WEB-INF/**", "/resources/**","/templates/**", "/jsp/**", "/tiles/**", 
	    		"/styles/**", "/images/**", "/scripts/**", "/fonts/**", "/pdf/**", "/WRSISGIS/**",
	    		"/wrsis/**","/icons/**","/justifiedGallery/**","/Portal/**","/css/**","/img/**","/script/**","/api/**","/mobile/**","/pagejs/**","/gisapi/**","/external/**");
		WebMvcConfigurer.super.addInterceptors(registry);
	}
	
	 
	 
	@Bean
	public HandlerInterceptor handlerInterceptor() {
		
		return new HandlerInterceptor() {
			
			
			
			
			@Override
			public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
					throws Exception {
				
				
				String userPermission = null;
				HttpSession session = request.getSession();
				
				List<String> userPermissions=new ArrayList<>();
				Object obj =session.getAttribute(WrsisPortalConstant.USER_PERMISSIONS);
				if(obj!=null)
					userPermissions=(List)obj;
				if(userPermissions!=null || !userPermissions.isEmpty()) {
					userPermissions.add("Home");
					userPermissions.add("userProfile");
					userPermissions.add("changePassword");
					userPermissions.add("raceAnalysisReport");
					
					
				}
				
				String userAction = request.getRequestURI();
				
				if(userAction.contains("/WRSIS")) {
					userAction=userAction.replace("/WRSIS", "");
				}
					
				

		        Iterator iter = mapUrls.entrySet().iterator();
				while (iter.hasNext()) {
					Map.Entry mEntry = (Map.Entry) iter.next();
					String key=(String)mEntry.getKey();
					if(userAction.equals(key)){
						userPermission=(String)mEntry.getValue();
						session.setAttribute("viewUrl", userAction.replace("/",""));
						session.setAttribute("surl", userPermission);
						
						
						if(!userPermissions.contains(userPermission)) {

							session.invalidate();
							response.sendRedirect("/WRSIS");
							return false;
						}
						
						break;
					}
				}
				
				
				
				try {
				
					
					if(session.getAttribute(WrsisPortalConstant.LEFT_MENU_PERMISSION) == null ||
							session.getAttribute(WrsisPortalConstant.USER_ID) == null || session.getAttribute(WrsisPortalConstant.USER_ID).toString().isEmpty()) {
					response.sendRedirect("/WRSIS");
					return false;
					}
					
					
							
					if(session.getAttribute(WrsisPortalConstant.LEFT_MENU_PERMISSION) instanceof  Map<? , ?> ){
						@SuppressWarnings("unchecked")
						Map<String, List<PrimaryLinkVo>> leftmenu = (Map<String, List<PrimaryLinkVo>>)session.getAttribute(WrsisPortalConstant.LEFT_MENU_PERMISSION);
						 if(leftmenu.size()==0) {
						 response.sendRedirect("/WRSIS");
								return false;
						 }
					 }
					

					
				} catch (Exception e) {
					
				}
				return HandlerInterceptor.super.preHandle(request, response, handler);

			}
		};
	}
	
	
	
	
}
