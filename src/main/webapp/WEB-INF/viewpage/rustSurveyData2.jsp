<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix = "fmt" uri = "http://java.sun.com/jsp/jstl/fmt" %>
<link rel="stylesheet" type="text/css" href="https://cdn.datatables.net/1.10.16/css/dataTables.bootstrap4.min.css"/>
<script src="https://cdn.datatables.net/1.10.16/js/jquery.dataTables.min.js"></script>
	<script src="https://cdn.datatables.net/1.10.16/js/dataTables.bootstrap4.min.js"></script>
	
<div class="mainpanel">
            <div class="section">
               <div class="page-title">
                  <div class="title-details">
                     <h4>Survey Data</h4>
                     <nav aria-label="breadcrumb">
                        <ol class="breadcrumb">
                           <li class="breadcrumb-item"><a href="Home"><span class="icon-home1"></span></a></li>
                           <li class="breadcrumb-item">Manage Survey</li>
                           <li class="breadcrumb-item active" aria-current="page">Survey Data</li>
                        </ol>
                     </nav>
                  </div>
       
                  
     	
		          
               </div>
               <div class="row">
                  <div class="col-md-12 col-sm-12">
                     <div class="card">
                        <div class="card-header">
                           <ul class="nav nav-tabs nav-fill" role="tablist">
                              
                              <a class="nav-item nav-link active"  href="rustSurveyData">View</a>
                              
                           </ul>
                    <div class="indicatorslist">
                              
                             
       
       <!-- This only download only page wise -->                     
          <%--  <form action="survey-publish-excel-download" id="excelForm" method="post">
       		<input type="hidden" name="csrf_token_" value="<c:out value='${csrf_token_}'/>"/>
       		<input type="hidden" id="regionEId" name="region">
       		<input type="hidden" id="zoneEId" name="zone">
       		<input type="hidden" id="woredaEId" name="woreda">
       		<input type="hidden" id="kebeleEId" name="kebele">
       		<input type="hidden" id="strtDateId" name="startDate">
       		<input type="hidden" id="endDateId" name="endDate">
       		<input type="hidden" id="surveyEnumber" name="surveyNumber">
       		<input type="hidden" id="rustEId" name="rustId">
       		 <button title="Excel" class="btn btn-outline-success btn-sm shadow-none"><i class="icon-excel-file" ></i></button>
       </form> --%>
       
        <!-- This download details Data -->
        <form action="survey-publish-data-excel-generate" id="excelForm" method="post">
       		<input type="hidden" name="csrf_token_" value="<c:out value='${csrf_token_}'/>"/>
       		<input type="hidden" id="regionEId" name="region">
       		<input type="hidden" id="zoneEId" name="zone">
       		<input type="hidden" id="woredaEId" name="woreda">
       		<input type="hidden" id="kebeleEId" name="kebele">
       		<input type="hidden" id="strtDateId" name="startDate">
       		<input type="hidden" id="endDateId" name="endDate">
       		<input type="hidden" id="surveyEnumber" name="surveyNumber"> 
       		<input type="hidden" id="rustEId" name="rustId">
       		 <button title="Excel" class="btn btn-outline-success btn-sm shadow-none"><i class="icon-excel-file" ></i></button>
       </form>
       </div>
       <div class="indicatorslist">
       <form action="survey-publish-pdf-download" id="pdfform" method="post" target="_blank">
       		<%-- <input type="hidden" name="csrf_token_" value="<c:out value='${csrf_token_}'/>"/> --%>

       		<input type="hidden" id="regionPDF" name="region">
       		<input type="hidden" id="zonePDF" name="zone">
       		<input type="hidden" id="woredaPDF" name="woreda">
       		<input type="hidden" id="kebelePDF" name="kebele">
       		<input type="hidden" id="strtDatePDF" name="startDate">
       		<input type="hidden" id="endDatePDF" name="endDate">
       		<input type="hidden" id="surveyEnumberPDF" name="surveyNumber">
       		<input type="hidden" id="rustPDF" name="rustId">
       		<button title="Print" id="pdfdownload" class="btn btn-outline-success btn-sm shadow-none"><i class="icon-printer1" ></i></button>
       </form>
                           </div> 
                        </div>
                        
                        <input type="text" id="searchRegionId" value="${regionId }" style="display: none;">
     					<input type="text" id="searchZoneId" value="${zoneId }" style="display: none;">
						<input type="text" id="searchWoredaId" value="${woredaId }" style="display: none;">
						<input type="text" id="searchKebeleId" value="${kebeleId}" style="display: none;">
						<input type="text" id="searchStartDId" value="${startDate}" style="display: none;">
						<input type="text" id="searchEndDId" value="${endDate}" style="display: none;">
						<input type="text" id="searchSurveyNoId" value="${surveyNo}" style="display: none;">
						<input type="text" id="searchRustId" value="${rustTypeId}" style="display: none;">
                        
                        
                        <!-- Search Panel -->
                        <div class="search-container">
                         <c:if test="${showSearch eq false}">
                           <div class="search-sec">
                           </c:if>
                            <c:if test="${showSearch eq true}">
                           <div class="search-sec" style="display:block;">
                           </c:if>
                           <%--  <form:form action="rustSurveyDataSearch" autocomplete="off"   method="post" onsubmit="return false" modelAttribute="searchVo"> --%>
                             <form action="rustSurveyDataSearch" autocomplete="off"   method="post" onsubmit="return false" >
                                   <input type="hidden" name="csrf_token_" value="<c:out value='${csrf_token_}'/>"/>
                                   <div class="form-group">
                                 <div class="row">
                                    <label class="col-lg-2 ">Region Name</label>
                                    <div class="col-lg-3">
                                       <select  class="form-control" id="regionId" name="regionId"  onchange="findZoneByRegionId(3,this.value);"  tabindex="2"> 
                                       <%-- <form:select  class="form-control" id="regionId" path="servRegionId"  onchange="findZoneByRegionId(3,this.value);"  tabindex="2"> --%>
                                   		<option value="0">-Select-</option>
                                   		<c:forEach items="${regionList}" var="region"> 
                                   		<c:choose>
											<c:when test="${region.demographyId eq regionId}">
												<option value="${region.demographyId}" selected="selected">${region.demographyName}</option>
											</c:when>
											<c:otherwise>
												<option value="${region.demographyId}">${region.demographyName}</option>
											</c:otherwise>
									   </c:choose>
                                   </c:forEach> 
                                    </select>
                                    </div>
                                    <label class="col-lg-2 ">Zone Name</label>
                                    <div class="col-lg-3">
                                    <%-- <form:select class="form-control" path="servZoneId" id="zoneId" onchange=" findWoredaByZoneId(4,this.value);">
                                   <option value="0">-Select-</option>
                                    </form:select> --%>
                                    <select class="form-control" name="zoneId" id="zoneId" onchange=" findWoredaByZoneId(4,this.value);">
                                   <option value="0">-Select-</option>
                                    </select>
                                    </div>
                                 </div>
                              </div>
                              <div class="form-group">
                              <div class="row">
                               <label class="col-lg-2 ">Woreda Name</label>
                                    <div class="col-lg-3">
                                       <select class="form-control" name="woredaId" id="woredaId" onchange="searchKebele(5,this.value);">
                                      <option value="0">-Select-</option>
                                      </select>
                                       <%-- <form:select class="form-control" path="servWoredaId" id="woredaId" onchange="searchKebele(5,this.value);">
                                      <option value="0">-Select-</option>
                                      </form:select> --%>
                                    </div>
                               <label class="col-lg-2 ">Kebele Name</label>
                                    <div class="col-lg-3">
                                    <select class="form-control" name="kebeleId" id="kebeleId">
                                         	<option value="0">-Select-</option>
                                       </select>
                                        <%-- <form:select class="form-control" path="servKebeleId" id="kebeleId">
                                         	<option value="0">-Select-</option>
                                       </form:select> --%>
                                    </div>
                              </div>
                              </div>
                              <div class="form-group">
                              	<div class="row">
                              	<label class="col-lg-2 ">Survey Date From</label>
                                    <div class="input-group col-lg-3">
                                       <input type="text" class="form-control datepicker" data-date-end-date="0d" aria-label="Default" aria-describedby="inputGroup-sizing-default" value="${startDate}" name="startDate" id="startDate">
                                       <div class="input-group-append">
											<span class="input-group-text" id="inputGroup-sizing-default"><i class="icon-calendar1"></i></span>
										</div>
                                    </div>
                                 <label class="col-lg-2 ">To Survey Date</label>
                                    <div class="input-group col-lg-3">
                                       <input type="text" class="form-control datepicker" data-date-end-date="0d" aria-label="Default" aria-describedby="inputGroup-sizing-default"  name="endDate" id="endDate" value="${endDate}">
                                       <div class="input-group-append">
											<span class="input-group-text" id="inputGroup-sizing-default"><i class="icon-calendar1"></i></span>
										</div>
                                    </div>
                              	</div>
                              </div>
                              <div class="form-group">
                                 <div class="row">
                               <label class="col-lg-2 ">Survey No.</label>
                                    <div class="col-lg-3">
                                        <input type="text" class="form-control"  placeholder="" data-bv-field="fullName" id="surveyNo" name="surveyNo" value="${surveyNo}"/>
                                    </div>
                                    <label class="col-sm-2 ">Type of Rust</label>
                                    <div class="col-sm-3">
                                     <select class="form-control" id="rustTypeId" name="servRustId">
											<option value="0">--select--</option>
											<c:forEach items="${rustTypeList}" var="rustTypeList">
												<c:choose>
													<c:when
														test="${rustTypeList.rustId eq rustTypeId}">
														<option value="${rustTypeList.rustId}" selected="selected">${ rustTypeList.typeOfRust}</option>
													</c:when>
													<c:otherwise>
														<option value="${ rustTypeList.rustId}">${ rustTypeList.typeOfRust}</option>
													</c:otherwise>
												</c:choose>
											</c:forEach>
										</select>
                                    </div>
                                    
                                     <div class="col-lg-2">
                                       <button class="btn btn-primary" onclick="searchData()"> <i class="fa fa-search"></i> Search</button>
                                    </div>
                                 </div>
                              </div>
                      </form>       
                                
                             
                              
                              
                      
                           </div>
                           <div class="text-center"> <a class="searchopen" title="Search" data-toggle="tooltip" data-placement="bottom" > <i class="icon-angle-arrow-down"></i> </a></div>
                        </div>
                        <!-- Search Panel -->
                        <!--===================================================-->
                        <div class="card-body">
                           <div class="table-responsive">
                              <table data-toggle="table" class="table table-hover table-bordered" id="viewTable">
                                 <thead>
                                    <tr>
                                   
                                       <th width="40px">Sl#</th>
                                       <th>Survey No.</th>
                                       <th>Survey Date</th>
                                       <th>Published Date</th>
                                       <th>Region</th>
                                       <th>Zone</th>
                                       <th>Woreda</th>
                                       <th>Kebele</th>
                                       <th>Instituition Name</th>
                                       <th>Type of Rust</th>
                                       <th>Mode of Data Collection</th>
                                       
                                    </tr>
                                 </thead>
                                 <tbody>
                                    <%-- <c:forEach items="${details}" var="dtls" varStatus="count">
                                    <tr>
                                       <td>${count.index + 1 }</td>
                                       <td><a href="javascript:void(0);" class="viewsurvey" survey-id="${dtls.surveyId }">${dtls.surveyNo }</a></td>
                                       <td> ${dtls.surveyDate }</td>
                                       <td>${dtls.publishedDate}</td>
                                       <td>${dtls.region }</td>
                                       <td>${dtls.zone  }</td>
                                       <td>${dtls.woreda  }</td>
                                       <td>${dtls.kebele}</td>
                                       <td>${dtls.institutionName}</td>
                                       <td>${dtls.rust  }</td>
                                       <td>${dtls.mode  }</td>
                                     </tr>
                                   </c:forEach> --%>
                                   </tbody>
                              </table>
                           </div>
                           
                        </div>
                        <!--===================================================-->
                     </div>
                  </div>
               </div>
               <form action="viewSurveyDetailsOnPublished" id="myForm1" method="post">
          	<input type="hidden" name="csrf_token_" value="<c:out value='${csrf_token_}'/>"/>

          	<input type="text" value="" style="display:none;" name="surveyId" id="surveyId1">
          	<input type="hidden"   name="r_url" value="rustSurveyData">
          </form>
         
            </div>
         </div>
 <script>
 /* $(document).ready(function() {
	    $('#viewTable').DataTable();
	} ); */

/* 
 $(function() {
		$('#viewTable').dataTable({
			'paging':true,
			'lengthChange':true,
			'searching':true,
			'ordering':true,
			'info':true,
			'autoWidth':false,
		
	   //initialization params as usual
	   fnInitComplete : function() {
	      if ($(this).find('td').length<=1) {
	         $('.dataTables_wrapper').hide();
	         swal("No record found")
	         $('#actionDivId').hide();
	     	$('#actionBtnID').hide();
	         }
	      } 
		});
		});	 */
 </script>  
 <script>
         
$(document).on('click', '.viewsurvey', function()
{
	
   var surveyId = $(this).attr('survey-id');
    $("#surveyId1").val(surveyId);
    $("#myForm1").submit();
});
</script>
<script>
function findZoneByRegionId(levelId,parentId)
{
	
	$.ajax({
		type : "GET",
		url : "find-zone-by-region-id", 
		 async:false,
		data : {
			"levelId":levelId,
			"parentId" : parentId
			
		},
		success : function(response) {

			var html = "<option value='0'>-Select-</option>";
			var val=JSON.parse(response);
			 
			if (val != "" || val != null) {
				$.each(val,function(index, value) {
				var zoneDocId=$("#searchZoneId").val();
     			 if(zoneDocId==value.zoneId)	
				{					
	 				html=html+"<option value="+value.zoneId+" selected='selected'>"+value.zoneName+"</option>";
				}else{ 
					html=html+"<option value="+value.zoneId+" >"+value.zoneName+"</option>";
					} 
				});
			}
			$('#zoneId').empty().append(html);
			$('#woredaId').empty().append("<option value='0'>-Select-</option>");
			$('#kebeleId').empty().append("<option value='0' >-Select-</option>");
		},error : function(error) {
			 
		}
	});
}

function findWoredaByZoneId(levelId,parentId)
{
	$.ajax({
		type : "GET",
		url : "find-woreda-by-zone-id", 
		 async:false,
		data : {
			"levelId":levelId,
			"parentId" : parentId
			
		},
		success : function(response) {
			 
			//alert(response);
			var html ="";
			var val=JSON.parse(response);
			html=html+"<option value='0' >-Select-</option>";
			if (val != "" || val != null) {
				$.each(val,function(index, value) {			
					
					var woredaDocId=$("#searchWoredaId").val();
					 if(woredaDocId==value.woridaId)	
						{					
							html=html+"<option value="+value.woridaId+" selected='selected'>"+value.woridaName+"</option>";
						}else{ 
							html=html+"<option value="+value.woridaId+" >"+value.woridaName+"</option>";
						} 
				});
			}
			$('#woredaId').empty().append(html);
			$('#kebeleId').empty().append("<option value='0' >-Select-</option>");
		},error : function(error) {
			 
		}
	});
}
function searchKebele(levelId,woredaId)
{
	
   
     $.ajax({
		type : "GET",
		 url : "find-multiple-kebele-by-woreda-id",
		 async:false, 
	   data : {
			"levelId":levelId,
			"woredaId":woredaId
			
		},
		success : function(response) {
			var html = " ";
			html=html+"<option value='0' >-Select-</option>";
			var val=JSON.parse(response);
			
			if (val != "" || val != null) {
				$.each(val,function(index, value) {			
					var kebeleDocId=$("#searchKebeleId").val();
					 if(kebeleDocId==value.kebeleId)	
					{					
						html=html+"<option value="+value.kebeleId+" selected='selected'>"+value.kebeleName+"</option>";
					}else{
					html=html+"<option value="+value.kebeleId+" >"+value.kebeleName+"</option>";
				} 
				});
			}
			$('#kebeleId').empty().append(html);
			
		},error : function(error) {
			 
		}
	});

	
} 

function downloadExcel(){
	//alert("In Excel");
	
}
$(document).ready(function()
{

	 $("#regionEId").val($("#regionId").val());
		$("#zoneEId").val($("#zoneId").val());
		$("#woredaEId").val($("#woredaId").val());
		var t=$("#kebeleId").val();
		$("#kebeleEId").val($("#kebeleId").val());
		$("#strtDateId").val($("#startDate").val());
		$("#endDateId").val($("#endDate").val());
		$("#surveyEnumber").val($("#surveyNo").val());
		$("#rustEId").val($("#rustTypeId").val());
		//$("#excelForm").submit(); 
	
	$("#pdfdownload").click(function(){
		 	$("#regionPDF").val($("#regionId").val());
			$("#zonePDF").val($("#zoneId").val());
			$("#woredaPDF").val($("#woredaId").val());
			$("#kebelePDF").val($("#kebeleId").val());
			$("#strtDatePDF").val($("#startDate").val());
			$("#endDatePDF").val($("#endDate").val());
			$("#surveyEnumberPDF").val($("#surveyNo").val());
			$("#rustPDF").val($("#rustTypeId").val());
			$("#pdfform").submit(); 

		});
});
</script>     

<script>
function searchData(){
	 event.preventDefault();
	 var form = event.target.form;
	var fromDate=$("#startDate").val();
	
	var toDate=$("#endDate").val();
	if(fromDate!='')
	{	
	
		if(toDate =='')
		{
			swal('Warning!','Please enter to date!','warning')
   			$("#toDate").focus();
   			return false; 
		}
		if(Date.parse(fromDate)>Date.parse(toDate))
		{
			swal('Warning!','From Date Should be less than To Date!','warning')
   			return false; 
		}
	}
	form.submit();	
	
}


$(document).ready(function(){
	/* findZoneByRegionId(3,${regionId});
	findWoredaByZoneId(4,${zoneId});
	searchKebele(5,${woredaId}); */ 
});
</script>   
<script>

$(function() {
	

	$('#viewTable').dataTable({
		
			'processing' : true,
			'serverSide' : true,
			 "searching": false,
	      "ordering": false, 
			 "ajax" : {
					"url" : "rustSurveyDataPageWise",
					"data" : function(d) {
						d.regionId= $("#regionId").val();
						d.zoneId = $("#zoneId").val();
						d.woredaId =$("#woredaId").val();
						d.kebeleId = $("#kebeleId").val();
						d.startDate =$("#startDate").val();
						d.endDate = $("#endDate").val();
						d.surveyNo = $("#surveyNo").val();
						d.rustTypeId = $("#rustTypeId").val();
					}
			},
			"dataSrc": "",
			"columns":[
				{"data":"sNo"},
				{"data":"surveyNo"},
				{"data":"surveyDate"},
				{"data":"publishedDate"},
				{"data":"region"},
				{"data":"zone"},
				{"data":"woreda"},
				{"data":"kebele"},
				{"data":"institutionName"},
				{"data":"rust"},
				{"data":"mode"}
				
			]
	
  
	});
	});	
	
</script>

<script>

var regionId = '${regionId}' ;

if(regionId != '' && regionId != undefined && regionId != null)
{
	
		$("#regionId").val(regionId);
		$("#regionId").change();
		 var zoneId='${zoneId}';
		if(zoneId != null && zoneId != undefined && zoneId != '' && zoneId != -1 && zoneId !=0)
		{
			$("#zoneId").val(zoneId);
			$("#zoneId").change();
			var woredaId='${woredaId}';
			if(woredaId != null && woredaId != undefined && woredaId != '' && woredaId != -1 && woredaId !=0)
			{
				$("#woredaId").val(woredaId);
				$("#woredaId").change();
				var kebeleId='${kebeleId}';
				if(kebeleId != null && kebeleId != undefined && kebeleId != '' && kebeleId != -1 && kebeleId !=0)
				{
					$("#kebeleId").val(kebeleId);
					$("#kebeleId").change();
				}
			}
		} 
		
		
}

$("#startDate").val('${startDate}');
$("#endDate").val('${endDate}');
$("#surveyNo").val('${surveyNo}');



</script>

