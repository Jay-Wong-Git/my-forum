package com.example.entity.vo.response;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import lombok.Data;

/**
 * @author Jay Wong
 * @date 2023/11/3
 */
@Data
public class WeatherVO {
    private JSONObject location;
    private JSONObject now;
    private JSONArray hourly;
}
