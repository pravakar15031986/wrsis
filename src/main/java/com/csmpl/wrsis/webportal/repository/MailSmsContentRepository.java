package com.csmpl.wrsis.webportal.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.csmpl.wrsis.webportal.entity.MailSmsContentEntity;

public interface MailSmsContentRepository extends JpaRepository<MailSmsContentEntity, Integer> {

	MailSmsContentEntity findByMscontentid(Integer mscontentid);

}
