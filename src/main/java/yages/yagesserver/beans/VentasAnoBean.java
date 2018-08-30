/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yages.yagesserver.beans;


import java.util.ArrayList;

import lombok.Data;

@Data
public class VentasAnoBean {
	private ArrayList<VentasMesBean> ventasMes=new ArrayList<VentasMesBean>();
	private double kilosVentaAct,impVentaAct,kilosVentaAnt,impVentaAnt;
	
	/**
	 * Añade un mes y actualiza acumuladores
	 * @param ventas
	 */
	public void addMes(VentasMesBean ventas)
	{
		ventasMes.add(ventas);
		setKilosVentaAct(getKilosVentaAct()+ventas.getKilosVentaAct());
		setKilosVentaAnt(getKilosVentaAnt()+ventas.getKilosVentaAnt());
  	    setImpVentaAct(getImpVentaAct()+ventas.getImpVentaAct());
  	    setImpVentaAnt(getImpVentaAnt()+ventas.getImpVentaAnt());
	}
	public double getKilosVentaDiferencia()
	{
		return kilosVentaAct-kilosVentaAnt;
	}
}

