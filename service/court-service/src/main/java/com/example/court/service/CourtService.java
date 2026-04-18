package com.example.court.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.court.entity.Court;
import com.example.court.entity.CourtMonitor;

import java.time.LocalDateTime;
import java.util.List;

public interface CourtService {

    List<Court> getAllCourts();

    Court getCourtById(Integer courtId);

    Court addCourt(Court court);

    Court updateCourt(Integer courtId, Court court);

    boolean deleteCourt(Integer courtId);

    CourtMonitor getLatestMonitorData(Integer courtId);

    IPage<CourtMonitor> getHistoryMonitorData(Integer courtId, LocalDateTime startTime, LocalDateTime endTime, int page, int size);

    List<CourtMonitor> getAllLatestMonitorData();
}