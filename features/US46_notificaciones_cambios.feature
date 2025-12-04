Característica: Notificaciones de cambios
  Como empleado
  Quiero recibir notificaciones cuando los eventos cambien
  Para estar siempre actualizado

  Escenario: Notificación de evento cancelado
    Dado que soy un empleado registrado para un evento
    Cuando el organizador cancela el evento
    Entonces debo recibir una notificación inmediata de cancelación
    Y la notificación debe incluir la razón de la cancelación

  Escenario: Notificación de cambio de horario de evento
    Dado que soy un empleado confirmado para una reunión
    Cuando el gerente cambia el horario del evento
    Entonces debo recibir notificación del nuevo horario
    Y debo poder confirmar si puedo asistir en el nuevo horario

  Escenario: Notificación de cambio de ubicación
    Dado que soy un empleado registrado para un evento presencial
    Cuando se cambia la ubicación del evento
    Entonces debo recibir notificación con la nueva ubicación
    Y debo poder ver la dirección actualizada en el mapa

  Escenario: Notificación de nueva información en evento
    Dado que soy un empleado inscrito en una capacitación
    Cuando el organizador agrega materiales o requisitos nuevos
    Entonces debo recibir notificación de la información actualizada
    Y debo poder acceder directamente a los nuevos materiales

  Escenario: Configurar frecuencia de notificaciones de cambios
    Dado que soy un empleado en configuración de notificaciones
    Cuando ajusto las preferencias para cambios de eventos
    Entonces debo poder elegir recibir notificaciones inmediatas o resúmenes diarios
    Y la configuración debe aplicarse a todos los eventos futuros