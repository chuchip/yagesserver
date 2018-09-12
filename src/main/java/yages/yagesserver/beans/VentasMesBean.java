package yages.yagesserver.beans;

import lombok.Data;

@Data
public class VentasMesBean {

    private int  mes;
    private double kilosVentaAct, impVentaAct, kilosVentaAnt, impVentaAnt;
    private double gananAct, gananAnt;

    public VentasMesBean(int mes) {
        setMes(mes);
    }

    public VentasMesBean( int mes, double kilosVenta, double impVenta, double ganancia) {        
        setMes(mes);
        setKilosVentaAct(kilosVenta);
        setImpVentaAct(impVenta);
        setGananAct(ganancia);
    }
}
