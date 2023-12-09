package com.mitocode.college.config;

import com.mitocode.college.handlers.CourseHandler;
import com.mitocode.college.handlers.EnrollmentHandler;
import com.mitocode.college.handlers.StudentHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RequestPredicates.PUT;
import static org.springframework.web.reactive.function.server.RequestPredicates.DELETE;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;


@Configuration
public class RouterConfig {

    @Bean
    public RouterFunction<ServerResponse> routesStudent(StudentHandler studentHandler){
        return route(GET("/v2/student"), studentHandler::findAll)
                .andRoute(GET("/v2/student/{id}"), studentHandler::findById)
                .andRoute(POST("/v2/student"), studentHandler::create)
                .andRoute(PUT("/v2/student/{id}"), studentHandler::update)
                .andRoute(DELETE("/v2/student/{id}"), studentHandler::delete)
                .andRoute(GET("/v2/student-asc"), studentHandler::getStudentByAgeAsc)
                .andRoute(GET("/v2/student-desc"), studentHandler::getStudentByAgeDesc);
    }

    @Bean
    public RouterFunction<ServerResponse> routesCourse(CourseHandler courseHandler){
        return route(GET("/v2/course"), courseHandler::findAll)
                .andRoute(GET("/v2/course/{id}"), courseHandler::findById)
                .andRoute(POST("/v2/course"), courseHandler::create)
                .andRoute(PUT("/v2/course/{id}"), courseHandler::update)
                .andRoute(DELETE("/v2/course/{id}"), courseHandler::delete);
    }

    @Bean
    public RouterFunction<ServerResponse> routesEnrollment(EnrollmentHandler enrollmentHandler){
        return route(POST("/v2/enrollment"), enrollmentHandler::create);
    }
}
