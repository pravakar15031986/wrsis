<link href="wrsis/css/dashboard.css" rel="stylesheet">
<script src="wrsis/js/dashboard.js"></script>
<script src="https://code.highcharts.com/highcharts.js"></script>
<script src="https://code.highcharts.com/modules/exporting.js"></script> 
 <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<div class="mainpanel">
            <div class="section">
               <div class="top-portlets">
                  <div class="dashboard-title">
                  	<h6>User Details</h6>
                     <hr>
                  </div>
                 <div class="row my-2">
                     <div class="col-12 col-sm-6 col-xl-3">
                         <div class="portlets__card portlets__card-border " title="Surveyors">
                            <div class="portlets__card__count mx-auto mb-2 dashboardcolor1" >
                                 <a href="surveyorReport"  ></a>
                                 <span class="number" id="surveyorcount"></span>
                            </div>
                             <b>Surveyors</b>
                         </div>

                     </div>
                    
                     <div class="col-12 col-sm-6 col-xl-3">
                         <div class="portlets__card portlets__card-border " title="Pathologists">
                            <div class="portlets__card__count mx-auto mb-2 dashboardcolor2">
                                <a href="pathologistReport" ></a>
                                <span class="number" id="pathologistcount"></span>
                            </div>
                            <b>Pathologists/Race Analyst</b>
                         </div>

                     </div>
                     <div class="col-12 col-sm-6 col-xl-3">
                        <div class="portlets__card portlets__card-border" title="Woreda Experts">
                            <div class="portlets__card__count mx-auto mb-2 dashboardcolor3" >
                               <a href="woredaExpertsReport" ></a>
                               <span class="number" id="woredacount"></span>
                            </div>
                            <b>Woreda Experts</b>
                         </div>

                     </div>
                     <div class="col-12 col-sm-6  col-xl-3">
                        <div class="portlets__card " title="Development Agents">
                            <div class="portlets__card__count mx-auto mb-2 dashboardcolor4" >
                                  <a href="devAgentReport" ></a>
                                  <sapn class="number" id="developmentcount"></sapn>
                            </div>
                            <b>Development Agents</b>
                         </div>

                     </div>
                    
                  </div>
               </div>
               <hr>
               <div class="top-portlets">
                 
                  	<h6 class="mb-3">Statistics for the current season</h6>
                  	<!-- <h6 class="mb-3">Statistics for the current season&nbsp;(<span id="currentseason"></span>)</h6> -->
                 
                  <div class="row">
                  
                     <div class="col-12 col-sm-6 col-xl-3">
                        <div class="portlets__card ">
                            <div class="portlets__card__content dashboardbg1 d-flex py-2">
                                <div class="portlets__card-icon mr-4 mt-2">
                                    <i class="fa fa-file-text-o"></i>
                                </div>
                                <div class="portlets__card-details text-left">
                                   <a href="rustReport" id="rustcount" class="dashnumber"></a>
                                   <p class="countTitle"><a href="rustReport" class="text-white">Rust Survey</p></a></p>
                                </div>
                            </div>
                          
                        </div>
                     </div>
                     <div class="col-12 col-sm-6 col-xl-3">
                        <div class="portlets__card ">
                          <div class="portlets__card__content dashboardbg2 d-flex py-2">
                              <div class="portlets__card-icon mr-4 mt-2">
                                  <i class="fa fa-sellsy"></i>
                              </div>
                              <div class="portlets__card-details text-left">
                                <a href="raceAnalysisReport" id="racecount"  class="dashnumber"></a>
                                 <p class="countTitle"><a href="raceAnalysisReport" class="text-white">Race Analysis</a></p>
                              </div>
                          </div>

                        </div>
                     </div>
                     <div class="col-12 col-sm-6  col-xl-3">
                        <div class="portlets__card ">
                            <div class="portlets__card__content dashboardbg3 d-flex py-2">
                                <div class="portlets__card-icon mr-4 mt-2">
                                    <i class="fa fa-exclamation-triangle"></i>
                                </div>
                                <div class="portlets__card-details text-left">
                                  <a href="dashboardRustIncident" id="ivrcount"  class="dashnumber"></a>
                                   <p class="countTitle"><a href="dashboardRustIncident" class="text-white">Rust Incident</a></p>
                                </div>
                            </div>

                        </div>
                     </div>
                     <div class="col-12 col-sm-6 col-xl-3">
                        <div class="portlets__card ">
                            <div class="portlets__card__content dashboardbg4 d-flex py-2">
                                <div class="portlets__card-icon mr-4 mt-2">
                                    <i class="fa fa-server"></i>
                                </div>
                                <div class="portlets__card-details text-left">
                                   <a href="monitorDataReport" id="monitoringcount"  class="dashnumber"></a>
                                   <p class="countTitle"><a href="monitorDataReport" class="text-white">Monitoring Data</a></p>
                                </div>
                            </div>

                        </div>
                     </div>
                   
                  </div>
               </div>
               <div class="row">
                  <div class="col-12 col-md-6 col-lg-4">
                     <div class="dashboard-portlet">
                     	<h6>Rust Type Statistics 
                     		<a class="float-right btn btn-outline-secondary btn-sm shadow-none" title="Click to view details" href="rustTypeChartReport" >
	                     	<span class="fa fa-external-link"></span> </a>
	                     	<a class="float-right btn btn-outline-secondary btn-sm shadow-none p-search1" href="javascript:void(0)" id="rustChartSearch"><span class="fa fa-search "></span></a>
                     		
                           </h6>
                           <div class="portlet-filter portletf1">
                           <form>
                           <div class="form-group">
								    <label class="">Search By :</label>
								    <div class="radio">
									  <input type="radio" name="rustStatus" value="0" class="magic-radio sampleyes" id="radioSeasonRust" onclick="changeRustSearchBy(this.value)">
									  <label for="radioSeasonRust" tabindex="4">Season</label>  
							   			<input type="radio" name="rustStatus" value="1" class="magic-radio sampleno" id="radioDateRust" onclick="changeRustSearchBy(this.value)">
									  <label for="radioDateRust" tabindex="5">Date Range</label>
								   </div>
								  </div>
                           
								  <div class="form-group rustSeason">
								    <label >Year</label> <select class="form-control" id="dropdownYearRust"
									name="rustyear">
									<c:forEach items="${year}" var="vo">
										<option value="${vo}">${vo}</option>
									</c:forEach>
								</select>
							</div>
								  <div class="form-group rustSeason">
								    <label>Season</label> <select class="form-control" id="seasonIdRust"
									name="rustSeasonId">
									<c:forEach items="${seasons}" var="vo">
										<option value="${vo.intSeasoonId}">${vo.vchSeason}</option>
									</c:forEach>
								</select>
							</div>
								  <div class="form-group rustDate">
								  <label >Survey Date From<span class="text-danger">*</span></label>
										<div class="input-group col-sm-12">
                                      <input type="text" class="form-control datepicker" data-date-end-date="0d" name="rustDateFrom"  aria-label="Default" aria-describedby="inputGroup-sizing-default" id="rustDateFrom"/>
                                       <div class="input-group-append">
											<span class="input-group-text" id="inputGroup-sizing-default"><i class="icon-calendar1"></i></span>
										</div>
                                    </div>
								  </div>
								  <div class="form-group rustDate">
								  <label>Survey Date To<span class="text-danger">*</span></label>
								   <div class="input-group col-sm-12">
                                      <input type="text" class="form-control datepicker" data-date-end-date="0d" name="rustDateTo"  aria-label="Default" aria-describedby="inputGroup-sizing-default" id="rustDateTo"/>
                                       <div class="input-group-append">
											<span class="input-group-text" id="inputGroup-sizing-default"><i class="icon-calendar1"></i></span>
										</div>
                                    </div>
								  </div>
								 
								  <button type="button" class="btn btn-primary" id="showRustTypeChart">Show</button>
								</form>
                           </div>
                           <div id="chartContainerPai" style="height: 290px; width: 100%;"></div>
                     </div>
                  </div>
                  <div class="col-12 col-md-6 col-lg-4">
                     <div class="dashboard-portlet">
                     	<h6>Survey, IVR and Incident 
                     	
                     	<a class="float-right btn btn-outline-secondary btn-sm shadow-none" title="Click to view details" href="surveyAndIvrReport" >
	                     	<span class="fa fa-external-link"></span> </a>
	                     	<a class="float-right btn btn-outline-secondary btn-sm shadow-none p-search2" href="javascript:void(0)" id="surveyChartSearch"><span class="fa fa-search "></span></a>
                           </h6>
                           <div class="portlet-filter portletf2">
                           <form>
                           <div class="form-group">
								    <label >Search By :</label>
								    <div class="radio">
									  <input type="radio" name="surveyStatus" value="0" class="magic-radio sampleyes" id="radioSeasonSurvey" onclick="changeSurveySearchBy(this.value)">
									  <label for="radioSeasonSurvey" tabindex="4">Season</label>  
							   			<input type="radio" name="surveyStatus" value="1" class="magic-radio sampleno" id="radioDateSurvey" onclick="changeSurveySearchBy(this.value)">
									  <label for="radioDateSurvey" tabindex="5">Date Range</label>
								   </div>
								  </div>
                           
								  <div class="form-group surveySeason">
								    <label >Year</label>
										   <select class="form-control" id="dropdownYearSurvey" name="dropdownYearSurvey">
									<c:forEach items="${year}" var="vo">
										<option value="${vo}">${vo}</option>
									</c:forEach>
								</select>
								  </div>
								  <div class="form-group surveySeason">
								    <label>Season</label>
								    <select class="form-control" name="seasonIdSurvey" id="seasonIdSurvey">
									<c:forEach items="${seasons}" var="vo">
										<option value="${vo.intSeasoonId}">${vo.vchSeason}</option>
									</c:forEach>
								</select>
								  </div>
								 
								 <div class="form-group surveyDate">
								  <label >Survey Date From<span class="text-danger">*</span></label>
										<div class="input-group col-sm-12">
                                      <input type="text" class="form-control datepicker" data-date-end-date="0d" name="surveyDateFrom"  aria-label="Default" aria-describedby="inputGroup-sizing-default" id="surveyDateFrom"/>
                                       <div class="input-group-append">
											<span class="input-group-text" id="inputGroup-sizing-default"><i class="icon-calendar1"></i></span>
										</div>
                                    </div>
								  </div>
								  <div class="form-group surveyDate">
								  <label>Survey Date To<span class="text-danger">*</span></label>
								   <div class="input-group col-sm-12">
                                      <input type="text" class="form-control datepicker" data-date-end-date="0d" name="surveyDateTo"  aria-label="Default" aria-describedby="inputGroup-sizing-default" id="surveyDateTo"/>
                                       <div class="input-group-append">
											<span class="input-group-text" id="inputGroup-sizing-default"><i class="icon-calendar1"></i></span>
										</div>
                                    </div>
								  </div>
								  <button type="button" class="btn btn-primary" id="showSurveyChart">Show</button>
								</form>
                           </div>
                            <div id="chartContainerBar" class="dashboard-portlet__content__chart" style="height: 290px; width: 100%;"></div> 
                           
                     </div>
                  </div>
                  <div class="col-12 col-md-6 col-lg-4">
                     <div class="dashboard-portlet">
                     	<h6>Rust vs Recom Survey<a class="float-right btn btn-outline-secondary btn-sm shadow-none" title="Click to view details" href="rustSurveyAndRecommentationSurveyChart" >
	                     	<span class="fa fa-external-link"></span> </a>
	                     	<a class="float-right btn btn-outline-secondary btn-sm shadow-none p-search3" href="javascript:void(0)" id="rustRecomSearch"><span class="fa fa-search "></span></a>
                           </h6>
                           <div class="portlet-filter portletf3">
                           <form>
                           <div class="form-group">
								    <label >Search By :</label>
								    <div class="radio">
									  <input type="radio" name="recStatus" value="0" class="magic-radio sampleyes" id="radioSeasonRec" onclick="changeRecSearchBy(this.value)">
									  <label for="radioSeasonRec" tabindex="4">Season</label>  
							   			<input type="radio" name="recStatus" value="1" class="magic-radio sampleno" id="radioDateRec" onclick="changeRecSearchBy(this.value)">
									  <label for="radioDateRec" tabindex="5">Date Range</label>
								   </div>
								  </div>
                           
								  <div class="form-group recSeason">
								    <label >Year</label>
										   <select class="form-control" id="dropdownYearRec" name="dropdownYearRec">
									<c:forEach items="${year}" var="vo">
										<option value="${vo}">${vo}</option>
									</c:forEach>
								</select>
								  </div>
								  <div class="form-group recSeason">
								    <label>Season</label>
								    <select class="form-control" id="seasonIdRec">
									<c:forEach items="${seasons}" var="vo">
										<option value="${vo.intSeasoonId}">${vo.vchSeason}</option>
									</c:forEach>
								</select>
								  </div>
								  
								  <div class="form-group recDate">
								  <label >Survey Date From<span class="text-danger">*</span></label>
										<div class="input-group col-sm-12">
                                      <input type="text" class="form-control datepicker" data-date-end-date="0d" name="recDateFrom"  aria-label="Default" aria-describedby="inputGroup-sizing-default" id="recDateFrom" autocomplete="off"/>
                                       <div class="input-group-append">
											<span class="input-group-text" id="inputGroup-sizing-default"><i class="icon-calendar1"></i></span>
										</div>
                                    </div>
								  </div>
								  <div class="form-group recDate">
								  <label>Survey Date To<span class="text-danger">*</span></label>
								   <div class="input-group col-sm-12">
                                      <input type="text" class="form-control datepicker" data-date-end-date="0d" name="recDateTo"  aria-label="Default" aria-describedby="inputGroup-sizing-default" id="recDateTo" autocomplete="off"/>
                                       <div class="input-group-append">
											<span class="input-group-text" id="inputGroup-sizing-default"><i class="icon-calendar1"></i></span>
										</div>
                                    </div>
								  </div>
								 
								  <button type="button" class="btn btn-primary" id="showRecChart">Show</button>
								</form>
                           </div>
                           <div id="chartContainerLine" style="height: 290px; width:100%; margin: 0px auto;"></div> 
                          
                     </div>
                  </div>
                 
               </div>
               
                <div class="row  my-2">
                  <div class="col-12 col-md-5">
                  <div class="dashboard-portlet portlet-two mb-3">
                     	<h6>Advisory</h6>
                            <div class="dashboard-portlet__content__chart ulgismap" >
							
	                         <table class="table table-bordered mb-0">
	                     	<tr id="advisorytr"><th>Advisory No.</th>
	                     	<th> Published Date</th>
	                     	<th width="120px" class="text-center">Download</th></tr>
	       	               <tbody id="documentsAdvisory">
			                </tbody>
	                      
	                         </table>
	                         
                            	
                            </div> 
                        </div>

                  </div>
                  <div class="col-12 col-md-7">
                  <div class="dashboard-portlet portlet-two mb-3">
                     	<h6>Recommendation</h6>
                            <div class="dashboard-portlet__content__chart ulgismap" >
                            
                             <table class="table table-bordered mb-0">
	                     	<tr id="recomtr"><th>Recom No.</th>
	                     	<th>Recom Published Date</th><th>Type of Recom</th>
	                     	<th width="120px" class="text-center">Download</th></tr>
	       	               <tbody id="documentsRecom">
			                </tbody>
	                      
	                         </table>
	                         
	                         
                            </div> 
                        </div>
                  </div>
                </div>
               <!-- MIS Report & GIS MAp -->
               <div class="row  my-2">
                  <div class="col-12 col-md-8 col-lg-8">
                     <div class="dashboard-portlet">
                     	<h6>MIS Report
                           </h6>
                           <div class="dashboard-portlet__content__chart ulmapbox" >
                           <ul>
	                           <li>
	                           		<a href="prevalenceReport" title="" >Rust Prevalence Report</a>
	                           </li>
	                           <li>
	                           		<a href="incidence-Severity-RustReport" title="" >Rust Incidence Report</a>
	                           </li>
	                           <li>
	                           		<a href="reaction-ToRustReport" title="" >Rust Reaction Report</a>
	                           </li>
	                           <li>
	                           		<a href="implementationReport" title="" >Implementation Report</a>
	                           </li>
	                           <li>
	                           		<a href="implementationSummaryReport" title="" >Implementation Summary</a>
	                           </li>
	                           <li>
	                           		<a href="stemRustRaces-DetectedCropping" title="" >Race By Variety</a>
	                           </li>
	                            <li>
	                           		<a href="stemRustRaces-IdentifiedCropping" title="" >Race By Sample</a>
	                           </li>
	                           <li>
	                           		<a href="virulence-SpectrumPgtRaces" title="" >Race By Virulence</a>
	                           </li>
	                           <li>
	                           		<a href="regionAndRustTypeReport" title="" >Region vs Rust Report</a>
	                           </li>
	                            <li>
	                           		<a href="surveyAndIvrReport" title="" >Survey vs IVR Report</a>
	                           </li>
	                           <li>
	                           		<a href="rustTypeChartReport" title="" >Rust Type Chart Report</a>
	                           </li>
	                           <li>
	                           		<a href="reportSurveyReportByGlobalRust" title="" >Global Rust Survey Report</a>
	                           </li>
	                            <li>
	                           		<a href="rustSurveyOtherDisease" title="" >Survey Other Disease</a>
	                           </li>
	                           <li>
	                           		<a href="rustSurveyAndRecommentationSurveyChart" title="" >Rust vs Recom Survey</a>
	                           </li>
                           </ul>
                            </div> 
                     </div>
                  </div>
                  <div class="col-12 col-md-4 col-lg-4">

                        <div class="dashboard-portlet">
                     	<h6>GIS Map</h6>
                            <div class="dashboard-portlet__content__chart ulgismap" >
                            	<ul>
			                           <li>
			                           		<a href="mapdetails" title="" ><i class="fa fa-map-o"></i> Survey Map</a>
			                           </li>
			                           <li>
			                           		<a href="IVRMapDetails" title="" ><i class="fa fa-map-o" aria-hidden="true"></i> IVR Map</a>
			                           </li>
			                           <li>
			                           		<a href="IncidentMapDetails" title="" ><i class="fa fa-exclamation-triangle"></i> Incident</a>
			                           </li>
			                           <li>
			                           		<a href="viewForecastingMap" title="" ><i class="fa fa-eercast" aria-hidden="true"></i> Forecasting Map</a>
			                           </li>
			                   </ul>
                            </div> 
                        </div>
                  </div>
                 </div>
               
               <!-- Rust Incidence Severity -->
               
               
               
            </div>
         </div>
         
         
         
                                 <!-- Modal -->
<div class="modal fade" id="locationModal" role="dialog">
	<div class="modal-dialog">

		<!-- Modal content-->
		<div class="modal-content">
			<div class="modal-header">
				<h4 class="modal-title">Location Details</h4>
			</div>

			<div class="modal-body">

				<table data-toggle="table" class="table table-hover table-bordered">
					<thead id="loctblHd">

					</thead>
					<tbody id="loctblBdy">

					</tbody>
				</table>
				<p id="locMsg"></p>

			</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
			</div>
		</div>

	</div>
</div>

<form action="recommendation-file-download" id="downloadForm"
	method="post">
	<input type="hidden" name="csrf_token_" value="<c:out value='${csrf_token_}'/>"/>
	<input type="hidden" name="downId" id="downId">
</form>


<form action="advisory-file-download" id="downloadFormAdv"
method="post">
<input type="hidden" name="csrf_token_" value="<c:out value='${csrf_token_}'/>"/>
<input type="hidden" name="downId" id="downadvId">
</form>
<!--  -->
         
         <script src="wrsis/js/canvasjs.min.js"></script>
         <script src="wrsis/js/jquery.canvasjs.min.js"></script>
         <script>

         //Author : Raman Shrestha
         $.ajax({
     		type : "POST",
     		url : 'getUsersCountByAjax',
     		cache : false,
     		async : false,
     		success : function(data) {
     			json = atob(data);
     			json = JSON.parse(json);
     			var pathologist = json.pathologist;
     	         var woredaexpert = json.woredaexpert;
     	         var surveyor = json.surveyor;
     	         var developmentagent = json.developmentagent;
     	        var currentseason = json.currentseason;
     	         $("#currentseason").html(currentseason);
     			$("#surveyorcount").html(surveyor);
     	         $("#pathologistcount").html(pathologist);
     	         $("#woredacount").html(woredaexpert);
     	         $("#developmentcount").html(developmentagent);
     		},
     		error : function(e) {
     			console.log("ERROR: ", e);
     		},
     		done : function(e) {
     			console.log("DONE");
     		}
     	});
         
       //Author : Raman Shrestha
         $.ajax({
      		type : "POST",
      		url : 'getTransactionCountByAjax',
      		cache : false,
      		async : false,
      		success : function(data) {
      			json = atob(data);
      			json = JSON.parse(json);
      			var rustaffetced = json.rustaffetced;
      	        var raceanalysis = json.raceanalysis;
      			var rustincidentcount = json.rustincidentcount;
      			var monitoring = json.monitoring;
      			$("#rustcount").html(rustaffetced);
      	        $("#racecount").html(raceanalysis);
      	      $("#ivrcount").html(rustincidentcount);
              $("#monitoringcount").html(monitoring)
          		},
      		error : function(e) {
      			console.log("ERROR: ", e);
      		},
      		done : function(e) {
      			console.log("DONE");
      		}
      	});
       	
         
         /* var json = '${DashBoardJson}';  */
        // var userJson='${DashBoardJsonUsersCount}';
        // var transJson='${DashBoardJsonTransCount}';
         /* json = atob(json);
         json = JSON.parse(json); */
         //userJson = atob(userJson);
        // userJson = JSON.parse(userJson);
        // transJson = atob(transJson);
        // transJson = JSON.parse(transJson);
         //{"pathologist":13,"woredaexpert":2,"surveyor":6,"developmentagent":6,"rustaffetced":25,"raceanalysis":3,"ivrdata":2,"monitoring":2}
         /* var pathologist = userJson.pathologist;
         var woredaexpert = userJson.woredaexpert;
         var surveyor = userJson.surveyor;
         var developmentagent = userJson.developmentagent; */
         /* var rustaffetced = transJson.rustaffetced;
         var raceanalysis = transJson.raceanalysis; */
         /* var ivrdata = json.ivrdata; */
         /* var monitoring = transJson.monitoring;
         var rustincidentcount = transJson.rustincidentcount; */
         /* var currentseason = userJson.currentseason;
         $("#currentseason").html(currentseason); */
         
         /* $("#surveyorcount").html(surveyor);
         $("#pathologistcount").html(pathologist);
         $("#woredacount").html(woredaexpert);
         $("#developmentcount").html(developmentagent); */
         /* $("#rustcount").html(rustaffetced);
         $("#racecount").html(raceanalysis); */
         /* $("#ivrcount").html(ivrdata); */
         /* $("#ivrcount").html(rustincidentcount);
         $("#monitoringcount").html(monitoring); */
         
        /*  var rustchart = json.rustchart;
        
         
         
         window.onload = function() {
        	 var json = '${DashBoardJson}';
             json = atob(json);
             json = JSON.parse(json);
             
             var rustchart = json.rustchart;
             var total = 0;
             for(i=0;i<rustchart.length;i++)
            	 {
            	 total += rustchart[i].y;
            	 }
             for(i=0;i<rustchart.length;i++)
        	 {
        	 var js = rustchart[i];
        	 js.data = js.y;
        	 js.y = (((js.y)*100)/total);
        	 rustchart[i] = js;
        	 }
 
        	chart.render();
        	}   */      
         
        	  /* var json = '${DashBoardJson}';
              json = atob(json);
              json = JSON.parse(json);
              
              var monsurvey = json.linemonitorsurvey */
              
            /*   var total = 0;
              for(i=0;i<monsurvey.length;i++)
             	 {
             	 total += rustchart[i].y;
             	 }
              for(i=0;i<monsurvey.length;i++)
         	 {
         	 var js = monsurvey[i];
         	 js.y = (((js.y)*100)/total);
         	monsurvey[i] = js;
         	 } */
              
              /* var moninci = json.linemonitomonitoring; */
              
              /*  total = 0;
              for(i=0;i<moninci.length;i++)
             	 {
             	 total += moninci[i].y;
             	 }
              for(i=0;i<moninci.length;i++)
         	 {
         	 var js = moninci[i];
         	 js.y = (((js.y)*100)/total);
         	moninci[i] = js;
         	 }
               */
        	/* var options = {
        			animationEnabled: true,
        			theme: "light2",
        			title:{
        				//text: "Monitoring Progress"
        			},
        			axisX:{
        				valueFormatString: "#,##0.##",
        			},
        			axisY: {
        				title: "Survey Recorded",
        				//suffix: "K",
        				minimum: 20
        			},
        			toolTip:{
        				shared:true
        			},  
        			legend:{
        				cursor:"pointer",
        				verticalAlign: "bottom",
        				horizontalAlign: "left",
        				dockInsidePlotArea: true,
        				itemclick: toogleDataSeries
        			},
        			data: [{
        				type: "line",
        				showInLegend: true,
        				name: "Survey",
        				markerType: "square",
        				xValueFormatString: "String",
        				color: "#F08080",
        				
        				yValueFormatString: "##0\"\"",
        				dataPoints: monsurvey
        			},
        			{
        				type: "line",
        				showInLegend: true, 
        				name: "Monitor",
        				markerType: "square",
        				
        				yValueFormatString: "##0\"\"",
        				dataPoints: moninci
        			}]
        		};
        		$("#chartContainerLine").CanvasJSChart(options);

        		function toogleDataSeries(e){
        			if (typeof(e.dataSeries.visible) === "undefined" || e.dataSeries.visible) {
        				e.dataSeries.visible = false;
        			} else{
        				e.dataSeries.visible = true;
        			}
        			e.chart.render();
        		}  */   
        		
        		$("#showRustTypeChart").click(
        				function() {
        					rustDataArray.splice(0, rustDataArray.length)
        					rustYear = $("#dropdownYearRust").val();
        					rustSeasonId = $("#seasonIdRust").val();
        					rustDateFrom = $("#rustDateFrom").val();
        					rustDateTo = $("#rustDateTo").val();
        					docReady=1;
        					var radioValue = $("input[name='rustStatus']:checked").val();
        					if (radioValue == 0) {
        						if (rustYear == 0 && rustSeasonId != 0) {
        							$("#dropdownYearRust").focus();
        							swal("Error", "Please select Year", "error").then(
        									function() {
        									});
        							return false;
        						}

        						if (rustSeasonId == 0 && rustYear != 0) {
        							$("#seasonIdRust").focus();
        							swal("Error", "Please select Season", "error").then(
        									function() {
        									});
        							return false;
        						}

        					} else {
        						if (rustDateFrom == '') {
        							$("#rustDateFrom").focus();
        							swal('Warning!', 'Please enter From date!', 'warning')
        							return false;
        						}
        						if (rustDateFrom != '') {

        							if (rustDateTo == '') {
        								$("#rustDateTo").focus();
        								swal('Warning!', 'Please enter to date!', 'warning')
        								return false;
        							}
        							if (Date.parse(rustDateFrom) > Date.parse(rustDateTo)) {
        								$("#rustDateFrom").focus();
        								swal('Warning!',
        										'From Date Should be less than To Date!',
        										'warning')
        								return false;
        							}
        						}
        					}

        					rustChart(rustYear, rustSeasonId, rustDateFrom, rustDateTo,docReady)
        				});
        		$("#showSurveyChart").click(
        				function() {
        					regionArray.splice(0, regionArray.length)
        					surveyRegionArr.splice(0, surveyRegionArr.length);
        					incidentRegionArr.splice(0, incidentRegionArr.length)
        					surveyDataArray.splice(0, surveyDataArray.length)
        					surveyYear = $("#dropdownYearSurvey").val();
        					surveySeasonId = $("#seasonIdSurvey").val();
        					surveyDateFrom = $("#surveyDateFrom").val();
        					surveyDateTo = $("#surveyDateTo").val();
        					docReady=1;
        					var radioValue = $("input[name='surveyStatus']:checked").val();
        					if (radioValue == 0) {
        						if (surveyYear == 0 && surveySeasonId != 0) {
        							$("#dropdownYearSurvey").focus();
        							swal("Error", "Please select Year", "error").then(
        									function() {
        									});
        							return false;
        						}

        						if (surveySeasonId == 0 && surveyYear != 0) {
        							$("#seasonIdSurvey").focus();
        							swal("Error", "Please select Season", "error").then(
        									function() {

        									});
        							return false;
        						}

        					} else {
        						if (surveyDateFrom == '') {
        							$("#surveyDateFrom").focus();
        							swal('Warning!', 'Please enter From date!', 'warning')
        							return false;
        						}
        						if (surveyDateFrom != '') {

        							if (surveyDateTo == '') {
        								$("#surveyDateTo").focus();
        								swal('Warning!', 'Please enter to date!', 'warning')
        								return false;
        							}
        							if (Date.parse(surveyDateFrom) > Date.parse(surveyDateTo)) {
        								$("#surveyDateFrom").focus();
        								swal('Warning!',
        										'From Date Should be less than To Date!',
        										'warning')
        								return false;
        							}
        						}
        					}

        					surveyChart(surveyYear, surveySeasonId, surveyDateFrom,
        							surveyDateTo,docReady)
        				});
        		$("#showRecChart").click(
        				function() {
        					montharr.splice(0, montharr.length)
        					surveyMonthArr.splice(0, surveyMonthArr.length);
        					monitorMonthArr.splice(0, monitorMonthArr.length)
        					recDataArray.splice(0, recDataArray.length)
        					recYear = $("#dropdownYearRec").val();
        					recSeasonId = $("#seasonIdRec").val();
        					recDateFrom = $("#recDateFrom").val();
        					recDateTo = $("#recDateTo").val();
        					docReady=1;
        					var radioValue = $("input[name='recStatus']:checked").val();
        					if (radioValue == 0) {
        						if (recYear == 0 && recSeasonId != 0) {
        							$("#dropdownYearRec").focus();
        							swal("Error", "Please select Year", "error").then(
        									function() {
        									});
        							return false;
        						}

        						if (recSeasonId == 0 && recYear != 0) {
        							$("#seasonIdRec").focus();
        							swal("Error", "Please select Season", "error").then(
        									function() {
        									});
        							return false;
        						}

        					} else {
        						if (recDateFrom == '') {
        							$("#recDateFrom").focus();
        							swal('Warning!', 'Please enter From date!', 'warning')
        							return false;
        						}
        						if (recDateFrom != '') {

        							if (rustDatrecDateToeTo == '') {
        								$("#recDateTo").focus();
        								swal('Warning!', 'Please enter to date!', 'warning')
        								return false;
        							}
        							if (Date.parse(recDateFrom) > Date.parse(recDateTo)) {
        								$("#recDateFrom").focus();
        								swal('Warning!',
        										'From Date Should be less than To Date!',
        										'warning')
        								return false;
        							}
        						}
        					}
        					recChart(recYear, recSeasonId, recDateFrom, recDateTo,docReady)
        				});
        		$("#showRustTypeChart").click(function() {
					$(".portletf1").slideToggle();
				});

				$("#showSurveyChart").click(function() {
					$(".portletf2").slideToggle();
				});

				$("#showRecChart").click(function() {
					$(".portletf3").slideToggle();
				});
				$(".p-search1").click(function() {
					$(".portletf1").slideToggle();
				});

				$(".p-search2").click(function() {
					$(".portletf2").slideToggle();
				});

				$(".p-search3").click(function() {
					$(".portletf3").slideToggle();
				});
				
				
				
				 //Author : Debendra Nayak
				 //Get Advisory Latest Document
		         $.ajax({
		      		type : "POST",
		      		url : 'getAdvisoryLatestRecord',
		      		cache : false,
		      		async : false,
		      		success : function(data) {
		      			json = atob(data);
		      			json = JSON.parse(json);

		      			$("#advisorytr").show();
		      			if(json.advisoryId!=undefined || json.advisoryNo!= undefined || json.advPublishDate!= undefined)
	    				{
		      			
		      			var html_ = '';
		      			
		      			var advisoryId = json.advisoryId;
		      	        var advisoryNo = json.advisoryNo;
		      			var advPublishDate = json.advPublishDate;
		      			
		      			html_+='<tr> ';
		      			html_ += '<td class="align-middle">'+advisoryNo+'</td>';
		      			html_ += '<td class="align-middle">'+advPublishDate+'</td>';
		      			html_ += '<td class="text-center align-middle"><a title="" class="btn btn-success btn-sm mr-0" href="javascript:void(0)" id="downloadIcon" data-toggle="tooltip" data-placement="top" data-original-title="Download" onclick="downloadAdvioryFile('+advisoryId+')"><i class="fa fa-file-pdf-o " aria-hidden="true"></i></a></td>';
		      			html_+='</tr> ';
		      			
		              $("#documentsAdvisory").html(html_)
		              
    				   }else{
    					$("#advisorytr").hide();
    					$( "#documentsAdvisory" ).html('');
    			        $( "#documentsAdvisory" ).html('No data found !');
    			        }
		              
		          		},
		      		error : function(e) {
		      			console.log("ERROR: ", e);
		      		},
		      		done : function(e) {
		      			console.log("DONE");
		      		}
		      	});	
				
				
				
				
		       //Author : Debendra Nayak
				 //Get Recommendation Latest Document
		         $.ajax({
		      		type : "POST",
		      		url : 'getRecomLatestRecord',
		      		cache : false,
		      		async : false,
		      		success : function(data) {
		      			json = atob(data);
		      			json = JSON.parse(json);
		      			$("#recomtr").show();
		      			if(json.recomId!=undefined || json.recomNo!= undefined || json.recomType!= undefined || json.recomPublishDate!= undefined)
	    				{
		      			var html_ = '';
		      			
		      			var recomId = json.recomId;
		      	        var recomNo = json.recomNo;
		      			var recomType = json.recomType;
		      			var recomPublishDate = json.recomPublishDate;
		      			
		      			html_+='<tr> ';
		      			html_ += '<td class="align-middle">'+recomNo+'</td>';
		      			html_ += '<td class="align-middle">'+recomPublishDate+'</td>';
		      			if(recomType == 1){
		      				html_ += '<td>General</td>';
		      			}else{
		      				html_ += '<td class="align-middle">Region Specific <a title="" data-placement="top" data-toggle="modal" data-target="#locationModal" onclick="viewLocation('+recomId+')"><i class="fa fa-info-circle" aria-hidden="true" style="color: #83a750"></i></a></td>';
		      			}
		      			html_ += '<td class="text-center align-middle"><a title="" class="btn btn-success btn-sm mr-0" href="javascript:void(0)" id="downloadIcon" data-toggle="tooltip" data-placement="top" data-original-title="Download" onclick="downloadRecomFile('+recomId+')"><i class="fa fa-file-pdf-o " aria-hidden="true"></i></a></td>';
		      			
		      			html_+='</tr> ';
		      			
		              $("#documentsRecom").html(html_)
		              
	    				}else{
	    				$("#recomtr").hide();
	    				$( "#documentsRecom" ).html('');
    			        $( "#documentsRecom" ).html('No data found !');
    			        }
		              
		          		},
		      		error : function(e) {
		      			console.log("ERROR: ", e);
		      		},
		      		done : function(e) {
		      			console.log("DONE");
		      		}
		      	});	
				
				
				
         </script>
         
  <script type="text/javascript">

         function downloadAdvioryFile(id)
         {	
        	
        	
         	$.ajax({
         		type:"GET",
         		url:"advisory-file-exists",
         		data:{
         				"downId":id
         		},
         		success:function(response){
        			
         			var res=JSON.parse(response);
         			if(res.msg == 'Yes')
         			{	
         				$("#downadvId").val(id);
         				$("#downloadFormAdv").submit();
         			}else{
         				swal("File not found"," ", "error"); 
         				}
         		},
         		error:function(error)
         		{
         		},		
         		}); 
         }
         
         
         
         function downloadRecomFile(id)
         {	
        	
         	$.ajax({
         		type:"GET",
         		url:"recommendation-file-exists",
         		data:{
         				"downId":id
         		},
         		success:function(response){
        			//alert(response);
         			var res=JSON.parse(response);
         			if(res.msg == 'Yes')
         			{	
         				$("#downId").val(id);
         				$("#downloadForm").submit();
         			}else{
         				swal("File not found"," ", "error"); 
         				}
         		},
         		error:function(error)
         		{
         		},		
         		}); 
         }
         
         
         function viewLocation(id)
         {
         	//alert(id);
         	$('#loctblHd').empty();
         	$('#loctblBdy').empty();
         	$('#locMsg').empty(); 
         	 $.ajax({
         		type : "POST",
         		url : "getRecLocDetails", 
         	 
         		data : {
         			"recId":id
         		},
         		success : function(response) {
         			
         			var val=JSON.parse(response);
         		    if(val.length == 0 ){
         				$('#locMsg').html('No data available');
         			}else{
         			    var htmlBody ="";
         			    var htmlHead ='<tr><th>Region</th><th>Zone</th><th>Woreda</th><th>Kebele</th></tr>';
         				$.each(val,function(i, item) {
         					htmlBody += '<tr><td>' + item.region + '</td><td>' + item.zone + '</td><td>' + item.woreda +'</td><td>' + item.kebele + '</td></tr>';
         				});
         				$('#loctblHd').append(htmlHead);
         				$('#loctblBdy').append(htmlBody);
         			}
         			
         		},error : function(error) {
         			 
         		}
         	});  
         }
         
</script>
         