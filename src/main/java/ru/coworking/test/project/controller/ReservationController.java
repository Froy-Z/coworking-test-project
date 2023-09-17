package ru.coworking.test.project.controller;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import ru.coworking.test.project.dto.ReservationDto;
import ru.coworking.test.project.exception.BadRequestException;
import ru.coworking.test.project.exception.IllegalArgumentException;
import ru.coworking.test.project.exception.NotFoundException;
import ru.coworking.test.project.factory.ReservationDtoFactory;
import ru.coworking.test.project.model.Reservation;
import ru.coworking.test.project.model.Room;
import ru.coworking.test.project.repository.CoworkingRepo;
import ru.coworking.test.project.repository.ReservationRepo;
import ru.coworking.test.project.repository.RoomRepo;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Transactional
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@RestController
public class ReservationController {

    RoomRepo roomRepo;
    CoworkingRepo coworkingRepo;
    ReservationRepo reservationRepo;

    ReservationDtoFactory reservationDtoFactory;


    public static final String GET_ROOMS_FOR_MIN_WORKSPACES = "/api/coworkings/rooms/spaces";
    public static final String GET_ROOM_IN_COWORKING_FOR_MIN_WORKSPACES = "/api/coworkings/{coworking_id}/rooms/spaces";
    public static final String GET_FREE_ROOMS_FOR_TIME = "/api/coworkings/rooms/time";
    public static final String GET_FREE_ROOM_IN_COWORKING_FOR_TIME = "/api/coworkings/{coworking_id}/rooms/time";
    public static final String GET_ROOMS_FOR_MIN_WORKSPACES_AND_TIME = "/api/coworkings/rooms/optional";
    public static final String GET_ROOM_IN_COWORKING_FOR_MIN_WORKSPACES_AND_TIME = "/api/coworkings/{coworking_id}/rooms/optional";
    public static final String CREATE_BOOKING_FOR_ROOM = "/api/coworkings/rooms/{room_id}/reservation";


    // http://localhost:9999/api/coworkings/rooms/spaces?minWorkSpaces=5
    // http://localhost:9999/api/coworkings/1/rooms/spaces?minWorkSpaces=5
    @GetMapping(path = {GET_ROOM_IN_COWORKING_FOR_MIN_WORKSPACES, GET_ROOMS_FOR_MIN_WORKSPACES})
    public List<Room> getRoomsMinWorkSpaces(
            @PathVariable(required = false) Long coworking_id,
            @RequestParam Integer minWorkSpaces) {

        checkWorkSpacesLimit(minWorkSpaces);

        if (coworking_id != null) {
            checkCoworkingIdExist(coworking_id);

            return roomRepo.findByCoworkingId(coworking_id)
                    .stream()
                    .filter(rooms -> rooms.getWorkSpaces() >= minWorkSpaces)
                    .toList();
        }

        return roomRepo.findAll()
                .stream()
                .filter(rooms -> rooms.getWorkSpaces() >= minWorkSpaces)
                .toList();

    }

    // http://localhost:9999/api/coworkings/rooms/time?x=2023-09-16T12:00&y=2023-09-20T22:00
    // http://localhost:9999/api/coworkings/4/rooms/time?x=2023-09-16T12:00&y=2023-09-21T11:00
    @GetMapping(path = {GET_FREE_ROOMS_FOR_TIME, GET_FREE_ROOM_IN_COWORKING_FOR_TIME})
    public List<Room> getFreeRoomsForTime(
            @PathVariable(required = false) Long coworking_id,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm") LocalDateTime x,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm") LocalDateTime y) {

        checkValidTime(x, y);


        if (coworking_id != null) {
            checkCoworkingIdExist(coworking_id);

            return roomRepo.findByCoworkingId(coworking_id)
                    .stream()
                    .filter(room -> roomAvailableForTime(room, x, y))
                    .toList();
        }

        return roomRepo.findAll()
                .stream()
                .filter(room -> roomAvailableForTime(room, x, y))
                .toList();
    }

    // http://localhost:9999/api/coworkings/rooms/optional?minWorkSpaces=12&x=2023-09-18T00:00&y=2023-09-20T23:30
    // http://localhost:9999/api/coworkings/1/rooms/optional?minWorkSpaces=10&x=2023-09-18T00:00&y=2023-09-20T23:30
    @GetMapping(path = {GET_ROOM_IN_COWORKING_FOR_MIN_WORKSPACES_AND_TIME, GET_ROOMS_FOR_MIN_WORKSPACES_AND_TIME})
    public List<Room> getFreeRoomsMinWorkSpacesAndForTime(
            @PathVariable(required = false) Long coworking_id,
            @RequestParam Integer minWorkSpaces,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm") LocalDateTime x,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm") LocalDateTime y) {

        checkValidTime(x, y);

        checkWorkSpacesLimit(minWorkSpaces);

        if (coworking_id != null) {
            checkCoworkingIdExist(coworking_id);

            return roomRepo.findByCoworkingId(coworking_id)
                    .stream()
                    .filter(room -> room.getWorkSpaces() >= minWorkSpaces && roomAvailableForTime(room, x, y))
                    .toList();

        }
        return roomRepo.findAll()
                .stream()
                .filter(room -> room.getWorkSpaces() >= minWorkSpaces && roomAvailableForTime(room, x, y))
                .toList();

    }

    // http://localhost:9999/api/coworkings/rooms/2/reservation?x=2023-09-16T16:00&y=2023-09-17T17:00
    @PostMapping(CREATE_BOOKING_FOR_ROOM)
    public ReservationDto bookingRoom(@PathVariable Long room_id,
                                      @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm") LocalDateTime x,
                                      @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm") LocalDateTime y) {

        checkValidTime(x, y);

        Room room = roomRepo.findById(room_id)
                .orElseThrow(() ->
                        new NotFoundException("Room with <id = %d> doesn't not exist."));

        /*
         * проверка пересечений для коррекного бронирования
         */
        Optional<Reservation> overlappingReservation = reservationRepo
                .findByRoomId(room_id)
                .stream()
                .filter(reservation -> timeOverlap(reservation.getStartBooking(), reservation.getEndBooking(), x, y))
                .findFirst();

        if (overlappingReservation.isPresent()) {
            throw new BadRequestException("This reservation overlaps with an existing one.");
        }

        Reservation reservation = reservationRepo.save(
                Reservation.builder()
                        .startBooking(x)
                        .endBooking(y)
                        .room(room)
                        .build()
        );

        return reservationDtoFactory.makeReservationDto(reservation);

    }


    private boolean roomAvailableForTime(Room room, LocalDateTime x, LocalDateTime y) {

        return reservationRepo.findByRoomId(room.getId())
                .stream()
                .noneMatch(reservation -> timeOverlap(reservation.getStartBooking(), reservation.getEndBooking(), x, y));
    }

    private boolean timeOverlap(LocalDateTime start, LocalDateTime end,
                                LocalDateTime requestX, LocalDateTime requestY) {

        return start.isBefore(requestY) && end.isAfter(requestX);

    }

    private void checkValidTime(LocalDateTime ldtFirst, LocalDateTime ldtSecond) {
        if (ldtFirst.isAfter(ldtSecond)) {
            throw new IllegalArgumentException("The current start of the booking cannot be after the end of the booking..");
        }

        if (ldtFirst.getMinute() % 30 != 0 || ldtSecond.getMinute() % 30 != 0) {
            throw new IllegalArgumentException("Reservations are only available in 30 minute increments.");
        }
    }

    private void checkCoworkingIdExist(Long coworking_id) {
        coworkingRepo.findById(coworking_id)
                .orElseThrow(() ->
                        new NotFoundException(
                                String.format(
                                        "Coworking with <id = %d> doesn't not exist",
                                        coworking_id)
                        )
                );
    }

    private void checkWorkSpacesLimit(Integer workSpaces) {
        if (workSpaces < 1 || workSpaces > 20) {
            throw new IllegalArgumentException(
                    "Workplaces cannot be absent or exceed 20 seats."
            );
        }
    }
}


//
