package com.mitocode.college.handlers;

import com.mitocode.college.models.documents.Student;
import com.mitocode.college.models.dtos.StudentDTO;
import com.mitocode.college.services.IStudentService;
import com.mitocode.college.utils.validator.RequestValidator;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.validation.Validator;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.net.URI;

import static org.springframework.web.reactive.function.BodyInserters.fromValue;

@Component
@RequiredArgsConstructor
public class StudentHandler {

    private final IStudentService studentService;

    @Qualifier("defaultMapper")
    private final ModelMapper mapper;

    private final Validator validator;
    private final RequestValidator requestValidator;

    public Mono<ServerResponse> findAll(ServerRequest serverRequest) {
        return ServerResponse
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(studentService.findAll().map(this::convertToDto), StudentDTO.class);
    }

    public Mono<ServerResponse> getStudentByAgeAsc(ServerRequest serverRequest) {
        return ServerResponse
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(studentService.getStudentByAgeOrderByAsc().map(this::convertToDto), StudentDTO.class);
    }

    public Mono<ServerResponse> getStudentByAgeDesc(ServerRequest serverRequest) {
        return ServerResponse
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(studentService.getStudentByAgeOrderByDesc().map(this::convertToDto), StudentDTO.class);
    }

    public Mono<ServerResponse> findById(ServerRequest serverRequest) {
        String id = serverRequest.pathVariable("id");

        return studentService.findById(id)
                .map(this::convertToDto)
                .flatMap(e -> ServerResponse
                        .ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(fromValue(e))
                )
                .switchIfEmpty(ServerResponse.notFound().build());
    }

    public Mono<ServerResponse> create(ServerRequest serverRequest) {
        Mono<StudentDTO> studentDTOMono = serverRequest.bodyToMono(StudentDTO.class);

        return studentDTOMono
                .flatMap(requestValidator::validate)
                .flatMap(e -> studentService.save(this.convertToEntity(e)))
                .map(this::convertToDto)
                .flatMap(e -> ServerResponse
                        .created(URI.create(serverRequest.uri().toString().concat("/").concat(e.getId())))
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(fromValue(e))
                );
    }

    public Mono<ServerResponse> update(ServerRequest serverRequest) {
        String id = serverRequest.pathVariable("id");

        return serverRequest.bodyToMono(StudentDTO.class)
                .map(e -> {
                    e.setId(id);
                    return e;
                })
                .flatMap(requestValidator::validate)
                .flatMap(e -> studentService.update(convertToEntity(e), id))
                .map(this::convertToDto)
                .flatMap(e -> ServerResponse
                        .ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(fromValue(e)))
                .switchIfEmpty(ServerResponse.notFound().build());
    }

    public Mono<ServerResponse> delete(ServerRequest serverRequest) {
        String id = serverRequest.pathVariable("id");

        return studentService.deleteById(id)
                .flatMap(result -> {
                    if (result) {
                        return ServerResponse.noContent().build();
                    } else {
                        return ServerResponse.notFound().build();
                    }
                });
    }


    private StudentDTO convertToDto(Student student) {
        return mapper.map(student, StudentDTO.class);
    }

    private Student convertToEntity(StudentDTO studentDTO) {
        return mapper.map(studentDTO, Student.class);
    }
}
