package com.example.moviescrapper.entities;

public class StreamingOption {
    private String name;
    private String link;
    private String img;

    // Constructor
    public StreamingOption(String serviceName, String serviceLink, String img) {
        this.name = serviceName;
        this.link = serviceLink;
        this.img = img;
    }

    // Getters y setters
    public String getServiceName() { return name; }
    public String getServiceLink() { return link; }
    public String getImg() { return img; }

    @Override
    public String toString() {
        return name + " -> " + link;
    }
}
