package com.example.validation.model;

import com.example.validation.annotation.PhoneNumber;
import com.example.validation.annotation.YearMonth;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Objects;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserRegisterRequest {
    private String name;
    private String nickName;

    @NotBlank
    @Size(min = 1, max = 12)
    private String password;

    @NotNull
    @Min(1)
    @Max(100)
    private Integer age;

    @Email
    private String email;

    @PhoneNumber
    private String phoneNumber;

    @FutureOrPresent
    private LocalDateTime registerAt;

    @YearMonth(pattern = "yyyy-MM")
    private String birthDayYearMonth;
    //custom validation
    //AssertTrue 어노테이션은 is로 시작하는 메소드에 붙여줘야지만이 제대로 동작한다.
    @AssertTrue(message = "name or nickname 은 존재해야 합니다.")
    public boolean isNameCheck(){
        if(Objects.nonNull(name) && !name.isBlank()){
            return true;
        }
        if(Objects.nonNull(nickName) && !nickName.isBlank()) {
            return true;
        }
        return false;
    }
}
