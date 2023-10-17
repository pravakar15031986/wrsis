package com.csmpl.adminconsole.webportal.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.csmpl.adminconsole.webportal.entity.UserType;

@Repository("userTypeRepository")
public interface UserTypeRepository extends JpaRepository<UserType, Integer> {

}
