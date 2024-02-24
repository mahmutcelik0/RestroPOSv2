package com.restropos.systemcore.dto;

import jakarta.validation.Valid;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public abstract class GenericDto<T> {
    @Valid public T dto;
}
