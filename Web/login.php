<?php

//This is for USER's LOGIN
require('dbConnect.php') ;

$email = null ;
$pwd = null ;
$type = 0 ;

if( isset($_REQUEST['email']) && !empty($_REQUEST['email'])) {
	$email = $_REQUEST['email'] ;
	}
else { 
	header('Content-type: application/json');
	$arr = array();
	$arr['status'] = 'Error';
	$arr['message'] = 'Missing parameter in the request : email';
	echo json_encode($arr);
	die();
}


if( isset($_REQUEST['pwd']) && !empty($_REQUEST['pwd'])) {
	$pwd = sha1($_REQUEST['pwd']) ;
	}
else {
	header('Content-type: application/json');
	$arr = array();
	$arr['status'] = 'Error';
	$arr['message'] = 'Missing parameter in the request : pwd';
	echo json_encode($arr);
	die();
}


$conn = mysqli_connect(SERVER_ADDRESS,USER_NAME,PASSWORD,DATABASE);
if (mysqli_connect_errno())
  {
  echo "Failed to connect to MySQL: " . mysqli_connect_error();
  }

$sql = "SELECT * from user_info where email_id = '$email' and password = '$pwd'" ;
if(!($result = mysqli_query($conn,$sql) ) )
{
	echo("Error description:1 " . mysqli_error($conn));
	die() ;
}

$rowcount = mysqli_num_rows($result) ;
if( $rowcount== 0 ){
	header('Content-type: application/json');
	$arr = array();
	$arr['status'] = 'Error';
	$arr['message'] = 'No such user';
	echo json_encode($arr);
	die();
	mysqli_free_result($result) ;
} 

$inf = mysqli_fetch_assoc($result) ;
$uid = $inf['uid'];
$name = $inf['name'];
$key = sha1(base64_encode(openssl_random_pseudo_bytes(100))) ;

$sql = "UPDATE user_info set mykey = '$key' where uid = '$uid'" ;

if(mysqli_query($conn,$sql)) { } 
else echo("Error description:2 " . mysqli_error($conn));

//mysqli_free_result($res) ;

mysqli_close($conn);
header('Content-type: application/json');

$finresult = array() ;
$finresult['status'] = 'Login' ;
$finresult['uid'] = $uid ;
$finresult['key'] = $key ;
$finresult['email'] = $email ;
$finresult['name'] = $name ;
echo json_encode($finresult);




?>