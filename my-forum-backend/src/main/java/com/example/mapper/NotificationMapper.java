package com.example.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.entity.dto.Notification;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author Jay Wong
 * @date 2023/11/6
 */
@Mapper
public interface NotificationMapper extends BaseMapper<Notification> {
}
