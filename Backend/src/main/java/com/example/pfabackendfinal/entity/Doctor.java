package com.example.pfabackendfinal.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.RandomStringUtils;
import org.hibernate.annotations.Type;

import java.math.BigDecimal;
import java.sql.Blob;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor

public class Doctor {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private  Long id;
        private String specialite;
        private String doctorName;
        private String email;
        private String experience;

        private boolean isBooked = false;

        @Lob
        private Blob photo;// blob stock des donnes binaire

        @OneToMany(mappedBy="doctor", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
        private List<BookedApp> bookings;

        public Doctor() {
            this.bookings = new ArrayList<>();
        }
       public void addBooking(BookedApp booking){
            if (bookings == null){
                bookings = new ArrayList<>();
            }
            bookings.add(booking);
            booking.setDoctor(this);
            isBooked = true;
            String bookingCode = RandomStringUtils.randomNumeric(10);
            booking.setBookingConfirmationCode(bookingCode);
        }
    }
