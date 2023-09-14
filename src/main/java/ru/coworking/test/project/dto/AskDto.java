package ru.coworking.test.project.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AskDto {

    String answer;

    public static AskDto makeDefault(String answer) {
        return builder()
                .answer(answer)
                .build();
    }
}
