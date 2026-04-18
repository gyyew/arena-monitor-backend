package com.example.court.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.court.entity.Court;
import com.example.court.entity.CourtMonitor;
import com.example.court.mapper.CourtMapper;
import com.example.court.mapper.CourtMonitorMapper;
import com.example.court.service.CourtService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CourtServiceImpl implements CourtService {

    private final CourtMapper courtMapper;
    private final CourtMonitorMapper courtMonitorMapper;

    @Override
    public List<Court> getAllCourts() {
        return courtMapper.selectList(new LambdaQueryWrapper<Court>()
                .orderByAsc(Court::getCourtId));
    }

    @Override
    public Court getCourtById(Integer courtId) {
        return courtMapper.selectById(courtId);
    }

    @Override
    public Court addCourt(Court court) {
        courtMapper.insert(court);
        return court;
    }

    @Override
    public Court updateCourt(Integer courtId, Court court) {
        Court existingCourt = courtMapper.selectById(courtId);
        if (existingCourt == null) {
            throw new RuntimeException("Court not found: " + courtId);
        }
        court.setCourtId(courtId);
        courtMapper.updateById(court);
        return court;
    }

    @Override
    public boolean deleteCourt(Integer courtId) {
        return courtMapper.deleteById(courtId) > 0;
    }

    @Override
    public CourtMonitor getLatestMonitorData(Integer courtId) {
        return courtMonitorMapper.getLatestByCourtId(courtId);
    }

    @Override
    public IPage<CourtMonitor> getHistoryMonitorData(Integer courtId, LocalDateTime startTime, LocalDateTime endTime, int page, int size) {
        Page<CourtMonitor> pageInfo = new Page<>(page, size);
        return courtMonitorMapper.getHistoryByCourtId(pageInfo, courtId, startTime, endTime);
    }

    @Override
    public List<CourtMonitor> getAllLatestMonitorData() {
        return courtMonitorMapper.getAllLatestMonitorData();
    }
}