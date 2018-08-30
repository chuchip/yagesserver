/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yages.yagesserver.beans;

import lombok.Data;

@Data
public class VentasSemanaBean {
		
	private java.util.Date fechaInicioAct,fechaFinalAct, fechaInicioAnt,fechaFinalAnt;
	private String kilosVentaAct,impVentaAct,kilosVentaAnt,impVentaAnt;
}
