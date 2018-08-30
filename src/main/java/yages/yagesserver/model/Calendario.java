package yages.yagesserver.model;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.SqlResultSetMapping;
import javax.persistence.ConstructorResult;
import javax.persistence.ColumnResult;
import javax.persistence.Temporal;
import javax.persistence.Transient;
import javax.persistence.Table;


@Entity 
@Table(name="calendario",schema="anjelica")
@SqlResultSetMapping(name = "ventasMes",
classes={
@ConstructorResult(targetClass=yages.yagesserver.beans.VentasMesBean.class,
columns={
@ColumnResult(name="cal_ano"),
@ColumnResult(name="cal_mes"),
@ColumnResult(name="kilosVenta"),
})
})
public class Calendario implements Serializable{		
	    private static final long serialVersionUID = 1L;
		@Column(name = "cal_fecini",nullable = false)
	    @Temporal(javax.persistence.TemporalType.DATE)
	    private Date fechaInicio;
		
	    @Column(name = "cal_fecfin",nullable = false)
	    @Temporal(javax.persistence.TemporalType.DATE)
	    private Date fechaFinal;
	    
	    @Transient
	    private Date fechaFinalSemana;
	    	 
	    public Calendario(int ano,int mes,Date fecInicio,Date fecFinal)
	    {
	    	calendarioKey = new CalendarioKey(ano,mes);
	    	setFechaInicio(fecInicio);
	    	setFechaFinal(fecFinal);
	    }
	    @EmbeddedId
	    private CalendarioKey calendarioKey;

	    public Calendario() {

	    }
	    
	    

	    public CalendarioKey getCalendarioKey() {
			return calendarioKey;
		}



		public void setCalendarioKey(CalendarioKey calendarioKey) {
			this.calendarioKey = calendarioKey;
		}



		public Date getFechaInicio() {
	        return fechaInicio;
	    }

	    public void setFechaInicio(Date fechaInicio) {
	        this.fechaInicio = fechaInicio;
	    }

	    public Date getFechaFinal() {
	        return fechaFinal;
	    }

	    public void setFechaFinal(Date fechaFinal) {
	        this.fechaFinal = fechaFinal;
	    }



		public Date getFechaFinalSemana() {
			return fechaFinalSemana;
		}



		public void setFechaFinalSemana(Date fechaFinalAnt) {
			this.fechaFinalSemana = fechaFinalAnt;
		}



		public static long getSerialversionuid() {
			return serialVersionUID;
		}

}
