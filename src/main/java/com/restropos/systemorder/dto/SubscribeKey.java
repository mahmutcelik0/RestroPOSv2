package com.restropos.systemorder.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SubscribeKey {
    private String businessDomain;
    private SubscribeDto subscribeDto;
    private List<OrderDto> order;
}
