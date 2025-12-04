Característica: Segmentación de anuncios por departamento
  Como gerente
  Quiero dirigir anuncios a departamentos específicos
  Para que la información relevante llegue solo a quienes corresponda

  Escenario: Crear anuncio dirigido a un departamento específico
    Dado que soy un gerente autenticado
    Cuando creo un nuevo anuncio sobre "Nuevas políticas de IT"
    Y selecciono solo el departamento "Tecnología"
    Entonces el anuncio debe ser visible únicamente para empleados de IT
    Y no debe aparecer en otros departamentos

  Escenario: Anuncio dirigido a múltiples departamentos
    Dado que soy un gerente creando un anuncio sobre "Capacitación de seguridad"
    Cuando selecciono los departamentos "Recursos Humanos" y "Administración"
    Entonces ambos departamentos deben recibir el anuncio
    Y empleados de otros departamentos no deben verlo

  Escenario: Anuncio general para toda la empresa
    Dado que soy un gerente creando un anuncio sobre "Reunión general"
    Cuando selecciono "Todos los departamentos"
    Entonces todos los empleados deben ver el anuncio
    Y debe aparecer en la lista principal de anuncios

  Escenario: Validación de permisos para segmentación
    Dado que soy un gerente con permisos limitados
    Cuando intento crear un anuncio para departamentos fuera de mi jurisdicción
    Entonces el sistema debe mostrar un error de permisos
    Y solo debo poder seleccionar departamentos bajo mi gestión