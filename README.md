# Spring Boot Validation(English)

@GenuineLukas 

**Spring Boot Validation**

- **Project Description**: Implemented advanced validation techniques in a Spring Boot application to ensure robust data integrity and error handling. Designed APIs and DTOs to systematically validate incoming data across various endpoints.
- **Technologies Used**:
    - **Spring Boot**: Leveraged for the core application framework to create scalable Java applications.
    - **Jakarta Bean Validation (formerly Java Bean Validation)**: Utilized constraints such as **`@NotBlank`**, **`@Size`**, **`@Email`**, **`@Pattern`**, and more to enforce field validity within DTOs.
    - **Lombok**: Employed to reduce boilerplate code in Java objects, utilizing annotations such as **`@Data`**, **`@NoArgsConstructor`**, **`@AllArgsConstructor`**, and **`@Builder`**.
    - **Jackson**: Configured with **`PropertyNamingStrategies.SnakeCaseStrategy`** for JSON serialization and deserialization to match API specifications.
- **Key Responsibilities**:
    - **DTO Design**: Crafted Data Transfer Objects (DTOs) with integrated validation annotations to ensure data received through APIs met business requirements.
    - **API Development**: Developed RESTful endpoints using Spring Boot, managing requests and responses, and ensuring that all data adheres to defined validation rules.
    - **Error Handling**: Implemented a global exception handling mechanism using **`@RestControllerAdvice`** to catch and respond to validation errors, providing detailed error messages to clients.
    - **Validation Logic**: Orchestrated complex validation logic across the server side, enhancing security and data integrity, and ensuring all client inputs were validated before processing.
- **Outcome**: Achieved a highly maintainable and scalable application structure, improving data quality and reducing the risk of data-related errors in business processes.

# **Spring Boot Web-Validation**

Project Setup (add validation to dependencies)

(An image was shown here)

As the number of variables requesting validation increases, the length of the code performing validation checks also becomes excessively long.

This can interfere with service logic, and when scattered, it becomes difficult to determine where validation occurred. If the validation logic changes, it can shake up the entire logic, including test code.

| Annotation | Capability | Additional Notes |
| --- | --- | --- |
| @Size | Measure string length | Not applicable to Int type |
| @NotNull | Cannot be null |  |
| @NotEmpty | Cannot be null or empty string |  |
| @NotBlank | Cannot be null, empty string, or whitespace only |  |
| @Pattern | Applies regular expression |  |
| @Max | Maximum value |  |
| @Min | Minimum value |  |
| @AssertTrue / False | Applies separate logic |  |
| @Valid | Executes validation of the specified object |  |
| @Past | Past date |  |
| @PastOrPresent | Today or a past date |  |
| @Future | Future date |  |
| @FutureOrPresent | Today or a future date |  |

# **Practical Validation Application-01: Validating DTO Variables with @Email, @Size, etc.**

Apply appropriate annotations to the member variables of the DTO like this:

```java
javaCopy code
package com.example.validation.model;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserRegisterRequest {
    @NotBlank
    private String name;

    @NotBlank
    @Size(min = 1, max = 12)
    private String password;

    @NotNull
    @Min(1)
    @Max(100)
    private Integer age;

    @Email
    private String email;

    @Pattern(regexp = "^[2-9]\\d{2}-\\d{3}-\\d{4}$")
    private String phoneNumber;

    @FutureOrPresent
    private LocalDateTime registerAt;
}

```

UserApiController

```java
javaCopy code
package com.example.validation.controller;

import com.example.validation.model.UserRegisterRequest;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/user")
public class UserApiController {
    @PostMapping("")
    public UserRegisterRequest register(
            @Valid
            @RequestBody
            UserRegisterRequest userRegisterRequest
    ){
        log.info("init: {}", userRegisterRequest);

        return userRegisterRequest;
    }
}

```

This code exemplifies how to implement annotations for validating the fields of a Data Transfer Object (DTO) in a Spring Boot application. The annotations enforce various constraints like string length, numerical limits, and formatting rules to ensure that the data adheres to expected standards before processing.

# **Practical Validation Application-02: Delivering validation to the Client**

Structure of the API that wraps the DTO

```java
javaCopy code
package com.example.validation.model;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class Api<T> {
    private String resultCode;

    private String resultMessage;

    @Valid // This must be attached to also perform validation checks on the generic type.
    private T data;

    private Error error;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class Error {
        private List<String> errorMessage;
    }
}

```

Basic structure of the DTO

```java
javaCopy code
package com.example.validation.model;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserRegisterRequest {
    @NotBlank
    private String name;

    @NotBlank
    @Size(min = 1, max = 12)
    private String password;

    @NotNull
    @Min(1)
    @Max(100)
    private Integer age;

    @Email
    private String email;

    @Pattern(regexp = "^[2-9]\\d{2}-\\d{3}-\\d{4}$")
    private String phoneNumber;

    @FutureOrPresent
    private LocalDateTime registerAt;
}

```

Appearance of incoming PostRequest

```java
javaCopy code
package com.example.validation.controller;

import com.example.validation.model.Api;
import com.example.validation.model.UserRegisterRequest;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/user")
public class UserApiController {
    @PostMapping("")

    public Api<UserRegisterRequest> register(
            @Valid
            @RequestBody
            Api<UserRegisterRequest> userRegisterRequest

            // If you delegate validation logic to an Exception Handler,
            // naturally any requests reaching this class imply that the values contained are valid,
            // and now only business logic needs to be processed
            // and then return the value.
    ){
        log.info("init: {}", userRegisterRequest);

        var body = userRegisterRequest.getData();
        Api<UserRegisterRequest> response = Api.<UserRegisterRequest>builder()
                .resultCode(String.valueOf(HttpStatus.OK.value()))
                .resultMessage(HttpStatus.OK.getReasonPhrase())
                .data(body)
                .build();

        return response;
    }
}

```

Exception handling + notifying the client something went wrong

```java
javaCopy code
package com.example.validation.exception;

import com.example.validation.model.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class ValidationExceptionHandler{
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Api> validateException(
            MethodArgumentNotValidException e
    ){
        log.error(e.getMessage());

        var errorMessageList = e.getFieldErrors().stream()
                .map(it -> {
                    var format = "%s : { %s } %s";
                    var message = String.format(format, it.getField(), it.getRejectedValue() , it.getDefaultMessage());
                    return message;
                }).collect(Collectors.toList());

        var error = Api.Error
                .builder()
                .errorMessage(errorMessageList)
                .build();

        var errorResponse = Api
                .builder()
                .resultCode(String.valueOf(HttpStatus.BAD_REQUEST.value()))
                .resultMessage(HttpStatus.BAD_REQUEST.getReasonPhrase())
                .erorr(error)
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }
}

```

These code snippets demonstrate the implementation of API validation responses and DTO structures in a Spring Boot application, ensuring that incoming data is validated and properly handled even when errors occur.