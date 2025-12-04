Característica: Representación de navegación y acciones basada en roles
  Como desarrollador
  Quiero ver los elementos de navegación según los roles autenticados
  Para que los empleados solo vean las secciones a las que están autorizados a acceder

  Escenario: Navegación de empleado básico
    Dado que soy un empleado con rol "USER"
    Cuando accedo al menú principal de la aplicación
    Entonces debo ver solo opciones de "Anuncios", "Eventos" y "Mi Perfil"
    Y no debo ver opciones administrativas como "Gestión de Usuarios"

  Escenario: Navegación de gerente
    Dado que soy un empleado con rol "MANAGER"
    Cuando accedo al menú principal
    Entonces debo ver opciones adicionales como "Crear Anuncios" y "Gestión de Eventos"
    Y debo poder acceder a dashboards de reportes
    Y debo mantener acceso a funciones de empleado básico

  Escenario: Navegación de administrador
    Dado que soy un empleado con rol "ADMIN"
    Cuando navego por la aplicación
    Entonces debo ver todas las opciones disponibles incluyendo "Gestión de Usuarios"
    Y debo poder acceder a configuración del sistema
    Y debo ver opciones de auditoría y logs

  Escenario: Ocultación dinámica de botones por permisos
    Dado que soy un empleado sin permisos de eliminación
    Cuando veo la lista de anuncios
    Entonces no debo ver botones de "Eliminar" en ningún anuncio
    Y solo debo ver acciones permitidas como "Ver detalles"

  Escenario: Redirección por falta de permisos
    Dado que soy un empleado básico
    Cuando intento acceder directamente a una URL administrativa
    Entonces debo ser redirigido a mi dashboard correspondiente
    Y debo ver un mensaje informativo sobre permisos insuficientes