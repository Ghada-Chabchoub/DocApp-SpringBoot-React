package com.example.pfabackendfinal.repository;

import com.example.pfabackendfinal.entity.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface DoctorRepository extends JpaRepository<Doctor, Long> {
    @Query("SELECT DISTINCT d.specialite FROM Doctor d")
    List<String> findDistinctSpecialites();
    @Query(" SELECT d FROM Doctor d" +
            " WHERE d.specialite LIKE %:specialite% " +
            " AND d.id NOT IN (" +
            "  SELECT ba.doctor.id FROM BookedApp ba " +
            "  WHERE ((ba.date = :date) AND (ba.heur = :heur))" +
            ")")

    List<Doctor> findAvailableDoctorsByDatesAndSpeciality(LocalDate date, LocalTime heur, String specialite);

    @Query("SELECT d FROM Doctor d WHERE d.email = :email")
    Doctor findDoctorByEmail(@Param("email") String email);

    @Query("SELECT d.email FROM Doctor d ")
    Doctor findAllDoctorByEmail(String email);


}
