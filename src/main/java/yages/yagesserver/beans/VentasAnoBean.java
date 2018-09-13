package yages.yagesserver.beans;


import java.util.ArrayList;

import lombok.Data;

@Data
public class VentasAnoBean {
    private ArrayList<VentasMesBean> ventasAno= new ArrayList();

    /**
     * @param ventasMes Objeto VentasMesBean con las  ventas de un mes
     */
    public void addMes(VentasMesBean ventasMes) {
        this.ventasAno.add(ventasMes);
    }
 
}
