package yages.yagesserver.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class CalendarioKey implements Serializable {
		  
	    private static final long serialVersionUID = 2L;
		@Column (name="cal_mes", nullable=false)    
	    private int mes;
	    @Column (name="cal_ano", nullable=false)
	    private int ano;
	
	    
	    public int getAno() {
	        return ano;
	    }

	    public void setAno(int ano) {
	        this.ano = ano;
	    }

	    public int getMes() {
	        return mes;
	    }

	    public void setMes(int mes) {
	        this.mes = mes;
	    }
	    public CalendarioKey() {}
	    
	 public CalendarioKey(int ano, Integer mes) {
	        this.setAno(ano);
	        this.setMes(mes);
	    }
	  @Override
	    public int hashCode() {
	        return (this.getAno()
	                ^  this.getMes());
	    }

	    @Override
	    public boolean equals(Object otherOb) {

	        if (this == otherOb) {
	            return true;
	        }
	        if (!(otherOb instanceof CalendarioKey)) {
	            return false;
	        }
	        CalendarioKey other = (CalendarioKey) otherOb;
	        return this.getAno()==other.getAno()   && this.getMes() == other.getMes();
	    }

	    @Override
	    public String toString() {
	        return "" + getAno() + "-" + getMes();
	    }

	       
}
