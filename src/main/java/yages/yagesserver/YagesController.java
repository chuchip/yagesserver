/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yages.yagesserver;


import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import yages.yagesserver.beans.VentasAnoBean;
import yages.yagesserver.beans.VentasSemanaBean;
import yages.yagesserver.bussines.YagesBussines;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.view.ContentNegotiatingViewResolver;

@RestController
@RequestMapping("/rest")
public class YagesController {

	@Autowired
	private YagesBussines yagesBussines;

     
//	@CrossOrigin(origins = "http://localhost:80")
	@RequestMapping(value = "/{anoId}", method=RequestMethod.GET,
            produces="application/json")
	public VentasAnoBean getAno(@PathVariable int anoId) {
		System.out.println("getAno.Año: "+anoId);
		return yagesBussines.getVentasAno(anoId);
	}
	
//	@CrossOrigin(origins = "http://localhost:80")
	@RequestMapping(value ="/{anoId}/{mesId}", method=RequestMethod.GET,produces="application/json")
	public ArrayList<VentasSemanaBean> getSemanas(@PathVariable int anoId,@PathVariable int mesId)	
	{
		 System.out.println("GetSemanas. Mes "+mesId+" Año: "+anoId);
		 return yagesBussines.getDatosSemana(mesId, anoId);
	}
}
