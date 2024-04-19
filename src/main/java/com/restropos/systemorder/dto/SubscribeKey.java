package com.restropos.systemorder.dto;

import com.restropos.systemshop.constants.UserTypes;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SubscribeKey {
    private String businessDomain;
    private SubscribeDto subscribeDto;
    private String order;
}
