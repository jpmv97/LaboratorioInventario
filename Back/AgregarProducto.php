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
	$descripcion = $_REQUEST['descripcion'];
	$cantidad = $_REQUEST['cantidad'];
	$codigo = $_REQUEST['codigo'];
	if(isset($nombre, $descripcion, $cantidad, $codigo)){
		$datos = array();
		$sql = "INSERT INTO producto VALUES(null, '$nombre', '$descripcion', '$cantidad', $codigo)";
		if(!$result = $db->query($sql)){
			die('{"err":' . $db->error . '}');
		}else{
			//echo json_encode($matricula, JSON_UNESCAPED_UNICODE);
		}
	}
?>