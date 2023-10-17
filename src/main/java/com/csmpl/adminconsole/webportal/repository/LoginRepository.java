/**
 * 
 */
package com.csmpl.adminconsole.webportal.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.csmpl.adminconsole.webportal.entity.User;

@Repository("loginRepository")
public interface LoginRepository extends JpaRepository<User, Integer> {

	@Query("select usr from User usr where usr.userName= :userName")
	User findByUserName(@Param("userName") String userName);

	@Query("select distinct globalName,GlobalId FROM UserPermission where userId=?1")
	List<Object[]> findByUserId(int userId);

	@Query("select primaryName, fileName FROM UserPermission where globalName = :globalval and userId = :userid")
	List<Object[]> findByGlobalNameAndUserId(@Param("globalval") String valueOf, @Param("userid") int userId);

	User findByUserNameAndBitStatus(String userName, boolean b);

	
	
	
	

	

	@Query(nativeQuery = true, value = " select  urole.int_user_id,STRING_AGG (role.alias_name,',') as alias_name from wrsis.m_adm_role role inner join wrsis.m_adm_user_role urole on role.role_id=urole.int_role_id "
			+ " where urole.int_user_id=:userId group by urole.int_user_id")
	List<Object[]> findRoleByUserId(@Param("userId") int userId);


}
