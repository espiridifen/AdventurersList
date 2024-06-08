# Información importante
Respecto al trabajo de cada uno de nosotros, hemos trabajado los dos en realizar todas las mejoras que se listan a continuación. Nuestros nombres son Santiago y Daniel.

Esta es la URL del repo: https://github.com/espiridifen/AdventurersList

Como gran resumen de los puntos de abajo: hemos implementado todo lo que nos recomendó en el examen para llegar al notable, y mucho más aún.

Para logearse:
 - Username: admin. Password: aa
 - Username: user. Password: aa

# Mejoras realizadas

- Ahora se pueden editar las partidas y las sesiones (incluyendo horarios, imágenes, etc). Solo el Owner del game puede realizar esta acción.
- Las sesiones ahora se listan según si ya han ocurrido o si son próximas. Las próximas son editables por el Owner mientras que las previas no (lo cual también se verifica en los GET/POST mappings). Están ordenadas según fecha, para que sea más intuitivo de ver. Además, las sesiones previas ahora muestran las personas que han asistido a ella.
- Ahora hay una cabecera unificada para todas las páginas.
- En la página de partida, la fuente se ha hecho más clara y ahora hay mejor fondo para la siguiente sesión. Se han añadido también los cambios a los websockets del examen. A mayores, se ha renovado la organización de la página, y ahora el chat es scrolleable y está a la derecha del todo, siendo mucho más práctico de utilizar.
- Ahora hay iconos para los jugadores que todavía no hayan decidido si atenderán o no a la próxima sesión.
- Ahora se pueden visualizar bien los perfiles de los otros usuarios, y si eres admin, se pueden ver los mensajes del usuario. Además, si no es tu perfil, no aparece la opción de cambiar tu imágen de perfil.
- En un mensaje, al hacer click en la imagen de perfil del usuario, te lleva a su perfil.
- Ahora los admins pueden banear usuarios, como también borrar mensajes de los usuarios (al entrar a su perfil, un admin puede eliminar sus mensajes). No te puedes banear a ti mismo. Una vez baneado, si el usuario intenta seguir usando la aplicación, un filtro de seguridad le echará. Si intenta volver a entrar, no podrá.
- Se han hecho mejoras de visibilidad en toda la página, y se ha creado un logo para la aplicación
- Ahora los admins pueden ver los reports. Los reports se pueden marcar como resueltos, y aparecen en una lista por separado, para que sea más fácil de gestionar. Además, por comodidad, se ponen links directos a la partida reportada, como al perfil del usuario que ha realizado el reporte. Además, ahora se pueden visitar los perfiles de los usuarios al hacerles click en la lista que aparece a la izquierda en un game.
- Ahora puedes registrarte a la página, es decir, crear una cuenta nueva. De paso, removimos los botones de auto-login que usamos para debug.
- Establecimos estilos generales para toda la página. Ahora no hay páginas que desentonan totalmente. También hemos intentado mejorar un poco el aspecto visual, que era bastante terrible.


Cambios realizados en el examen y modificaciones al respecto:
- En el examen, nos pidió que pongamos un background gris claro al texto de las partidas. Lo habíamos implementado exitosamente, pero decidimos ahora darle otra vuelta al asunto, y logramos que se vea bien, pero sin el fondo gris.
- Enter para enviar mensajes al chat.
- Las sesiones pueden tener un enlace. Este campo es solo opcional. Cuando se deja vacío, no aparece un campo vacío al mostrar la sesión, pero si se agrega una URL, aparece "URL: algunaurl.com", así queda más bonito.
- Se muestra en la pantalla de búsqueda la cantidad de sesiones que cumplen la condición.
- Los filtros actualizan en tiempo real los resultados de la búsqueda.
- Ahora se pueden borrar partidas que tienen reports.