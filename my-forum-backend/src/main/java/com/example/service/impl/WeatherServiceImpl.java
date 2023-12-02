package com.example.service.impl;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.example.entity.vo.response.WeatherVO;
import com.example.service.WeatherService;
import com.example.utils.Const;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.zip.GZIPInputStream;

/**
 * @author Jay Wong
 * @date 2023/11/3
 */
@Service
public class WeatherServiceImpl implements WeatherService {
    @Resource
    private RestTemplate restTemplate;
    @Resource
    private StringRedisTemplate stringRedisTemplate;
    @Value("${weather.key}")
    private String key;

    @Override
    public WeatherVO fetchWeather(double longitude, double latitude) {
        return this.fetchFromCache(longitude, latitude);
    }

    private WeatherVO fetchFromCache(double longitude, double latitude) {
        byte[] data = restTemplate.getForObject(
                "https://geoapi.qweather.com/v2/city/lookup?location=" + longitude + "," + latitude + "&key=" + key,
                byte[].class);
        JSONObject geo = decompressToJson(data);
        if (geo == null) {
            return null;
        }
        JSONObject location = geo.getJSONArray("location").getJSONObject(0);
        int id = location.getInteger("id");
        String key = Const.FORUM_WEATHER_CACHE + id;
        String cache = stringRedisTemplate.opsForValue().get(key);
        if (cache != null) {
            return JSONObject.parseObject(cache).to(WeatherVO.class);
        }
        WeatherVO vo = this.fetchFromAPI(id, location);
        if (vo == null) {
            return null;
        }
        stringRedisTemplate.opsForValue().set(key, JSONObject.from(vo).toJSONString(), 30, TimeUnit.MINUTES);
        return vo;
    }

    private WeatherVO fetchFromAPI(int id, JSONObject location) {
        WeatherVO vo = new WeatherVO();
        vo.setLocation(location);
        byte[] nowData = restTemplate.getForObject(
                "https://devapi.qweather.com/v7/weather/now?location=" + id + "&key=" + key,
                byte[].class);
        JSONObject now = decompressToJson(nowData);
        if (now == null) {
            return null;
        }
        vo.setNow(now.getJSONObject("now"));
        byte[] hourlyData = restTemplate.getForObject(
                "https://devapi.qweather.com/v7/weather/24h?location=" + id + "&key=" + key,
                byte[].class);
        JSONObject hourly = decompressToJson(hourlyData);
        if (hourly == null) {
            return null;
        }
        vo.setHourly(new JSONArray(hourly.getJSONArray("hourly").stream().limit(5).toList()));
        return vo;
    }

    private JSONObject decompressToJson(byte[] data) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try {
            GZIPInputStream inputStream = new GZIPInputStream(new ByteArrayInputStream(data));
            byte[] buffer = new byte[1024];
            int read;
            while ((read = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, read);
            }
            inputStream.close();
            outputStream.close();
            return JSONObject.parseObject(outputStream.toString());
        } catch (IOException e) {
            return null;
        }
    }
}
