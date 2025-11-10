package com.example.taller.adapter;

import com.example.taller.dto.UsuarioSafeDTO;
import com.example.taller.entity.Usuario;

public class UsuarioAdapter {

    public static UsuarioSafeDTO toSafeDTO(Usuario u) {
        if (u == null) return null;
        UsuarioSafeDTO dto = new UsuarioSafeDTO();
        dto.setId(u.getId());
        dto.setNombreUsuario(u.getNombreUsuario());
        dto.setRol(u.getRol());
        dto.setEliminado(u.getEliminado());
        return dto;
    }
}
