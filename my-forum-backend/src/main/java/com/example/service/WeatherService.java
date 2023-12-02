package com.example.service;

import com.example.entity.vo.response.WeatherVO;

/**
 * @author Jay Wong
 * @date 2023/11/3
 */
public interface WeatherService {
    WeatherVO fetchWeather(double longitude, double latitude);
}
