<?php

//Used to sync user's amount, how much virtual money he currently has.

require('dbConnect.php') ;

$uid = 0 ;
$mykey = null ;
if( isset($_REQUEST['uid']) && !empty($_REQUEST['uid'])) {
	$uid = $_REQUEST['uid'] ;
	}
else { 
	header('Content-type: application/json');
	$arr = array();
	$arr[] = -1;
	$arr[] = 'Missing parameter in the request : uid';
	echo json_encode($arr);
	die();
}



if( isset($_REQUEST['mykey']) && !empty($_REQUEST['mykey'])) {
	$mykey = $_REQUEST['mykey'] ;
	}
else { 
	header('Content-type: application/json');
	$arr = array();
	$arr[] = -1;
	$arr[] = 'Missing parameter in the request : mykey';
	echo json_encode($arr);
	die();
}

$vcid = array() ;






$conn = mysqli_connect(SERVER_ADDRESS,USER_NAME,PASSWORD,DATABASE);
if (mysqli_connect_errno())
  {
  echo "Failed to connect to MySQL: " . mysqli_connect_error();
  }

$sql = "SELECT * from user_info where uid = '$uid' " ;
if(!($result = mysqli_query($conn,$sql) ) )
{
	echo("Error description:1 " . mysqli_error($conn));
	die() ;
}

$inf = mysqli_fetch_assoc($result) ;
if( strcmp($inf['mykey'],$mykey)) != 0 ) {
	header('Content-type: application/json');
	$arr = array();
	$arr[] = -1;
	$arr[] = 'Sorry, your OTP has either expired or it is wrong.';
	echo json_encode($arr);
	die();
}



$sql = "SELECT * from cash_holder where uid = '$uid'" ;
$finresult = array() ;
if ($result=mysqli_query($conn,$sql))
  {
 while ($row=mysqli_fetch_row($result))
    {
 //   printf ("%s (%s)\n",$row[0],$row[1]);
    	$finresult[] = $row[1] ;
    }
}

$amnt = 0 ;
foreach($finresult as $arr)
{
	$sql = "SELECT * from virtual_cash where vcid = '$arr'" ;
	if(!($result = mysqli_query($conn,$sql) ) )
{
	echo("Error description:1 " . mysqli_error($conn));
	die() ;
}


    $inf = mysqli_fetch_assoc($result) ;
	if( $inf["isvalid"] == 1 ) $amnt = $amnt + $inf['amount'] ;
	mysqli_free_result($result) ;

}

$ans = array() ;
$ans = $amnt ;
echo json_encode($ans);


?>
