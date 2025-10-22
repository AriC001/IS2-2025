package com.example.bvideojuegosrest.entities;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.util.Date;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Videogame {
    @Id
    @GeneratedValue(strategy =GenerationType.IDENTITY)
    @JsonProperty("id")
    private long id;
    @NotEmpty(message="{NotEmpty.Videojuego.titulo}")
    private String title;
    @Size(min=2,max=250,message= "La descripción debe ser entre 2 y 250 caracteres")
    private String description;
    private String img;
    @Min(value=0,message="El precio debe ser mínimo $0")
    @Max(value=100,message = "El precio debe ser máximo $100")
    private float price;
    @Min(value=0,message = "El stock debe ser mínimo 0")
    @Max(value=1000, message = "El stock debe ser menor a 1000")
    private short stock;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @NotNull(message="No puede ser nulo la fecha")
    @PastOrPresent(message="Debe ser igual o menor a la fecha de hoy")
    private Date releaseDate;
    private boolean active = true;

    //@NotNull(message="Es requerido el estudio")
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "fk_developers", nullable = false)
    private Developer developers;

    //@NotNull(message="Es requerida la categoria")
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "fk_genres", nullable = false)
    private Genre genres;


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public short getStock() {
        return stock;
    }

    public void setStock(short stock) {
        this.stock = stock;
    }

    public Date getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(Date releaseDate) {
        this.releaseDate = releaseDate;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Developer getDevelopers() {
        return developers;
    }

    public void setDevelopers(Developer developers) {
        this.developers = developers;
    }

    public Genre getGenres() {
        return genres;
    }

    public void setGenres(Genre genres) {
        this.genres = genres;
    }
}
