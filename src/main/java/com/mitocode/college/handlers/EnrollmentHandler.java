package com.mitocode.college.handlers;

import com.mitocode.college.models.documents.Enrollment;
import com.mitocode.college.models.dtos.EnrollmentDTO;
import com.mitocode.college.services.IEnrollmentService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.time.LocalDateTime;

import static org.springframework.web.reactive.function.BodyInserters.fromValue;

@Component
@RequiredArgsConstructor
public class EnrollmentHandler {

    private final IEnrollmentService enrollmentService;
    @Qualifier("defaultMapper")
    private final ModelMapper mapper;

    public Mono<ServerResponse> create(ServerRequest serverRequest) {

        Mono<EnrollmentDTO> enrollmentDTOMono = serverRequest.bodyToMono(EnrollmentDTO.class);
        return enrollmentDTOMono
                .flatMap(e -> {
                    e.setEnrollmentDate(LocalDateTime.now());
                    return enrollmentService.save(convertToEntity(e));
                })
                .flatMap(e -> ServerResponse
                        .created(URI.create(serverRequest.uri().toString().concat("/").concat(e.getId())))
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(fromValue(e)));
    }

    private EnrollmentDTO convertToDto(Enrollment enrollment) {
        return mapper.map(enrollment, EnrollmentDTO.class);
    }

    private Enrollment convertToEntity(EnrollmentDTO enrollmentDTO) {
        return mapper.map(enrollmentDTO, Enrollment.class);
    }
}
