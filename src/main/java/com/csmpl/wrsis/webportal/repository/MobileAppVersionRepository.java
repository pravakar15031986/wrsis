package com.csmpl.wrsis.webportal.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.csmpl.wrsis.webportal.entity.MobileAppVersionEntity;

public interface MobileAppVersionRepository extends JpaRepository<MobileAppVersionEntity, Integer> {

	Optional<MobileAppVersionEntity> findByOsTypeIdAndBitstatus(int osTypeId,boolean bitstatus);
	Optional<MobileAppVersionEntity> findByOsTypeId(int osTypeId);

}
