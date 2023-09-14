package ru.coworking.test.project.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ReservationDto {

    @NonNull
    Long id;

    @NonNull
    LocalDateTime startBooking;

    @NonNull
    LocalDateTime endBooking;
}
