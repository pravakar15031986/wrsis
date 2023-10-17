package com.csmpl.wrsis.webportal.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.csmpl.wrsis.webportal.entity.MobileTokenEntity;

public interface MobileTokenRepository extends JpaRepository<MobileTokenEntity, Integer> {



	/**
	 * @param userId
	 * @param token
	 * @param b
	 * @return
	 */
	Optional<MobileTokenEntity> findByUserIdAndTokenAndBitStatus(int userId, String token, boolean b);

	/**
	 * @param userId
	 * @param b
	 * @return
	 */
	Optional<MobileTokenEntity> findByUserIdAndBitStatus(int userId, boolean b);

}
