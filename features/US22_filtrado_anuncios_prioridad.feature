Característica: Filtrado de anuncios por prioridad
  Como empleado
  Quiero filtrar anuncios por prioridad
  Para ver primero lo más importante

  Escenario: Filtrar anuncios por prioridad alta
    Dado que soy un empleado autenticado
    Cuando accedo a la lista de anuncios
    Y selecciono el filtro "Prioridad Alta"
    Entonces debo ver únicamente anuncios marcados como alta prioridad
    Y deben aparecer ordenados por fecha de publicación

  Escenario: Ver todos los anuncios por prioridad
    Dado que soy un empleado en la pantalla de anuncios
    Cuando no aplico ningún filtro de prioridad
    Entonces debo ver todos los anuncios ordenados primero por prioridad
    Y dentro de cada prioridad, ordenados por fecha más reciente

  Escenario: Combinar filtros de prioridad y departamento
    Dado que soy un empleado de IT
    Cuando filtro por "Prioridad Media" y "Departamento IT"
    Entonces debo ver solo anuncios de prioridad media dirigidos a IT
    Y no deben aparecer anuncios de otras prioridades o departamentos

  Escenario: Indicadores visuales de prioridad
    Dado que soy un empleado viendo la lista de anuncios
    Cuando reviso los anuncios mostrados
    Entonces cada anuncio debe tener un indicador visual claro de su prioridad
    Y los colores deben ser consistentes (rojo=alta, amarillo=media, verde=baja)

  Escenario: Contador de anuncios por prioridad
    Dado que soy un empleado en la pantalla de filtros
    Cuando veo las opciones de prioridad
    Entonces debo ver el número de anuncios disponibles en cada categoría
    Y los contadores deben actualizarse al aplicar otros filtros