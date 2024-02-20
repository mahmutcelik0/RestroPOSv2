package com.restropos.systemrefactor.repository;

import com.restropos.systemrefactor.constants.EmailTemplates;
import com.restropos.systemrefactor.entity.RawEmailTemplate;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmailTemplateRepository extends JpaRepository<RawEmailTemplate, EmailTemplates> {

}
