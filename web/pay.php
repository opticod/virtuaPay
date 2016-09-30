<?php

//This is used for a user to convert his physical money to virtual money
require('dbConnect.php') ;

$uid = 0 ;
$amnt = 0 ;



if( isset($_REQUEST['uid']) && !empty($_REQUEST['uid'])) {
	$uid = $_REQUEST['uid'] ;
	}
else { 
	header('Content-type: application/json');
	$arr = array();
	$arr[] = -1;
	$arr[] = 'Missing parameter in the request : user id';
	echo json_encode($arr);
	die();
}

if( isset($_REQUEST['amnt']) && !empty($_REQUEST['amnt']) && is_numeric($_REQUEST['amnt']) ) {
	$amnt = $_REQUEST['amnt'] ;
	}
else {
	header('Content-type: application/json');
	$arr = array();
	$arr[] = -1;
	$arr[] = 'Amount sent was not an integer.';
	echo json_encode($arr);
	die();
}

if( $amnt % 10 != 0 ) 
{
	header('Content-type: application/json');
	$arr = array();
	$arr[] = -1;
	$arr[] = 'Amount is not a multiple of 10!';
	echo json_encode($arr);
	die();
}


$conn = mysqli_connect(SERVER_ADDRESS,USER_NAME,PASSWORD,DATABASE);
if (mysqli_connect_errno())
  {
  echo "Failed to connect to MySQL: " . mysqli_connect_error();
  }


$org = $amnt ;
while( $amnt > 0 )
{
	$cost = 10 ;
	$code = sha1(base64_encode(openssl_random_pseudo_bytes(100))) ;
	$amnt -= $cost ;
	$sql = "INSERT into virtual_cash(amount,code) values('$cost','$code')" ;
	if(!($result = mysqli_query($conn,$sql) ) )
{
	echo("Error description:1 " . mysqli_error($conn));
	die() ;
}

   
    $sql = "SELECT * from virtual_cash where code = '$code'" ;
    if(!($result = mysqli_query($conn,$sql) ) )
{
	echo("Error description:1 " . mysqli_error($conn));
	die() ;
}
    
    $inf = mysqli_fetch_assoc($result) ;
   // mysqli_free_result($result) ;
    $vcid = $inf['vcid'] ;
    
    $sql = "INSERT into cash_holder values($uid,$vcid)" ;
    if(!($result = mysqli_query($conn,$sql) ) )
  {
	echo("Error description:1 " . mysqli_error($conn));
	die() ;
  }    
    
}


mysqli_close($conn);
?>







