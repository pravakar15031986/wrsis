package com.csmpl.wrsis.webportal.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.csmpl.wrsis.webportal.entity.Question;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Integer> {

	@Query("select MAX(qustionId) from Question")
	Integer findMaxQustionId();

	@Query("FROM  Question d ORDER By d.qustionId ASC")
	Page<Question> viewAllQustionPage(Pageable pageable);

	Page<Question> findBystatus(boolean status, Pageable pageable);

	Page<Question> findByqustionType(int qustionType, Pageable pageable);

	List<Question> findByQustionOrder(Integer qustionOrder);

	@Query("from Question q where q.qustionOrder=:qustionOrder AND q.qustionType=:qustionType")
	Question validateQustionOrderAndqustionType(Integer qustionOrder, Integer qustionType);

	@Query(nativeQuery = true, value = " select que.int_question_id,que.vch_question,que.int_ques_order from wrsis.m_wr_question que "
			+ " where que.int_ques_type = 2 and que.bitstatus = 'false' order by que.int_ques_order ")
	List<Object[]> fetchAllIncidentQuestion();

	@Query("from Question q where q.qustionOrder=:qustionOrder AND q.qustionType=:qustionType AND status='false'")
	Question findByQustionOrderByQuestionType(Integer qustionOrder, Integer qustionType);

	Page<Question> findByQustionTypeAndStatus(int qustionType, boolean status, Pageable pageable);

	@Query(value = "SELECT int_question_id, int_ques_type, vch_question, int_ques_order, int_option_count, bitstatus, intcreatedby, dtmcreatedon, intupdatedby, dtmupdatedon,int_ivr_survey_id\r\n"
			+ " FROM wrsis.m_wr_question where bitstatus=false  and int_ques_type=1 order by int_ques_order", nativeQuery = true)
	List<Question> viewAllQustionExcel();

	@Query(value = "SELECT count(*) FROM wrsis.m_wr_question where bitstatus=false  and int_ques_type=1 ", nativeQuery = true)
	Integer countWebQustion();

	@Query(nativeQuery = true, value = " select que.int_question_id,que.vch_question,que.int_ques_order from wrsis.m_wr_question que "
			+ " where que.int_ques_type = 1 and que.bitstatus = 'false' order by que.int_ques_order ")
	List<Object[]> fetchAllIVRQuestion();

	Question findByIvrSurveyIdAndQustionOrder(int surveyId, int queOrder);

	Question findByQustionId(int qustionId);

	@Query(value = "SELECT int_question_id, int_ques_type, vch_question, int_ques_order, int_option_count, bitstatus, intcreatedby, dtmcreatedon, intupdatedby, dtmupdatedon,int_ivr_survey_id\r\n"
			+ " FROM wrsis.m_wr_question where bitstatus=false  and int_ques_type=3 order by int_ques_order", nativeQuery = true)
	List<Question> viewAllAPIQuestion();

}
