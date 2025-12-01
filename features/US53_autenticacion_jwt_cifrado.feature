Característica: Implementar autenticación segura con JWT y cifrado de contraseñas
  Como desarrollador
  Quiero implementar un endpoint de autenticación y emitir JWT firmados con gestión de expiración
  Para que se puedan establecer sesiones de empleados seguras

  Escenario: Autenticación exitosa con JWT válido
    Dado que un empleado proporciona credenciales válidas
    Cuando hace login con email y contraseña correctos
    Entonces el sistema debe generar un JWT firmado con RS256
    Y el token debe incluir claims de usuario, rol y expiración
    Y debe devolver también un refresh token seguro

  Escenario: Validación de contraseña con hash seguro
    Dado que un empleado intenta hacer login
    Cuando ingresa su contraseña
    Entonces el sistema debe verificar contra hash bcrypt almacenado
    Y debe usar salt único para cada contraseña
    Y debe rechazar contraseñas que no coincidan

  Escenario: Expiración y renovación de tokens JWT
    Dado que un empleado tiene un JWT válido próximo a expirar
    Cuando el token tiene menos de 15 minutos de validez
    Entonces el sistema debe permitir renovación automática con refresh token
    Y debe generar nuevo access token con nueva expiración
    Y debe mantener la sesión activa sin interrupciones

  Escenario: Rechazo de credenciales inválidas
    Dado que un usuario proporciona credenciales incorrectas
    Cuando intenta hacer login con email o contraseña erróneos
    Entonces debe recibir error 401 con mensaje genérico
    Y no debe revelar si el email existe o no
    Y debe implementar rate limiting para prevenir ataques de fuerza bruta

  Escenario: Validación de firma JWT en peticiones
    Dado que un empleado envía peticiones con JWT
    Cuando el servidor recibe el token en las peticiones
    Entonces debe validar la firma del token con clave pública
    Y debe verificar que no haya expirado
    Y debe rechazar tokens con firma inválida o manipulados