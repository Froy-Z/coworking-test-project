package ru.coworking.test.project.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RoomDto {

    @NonNull
    Long id;

    int workSpaces;
}
