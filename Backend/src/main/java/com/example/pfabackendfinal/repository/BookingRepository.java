package com.example.pfabackendfinal.repository;


import com.example.pfabackendfinal.entity.BookedApp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;



    public interface BookingRepository extends JpaRepository<BookedApp, Long> {

        List<BookedApp> findByDoctorId(Long doctorId);

       List<BookedApp> findByDoctorEmail(String doctorEmail);


        Optional<BookedApp> findByBookingConfirmationCode(String confirmationCode);

        List<BookedApp> findByPatientEmail(String email);
    }
