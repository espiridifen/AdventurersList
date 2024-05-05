
### Commit subido a la VM
https://github.com/espiridifen/AdventurersList/commit/833bfb7f9d42011c2543864023011bcad3fb453e

### URL de la VM
https://vm032.containers.fdi.ucm.es/

### Cambios realizados y pendientes

Hemos realizado todos los cambios que nos habías recomendado, entre ellos:

* Reportes: el admin pueda meterse al perfil de un user y ver algún historial o algo (así pueden decidir si banearlo)
* Partidas: que se puedan agendar las próximas sesiones, y que los participantes puedan decir si pueden ir o no
* Agregar filtros a la búsqueda
* Que los campos al crear una campaña sean fijos, por ejemplo, que el nivel de experiencia sea un selector o un slider, así luego podemos filtrar sobre eso

Ahora, lo que nos queda pendiente:

* Botón de eliminar partida (sencillo)
* Decidir cómo queremos manejar el tema de asistencia a sesiones, ya que actualmente solo puedes confirmar o denegar asistencia a las sesiones creadas LUEGO de que te hayas unido a la partida. Aceptamos sugerencias.
* Más testing sobre lo que ya tenemos.
* Podríamos implementar reports a mensajes particulares enviados en una partida.

### Nota importante

No hemos logrado montar la BBDD MySQL en la VM ya que no tiene Docker instalado, por lo que por ahora está utilizando H2. De todas formas, todo está montado, y en nuestros entornos locales sí estamos usando Docker (se pueden ver los archivos Dockerfile y docker-compose).