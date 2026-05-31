package com.example.weather;

import com.example.weather.client.WeatherServiceClient;
import com.example.weather.model.WeatherResponse;
import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.rest.client.inject.RestClient;

@ApplicationScoped
public class ActivityService {

    @RestClient
    WeatherServiceClient weatherClient;

    public String getRecommendation(double lat, double lon) {

        WeatherResponse response = weatherClient.getCurrentWeather(lat, lon, true);

        double temp = response.currentWeather().temperature();
        int weatherCode = response.currentWeather().weatherCode();

        String recommendation;

        if (weatherCode >= 51) {
            recommendation = "Stay home and read a book";
        } else if (temp > 25 && weatherCode == 0) {
            recommendation = "Go to the beach";
        } else if (temp >= 15 && temp <= 25 &&
                (weatherCode == 0 || weatherCode == 1)) {
            recommendation = "Go for a hike";
        } else if (temp < 15) {
            recommendation = "Visit a museum";
        } else {
            recommendation = "Take a walk";
        }

        return applyAnomalyLogic(temp, weatherCode, recommendation);
    }

    public String applyAnomalyLogic(
            double temp,
            int weatherCode,
            String recommendation) {

        // Step 1: Base score
        double baseScore = (temp * 0.8) + (weatherCode * 0.2);

        // IMPORTANT: Use type casting, NOT Math.round()
        int score = (int) baseScore;

        // Step 2: XOR only if weatherCode is odd
        if (weatherCode % 2 != 0) {
            score = score ^ 0x0F;
        }

        // Step 3: Reverse recommendation if score is Twin Prime
        if (isTwinPrime(score)) {
            return new StringBuilder(recommendation)
                    .reverse()
                    .toString();
        }

        return recommendation;
    }

    private boolean isTwinPrime(int n) {

        if (!isPrime(n)) {
            return false;
        }

        return isPrime(n - 2) || isPrime(n + 2);
    }

    private boolean isPrime(int n) {

        if (n < 2) {
            return false;
        }

        for (int i = 2; i * i <= n; i++) {
            if (n % i == 0) {
                return false;
            }
        }

        return true;
    }
}