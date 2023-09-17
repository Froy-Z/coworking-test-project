package ru.coworking.test.project.factory;

import org.springframework.stereotype.Component;
import ru.coworking.test.project.dto.ReservationDto;
import ru.coworking.test.project.model.Reservation;

@Component
public class ReservationDtoFactory {
    public ReservationDto makeReservationDto(Reservation reservation) {
        return ReservationDto.builder()
                .id(reservation.getId())
                .startBooking(reservation.getStartBooking())
                .endBooking(reservation.getEndBooking())
                .build();
    }
}
