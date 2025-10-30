package com.sport.proyecto.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.beans.factory.annotation.Autowired;

import com.sport.proyecto.servicios.EmpresaServicio;

@Controller
@RequestMapping("/portal")
public class PortalController {

    @Autowired
    private EmpresaServicio empresaServicio;
    @Autowired
    private com.sport.proyecto.servicios.RutinaServicio rutinaServicio;
    @Autowired
    private com.sport.proyecto.servicios.SocioServicio socioServicio;
    @Autowired
    private com.sport.proyecto.servicios.EmpleadoServicio empleadoServicio;

    private boolean hasRole(String role) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || auth.getAuthorities() == null) return false;
        return auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals(role));
    }

    @GetMapping("/socio")
    public String socioPortal(Model model) {
        if (!hasRole("ROLE_SOCIO")) {
            return "redirect:/login";
        }
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        model.addAttribute("username", auth.getName());
        return "portals/socio_portal";
    }

    @GetMapping("/socio/rutinas")
    public String socioRutinas(Model model) {
        if (!hasRole("ROLE_SOCIO")) return "redirect:/login";
        // reuse the existing /rutina/mis_rutinas controller/view which already filters by rol
        return "redirect:/rutina/mis_rutinas";
    }

    @GetMapping("/socio/pagos")
    public String socioPagos(Model model) {
        if (!hasRole("ROLE_SOCIO")) return "redirect:/login";
        model.addAttribute("username", SecurityContextHolder.getContext().getAuthentication().getName());
        return "portals/socio_pagos";
    }

    @GetMapping("/admin")
    public String adminPortal(Model model) {
        if (!hasRole("ROLE_ADMIN")) return "redirect:/login";
        model.addAttribute("username", SecurityContextHolder.getContext().getAuthentication().getName());
        return "portals/admin_portal";
    }

    @GetMapping("/admin/rutinas")
    public String adminRutinas(Model model) {
        if (!hasRole("ROLE_ADMIN")) return "redirect:/login";
        model.addAttribute("entityName", "Rutinas");
        try {
            model.addAttribute("items", rutinaServicio.listarTodasLasRutinas());
        } catch (Exception e) {
            model.addAttribute("items", new java.util.ArrayList<>());
        }
        // agregar listas para selects en el admin
        try {
            model.addAttribute("socios", socioServicio.obtenerSociosActivos());
        } catch (Exception e) {
            model.addAttribute("socios", new java.util.ArrayList<>());
        }
        try {
            model.addAttribute("profesores", empleadoServicio.obtenerProfesores());
        } catch (Exception e) {
            model.addAttribute("profesores", new java.util.ArrayList<>());
        }
        return "portals/admin_manage_rutinas";
    }

    @GetMapping("/admin/rutina/nuevo")
    public String adminNuevoRutina(Model model) {
        if (!hasRole("ROLE_ADMIN")) return "redirect:/login";
        try {
            model.addAttribute("socios", socioServicio.obtenerSociosActivos());
        } catch (Exception ex) {
            model.addAttribute("socios", new java.util.ArrayList<>());
        }
        try {
            model.addAttribute("profesores", empleadoServicio.obtenerProfesores());
        } catch (Exception ex) {
            model.addAttribute("profesores", new java.util.ArrayList<>());
        }
        return "portals/admin_rutina_form";
    }

    @GetMapping("/admin/rutina/editar/{id}")
    public String adminEditarRutina(@PathVariable String id, Model model) {
        if (!hasRole("ROLE_ADMIN")) return "redirect:/login";
        try {
            com.sport.proyecto.entidades.Rutina rutina = rutinaServicio.buscarRutina(id);
            model.addAttribute("rutina", rutina);
        } catch (Exception ex) {
            model.addAttribute("rutina", null);
        }
        try {
            model.addAttribute("socios", socioServicio.obtenerSociosActivos());
        } catch (Exception ex) {
            model.addAttribute("socios", new java.util.ArrayList<>());
        }
        try {
            model.addAttribute("profesores", empleadoServicio.obtenerProfesores());
        } catch (Exception ex) {
            model.addAttribute("profesores", new java.util.ArrayList<>());
        }
        return "portals/admin_rutina_form";
    }

    @GetMapping("/admin/cuotas")
    public String adminCuotas(Model model) {
        if (!hasRole("ROLE_ADMIN")) return "redirect:/login";
        model.addAttribute("entityName", "Cuotas");
        model.addAttribute("items", new java.util.ArrayList<>());
        return "portals/admin_manage_cuotas";
    }

    @GetMapping("/admin/sucursales")
    public String adminSucursales(Model model) {
        if (!hasRole("ROLE_ADMIN")) return "redirect:/login";
        model.addAttribute("entityName", "Sucursales");
        model.addAttribute("items", new java.util.ArrayList<>());
        return "portals/admin_manage_sucursales";
    }

    @GetMapping("/admin/empresas")
    public String adminEmpresas(Model model) {
        if (!hasRole("ROLE_ADMIN")) return "redirect:/login";
        model.addAttribute("entityName", "Empresas");
        try {
            model.addAttribute("items", empresaServicio.listarEmpresaActiva());
        } catch (Exception e) {
            model.addAttribute("items", new java.util.ArrayList<>());
        }
        return "portals/admin_manage_empresas";
    }

    @GetMapping("/admin/geografia")
    public String adminGeografia(Model model) {
        if (!hasRole("ROLE_ADMIN")) return "redirect:/login";
        model.addAttribute("entityName", "Geografía");
        model.addAttribute("items", new java.util.ArrayList<>());
        return "portals/admin_manage_geografia";
    }

    @GetMapping("/empleado")
    public String empleadoPortal(Model model) {
        if (!hasRole("ROLE_EMPLEADO")) return "redirect:/login";
        model.addAttribute("username", SecurityContextHolder.getContext().getAuthentication().getName());
        return "portals/empleado_portal";
    }

    @GetMapping("/empleado/rutinas")
    public String empleadoRutinas(Model model) {
        if (!hasRole("ROLE_EMPLEADO")) return "redirect:/login";
        return "portals/empleado_manage_rutinas";
    }

    @GetMapping("/empleado/rutina/detalle")
    public String empleadoRutinaDetalle(Model model) {
        if (!hasRole("ROLE_EMPLEADO")) return "redirect:/login";
        return "portals/empleado_manage_rutina_detalle";
    }
}
