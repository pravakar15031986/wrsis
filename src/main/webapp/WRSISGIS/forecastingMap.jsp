<link rel="stylesheet" href="https://openlayers.org/en/v4.6.5/css/ol.css" type="text/css">
<!-- The line below is only needed for old environments like Internet Explorer and Android 4.x -->
<script
    src="https://cdn.polyfill.io/v2/polyfill.min.js?features=requestAnimationFrame,Element.prototype.classList,URL"></script>
<script src="https://openlayers.org/en/v4.6.5/build/ol.js"></script>
<link rel="stylesheet" href="WRSISGIS/css/icomoon.css">
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">
<link rel="stylesheet" href="WRSISGIS/css/style_forecast.css">
<script src="https://cdnjs.cloudflare.com/ajax/libs/jspdf/1.3.4/jspdf.min.js"></script>
<style>
    #map {
        height: 600px;
    }
    
    .legend .card-body{
    	padding:0rem;
    }

    #loaderPage {
        position: absolute;
        width: 100%;
        text-align: center;
        height: 100vh;
        display: block;
        background: rgba(255, 255, 255, 0.61);
        z-index: 999;
    }
  

    /*****POPUP STYLE*****/
    .ol-popup {
        position: absolute;
        background-color: white;
        -webkit-filter: drop-shadow(0 1px 4px rgba(0, 0, 0, 0.2));
        filter: drop-shadow(0 1px 4px rgba(0, 0, 0, 0.2));
        border: 1px solid #cccccc;
        border-radius: 12px;
        bottom: 12px;
        left: -50px;
        min-width: 350px;
        font-size: .9rem;
        font-family: inherit;
    }

    .ol-popup .heading {
        background-color: #83a750;
        padding: 5px;
        color: white;
        border-top-right-radius: 10px;
        border-top-left-radius: 10px;
        margin-top: -1px;
        margin-left: -1px;
    }

    .ol-popup table {
        width: 100%;
        font-size: .7rem;
    }

    .ol-popup tr:nth-child(even) {
        background-color: #f2f2f2;
    }

    .ol-popup:after,
    .ol-popup:before {
        top: 100%;
        border: solid transparent;
        content: " ";
        height: 0;
        width: 0;
        position: absolute;
        pointer-events: none;
    }

    .ol-popup:after {
        border-top-color: white;
        border-width: 10px;
        left: 48px;
        margin-left: -10px;
    }

    .ol-popup:before {
        border-top-color: #cccccc;
        border-width: 11px;
        left: 48px;
        margin-left: -11px;
    }

    .ol-popup-closer {
        text-decoration: none;
        position: absolute;
        top: -1px;
        right: -1px;
        padding: 5px 10px;
        background-color: rgb(234, 183, 48);
        border-top-right-radius: 10px;
    }

    .ol-popup-closer:after {
        content: "X";
    }

    td {
        padding: 2px 10px 3px 10px;
    }

    /****MEASUREMENT TOOLTIP STYLE****/
    .tooltip {
        position: relative;
        background: rgba(0, 0, 0, 0.5);
        border-radius: 4px;
        color: white;
        padding: 4px 8px;
        opacity: 0.7;
        white-space: nowrap;
    }

    .tooltip-measure {
        opacity: 1;
        font-weight: bold;
    }

    .tooltip-static {
        background-color: #ffcc33;
        color: black;
        border: 1px solid white;
    }

    .tooltip-measure:before,
    .tooltip-static:before {
        border-top: 6px solid rgba(0, 0, 0, 0.5);
        border-right: 6px solid transparent;
        border-left: 6px solid transparent;
        content: "";
        position: absolute;
        bottom: -6px;
        margin-left: -7px;
        left: 50%;
    }

    .tooltip-static:before {
        border-top-color: #ffcc33;
    }

    .mapcontainer {
        margin-right: 220px;
        position: relative;
        border: 2px solid #ccc;
        background: #f2efe9;
    }

    .mapfilter {
        width: 220px;
        position: absolute;
        right: 20px;
        top: 15px;
        padding: 0 0 0 15px;
    }

    .expand {
        right: 3px;
        background: rgba(0, 0, 0, 0.82);
        padding: 4px 8px;
        left: auto;
        position: absolute;
        top: 3px;
        font-size: 1.2rem;
    }

    .expand button {
        background: transparent;
        border: 0px;
    }

    .expand button:focus {
        border: 0;
        outline: 0;
    }

    .expand button span {
        color: #fff;
    }

    #open_btn {
        display: none;
    }

    .mapcontainer .Legend ul {
        margin: 10px 0 0 0px;
        padding: 0 0 0 20px;
        font-weight: bold;
        border-bottom: #ccc 1px solid;
    }

    .mapcontainer .Legend li {
        display: inline-block;
        margin: 0 20px 0px 0;
        list-style-type: none;
    }

    .mapcontainer .Legend li p {
        float: left;
        margin-bottom: 5px;
    }
     #pauseBtn:hover,
        #clrBtn:hover {
            cursor: pointer;
        }
        

        .disabled {
            pointer-events: none;
            cursor: not-allowed;
        }

        .activeIcon {
        	color:green;
            text-decoration: none;
        }
        
        .mapPngContainer canvas{
        height:100%;
        width:100%;
        }
        
        
        /* The pdf-modal (background) */
        .pdf-modal {
            display: none;
            /* Hidden by default */
            position: fixed;
            /* Stay in place */
            z-index: 10;
            /* Sit on top */
            padding-top: 30px;
            /* Location of the box */
            left: 0;
            top: 0;
            width: 100%;
            /* Full width */
            height: 100%;
            /* Full height */
            overflow: auto;
            /* Enable scroll if needed */
            background-color: rgb(0, 0, 0);
            /* Fallback color */
            background-color: rgba(0, 0, 0, 0.4);
            /* Black w/ opacity */
        }

        /* pdf-modal Content */
        .pdf-modal-content {
            background-color: rgb(230, 230, 230);
            margin: auto;
            border: 1px solid #888;
            width: 793px;
            border-radius: 5px;
            /* width:793px */
        }

        #pdfMap {
            width: 793px;
            height: 450px;
            padding: 10px;
        }
        .pdfFooter {
            background-color: rgb(220, 220, 220);
            text-align: right;
            padding: 10px;
            border-radius: 5px;
        }

        .previewTitle {
            background-color: rgb(220, 220, 220);
            padding: 10px;
            font-size: 1.2rem;
            color: rgb(54, 54, 54);
            border-radius: 5px;
        }

        #savePdf {
            padding: 10px;
            border-radius: 5px;
            background-color: #01a05d;
            color: white;
            border: none;
        }

        #cancelPdf {
            padding: 10px 20px;
            border-radius: 5px;
            background-color: rgb(243, 243, 243);
            border: none;
        }

        #savePdf:hover {
            cursor: pointer;
            box-shadow: 0px 0px 5px gray;
        }

        #cancelPdf:hover {
            cursor: pointer;
            background-color: rgb(228, 228, 228);
            box-shadow: 0px 0px 10px gray;
        }

        /* The Close Button */
        .closepdf {
            color: #aaaaaa;
            font-size: 28px;
            font-weight: bold;
            float: right;
            margin-top: -0.3rem;
        }

        .closepdf:hover,
        .closepdf:focus {
            color: #000;
            text-decoration: none;
            cursor: pointer;
        }
        .Legend .fa{
        	font-size:1.5rem;
        	margin:4px;
        	margin-bottom:8px;
        }
        .Legend .fa:hover{
        cursor:pointer;
        }
    
</style>
<div class="mainpanel">
    <div class="section">
        <div class="page-title">
            <div class="title-details">
                <h4>View Forecast details on Map</h4>
                <nav aria-label="breadcrumb">
                    <ol class="breadcrumb">
                        <li class="breadcrumb-item"><a href="Home"><span class="icon-home1"></span></a></li>
                        <li class="breadcrumb-item">GIS Map</li>
                        <li class="breadcrumb-item active" aria-current="page">Forecast Map</li>
                    </ol>
                </nav>
            </div>
            
        </div>
        <div class="row">
            <div class="col-md-12 col-sm-12">
                <div class="card">

                    <!-- Search Panel -->
                    <!--===================================================-->
                    <form action="" autocomplete="off" name="myForm">
                        <input type="hidden" name="csrf_token_" value="<c:out value='${csrf_token_}'/>" />
                        <div class="card-body">
                            <div class="Surveysec">
                                <div class="clearfix"></div>
                                <div class="row">
                                    <div class="col-sm-12 col-md-12 ">
                                        <div class="mapcontainer">
                                            <div class="Legend">
                                                <ul>
                                                    <li>
                                                      <!-- <a href="javascript:void(0);" title='Show Timeline View' id='playTool'>
           									                <span class="icon-Timeline_view"></span>
            											</a>  --> 
            											<i class="fa fa-list-alt" title='Show Timeline View' aria-hidden="true" id='playTool'></i>
                                                    </li>
                                                    <li>
                                                        <!-- <a href="javascript:void(0);" title='Change Basemap' >
                											<span class="icon-Basemap"></span>
                											<ul class='baseMapSublist'>
                    											<li><input type='checkbox' name='osm' id='osm' checked disabled=true> <label for='osm'><img
                                									src="WRSISGIS/image/streetmap.png" alt="streetMap" width='100px'></label></li>
                    											<li><input type='checkbox' name='satellite' id='satellite'> <label for='satellite'><img
                                									src="WRSISGIS/image/satelitemap.png" alt="streetMap" width='100px'></label></li>
                											</ul>
            											</a>-->
            											<i id='baseMap' title='Change Basemap'  class="fa fa-map-o" aria-hidden="true"></i>
                                                    	<ul class='baseMapSublist'>
                    											<li><input type='checkbox' name='osm' id='osm' checked disabled=true> <label for='osm'><img
                                									src="WRSISGIS/image/streetmap.png" alt="streetMap" width='100px'></label></li>
                    											<li><input type='checkbox' name='satellite' id='satellite'> <label for='satellite'><img
                                									src="WRSISGIS/image/satelitemap.png" alt="streetMap" width='100px'></label></li>
                											</ul>
                                                    </li>
                                                  <!--  <li>
                                                      <!-- <a href="javascript:void(0);" title='Rotate Map' id='rotate'>
                											<span class="icon-Rotate_map"></span>
            											</a>
            											<i id='rotate' title='Rotate Map' class="fa fa-repeat" aria-hidden="true"></i>
                                                    </li>-->
                                                    <li>
                                                       <!-- <a href="javascript:void(0);" title='Go to Full Extent' id='home'>
                											<span class="icon-Home"></span>
            											</a>-->
            											<i id='home' title='Go to Full Extent'  class="fa fa-window-restore" aria-hidden="true"></i>
                                                    </li>
                                                    <li>
                                                        <!-- <a href="javascript:void(0);" title='Measurement' id='draw'>
                											<span class="icon-measurement"></span>
            											</a> -->
            											<i id='draw' title='Measurement'  class="fa fa-thermometer-three-quarters" aria-hidden="true"></i>
                                                    </li>
                                                    <li>
                                                    	<!-- <a href="javascript:void(0);" title='Draw on Map' id='drawMark' style='height:76px;'>
                											<img src="WRSISGIS/image/pencil.svg" height="24px" style="margin: -5px auto 0px;padding: 0 6px;">
            											</a> -->
            											<i id='drawMark' title='Draw on Map' class="fa fa-pencil" aria-hidden="true"></i>
                                                    </li>
                                                    <li>
                                                    	<!-- <a href="javascript:void(0);" title='Full Screen' id='fullscreen'>
                											<span class="icon-Full_scrren"></span>
            											</a> -->
            											<i id='fullscreen' title='Full Screen'  class="fa fa-arrows-alt" aria-hidden="true"></i>
                                                    </li>
                                                    <li>
                                                    	<!--<a href="javascript:void(0);" title='Overview Control' id='overview'>
                											<span class="icon-Overview_control"></span>
            											</a> -->
            											<i id='overview' title='Overview Control' class="fa fa-television" aria-hidden="true"></i>
                                                    </li>
                                                    <li>
                                                    	<!--<a href="javascript:void(0);" title='Export PDF' id='exportPDF'>
                											<span class="icon-PDF"></span>
            											</a> -->
            											<i id='exportPDF' title='Export PDF' class="fa fa-file-pdf-o" aria-hidden="true"></i>
                                                    </li>
                                                    <li>
                                                    	<!--<a href="javascript:void(0);" title='Print' data-toggle="modal" data-target="#printmodal">
                											<span class="icon-Print"></span>
            											</a> -->
            											<i  class="fa fa-print" title='Print' aria-hidden="true" data-toggle="modal" data-target="#printmodal"></i>
                                                    </li>
                                                </ul>


                                            </div>
                                            <div id="map">
                                            	<div class="description"> Lorem Ipsum is simply dummy text of the printing and typesetting industry </div>

            <div class="selectBox1">
                <button id='pauseBtn' type='button' class="buttons" title='play/pause'><i class="fa fa-pause"
                        aria-hidden="true"></i></button> <br>
                <button id='clrBtn'  type='button'  class="buttons" title='stop'><i class="fa fa-stop" aria-hidden="true"></i></button>
            </div>
                                            </div>
                                            <div class="expand">
                                                <!-- sidebar close button -->
                                                <button type="button" id="close_btn" class="border-0">
                                                    <span class="fa fa-arrows-alt"></span>
                                                </button>
                                                <!-- sidebar close button -->
                                                <!-- open button for sidebar -->
                                                <button type="button" id="open_btn" class="border-0">
                                                    <span class="fa fa-compress"></span>
                                                </button>
                                                <!-- open button for sidebar -->
                                            </div>
                                        </div>

                                        


                                        
                                    </div>


                                    <div class="rightpannel mapfilter">
                                        <div class="form-group row">
                                            <label class="col-12 col-md-12 col-xl-12 control-label"
                                                for="demo-email-input"><strong>Select Rust Type :</strong></label>
                                            <div class="col-12 col-md-12 col-xl-12">
                                                <select class='form-control' id='rustType'>
													<option>--Select Rust Type--</option>
                    							</select>
                                            </div>
                                        </div>
                                        
                                        
                                        <!-- <div class="form-group row">
                                            <label class="col-12 col-md-12 col-xl-12 control-label"
                                                for="demo-email-input"><strong>Select Map Type :</strong></label>
                                            <div class="col-12 col-md-12 col-xl-12">
                                                <select class='form-control' id='rustType'>

                    							</select>
                                            </div>
                                        </div>-->
                                        <div class="legend">
                                        <label><strong>Map Type :</strong></label>
                <div class="accordion" id="accordionExample">
                    <div class="card" style="margin-left:-5px;">
                        <div class="card-head" id="headingOne">
                            <div class="mb-0" data-toggle="collapse" data-target="#collapseOne" aria-expanded="true"
                                aria-controls="collapseOne">
                                <input type='radio' name='layerType' id='Deposition' value="deposition" checked
                                   ><label for="Deposition"><strong>Deposition</strong></label><br>
                            </div>
                        </div>

                        <div id="collapseOne" class="collapse show" aria-labelledby="headingOne"
                            data-parent="#accordionExample">
                            <div class="card-body">
                                <div class="checkbox" id='none'>
                                    <input id="checkbox1" class='depositionChkBox' type="checkbox" value='none/trace' checked>
                                    <label for="checkbox1">None/Trace (0 - 10)</label>
                                </div>

                                <div class="checkbox" id='low'>
                                    <input id="checkbox1" class='depositionChkBox' type="checkbox" value='low' checked>
                                    <label for="checkbox1">Low (10 - 1e+6)</label>
                                </div>

                                <div class="checkbox" id='medium'>
                                    <input id="checkbox1" class='depositionChkBox' type="checkbox" value='moderate' checked>
                                    <label for="checkbox1">Moderate (1e+6 - 1e+8)</label>
                                </div>

                                <div class="checkbox" id='high'>
                                    <input id="checkbox1" class='depositionChkBox' type="checkbox" value='high' checked>
                                    <label for="checkbox1">High (1e+8 - 1e+11)</label>
                                </div>

                                <div class="checkbox" id='veryHigh'>
                                    <input id="checkbox1" class='depositionChkBox' type="checkbox" value='very high' checked>
                                    <label for="checkbox1">Very High (1e+11 -)</label>
                                </div>

                            </div>
                        </div>
                    </div>
                    <div class="card" style="margin-left:-5px;">
                        <div class="card-head" id="headingTwo">
                            <div class="mb-0 collapsed" data-toggle="collapse" data-target="#collapseTwo"
                                aria-expanded="false" aria-controls="collapseTwo">
                                <input type='radio' name='layerType' id='Environment' value="environment"
                                   >&nbsp;<label for="Environment"><strong>Suitability</strong></label><br>
                            </div>
                        </div>
                        <div id="collapseTwo" class="collapse" aria-labelledby="headingTwo"
                            data-parent="#accordionExample">
                            <div class="card-body">
                                <div class="checkbox" id='none'>
                                    <input id="checkbox12" class='environmentChkBox' type="checkbox" value='no'>
                                    <label for="checkbox12">None/Trace (0%)</label>
                                </div>

                                <div class="checkbox" id='low'>
                                    <input id="checkbox13" class='environmentChkBox' type="checkbox" value='low'>
                                    <label for="checkbox13">Low (0% - 10.5%)</label>
                                </div>

                                <div class="checkbox" id='medium'>
                                    <input id="checkbox14" class='environmentChkBox' type="checkbox" value='moderate'>
                                    <label for="checkbox14">Moderate (10.5% - 21%)</label>
                                </div>

                                <div class="checkbox" id='high'>
                                    <input id="checkbox15" class='environmentChkBox' type="checkbox" value='high'>
                                    <label for="checkbox15">High (21% - 31.5%)</label>
                                </div>

                                <div class="checkbox" id='veryHigh'>
                                    <input id="checkbox16" class='environmentChkBox' type="checkbox" value='very high'>
                                    <label for="checkbox16">Very High(31.5%-42.1%)</label>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>

                                        

                                       

                                    </div>


                                </div>

                            </div>
                            <!-- Alert Modal -->
                            <div id="alertModal" class="modal3">

        <!-- Modal content -->
        <div class="modal-content3">
            <div class="modal-header3">
                <span class="close3">&times;</span>
            </div>
            <div>
                <div class="modal-body3">
                    <h4>Want to play timeline view</h4>
                </div>
            </div>
            <div class="modal-footer3">
                <button type='button' id="modalConfirm">Play Now</button>
                <button type='button' id="modalClose">Cancel</button>
            </div>
        </div>

    </div>

                             <!--popup content-->
    <div id="popup" class="ol-popup">
        <a href="#" id="popup-closer" class="ol-popup-closer"></a>
        <div id="popup-content"></div>
    </div>

    <!-- Print Modal popup-->
    <div class="modal fade" id="printmodal" tabindex="-1" role="dialog" aria-labelledby="exampleModalCenterTitle"
        aria-hidden="true">
        <div class="modal-dialog modal-dialog-centered modalprint-lg" role="document">
            <div class="modal-content">
                <div class="modal-header">
                    <h4 class="modal-title" id="exampleModalCenterTitle">Incident Rust Survey Detail on Map </h4>
                    <div class="text-right float-right">

                        <a id="downloadMap" href="#"><button type="button" class="btn btn-success">Save Map</button></a>
                        <button type="button" class="close " data-dismiss="modal" aria-label="Close">
                            <span aria-hidden="true">&times;</span>
                        </button>
                    </div>


                </div>
                <div class="modal-body">
                    <div class="row mapPngContainer">
                        <div class="col-sm-12">
                            <!-- <img src="image/print-popup-img.jpg" class="img-fluid w-100" /> -->
                            <div class="mapPng" style='height:800px;height:90vh;'></div>
                        </div>
                        <div class="col-sm-4" style='background-color:rgba(255,255,255,0.7);height:35vh;z-index:5;position:absolute;top:0;right:0;'>
                            <h4> Deposition</h4>
                            <div class="checkbox" id="none">
                                <!-- <input id="checkbox1" class="depositionChkBox" type="checkbox" value="none/trace"> -->
                                <label for="checkbox1" class='legendList1'>None/Trace (0 - 10)</label>
                            </div>

                            <div class="checkbox" id="low">
                                <!-- <input id="checkbox1" class="depositionChkBox" type="checkbox" value="low"> -->
                                <label for="checkbox1" class='legendList2'>Low (10 - 1e+06)</label>
                            </div>

                            <div class="checkbox" id="medium">
                                <!-- <input id="checkbox1" class="depositionChkBox" type="checkbox" value="moderate"> -->
                                <label for="checkbox1" class='legendList3'>Moderate (1e+06 - 1e+08)</label>
                            </div>

                            <div class="checkbox" id="high">
                                <!-- <input id="checkbox1" class="depositionChkBox" type="checkbox" value="high"> -->
                                <label for="checkbox1" class='legendList4'>High (1e+08 - 1e+11)</label>
                            </div>

                            <div class="checkbox" id="veryHigh">
                                <!-- <input id="checkbox1" class="depositionChkBox" type="checkbox" value="very high"> -->
                                <label for="checkbox1" class='legendList5'>Very High (1e+11 -)</label>
                            </div>

                        </div>
                    </div>
                </div>

            </div>
        </div>
    </div>
    <!-- Print Modal popup-->
    
    <!-- PDF Previe Modal -->
    <div id="pdfPreview" class="pdf-modal">
        <!-- pdf-modal content -->
        <div class="pdf-modal-content">
            <div style="padding:10px 10px 0px 10px;">
                <div class="previewTitle">
                    <span>PDF Preview</span><span title="close" class="closepdf">&times;</span>
                </div>
            </div>

            <div id='pdfMap'>

            </div>
            <div style="padding:0px 10px 10px 10px;">
                <div class="pdfFooter">
                    <button type="button" id='cancelPdf' title="Cancel">Cancel</button>&nbsp;
                    <button type="button" id='savePdf' title="Save PDF">Export PDF Now</button>
                </div>
            </div>
        </div>
    </div>
    <!-- PDF Previe Modal -->
                            
                            <link href="wrsis/css/bootstrap-datepicker.min.css" rel="stylesheet">
                            <script src="wrsis/js/bootstrap-datepicker.min.js"></script>
                            <script>
                            /***************Popup Configuration*************/
                            /**
                          * Elements that make up the popup.
                          */
                            var container = document.getElementById('popup');
                            var content = document.getElementById('popup-content');
                            var closer = document.getElementById('popup-closer');


                            /**
                             * Create an overlay to anchor the popup to the map.
                             */
                            var overlay = new ol.Overlay({
                                element: container,
                                autoPan: true,
                                autoPanAnimation: {
                                    duration: 250
                                }
                            });


                            /**
                             * Add a click handler to hide the popup.
                      
                             */
                            closer.onclick = function () {
                                overlay.setPosition(undefined);
                                closer.blur();
                                return false;
                            };
                            /***************Popup Configuration*************/




                            /*************Base Source and Base Layer of the Map*******************/
                            var baseSource = new ol.source.OSM();
                            var baseLayer = new ol.layer.Tile({
                                source: baseSource
                            });

                            var boundaryLayer = new ol.layer.Image({
                                source: new ol.source.ImageWMS({
                                    url: '${GIS_URL}',
                                    params: { 'LAYERS': '${WORKSPACE}:Ethiopia_boundary' },
                                    serverType: "geoserver",
                                    crossOrigin: 'anonymous'
                                })
                            });






                            /*************Initializing Map*******************/
                            var latestLayer;
                            var map = new ol.Map({
                                layers: [baseLayer,boundaryLayer],
                                target: 'map',
                                overlays: [overlay],
                                view: new ol.View({
                                    center: new ol.proj.fromLonLat([40.4897, 9.1450]),
                                    zoom: 6,
                                    maxZoom: 15
                                })
                            });


                            /**************Adding scaleline control for map****************/
                            var scaleLineControl = new ol.control.ScaleLine();
                            map.addControl(scaleLineControl);




                            /*Get GIS Master Data upon document Ready*/
                            $(document).ready(function () {
                                $(".baseMapSublist").hide();
                                var settings = {
                                    "url": "gisapi/getMasterDataForGISMap",
                                    "method": "POST",
                                    "timeout": 0,
                                };

                                $.ajax(settings).done(function (response) {
                                    var response = JSON.parse(response);
                                    if (response.status == 200) {
                                        var rustData = response.response.diseaseData;
                                        var mapData = response.response.mapTypeData;
                                        $("#rustType").html('');
                                        for (var i = 0; i < rustData.length; i++) {
                                            $("#rustType").append('<option value="' + rustData[i].rustId + '">' + rustData[i].rustName + '</option>');
                                        }
                                        // console.log(mapData)
                                        for (var i = 0; i < mapData.length; i++) {
                                            if (mapData[i].mapName == "Deposition") {
                                                $("#Deposition").val(mapData[i].mapId);
                                            } else {
                                                $("#Environment").val(mapData[i].mapId);
                                            }
                                        }

                                        getLatestLayer();
                                    }
                                });
                            });

                            $('#rustType').change(function () {
                                getLatestLayer();
                            });


                            /*******Fetching Latest Layer and creating Layer****************/
                            var filterQry = "";
                            var wmsSource;
                            var latestLayerName;
                            function getLatestLayer() {
                                /**remove popup*/
                                overlay.setPosition(undefined)
                                closer.blur();
                                map.removeLayer(latestLayer);
                                /**remove popup*/
                                var rustType = $('#rustType').val();
                                var layerType = $("input[name='layerType']:checked").val();
                                filterQry = "";
                                if (layerType == '1') {
                                    $(".depositionChkBox").each(function () {
                                        if ($(this).is(":checked")) {
                                            if (filterQry == "") {
                                                filterQry = "'" + $(this).val() + "'";
                                            } else {
                                                filterQry += ",'" + $(this).val() + "'";
                                            }
                                        }
                                    });
                                    if (filterQry == "") {
                                        filterQry = "'none/trace','low','moderate','high','very high'";
                                    }
                                } else {
                                    $(".environmentChkBox").each(function () {
                                        if ($(this).is(":checked")) {
                                            if (filterQry == "") {
                                                filterQry = "'" + $(this).val() + "'";
                                            } else {
                                                filterQry += ",'" + $(this).val() + "'";
                                            }
                                        }
                                    });
                                    if (filterQry == "") {
                                        filterQry = "'no','low','moderate','high','very high'";
                                    }
                                }


                                var settings = {
                                    "url": "gisapi/getLatestGISData",
                                    "method": "POST",
                                    "timeout": 0,
                                    "headers": {
                                        "Content-Type": "application/json"
                                    },
                                    "data": JSON.stringify({ "disease": rustType, "mapTypeId": layerType }),
                                };
                                $.ajax(settings).done(function (response) {
                                    var response = JSON.parse(response);
                                    if (response.status == 200) {
                                        if (response.response.gisLatestData != '') {
                                            latestLayerName = response.response.gisLatestData[0].layerName;
                                            wmsSource = new ol.source.ImageWMS({
                                                url: '${GIS_URL}',
                                                params: { 'LAYERS': '${WORKSPACE}:' + latestLayerName/*, "CQL_FILTER": "Category IN(" + filterQry + ")" */ },
                                                serverType: "geoserver",
                                                crossOrigin: 'anonymous'
                                            });
                                            latestLayer = new ol.layer.Image({
                                                source: wmsSource
                                            });

                                            map.addLayer(latestLayer);
                                            map.removeLayer(boundaryLayer);
                                            map.addLayer(boundaryLayer);
                                        }
                                    }
                                });



                                getHistoryLayer(rustType, layerType, filterQry);

                                if (drawMode == 1) {
                                    map.removeLayer(drawingLayer)
                                    map.addLayer(drawingLayer)
                                }
                            }

                            $(".environmentChkBox").change(function () {
                                if ($("#Environment").is(":checked")) {
                                    var rustType = $('#rustType').val();
                                    var layerType = $("input[name='layerType']:checked").val();
                                    filterQry = "";
                                    $(".environmentChkBox").each(function () {
                                        if ($(this).is(":checked")) {
                                            if (filterQry == "") {
                                                filterQry = "'" + $(this).val() + "'";
                                            } else {
                                                filterQry += ",'" + $(this).val() + "'";
                                            }
                                        }
                                    });
                                    if (filterQry == "") {
                                        filterQry = "'no','low','moderate','high','very high'";
                                    }

                                    wmsSource = new ol.source.ImageWMS({
                                        url: '${GIS_URL}',
                                        params: { 'LAYERS': '${WORKSPACE}:' + latestLayerName, "CQL_FILTER": "Category IN(" + filterQry + ")" },
                                        serverType: "geoserver",
                                        crossOrigin: 'anonymous'
                                    });
                                    latestLayer.setSource(null);
                                    latestLayer.setSource(wmsSource);
                                    getHistoryLayer(rustType, layerType, filterQry);
                                }
                            });

                            $(".depositionChkBox").change(function () {
                                if ($("#Deposition").is(":checked")) {
                                    var rustType = $('#rustType').val();
                                    var layerType = $("input[name='layerType']:checked").val();
                                    filterQry = "";
                                    $(".depositionChkBox").each(function () {
                                        if ($(this).is(":checked")) {
                                            if (filterQry == "") {
                                                filterQry = "'" + $(this).val() + "'";
                                            } else {
                                                filterQry += ",'" + $(this).val() + "'";
                                            }
                                        }
                                    });
                                    if (filterQry == "") {
                                        filterQry = "'none/trace','low','moderate','high','very high'";
                                    }

                                    wmsSource = new ol.source.ImageWMS({
                                        url: '${GIS_URL}',
                                        params: { 'LAYERS': '${WORKSPACE}:' + latestLayerName, "CQL_FILTER": "Category IN(" + filterQry + ")" },
                                        serverType: "geoserver",
                                        crossOrigin: 'anonymous'
                                    });
                                    latestLayer.setSource(null);
                                    latestLayer.setSource(wmsSource);
                                    getHistoryLayer(rustType, layerType, filterQry);
                                }
                            });








                            /**************Show latest Layer upon clicking on show map Button***********************/
                            $("#Deposition, #Environment").click(function () {
                                $(".baseMapSublist").hide();
                                getLatestLayer();

                    			/*Check all checkbox of rust types upon clicking on rust type radio button*/
                    			var ele=$(this).parent().parent().parent().find('div:last-child').find('.card-body .checkbox').find('input');
                    			var count=0;
                    			$(ele).each(function(){
                    				if($(this).is(":checked")==false){
                    					count++;
                    				}
                    			})
                    			if(count==5){
                    				$(ele).prop("checked",true);
                    			}

                    			if($(this).val()==1){
                    				$("#Environment").parent().parent().parent().find('div:last-child').find('.card-body .checkbox').find('input').prop("checked",false);
                    			}else{
                    				$("#Deposition").parent().parent().parent().find('div:last-child').find('.card-body .checkbox').find('input').prop("checked",false);
                    			}

                    			
                    			
                                

                                if (drawMode == 1) {
                                    map.removeLayer(drawingLayer)
                                    map.addLayer(drawingLayer)
                                }
                            });

                            var modal3 = document.getElementById("alertModal");
                            $(".close3").click(function () {
                                modal3.style.display = "none";
                            });
                            $("#modalClose").click(function () {
                                modal3.style.display = "none";
                            });
                            window.onclick = function (event) {
                                if (event.target == modal3) {
                                    modal3.style.display = "none";
                                }
                            }


                            var arrayLayer = [];
                            function getHistoryLayer(rustType, layerType, filterQry) {
                                var settings = {
                                    "url": "gisapi/getGISHistoryData",
                                    "method": "POST",
                                    "timeout": 0,
                                    "headers": {
                                        "Content-Type": "application/json"
                                    },
                                    "data": JSON.stringify({ "disease": rustType, "mapTypeId": layerType }),
                                };

                                $.ajax(settings).done(function (response) {
                                    var response = JSON.parse(response);
                                    arrayLayer = [];
                                    if (response.status == 200) {
                                        if (response.response.gisHistoryData != '') {
                                            var resData = response.response.gisHistoryData;
                                            //console.log(resData)
                                            for (var i = 0; i < resData.length; i++) {
                                                var layer = new ol.layer.Image({
                                                    source: new ol.source.ImageWMS({
                                                        url: '${GIS_URL}',
                                                        params: { 'LAYERS': '${WORKSPACE}:' + resData[i].layerName,"CQL_FILTER":"Category IN("+filterQry+")" },
                                                        serverType: "geoserver",
                                                        crossOrigin: 'anonymous'
                                                    })
                                                });
                                                //arrayLayer is the collection of last 15 days layers fetched by api*/
                                                arrayLayer.push(layer);
                                            }
                                        }
                                    }
                                });
                            }




                            /**************TimeLine View***********************/
                            $("#playTool").click(function () {
                                $("#alertModal").show();
                            });
                            // var confirm = window.confirm('Click Ok to Play timeline View');
                            $("#modalConfirm").click(function () {
                                $("#alertModal").hide();
                                $(".description").show();
                                $('.selectBox1').show();
                                setIconMode(0);
                                pauseBtn.disabled = false;
                                clrBtn.disabled = false;
                                map.removeLayer(latestLayer);
                               
                                playTimelineView();

                            });


                            /********************Show Timeline view********************/
                            function playTimelineView() {
                                i = 0;
                                opacityValue = 0.001;
                                map.removeLayer(arrayLayer[0]);   
                                map.addLayer(arrayLayer[0]);
                                map.removeLayer(boundaryLayer);
                                map.addLayer(boundaryLayer);
                                $('.description').html(arrayLayer[0].getSource().a.LAYERS.split(':')[1]);
                                fadeIn();
                            }

                            var opacityValue = 0.001;
                            var i = 0;
                            var timeoutId;

                            function fadeIn() {
                                arrayLayer[i].setOpacity(opacityValue);
                                if (opacityValue < 1) {
                                    opacityValue += 0.001;
                                    timeoutId = setTimeout(fadeIn, .05);
                                } else {
                                    opacityValue = 0.001;
                                    i++;
                                    if (i < arrayLayer.length - 1) {
                                        arrayLayer[i].setOpacity(0.0);
                                        map.removeLayer(arrayLayer[i]);
                                        map.addLayer(arrayLayer[i]);
                                        $('.description').html(arrayLayer[i].getSource().a.LAYERS.split(':')[1])
                                        map.removeLayer(boundaryLayer);
                                        map.addLayer(boundaryLayer);
                                        if (i < arrayLayer.length - 1) {
                                            fadeIn();
                                        }
                                    } else {
                                        playTimelineView();
                                    }
                                }
                            }


                            /*Play Pause controllers*/
                            var status = 1;
                            var pauseBtn = document.getElementById('pauseBtn');
                            pauseBtn.addEventListener('click', function () {
                                status++;
                                $("#pauseBtn i").toggleClass('fa fa-pause fa fa-play');
                                if (status % 2 == 0) {
                                    clearTimeout(timeoutId);
                                } else {
                                    fadeIn();
                                }
                            });

                            var clrBtn = document.getElementById('clrBtn');
                            clrBtn.addEventListener('click', function () {
                                $('.description').hide();
                                $('.selectBox1').hide();
                                pauseBtn.disabled = true;
                                clrBtn.disabled = true;
                                setIconMode(1);
                                clearTimeout(timeoutId);
                                status = 1;
                                for (var i = 0; i < arrayLayer.length - 1; i++) {
                                    map.removeLayer(arrayLayer[i]);
                                }
                                getLatestLayer();
                            });


                            /**************Base Map Changing function************/

                            $("#baseMap").click(function () {
                                $(".baseMapSublist").toggle();
                            });

                            $("#osm").click(function () {
                                $("#satellite").prop("checked", false);
                                $("#satellite").attr('disabled', false);
                                $("#osm").attr('disabled', true);
                                var baseSource = new ol.source.OSM();
                                baseLayer.setSource(baseSource);
                            });

                            $("#satellite").click(function () {
                                $("#osm").prop("checked", false);
                                $("#osm").attr('disabled', false);
                                $("#satellite").attr('disabled', true);
                                var baseSource = new ol.source.BingMaps({
                                    key:
                                        "AhwEak-Pn6513R37o8zqIfN3HutOSk38RYKI5HGCg-lpYFfCwdGQM0d9i4GNgtlJ",
                                    imagerySet: "AerialWithLabels"
                                });
                                baseLayer.setSource(baseSource);
                            });


                            /********Zoom to center*************/
                            $("#home").click(function () {
                                map.getView().animate({
                                    zoom: 6,
                                    duration: 2000,
                                    center: new ol.proj.fromLonLat([40.4897, 9.1450])
                                });
                            });


                            /********Full Screen Control***************/
                            $("#fullscreen").click(function () {
                                openFullscreen();
                            });
                            var elem = document.getElementById("map");
                            function openFullscreen() {
                                if (elem.requestFullscreen) {
                                    elem.requestFullscreen();
                                } else if (elem.mozRequestFullScreen) {
                                    /* Firefox */
                                    elem.mozRequestFullScreen();
                                } else if (elem.webkitRequestFullscreen) {
                                    /* Chrome, Safari and Opera */
                                    elem.webkitRequestFullscreen();
                                } else if (elem.msRequestFullscreen) {
                                    /* IE/Edge */
                                    elem.msRequestFullscreen();
                                }
                            }


                            /****ADDING MEASUREMENT TOOL****/
                            var measurementVal = 1;
                            $("#draw").click(function () {
                                if (measurementVal == 1) {
                                    map.addLayer(vectorDraw);
                                    addInteraction();
                                    measurementVal = 2;
                                } else {
                                    //measurementVal = 1
                                    clearDraw();
                                }
                            });


                            var sourceDraw = new ol.source.Vector();
                            var vectorDraw = new ol.layer.Vector({
                                source: sourceDraw,
                                style: new ol.style.Style({
                                    fill: new ol.style.Fill({
                                        color: "rgba(255, 255, 255, 0.2)"
                                    }),
                                    stroke: new ol.style.Stroke({
                                        color: "#ffcc33",
                                        width: 2
                                    }),
                                    image: new ol.style.Circle({
                                        radius: 7,
                                        fill: new ol.style.Fill({
                                            color: "#ffcc33"
                                        })
                                    })
                                })
                            });

                            /**
                             * Currently drawn feature.
                             * @type {ol.Feature}
                             */
                            var sketch;

                            /**
                             * The help tooltip element.
                             * @type {Element}
                             */
                            var helpTooltipElement;

                            /**
                             * Overlay to show the help messages.
                             * @type {ol.Overlay}
                             */
                            var helpTooltip;

                            /**
                             * The measure tooltip element.
                             * @type {Element}
                             */
                            var measureTooltipElement;

                            /**
                             * Overlay to show the measurement.
                             * @type {ol.Overlay}
                             */
                            var measureTooltip;


                            var draw; // global so we can remove it later

                            function addInteraction() {
                                /**
                                 * Message to show when the user is drawing a line.
                                 * @type {string}
                                 */
                                var continueLineMsg = "Click to continue drawing the line";
                                var type = "LineString";
                                draw = new ol.interaction.Draw({
                                    source: sourceDraw,
                                    type: type,
                                    style: new ol.style.Style({
                                        fill: new ol.style.Fill({
                                            color: "rgba(255, 255, 255, 0.2)"
                                        }),
                                        stroke: new ol.style.Stroke({
                                            color: "rgba(0, 0, 0, 0.5)",
                                            lineDash: [10, 10],
                                            width: 2
                                        }),
                                        image: new ol.style.Circle({
                                            radius: 5,
                                            stroke: new ol.style.Stroke({
                                                color: "rgba(0, 0, 0, 0.7)"
                                            }),
                                            fill: new ol.style.Fill({
                                                color: "rgba(255, 255, 255, 0.2)"
                                            })
                                        })
                                    })
                                });
                                map.addInteraction(draw);

                                createMeasureTooltip();
                                createHelpTooltip();

                                var listener;
                                draw.on(
                                    "drawstart",
                                    function (evt) {

                                        $('.ol-overlay-container').find('.tooltip-static').hide();
                                        // set sketch
                                        sketch = evt.feature;

                                        /** @type {ol.Coordinate|undefined} */
                                        var tooltipCoord = evt.coordinate;

                                        listener = sketch.getGeometry().on("change", function (evt) {
                                            var geom = evt.target;
                                            var output;
                                            output = formatLength(geom);
                                            //console.log(output)
                                            tooltipCoord = geom.getLastCoordinate();
                                            measureTooltipElement.innerHTML = output;
                                            measureTooltip.setPosition(tooltipCoord);
                                        });
                                    },
                                    this
                                );

                                draw.on(
                                    "drawend",
                                    function (e) {
                                        measureTooltipElement.className = "tooltip tooltip-static";
                                        measureTooltip.setOffset([0, -7]);
                                        // unset sketch
                                        sketch = null;
                                        // unset tooltip so that a new one can be created
                                        measureTooltipElement = null;
                                        createMeasureTooltip();
                                        ol.Observable.unByKey(listener);
                                    },
                                    this
                                );

                                /**
                                 * Creates a new help tooltip
                                 */
                                function createHelpTooltip() {
                                    if (helpTooltipElement) {
                                        helpTooltipElement.parentNode.removeChild(helpTooltipElement);
                                    }
                                    helpTooltipElement = document.createElement("div");
                                    helpTooltipElement.className = "tooltip hidden";
                                    helpTooltip = new ol.Overlay({
                                        element: helpTooltipElement,
                                        offset: [15, 0],
                                        positioning: "center-left"
                                    });
                                    map.addOverlay(helpTooltip);
                                }

                                /**
                                 * Creates a new measure tooltip
                                 */
                                function createMeasureTooltip() {
                                    if (measureTooltipElement) {
                                        measureTooltipElement.parentNode.removeChild(measureTooltipElement);
                                    }
                                    measureTooltipElement = document.createElement("div");
                                    measureTooltipElement.className = "tooltip tooltip-measure";
                                    measureTooltip = new ol.Overlay({
                                        element: measureTooltipElement,
                                        offset: [0, -15],
                                        positioning: "bottom-center"
                                    });
                                    map.addOverlay(measureTooltip);
                                }

                                var pointerMoveHandler = function (evt) {
                                    if (evt.dragging) {
                                        return;
                                    }
                                    /** @type {string} */
                                    var helpMsg = "Click to start drawing";

                                    if (sketch) {
                                        var geom = sketch.getGeometry();
                                        if (geom instanceof ol.geom.Polygon) {
                                            helpMsg = continuePolygonMsg;
                                        } else if (geom instanceof ol.geom.LineString) {
                                            helpMsg = continueLineMsg;
                                        }
                                    }
                                    //console.log(helpMsg)
                                    helpTooltipElement.innerHTML = helpMsg;

                                    helpTooltip.setPosition(evt.coordinate);

                                    helpTooltipElement.classList.remove("hidden");
                                };
                                map.on("pointermove", pointerMoveHandler);

                                map.getViewport().addEventListener("mouseout", function () {
                                    helpTooltipElement.classList.add("hidden");
                                });

                                var typeSelect = document.getElementById("type");

                                /**
                                 * Format length output.
                                 * @param {ol.geom.LineString} line The line.
                                 * @return {string} The formatted length.
                                 */
                                var formatLength = function (line) {
                                    var length = ol.Sphere.getLength(line);
                                    var output;
                                    if (length > 100) {
                                        output = Math.round((length / 1000) * 100) / 100 + " " + "km";
                                    } else {
                                        output = Math.round(length * 100) / 100 + " " + "m";
                                    }
                                    return output;
                                };

                                /**
                                 * Format area output.
                                 * @param {ol.geom.Polygon} polygon The polygon.
                                 * @return {string} Formatted area.
                                 */
                                var formatArea = function (polygon) {
                                    var area = ol.Sphere.getArea(polygon);
                                    var output;
                                    if (area > 10000) {
                                        output =
                                            Math.round((area / 1000000) * 100) / 100 + " " + "km<sup>2</sup>";
                                    } else {
                                        output = Math.round(area * 100) / 100 + " " + "m<sup>2</sup>";
                                    }
                                    return output;
                                };
                            }

                            function clearDraw() {//Function to clear the draw
                                measurementVal = 1;
                                map.removeInteraction(draw);
                                map.removeLayer(vectorDraw);
                                sourceDraw.clear();
                                map.removeOverlay(helpTooltip);
                                map.removeOverlay(measureTooltip);
                                $('.ol-overlay-container').find('.tooltip-static').hide()
                            }


                            /********EXPORT PDF***********************/
                            var pdfMap = '';
                            $("#exportPDF").click(function () {
                                $("#pdfMap").html('');
                                $("#pdfPreview").show();
                                pdfMap = new ol.Map({
                                    layers: map.getLayers().a,
                                    target: 'pdfMap',
                                    controls: ol.control.defaults({
                                        attributionOptions: {
                                            collapsible: false
                                        }
                                    })
                                });
                                pdfMap.setView(map.getView());
                                pdfMap.renderSync();
                            });

                            $("#cancelPdf,.closepdf").click(function () {
                                $("#pdfPreview").hide();
                            });

                            $("#savePdf").click(function () {
                                var conf = confirm("Are you Sure ?");
                                if (conf == true) {
                                    var data = $("#pdfMap").find('canvas')[0].toDataURL("image/png");
                                    var pdf = new jsPDF('landscape');
                                    pdf.addImage(data, 10, 10);
                                    var cdate = new Date();
                                    var pdfName = "WRSISPDF_" + cdate.getDate() + "" + (cdate.getMonth() + 1) + "" + cdate.getFullYear() + "" + cdate.getHours() + "" + cdate.getMinutes() + "" + cdate.getSeconds() + ".pdf";                
                                    pdf.save(pdfName);
                                }
                            });
                            
                            
                           /* var dims = {
                                a0: [1189, 841],
                                a1: [841, 594],
                                a2: [594, 420],
                                a3: [420, 297],
                                a4: [297, 210],
                                a5: [210, 148]
                            };
                            var loading = 0;
                            var loaded = 0;

                            var exportButton = document.getElementById("exportPDF");

                            exportButton.addEventListener(
                                "click",
                                function () {
                                    exportButton.disabled = true;
                                    document.body.style.cursor = "progress";

                                    var format = "a4";
                                    var resolution = '120';
                                    var dim = dims[format];
                                    var width = Math.round((dim[0] * resolution) / 31.4);
                                    var height = Math.round((dim[1] * resolution) / 31.4);
                                    var size =  (map.getSize());// @type {ol.Size} 
                                    var extent = map.getView().calculateExtent(size);

                                    var source = baseLayer.getSource();

                                    var tileLoadStart = function () {
                                        ++loading;
                                    };

                                    var tileLoadEnd = function () {
                                        ++loaded;
                                        if (loading === loaded) {
                                            var canvas = this;
                                            window.setTimeout(function () {
                                                loading = 0;
                                                loaded = 0;
                                                var data = canvas.toDataURL("image/png");
                                                var pdf = new jsPDF("landscape", undefined, format);
                                                pdf.addImage(data, "JPEG", 0, 0, dim[0], dim[1]);
                                                pdf.save("map.pdf");
                                                source.un("tileloadstart", tileLoadStart);
                                                source.un("tileloadend", tileLoadEnd, canvas);
                                                source.un("tileloaderror", tileLoadEnd, canvas);
                                                map.setSize(size);
                                                map.getView().fit(extent);
                                                map.renderSync();
                                                exportButton.disabled = false;
                                                document.body.style.cursor = "auto";
                                            }, 100);
                                        }
                                    };

                                    map.once("postcompose", function (event) {
                                        source.on("tileloadstart", tileLoadStart);
                                        source.on("tileloadend", tileLoadEnd, event.context.canvas);
                                        source.on("tileloaderror", tileLoadEnd, event.context.canvas);
                                    });

                                    map.setSize([width, height]);
                                    map.getView().fit(extent);
                                    map.renderSync();
                                },
                                false
                            );*/


                            /*****Rotate Map******/
                           /* var rotateIndex = [-4.725660433524355, -3.1374249228564333, -1.6101493759315917, 0];
                            var j = 0;
                            $("#rotate").click(function () {
                                map.getView().setRotation(rotateIndex[j]);
                                j++;
                                if (j == 4) {
                                    j = 0;
                                }
                            });*/

                            /****Overview control******/
                            var overViewControl = new ol.control.OverviewMap();
                            var overviewstatus = 0;
                            $("#overview").click(function () {
                                if (overviewstatus == 0) {
                                    map.addControl(overViewControl)
                                    $("button[title='Overview map']").trigger('click');
                                    overviewstatus = 1;
                                } else {
                                    map.removeControl(overViewControl);
                                    overviewstatus = 0;
                                }

                            });

                            map.on('singleclick', function (evt) {
                                var feature = map.forEachFeatureAtPixel(evt.pixel, function (feature) {
                                    return feature;
                                });
                                if (feature) {
                                    var properties = feature.getProperties().features[0].N;
                                    content.innerHTML = "<div><div class='heading'><strong>Rust Detail</strong></div><div style='padding:10px;'><table><tr><th>Severity</th><td>" + properties.severity + "</td></tr><tr><th>Disease</th><td>" + properties.disease + "</td></tr><tr><th>Groth Stage</th><td>" + properties.growth + "</td></tr><tr><th>Rust Percentage</th><td>" + properties.rush_per + "</td></tr></table></div></div>";
                                    overlay.setPosition(evt.coordinate);
                                } else {
                                    var viewResolution = /** @type {number} */ (map.getView().getResolution());
                                    var coordinate = ol.proj.toLonLat(evt.coordinate);
                                    var url = wmsSource.getGetFeatureInfoUrl(
                                        evt.coordinate,
                                        viewResolution,
                                        "EPSG:3857",
                                        "EPSG:4326",
                                        { INFO_FORMAT: "text/xml" }
                                    );


                                    if (url) {
                                        var x = new XMLHttpRequest();
                                        x.open("GET", url, true);
                                        x.send();
                                        x.onreadystatechange = function () {
                                            if (x.readyState == 4 && x.status == 200) {
                                                var num;
                                                var resultArr = x.responseText.split("\n");
                                                if (resultArr[3].split("=")[0].trim() == 'Deposition') {
                                                    popupTitle = 'Rust Spore Deposition';
                                                } else {
                                                    popupTitle = 'Rust Environment Suitability';
                                                }
                                                // popupTitle = 'Stripe rust spore ' + head;
                                                popupTemplate = '';
                                                if (resultArr[3].split("=")[0] == 'Deposition ') {
                                                    // console.log(Number(resultArr[3].split("=")[1]))
                                                    var vall = Number(resultArr[3].split("=")[1]);
                                                    var exponent = Math.round(vall).toString().length - 1
                                                    var val = (vall / Math.pow(10, exponent)).toFixed(3)
                                                    var num = val + ' x 10^' + exponent + ' per m^2'
                                                    popupTemplate += '<tr><td><strong>Number of spores</strong></td><td>' + num + '</td></tr><tr><td><strong>Category</strong></td><td>' + resultArr[4].split("=")[1] + '</td></tr>';

                                                } else {
                                                    num = parseInt(resultArr[3].split("=")[1]).toFixed(2) + '%';
                                                    popupTemplate += '<tr><td><strong>Infection efficency</strong></td><td>' + num + '</td></tr><tr><td>Category</td><td>' + resultArr[4].split("=")[1] + '</td></tr>';
                                                }


                                                var popupContent = "<div><div class='heading'>" + popupTitle + "</div><div style='padding:10px;'><table>" + popupTemplate + "</table></div></div>";
                                                content.innerHTML = popupContent;
                                                overlay.setPosition(evt.coordinate);
                                            } else {
                                                overlay.setPosition(undefined)
                                                closer.blur();
                                            }
                                        }
                                    }
                                }

                            });

                            //Enable or disable spatial tools icon and filter option upon timeline view
                            function setIconMode(status) {
                                if (status == 0) {
                                    $('.rightsec a').each(function () {
                                        $(this).addClass('disabled');
                                    });
                                    $('#rustType').attr('disabled', true);
                                    $('input[name="layerType"]').each(function () {
                                        $(this).attr('disabled', true);
                                    });

                                    $('.depositionChkBox').each(function () {
                                        $(this).attr('disabled', true);
                                    });

                                    $('.environmentChkBox').each(function () {
                                        $(this).attr('disabled', true);
                                    });
                                } else {
                                    $('.rightsec a').each(function () {
                                        $(this).removeClass('disabled');
                                    });

                                    $('#rustType').attr('disabled', false);
                                    $('input[name="layerType"]').each(function () {
                                        $(this).attr('disabled', false);
                                    });

                                    $('.depositionChkBox').each(function () {
                                        $(this).attr('disabled', false);
                                    });

                                    $('.environmentChkBox').each(function () {
                                        $(this).attr('disabled', false);
                                    });
                                }
                            }

                            /****Printin map section start ***/
                            var element = $('#map'); // global variable
                            var element2 = $('.mapPngContainer');
                            var getCanvas; // global variable
                            var newData;
                            $("#printmodal").on("shown.bs.modal", function () {
                                html2canvas(element, {
                                    onrendered: function (canvas) {
                                        $(".mapPng").html(canvas);
                                        var imageData = canvas.toDataURL("image/png");
                                        // Now browser starts downloading it instead of just showing it
                                        newData = imageData.replace(/^data:image\/png/, "data:application/octet-stream");
                                        prepareImage();
                                    }
                                });

                                if ($("#Deposition").is(":checked")) {
                                    $(".legendList1").html("None/Trace (0 - 10)");
                                    $(".legendList2").html("Low (10 - 1e+6)");
                                    $(".legendList3").html("Moderate (1e+6 - 1e+8)");
                                    $(".legendList4").html("High (1e+8 - 1e+11)");
                                    $(".legendList5").html("Very High (1e+11 -)");
                                } else {
                                    $(".legendList1").html("None/Trace (0%)");
                                    $(".legendList2").html("Low (0% - 10.5%)");
                                    $(".legendList3").html("Moderate (10.5% - 21%)");
                                    $(".legendList4").html("High (21% - 31.5%)");
                                    $(".legendList5").html("Very High (31.5% - 42.1%)");
                                }
                            });

                            function prepareImage() {
                                html2canvas(element2, {
                                    onrendered: function (canvas) {
                                        // $(".mapPng").html(canvas);
                                        var imageData2 = canvas.toDataURL("image/png");
                                        // Now browser starts downloading it instead of just showing it
                                        var newData2 = imageData2.replace(/^data:image\/png/, "data:application/octet-stream");
                                        var cdate = new Date();
                                        var imgName = "WRSIS_" + cdate.getDate() + "" + (cdate.getMonth() + 1) + "" + cdate.getFullYear() + "" + cdate.getHours() + "" + cdate.getMinutes() + "" + cdate.getSeconds() + ".png";
                                        $("#downloadMap").attr("download", imgName).attr("href", newData2);
                                    }
                                });
                            }

                            /****Printin map section end**/

                            /***Drawing Line Section Starts***/
                            var drawMark;
                            var drawMode = 0;

                            var drawingLayer = new ol.layer.Vector({
                                source: null,
                                style: new ol.style.Style({
                                    fill: new ol.style.Fill({
                                        color: '#0712b0',
                                    }),
                                    stroke: new ol.style.Stroke({
                                        color: '#0712b0',
                                        width: 4
                                    })
                                })
                            });
                            $("#drawMark").click(function () {
                                if (drawMode == 0) {
                                    drawMode = 1;
                                    var drawingSource = new ol.source.Vector({ wrapX: false });
                                    drawingLayer.setSource(null);
                                    drawingLayer.setSource(drawingSource);
                                    map.addLayer(drawingLayer)
                                    drawMark = new ol.interaction.Draw({
                                        source: drawingSource,
                                        type: 'LineString',
                                        freehand: true
                                    });
                                    map.addInteraction(drawMark);
                                } else {
                                    drawMode = 0;
                                    map.removeInteraction(drawMark);
                                    map.removeLayer(drawingLayer);
                                }
                            });
                            /***Drawing Line Section Ends***/

                            $("#baseMap, #draw, #drawMark, #overview").click(function () {
                                $(this).toggleClass("activeIcon");
                            });

                            </script>
                        </div>
                    </form>
                    <!--===================================================-->
                </div>
            </div>
        </div>
    </div>

</div>

<script>
    $(document).ready(function () {
        $('#action').on('change', function () {
            if (this.value == '2')
            //.....................^.......
            {
                $("#remark").show();
            }
            else {
                $("#remark").hide();
            }
        });


        // Map Expand collapse

        /*close button sidebar */
        $('#close_btn').click(function () {
            $('.mapfilter').animate({
                width: "0"
            }, function () {
                $(this).css("display", "none")
            });
            $('.mapcontainer').animate({
                marginRight: "0"
            });
            $(this).hide('fast');
            $('#open_btn').show('slow');
            setTimeout(function () {
                map.updateSize();
            }, 1000);
        });
        /*close button sidebar */
        /*open button sidebar */
        $('#open_btn').click(function () {
            $('.mapfilter').animate({
                width: "220px"
            }).css("display", "block");
            $('.mapcontainer').animate({
                marginRight: "220px"
            });
            $(this).hide('fast');
            $('#close_btn').show('slow');
            setTimeout(function () {
                map.updateSize();
            }, 1000);
        });
        /*open button sidebar */


    });



</script>


<script language="JavaScript">
    var card = document.getElementById("action")[0].value;
    function validateForm(f) {
        if (action.value == 0) {
            alert("Please select Action");
            return false;
        }
        else
            return true;
    }
    function checkData(field) {
        if (action.value == 2 && field.value === "") {
            alert("Please enter the remark.");
        }
    }


</script>