package ru.coworking.test.project.controller;


import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import ru.coworking.test.project.dto.AskDto;
import ru.coworking.test.project.dto.CoworkingDto;
import ru.coworking.test.project.exception.BadRequestException;
import ru.coworking.test.project.exception.NotFoundException;
import ru.coworking.test.project.factory.CoworkingDtoFactory;
import ru.coworking.test.project.model.Coworking;
import ru.coworking.test.project.repository.CoworkingRepo;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * реализация REST-контроллера для сущности Коворкинга, создан CRUD
 */

@Transactional
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor // добавление всех полей в конструктор
@RestController
public class CoworkingController {

    CoworkingDtoFactory coworkingDtoFactory;

    CoworkingRepo coworkingRepo;

    public static final String GET_COWORKING_ALL = "/api/coworkings";
    public static final String GET_COWORKING = "/api/coworkings/{coworking_id}";
    public static final String CREATE_COWORKING = "/api/coworkings";
    public static final String EDIT_COWORKING = "/api/coworkings/{coworking_id}";
    public static final String DELETE_COWORKING_ALL = "/api/coworkings";
    public static final String DELETE_COWORKING = "/api/coworkings/{coworking_id}";

    // http://localhost:9999/api/coworkings?name=Уимблдон
    @PostMapping(CREATE_COWORKING) // создание coworking
    public CoworkingDto createCoworking(@RequestParam String name) {

        if (name.trim().isEmpty()) throw new BadRequestException("Name can't be empty");

        Coworking coworking = coworkingRepo.saveAndFlush(
                Coworking.builder()
                        .name(name)
                        .build()
        );
        return coworkingDtoFactory.makeCoworkingDto(coworking);

    }

    // http://localhost:9999/api/coworkings
    // http://localhost:9999/api/coworkings/1
    @GetMapping(path = {GET_COWORKING_ALL, GET_COWORKING}) // получение информации о коворкинге(ах)
    public ResponseEntity<?> readCoworking(@PathVariable(required = false) Long coworking_id) {
        if (coworking_id != null) {

            Coworking coworking = coworkingRepo.findById(coworking_id)
                    .orElseThrow(() ->
                            new NotFoundException(
                                    String.format(
                                            "Coworking with <id = %d> doesn't not found",
                                            coworking_id)));

            return ResponseEntity.ok(coworkingDtoFactory.makeCoworkingDto(coworking));

        } else {

            List<Coworking> coworkings = coworkingRepo.findAll();

            List<CoworkingDto> coworkingDtos = coworkings.stream()
                    .map(coworkingDtoFactory::makeCoworkingDto)
                    .collect(Collectors.toList());

            return ResponseEntity.ok(coworkingDtos);
        }
    }


    // http://localhost:9999/api/coworkings/4?name=Необычный%20экипаж
    @PatchMapping(EDIT_COWORKING) // смена доступности и имени coworking
    public CoworkingDto updateCoworking(@PathVariable Long coworking_id,
                                         @RequestParam(required = false) String name) {

        Coworking coworking = coworkingRepo
                .findById(coworking_id)
                .orElseThrow(() ->
                        new NotFoundException(
                                String.format(
                                        "Coworking with <id = %d> doesn't exist.",
                                        coworking_id
                                )
                        )
                );

        if (name != null) {
            coworkingRepo
                    .findByName(name)
                    .filter(anotherCoworking ->
                            !Objects.equals(anotherCoworking.getId(),
                                    coworking.getId()))
                    .ifPresent(anotherCoworking -> {
                        throw new BadRequestException(
                                String.format(
                                        "A coworking space with <%s> already exists.",
                                        name
                                )
                        );
                    });
            coworking.setName(name);
        }


        return coworkingDtoFactory.makeCoworkingDto(coworking);
    }

    @DeleteMapping(path = {DELETE_COWORKING, DELETE_COWORKING_ALL})
    public AskDto deleteCoworking(@PathVariable(required = false) Long coworking_id) {

        if (coworking_id != null) {
            if (coworkingRepo.existsById(coworking_id)) {
                coworkingRepo.deleteById(coworking_id);
            } else {
                throw new NotFoundException(
                        String.format("Coworking with <id = %d> doesn't exist.",
                                coworking_id
                        )
                );
            }
        } else {
            coworkingRepo.deleteAll();
        }

        return AskDto.makeDefault("Coworking(-s) deleted successfully.");
    }

    // TODO: рефакторинг кода на наличие дублей
}

