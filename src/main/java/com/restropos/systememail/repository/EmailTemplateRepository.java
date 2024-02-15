package com.restropos.systememail.repository;

import com.restropos.systememail.constants.EmailTemplates;
import com.restropos.systememail.entity.RawEmailTemplate;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EmailTemplateRepository extends JpaRepository<RawEmailTemplate, EmailTemplates> {

}
