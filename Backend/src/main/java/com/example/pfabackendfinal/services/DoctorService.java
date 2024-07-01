package com.example.pfabackendfinal.services;



import com.example.pfabackendfinal.entity.Doctor;
import com.example.pfabackendfinal.exception.InternalServerException;
import com.example.pfabackendfinal.exception.ResourceNotFoundException;
import com.example.pfabackendfinal.repository.DoctorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.sql.rowset.serial.SerialBlob;
import java.io.IOException;
import java.sql.Blob;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;



@Service
@RequiredArgsConstructor
public class DoctorService implements IDoctorService {
        private final DoctorRepository doctorRepository;
    @Override
    public Doctor addNewDoctor(MultipartFile file,
                                   String specialite,
                                   String doctorName,
                                   String email,
                                   String experience) throws SQLException, IOException {
            Doctor doctor = new Doctor();
            doctor.setSpecialite(specialite);
            doctor.setDoctorName(doctorName);
            doctor.setEmail(email);
            doctor.setExperience(experience);

            if (!file.isEmpty()){
                byte[] photoBytes = file.getBytes();
                Blob photoBlob = new SerialBlob(photoBytes);
                doctor.setPhoto(photoBlob);
            }
            return doctorRepository.save(doctor);
        }

        @Override
        public List<String> getAllSpecialites() {
            return doctorRepository.findDistinctSpecialites();
        }

        @Override
        public List<Doctor> getAllDoctors() {
            return doctorRepository.findAll();
        }

        @Override
        public byte[] getDoctorPhotoByDoctorId(Long doctorId) throws SQLException {
            Optional<Doctor> theDoctor = doctorRepository.findById(doctorId);
            if(theDoctor.isEmpty()){
                throw new ResourceNotFoundException("Sorry, Doctor not found!");
            }
            Blob photoBlob = theDoctor.get().getPhoto();
            if(photoBlob != null){
                return photoBlob.getBytes(1, (int) photoBlob.length());
            }
            return null;
        }

        @Override
        public void deleteDoctor(Long doctorId) {
            Optional<Doctor> theDoctor = doctorRepository.findById(doctorId);
            if(theDoctor.isPresent()){
                doctorRepository.deleteById(doctorId);
            }
        }

        @Override
        public Doctor updateDoctor(Long doctorId, String specialite,String doctorName, String email, String experience, byte[] photoBytes) {
            Doctor doctor = doctorRepository.findById(doctorId).get();
            if (specialite != null) doctor.setSpecialite(specialite);
            if (doctorName != null) doctor.setDoctorName(doctorName);
            if (email != null) doctor.setEmail(email);
            if (experience != null) doctor.setExperience(experience);
            if (photoBytes != null && photoBytes.length > 0) {
                try {
                    doctor.setPhoto(new SerialBlob(photoBytes));
                } catch (SQLException ex) {
                    throw new InternalServerException("Fail updating doctor");
                }
            }
            return doctorRepository.save(doctor);
        }

        @Override
        public Optional<Doctor> getDoctorById(Long doctorId) {
            return Optional.of(doctorRepository.findById(doctorId).get());
        }

   @Override
   public Optional<Doctor> getDoctorByEmail(String doctorEmail) {
       return Optional.of(doctorRepository.findDoctorByEmail(doctorEmail));
   }
    ////
    @Override
    public Doctor getAllDoctorByEmail(String email) {
        return doctorRepository.findAllDoctorByEmail(email);
    }
////
       @Override
        public List<Doctor> getAvailableDoctors(LocalDate date, LocalTime heur, String specialite) {
            return doctorRepository.findAvailableDoctorsByDatesAndSpeciality(date, heur, specialite);
        }


    }
