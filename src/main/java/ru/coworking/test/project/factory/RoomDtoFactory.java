package ru.coworking.test.project.factory;

import org.springframework.stereotype.Component;
import ru.coworking.test.project.dto.RoomDto;
import ru.coworking.test.project.model.Room;

@Component
public class RoomDtoFactory {
    public RoomDto makeRoomDto(Room room) {
        return RoomDto.builder()
                .id(room.getId())
                .workSpaces(room.getWorkSpaces())
                .build();
    }
}
