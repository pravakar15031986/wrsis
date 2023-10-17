package com.csmpl.wrsis.webportal.service;

import java.util.List;

import org.springframework.data.domain.Pageable;

import com.csmpl.wrsis.webportal.entity.TypeOfRust;

public interface TypeOfRustService {

	String saveAndUpdateTypeOfRust(TypeOfRust rust);

	TypeOfRust getTypeOfRustById(Integer rustId);

	List<TypeOfRust> vewAllTypeOFRust();

	String TypeOfRustByPage(Integer pageSize, Integer pageNumber, Pageable pageable);

	String searchTypeOfRustByPage(Integer pageSize, Integer pageNumber, Pageable pageable, String fName, String sName,
			String status);
}
