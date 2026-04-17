package com.example.court.service;

import com.example.court.entity.Court;

import java.util.List;

public interface CourtService {

    List<Court> getAllCourts();

    Court getCourtById(Long id);

    boolean updateCourtStatus(Long id, String status);
}