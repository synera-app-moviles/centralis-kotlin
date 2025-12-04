Característica: Vincular el selector de idioma al módulo i18n y activar el cambio de idioma
  Como desarrollador
  Quiero vincular un selector de idioma desplegable al módulo i18n
  Y activar la función dinámicamente

  Escenario: Integración del selector con el módulo i18n
    Dado que el módulo i18n está configurado correctamente
    Cuando un usuario selecciona un idioma del selector desplegable
    Entonces el módulo i18n debe recibir la señal de cambio
    Y debe cargar los archivos de traducción correspondientes

  Escenario: Carga dinámica de archivos de traducción
    Dado que un empleado cambia el idioma de español a alemán
    Cuando se activa el cambio de idioma
    Entonces el sistema debe cargar dinámicamente el archivo de.json
    Y debe aplicar las traducciones sin reiniciar la aplicación

  Escenario: Fallback para idiomas no disponibles
    Dado que un empleado selecciona un idioma no soportado
    Cuando el sistema intenta cargar las traducciones
    Entonces debe usar el idioma inglés como fallback
    Y debe notificar al usuario sobre la limitación

  Escenario: Validación de archivos de traducción completos
    Dado que se está cambiando a un nuevo idioma
    Cuando el sistema carga el archivo de traducción
    Entonces debe validar que todas las claves requeridas estén presentes
    Y debe mostrar claves faltantes en el idioma fallback

  Escenario: Performance del cambio de idioma
    Dado que un empleado frecuentemente cambia entre idiomas
    Cuando se realiza un cambio de idioma
    Entonces el cambio debe completarse en menos de 2 segundos
    Y la interfaz no debe mostrar elementos sin traducir