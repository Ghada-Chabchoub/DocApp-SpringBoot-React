package com.example.pfabackendfinal.services;


import com.example.pfabackendfinal.entity.Doctor;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;



    public interface IDoctorService {
        Doctor addNewDoctor(MultipartFile photo,
                            String specialite,
                            String doctorName,
                            String email,
                            String experience) throws SQLException, IOException;

        List<String> getAllSpecialites();

         List<Doctor> getAllDoctors();

       byte[] getDoctorPhotoByDoctorId(Long doctorId) throws SQLException;

        void deleteDoctor(Long doctorId);

      Doctor updateDoctor(Long doctorId, String specialite, String doctorName, String email, String experience, byte[] photoBytes);

        Optional<Doctor> getDoctorById(Long doctorId);

        Optional<Doctor> getDoctorByEmail(String doctorEmail);


        List<Doctor> getAvailableDoctors(LocalDate date, LocalTime heur, String specialite);
        Doctor getAllDoctorByEmail(String email);



    }
