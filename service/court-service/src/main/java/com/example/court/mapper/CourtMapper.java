package com.example.court.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.court.entity.Court;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface CourtMapper extends BaseMapper<Court> {

    @Select("SELECT * FROM court WHERE status = #{status} AND deleted = 0 ORDER BY court_number")
    List<Court> findByStatus(@Param("status") String status);

    @Select("SELECT * FROM court WHERE court_number = #{courtNumber} AND deleted = 0")
    Court findByCourtNumber(@Param("courtNumber") Integer courtNumber);

    @Update("UPDATE court SET status = #{status}, update_time = NOW() WHERE id = #{id} AND deleted = 0")
    int updateStatusById(@Param("id") Long id, @Param("status") String status);
}