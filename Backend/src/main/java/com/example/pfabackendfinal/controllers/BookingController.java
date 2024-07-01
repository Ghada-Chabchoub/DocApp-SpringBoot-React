package com.example.pfabackendfinal.controllers;

import com.example.pfabackendfinal.dto.UpdateBookingDto;
import com.example.pfabackendfinal.dto.UpdateUserDto;
import com.example.pfabackendfinal.entity.BookedApp;
import com.example.pfabackendfinal.entity.Doctor;
import com.example.pfabackendfinal.entity.User;
import com.example.pfabackendfinal.exception.InvalidBookingRequestException;
import com.example.pfabackendfinal.exception.ResourceNotFoundException;
import com.example.pfabackendfinal.response.BookingResponse;
import com.example.pfabackendfinal.response.DoctorResponse;
import com.example.pfabackendfinal.response.UserResponse;
import com.example.pfabackendfinal.services.IBookingService;
import com.example.pfabackendfinal.services.IDoctorService;
import com.example.pfabackendfinal.services.IUserService;
import com.example.pfabackendfinal.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;



@RestController
@RequiredArgsConstructor
@RequestMapping("/bookings")
//@CrossOrigin(origins = "http://localhost:5173")
public class BookingController {
    @Autowired
     private final IBookingService bookingService;
    @Autowired
    private final IDoctorService doctorService;
    @Autowired
    private final IUserService userService;

    @GetMapping("/all-bookings")
  //  @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<List<BookingResponse>> getAllBookings(){
        List<BookedApp> bookings = bookingService.getAllBookings();
        List<BookingResponse> bookingResponses = new ArrayList<>();
        for (BookedApp booking : bookings){
            BookingResponse bookingResponse = getBookingResponse(booking);
            bookingResponses.add(bookingResponse);
        }
        return ResponseEntity.ok(bookingResponses);
    }

    @GetMapping("/confirmation/{confirmationCode}")
    public ResponseEntity<?> getBookingByConfirmationCode(@PathVariable String confirmationCode){
        try{
            BookedApp booking = bookingService.findByBookingConfirmationCode(confirmationCode);
            BookingResponse bookingResponse = getBookingResponse(booking);
            return ResponseEntity.ok(bookingResponse);
        }catch (ResourceNotFoundException ex){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        }
    }

   @PostMapping("/doctor/{doctorId}/booking")
    public ResponseEntity<?> saveBooking(@PathVariable Long doctorId,
                                         @RequestBody BookedApp bookingRequest){
        try{
            String confirmationCode = bookingService.saveBooking(doctorId, bookingRequest);

            return ResponseEntity.ok(
                    "Apointment booked successfully, Your booking confirmation code is :"+confirmationCode);

        }catch (InvalidBookingRequestException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
  /* @PostMapping("/doctor/{doctorEmail}/booking")
   public ResponseEntity<?> saveBooking(@PathVariable String doctorEmail,
                                        @RequestBody BookedApp bookingRequest){
       try{
           String confirmationCode = bookingService.saveBooking(doctorEmail, bookingRequest);

           return ResponseEntity.ok(
                   "Apointment booked successfully, Your booking confirmation code is :"+confirmationCode);

       }catch (InvalidBookingRequestException e){
           return ResponseEntity.badRequest().body(e.getMessage());
       }
   }*/



    @GetMapping("/user/{email}/bookings")
    public ResponseEntity<List<BookingResponse>> getBookingsByUserEmail(@PathVariable String email) {
        List<BookedApp> bookings = bookingService.getBookingsByUserEmail(email);
        List<BookingResponse> bookingResponses = new ArrayList<>();
        for (BookedApp booking : bookings) {
            BookingResponse bookingResponse = getBookingResponse(booking);
            bookingResponses.add(bookingResponse);
        }
        return ResponseEntity.ok(bookingResponses);
    }

    @DeleteMapping("/booking/{bookingId}/delete")
    public void cancelBooking(@PathVariable Long bookingId){
        bookingService.cancelBooking(bookingId);
    }

    private BookingResponse getBookingResponse(BookedApp booking) {
        Doctor theDoctor = doctorService.getDoctorById(booking.getDoctor().getId()).get();
        DoctorResponse doctor = new DoctorResponse(
                theDoctor.getId(),
                theDoctor.getSpecialite(),
                theDoctor.getDoctorName(),
                theDoctor.getEmail(),
                theDoctor.getExperience());
        return new BookingResponse(
                booking.getBookingId(), booking.getDate(),
                booking.getHeur(),booking.getPatientFullName(),
                booking.getPatientEmail(), booking.getBookingConfirmationCode()
                , doctor);
    }
 /*  private BookingResponse getBookingResponse(BookedApp booking) {
        Doctor theDoctor = doctorService.getAllDoctorByEmail(booking.getDoctor().getEmail());
        DoctorResponse doctor = new DoctorResponse(
                theDoctor.getId(),
                theDoctor.getSpecialite(),
                theDoctor.getDoctorName(),
                theDoctor.getEmail(),
                theDoctor.getExperience());
        return new BookingResponse(
                booking.getBookingId(), booking.getDate(),
                booking.getHeur(),booking.getPatientFullName(),
                booking.getPatientEmail(), booking.getBookingConfirmationCode()
                , doctor);
    }*/


    //zyedaa w shiiha
    /*@GetMapping("/doctor/{id}/bookings")
    public ResponseEntity<List<BookingResponse>> getBookingsByDoctorId(@PathVariable Long id) {
        List<BookedApp> bookings = bookingService.getAllBookingsByDoctorId(id);
        List<BookingResponse> bookingResponses = new ArrayList<>();
        for (BookedApp booking : bookings) {
            BookingResponse bookingResponse = getDoctorBookingResponse(booking);
            bookingResponses.add(bookingResponse);
        }
        return ResponseEntity.ok(bookingResponses);
    }*/

    @GetMapping("/doctor/{email}/bookings")
    public ResponseEntity<List<BookingResponse>> getBookingsByDoctorEmail(@PathVariable String email) {
        List<BookedApp> bookings = bookingService.getAllBookingsByDoctorEmail(email);
        List<BookingResponse> bookingResponses = new ArrayList<>();
        for (BookedApp booking : bookings) {
            BookingResponse bookingResponse = getDoctorBookingResponse(booking);
            bookingResponses.add(bookingResponse);
        }
        return ResponseEntity.ok(bookingResponses);
    }


    private BookingResponse getDoctorBookingResponse(BookedApp booking) {
        User theUser = userService.getUser(booking.getPatientEmail());
        UserResponse user = new UserResponse(

                theUser.getName(),
                theUser.getEmail()
                );
        return new BookingResponse(
                booking.getBookingId(), booking.getDate(),
                booking.getHeur(),booking.getPatientFullName(),
                booking.getPatientEmail()
                ,user);
    }

    @PutMapping("/update/{bookingId}")

    public ResponseEntity<BookingResponse> updateBooking(@PathVariable Long bookingId,
                                                      @RequestBody UpdateBookingDto up
    ) throws SQLException, IOException {


        BookedApp theBooking = bookingService.updateBooking(bookingId, up.getDate(),up.getHeur());
        BookingResponse userResponse = getUpdatedBookingResponse(theBooking);
        return ResponseEntity.ok(userResponse);
    }

    private BookingResponse getUpdatedBookingResponse(BookedApp theBooking) {

        return new BookingResponse(
                theBooking.getBookingId(),
                theBooking.getDate(),
                theBooking.getHeur(),
                theBooking.getPatientEmail()

        );
    }

    @GetMapping("/{bookingId}")
    public ResponseEntity<?> getBooking(@PathVariable("bookingId") Long bookingId){
        try{
            BookedApp theBooking = bookingService.getBooking(bookingId);
            return ResponseEntity.ok(theBooking);
        }catch (UsernameNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());

        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error fetching booking");
        }
    }



}//fin controller
