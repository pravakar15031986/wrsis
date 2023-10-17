package com.csmpl.wrsis.webportal.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.csmpl.wrsis.webportal.entity.IVRDataQuestionEntity;

@Repository
public interface IVRDataQuestionRepository extends JpaRepository<IVRDataQuestionEntity, Integer> {

	@Query(value = "select * from wrsis.t_wr_ivr_data_ques where int_ivr_data_id=:ivrDataId", nativeQuery = true)
	List<IVRDataQuestionEntity> findQustionByIvrDataId(Integer ivrDataId);

	List<IVRDataQuestionEntity> findQustionIdByQustionId(int qustionBeanId);
}
