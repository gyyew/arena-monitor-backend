package com.example.court.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.court.entity.Court;
import com.example.court.mapper.CourtMapper;
import com.example.court.service.CourtService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CourtServiceImpl implements CourtService {

    private final CourtMapper courtMapper;

    @Override
    public List<Court> getAllCourts() {
        return courtMapper.selectList(new LambdaQueryWrapper<Court>()
                .orderByAsc(Court::getCourtNumber));
    }

    @Override
    public Court getCourtById(Long id) {
        return courtMapper.selectById(id);
    }

    @Override
    public boolean updateCourtStatus(Long id, String status) {
        Court court = courtMapper.selectById(id);
        if (court == null) {
            throw new RuntimeException("Court not found: " + id);
        }
        court.setStatus(status);
        return courtMapper.updateById(court) > 0;
    }
}