package com.restropos.systemorder.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SubscribeKey {
    private String businessDomain;
    private SubscribeDto subscribeDto;
    private OrderDto order;
}
