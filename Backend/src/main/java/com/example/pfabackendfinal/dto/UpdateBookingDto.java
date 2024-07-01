package com.example.pfabackendfinal.dto;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class UpdateBookingDto
{
    private LocalDate date;
    private LocalTime heur;
    private String patientEmail;


}
