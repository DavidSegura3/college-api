package com.mitocode.college.services.impl;

import com.mitocode.college.models.documents.Enrollment;
import com.mitocode.college.repositories.IEnrollmentRepository;
import com.mitocode.college.repositories.IGenericRepository;
import com.mitocode.college.services.IEnrollmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EnrollmentServiceImpl extends CrudGenericImpl<Enrollment, String> implements IEnrollmentService {

    private final IEnrollmentRepository enrollmentRepository;
    @Override
    protected IGenericRepository<Enrollment, String> getRepository() {
        return enrollmentRepository;
    }
}
