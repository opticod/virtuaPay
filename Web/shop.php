<?php

//This is used for a shopkeeper to convert some client's notes into his own.
require('dbConnect.php') ;


$uid = null ;
$sumtransfer = -1 ;
$iter = array() ;

if( isset($_REQUEST['uid']) && !empty($_REQUEST['uid'])) {
	$uid = $_REQUEST['uid'] ;
	}
else { 
	header('Content-type: application/json');
	$arr = array();
	$arr['status'] = 'Failed';
	$arr['message'] = 'The id does not exist';
	echo json_encode($arr);
	die();
}

if( isset($_REQUEST['sumtransfer']) ) {
	$sumtransfer = intval($_REQUEST['sumtransfer']) ;
	}
else { 
	header('Content-type: application/json');
	$arr = array();
	$arr['status'] = 'Failed';
	$arr['message'] = 'The sumtransfer does not exist'.$_REQUEST['sumtransfer'];
	echo json_encode($arr);
	die();
}

try{
	$notes = $_REQUEST['notes'];
	$iter = json_decode($notes);
	if($iter == null || sizeof($iter)==0) {
		header('Content-type: application/json');
		$arr = array();
		$arr['status'] = 'Failed';
		$arr['message'] = 'No Notes given';
		echo json_encode($arr);
		die();
	}
}
catch(Exception $e) { 
	header('Content-type: application/json');
	$arr = array();
	$arr['status'] = 'Failed';
	$arr['message'] = 'The notes are not received correctly';
	echo json_encode($arr);
	die();
}




$conn = mysqli_connect(SERVER_ADDRESS,USER_NAME,PASSWORD,DATABASE);
if (mysqli_connect_errno())
  {
  echo "Failed to connect to MySQL: " . mysqli_connect_error();
  }
$querycodes = "'$iter[0]'";
for ($i=1; $i <sizeof($iter) ; $i++) { 
	$querycodes = $querycodes . ",'$iter[$i]'";
}
$sql = "SELECT sum(amount) from virtual_cash where code in ($querycodes) and isvalid = 1" ;
if(!($result = mysqli_query($conn,$sql) ) )
{
	echo("Error description:0 " . mysqli_error($conn));
	die() ;
}
if(mysqli_num_rows($result) == 1) {
	$calculatedsum = (int)mysqli_fetch_assoc($result)['sum(amount)'];

} else {
	echo("Error description:-1 " . mysqli_error($conn));
	die() ;
}
if($calculatedsum != $sumtransfer || $sumtransfer<=0) {
	header('Content-type: application/json');
	$arr = array();
	$arr['status'] = 'Failed';
	$arr['message'] = 'Failed due to sum transfer';
	echo json_encode($arr);
	die();
}

$sql = "UPDATE cash_holder ch inner join virtual_cash vc on ch.vcid=vc.vcid set uid = '$uid' where code in ($querycodes) and isvalid=1" ;

if(!($result = mysqli_query($conn,$sql) ) )
{
	echo("Error description:10 " . mysqli_error($conn));
	die() ;
}


mysqli_close($conn);
header('Content-type: application/json');
	$arr = array();
	$arr['status'] = 'Done';
	$arr['message'] = 'Received Rs.'.$sumtransfer;
	echo json_encode($arr);
	die();




?>







