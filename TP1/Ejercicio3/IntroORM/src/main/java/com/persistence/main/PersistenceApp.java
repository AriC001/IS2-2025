package com.persistence.main;

import com.persistence.entidades.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import java.lang.instrument.ClassDefinition;
import java.sql.Date;
import java.time.LocalDate;

public class PersistenceApp {
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("PersistenceAppPU");
        EntityManager em = emf.createEntityManager();

        try{
            em.getTransaction().begin();

            Domicilio domicilio1 = Domicilio.builder().nombreCalle("Av. San Martin").numero(4).build();
            Domicilio domicilio2 = Domicilio.builder().nombreCalle("Av. Libertador").numero(1).build();


            Cliente cliente1 = Cliente.builder().nombre("Lucas").apellido("Lopez").dni(4534765).build();
            cliente1.setDomicilio(domicilio1);

            Cliente cliente2 = Cliente.builder().nombre("Juan Ignacio").apellido("Massacesi").dni(4534765).build();
            cliente2.setDomicilio(domicilio2);

            Factura factura1 = Factura.builder().fecha("17/08/2009").total(800).numero(1).build();
            factura1.setCliente(cliente1);

            Factura factura2 = Factura.builder().fecha("17/09/2009").total(500).numero(2).build();
            Factura factura3 = Factura.builder().fecha("17/10/2009").total(600).numero(3).build();
            Factura factura4 = Factura.builder().fecha("7/07/2011").total(600).numero(1).cliente(cliente2).build();

            Categoria perecederos = Categoria.builder().denominacion("Perecederos").build();
            Categoria lacteos = Categoria.builder().denominacion("Lacteos").build();

            Articulo art1 = Articulo.builder().cantidad(400).donominacion("Leche entera").precio(2000).build();
            Articulo art2 = Articulo.builder().cantidad(300).donominacion("Brownie").precio(1500).build();

            art1.getCategorias().add(perecederos);
            art1.getCategorias().add(lacteos);
            perecederos.getArticulos().add(art1);
            lacteos.getArticulos().add(art1);

            art2.getCategorias().add(perecederos);
            perecederos.getArticulos().add(art2);

            DetalleFactura det1 = DetalleFactura.builder().articulo(art1).cantidad(2).factura(factura1).build();
            det1.setSubtotal(det1.getCantidad() * det1.getArticulo().getPrecio());

            art1.getDetalles().add(det1);
            factura1.getDetalles().add(det1);

            DetalleFactura det2 = DetalleFactura.builder().articulo(art2).cantidad(3).factura(factura1).build();
            det2.setSubtotal(det2.getCantidad() * det2.getArticulo().getPrecio());

            art2.getDetalles().add(det2);
            factura1.getDetalles().add(det2);

            factura1.setTotal(det1.getSubtotal() + det2.getSubtotal());



            em.persist(factura1);

            em.flush();
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
        }
        em.close();
        emf.close();

        }
    }
