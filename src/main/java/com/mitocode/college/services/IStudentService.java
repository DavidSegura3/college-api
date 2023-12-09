package com.mitocode.college.services;

import com.mitocode.college.models.documents.Student;
import reactor.core.publisher.Flux;

public interface IStudentService extends IGenericCrud<Student, String>{

    Flux<Student> getStudentByAgeOrderByAsc();
    Flux<Student> getStudentByAgeOrderByDesc();
}
