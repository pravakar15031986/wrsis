package com.csmpl.wrsis.webportal.repository;

import java.util.List;



import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.csmpl.wrsis.webportal.entity.QuestionOption;

@Repository
public interface QuestionOptionRepository extends JpaRepository<QuestionOption, Integer> {

	@Query(value = "select * from wrsis.m_wr_question_option where int_question_id=:qustionId and bitstatus='false' ", nativeQuery = true)
	List<QuestionOption> findByQustionId(@Param("qustionId") Integer qustionId);

	@Query(nativeQuery = true, value = "select opt.vch_option_value,opt.vch_option_info  from wrsis.m_wr_question_option opt where opt.int_question_id=:qustionId and opt.bitstatus='false' ")
	List<Object[]> findOptionByQustionId(@Param("qustionId") Integer qustionId);

	@Query(nativeQuery = true, value = "update wrsis.m_wr_question_option set bitstatus='true' where int_question_id=:qustionId and bitstatus='false' ")
	void deleteExistingOptionsByQuestionId(int qustionId);

}
