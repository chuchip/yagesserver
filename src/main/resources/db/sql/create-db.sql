drop table calendario;
create table calendario
(
	cal_ano int,  -- Año
	cal_mes int,  -- Mes
	cal_fecini date,  -- Fecha Inicial
	cal_fecfin date,  -- Fecha Final.
	constraint ix_calendario primary  key (cal_ano,cal_mes)
);
drop table histventas;
create table histventas
(
    hve_fecini date not null, -- Fecha Inicial
    hve_fecfin date not null, -- Fecha Final
    div_codi int not null , -- Divisa
    hve_kilven float,        -- Kilos Venta (Total)
    hve_impven float,        -- Importe Ventas (Total)
    hve_impgan float,         -- Importe ganancia  
    constraint ix_histventas primary key (hve_fecini ,hve_fecfin,div_codi  )
);
