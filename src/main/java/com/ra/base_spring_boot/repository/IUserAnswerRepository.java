package com.ra.base_spring_boot.repository;

import com.ra.base_spring_boot.model.UserAnswer;
import com.ra.base_spring_boot.model.constants.SessionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface IUserAnswerRepository extends JpaRepository<UserAnswer, Long> {
    @Query("SELECT ua FROM UserAnswer ua " +
            "JOIN ua.question q " +
            "JOIN q.examSession es " +
            "WHERE ua.userId = :userId " +
            "AND q.id IN :questionIds " +
            "AND es.id = :sessionId " +
            "AND es.sessionType = :sessionType")
    List<UserAnswer> findByUserAndQuestionIdsAndSessionType(
            @Param("userId") Long userId,
            @Param("sessionId") Long sessionId,
            @Param("questionIds") List<Long> questionIds,
            @Param("sessionType") SessionType sessionType
    );
}

