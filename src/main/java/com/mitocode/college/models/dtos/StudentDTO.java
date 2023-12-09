package com.mitocode.college.models.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class StudentDTO {


    private String id;

    @NotNull
    @NotEmpty
    @NotBlank
    private String nameStudent;

    @NotNull
    @NotEmpty
    @NotBlank
    private String lastnameStudent;

    @NotNull
    @NotEmpty
    @NotBlank
    private String dniStudent;

    @Positive
    private Integer ageStudent;
}
