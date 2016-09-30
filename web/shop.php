<?php

//This is used for a shopkeeper to convert some client's notes into his own.
require('dbConnect.php') ;


$uid = null ;
$iter = array() ;

if( isset($_REQUEST['uid']) && !empty($_REQUEST['uid'])) {
	$uid = $_REQUEST['uid'] ;
	}
else { 
	header('Content-type: application/json');
	$arr = array();
	$arr[] = -1;
	$arr[] = 'The id does not exist';
	echo json_encode($arr);
	die();
}

if( isset($_REQUEST['notes']) && !empty($_REQUEST['notes'])) {
	echo $_REQUEST['notes']."<br>";
	$iter = json_decode($_REQUEST['notes'], true);
	echo sizeof($iter);
	}
else { 
	header('Content-type: application/json');
	$arr = array();
	$arr[] = -1;
	$arr[] = 'The notes are not received correctly';
	echo json_encode($arr);
	die();
}




$conn = mysqli_connect(SERVER_ADDRESS,USER_NAME,PASSWORD,DATABASE);
if (mysqli_connect_errno())
  {
  echo "Failed to connect to MySQL: " . mysqli_connect_error();
  }


foreach($iter as $code)
{
$sql = "SELECT * from virtual_cash where code = '$code'" ;

if(!($result = mysqli_query($conn,$sql) ) )
{
	echo("Error description:1 " . mysqli_error($conn));
	die() ;
}

$rowcount = mysqli_num_rows($result) ;
if( $rowcount == 0 ) { 
	header('Content-type: application/json');
	$arr = array();
	$arr[] = -1;
	$arr[] = 'Invalid Note';
	echo json_encode($arr);
	die();
	mysqli_free_result($result) ;
}
$inf = mysqli_fetch_assoc($result) ;
$vid = $inf["vcid"] ;
if( $inf["isvalid"] == 0 ) {
header('Content-type: application/json');
	$arr = array();
	$arr[] = -1;
	$arr[] = 'This note has been used';
	echo json_encode($arr);
	die();
	mysqli_free_result($result) ;
}



$sql = "UPDATE cash_holder set uid = '$uid' where vcid = '$vid'" ;

if(!($result = mysqli_query($conn,$sql) ) )
{
	echo("Error description:1 " . mysqli_error($conn));
	die() ;
}



$sql = "UPDATE virtual_cash set isvalid = 0 where vcid = '$vid'" ;

if(!($result = mysqli_query($conn,$sql) ) )
{
	echo("Error description:1 " . mysqli_error($conn));
	die() ;
}


}


mysqli_close($conn);
header('Content-type: application/json');






?>







