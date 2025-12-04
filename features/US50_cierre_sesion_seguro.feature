Característica: Funcionalidad de Cierre de Sesión Seguro
  Como empleado
  Quiero cerrar sesión de forma segura en la aplicación
  Para que mi sesión finalice por completo y no pueda ser reutilizada por terceros no autorizados

  Escenario: Cierre de sesión manual exitoso
    Dado que soy un empleado autenticado
    Cuando selecciono "Cerrar sesión" desde el menú
    Entonces debo ser redirigido a la pantalla de login
    Y mi token de sesión debe ser invalidado completamente
    Y debo confirmar que deseo cerrar sesión

  Escenario: Limpieza completa de datos de sesión
    Dado que soy un empleado cerrando sesión
    Cuando confirmo el cierre de sesión
    Entonces todos los datos locales deben eliminarse
    Y la caché de la aplicación debe limpiarse
    Y no deben quedar credenciales almacenadas temporalmente

  Escenario: Cierre de sesión en todos los dispositivos
    Dado que soy un empleado con sesión activa en múltiples dispositivos
    Cuando selecciono "Cerrar sesión en todos los dispositivos"
    Entonces todas mis sesiones activas deben invalidarse
    Y debo recibir confirmación de cierre exitoso en todos los dispositivos

  Escenario: Prevención de reutilización de tokens
    Dado que soy un empleado que cerró sesión
    Cuando alguien intenta usar mi token anterior
    Entonces el sistema debe rechazar todas las peticiones
    Y debe registrar el intento de acceso no autorizado
    Y no debe permitir ningún acceso a datos

  Escenario: Cierre automático por tiempo de inactividad
    Dado que soy un empleado autenticado pero inactivo por 2 horas
    Cuando el sistema detecta inactividad prolongada
    Entonces debe cerrar mi sesión automáticamente
    Y debe mostrar mensaje informativo al volver