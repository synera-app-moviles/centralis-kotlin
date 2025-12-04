Característica: Confirmaciones de lectura
  Como gerente
  Quiero ver confirmaciones de lectura de anuncios
  Para saber quién ha leído la información importante

  Escenario: Ver lista de empleados que han leído el anuncio
    Dado que soy un gerente que ha publicado un anuncio importante
    Cuando accedo a los detalles del anuncio
    Entonces debo ver una lista de empleados que lo han marcado como leído
    Y debo ver la fecha y hora en que cada uno lo leyó

  Escenario: Ver estadísticas de lectura del anuncio
    Dado que soy un gerente revisando un anuncio publicado hace 2 días
    Cuando veo las estadísticas de lectura
    Entonces debo ver el porcentaje de empleados que lo han leído
    Y debo ver cuántos empleados aún no lo han leído

  Escenario: Identificar empleados que no han leído anuncios críticos
    Dado que soy un gerente con un anuncio marcado como "Crítico"
    Cuando reviso las confirmaciones de lectura después de 24 horas
    Entonces debo ver claramente qué empleados no lo han leído
    Y debo poder enviar recordatorios específicos

  Escenario: Exportar reporte de confirmaciones de lectura
    Dado que soy un gerente que necesita documentar el cumplimiento
    Cuando solicito un reporte de confirmaciones de lectura
    Entonces debo poder exportar un archivo con todos los datos de lectura
    Y el archivo debe incluir nombres, departamentos y fechas de lectura