package yages.yagesserver.model;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Temporal;
import javax.persistence.Transient;
import javax.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity 
@Table(name="calendario")
@NoArgsConstructor
public class Calendario implements Serializable{		
	    @Getter private static final long serialVersionUID = 1L;
            
            @Column(name = "cal_fecini",nullable = false)
	    @Temporal(javax.persistence.TemporalType.DATE)
	    @Setter @Getter private Date fechaInicio;
		
	    @Column(name = "cal_fecfin",nullable = false)
	    @Temporal(javax.persistence.TemporalType.DATE)
	    @Setter @Getter private Date fechaFinal;
	    
	    @Transient
	    @Setter @Getter private Date fechaFinalSemana;
	    	 
            @EmbeddedId
	    @Setter @Getter private CalendarioKey calendarioKey;
            
	    public Calendario(int ano,int mes,Date fecInicio,Date fecFinal)
	    {
	    	calendarioKey = new CalendarioKey(ano,mes);
	    	setFechaInicio(fecInicio);
	    	setFechaFinal(fecFinal);
	    }
}
