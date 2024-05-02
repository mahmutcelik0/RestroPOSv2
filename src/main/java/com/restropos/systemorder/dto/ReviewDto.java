package com.restropos.systemorder.dto;

import com.restropos.systemshop.dto.CustomerDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReviewDto {
    private CustomerDto customerDto;
    private OrderDto orderDto;
}
