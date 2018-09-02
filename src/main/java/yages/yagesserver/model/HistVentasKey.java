package yages.yagesserver.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Temporal;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Setter;
import lombok.Getter;

@Data
@Embeddable
@NoArgsConstructor
@AllArgsConstructor
public class HistVentasKey implements Serializable  {

	 private static final long serialVersionUID = 1L;
	
	 @Column(name = "hve_fecini",nullable = false)
	 @Temporal(javax.persistence.TemporalType.DATE)
	 @Setter @Getter private Date fechaInicio;
	 
	 @Column(name = "hve_fecfin",nullable = false)
	 @Temporal(javax.persistence.TemporalType.DATE)
	 @Setter @Getter private Date FechaFinal;

	 @Column(name = "div_codi", columnDefinition="int default 1", nullable = false)
	 @Setter @Getter private int divisa;
         
	 public HistVentasKey(Date fecInicio,Date fecFinal)
	 {
		this(fecInicio, fecFinal, 0);
	 }
//	 public HistVentasKey(Date fecInicio,Date fecFinal, int divCodi)
//	 {
//		 fechaInicio=fecInicio;
//		 FechaFinal=fecFinal;
//		 divisa=divCodi;
//	 }
}
