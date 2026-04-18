package com.example.court.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.court.entity.CourtMonitor;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author 
 * @since 2024-04-01
 */
public interface CourtMonitorMapper extends BaseMapper<CourtMonitor> {

    /**
     * 获取场地最新的监测数据
     */
    CourtMonitor getLatestByCourtId(@Param("courtId") Integer courtId);

    /**
     * 分页查询场地历史监测数据
     */
    IPage<CourtMonitor> getHistoryByCourtId(Page<CourtMonitor> page, 
                                           @Param("courtId") Integer courtId, 
                                           @Param("startTime") LocalDateTime startTime, 
                                           @Param("endTime") LocalDateTime endTime);

    /**
     * 获取所有场地的最新监测数据
     */
    List<CourtMonitor> getAllLatestMonitorData();
}
