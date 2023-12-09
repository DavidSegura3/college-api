package com.mitocode.college.services.impl;

import com.mitocode.college.models.documents.Student;
import com.mitocode.college.repositories.IGenericRepository;
import com.mitocode.college.repositories.IStudentRepository;
import com.mitocode.college.services.IStudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
@RequiredArgsConstructor
public class StudentServiceImpl extends CrudGenericImpl<Student, String> implements IStudentService {

    private final IStudentRepository studentRepository;
    @Override
    protected IGenericRepository<Student, String> getRepository() {
        return studentRepository;
    }

    @Override
    public Flux<Student> getStudentByAgeOrderByAsc() {
        return studentRepository.findByOrderByAgeAsc();
    }

    @Override
    public Flux<Student> getStudentByAgeOrderByDesc() {
        return studentRepository.findByOrderByAgeDesc();
    }
}
