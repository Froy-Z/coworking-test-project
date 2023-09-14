package ru.coworking.test.project.controllers;


import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.coworking.test.project.dto.RoomDto;
import ru.coworking.test.project.exceptions.IllegalArgumentException;
import ru.coworking.test.project.exceptions.NotFoundException;
import ru.coworking.test.project.factories.RoomDtoFactory;
import ru.coworking.test.project.model.Coworking;
import ru.coworking.test.project.model.Room;
import ru.coworking.test.project.repository.CoworkingRepo;
import ru.coworking.test.project.repository.RoomRepo;

/**
 * реализация REST-контроллера для сущности Комнаты, создан CRUD
 */

@Transactional
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor // добавление всех полей в конструктор
@RestController
public class RoomController {

    RoomDtoFactory roomDtoFactory;

    CoworkingRepo coworkingRepo;
    RoomRepo roomRepo;

    public static final String GET_ROOM_ALL_IN_COWORKING = "/api/coworkings/{coworking_id}/rooms";
    public static final String GET_ROOM_IN_COWORKING = "/api/coworkings/{coworking_id}/rooms/{room_id}";
    public static final String CREATE_ROOM_IN_COWORKING = "/api/coworkings/{coworking_id}/rooms";
    public static final String EDIT_ROOM_IN_COWORKING = "/api/coworkings/{coworking_id}/rooms/{room_id}";
    public static final String DELETE_ROOM_ALL_IN_COWORKING = "/api/coworkings/{coworking_id}/rooms";
    public static final String DELETE_ROOM_IN_COWORKING = "/api/coworkings/{coworking_id}/rooms/{room_id}";

    // http://localhost:9999/api/coworkings/6/rooms?workSpaces=8
    @PostMapping(CREATE_ROOM_IN_COWORKING) // добавление комнат для коворкинга
    public RoomDto createRoomsInCoworking(@PathVariable Long coworking_id,
                                          @RequestParam int workSpaces) {

        Coworking coworking = coworkingRepo.getReferenceById(coworking_id);

        if (coworking.getId() != null) {
            throw new NotFoundException(
                    String.format(
                            "Coworking with <id = %d> is not found",
                            coworking_id));
        }
        if (workSpaces < 1 || workSpaces > 20) {
            throw new IllegalArgumentException(
                    "Workplaces cannot be absent or exceed 20 seats.");
        }

        Room room = roomRepo.saveAndFlush(
                Room.builder()
                        .workSpaces(workSpaces)
                        .coworking(coworking)
                        .build()
        );

        return roomDtoFactory.makeRoomDto(room);

    }
}
