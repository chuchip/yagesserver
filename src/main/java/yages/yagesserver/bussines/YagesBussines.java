/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yages.yagesserver.bussines;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Optional;
import javax.persistence.Query;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import yages.util.Formatear;
import yages.yagesserver.beans.VentasAnoBean;
import yages.yagesserver.beans.VentasMesBean;
import yages.yagesserver.beans.VentasSemanaBean;
import yages.yagesserver.dao.CalendarioRepository;
import yages.yagesserver.dao.HistVentasRepository;
import yages.yagesserver.model.Calendario;
import yages.yagesserver.model.CalendarioKey;
import yages.yagesserver.model.HistVentas;
import yages.yagesserver.model.HistVentasKey;

@Component
public class YagesBussines {

    @Autowired
    CalendarioRepository calendarioRepository;

    @Autowired
    HistVentasRepository histVentasRepository;

    @Autowired
    DataSource dataSource;        
  
    
//	  private static final Logger mylog = Logger.getLogger("gnu.chu.yages.bussines.CalendarioRepository");
    public YagesBussines() {
        System.out.println("Arrancado bean Bussines");
    }

    public VentasAnoBean getVentasAno(int ano) {
        List<VentasMesBean> cal = getKilosPorMes(ano);
        VentasAnoBean ventasAno = new VentasAnoBean();
        cal.forEach((v) -> {
            ventasAno.addMes(v);
        });
       
        if (ventasAno.getKilosVentaAct() == 0) {
            throw new VentasNotFoundException(ano);
        }
        return ventasAno;
    }
    
    @Autowired
    private JdbcOperations jdbc;

    @Bean
    public JdbcTemplate jdbcTemplate(DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }

    public List<VentasMesBean> getKilosPorMes(int ano) {        
          
          List<VentasMesBean> cal =
                jdbc.query(
                "SELECT c.cal_ano,c.cal_mes , sum(avc_kilos) as kilosVenta "
                    + "FROM Calendario c,  v_Albavec v "
                    + " where  c.cal_ano =  ?" 
                    + " and v.avc_fecalb between c.cal_fecini and c.cal_fecfin "
                    + " group by cal_ano,cal_mes "
                    + " order by c.cal_mes",  new Object[] { ano },
                (rs, rowNum) -> new VentasMesBean(rs.getInt("cal_ano"),rs.getInt("cal_mes"),rs.getDouble("kilosVenta"))
                        );
             List<VentasMesBean> calAnt =
                jdbc.query(
                "SELECT c.cal_ano,c.cal_mes , sum(avc_kilos) as kilosVenta "
                    + "FROM Calendario c,  v_Albavec v "
                    + " where  c.cal_ano =  ?" 
                    + " and v.avc_fecalb between c.cal_fecini and c.cal_fecfin "
                    + " group by cal_ano,cal_mes "
                    + " order by c.cal_mes",  new Object[] { ano - 1},
                (rs, rowNum) -> new VentasMesBean(rs.getInt("cal_ano"),rs.getInt("cal_mes"),rs.getDouble("kilosVenta"))
                        );
                        
            for (VentasMesBean v : cal) {
                for (VentasMesBean vAnt : calAnt) {
                    if (v.getMes() == vAnt.getMes()) {
                        v.setKilosVentaAnt(vAnt.getKilosVentaAct());
                    }
                }
            }
            return cal;
       
    }

    public ArrayList<VentasSemanaBean> getDatosSemana(int mes, int ano) {
        Calendario calAnterior = null;

        Optional<Calendario> calOpc = calendarioRepository.findById(new CalendarioKey(ano - 1, mes));
        if (calOpc.isPresent()) {
            calAnterior = calOpc.get();
            try {
                ajustaFechas(calAnterior, ano - 1, mes);
            } catch (ParseException e) {
                throw new VentasNotFoundException(e.getMessage(), ano, mes);
            }
        }
        calOpc = calendarioRepository.findById(new CalendarioKey(ano, mes));
        if (!calOpc.isPresent()) {
            throw new VentasNotFoundException(ano, mes);
        }
        Calendario calActual = calOpc.get();
        try {
            ajustaFechas(calActual, ano, mes);
        } catch (ParseException e) {
            throw new VentasNotFoundException(e.getMessage(), ano, mes);
        }

        ArrayList<HistVentas> histVentActual = new ArrayList<HistVentas>();
        ArrayList<HistVentas> histVentAnt = new ArrayList<HistVentas>();

        while (Formatear.comparaFechas(calActual.getFechaInicio(), calActual.getFechaFinal()) <= 0) {

            if (Formatear.comparaFechas(calActual.getFechaFinalSemana(), calActual.getFechaFinal()) > 0 && mes == 12) {
                calActual.setFechaFinalSemana(calActual.getFechaFinal());
            }
            histVentActual.add(addSemana(calActual.getFechaInicio(), calActual.getFechaFinalSemana()));
            incrementaSemana(calActual);

            if (calAnterior.getFechaInicio() != null && Formatear.comparaFechas(calAnterior.getFechaInicio(), calAnterior.getFechaFinal()) <= 0) {
                if (Formatear.comparaFechas(calAnterior.getFechaFinalSemana(), calAnterior.getFechaFinal()) > 0 && mes == 12) {
                    calAnterior.setFechaFinalSemana(calAnterior.getFechaFinal());
                }
                histVentAnt.add(addSemana(calAnterior.getFechaInicio(), calAnterior.getFechaFinalSemana()));
                incrementaSemana(calAnterior);
            } else {
                histVentAnt.add(null);
            }
        } // Fin de bucle

        if (calAnterior.getFechaInicio() != null && Formatear.comparaFechas(calAnterior.getFechaInicio(), calAnterior.getFechaFinal()) < 0) {
            histVentActual.add(null);
            histVentAnt.add(addSemana(calAnterior.getFechaInicio(), calAnterior.getFechaFinalSemana()));
        }
        int n = 0;
        ArrayList<VentasSemanaBean> ventasSemana = new ArrayList<VentasSemanaBean>();
        for (HistVentas hVen : histVentActual) {
            HistVentas hVenAnt = histVentAnt.get(n);
            VentasSemanaBean vSem = new VentasSemanaBean();

            if (hVen != null) {
                vSem.setFechaInicioAct(hVen.getHistVentasKey().getFechaInicio());
                vSem.setFechaFinalAct(hVen.getHistVentasKey().getFechaFinal());
                vSem.setKilosVentaAct(Formatear.format(hVen.getKilosVenta(), "-,---,--9"));
                vSem.setImpVentaAct(Formatear.format(hVen.getImporteVenta(), "--,---,--9"));
            }
            if (hVenAnt != null) {
                vSem.setFechaInicioAnt(hVenAnt.getHistVentasKey().getFechaInicio());
                vSem.setFechaFinalAnt(hVenAnt.getHistVentasKey().getFechaFinal());
                vSem.setKilosVentaAnt(Formatear.format(hVenAnt.getKilosVenta(), "-,---,--9"));
                vSem.setImpVentaAnt(Formatear.format(hVenAnt.getImporteVenta(), "--,---,--9"));
            }
            ventasSemana.add(vSem);
            n++;
        }
        return ventasSemana;
    }

    /**
     * Incrementa en siete dias los valores de las fechas de la clase Calendario
     * mandado
     *
     * @param Clase Calendario
     */
    void incrementaSemana(Calendario cal) {
        cal.setFechaInicio(Formatear.sumaDiasDate(cal.getFechaFinalSemana(), 1));
        cal.setFechaFinalSemana(Formatear.sumaDiasDate(cal.getFechaFinalSemana(), 7));
        if (Formatear.comparaFechas(cal.getFechaFinalSemana(), cal.getFechaFinal()) > 0) {
            cal.setFechaFinalSemana(cal.getFechaFinal());
        }
    }

    HistVentas addSemana(java.util.Date fecIni, java.util.Date fecFin) {
        if (Formatear.comparaFechas(fecIni, fecFin) > 0) {
            return null;
        }

        Optional<HistVentas> opcVen = histVentasRepository.findById(new HistVentasKey(fecIni, fecFin));
        if (opcVen.isPresent()) {
            return opcVen.get();
        } else {
            return new HistVentas(new HistVentasKey(fecIni, fecFin));
        }

    }

    /**
     * Ajusta fechas Final semana, de tal manera que si el primer dia es el 1 de
     * enero, busco el primer domingo, para establecerlo como final de la
     * semana. En caso contrario le suma 7 dias a la inicial.
     *
     * @param cal
     * @param ano
     * @param mes
     * @throws ParseException
     */
    private void ajustaFechas(Calendario cal, int ano, int mes) throws ParseException {

        if (mes == 1 && Formatear.comparaFechas(cal.getFechaInicio(),
                Formatear.getDate("01-01-" + Formatear.format(ano, "9999"), "dd-MM-yyyy")) == 0) { // Principio del a�o.Busco el primer domingo
            GregorianCalendar gcal = new GregorianCalendar();
            for (int n = 1; n <= 8; n++) {
                cal.setFechaFinalSemana(Formatear.sumaDiasDate(cal.getFechaInicio(), n));
                gcal.setTime(cal.getFechaFinalSemana());
                if (gcal.get(GregorianCalendar.DAY_OF_WEEK) == GregorianCalendar.SUNDAY && n > 1) {
                    break;
                }
            }
        } else {
            cal.setFechaFinalSemana(Formatear.sumaDiasDate(cal.getFechaInicio(), 7));
        }
    }

}
