package com.mitocode.college.models.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.mitocode.college.models.documents.Course;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EnrollmentDTO {

    private String id;
    private LocalDateTime enrollmentDate;
    private List<Course> courses;
    @NotNull
    private Boolean status;
}
