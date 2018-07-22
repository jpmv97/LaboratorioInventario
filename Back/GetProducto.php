
<?php
$db = new mysqli("localhost", "root", "", "laboratorios");
$db->set_charset("utf8");
$datos = array();
$sql = "SELECT SELECT nombre, cantidad, existencia from producto";
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