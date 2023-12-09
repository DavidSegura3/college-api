package com.mitocode.college.repositories;

import com.mitocode.college.models.documents.Student;
import reactor.core.publisher.Flux;

public interface IStudentRepository extends IGenericRepository<Student, String>{

    Flux<Student> findByOrderByAgeAsc();
    Flux<Student> findByOrderByAgeDesc();
}
