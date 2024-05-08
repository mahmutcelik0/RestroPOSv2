package com.restropos.systemorder.dto;

import com.restropos.systemshop.dto.CustomerDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReviewResponse {
    private Date commentTime;
    private String orderReviewComment;
    private Integer orderReviewStar;
    private CustomerDto customerDto;
}
