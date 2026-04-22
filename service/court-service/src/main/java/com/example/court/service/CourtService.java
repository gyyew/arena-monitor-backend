package com.example.court.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.court.entity.Court;
import com.example.court.entity.CourtMonitor;

import java.time.LocalDateTime;
import java.util.List;

public interface CourtService {

    List<Court> getAllCourts();

    Court getCourtById(Long courtId);

    Court addCourt(Court court);

    Court updateCourt(Long courtId, Court court);

    boolean deleteCourt(Long courtId);

    CourtMonitor getLatestMonitorData(Long courtId);

    IPage<CourtMonitor> getHistoryMonitorData(Long courtId, LocalDateTime startTime, LocalDateTime endTime, int page, int size);

    List<CourtMonitor> getAllLatestMonitorData();
}