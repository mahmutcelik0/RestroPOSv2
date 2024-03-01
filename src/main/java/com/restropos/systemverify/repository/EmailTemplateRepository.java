package com.restropos.systemverify.repository;

import com.restropos.systemverify.constants.EmailTemplates;
import com.restropos.systemverify.entity.RawEmailTemplate;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmailTemplateRepository extends JpaRepository<RawEmailTemplate, EmailTemplates> {

}
