package com.example.court.controller;

import com.example.court.common.Result;
import com.example.court.entity.Court;
import com.example.court.service.CourtService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/courts")
@RequiredArgsConstructor
@Validated
public class CourtController {

    private final CourtService courtService;

    @GetMapping
    public Result<List<Court>> getAllCourts() {
        List<Court> courts = courtService.getAllCourts();
        return Result.success(courts);
    }

    @GetMapping("/{id}")
    public Result<Court> getCourt(@PathVariable("id") Long id) {
        Court court = courtService.getCourtById(id);
        if (court == null) {
            return Result.error(404, "Court not found");
        }
        return Result.success(court);
    }

    @PutMapping("/{id}/status")
    public Result<Void> updateStatus(
            @PathVariable("id") Long id,
            @RequestParam("status") String status) {
        boolean updated = courtService.updateCourtStatus(id, status);
        if (!updated) {
            return Result.error(404, "Court not found");
        }
        return Result.success();
    }
}