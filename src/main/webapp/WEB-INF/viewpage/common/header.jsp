<header>
         <nav class="navbar">
            <!-- Navbar brand -->
            <a class="navbar-brand" href="Home" title="Wheat Rust Surveillance Information System">
               <h1><img src="wrsis/images/wrsis-logo.png" alt="Logo" class="lg-logo"> <span>WRSIS</span></h1>
            </a>
            <a class="nav-toggle-btn">
            <span></span>
            </a>
            <!-- Collapse button -->
           
            <!-- Collapsible content -->
            <div class="navbar-collapse d-flex justify-content-end collapse" id="basicExampleNav">
				<div class="nav-logo d-none d-lg-block">
				<a href="http://www.moa.gov.et/" title="Ministry of Agriculture" target="_blank"><img src="wrsis/images/logo.png" alt="Logo" class="float-left mr-2"></a>
					<a href="http://www.ata.gov.et/" title="ATA" target="_blank"><img src="wrsis/images/atl_logo.jpg" alt="Logo" class="float-left"></a>
					<a href="http://www.eiar.gov.et/" title="eiar" target="_blank"><img src="wrsis/images/eiar_logo.jpg" alt="Logo" class="float-left"></a>
					<a href="https://www.cimmyt.org/" title="cimmyt" target="_blank"><img src="wrsis/images/cimmyt_logo.jpg" alt="Logo" class="float-left"></a>
				</div>
				
				
               <!-- Links -->
               <ul class="navbar-nav float-right">
                  <li class="nav-item dropdown">
                     <a class="nav-link dropdown-toggle" id="navbarDropdownMenuLink" data-toggle="dropdown"
                        aria-haspopup="true" aria-expanded="false"><i class="icon-bell1"></i> <span class="nobadge" id="totalUnreadNtf"></span></a>
                     <div class="dropdown-menu notifications scroll-bar-wrap" aria-labelledby="navbarDropdownMenuLink" >
						<div class="scroll-box">
							<ul id="notifications">
								
								<span id="emptyMsg"></span>
							</ul>
						</div>
                     </div>
                  </li>
                  <li class="nav-item">
                     <a class="nav-link setting-link" href="Javascript:void(0);"><i class="icon-settings1"></i></a>
                  </li>
                  <li class="nav-item">
                     <a class="nav-link logout" href="Javascript:void(0);"><i class="icon-log-out1"></i></a>
                  </li>
                  <!-- Dropdown -->
               </ul>
               <!-- Links -->
            </div>
            <!-- Collapsible content -->
         </nav>
         <!--/.Navbar-->
      </header>
      <!-- Modal -->
  <div class="modal fade bd-example-modal-lg" id="notifyModal" role="dialog">
    <div class="modal-dialog modal-dialog-centered">
    
      <!-- Modal content-->
      <div class="modal-content">
        <div class="modal-header">
         
          <h4 class="modal-title">Notification Details</h4>
        </div>
        
        <div class="modal-body">
        		
         		<table data-toggle="table" class="table table-hover table-bordered">
                                 <thead id="tblHd">
                                    
                                 </thead>
                                 <tbody id="tblBdy">
                                   
                                 </tbody>
                              </table>
                              <p id="modalMsg"></p>
                          
        </div>
        <div class="modal-footer">
          <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
        </div>
      </div>
      
    </div>
  </div>
      
       <div class="logout-modal">
         <div class="logou-container text-center">
            <div class="container">
               <h3>Do you really want to log out?</h3>
               <a href="logout" class="btn btn-primary">Yes</a>
               <a class="btn btn-danger nologout">No</a>
            </div>
         </div>
      </div>
      <span id="totalNot" style="display:none;"></span>
      <script>
      $(document).ready(function()
      {
    	  
    	  $.ajax({
      		type : "POST",
      		url : "getUnreadNtfsByUserId",
      		
      		success : function(response) {
      			 
      		
      			var html ='';
      			var val=JSON.parse(response);
      			var html ="";
  					html += val.unreadNtfCount;
  				$("#totalNot").html(val.TotalNot);
  				$('#totalUnreadNtf').append(html);
  				
      		},error : function(error) {
      			 
      		}
      	});
    	  
        	$.ajax({
        		type : "POST",
        		url : "getDesktopNotificationsByUserId", 
        	 
        		
        		success : function(response) {
        			 
        		
        			var html ='';
        			var val=JSON.parse(response);
        			 
        			 /* <li><a>Message for you <span>25-Jun-2019</span></a></li> */ 
        			var html ="";
        			 var fullMsg="";
        			 var shortMsg="";
        			 var ntfId="";
        			 var ntfDate="";
        			 var ntfFrom="";
    				$.each(val,function(i, item) {
    					ntfId=item.ntfId;
    					fullMsg=item.ntfMsg;
    					ntfDate=item.ntfDate;
    					ntfFrom=item.fullname + "("+item.username+")";
    					if(fullMsg.length>15)
    						{
    						shortMsg=fullMsg.substr(0,18).concat("...");
    						}
    					else
    						{
    						shortMsg=fullMsg;
    						}
    					if(item.ntfReadStatus=="false"){
    						html += "<li style='color: #83a750;'><a data-placement='top' data-toggle='modal' data-target='#notifyModal' class='ntf' id='notification"+ntfId+"' onclick='return ntfInfo( \"" + ntfId + "\"  , \"" + fullMsg + "\" , \"" + ntfDate + "\" , \""+ ntfFrom +"\" );'>" + shortMsg + "<span>" + item.ntfDate + "</span></a></l>"; 
    					}
    					else
    						{
    						html += "<li><a data-placement='top' data-toggle='modal' data-target='#notifyModal' class='ntf' id='notification"+ntfId+"' onclick='return ntfInfo( \"" + ntfId + "\"  , \"" + fullMsg + "\" , \"" + ntfDate + "\" , \""+ ntfFrom +"\" );'>" + shortMsg + "<span>" + item.ntfDate + "</span></a></l>"; 
    						}
    				});/* data-placement="top" data-toggle="modal" title="User Info" data-target="#notifyModal" */
    				var totalNot = $("#totalNot").html();
    				totalNot = parseInt(totalNot);
    				if((1*10) < totalNot)
    				{
    				html += "<li class='pt-2 text-center'><input  type='button' value='Load More' class='btn btn-primary' id='loadMore' offset='1' ></li>";
    				}
    				$('#notifications').append(html);
        			
        		},error : function(error) {
        			 
        		}
        	});
        	
        	
        	
        	
        });
      </script>
      <script>
      function ntfInfo(ntfId,fullMsg,ntfDate,ntfFrom)
      {
      	$('#tblHd').empty();
      	$('#tblBdy').empty();
      	$('#modalMsg').empty(); 
      	
      	var htmlBody ="";
		    var htmlHead ='<tr><th>Notification Sent From</th><th>Notification Message</th><th>Date</th></tr>';
			
			htmlBody += '<tr><td>' + ntfFrom+ '</td><td>' + fullMsg + '</td><td>' + ntfDate  + '</td></tr>';
			
			$('#tblHd').append(htmlHead);
			$('#tblBdy').append(htmlBody);
      	
      	
      	 $.ajax({
      		type : "POST",
      		url : "updateNtfReadStatus", 
      	 
      		data : {
      			"notifyId":ntfId
      		},
      		success : function(response) {
      			var val=JSON.parse(response);
    			var html ="";
					html += val.unreadNtfCount;
				
				$('#totalUnreadNtf').html(html);
				$("#notification"+ntfId).css("color","black");
      		},error : function(error) {
      			 
      		}
      	});  
      }
      </script>
      <script>

$(document).on('click','#loadMore',function()
		{
	var offset = $(this).attr('offset');
	$.ajax({
		type : "POST",
		url : "getDesktopNotificationsByUserIdLoadMore", 
	 	data:"offset="+offset,
		
		success : function(response) {
			var offset1 = $(this).attr('offset');
			offset1 = parseInt(offset) +1;
		
			 var html ='';
			var val=JSON.parse(response);
			if(val.length == 0 ){
				/* $('#emptyMsg').html('No Notifications available'); */
			} 
			 /* <li><a>Message for you <span>25-Jun-2019</span></a></li> */ 
			var html ="";
			 var fullMsg="";
			 var shortMsg="";
			 var ntfId="";
			 var ntfDate="";
			 var ntfFrom="";
			$.each(val,function(i, item) {
				ntfId=item.ntfId;
				fullMsg=item.ntfMsg;
				ntfDate=item.ntfDate;
				ntfFrom=item.fullname + "("+item.username+")";
				if(fullMsg.length>15)
					{
					shortMsg=fullMsg.substr(0,18).concat("...");
					}
				else
					{
					shortMsg=fullMsg;
					}
				if(item.ntfReadStatus=="false"){
					html += "<li style='color: #83a750;'><a data-placement='top' data-toggle='modal' data-target='#notifyModal' class='ntf' id='notification"+ntfId+"' onclick='return ntfInfo( \"" + ntfId + "\"  , \"" + fullMsg + "\" , \"" + ntfDate + "\" , \""+ ntfFrom +"\" );'>" + shortMsg + "<span>" + item.ntfDate + "</span></a></l>"; 
				}
				else
					{
					html += "<li><a data-placement='top' data-toggle='modal' data-target='#notifyModal' class='ntf' id='notification"+ntfId+"' onclick='return ntfInfo( \"" + ntfId + "\"  , \"" + fullMsg + "\" , \"" + ntfDate + "\" , \""+ ntfFrom +"\" );'>" + shortMsg + "<span>" + item.ntfDate + "</span></a></l>"; 
					}
				
				
			});/* data-placement="top" data-toggle="modal" title="User Info" data-target="#notifyModal" */
			$('#notifications li:last-child').remove();
			var totalNot = $("#totalNot").html();
			totalNot = parseInt(totalNot);
			if((offset1*10) < totalNot)
				{
			html += "<li><br><center><input  type='button' value='Load More' class='btn btn-primary' id='loadMore' style='width:100px;height:30px;' offset='"+offset1+"' ></center><br></li>";
				}
			
			$('#notifications').append(html);
			
		},error : function(error) {
			 
		}
	});
		});
</script>