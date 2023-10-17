package com.csmpl.adminconsole.webportal.service;

import java.util.List;

import com.csmpl.adminconsole.webportal.bean.DesignationBean;
import com.csmpl.adminconsole.webportal.entity.Designation;

public interface ManageDesignationMasterService {

	String addDesignation(DesignationBean des);

	List<Designation> viewALlDesignation();

	Designation getDesignationById(Integer id);

	List<Designation> viewAllActiveDesignations();

}
