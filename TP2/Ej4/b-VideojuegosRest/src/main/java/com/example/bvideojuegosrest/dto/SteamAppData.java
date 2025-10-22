package com.example.bvideojuegosrest.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
 

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SteamAppData {
    private String type;
    private String name;
    private Integer steam_appid;
    private String short_description;
    private String about_the_game;
    private String header_image;
    private List<String> developers;
    private List<SteamGenreItem> genres;
    private SteamPriceOverview price_overview;
    private SteamReleaseDate release_date;

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public Integer getSteam_appid() { return steam_appid; }
    public void setSteam_appid(Integer steam_appid) { this.steam_appid = steam_appid; }
    public String getShort_description() { return short_description; }
    public void setShort_description(String short_description) { this.short_description = short_description; }
    public String getAbout_the_game() { return about_the_game; }
    public void setAbout_the_game(String about_the_game) { this.about_the_game = about_the_game; }
    public String getHeader_image() { return header_image; }
    public void setHeader_image(String header_image) { this.header_image = header_image; }
    public List<String> getDevelopers() { return developers; }
    public void setDevelopers(List<String> developers) { this.developers = developers; }
    public List<SteamGenreItem> getGenres() { return genres; }
    public void setGenres(List<SteamGenreItem> genres) { this.genres = genres; }
    public SteamPriceOverview getPrice_overview() { return price_overview; }
    public void setPrice_overview(SteamPriceOverview price_overview) { this.price_overview = price_overview; }
    public SteamReleaseDate getRelease_date() { return release_date; }
    public void setRelease_date(SteamReleaseDate release_date) { this.release_date = release_date; }
}
