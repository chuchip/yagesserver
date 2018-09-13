package yages.yagesserver.bussines;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Optional;
import javax.sql.DataSource;
import lombok.NoArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import yages.util.Formatear;
import yages.yagesserver.beans.VentasAnoBean;
import yages.yagesserver.beans.VentasMesBean;
import yages.yagesserver.beans.VentasSemanaBean;
import yages.yagesserver.dao.CalendarioRepositorioService;
import yages.yagesserver.dao.HistVentasRepository;
import yages.yagesserver.model.Calendario;
import yages.yagesserver.model.CalendarioKey;
import yages.yagesserver.model.HistVentas;
import yages.yagesserver.model.HistVentasKey;

@Component
@NoArgsConstructor
public class YagesBussines {
    double kilosTotal;
    @Autowired
    CalendarioRepositorioService calendarioRepositorio;

    @Autowired
    HistVentasRepository histVentasRepository;

//    @Autowired
//    DataSource dataSource;
//    
    @Autowired
    private JdbcOperations jdbc;

    @Bean
    public JdbcTemplate jdbcTemplate(DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }
    
    public VentasAnoBean getVentasAno(int ano) {
        List<VentasMesBean> cal = getKilosPorMes(ano);
        VentasAnoBean ventasAno = new VentasAnoBean();
        kilosTotal=0;
        cal.forEach((v) -> {
            kilosTotal+=v.getKilosVentaAct();
            ventasAno.addMes(v);
        });

        if (kilosTotal == 0) {
            throw new VentasNotFoundException(ano);
        }
        return ventasAno;
    }

   

    public List<VentasMesBean> getKilosPorMes(int ano) {

        List<VentasMesBean> cal
                = jdbc.query(
                        "SELECT c.cal_ano,c.cal_mes , sum(hve_kilven) as kilosVenta,sum(hve_impven) as impVenta,sum(hve_impgan) as impGanancia "
                        + "FROM Calendario c,  histventas h "
                        + " where  c.cal_ano =  ?"
                        + " and h.hve_fecini between c.cal_fecini and c.cal_fecfin "
                        + " and h.hve_fecfin between c.cal_fecini and c.cal_fecfin "
                        + " group by cal_ano,cal_mes "
                        + " order by c.cal_mes", new Object[]{ano},
                        (rs, rowNum) -> new VentasMesBean(rs.getInt("cal_mes"), rs.getDouble("kilosVenta"), rs.getDouble("impVenta"), rs.getDouble("impGanancia"))
                );
        List<VentasMesBean> calAnt
                = jdbc.query(
                        "SELECT c.cal_ano,c.cal_mes , sum(hve_kilven) as kilosVenta,sum(hve_impven) as impVenta,sum(hve_impgan) as impGanancia "
                        + "FROM Calendario c,  histventas h "
                        + " where  c.cal_ano =  ?"
                        + " and h.hve_fecini between c.cal_fecini and c.cal_fecfin "
                        + " and h.hve_fecfin between c.cal_fecini and c.cal_fecfin "
                        + " group by cal_ano,cal_mes "
                        + " order by c.cal_mes", new Object[]{ano - 1},
                        (rs, rowNum) -> new VentasMesBean( rs.getInt("cal_mes"), rs.getDouble("kilosVenta"), rs.getDouble("impVenta"), rs.getDouble("impGanancia"))
                );
        cal.forEach(v -> 
        {
            calAnt.forEach (vAnt -> 
                {
                    if (v.getMes() == vAnt.getMes()) 
                    {
                        v.setKilosVentaAnt(vAnt.getKilosVentaAct());
                        v.setImpVentaAnt(vAnt.getImpVentaAct());
                        v.setGananAnt(vAnt.getGananAct());
                    }
                });
        });            
        
        return cal;

    }

    public ArrayList<VentasSemanaBean> getDatosSemana(int mes, int ano) {
        Calendario calAnterior = null;
        
        Optional<Calendario> calOpc = calendarioRepositorio.getCalendario(new CalendarioKey(mes, ano - 1));
        if (calOpc.isPresent()) {
            calAnterior = calOpc.get();
            try {
                ajustaFechas(calAnterior, ano - 1, mes);
            } catch (ParseException e) {
                throw new VentasNotFoundException(e.getMessage(), ano, mes);
            }
        }

        calOpc = calendarioRepositorio.getCalendario(new CalendarioKey(mes, ano));
        if (!calOpc.isPresent()) {
            throw new VentasNotFoundException(ano, mes);
        }
        Calendario calActual = calOpc.get();
        try {
            ajustaFechas(calActual, ano, mes);
        } catch (ParseException e) {
            throw new VentasNotFoundException(e.getMessage(), ano, mes);
        }

        ArrayList<HistVentas> histVentActual = new ArrayList();
        ArrayList<HistVentas> histVentAnt = new ArrayList();

        while (Formatear.comparaFechas(calActual.getFechaInicio(), calActual.getFechaFinal()) <= 0) {
            if (Formatear.comparaFechas(calActual.getFechaFinalSemana(), calActual.getFechaFinal()) > 0 && mes == 12) {
                calActual.setFechaFinalSemana(calActual.getFechaFinal());
            }
            histVentActual.add(addSemana(calActual.getFechaInicio(), calActual.getFechaFinalSemana()));
            incrementaSemana(calActual);

            if (calAnterior != null && Formatear.comparaFechas(calAnterior.getFechaInicio(), calAnterior.getFechaFinal()) <= 0) {
                if (Formatear.comparaFechas(calAnterior.getFechaFinalSemana(), calAnterior.getFechaFinal()) > 0 && mes == 12) {
                    calAnterior.setFechaFinalSemana(calAnterior.getFechaFinal());
                }
                histVentAnt.add(addSemana(calAnterior.getFechaInicio(), calAnterior.getFechaFinalSemana()));
                incrementaSemana(calAnterior);
            } else {
                histVentAnt.add(null);
            }
        } // Fin de bucle

        if (calAnterior != null && Formatear.comparaFechas(calAnterior.getFechaInicio(), calAnterior.getFechaFinal()) < 0) {
            histVentActual.add(null);
            histVentAnt.add(addSemana(calAnterior.getFechaInicio(), calAnterior.getFechaFinalSemana()));
        }
        int n = 0;
        ArrayList<VentasSemanaBean> ventasSemana = new ArrayList();
        for (HistVentas hVen : histVentActual) {
            HistVentas hVenAnt = histVentAnt.get(n);
            VentasSemanaBean vSem = new VentasSemanaBean();

            if (hVen != null) {
                vSem.setFechaInicioAct(hVen.getHistVentasKey().getFechaInicio());
                vSem.setFechaFinalAct(hVen.getHistVentasKey().getFechaFinal());
                vSem.setKilosVentaAct(hVen.getKilosVenta());
                vSem.setImpVentaAct(hVen.getImporteVenta());
                vSem.setImpGanaAct(hVen.getImporteGanancia());
            }
            if (hVenAnt != null) {
                vSem.setFechaInicioAnt(hVenAnt.getHistVentasKey().getFechaInicio());
                vSem.setFechaFinalAnt(hVenAnt.getHistVentasKey().getFechaFinal());
                vSem.setKilosVentaAnt(hVenAnt.getKilosVenta());
                vSem.setImpVentaAnt(hVenAnt.getImporteVenta());
                vSem.setImpGanaAnt(hVenAnt.getImporteGanancia());
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

    /**
     * Añade semana con los kilos sacados del Repositorio (histVentasRepository)
     *
     * @param fecIni
     * @param fecFin
     * @return
     */
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
