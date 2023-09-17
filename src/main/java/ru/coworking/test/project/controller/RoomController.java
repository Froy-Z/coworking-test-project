package ru.coworking.test.project.controller;


import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import ru.coworking.test.project.dto.AskDto;
import ru.coworking.test.project.dto.CoworkingDto;
import ru.coworking.test.project.dto.RoomDto;
import ru.coworking.test.project.exception.IllegalArgumentException;
import ru.coworking.test.project.exception.NotFoundException;
import ru.coworking.test.project.factory.CoworkingDtoFactory;
import ru.coworking.test.project.factory.RoomDtoFactory;
import ru.coworking.test.project.model.Coworking;
import ru.coworking.test.project.model.Room;
import ru.coworking.test.project.repository.CoworkingRepo;
import ru.coworking.test.project.repository.RoomRepo;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * реализация REST-контроллера для сущности Комнаты, создан CRUD
 */

@Transactional
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@RestController
public class RoomController {

    RoomDtoFactory roomDtoFactory;
    CoworkingDtoFactory coworkingDtoFactory;

    CoworkingRepo coworkingRepo;
    RoomRepo roomRepo;

    public static final String GET_ROOM_IN_COWORKING = "/api/coworkings/{coworking_id}/rooms/{room_id}";
    public static final String GET_ROOM_ALL_IN_COWORKING = "/api/coworkings/{coworking_id}/rooms";
    public static final String GET_ROOM_ALL_IN_COWORKING_ALL = "/api/coworkings/rooms";
    public static final String CREATE_ROOM_IN_COWORKING = "/api/coworkings/{coworking_id}/rooms";
    public static final String EDIT_ROOM_IN_COWORKING = "/api/coworkings/{coworking_id}/rooms/{room_id}";
    public static final String DELETE_ROOM_IN_COWORKING = "/api/coworkings/{coworking_id}/rooms/{room_id}";
    public static final String DELETE_ROOM_ALL_IN_COWORKING = "/api/coworkings/{coworking_id}/rooms";
    public static final String DELETE_ROOM_ALL_IN_COWORKING_ALL = "/api/coworkings/rooms";

    // http://localhost:9999/api/coworkings/6/rooms?workSpaces=8
    @PostMapping(CREATE_ROOM_IN_COWORKING) // добавление комнат для коворкинга
    public RoomDto createRoomsInCoworking(@PathVariable Long coworking_id,
                                          @RequestParam int workSpaces) {

        Coworking coworking = checkCoworkingIdExistAndSave(coworking_id);

        checkWorkSpacesLimit(workSpaces);

        Room room = roomRepo.saveAndFlush(
                Room.builder()
                        .workSpaces(workSpaces)
                        .coworking(coworking)
                        .build()
        );

        return roomDtoFactory.makeRoomDto(room);

    }

    // http://localhost:9999/api/coworkings/8/rooms
    // http://localhost:9999/api/coworkings/8/rooms/4
    @GetMapping(path = {GET_ROOM_IN_COWORKING, GET_ROOM_ALL_IN_COWORKING}) // получение списка комнат(ы) коворкинга)
    public ResponseEntity<?> readRoomInCoworking(@PathVariable(required = false) Long coworking_id,
                                                 @PathVariable(required = false) Long room_id) {

        if (coworking_id != null & room_id != null) {
            checkCoworkingIdExist(coworking_id);
            Room room = roomRepo.findById(room_id)
                    .orElseThrow(() ->
                            new NotFoundException(
                                    String.format(
                                            "Room is <id = %d> not exists inside Coworking <id = %d>.",
                                            room_id, coworking_id
                                    )
                            )
                    );

            return ResponseEntity.ok(roomDtoFactory.makeRoomDto(room));

        } else if (coworking_id != null) {

            checkCoworkingIdExist(coworking_id);

            List<Room> rooms = roomRepo.findByCoworkingId(coworking_id);

            List<RoomDto> roomDtos = rooms.stream()
                    .map(roomDtoFactory::makeRoomDto)
                    .collect(Collectors.toList());

            return ResponseEntity.ok(roomDtos);

        } else {
            List<Coworking> coworkings = coworkingRepo.findAll();

            List<CoworkingDto> coworkingDtos = coworkings.stream()
                    .map(coworkingDtoFactory::makeCoworkingDto)
                    .collect(Collectors.toList());

            return ResponseEntity.ok(coworkingDtos);
        }
    }

    // http://localhost:9999/api/coworkings/rooms
    @GetMapping(GET_ROOM_ALL_IN_COWORKING_ALL) // получение всех коворкингов и их комнат
    public List<RoomDto> readAllRoomInCoworkings() {
        List<Coworking> coworkings = coworkingRepo.findAll();
        List<RoomDto> allRoomDtos = new ArrayList<>();

        for (Coworking coworking : coworkings) {
            List<Room> rooms = coworking.getRooms();
            List<RoomDto> roomDtos = rooms.stream()
                    .map(roomDtoFactory::makeRoomDto)
                    .toList();
            allRoomDtos.addAll(roomDtos); // Добавляем все RoomDto из roomDtos
        }
        return allRoomDtos;
    }

    // http://localhost:9999/api/coworkings/8/rooms/5?available=true
    @PatchMapping(EDIT_ROOM_IN_COWORKING) // изменение кол-ва рабочих мест
    public RoomDto updateRoom(@PathVariable Long coworking_id,
                              @PathVariable Long room_id,
                              @RequestParam(required = false) Integer workSpaces) {

        Coworking coworking = checkCoworkingIdExistAndSave(coworking_id);

        Optional<Room> optionalRoom = coworking
                .getRooms()
                .stream()
                .filter(room -> Objects.equals(room.getId(), room_id))
                .findFirst();

        Room room = optionalRoom.orElseThrow(() ->
                new NotFoundException(
                        String.format(
                                "Room with <id = %d> doesn't exist in Coworking <id = %d>",
                                room_id, coworking_id)
                )
        );

        if (workSpaces != null){

            checkWorkSpacesLimit(workSpaces);

            room.setWorkSpaces(workSpaces);
        }

        roomRepo.saveAndFlush(room);

        return roomDtoFactory.makeRoomDto(room);
    }

    // http://localhost:9999/api/coworkings/1/rooms/1
    // http://localhost:9999/api/coworkings/1/rooms
    // http://localhost:9999/api/coworkings/rooms
    // удаление одной комнаты, всех в коворкинге, или всех в коворкингах
    @DeleteMapping(path = {DELETE_ROOM_IN_COWORKING, DELETE_ROOM_ALL_IN_COWORKING, DELETE_ROOM_ALL_IN_COWORKING_ALL})
    public AskDto deleteRoomInCoworking(@PathVariable(required = false) Long coworking_id,
                                        @PathVariable(required = false) Long room_id) {

        if(coworking_id != null && room_id != null) {
            checkCoworkingIdExist(coworking_id);

            roomRepo.findByIdAndCoworkingId(room_id, coworking_id).orElseThrow(() ->
                    new NotFoundException(String.format(
                            "Room <id = %d> is not exist.",
                            room_id)
                    )
            );
            roomRepo.deleteById(room_id);

        } else if(coworking_id != null) {
            checkCoworkingIdExist(coworking_id);

            List<Room> rooms = roomRepo
                    .findByCoworkingId(coworking_id)
                    .stream()
                    .toList();

            roomRepo.deleteAll(rooms);
        } else {
            roomRepo.deleteAll();
        }

        return  AskDto.makeDefault("Room(-s) deleted successfully.");
    }

    private void checkCoworkingIdExist(Long coworking_id) {
        coworkingRepo.findById(coworking_id)
                .orElseThrow(() ->
                        new NotFoundException(
                                String.format(
                                        "Coworking with <id = %d> doesn't not exists",
                                        coworking_id)
                        )
                );
    }

    private void checkWorkSpacesLimit(int workSpaces) {
        if (workSpaces < 1 || workSpaces > 20) {
            throw new IllegalArgumentException(
                    "Workplaces cannot be absent or exceed 20 seats."
            );
        }
    }

    private Coworking checkCoworkingIdExistAndSave(Long coworking_id) {
        return coworkingRepo.findById(coworking_id)
                .orElseThrow(() ->
                        new NotFoundException(
                                String.format(
                                        "Coworking with <id = %d> doesn't not exists",
                                        coworking_id)
                        )
                );
    }
}