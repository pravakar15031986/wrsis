package com.csmpl.adminconsole.webportal.config;

import com.google.common.cache.Cache;
import java.io.IOException;
import java.util.Arrays;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
@Order(2)
//@WebFilter( filterName = "validationfilter",  urlPatterns = { "/viewSucessWebLoginDetails"})
public class ValidateSalt extends OncePerRequestFilter  {

    
    @Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
    	
        // Assume its HTTP
        HttpServletRequest httpReq = (HttpServletRequest) request;
       
        // Get the salt sent with the request
        String salt = (String) httpReq.getParameter("csrf_token_");

        // Validate that the salt is in the cache
        Cache<String, Boolean> csrfPreventionSaltCache = (Cache<String, Boolean>)
            httpReq.getSession(false).getAttribute("csrf_token_repo_cache");

        if (csrfPreventionSaltCache != null &&
                salt != null &&
                csrfPreventionSaltCache.getIfPresent(salt) != null){
        		
        	csrfPreventionSaltCache.invalidate(salt);
            // If the salt is in the cache, we move on
        	filterChain.doFilter(request, response);
        } else {
            // Otherwise we throw an exception aborting the request flow
            throw new ServletException("CSRF attack detected!! Inform to sys admin ASAP.");
        }
    }
    
    public static boolean substringExistsInArray(String inputStr, String[] items) {
        return Arrays.stream(items).parallel().anyMatch(inputStr::contains);
     }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
    	String[] exludeURLs= {"/home","/forgotPassword","/firstTimeLogin","/viewProfilePhoto","/firstTimeChangePassword","/passwordReset","/addTagSample","/raceAnalysisDetails","/getNotificationUserByRoleAndOrgId","/published-discard-monitor","/monitorRecommendationPublished",
					"getUserByRoleAndOrgId/","/logout","/getMinistryList","/getFungicideDetails","/api/getMasterDataforGIS","/getUnreadNtfsByUserId","/incidence-Severity-RustReportExcel","/allocationforrestanalysis","/getAuthorityData","/getUsersByRoleId",
					"/webapp/", "/WEB-INF/", "/resources/","/templates/", "/jsp/", "/tiles/","/getDesktopNotificationsByUserId","/getDemographicListZone","/getDemographicListKebele","/gisapi/getLatestGISData", "/dumpSampleTagDetails","/addApprovalAuthorityStoring",
		    		"/styles/", "/images/", "/scripts/", "/fonts/", "/pdf/", "/WRSISGIS/","/thymeleaf/Captcha.jpg","/getReactionList","/getDemographicListWoreda","/gisapi/getMasterDataForGISMap","/tagsamplePdfDownload","/getUsersByRoleId","/getUserResInfo",
		    		"/api/fetchSampleCollected","/api/checkDuplicateSampleIds","/api/getSurveyDataforGIS","/updateNtfReadStatus","/getDesktopNotificationsByUserIdLoadMore","/gisapi/getGISHistoryData","/recommendation-file-download","/raceAnalysisNewDetails",
		    		"/wrsis/","/icons/","/justifiedGallery/","/Portal/","/css/","/img/","/script/","/api/","/mobile/","/pagejs/","/gisapi/","/external/","/api/getMasterforRustIncidentMap","/tagsampleExcelDownload","/searchGisForcastingFile","/getAuthorityStatusEdit",
		    		"/updateGroupPermission","/searchViewGisFileLog","/downloadSurveyExcelFormat","/downloadSurveyPDF","/filterSurveyViewDatatable","/survey-publish-excel-download","/getNotificationMemberDetails","/gisapi/getRustSurveyDataForGlobalRust",
		    		"/downloadsurveypublishpdf","/survey-publish-excel-download","/survey-publish-pdf-download","/downloadsurveydiscardpdf","/surveyDiscardedReportDownload","/gisapi/uploadGisZipFile","/advisory-file-download","/uploadExcelFileRedData",
		    		"/searchPrevelanceeport","/searchReactionReport","/implementationSummaryReportExcelDownload","/implementationSummaryReportSearch","/implementationReportSearch","/implementationReportExcelDownload","/raceAnalysisNewDetailsView","/rust-report-pdf-download",
		    		"/raceByVarietyReportExcelDownload","/searcharietyReport","/stemRustRaces-IdentifiedCroppingSearch","/downloadReportPdf","/viewReportDetails","/reportSurveyRustOtherDiseaseCSV","/reportSurveyRustOtherDiseaseExcel","/getUserReqInfo",
		    		"/reportSurveyReportByGlobalRustExcel","/reportSurveyReportByGlobalRustCsv","/downloadPendingSurveyExcel","/downloadPendingSurveyPdf","/downloadPublishedSurveyExcel","/downloadPublishedSurveyPdf","/raceAnalysisFinalResult","/downloadDashBoardRustIncidentExcel",
		    		"/downloadDiscardSurveyExcel","/downloadDiscardSurveyPdf","/ivr-survey-data-excel-download","/ivr-survey-data-pdf-download","/downloadRustIncidentExcel","/downloadViewRustIncidentPdf","/getAuthorityStatus","/ivr-data-excel-data-count",
		    		"/getUsersByLevel","/surveyorPdfDownload","/surveyorExcelDownload","/rust-report-excel-download","/raceReport-excel-download","/downloadRaceAnalysisPDF","/downloadMonitorDataPDF","/monitorData-excel-download","/getMemberDetails",
		    		"/showViewByExcelFileId","/surveyExcelUpload","/saveSurveyDetailsSingle","/getUserQueryInfo","/getRecLocDetails","/deleteMonitorDetails","/mobileLoginExcel","/mobileLoginPdf","/downloadSucessWebLoginDetailsPdf","/viewSurveyDetailsById",
		    		"/api/userLogin","/api/getAllMasterData","/api/incidentQuestions","/api/rustIncident","/api/getSurveyDataforGIS","/api/saveWheatRustSurvey","/api/viewWheatRustSurvey","/api/viewWheatRustSurveyDetail","/downloadSucessWebLoginDetailsExcel",
		    		"/api/viewRaceAnalysis","/api/viewRecommendationAndAdvisory","/api/getMasterforRustIncident","/api/saveSurveyImplementation","/api/viewRustIncident","/api/getMasterforSurveyImplementation","/api/viewRecommendationData","/downloadRustIncidentPDF",
		    		"/api/userProfile","/api/viewImplementation","/api/viewDetailsImplementation","/api/viewRaceAnalysisDetails","/api/viewDashBoardCount","/api/viewRustIncidentForGis","/api/viewRustIncidentByList","/api/viewIncidentDetailsByIncidentid",
		    		"/api/viewNotificationByUser","/api/updateNotificationStatus","/api/mobileUserLogout","/gisapi/getUKMetSurveyData","/editGrowthStage","/downloadRaceFile","/downloadPathologistReportExcel","/downloadPathologistReportPdf","/downloadGisFile",
		    		"/downloadWoredaExpertReportPdf","/downloadWoredaExpertReportExcel","/downloadDevAgentReportPdf","/downloadDevAgentReportExcel","/downloadRecomendationSurveyDataDetailsWePublishedPdf","/downloadRecomendationSurveyDataDetailsWePublishedExcel",
		    		"/downloadRecomendationSurveyDataDetailsWeDiscardPdf","/downloadRecomendationSurveyDataDetailsWeDiscardExcel","/downloadRaceAnalysisStatusExcel","/downloadRaceAnalysisStatusPdf","/viewRaceAnalysisStatus","/getUserByRoleAndOrgId",
		    		"/getRustTypeChartReportAjax" ,"/getCurrentYearSeason","/getSurveyIvrIncidentChartReportAjax","/getRecomChartReportAjax","/api/getMobileVersion","/api/setMobileVersion","/api/getMobileToken","/api/getSurveyMasterData",
		    		"/getDemographicListZoneinRecom","/getDemographicListWoredainRecom","/getKebeleListInRecom","/gisapi/uploadIvrDataAPI","/api/checkDuplicateSampleId","/getUsersCountByAjax","/getTransactionCountByAjax","/raceAnalysisReportPagination",
		    		"/getRecSummaryDetails","/pendingrecommendation","/getRecLocDetailsForBoa","/getRecLocDetailsForwardedByBoa","/gisapi/getUKMetRawSurveyData","/gisapi/getStyleDetails","/gisapi/uploadAPKZipFile","/viewsurvey","/getAdvisoryLatestRecord",
		    		"/getRecomLatestRecord","/gisapi/getGISIVRdata","/gisapi/getIVRCountDetails","/api/saveMultiSurveyData"};

		
		if("GET".equalsIgnoreCase(request.getMethod())) {
			return true;
		}
		
    	
        String path = request.getServletPath();
        System.out.println(path+"................servlt path in csrf......"+Arrays.asList(exludeURLs).contains(path));
      
        return Arrays.asList(exludeURLs).contains(path);
    }
    
    
}
