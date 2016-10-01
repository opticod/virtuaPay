<?php


//This is the code for USER REGISTRATION

require('dbConnect.php') ;

$name = null ;
$eid = null ;
$pwd = null ;
$key = null ;
$type = 0 ;

if( isset($_REQUEST['name']) && !empty($_REQUEST['name'])) {
	$name = $_REQUEST['name'] ;
	}
else { 
	header('Content-type: application/json');
	$arr = array();
	$arr[] = -1;
	$arr[] = 'Missing parameter in the request : name';
	echo json_encode($arr);
	die();
}

if( isset($_REQUEST['email']) && !empty($_REQUEST['email'])) {
	$eid = $_REQUEST['email'] ;
if (filter_var($eid, FILTER_VALIDATE_EMAIL) === false) {
  echo("$eid is not a valid email address");
  die() ;
}

	}
else {
	header('Content-type: application/json');
	$arr = array();
	$arr[] = -1;
	$arr[] = 'Missing parameter in the request : email';
	echo json_encode($arr);
	die();
}

if( isset($_REQUEST['pwd']) && !empty($_REQUEST['pwd'])) {
	$pwd = sha1($_REQUEST['pwd']) ;
	}
else {
	header('Content-type: application/json');
	$arr = array();
	$arr[] = -1;
	$arr[] = 'Missing parameter in the request : pwd';
	echo json_encode($arr);
	die();
}


$conn = mysqli_connect(SERVER_ADDRESS,USER_NAME,PASSWORD,DATABASE);
if (mysqli_connect_errno())
  {
  echo "Failed to connect to MySQL: " . mysqli_connect_error();
  }

$sql = "SELECT * from user_info where email_id = '$eid'" ;
if($result = mysqli_query($conn,$sql))
{
	$rowcount = mysqli_num_rows($result) ;
	if( $rowcount >= 1 ) { 
header('Content-type: application/json');
	$arr = array();
	$arr[] = -1;
	$arr[] = 'This email id has already registered';
	echo json_encode($arr);
	die();
	}
	mysqli_free_result($result) ;
}  

$sql = "INSERT INTO user_info(name,email_id,password,type) values('$name','$eid','$pwd',0)" ;
if(mysqli_query($conn,$sql)) { } 
else echo("Error description: " . mysqli_error($conn));
$sql = "SELECT * from user_info where email_id = '$eid'" ;
$uid = 0 ;
$res = mysqli_query($conn,$sql);

$fieldinfo=mysqli_fetch_assoc($res);
$uid = $fieldinfo["uid"] ;

mysqli_free_result($res) ;

mysqli_close($conn);
header('Content-type: application/json');

$finresult = array() ;
$finresult['status'] = true ;
echo json_encode($finresult);
?>







