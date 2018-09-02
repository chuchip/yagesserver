package yages.yagesserver.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@Data		 
@AllArgsConstructor
@NoArgsConstructor
public class CalendarioKey implements Serializable {		  
	    private static final long serialVersionUID = 2L;
            
            @Column (name="cal_mes", nullable=false)    
	    private int mes;
	    @Column (name="cal_ano", nullable=false)
	    private int ano;		      
}
