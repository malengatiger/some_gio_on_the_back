package com.boha.geo.models;

import com.boha.geo.monitor.data.City;

import java.util.List;

public class CacheBag {
    private List<City> cities;
    private List<CityPlace> places;
    private List<CityAggregate> aggregates;
    private List<DashboardData> dashboards;
    private double elapsedSeconds;
    private String date;

    public List<City> getCities() {
        return cities;
    }

    public void setCities(List<City> cities) {
        this.cities = cities;
    }

    public List<CityPlace> getPlaces() {
        return places;
    }

    public void setPlaces(List<CityPlace> places) {
        this.places = places;
    }

    public List<CityAggregate> getAggregates() {
        return aggregates;
    }

    public void setAggregates(List<CityAggregate> aggregates) {
        this.aggregates = aggregates;
    }

    public List<DashboardData> getDashboards() {
        return dashboards;
    }

    public void setDashboards(List<DashboardData> dashboards) {
        this.dashboards = dashboards;
    }

    public double getElapsedSeconds() {
        return elapsedSeconds;
    }

    public void setElapsedSeconds(double elapsedSeconds) {
        this.elapsedSeconds = elapsedSeconds;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
