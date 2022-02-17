package com.example.maledetta_treest;

public class Sponsor {
    private String nome, url, message, lat, lon, logo;

    public Sponsor(String nome, String url, String message, String lat, String lon, String logo) {
        this.nome = nome;
        this.url = url;
        this.message = message;
        this.lat = lat;
        this.lon = lon;
        this.logo = logo;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLon() {
        return lon;
    }

    public void setLon(String lon) {
        this.lon = lon;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }
}
