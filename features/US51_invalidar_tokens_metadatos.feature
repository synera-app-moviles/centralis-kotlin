Característica: Invalidar tokens y borrar metadatos de sesión al cerrar sesión
  Como desarrollador
  Quiero implementar un mecanismo de cierre de sesión que invalide los tokens de actualización y borre los metadatos de sesión
  Para mejorar la seguridad

  Escenario: Invalidación de refresh tokens en cierre de sesión
    Dado que un empleado cierra sesión correctamente
    Cuando el sistema procesa la solicitud de logout
    Entonces debe invalidar inmediatamente el refresh token
    Y debe remover el token de la base de datos
    Y no debe permitir futuras renovaciones con tokens viejos

  Escenario: Borrado de metadatos de sesión del servidor
    Dado que un empleado termina su sesión
    Cuando se ejecuta el proceso de logout
    Entonces deben eliminarse todos los metadatos de sesión del servidor
    Y debe limpiarse información de dispositivo asociada
    Y deben removerse datos temporales de la sesión

  Escenario: Limpieza de tokens en logout de múltiples dispositivos
    Dado que un empleado solicita cerrar sesión en todos los dispositivos
    Cuando el sistema procesa la solicitud global
    Entonces debe invalidar todos los refresh tokens del usuario
    Y debe eliminar metadatos de todas las sesiones activas
    Y debe notificar a todos los dispositivos sobre la invalidación

  Escenario: Auditoría de invalidación de tokens
    Dado que se ejecuta un logout con invalidación de tokens
    Cuando el sistema completa el proceso
    Entonces debe registrar la invalidación en logs de auditoría
    Y debe incluir timestamp, usuario y dispositivo
    Y debe mantener registro para cumplimiento de seguridad

  Escenario: Manejo de errores en invalidación de tokens
    Dado que ocurre un error durante la invalidación de tokens
    Cuando el sistema no puede completar el proceso normalmente
    Entonces debe marcar los tokens como sospechosos
    Y debe alertar al equipo de seguridad
    Y debe prevenir el uso de tokens potencialmente comprometidos