package com.example.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.entity.dto.TopicType;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author Jay Wong
 * @date 2023/11/4
 */
@Mapper
public interface TopicTypeMapper extends BaseMapper<TopicType> {
}
