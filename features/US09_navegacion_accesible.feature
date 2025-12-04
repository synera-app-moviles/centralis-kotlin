Característica: Navegación Accesible
  Como visitante que utiliza tecnologías de asistencia
  Quiero que la landing page anuncie claramente las etiquetas de las secciones
  Para comprender su estructura y propósito

  Escenario: Navegación con lector de pantalla
    Dado que soy un visitante que usa tecnología de asistencia
    Cuando navego por la landing page con un lector de pantalla
    Entonces cada sección debe tener etiquetas aria-label apropiadas
    Y los encabezados deben estar jerarquizados correctamente

  Escenario: Navegación por teclado
    Dado que soy un visitante que usa navegación por teclado
    Cuando navego por la landing page usando solo el teclado
    Entonces todos los elementos interactivos deben ser accesibles con Tab
    Y debe haber indicadores visuales de foco claros

  Escenario: Alto contraste para usuarios con discapacidad visual
    Dado que soy un visitante con discapacidad visual
    Cuando activo el modo de alto contraste
    Entonces todos los elementos deben mantener legibilidad
    Y los colores deben cumplir con estándares WCAG 2.1