Característica: Marcar anuncios como leídos
  Como empleado
  Quiero marcar anuncios como leídos
  Para llevar un control de la información revisada

  Escenario: Marcar anuncio como leído exitosamente
    Dado que soy un empleado autenticado
    Cuando leo un anuncio sobre "Nueva política de vacaciones"
    Y hago clic en "Marcar como leído"
    Entonces el anuncio debe marcarse visualmente como leído
    Y debe registrarse la fecha y hora de lectura

  Escenario: Ver estado de lectura en lista de anuncios
    Dado que soy un empleado con varios anuncios disponibles
    Cuando veo mi lista de anuncios
    Entonces debo ver claramente cuáles he marcado como leídos
    Y cuáles siguen pendientes por leer

  Escenario: Marcar múltiples anuncios como leídos
    Dado que soy un empleado con varios anuncios pendientes
    Cuando selecciono múltiples anuncios
    Y uso la opción "Marcar todos como leídos"
    Entonces todos los anuncios seleccionados deben marcarse como leídos
    Y debo ver confirmación de la acción

  Escenario: Desmarcar anuncio como no leído
    Dado que soy un empleado que marcó un anuncio como leído por error
    Cuando accedo al anuncio marcado
    Y selecciono "Marcar como no leído"
    Entonces el anuncio debe volver al estado de pendiente
    Y debe eliminarse el registro de fecha de lectura

  Escenario: Recordatorio de anuncios no leídos
    Dado que soy un empleado con anuncios importantes sin leer
    Cuando han pasado 48 horas desde la publicación
    Entonces debo recibir una notificación recordatoria
    Y debo poder acceder directamente al anuncio desde la notificación