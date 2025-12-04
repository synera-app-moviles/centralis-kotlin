Característica: Validar y almacenar datos cifrados de usuario al registrarse
  Como desarrollador
  Quiero validar los datos de registro entrantes y almacenar las credenciales cifradas de los empleados
  Para que las cuentas se creen de forma segura y estén protegidas contra accesos no autorizados

  Escenario: Registro exitoso con datos válidos
    Dado que un empleado intenta registrarse con datos válidos
    Cuando envía email "empleado@empresa.com" y contraseña segura
    Entonces el sistema debe validar el formato del email
    Y debe cifrar la contraseña antes de almacenarla
    Y debe crear la cuenta exitosamente

  Escenario: Validación de contraseña segura
    Dado que un empleado intenta registrarse
    Cuando ingresa una contraseña que no cumple requisitos de seguridad
    Entonces el sistema debe rechazar la contraseña
    Y debe mostrar los requisitos específicos no cumplidos
    Y no debe almacenar datos hasta cumplir todos los requisitos

  Escenario: Prevenir registro con email duplicado
    Dado que existe un empleado con email "juan@empresa.com"
    Cuando otro usuario intenta registrarse con el mismo email
    Entonces el sistema debe rechazar el registro
    Y debe mostrar mensaje de email ya existente
    Y no debe almacenar datos duplicados

  Escenario: Cifrado seguro de credenciales
    Dado que un empleado completa el registro correctamente
    Cuando el sistema almacena sus credenciales
    Entonces la contraseña debe estar cifrada con algoritmo seguro
    Y nunca debe almacenarse la contraseña en texto plano
    Y debe generarse un salt único para cada usuario