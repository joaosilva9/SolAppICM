/*
 * Copyright (C) 2017 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package ua.pt.solapp;

import android.support.annotation.Nullable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import ua.pt.solapp.utils.SunshineDateUtils;

/**
 * Parser for OpenWeatherMap JSON data.
 */
final class OpenWeatherJsonParser {

    // Weather information. Each day's forecast info is an element of the "list" array
    private static final String OWM_LIST = "data";

    private static final String OWM_PRECIPITYPROB = "precipitaProb";
    private static final String OWM_TMIN = "tMin";
    private static final String OWM_TMAX= "tMax";
    private static final String OWM_PREDWINDDIR = "predWindDir";
    private static final String OWM_IDWEATHERTYPE = "idWeatherType";
    private static final String OWM_CLASSWINDSPEED = "classWindSpeed";
    private static final String OWM_LONGITUDE = "longitude";
    private static final String OWM_DATA = "forecastDate";
    private static final String OWM_LATITUDE = "latitude";
    private static final String OWM_IDLOCAL = "globalIdLocal";
    private static final String OWM_dateUpdate = "dataUpdate";
    private static final String DATE_FORMAT_PATTERN1 = "yyyy-MM-dd'T'HH:mm:ss";
    private static final String DATE_FORMAT_PATTERN2 = "yyyy-MM-dd";

    private static final String OWM_MESSAGE_CODE = "IPMA";

    private static boolean hasHttpError(JSONObject forecastJson) throws JSONException {
        if (forecastJson.has(OWM_MESSAGE_CODE)) {
            int errorCode = forecastJson.getInt(OWM_MESSAGE_CODE);

            switch (errorCode) {
                case HttpURLConnection.HTTP_OK:
                    return false;
                case HttpURLConnection.HTTP_NOT_FOUND:
                    // Location invalid
                default:
                    // Server probably down
                    return true;
            }
        }
        return false;
    }

    private static WeatherEntry[] fromJson(final JSONObject forecastJson) throws JSONException {
        JSONArray jsonWeatherArray = forecastJson.getJSONArray(OWM_LIST);

        WeatherEntry[] weatherEntries = new WeatherEntry[jsonWeatherArray.length()];

        /*
         * OWM returns daily forecasts based upon the local time of the city that is being asked
         * for, which means that we need to know the GMT offset to translate this data properly.
         * Since this data is also sent in-order and the first day is always the current day, we're
         * going to take advantage of that to get a nice normalized UTC date for all of our weather.
         */
        long normalizedUtcStartDay = SunshineDateUtils.getNormalizedUtcMsForToday();

        for (int i = 0; i < jsonWeatherArray.length(); i++) {
            // Get the JSON object representing the day
            JSONObject dayForecast = jsonWeatherArray.getJSONObject(i);

            // Create the weather entry object
            long dateTimeMillis = normalizedUtcStartDay + SunshineDateUtils.DAY_IN_MILLIS * i;
            WeatherEntry weather = fromJson(forecastJson, dayForecast, dateTimeMillis);

            weatherEntries[i] = weather;
        }
        return weatherEntries;
    }

    private static WeatherEntry fromJson(final JSONObject forecastJson, final JSONObject dayForecast,
                                         long dateTimeMillis) throws JSONException {
        // We ignore all the datetime values embedded in the JSON and assume that
        // the values are returned in-order by day (which is not guaranteed to be correct).

        Date date = null;
        try {
            date = new SimpleDateFormat(DATE_FORMAT_PATTERN2).parse(dayForecast.getString(OWM_DATA)); //forecastDate
        } catch (ParseException e) {
            e.printStackTrace();
        }

        double maxTemp = Double.parseDouble(dayForecast.getString(OWM_TMAX));
        double minTemp = Double.parseDouble(dayForecast.getString(OWM_TMIN));
        double probPrecipitacao = Double.parseDouble(dayForecast.getString(OWM_PRECIPITYPROB));
        int globalIdLocal = forecastJson.getInt(OWM_IDLOCAL);

        Date dataUpdate = null;
        try {
            dataUpdate = new SimpleDateFormat(DATE_FORMAT_PATTERN1).parse(forecastJson.getString(OWM_dateUpdate)); //dataUpdate
        } catch (ParseException e) {
            e.printStackTrace();
        }

        String predWindDir = dayForecast.getString(OWM_PREDWINDDIR);
        int idWeatherType = dayForecast.getInt(OWM_IDWEATHERTYPE);
        int classWindSpeed = dayForecast.getInt(OWM_CLASSWINDSPEED);
        double longitude = Double.parseDouble(dayForecast.getString(OWM_LONGITUDE));
        double latitude = Double.parseDouble(dayForecast.getString(OWM_LATITUDE));


        // Create the weather entry object
        return new WeatherEntry(date, maxTemp, minTemp, probPrecipitacao, globalIdLocal, dataUpdate, predWindDir, idWeatherType,
                                    classWindSpeed, longitude, latitude);
    }

    /**
     * This method parses JSON from a web response and returns an array of Strings
     * describing the weather over various days from the forecast.
     *
     * @param forecastJsonStr JSON response from server
     * @return Array of Strings describing weather data
     * @throws JSONException If JSON data cannot be properly parsed
     */
    @Nullable
    WeatherResponse parse(final String forecastJsonStr) throws JSONException {
        JSONObject forecastJson = new JSONObject(forecastJsonStr);

        // Is there an error?
        if (hasHttpError(forecastJson)) {
            return null;
        }

        WeatherEntry[] weatherForecast = fromJson(forecastJson);

        return new WeatherResponse(weatherForecast);
    }
}
