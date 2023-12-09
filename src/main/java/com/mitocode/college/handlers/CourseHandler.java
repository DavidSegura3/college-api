package com.mitocode.college.handlers;

import com.mitocode.college.models.documents.Course;
import com.mitocode.college.models.dtos.CourseDTO;
import com.mitocode.college.models.dtos.StudentDTO;
import com.mitocode.college.services.ICourseService;
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
public class CourseHandler {

    private final ICourseService courseService;

    @Qualifier("defaultMapper")
    private final ModelMapper mapper;

    private final Validator validator;
    private final RequestValidator requestValidator;

    public Mono<ServerResponse> findAll(ServerRequest serverRequest) {
        return ServerResponse
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(courseService.findAll().map(this::convertToDto), StudentDTO.class);
    }

    public Mono<ServerResponse> findById(ServerRequest serverRequest) {
        String id = serverRequest.pathVariable("id");

        return courseService.findById(id)
                .map(this::convertToDto)
                .flatMap(e -> ServerResponse
                        .ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(fromValue(e))
                )
                .switchIfEmpty(ServerResponse.notFound().build());
    }

    public Mono<ServerResponse> create(ServerRequest serverRequest) {
        Mono<CourseDTO> courseDTOMono = serverRequest.bodyToMono(CourseDTO.class);

        return courseDTOMono
                .flatMap(requestValidator::validate)
                .flatMap(e -> courseService.save(this.convertToEntity(e)))
                .map(this::convertToDto)
                .flatMap(e -> ServerResponse
                        .created(URI.create(serverRequest.uri().toString().concat("/").concat(e.getId())))
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(fromValue(e))
                );
    }

    public Mono<ServerResponse> update(ServerRequest serverRequest) {
        String id = serverRequest.pathVariable("id");

        return serverRequest.bodyToMono(CourseDTO.class)
                .map(e -> {
                    e.setId(id);
                    return e;
                })
                .flatMap(requestValidator::validate)
                .flatMap(e -> courseService.update(convertToEntity(e), id))
                .map(this::convertToDto)
                .flatMap(e -> ServerResponse
                        .ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(fromValue(e)))
                .switchIfEmpty(ServerResponse.notFound().build());
    }

    public Mono<ServerResponse> delete(ServerRequest serverRequest) {
        String id = serverRequest.pathVariable("id");

        return courseService.deleteById(id)
                .flatMap(result -> {
                    if (result) {
                        return ServerResponse.noContent().build();
                    } else {
                        return ServerResponse.notFound().build();
                    }
                });
    }


    private CourseDTO convertToDto(Course course) {
        return mapper.map(course, CourseDTO.class);
    }

    private Course convertToEntity(CourseDTO courseDTO) {
        return mapper.map(courseDTO, Course.class);
    }
}
