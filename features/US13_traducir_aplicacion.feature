Característica: Traducir la aplicación móvil
  Como empleado
  Quiero elegir mi idioma preferido en la interfaz de usuario
  Para poder interactuar con la plataforma en mi idioma nativo

  Escenario: Cambiar idioma a español
    Dado que soy un empleado autenticado
    Cuando accedo a la configuración de idioma
    Y selecciono "Español" como idioma preferido
    Entonces toda la interfaz debe mostrarse en español
    Y la configuración debe guardarse para futuras sesiones

  Escenario: Cambiar idioma a inglés
    Dado que soy un empleado autenticado
    Cuando accedo a la configuración de idioma
    Y selecciono "English" como idioma preferido
    Entonces toda la interfaz debe mostrarse en inglés
    Y los mensajes de error también deben estar en inglés

  Escenario: Persistencia de idioma seleccionado
    Dado que soy un empleado que ha configurado el idioma a francés
    Cuando cierro la aplicación y la vuelvo a abrir
    Entonces la aplicación debe mantener el francés como idioma activo
    Y no debe resetear a un idioma por defecto

  Escenario: Cambio de idioma en tiempo real
    Dado que soy un empleado navegando por la aplicación
    Cuando cambio el idioma desde cualquier pantalla
    Entonces el cambio debe aplicarse inmediatamente sin necesidad de reiniciar
    Y todas las pantallas abiertas deben reflejar el nuevo idioma