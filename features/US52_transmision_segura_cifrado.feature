Característica: Implementar la transmisión segura de datos y el cifrado
  Como desarrollador
  Quiero asegurarme de que todos los datos confidenciales estén cifrados en reposo y se transmitan de forma segura
  Para la seguridad de los datos

  Escenario: Cifrado en reposo de datos sensibles
    Dado que el sistema almacena información confidencial de empleados
    Cuando los datos se guardan en la base de datos
    Entonces toda información sensible debe estar cifrada con AES-256
    Y las claves de cifrado deben almacenarse separadamente
    Y debe usarse cifrado de campo específico para datos críticos

  Escenario: Transmisión segura con HTTPS
    Dado que un empleado realiza peticiones a la API
    Cuando se transmiten datos entre cliente y servidor
    Entonces toda comunicación debe usar HTTPS/TLS 1.3
    Y debe rechazar conexiones HTTP no seguras
    Y debe validar certificados SSL correctamente

  Escenario: Cifrado de extremo a extremo para mensajes
    Dado que empleados intercambian mensajes en la aplicación
    Cuando se envía un mensaje privado
    Entonces el mensaje debe cifrarse antes de salir del dispositivo emisor
    Y solo debe descifrarse en el dispositivo receptor
    Y el servidor no debe poder leer el contenido del mensaje

  Escenario: Gestión segura de claves de cifrado
    Dado que el sistema maneja múltiples claves de cifrado
    Cuando se requiere acceso a las claves
    Entonces las claves deben rotarse automáticamente cada 90 días
    Y deben almacenarse en un HSM o servicio seguro de gestión
    Y debe mantenerse historial cifrado para desencriptar datos antiguos

  Escenario: Validación de integridad de datos
    Dado que se transmiten datos importantes
    Cuando los datos viajan por la red
    Entonces debe incluirse hash criptográfico para validar integridad
    Y debe detectarse cualquier modificación durante el tránsito
    Y debe rechazar datos que no pasen validación de integridad