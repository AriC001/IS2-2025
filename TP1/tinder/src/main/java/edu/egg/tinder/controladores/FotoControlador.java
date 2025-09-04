package edu.egg.tinder.controladores;

import edu.egg.tinder.entidades.Foto;
import edu.egg.tinder.entidades.Mascota;
import edu.egg.tinder.entidades.Usuario;
import edu.egg.tinder.servicios.MascotaServicio;
import edu.egg.tinder.servicios.UsuarioServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/foto")
public class FotoControlador {

    @Autowired
    private MascotaServicio mascotaServicio;
    @Autowired
    private UsuarioServicio usuairoServicio;

    @GetMapping("/mascota/{id}")
    @ResponseBody
    public ResponseEntity<byte[]> fotoMascota(@PathVariable Long id) {
        try {
            Mascota mascota = mascotaServicio.buscarMascota(id);

            if (mascota.getFoto() == null) {
                return ResponseEntity.notFound().build();
            }

            Foto foto = mascota.getFoto();

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_TYPE, foto.getMime())
                    .body(foto.getContenido());

        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
    //<img th:if="${perfil.foto != null}" class="img-fluid rounded-circle" th:src="${'/foto/usuario/' + perfil.id}" alt="">
    @GetMapping("/usuario/{id}")
    @ResponseBody
    public ResponseEntity<byte[]> fotoUsuario(@PathVariable Long id) {
        try {
            Usuario usuario = usuairoServicio.findById(id);

            if (usuario.getFoto() == null) {
                return ResponseEntity.notFound().build();
            }

            Foto foto = usuario.getFoto();

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_TYPE, foto.getMime())
                    .body(foto.getContenido());

        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
}