package com.example.taller.controller;



import com.example.taller.entity.Mecanico;
import com.example.taller.service.MecanicoService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/mecanico")
public class MecanicoController extends BaseController<Mecanico, String> {

    public MecanicoController(MecanicoService service) {
        super(service);
        initController(new Mecanico(), "Listado de Mecánicos", "Editar Mecánico");
    }
}
