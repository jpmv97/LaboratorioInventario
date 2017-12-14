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
$datos = array();
$sql = "SELECT fechaRenta, r.cantidad, p.nombre, u.nombre usuario from renta r, producto p, usuario u where r.fkProducto = p.idProducto and r.fkUsuario = u.idUsuario";
	if(!$result = $db->query($sql)){
		die('{"err":' . $db->error . '}');
	}else{
		while($row = $result->fetch_assoc()){
		    $datos[] = $row;
		}
		$result->free();
}
echo json_encode($datos, JSON_UNESCAPED_UNICODE);
?>