package com.restropos.systemmenu.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductSelectedModifierDto {
    private Long id;
    private String name;
    private List<SelectionDto> selections;
}
