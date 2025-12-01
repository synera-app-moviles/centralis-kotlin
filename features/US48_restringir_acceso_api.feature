Característica: Restringir el acceso a la API
  Como desarrollador
  Quiero proteger las rutas de la API mediante control de acceso basado en roles y protecciones de middleware
  Para bloquear el acceso no autorizado

  Escenario: Acceso denegado sin token de autenticación
    Dado que un usuario no autenticado intenta acceder a la API
    Cuando realiza una petición a "/api/announcements" sin token
    Entonces debe recibir error 401 Unauthorized
    Y no debe acceder a ningún dato sensible

  Escenario: Acceso con token válido de empleado
    Dado que un empleado tiene un token JWT válido
    Cuando realiza una petición a endpoints de empleado
    Entonces debe acceder exitosamente a sus datos permitidos
    Y no debe ver datos de otros usuarios

  Escenario: Restricción de acceso por rol de gerente
    Dado que un empleado común intenta acceder a funciones de gerente
    Cuando realiza petición a "/api/admin/users" con token de empleado
    Entonces debe recibir error 403 Forbidden
    Y debe recibir mensaje de permisos insuficientes

  Escenario: Middleware de validación de permisos
    Dado que existe middleware de validación de roles
    Cuando cualquier petición llega a endpoints protegidos
    Entonces el middleware debe validar el token JWT
    Y debe verificar que el rol coincida con los permisos requeridos
    Y debe bloquear accesos no autorizados antes de llegar al controlador