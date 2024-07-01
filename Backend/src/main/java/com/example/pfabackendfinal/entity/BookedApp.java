package com.example.pfabackendfinal.entity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BookedApp {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long bookingId;

        @Column(name = "date")
        private LocalDate date;

        @Column(name = "heur")
        private LocalTime heur;


        @Column(name = "patient_fullName")
        private String patientFullName;

        @Column(name = "patient_email")
        private String patientEmail;


        @Column(name = "confirmation_Code")
        private String bookingConfirmationCode;

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "doctor_email")
        private Doctor doctor;



        public void setBookingConfirmationCode(String bookingConfirmationCode) {
            this.bookingConfirmationCode = bookingConfirmationCode;
        }
    }
