<%@ page language="java" import="java.util.*" pageEncoding="ISO-8859-1"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<script language="javascript">
	pageHeader="";
	strFirstLink="Welcome";
	strLastLink="";

</script>
<script>
$(function () {
    //Initialize Select2 Elements
    $('.select2').select2();
    
  //Date range picker
    $('#reservation').daterangepicker()
    
  

    //Date picker
    $('#datepicker').datepicker({
      autoclose: true
      
    })
    //Colorpicker
    $('.my-colorpicker1').colorpicker()
    

    //Timepicker
    $('.timepicker').timepicker({
      showInputs: false
    })
    //iCheck for checkbox and radio inputs
    $('input[type="checkbox"].minimal, input[type="radio"].minimal').iCheck({
      checkboxClass: 'icheckbox_minimal-blue',
      radioClass   : 'iradio_minimal-blue'
    })
    //Red color scheme for iCheck
    $('input[type="checkbox"].minimal-red, input[type="radio"].minimal-red').iCheck({
      checkboxClass: 'icheckbox_minimal-red',
      radioClass   : 'iradio_minimal-red'
    })
    //Flat red color scheme for iCheck
    $('input[type="checkbox"].flat-red, input[type="radio"].flat-red').iCheck({
      checkboxClass: 'icheckbox_flat-green',
      radioClass   : 'iradio_flat-green'
    })
  });

/*Validation For  Click On Submit Button   */

 function checkValidate(){
    var countryName=$("#countryName").val();
	var countryCode=$("#countryCode").val();
	
	if(countryName == ""){
		  swal("Country Name should not be blank.");
		  $("#countryName").focus();
		  return false;
	     }
	if(countryCode == ""){
		  swal("Country Code should not be blank.");
		  $("#countryCode").focus();
		  return false;
		
	     }
	else{
		
		 swal("Data Submitted Successfully.", "", "success")
	}
	}

 
</script>
<script> 
/*   Check Validation for Field Data */  
 
 
 function checkCountryName(e){
    var regex = new RegExp("^[a-zA-Z ]");
var str = String.fromCharCode(!e.charCode ? e.which : e.charCode);
var code = (e.keyCode ? e.keyCode : e.which);
if (regex.test(str) || code==8) {
   return true;
}
	swal("Please Enter a Valid Country Name.");
return false;
}


function checkCountryCode(e){
 var regex = new RegExp("^[0-9]");
	 var str = String.fromCharCode(!e.charCode ? e.which : e.charCode);
	 var code = (e.keyCode ? e.keyCode : e.which);
	 if (regex.test(str) || code==8) {
	    return true;
	}
		swal("Please Enter a Valid Country Code.");
	 return false;
}
    </script> 
    
<div id="mainTable">

 <div class="row">
        <div class="col-xs-12">
          <div class="nav-tabs-custom">
            <ul class="nav nav-tabs">
              <li class="active"><a href="addCountry" >Add</a></li>
              <li><a href="viewCountry" >View</a></li>
            </ul>
            <div class="tab-content">
            
              <div class="tab-pane active" id="fa-icons">
                <section id="new">
                  
                  
                  <div class="row">
                  <%-- 	<div class="col-md-12">
	                  	<c:if test="${not empty errMsg}">
	                  			<div class="alert alert-custom-danger fade in alert-dismissible" style="margin-top:18px;">
							    	<a href="#" class="close" data-dismiss="alert" aria-label="close" title="close">x</a>
							    	<strong>Error!</strong>${errMsg}
								</div>
	                  	 </c:if>
	                	 <c:if test="${not empty msg}">
	                			<div class="alert alert-custom-success fade in alert-dismissible" style="margin-top:18px;">
							    	<a href="#" class="close" data-dismiss="alert" aria-label="close" title="close">x</a>
							    	<strong>Success!</strong>${msg}
								</div>
	                	</c:if>
                	 </div> --%>
                  
                  	<form:form class="col-sm-12 form-horizontal customform" action="registerCountry" modelAttribute="countryVo" method="post">
<input type="hidden" name="csrf_token_" value="<c:out value='${csrf_token_}'/>"/>

                  	
                  	
                  	<c:if test="${levelId eq 1}">
                  	
                  	   <div class="form-group">
		                  <label for="inputEmail3" class="col-sm-2 control-label">Country Name<span class="mandatory">*</span></label>
		                  <div class="col-sm-4">
		                    <form:input type="text" class="form-control" path="countryName" maxlength="20" id="countryName" placeholder="Enter Country Name"  onkeypress="return checkCountryName(event);"></form:input>
		                  </div>
		                  <label ></label>
		                  </div>
		                
		             <div class="form-group">
		                  <label for="inputEmail3" class="col-sm-2 control-label">Country Code<span class="mandatory">*</span></label>
		                  <div class="col-sm-4">
		                    <form:input type="text" class="form-control" path="countryCode" maxlength="10" id="countryCode" placeholder="Enter Country Code"  onkeypress="return checkCountryCode(event);"></form:input>
		                  </div>
		                  <label ></label>
		                  </div>
                  	
                  	
                  	</c:if>
                  	
                  	
                  	
                  	
                  	
                  	
                  	
                  	
                  	

                  	
                  	
                  	
                  	
                  	
                  	
		           
		                  
		               <div class="form-group">
			              <label class="col-sm-2 control-label">Status</label>
			              
			              	<div class="col-sm-1">
			              		<label class="myradio">Active
								  <input id="complist" type="radio" value="1" name="radio1" >
								  <span class="checkmark1"></span>
								</label>
			              	
			              	</div>
			              	
			              	<div class="col-sm-1">
			              		<label class="myradio">Inactive
								  <input id="complist" type="radio" value="1" name="radio1" >
								  <span class="checkmark1"></span>
								</label>
			              	
			              	</div>
			              </div>
              <div class="form-group">
		                
		                <div class="form-group">
		                  <div class="col-sm-offset-2 col-sm-10">
		                   <button type="submit" class="btn btn-default">Reset</button>
		                	<button type="submit" class="btn btn-info" onclick="return checkValidate();">Submit</button>
		                  </div>
		                </div>
		              </div></form:form></div></section></div>
                  </div>
                  <hr>
				
              </div>
             
             

            </div>
          </div>
        </div>
      