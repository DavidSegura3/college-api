package com.mitocode.college.controllers;


import com.mitocode.college.models.documents.Student;
import com.mitocode.college.models.dtos.StudentDTO;
import com.mitocode.college.services.IStudentService;
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
@RequestMapping("/student")
@RequiredArgsConstructor
public class StudentController {

    private final IStudentService studentService;

    @Qualifier("defaultMapper")
    private final ModelMapper mapper;


    @GetMapping
    public Mono<ResponseEntity<Flux<StudentDTO>>> findAll() {
        Flux<StudentDTO> fx = studentService.findAll().map(this::convertToDto);

        return Mono.just(ResponseEntity.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(fx)
                )
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<StudentDTO>> findById(@PathVariable String id) {
        return studentService.findById(id)
                .map(this::convertToDto)
                .map(e -> ResponseEntity.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(e)
                )
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Mono<ResponseEntity<StudentDTO>> save(@Valid @RequestBody StudentDTO dto, final ServerHttpRequest req) {
        return studentService.save(convertToEntity(dto))
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
    public Mono<ResponseEntity<StudentDTO>> update(@Valid @RequestBody StudentDTO dto, @PathVariable String id) {
        return Mono.just(dto)
                .map(e -> {
                    e.setId(id);
                    return e;
                })
                .flatMap(e -> studentService.update(convertToEntity(dto), id))
                .map(this::convertToDto)
                .map(e -> ResponseEntity
                        .ok()
                        .body(e)
                )
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Object>> delete(@PathVariable String id) {
        return studentService.deleteById(id)
                .flatMap(result -> {
                    if (result) {
                        return Mono.just(ResponseEntity.noContent().build());
                    } else {
                        return Mono.just(ResponseEntity.notFound().build());
                    }
                })
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    private StudentDTO convertToDto(Student student) {
        return mapper.map(student, StudentDTO.class);
    }

    private Student convertToEntity(StudentDTO studentDTO) {
        return mapper.map(studentDTO, Student.class);
    }
}
