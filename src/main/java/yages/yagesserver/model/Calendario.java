package yages.yagesserver.model;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Temporal;
import javax.persistence.Transient;
import javax.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity 
@Table(name="calendario")
@NoArgsConstructor
@Data	
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
	    	 
            @EmbeddedId
	    private CalendarioKey calendarioKey;
            
	    public Calendario(int ano,int mes,Date fecInicio,Date fecFinal)
	    {
	    	calendarioKey = new CalendarioKey(ano,mes);
	    	setFechaInicio(fecInicio);
	    	setFechaFinal(fecFinal);
	    }
}
