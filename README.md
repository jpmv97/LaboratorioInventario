# LaboratorioInventario
Esta es una aplicacion creada en Android Studio para el manejo del inventario de los laboratorios que hay en la escuela. Se usa una base de datos MySQL, php y java para hacer el manejo de los datos y poder llevar el inventario de una manera correecta. 

En la ventana principal tenemos 5 opciones, las cuales revisaremos una por una:

Crear usuario:
	Se introduce el nombre y la matricula de un alumno, el correo se autogenera usando su matricula para el correo institucional.

Rentar: 
	Al ingresar la matricula de un usuario(ya tiene que estar registrado) es posible rentar uno o varios objetos.
	Para rentar varios objetos aun sean los mismos, es necesario ya sea escanear el codigo de barras o agregar manualmente el codigo.
	Una vez hecho esto se presiona agregar para terminar y la renta queda activa.

Consultar:
	En esta seccion se despliega una lista de las rentas activas.

Devolver:
	En esta seccion se pueden devolver los articulos rentados, se introduce la matricula del alumno, el codigo del objeto y cuantos unidades de ese objeto se van a regresar.

Agregar producto:
	En esta seccion se agrega un producto nuevo a la base de datos. 
