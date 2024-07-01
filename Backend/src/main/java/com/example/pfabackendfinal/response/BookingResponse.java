package com.example.pfabackendfinal.response;

import com.example.pfabackendfinal.entity.Doctor;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookingResponse {
    private Long id;
    private LocalDate date;
    private LocalTime heur;
    private String patientFullName;
    private String patientEmail;
    private String bookingConfirmationCode;
    private DoctorResponse doctor;
    private UserResponse user;

    public BookingResponse(Long id, LocalDate date, LocalTime heur,
                           String bookingConfirmationCode) {
        this.id = id;
        this.date = date;
        this.heur = heur;
        this.bookingConfirmationCode = bookingConfirmationCode;
    }


    public BookingResponse(Long id, LocalDate date, LocalTime heur, String patientFullName, String patientEmail ) {
        this.id=id;
        this.date = date;
        this.heur=heur;
        this.patientFullName=patientFullName;
        this.patientEmail=patientEmail;

    }


    public BookingResponse(Long id, LocalDate date, LocalTime heur, String patientFullName, String patientEmail, String bookingConfirmationCode, DoctorResponse doctor) {
        this.id=id;
        this.date = date;
        this.heur=heur;
        this.patientFullName=patientFullName;
        this.patientEmail=patientEmail;
        this.bookingConfirmationCode = bookingConfirmationCode;
        this.doctor = doctor;

    }
    public BookingResponse(Long id, LocalDate date, LocalTime heur, String patientFullName, String patientEmail,  UserResponse user) {
        this.id=id;
        this.date = date;
        this.heur=heur;
        this.patientFullName=patientFullName;
        this.patientEmail=patientEmail;
        this.user = user;

    }
}
