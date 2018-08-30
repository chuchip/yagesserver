/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yages.yagesserver.beans;


import lombok.Data;

@Data
public class VentasMesBean {

	    private int ano,mes;
	    private double kilosVentaAct,impVentaAct,kilosVentaAnt,impVentaAnt;
	 
	    
	    public VentasMesBean(int ano,int mes) {
	        setAno(ano);
	        setMes(mes);
	     }
	    public VentasMesBean(int ano,int mes,double kilosVenta) {
	        setAno(ano);
	        setMes(mes);
	        setKilosVentaAct(kilosVenta);
	     }
	   
	    public double getKilosDiferencia()
	    {
	        return kilosVentaAct-kilosVentaAnt;
	    }
	    public void setKilosDiferencia(boolean kg)
	    {
	        
	    }

}
