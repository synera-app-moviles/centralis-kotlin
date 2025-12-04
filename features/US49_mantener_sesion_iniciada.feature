Característica: Mantener la sesión iniciada
  Como empleado
  Quiero mantener la sesión iniciada de forma segura en todas las sesiones
  Para no tener que volver a autenticarme con frecuencia al volver a la aplicación

  Escenario: Sesión persistente después de cerrar app
    Dado que soy un empleado autenticado
    Cuando cierro la aplicación y la vuelvo a abrir después de 30 minutos
    Entonces debo seguir autenticado automáticamente
    Y no debo necesitar ingresar credenciales nuevamente

  Escenario: Renovación automática de token
    Dado que soy un empleado con sesión activa por varias horas
    Cuando mi token JWT está próximo a expirar
    Entonces el sistema debe renovarlo automáticamente en segundo plano
    Y no debo notar interrupciones en mi experiencia

  Escenario: Expiración de sesión por inactividad prolongada
    Dado que soy un empleado autenticado pero inactivo
    Cuando no uso la aplicación por más de 7 días
    Entonces mi sesión debe expirar por seguridad
    Y debo autenticarme nuevamente al volver

  Escenario: Sesión segura en múltiples dispositivos
    Dado que soy un empleado con sesión en teléfono y tablet
    Cuando inicio sesión en un dispositivo nuevo
    Entonces debo mantener sesiones activas en todos mis dispositivos autorizados
    Y cada dispositivo debe tener su propio token de sesión

  Escenario: Configuración de duración de sesión
    Dado que soy un empleado en configuración de seguridad
    Cuando accedo a preferencias de sesión
    Entonces debo poder elegir duración de sesión automática
    Y debo poder configurar opciones desde 1 día hasta 30 días