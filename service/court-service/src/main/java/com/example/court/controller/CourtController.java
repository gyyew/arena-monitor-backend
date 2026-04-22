package com.example.court.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.common.api.Result;
import com.example.court.entity.Court;
import com.example.court.entity.CourtMonitor;
import com.example.court.service.CourtService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/v1/courts")
@RequiredArgsConstructor
@Validated
public class CourtController {

    private final CourtService courtService;

    @GetMapping
    public Result<List<Court>> getAllCourts() {
        List<Court> courts = courtService.getAllCourts();
        return Result.success(courts);
    }

    @GetMapping("/{courtId}")
    public Result<Court> getCourt(@PathVariable("courtId") Integer courtId) {
        Court court = courtService.getCourtById(courtId);
        if (court == null) {
            return Result.error(404, "Court not found");
        }
        return Result.success(court);
    }

    @PostMapping
    public Result<Court> addCourt(@RequestBody Court court) {
        try {
            Court newCourt = courtService.addCourt(court);
            return Result.success(newCourt);
        } catch (RuntimeException e) {
            return Result.error(400, e.getMessage());
        }
    }

    @PutMapping("/{courtId}")
    public Result<Court> updateCourt(@PathVariable("courtId") Integer courtId, @RequestBody Court court) {
        try {
            Court updatedCourt = courtService.updateCourt(courtId, court);
            return Result.success(updatedCourt);
        } catch (RuntimeException e) {
            return Result.error(400, e.getMessage());
        }
    }

    @DeleteMapping("/{courtId}")
    public Result<String> deleteCourt(@PathVariable("courtId") Integer courtId) {
        try {
            boolean success = courtService.deleteCourt(courtId);
            if (success) {
                return Result.success("Court deleted successfully");
            } else {
                return Result.error(404, "Court not found");
            }
        } catch (RuntimeException e) {
            return Result.error(400, e.getMessage());
        }
    }

    @GetMapping("/{courtId}/monitor/latest")
    public Result<CourtMonitor> getLatestMonitorData(@PathVariable("courtId") Integer courtId) {
        CourtMonitor monitorData = courtService.getLatestMonitorData(courtId);
        if (monitorData == null) {
            return Result.error(404, "No monitor data found");
        }
        return Result.success(monitorData);
    }

    @GetMapping("/{courtId}/monitor/history")
    public Result<IPage<CourtMonitor>> getHistoryMonitorData(
            @PathVariable("courtId") Integer courtId,
            @RequestParam("startTime") @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime startTime,
            @RequestParam("endTime") @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime endTime,
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {
        IPage<CourtMonitor> historyData = courtService.getHistoryMonitorData(courtId, startTime, endTime, page, size);
        return Result.success(historyData);
    }

    @GetMapping("/monitor/all")
    public Result<List<CourtMonitor>> getAllLatestMonitorData() {
        List<CourtMonitor> monitorDataList = courtService.getAllLatestMonitorData();
        return Result.success(monitorDataList);
    }
}