package com.example.SportNotifyApp.model;

import java.time.LocalDate;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "matches")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Match {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate  matchDate; // ví dụ: "Saturday, June 29"
    private String league;
    private String teamA;
    private String teamB;
    private String time;
    @Builder.Default
    private boolean notified = false;
}