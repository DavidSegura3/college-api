package com.mitocode.college.models.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CourseDTO {

    private String id;

    @NotNull
    @NotEmpty
    @NotBlank
    private String nameCourse;

    @NotNull
    @NotEmpty
    @NotBlank
    private String acronymCourse;

    @NotNull
    private Boolean statusCourse;
}
