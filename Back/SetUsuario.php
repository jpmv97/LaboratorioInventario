<?php
/*$conexion = mysql_connect("localhost","root","")
or die("No se pudo conectar a la BD");
mysql_select_db("laboratorios");
$sql = "SELECT fechaRenta FROM renta";
$datos = array();
$result = mysql_query($sql,$conexion);
while($row = mysql_fetch_object($result)){
$datos[] = $row;
}
echo json_encode($datos);
*/
	$db = new mysqli("localhost", "root", "", "laboratorios");
	$db->set_charset("utf8");
	$nombre = $_REQUEST['nombre'];
	$matricula = $_REQUEST['matricula'];
	$apellido = $_REQUEST['apellido'];
	$carrera = $_REQUEST['carrera'];

	if(isset($nombre, $matricula, $apellido, $carrera)){
		$datos = array();
		$sql = "CALL AgregarUsuario('$matricula', '$nombre', '$apellido', '$carrera')";
		if(!$result = $db->query($sql)){
			die('{"err":' . $db->error . '}');
		}
		
	}

/*DELIMITER //
CREATE PROCEDURE AgregarUsuario(matricul VARCHAR(50), nombr VARCHAR(50), apellid varchar(50), carrer VARCHAR(50))
	BEGIN
    	DECLARE aux INT;
        DECLARE upmatricula varchar(50);
        declare email varchar(50);
        SET aux = 0;
                
        SET upmatricula = UPPER(matricul);

        SELECT idUsuario into aux from usuario where matricula = upmatricula;
        IF aux > 0 THEN
        	SIGNAL SQLSTATE '45000'
            SET MESSAGE_TEXT = 'usuario ya existe';
        END IF;
        SET upmatricula = UPPER(matricul);

        set email = CONCAT(upmatricula, '@itesm.mx'); 

        INSERT into usuario (corre, matricula, nombre, apellido, carrera) VALUES (email, upmatricula, nombr, apellid, carrer);
        
        
    END //
DELIMITER ;
call AgregarUsuario('a00', 'prueba', 'prueba', 'prueba');
*/
?>
