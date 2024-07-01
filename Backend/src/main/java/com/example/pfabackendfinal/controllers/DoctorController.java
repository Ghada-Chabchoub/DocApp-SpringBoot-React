package com.example.pfabackendfinal.controllers;


import com.example.pfabackendfinal.entity.BookedApp;
import com.example.pfabackendfinal.entity.Doctor;
import com.example.pfabackendfinal.exception.PhotoRetrievalException;
import com.example.pfabackendfinal.exception.ResourceNotFoundException;
import com.example.pfabackendfinal.response.BookingResponse;
import com.example.pfabackendfinal.response.DoctorResponse;
import com.example.pfabackendfinal.services.BookingService;
import com.example.pfabackendfinal.services.IDoctorService;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import javax.sql.rowset.serial.SerialBlob;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Blob;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@RestController
//@CrossOrigin(origins = "http://localhost:5173")
@RequiredArgsConstructor // to inject les parm hekom fl constructeur
@RequestMapping("/doctors")
public class DoctorController{
    @Autowired
    private final IDoctorService doctorService;
    @Autowired
    private final BookingService bookingService;

    @PostMapping("/add/new-doctor")
   //@PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<DoctorResponse> addNewDoctor(
            @RequestParam("photo") MultipartFile photo,
            @RequestParam("specialite") String specialite,
            @RequestParam("doctorName") String doctorName,
            @RequestParam("email") String email,
            @RequestParam("experience") String experience)throws SQLException, IOException {
        Doctor savedDoctor = doctorService.addNewDoctor(photo, specialite, doctorName,email,experience);

        DoctorResponse response = new DoctorResponse(
                savedDoctor.getId(),
                savedDoctor.getSpecialite(),
                savedDoctor.getDoctorName(),
                savedDoctor.getEmail(),
                savedDoctor.getExperience());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/doctor/doctor-specialite")
    public List<String> getSpecialites() {
        return doctorService.getAllSpecialites();
    }

    @GetMapping("/all-doctors")
    public ResponseEntity<List<DoctorResponse>> getAllDoctors() throws SQLException {
        List<Doctor> doctors = doctorService.getAllDoctors();
        List<DoctorResponse> doctorResponses = new ArrayList<>();
        for (Doctor doctor : doctors) {
            byte[] photoBytes = doctorService.getDoctorPhotoByDoctorId(doctor.getId());
            if (photoBytes != null && photoBytes.length > 0) {
                String base64Photo = Base64.encodeBase64String(photoBytes);
                DoctorResponse doctorResponse = getDoctorResponse(doctor);
                doctorResponse.setPhoto(base64Photo);
                doctorResponses.add(doctorResponse);
            }
        }
        return ResponseEntity.ok(doctorResponses);
    }
   @DeleteMapping("/delete/doctor/{doctorId}")
   // @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Void> deleteDoctor(@PathVariable Long doctorId){
        doctorService.deleteDoctor(doctorId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/update/{doctorId}")
    //@PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<DoctorResponse> updateDoctor(@PathVariable Long doctorId,
                                                   @RequestParam(required = false)  String specialite,
                                                   @RequestParam(required = false) String doctorName,
                                                   @RequestParam(required = false) String email,
                                                   @RequestParam(required = false) String experience,
                                                   @RequestParam(required = false) MultipartFile photo) throws SQLException, IOException {
       byte[] photoBytes = photo != null && !photo.isEmpty() ?
                photo.getBytes() : doctorService.getDoctorPhotoByDoctorId(doctorId);
        Blob photoBlob = photoBytes != null && photoBytes.length >0 ? new SerialBlob(photoBytes): null;
        Doctor theDoctor = doctorService.updateDoctor(doctorId, specialite, doctorName, email, experience, photoBytes);
        theDoctor.setPhoto(photoBlob);
        DoctorResponse doctorResponse = getDoctorResponse(theDoctor);
        return ResponseEntity.ok(doctorResponse);
    }

    @GetMapping("/doctor/{doctorId}")
    public ResponseEntity<Optional<DoctorResponse>> getDoctorById(@PathVariable Long doctorId){
        Optional<Doctor> theDoctor = doctorService.getDoctorById(doctorId);
        return theDoctor.map(doctor -> {
            DoctorResponse doctorResponse = getDoctorResponse(doctor);
            return  ResponseEntity.ok(Optional.of(doctorResponse));
        }).orElseThrow(() -> new ResourceNotFoundException("Doctor not found"));
    }

    @GetMapping("/available-doctors")
    public ResponseEntity<List<DoctorResponse>> getAvailableDoctors(
            @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)LocalDate date,
            @RequestParam("heur") @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime heur,
            @RequestParam("specialite") String specialite) throws SQLException {
        List<Doctor> availableDoctors = doctorService.getAvailableDoctors(date, heur, specialite);
        List<DoctorResponse> doctorResponses = new ArrayList<>();
        for (Doctor doctor : availableDoctors){
            byte[] photoBytes = doctorService.getDoctorPhotoByDoctorId(doctor.getId());
            if (photoBytes != null && photoBytes.length > 0){
                String photoBase64 = Base64.encodeBase64String(photoBytes);
                DoctorResponse doctorResponse = getDoctorResponse(doctor);
                doctorResponse.setPhoto(photoBase64);
                doctorResponses.add(doctorResponse);
            }
        }
        if(doctorResponses.isEmpty()){
            return ResponseEntity.noContent().build();
        }else{
            return ResponseEntity.ok(doctorResponses);
        }
    }




  /*  private DoctorResponse getDoctorResponse(Doctor doctor) {
        List<BookedApp> bookings = getAllBookingsByDoctorId(doctor.getId());
       List<BookingResponse> bookingInfo = bookings
                .stream()
                .map(booking -> new BookingResponse(booking.getBookingId(),
                        booking.getDate(),
                        booking.getHeur(),
                        booking.getBookingConfirmationCode())).toList();
        byte[] photoBytes = null;
        Blob photoBlob = doctor.getPhoto();
        if (photoBlob != null) {
            try {
                photoBytes = photoBlob.getBytes(1, (int) photoBlob.length());
            } catch (SQLException e) {
                throw new PhotoRetrievalException("Error retrieving photo");
            }
        }
        return new DoctorResponse(
                doctor.getId(),
                doctor.getSpecialite(),
                doctor.getDoctorName(),
                doctor.getEmail(),
                doctor.getExperience(),
                doctor.isBooked(), photoBytes , bookingInfo);
    }*/

    private DoctorResponse getDoctorResponse(Doctor doctor) {
        List<BookedApp> bookings = getAllBookingsByDoctorEmail(doctor.getEmail());
        List<BookingResponse> bookingInfo = bookings
                .stream()
                .map(booking -> new BookingResponse(booking.getBookingId(),
                        booking.getDate(),
                        booking.getHeur(),
                        booking.getBookingConfirmationCode())).toList();
        byte[] photoBytes = null;
        Blob photoBlob = doctor.getPhoto();
        if (photoBlob != null) {
            try {
                photoBytes = photoBlob.getBytes(1, (int) photoBlob.length());
            } catch (SQLException e) {
                throw new PhotoRetrievalException("Error retrieving photo");
            }
        }
        return new DoctorResponse(
                doctor.getId(),
                doctor.getSpecialite(),
                doctor.getDoctorName(),
                doctor.getEmail(),
                doctor.getExperience(),
                doctor.isBooked(), photoBytes , bookingInfo);
    }

    private List<BookedApp> getAllBookingsByDoctorId(Long doctorId) {
        return bookingService.getAllBookingsByDoctorId(doctorId);

    }
    private List<BookedApp> getAllBookingsByDoctorEmail(String doctorEmail) {
        return bookingService.getAllBookingsByDoctorEmail(doctorEmail);

    }



}