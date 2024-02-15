package com.restropos.systememail.entity;

import com.restropos.systememail.constants.EmailTemplates;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "RAW_EMAIL_TEMPLATE")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RawEmailTemplate {
    @Id
    private EmailTemplates templateName;

    @Column(name = "template_content",length = 5000)
    private String templateContent;
}
