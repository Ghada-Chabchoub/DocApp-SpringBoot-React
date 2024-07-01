package com.example.pfabackendfinal.services;

import com.example.pfabackendfinal.entity.BookedApp;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;


public interface IBookingService {
    void cancelBooking(Long bookingId);

    List<BookedApp> getAllBookingsByDoctorId(Long doctorId);
    List<BookedApp> getAllBookingsByDoctorEmail(String doctorEmail);


    String saveBooking(Long doctorId, BookedApp bookingRequest);
    //String saveBooking(String doctorEmail, BookedApp bookingRequest);


    BookedApp findByBookingConfirmationCode(String confirmationCode);

    List<BookedApp> getAllBookings();

    List<BookedApp> getBookingsByUserEmail(String email);
    BookedApp updateBooking(Long id, LocalDate date, LocalTime heur);

    BookedApp getBooking(Long bookingId);

}
