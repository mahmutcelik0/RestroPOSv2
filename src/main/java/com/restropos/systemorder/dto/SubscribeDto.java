package com.restropos.systemorder.dto;

import com.restropos.systemshop.constants.UserTypes;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SubscribeDto {
    private UserTypes userType;
    private String userInfo;
}
