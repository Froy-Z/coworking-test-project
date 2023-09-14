package ru.coworking.test.project.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne
    @JoinColumn(name = "coworking_id")
    Coworking coworking;

    @Column(name = "work_spaces")
    int workSpaces;

    @Column(name = "is_available")
    @Builder.Default
    boolean available = true;

}
