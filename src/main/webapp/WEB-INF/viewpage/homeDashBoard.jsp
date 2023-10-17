<link href="wrsis/css/dashboard.css" rel="stylesheet">
<link rel="stylesheet" href="//cdn.jsdelivr.net/chartist.js/latest/chartist.min.css">
<link rel="stylesheet" href="wrsis/css/chartist-plugin-tooltip.css">
 
<script src="wrsis/js/chartist.min.js"></script>
<script src="wrsis/js/chartist-plugin-tooltip.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/chartist-plugin-legend/0.6.1/chartist-plugin-legend.min.js"></script>
<script src="wrsis/js/dashboard.js"></script>


 
 
 
 
 <div class="mainpanel">
            <div class="section">
               <div class="top-portlets">
                  <div>
                  	<p style=" font-weight: bold;">User Details</p>
                  </div>
                  <div class="row">
                  
                     <div class="col-12 col-sm-6 col-md-6 col-lg-3">
                        <div class="portlets__card ">
                           <div class="portlets__card__content cyan">
                              <p><i class="icon-user1"></i> Surveyors</p>
                              <h4><a href="surveyorReport" style="color:white;" id="surveyorcount"></a></h4>
                           </div>
                           <div class="portlets__card__charts cyan-dark">
                              <div class="charts__content" id="newclints">
                              </div>
                           </div> 
                        </div>
                     </div>
                    
                     <div class="col-12 col-sm-6 col-md-6 col-lg-3">
                        <div class="portlets__card ">
                           <div class="portlets__card__content red">
                              <p><i class="icon-user1"></i> Pathologists</p>
                              <h4 ><a href="pathologistReport" style="color:white;" id="pathologistcount"></a></h4>
                           </div>
                           <div class="portlets__card__charts red-dark">
                              <div class="charts__content" id="totalsales">
                              </div>
                           </div>
                        </div>
                     </div>
                     <div class="col-12 col-sm-6 col-md-6 col-lg-3">
                        <div class="portlets__card ">
                           <div class="portlets__card__content orange">
                              <p><i class="icon-user1"></i> Woreda Experts</p>
                              <h4 ><a href="woredaExpertsReport" style="color:white;" id="woredacount"></a></h4>
                           </div>
                           <div class="portlets__card__charts orange-dark">
                              <div class="charts__content" id="profit">
                              </div>
                           </div>
                        </div>
                     </div>
                     <div class="col-12 col-sm-6 col-md-6 col-lg-3">
                        <div class="portlets__card ">
                           <div class="portlets__card__content green">
                              <p><i class="icon-user1"></i> Development Agents</p>
                              <h4 ><a href="devAgentReport" style="color:white;" id="developmentcount"></a></h4>
                              
                           </div>
                            <div class="portlets__card__charts green-dark">
                              <div class="charts__content" id="newinvoices">
                              </div>
                           </div>
                        </div>
                     </div>
                    
                  </div>
               </div>
               <div class="top-portlets">
                  <div>
                  	<p style=" font-weight: bold;">Statistics for the current season&nbsp;(<span id="currentseason"></span>)</p>
                  </div>
                  <div class="row">
                  
                     <div class="col-12 col-sm-6 col-md-6 col-lg-3">
                        <div class="portlets__card ">
                           <div class="portlets__card__content green">
                              <p><i class="icon-user1"></i> Rust Survey</p>
                              <h4><a href="rustReport" style="color:white;" id="rustcount"></a></h4>
                              
                           </div>
                           <div class="portlets__card__charts green-dark">
                              <div class="charts__content" id="newclints">
                              </div>
                           </div>
                        </div>
                     </div>
                     <div class="col-12 col-sm-6 col-md-6 col-lg-3">
                        <div class="portlets__card ">
                           <div class="portlets__card__content orange">
                              <p><i class="icon-user1"></i> Race Analysis</p>
                              <h4 ><a href="raceAnalysisReport" style="color:white;" id="racecount"></a></h4>
                              
                           </div>
                           <div class="portlets__card__charts orange-dark">
                              <div class="charts__content" id="newclints">
                              </div>
                           </div>
                        </div>
                     </div>
                     <div class="col-12 col-sm-6 col-md-6 col-lg-3">
                        <div class="portlets__card ">
                           <div class="portlets__card__content red">
                              <p><i class="icon-user1"></i> <!-- IVR Data Collected -->Rust Incident</p>
                              <h4 ><a href="dashboardRustIncident" style="color:white;" id="ivrcount"></a></h4>
                           </div>
                           <div class="portlets__card__charts red-dark">
                              <div class="charts__content" id="newclints">
                              </div>
                           </div>
                        </div>
                     </div>
                     <div class="col-12 col-sm-6 col-md-6 col-lg-3">
                        <div class="portlets__card ">
                           <div class="portlets__card__content cyan">
                              <p><i class="icon-user1"></i> Monitoring Data</p>
                              <h4 ><a href="monitorDataReport" style="color:white;" id="monitoringcount"></a></h4>
                           </div>
                           <div class="portlets__card__charts cyan-dark">
                              <div class="charts__content" id="newclints">
                              </div>
                           </div>
                        </div>
                     </div>
                   
                  </div>
               </div>
               <div class="row">
                  <div class="col-12 col-md-6 col-lg-4">
                     <div class="dashboard-portlet">
                     	<h6>Rust Type Statistics <a class="float-right fa fa-external-link" title="Click to view details" href="rustTypeChartReport" ><span class="icon-more-vertical1"></span></a>
                           </h6>
                           <div id="chartContainerPai" style="height: 300px; width: 100%;"></div>
                     </div>
                  </div>
                  <div class="col-12 col-md-6 col-lg-4">
                     <div class="dashboard-portlet">
                     	<h6>Rust Survey vs Rust Incident<a class="float-right fa fa-external-link" title="Click to view details" href="regionAndRustTypeReport" ><span class="icon-more-vertical1"></span></a>
                           </h6>
                            <div id="chartContainerBar" class="dashboard-portlet__content__chart" style="height: 300px; width: 100%;"></div> 
                           
                     </div>
                  </div>
                  <div class="col-12 col-md-6 col-lg-4">
                     <div class="dashboard-portlet">
                     	<h6>Rust Survey vs Recom Survey<a class="float-right fa fa-external-link" title="Click to view details" href="rustSurveyAndRecommentationSurveyChart" ><span class="icon-more-vertical1"></span></a>
                           </h6>
                           <div id="chartContainerLine" style="height: 300px; max-width: 400px; margin: 0px auto;"></div> 
                          
                     </div>
                  </div>
                 
               </div>
            </div>
         </div>
         <script src="wrsis/js/canvasjs.min.js"></script>
         <script src="wrsis/js/jquery.canvasjs.min.js"></script>
         <script>
         
         var json = '${DashBoardJson}';
         json = atob(json);
         json = JSON.parse(json);
         
         //{"pathologist":13,"woredaexpert":2,"surveyor":6,"developmentagent":6,"rustaffetced":25,"raceanalysis":3,"ivrdata":2,"monitoring":2}
         var pathologist = json.pathologist;
         var woredaexpert = json.woredaexpert;
         var surveyor = json.surveyor;
         var developmentagent = json.developmentagent;
         var rustaffetced = json.rustaffetced;
         var raceanalysis = json.raceanalysis;
         var ivrdata = json.ivrdata;
         var monitoring = json.monitoring;
         var rustincidentcount = json.rustincidentcount;
         var currentseason = json.currentseason;
         $("#currentseason").html(currentseason);
         
         $("#surveyorcount").html(surveyor);
         $("#pathologistcount").html(pathologist);
         $("#woredacount").html(woredaexpert);
         $("#developmentcount").html(developmentagent);
         $("#rustcount").html(rustaffetced);
         $("#racecount").html(raceanalysis);
         /* $("#ivrcount").html(ivrdata); */
         $("#ivrcount").html(rustincidentcount);
         $("#monitoringcount").html(monitoring);
         
         var rustchart = json.rustchart;
        
         
         
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
             
             
var chart = new CanvasJS.Chart("chartContainerPai", {
	animationEnabled: true,
	title: {
		//text: "Survey Statistic"
	},
	data: [{
		type: "doughnut",
		   //showInLegend: true, 
		   // indexLabelPlacement: "inside",
		    indexLabelLineColor: "red",
		//startAngle: 240,
		 radius:  "90%",
		 indexLabelColor: "red",
		yValueFormatString: "##0\"%\"",
		indexLabel: "{label} ({data})",
		dataPoints: rustchart
	}]
});
chart.render();

//}

         
         
         /* window.onload = function () { */
        	    var chart = new CanvasJS.Chart("chartContainerBar",
        	    {
        	      toolTip: {
        	        shared: true  //disable here. 
        	      },
        	      title:{
        	        //text: "Comparison Between Survey,Ivr and Incident"       
        	      },
        		  axisY: {
        			title: " Survey and  Incident"
        		},
        		legend: {
        			cursor:"pointer",
        			 itemclick: function (e) {
        	                   if (typeof (e.dataSeries.visible) === "undefined" || e.dataSeries.visible) {
        	                    e.dataSeries.visible = false;
        	                } else {
        	                    e.dataSeries.visible = true;
        	                }

        	                e.chart.render();
        	            }
        		},
        		dataPointWidth: 10,
        	        data: [
        	      {        
        	       type: "column",       
        	       name: "Survey",
        		   showInLegend: true, 
        	       dataPoints: json.regionsurvey
        	     },
        	     
        	     {        
        	       type: "column",       
        	       name: "Incident",
        		   showInLegend: true, 
        	       dataPoints: json.regionincident
        	     }
        	     
        	     ]
        	   });

        	chart.render();
        	}        
         
        	  var json = '${DashBoardJson}';
              json = atob(json);
              json = JSON.parse(json);
              
              var monsurvey = json.linemonitorsurvey
              
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
              
              var moninci = json.linemonitomonitoring;
              
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
        	var options = {
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
        		}      
         </script>