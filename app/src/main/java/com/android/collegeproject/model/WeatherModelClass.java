package com.android.collegeproject.model;

import java.util.ArrayList;

public class WeatherModelClass {
    Coord coord;
    ArrayList<Weather> weather;
    String base;
    Main main;
    Wind wind;
    Clouds cloud;
    Sys sys;
    int timezone;
    String name;
    int id;
    int statusCode;

    public Coord getCoord() {
        return coord;
    }

    public void setCoord(Coord coord) {
        this.coord = coord;
    }

    public ArrayList<Weather> getWeather() {
        return weather;
    }

    public String getBase() {
        return base;
    }

    public Main getMain() {
        return main;
    }

    public Wind getWind() {
        return wind;
    }

    public Clouds getCloud() {
        return cloud;
    }

    public Sys getSys() {
        return sys;
    }

    public int getTimezone() {
        return timezone;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setWeather(ArrayList<Weather> weather) {
        this.weather = weather;
    }

    public void setBase(String base) {
        this.base = base;
    }

    public void setMain(Main main) {
        this.main = main;
    }

    public void setWind(Wind wind) {
        this.wind = wind;
    }

    public void setCloud(Clouds cloud) {
        this.cloud = cloud;
    }

    public void setSys(Sys sys) {
        this.sys = sys;
    }

    public void setTimezone(int timezone) {
        this.timezone = timezone;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }



   public class Weather{
        int id;
        String main;
        String description;
        String icon;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getMain() {
            return main;
        }

        public void setMain(String main) {
            this.main = main;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getIcon() {
            return icon;
        }

        public void setIcon(String icon) {
            this.icon = icon;
        }
    }
   public class  Main{
        double temp;
        double feels_like;
        double temp_min;
        double temp_max;
        int pressure;
        int humidity;

        public double getTemp() {
            return temp-273.15;
        }

        public void setTemp(double temp) {
            this.temp = temp - 273.15;
        }

        public double getFeels_like() {
            return feels_like;
        }

        public void setFeels_like(double feels_like) {
            this.feels_like = feels_like;
        }

        public double getTemp_min() {
            return temp_min-273.15;
        }

        public void setTemp_min(double temp_min) {
            this.temp_min = temp_min-273.15;
        }

        public double getTemp_max() {
            return temp_max-273.15;
        }

        public void setTemp_max(double temp_max) {
            this.temp_max = temp_max-273.15;
        }

        public int getPressure() {
            return pressure;
        }

        public void setPressure(int pressure) {
            this.pressure = pressure;
        }

        public int getHumidity() {
            return humidity;
        }

        public void setHumidity(int humidity) {
            this.humidity = humidity;
        }
    }
   public class Wind{
        double speed;
        double deg;

        public double getSpeed() {
            return speed;
        }

        public void setSpeed(double speed) {
            this.speed = speed;
        }

        public double getDeg() {
            return deg;
        }

        public void setDeg(double deg) {
            this.deg = deg;
        }
    }
   public class Sys{
        int type;
        int id;
        double message;
        String country;
        long sunrise;
        long sunset;

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public double getMessage() {
            return message;
        }

        public void setMessage(double message) {
            this.message = message;
        }

        public String getCountry() {
            return country;
        }

        public void setCountry(String country) {
            this.country = country;
        }

        public long getSunrise() {
            return sunrise;
        }

        public void setSunrise(int sunrise) {
            this.sunrise = sunrise;
        }

        public long getSunset() {
            return sunset;
        }

        public void setSunset(int sunset) {
            this.sunset = sunset;
        }
    }

}
class Coord{
    double latitude;
    double longitude;

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}

class Clouds{
    int all;

    public int getAll() {
        return all;
    }

    public void setAll(int all) {
        this.all = all;
    }
}


