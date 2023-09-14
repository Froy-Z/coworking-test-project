package ru.coworking.test.project.exceptions;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ErrorDTO {

    String error; // что за ошибка

    @JsonProperty("error_description")
    String errorDescription; // описание ошибки
}
