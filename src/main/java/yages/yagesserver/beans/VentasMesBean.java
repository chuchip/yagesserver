package yages.yagesserver.beans;

import lombok.Data;

@Data
public class VentasMesBean {

    private int ano, mes;
    private double kilosVentaAct, impVentaAct, kilosVentaAnt, impVentaAnt;
    private double gananAct, gananAnt;

    public VentasMesBean(int ano, int mes) {
        setAno(ano);
        setMes(mes);
    }

    public VentasMesBean(int ano, int mes, double kilosVenta, double impVenta, double ganancia) {
        setAno(ano);
        setMes(mes);
        setKilosVentaAct(kilosVenta);
        setImpVentaAct(impVenta);
        setGananAct(ganancia);
    }
}
