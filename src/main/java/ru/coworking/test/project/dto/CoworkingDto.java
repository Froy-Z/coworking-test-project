package ru.coworking.test.project.dto;


import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CoworkingDto {

    @NonNull
    Long id;

    @NonNull
    String name;

    boolean available;
}
