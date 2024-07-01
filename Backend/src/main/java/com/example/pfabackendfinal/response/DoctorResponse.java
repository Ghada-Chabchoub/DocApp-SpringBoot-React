package com.example.pfabackendfinal.response;



import jakarta.persistence.Lob;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.tomcat.util.codec.binary.Base64;

import java.math.BigDecimal;
import java.sql.Blob;
import java.util.List;


@Data
@NoArgsConstructor
public class DoctorResponse {
    private  Long id;
    private String specialite;
    private String doctorName;
    private String email;
    private String experience;
    private boolean isBooked = false;
    private String photo;
    private List<BookingResponse>bookings;

    public DoctorResponse(Long id, String specialite, String doctorName, String email, String experience) {
        this.id = id;
        this.specialite = specialite;
        this.doctorName = doctorName;
        this.email = email;
        this.experience = experience;
    }

    public DoctorResponse(Long id, String specialite, String doctorName, String email, String experience, boolean isBooked,
                          byte[] photoBytes, List<BookingResponse> bookings ) {
        this.id = id;
        this.specialite = specialite;
        this.doctorName = doctorName;
        this.email = email;
        this.experience = experience;
        this.isBooked = isBooked;
        this.photo = photoBytes != null ? Base64.encodeBase64String(photoBytes) : null;
        this.bookings = bookings;
    }
}
