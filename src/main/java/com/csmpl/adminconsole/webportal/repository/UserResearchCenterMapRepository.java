package com.csmpl.adminconsole.webportal.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.csmpl.adminconsole.webportal.entity.UserResearchCenterMapping;

@Repository
public interface UserResearchCenterMapRepository extends JpaRepository<UserResearchCenterMapping, Integer> {

	List<UserResearchCenterMapping> findByUserIdAndBitStatus(Integer userId, boolean deletefalg);

	UserResearchCenterMapping findByUserId(Integer userId);

}
