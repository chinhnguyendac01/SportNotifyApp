package com.example.SportNotifyApp.repository;

import com.example.SportNotifyApp.model.Match;

import java.time.LocalDate;

import org.springframework.data.jpa.repository.JpaRepository;

public interface MatchRepository extends JpaRepository<Match, Long> {
    boolean existsByMatchDateAndTeamAAndTeamB(LocalDate matchDate, String teamA, String teamB);
}