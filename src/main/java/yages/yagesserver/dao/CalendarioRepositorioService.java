package yages.yagesserver.dao;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import yages.yagesserver.model.Calendario;
import yages.yagesserver.model.CalendarioKey;

/**
 *
 * @author chuchip
 */
@Service
@Transactional
public class CalendarioRepositorioService {
    
    @Autowired
    CalendarioRepository calendarioRepository;
    
    public Optional<Calendario>  getCalendario(CalendarioKey calendarioKey)
    {
        return calendarioRepository.findById(calendarioKey);        
    }
    public Iterable<Calendario>  getAllCalendarios()
    {
        return calendarioRepository.findAll();
    }
    
}
