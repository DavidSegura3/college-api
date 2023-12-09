package com.mitocode.college.controllers;


import com.mitocode.college.models.documents.Course;
import com.mitocode.college.models.dtos.CourseDTO;
import com.mitocode.college.services.ICourseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URI;

@RestController
@RequestMapping("/course")
@RequiredArgsConstructor
public class CourseController {

    private final ICourseService courseService;

    @Qualifier("defaultMapper")
    private final ModelMapper mapper;


    @GetMapping
    public Mono<ResponseEntity<Flux<CourseDTO>>> findAll() {
        Flux<CourseDTO> fx = courseService.findAll().map(this::convertToDto);

        return Mono.just(ResponseEntity.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(fx)
                )
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<CourseDTO>> findById(@PathVariable String id) {
        return courseService.findById(id)
                .map(this::convertToDto)
                .map(e -> ResponseEntity.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(e)
                )
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Mono<ResponseEntity<CourseDTO>> save(@Valid @RequestBody CourseDTO dto, final ServerHttpRequest req) {
        return courseService.save(convertToEntity(dto))
                .map(this::convertToDto)
                .map(e -> ResponseEntity.created(
                                        URI.create(req.getURI().toString().concat("/").concat(e.getId()))
                                )
                                .contentType(MediaType.APPLICATION_JSON)
                                .body(e)
                )
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public Mono<ResponseEntity<CourseDTO>> update(@Valid @RequestBody CourseDTO dto, @PathVariable String id) {
        return Mono.just(dto)
                .map(e -> {
                    e.setId(id);
                    return e;
                })
                .flatMap(e -> courseService.update(convertToEntity(dto), id))
                .map(this::convertToDto)
                .map(e -> ResponseEntity
                        .ok()
                        .body(e)
                )
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Object>> delete(@PathVariable String id) {
        return courseService.deleteById(id)
                .flatMap(result -> {
                    if (result) {
                        return Mono.just(ResponseEntity.noContent().build());
                    } else {
                        return Mono.just(ResponseEntity.notFound().build());
                    }
                })
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    private CourseDTO convertToDto(Course student) {
        return mapper.map(student, CourseDTO.class);
    }

    private Course convertToEntity(CourseDTO courseDTO) {
        return mapper.map(courseDTO, Course.class);
    }
}
