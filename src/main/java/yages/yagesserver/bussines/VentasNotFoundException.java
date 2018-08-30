/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yages.yagesserver.bussines;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
class VentasNotFoundException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public VentasNotFoundException(int ano) {
		this(ano, 0);
	}
	public VentasNotFoundException(int ano, int mes) {
		super("No encontradas ventas para año '" + ano + "'"+ (mes>0?" y Mes: "+mes:""));
	}
	public VentasNotFoundException(String msgError,int ano, int mes) {
		super("Error al buscar ventas para año '" + ano + "'"+ (mes>0?" y Mes: "+mes:"")+" \n Error: "+msgError);
	}
}