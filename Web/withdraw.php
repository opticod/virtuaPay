<?php

//This is used for user to convert his virtual money to physical money
require('dbConnect.php') ;
session_start();
if(!(isset($_SESSION['i_am_admin']) && $_SESSION['i_am_admin']))
{
	echo "NOT ADMIN";
	die();
}
$uid = 0 ;

if( isset($_REQUEST['uid']) && !empty($_REQUEST['uid'])) {
	$uid = $_REQUEST['uid'] ;
	}
else { 
	header('Content-type: application/json');
	$arr = array();
	$arr[] = -1;
	$arr[] = 'Missing parameter in the request : name';
	echo json_encode($arr);
	die();
}


$conn = mysqli_connect(SERVER_ADDRESS,USER_NAME,PASSWORD,DATABASE);
if (mysqli_connect_errno())
  {
  echo "Failed to connect to MySQL: " . mysqli_connect_error();
  }


$sql = "SELECT * from cash_holder where uid = '$uid'" ;
$res = mysqli_query($conn,$sql) ;

$amnt = 0 ;
$finresult = array() ;
if ($result=mysqli_query($conn,$sql))
  {
 while ($row=mysqli_fetch_row($result))
    {
 //   printf ("%s (%s)\n",$row[0],$row[1]);
    	$finresult[] = $row[1] ;
    }
}

$sql = "DELETE from cash_holder where uid = '$uid'" ;
 if(!($result = mysqli_query($conn,$sql) ) )
{
	echo("Error description:1 " . mysqli_error($conn));
	die() ;
}

foreach($finresult as $arr)
{
	$sql = "SELECT * from virtual_cash where vcid = '$arr'" ;
	if(!($result = mysqli_query($conn,$sql) ) )
{
	echo("Error description:1 " . mysqli_error($conn));
	die() ;
}
	$inf = mysqli_fetch_assoc($result) ;
	$amnt = $amnt + $inf['amount'] ;
	mysqli_free_result($result) ;

	$sql = "DELETE from virtual_cash where vcid = '$arr'" ;
	if(!($result = mysqli_query($conn,$sql) ) )
{
	echo("Error description:1 " . mysqli_error($conn));
	die() ;
}

}

$ans = array() ;
$ans = $amnt ;
echo json_encode($ans);


?>
