Característica: Notificaciones de anuncios
  Como empleado
  Quiero recibir notificaciones de nuevos anuncios
  Para estar informado

  Escenario: Recibir notificación push de anuncio nuevo
    Dado que soy un empleado con la aplicación instalada
    Cuando un gerente publica un nuevo anuncio dirigido a mi departamento
    Entonces debo recibir una notificación push inmediatamente
    Y la notificación debe incluir el título del anuncio

  Escenario: Notificación de anuncio de alta prioridad
    Dado que soy un empleado activo en la empresa
    Cuando se publica un anuncio marcado como "Alta Prioridad"
    Entonces debo recibir una notificación especial con sonido distintivo
    Y la notificación debe indicar claramente que es de alta prioridad

  Escenario: Configurar preferencias de notificaciones de anuncios
    Dado que soy un empleado en la configuración de la app
    Cuando accedo a las preferencias de notificaciones
    Entonces debo poder activar/desactivar notificaciones por tipo de anuncio
    Y debo poder configurar horarios de no molestar

  Escenario: Abrir anuncio desde notificación
    Dado que soy un empleado que recibe una notificación de anuncio
    Cuando toco la notificación en mi dispositivo
    Entonces la aplicación debe abrirse directamente en el anuncio correspondiente
    Y el anuncio debe marcarse como visto

  Escenario: Historial de notificaciones de anuncios
    Dado que soy un empleado que ha recibido múltiples notificaciones
    Cuando accedo al centro de notificaciones de la app
    Entonces debo ver todas las notificaciones recientes de anuncios
    Y debo poder acceder a cada anuncio desde el historial