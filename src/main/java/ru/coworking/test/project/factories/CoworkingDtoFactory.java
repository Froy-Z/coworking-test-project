package ru.coworking.test.project.factories;

import org.springframework.stereotype.Component;
import ru.coworking.test.project.dto.CoworkingDto;
import ru.coworking.test.project.model.Coworking;

@Component
public class CoworkingDtoFactory {

    public CoworkingDto makeCoworkingDto(Coworking coworking) {
        return CoworkingDto.builder()
                .id(coworking.getId())
                .name(coworking.getName())
                .available(coworking.isAvailable())
                .build();
    }
}
