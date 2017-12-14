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
    
    $cantidad = $_REQUEST['cantidad'];
    $producto = $_REQUEST['producto'];
    $matricula = $_REQUEST['matricula'];
    if(isset($cantidad, $producto, $matricula)){
        $datos = array();
        
        $sql = "SELECT idProducto from producto where codigo = '$producto'";
        if(!$result = $db->query($sql)){
            die('{"err":' . $db->error . '}');
        }else{
            while($row = $result->fetch_assoc()){
                $id = $row['idProducto'];
            }
            $sql ="SELECT idUsuario from usuario where matricula = '$matricula'";
            if(!$result = $db->query($sql)){
                die('{"err":' . $db->error . '}');
            }else{
                while($row = $result->fetch_assoc()){
                    $idUsuario = $row['idUsuario'];
                }
            }

            $result->free();
            for($i = 0; $i<$cantidad;$i++){
                $idRenta = 0;
                $sql = "SELECT idRenta from renta where fkProducto = '$id' and fkUsuario = '$idUsuario' and fechaRetorno is null limit 1";
                if(!$result = $db->query($sql)){
                    die('{"err":' . $db->error . '}');
                }else{
                    while($row = $result->fetch_assoc()){
                        $idRenta = $row['idRenta'];
                    }
                    echo $id, $idUsuario, $idRenta;
                    $sql = "UPDATE renta set fechaRetorno = NOW() where idRenta = '$idRenta'";
                    if(!$result = $db->query($sql)){
                        die('{"err":' . $db->error . '}');
                    }
                }
            }
           
        }
    }
?>