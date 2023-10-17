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
                  <div class="row">
                     <div class="col-12 col-sm-6 col-md-6 col-lg-3">
                        <div class="portlets__card ">
                           <div class="portlets__card__content cyan">
                              <p><i class="icon-user1"></i> New Clients</p>
                              <h4>566</h4>
                           </div>
                           <div class="portlets__card__charts cyan-dark">
                              <div class="charts__content" id="newclints">
                              </div>
                           </div>
                        </div>
                     </div>
                     <div class="col-12 col-sm-6 col-md-6 col-lg-3">
                        <div class="portlets__card">
                           <div class="portlets__card__content red">
                              <p><i class="icon-dollar-sign1"></i> Total Sales</p>
                              <h4>566</h4>
                           </div>
                           <div class="portlets__card__charts red-dark">
                              <div class="charts__content" id="totalsales">
                              </div>
                           </div>
                        </div>
                     </div>
                     <div class="col-12 col-sm-6 col-md-6 col-lg-3">
                        <div class="portlets__card">
                           <div class="portlets__card__content orange">
                              <p><i class="icon-bar-chart1"></i> Year Profit</p>
                              <h4>566</h4>
                           </div>
                           <div class="portlets__card__charts orange-dark">
                              <div class="charts__content" id="profit">
                              </div>
                           </div>
                        </div>
                     </div>
                     <div class="col-12 col-sm-6 col-md-6 col-lg-3 ">
                        <div class="portlets__card">
                           <div class="portlets__card__content green">
                              <p><i class="icon-edit-document"></i> New Invoice</p>
                              <h4>566</h4>
                           </div>
                           <div class="portlets__card__charts green-dark">
                              <div class="charts__content" id="newinvoices">
                              </div>
                           </div>
                        </div>
                     </div>
                  </div>
               </div>
               <div class="row">
                  <div class="col-12 col-md-6 col-lg-4">
                     <div class="dashboard-portlet">
                        <div class="dashboard-portlet__header">
                           <h5>Line Chart Demo  <a class="float-right more-links" href="javascript:void(0)" ><span class="icon-more-vertical1"></span></a>
                           </h5>
                           <div class="more-links-list">
                              <ul>
                                 <li> <a class="more-links refresh" href="javascript:void(0)"><span class="icon-rotate-cw1"></span></a></li>
                                 <li> <a class="more-links filter" href="javascript:void(0)"><span class="icon-filter1"></span></a></li>
                                 <li> <a class="more-links maximize" href="javascript:void(0)"><span class="icon-maximize1"></span></a></li>
                              </ul>
                           </div>
                        </div>
                        <div class="dashboard-portlet__content" >
                           <div class="dashboard-portlet__content__chart" id="line-chart">
                           </div>
                        </div>
                        <div class="filter-section">
                           <a href="javascript:void(0)" class="filter__close"><span class="icon-x1"></span></a>
                           <div class="row form-group">
                              <div class="col-12">
                                 <label>From Date</label>
                                 <input type="text" class="form-control" name="">
                              </div>
                           </div>
                           <div class="row form-group">
                              <div class="col-12">
                                 <label>To Date</label>
                                 <input type="text" class="form-control" name="">
                              </div>
                           </div>
                           <div class="row form-group">
                              <div class="col-12">
                                 <button type="submit" class="btn btn-primary btn-sm">Search</button>
                              </div>
                           </div>
                        </div>
                     </div>
                  </div>
                  <div class="col-12 col-md-6 col-lg-4">
                     <div class="dashboard-portlet">
                        <div class="dashboard-portlet__header">
                           <h5>Bar Chart Demo <a class="float-right more-links" href="javascript:void(0)"><span class="icon-more-vertical1"></span></a></h5>
                           <div class="more-links-list">
                              <ul>
                                 <li> <a class="more-links refresh" href="javascript:void(0)"><span class="icon-rotate-cw1"></span></a></li>
                                 <li> <a class="more-links filter" href="javascript:void(0)"><span class="icon-filter1"></span></a></li>
                                 <li> <a class="more-links maximize" href="javascript:void(0)"><span class="icon-maximize1"></span></a></li>
                              </ul>
                           </div>
                        </div>
                        <div class="dashboard-portlet__content" >
                           <div class="dashboard-portlet__content__chart" id="bar-chart">
                           </div>
                        </div>
                        <div class="filter-section">
                           <a href="javascript:void(0)" class="filter__close"><span class="icon-x1"></span></a>
                           <div class="row form-group">
                              <div class="col-12">
                                 <label>From Date</label>
                                 <input type="text" class="form-control" name="">
                              </div>
                           </div>
                           <div class="row form-group">
                              <div class="col-12">
                                 <label>To Date</label>
                                 <input type="text" class="form-control" name="">
                              </div>
                           </div>
                           <div class="row form-group">
                              <div class="col-12">
                                 <button type="submit" class="btn btn-primary btn-sm">Search</button>
                              </div>
                           </div>
                        </div>
                     </div>
                  </div>
                  <div class="col-12 col-md-6 col-lg-4">
                     <div class="dashboard-portlet">
                        <div class="dashboard-portlet__header">
                           <h5>Pie Chart Demo <a class="float-right more-links" href="javascript:void(0)"><span class="icon-more-vertical1"></span></a></h5>
                           <div class="more-links-list">
                              <ul>
                                 <li> <a class="more-links refresh" href="javascript:void(0)"><span class="icon-rotate-cw1"></span></a></li>
                                 <li> <a class="more-links filter" href="javascript:void(0)"><span class="icon-filter1"></span></a></li>
                                 <li> <a class="more-links maximize" href="javascript:void(0)"><span class="icon-maximize1"></span></a></li>
                              </ul>
                           </div>
                        </div>
                        <div class="dashboard-portlet__content" >
                           <div class="dashboard-portlet__content__chart" id="pie-chart">
                           </div>
                        </div>
                        <div class="filter-section">
                           <a href="javascript:void(0)" class="filter__close"><span class="icon-x1"></span></a>
                           <div class="row form-group">
                              <div class="col-12">
                                 <label>From Date</label>
                                 <input type="text" class="form-control" name="">
                              </div>
                           </div>
                           <div class="row form-group">
                              <div class="col-12">
                                 <label>To Date</label>
                                 <input type="text" class="form-control" name="">
                              </div>
                           </div>
                           <div class="row form-group">
                              <div class="col-12">
                                 <button type="submit" class="btn btn-primary btn-sm">Search</button>
                              </div>
                           </div>
                        </div>
                     </div>
                  </div>
                  <div class="col-12 col-md-6 col-lg-4">
                     <div class="dashboard-portlet">
                        <div class="dashboard-portlet__header">
                           <h5>Single Bar Chart Demo <a class="float-right more-links" href="javascript:void(0)" ><span class="icon-more-vertical1"></span></a></h5>
                           <div class="more-links-list">
                              <ul>
                                 <li> <a class="more-links refresh" href="javascript:void(0)"><span class="icon-rotate-cw1"></span></a></li>
                                 <li> <a class="more-links filter" href="javascript:void(0)"><span class="icon-filter1"></span></a></li>
                                 <li> <a class="more-links maximize" href="javascript:void(0)"><span class="icon-maximize1"></span></a></li>
                              </ul>
                           </div>
                        </div>
                        <div class="dashboard-portlet__content">
                           <div class="dashboard-portlet__content__chart" id="polar-chart">
                           </div>
                        </div>
                        <div class="filter-section">
                           <a href="javascript:void(0)" class="filter__close"><span class="icon-x1"></span></a>
                           <div class="row form-group">
                              <div class="col-12">
                                 <label>From Date</label>
                                 <input type="text" class="form-control" name="">
                              </div>
                           </div>
                           <div class="row form-group">
                              <div class="col-12">
                                 <label>To Date</label>
                                 <input type="text" class="form-control" name="">
                              </div>
                           </div>
                           <div class="row form-group">
                              <div class="col-12">
                                 <button type="submit" class="btn btn-primary btn-sm">Search</button>
                              </div>
                           </div>
                        </div>
                     </div>
                  </div>
                  <div class="col-12 col-md-6 col-lg-4">
                     <div class="dashboard-portlet filled cyan">
                        <div class="dashboard-portlet__header">
                           <h5>Bar Chart Demo <a class="float-right more-links" href="javascript:void(0)"><span class="icon-more-vertical1"></span></a></h5>
                           <div class="more-links-list">
                              <ul>
                                 <li> <a class="more-links refresh" href="javascript:void(0)"><span class="icon-rotate-cw1"></span></a></li>
                                 <li> <a class="more-links filter" href="javascript:void(0)"><span class="icon-filter1"></span></a></li>
                                 <li> <a class="more-links maximize" href="javascript:void(0)"><span class="icon-maximize1"></span></a></li>
                              </ul>
                           </div>
                        </div>
                        <div class="dashboard-portlet__content " >
                           <div class="dashboard-portlet__content__chart" id="fill-line-chart">
                           </div>
                        </div>
                        <div class="filter-section">
                           <a href="javascript:void(0)" class="filter__close"><span class="icon-x1"></span></a>
                           <div class="row form-group">
                              <div class="col-12">
                                 <label>From Date</label>
                                 <input type="text" class="form-control" name="">
                              </div>
                           </div>
                           <div class="row form-group">
                              <div class="col-12">
                                 <label>To Date</label>
                                 <input type="text" class="form-control" name="">
                              </div>
                           </div>
                           <div class="row form-group">
                              <div class="col-12">
                                 <button type="submit" class="btn btn-primary btn-sm">Search</button>
                              </div>
                           </div>
                        </div>
                     </div>
                  </div>
                  <div class="col-12 col-md-6 col-lg-4">
                     <div class="dashboard-portlet filled sea-blue">
                        <div class="dashboard-portlet__header">
                           <h5>Bar Chart Demo <a class="float-right more-links" href="javascript:void(0)"><span class="icon-more-vertical1"></span></a></h5>
                           <div class="more-links-list">
                              <ul>
                                 <li> <a class="more-links refresh" href="javascript:void(0)"><span class="icon-rotate-cw1"></span></a></li>
                                 <li> <a class="more-links filter" href="javascript:void(0)"><span class="icon-filter1"></span></a></li>
                                 <li> <a class="more-links maximize" href="javascript:void(0)"><span class="icon-maximize1"></span></a></li>
                              </ul>
                           </div>
                        </div>
                        <div class="dashboard-portlet__content">
                           <div class="dashboard-portlet__content__chart" id="fill-bar-chart">
                           </div>
                        </div>
                        <div class="filter-section">
                           <a href="javascript:void(0)" class="filter__close"><span class="icon-x1"></span></a>
                           <div class="row form-group">
                              <div class="col-12">
                                 <label>From Date</label>
                                 <input type="text" class="form-control" name="">
                              </div>
                           </div>
                           <div class="row form-group">
                              <div class="col-12">
                                 <label>To Date</label>
                                 <input type="text" class="form-control" name="">
                              </div>
                           </div>
                           <div class="row form-group">
                              <div class="col-12">
                                 <button type="submit" class="btn btn-primary btn-sm">Search</button>
                              </div>
                           </div>
                        </div>
                     </div>
                  </div>
                  <div class="col-12 col-md-6 col-lg-6">
                     <div class="dashboard-portlet">
                        <div class="dashboard-portlet__header">
                           <h5>Table details <a class="float-right more-links" href="javascript:void(0)"><span class="icon-more-vertical1"></span></a></h5>
                           <div class="more-links-list">
                              <ul>
                                 <li> <a href="javascript:void(0)" class="more-links refresh"><span class="icon-rotate-cw1"></span></a></li>
                                 <li> <a href="javascript:void(0)" class="more-links filter"><span class="icon-filter1"></span></a></li>
                                 <li> <a href="javascript:void(0)" class="more-links maximize"><span class="icon-maximize1"></span></a></li>
                              </ul>
                           </div>
                        </div>
                        <div class="dashboard-portlet__content">
                           <div class="dashboard-portlet__content__details" >
                              <table class="table">
                                 <thead>
                                    <tr>
                                       <th width="30px">Sl#</th>
                                       <th>Firstname</th>
                                       <th>Lastname</th>
                                       <th>Email</th>
                                    </tr>
                                 </thead>
                                 <tbody>
                                    <tr>
                                       <td>1</td>
                                       <td>John</td>
                                       <td>Doe</td>
                                       <td>john@example.com</td>
                                    </tr>
                                    <tr>
                                       <td>2</td>
                                       <td>Mary</td>
                                       <td>Moe</td>
                                       <td>mary@example.com</td>
                                    </tr>
                                    <tr>
                                       <td>3</td>
                                       <td>July</td>
                                       <td>Dooley</td>
                                       <td>july@example.com</td>
                                    </tr>
                                    <tr>
                                       <td>4</td>
                                       <td>July</td>
                                       <td>Dooley</td>
                                       <td>july@example.com</td>
                                    </tr>
                                    <tr>
                                       <td>5</td>
                                       <td>July</td>
                                       <td>Dooley</td>
                                       <td>july@example.com</td>
                                    </tr>
                                    <tr>
                                       <td>6</td>
                                       <td>July</td>
                                       <td>Dooley</td>
                                       <td>july@example.com</td>
                                    </tr>
                                    <tr>
                                       <td>7</td>
                                       <td>July</td>
                                       <td>Dooley</td>
                                       <td>july@example.com</td>
                                    </tr>
                                    <tr>
                                       <td>8</td>
                                       <td>July</td>
                                       <td>Dooley</td>
                                       <td>july@example.com</td>
                                    </tr>
                                    <tr>
                                       <td>9</td>
                                       <td>July</td>
                                       <td>Dooley</td>
                                       <td>july@example.com</td>
                                    </tr>
                                    <tr>
                                       <td>10</td>
                                       <td>July</td>
                                       <td>Dooley</td>
                                       <td>july@example.com</td>
                                    </tr>
                                 </tbody>
                              </table>
                           </div>
                        </div>
                        <div class="filter-section">
                           <a href="javascript:void(0)" class="filter__close"><span class="icon-x1"></span></a>
                           <div class="row form-group">
                              <div class="col-12">
                                 <label>From Date</label>
                                 <input type="text" class="form-control" name="">
                              </div>
                           </div>
                           <div class="row form-group">
                              <div class="col-12">
                                 <label>To Date</label>
                                 <input type="text" class="form-control" name="">
                              </div>
                           </div>
                           <div class="row form-group">
                              <div class="col-12">
                                 <button type="submit" class="btn btn-primary btn-sm">Search</button>
                              </div>
                           </div>
                        </div>
                     </div>
                  </div>
                  <div class="col-12 col-md-6 col-lg-6">
                     <div class="dashboard-portlet filled sea-blue">
                        <div class="dashboard-portlet__header">
                           <h5>Bar Chart Demo <a class="float-right more-links" href="javascript:void(0)" ><span class="icon-more-vertical1"></span></a></h5>
                           <div class="more-links-list">
                              <ul>
                                 <li> <a href="javascript:void(0)" class="more-links refresh"><span class="icon-rotate-cw1"></span></a></li>
                                 <li> <a href="javascript:void(0)" class="more-links filter"><span class="icon-filter1"></span></a></li>
                                 <li> <a href="javascript:void(0)" class="more-links maximize"><span class="icon-maximize1"></span></a></li>
                              </ul>
                           </div>
                        </div>
                        <div class="dashboard-portlet__content">
                           <div class="dashboard-portlet__content__details" >
                              <table class="table">
                                 <thead>
                                    <tr>
                                       <th width="30px">Sl#</th>
                                       <th>Firstname</th>
                                       <th>Lastname</th>
                                       <th>Email</th>
                                    </tr>
                                 </thead>
                                 <tbody>
                                    <tr>
                                       <td>1</td>
                                       <td>John</td>
                                       <td>Doe</td>
                                       <td>john@example.com</td>
                                    </tr>
                                    <tr>
                                       <td>2</td>
                                       <td>Mary</td>
                                       <td>Moe</td>
                                       <td>mary@example.com</td>
                                    </tr>
                                    <tr>
                                       <td>3</td>
                                       <td>July</td>
                                       <td>Dooley</td>
                                       <td>july@example.com</td>
                                    </tr>
                                    <tr>
                                       <td>4</td>
                                       <td>July</td>
                                       <td>Dooley</td>
                                       <td>july@example.com</td>
                                    </tr>
                                    <tr>
                                       <td>5</td>
                                       <td>July</td>
                                       <td>Dooley</td>
                                       <td>july@example.com</td>
                                    </tr>
                                    <tr>
                                       <td>6</td>
                                       <td>July</td>
                                       <td>Dooley</td>
                                       <td>july@example.com</td>
                                    </tr>
                                    <tr>
                                       <td>7</td>
                                       <td>July</td>
                                       <td>Dooley</td>
                                       <td>july@example.com</td>
                                    </tr>
                                    <tr>
                                       <td>8</td>
                                       <td>July</td>
                                       <td>Dooley</td>
                                       <td>july@example.com</td>
                                    </tr>
                                    <tr>
                                       <td>9</td>
                                       <td>July</td>
                                       <td>Dooley</td>
                                       <td>july@example.com</td>
                                    </tr>
                                    <tr>
                                       <td>10</td>
                                       <td>July</td>
                                       <td>Dooley</td>
                                       <td>july@example.com</td>
                                    </tr>
                                 </tbody>
                              </table>
                           </div>
                        </div>
                        <div class="filter-section">
                           <a href="javascript:void(0)" class="filter__close"><span class="icon-x1"></span></a>
                           <div class="row form-group">
                              <div class="col-12">
                                 <label>From Date</label>
                                 <input type="text" class="form-control" name="">
                              </div>
                           </div>
                           <div class="row form-group">
                              <div class="col-12">
                                 <label>To Date</label>
                                 <input type="text" class="form-control" name="">
                              </div>
                           </div>
                           <div class="row form-group">
                              <div class="col-12">
                                 <button type="submit" class="btn btn-primary btn-sm">Search</button>
                              </div>
                           </div>
                        </div>
                     </div>
                  </div>
               </div>
            </div>
         </div>
         
         