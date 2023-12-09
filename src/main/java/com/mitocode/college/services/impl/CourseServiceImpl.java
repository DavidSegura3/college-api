package com.mitocode.college.services.impl;

import com.mitocode.college.models.documents.Course;
import com.mitocode.college.repositories.ICourseRepository;
import com.mitocode.college.repositories.IGenericRepository;
import com.mitocode.college.services.ICourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CourseServiceImpl extends CrudGenericImpl<Course, String> implements ICourseService {

    private final ICourseRepository courseRepository;
    @Override
    protected IGenericRepository<Course, String> getRepository() {
        return courseRepository;
    }
}
