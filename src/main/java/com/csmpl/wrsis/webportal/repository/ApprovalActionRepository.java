package com.csmpl.wrsis.webportal.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.csmpl.wrsis.webportal.entity.ApprovalActionEntity;

public interface ApprovalActionRepository extends JpaRepository<ApprovalActionEntity, Integer> {

}
