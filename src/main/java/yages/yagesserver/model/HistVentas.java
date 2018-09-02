package yages.yagesserver.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Setter;

@Entity 
@Table(name="histventas")
@NoArgsConstructor
@AllArgsConstructor
public class HistVentas  implements Serializable{	
	 private static final long serialVersionUID = 1L;

	 @Column(name = "hve_kilven")
	 @Setter @Getter private double kilosVenta;
	 
	 @Column(name = "hve_impven")
	 @Setter @Getter private double importeVenta;
	 
	 @Column(name = "hve_impgan")
	 @Setter @Getter private double importeGanancia;
	 	 	
	 
	 @EmbeddedId
	 @Setter @Getter private HistVentasKey histVentasKey;
	 	
	 public HistVentas(HistVentasKey hVentasKey)
	 {
		 histVentasKey=hVentasKey;		 
	 }
}
