package com.archivos;

import java.util.List;
import java.util.ArrayList;
import java.util.Scanner;
import java.io.File;
import java.util.StringTokenizer;

public class Executor{
	public static void main(String[] args){
		List<Cliente> clientes = new ArrayList<Cliente>();
		String path = "clientes.txt";

		File file = new File(path);
		try{
			Scanner scanner = new Scanner(file);
			while (scanner.hasNextLine()) {
				//System.out.println("+++++");
				String linea = scanner.nextLine();

				StringTokenizer token = new StringTokenizer(linea, ",");
				Cliente cliente = new Cliente();
				while (token.hasMoreElements()) {
					cliente.setId(Integer.parseInt(token.nextElement().toString()));
					cliente.setNombre(token.nextElement().toString());
					cliente.setApellido(token.nextElement().toString());
					cliente.setDireccion(token.nextElement().toString());
				}
				clientes.add(cliente);

				//System.out.println(scanner.nextLine());

			}
			scanner.close();

			clientes.forEach(c -> {
				System.out.println("ID: " + c.getId());
				System.out.println("Nombre: " + c.getNombre());
				System.out.println("Apellido: " + c.getApellido());
				System.out.println("Direccion: " + c.getDireccion());
				System.out.println("-----");
			});
		}
		catch(Exception e){
			e.printStackTrace();
		
		}





	}

}