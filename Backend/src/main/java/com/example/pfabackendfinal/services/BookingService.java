package com.example.pfabackendfinal.services;

import com.example.pfabackendfinal.entity.BookedApp;
import com.example.pfabackendfinal.entity.Doctor;
import com.example.pfabackendfinal.entity.User;
import com.example.pfabackendfinal.exception.InvalidBookingRequestException;
import com.example.pfabackendfinal.exception.ResourceNotFoundException;
import com.example.pfabackendfinal.repository.BookingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookingService implements IBookingService {
    private final BookingRepository bookingRepository;
    private final IDoctorService doctorService;


    @Override
    public List<BookedApp> getAllBookings() {
        return bookingRepository.findAll();
    }
    @Override
    public BookedApp getBooking(Long bookingId) {
        return bookingRepository.findById(bookingId)
                .orElseThrow(() -> new UsernameNotFoundException("booking not found"));
    }

    @Override
    public List<BookedApp> getBookingsByUserEmail(String email) {
        return bookingRepository.findByPatientEmail(email);
    }

    @Override
    public void cancelBooking(Long bookingId) {
        bookingRepository.deleteById(bookingId);
    }

    @Override
    public List<BookedApp> getAllBookingsByDoctorId(Long doctorId) {
        return bookingRepository.findByDoctorId(doctorId);
    }
     @Override
    public List<BookedApp> getAllBookingsByDoctorEmail(String doctorEmail) {
        return bookingRepository.findByDoctorEmail(doctorEmail);
    }

    @Override
    public String saveBooking(Long doctorId, BookedApp bookingRequest) {
        if (!isValidAppointmentTime(bookingRequest.getHeur())) {
            throw new InvalidBookingRequestException("Appointment time must be between 8:00 AM and 5:00 PM.");
        }
        Doctor doctor = doctorService.getDoctorById(doctorId).get();
        List<BookedApp> existingBookings = doctor.getBookings();
        boolean doctorIsAvailable = doctorIsAvailable(bookingRequest,existingBookings);
        if (doctorIsAvailable){
            doctor.addBooking(bookingRequest);
            bookingRepository.save(bookingRequest);
        }else{
            throw  new InvalidBookingRequestException("Sorry, This doctor is not available for the selected date please try to change the date;");
        }
        return bookingRequest.getBookingConfirmationCode();
    }

    /*@Override
    public String saveBooking(String doctorEmail, BookedApp bookingRequest) {
        if (!isValidAppointmentTime(bookingRequest.getHeur())) {
            throw new InvalidBookingRequestException("Appointment time must be between 8:00 AM and 5:00 PM.");
        }
        Doctor doctor = doctorService.getDoctorByEmail(doctorEmail).get();
        List<BookedApp> existingBookings = doctor.getBookings();
        boolean doctorIsAvailable = doctorIsAvailable(bookingRequest,existingBookings);
        if (doctorIsAvailable){
            doctor.addBooking(bookingRequest);
            bookingRepository.save(bookingRequest);
        }else{
            throw  new InvalidBookingRequestException("Sorry, This doctor is not available for the selected date please try to change the date;");
        }
        return bookingRequest.getBookingConfirmationCode();
    }
*/
    @Override
    public BookedApp findByBookingConfirmationCode(String confirmationCode) {
        return bookingRepository.findByBookingConfirmationCode(confirmationCode)
                .orElseThrow(() -> new ResourceNotFoundException("No booking found with booking code :"+confirmationCode));

    }


    private boolean doctorIsAvailable(BookedApp bookingRequest, List<BookedApp> existingBookings) {
        return existingBookings.stream().noneMatch(existingBooking ->
                bookingRequest.getDate().equals(existingBooking.getDate()) &&
                        (bookingRequest.getHeur().isBefore(existingBooking.getHeur().plusMinutes(15)) ||
                                bookingRequest.getHeur().isAfter(existingBooking.getHeur().minusMinutes(15)))
        );
    }

    private boolean isValidAppointmentTime(LocalTime appointmentTime) {
        LocalTime startTime = LocalTime.of(8, 0);
        LocalTime endTime = LocalTime.of(17, 0);
        return appointmentTime.isAfter(startTime) && appointmentTime.isBefore(endTime);
    }
    @Override
    public BookedApp updateBooking(Long bookingId, LocalDate date, LocalTime heur) {
        BookedApp booking = bookingRepository.findById(bookingId).get();
        if (date != null) booking.setDate(date);
        if (heur != null) booking.setHeur(heur);
       // if (patientEmail != null) booking.setPatientEmail(patientEmail);
        return bookingRepository.save(booking);

    }





}
