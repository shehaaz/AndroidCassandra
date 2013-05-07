<?php
 
/*
 * Following code will get single product details
 * A product is identified by product id (pid)
 */
 
// array for JSON response
$response = array();
 
// include db connect class
require_once __DIR__ . '/db_connect.php';
 
// connecting to db
$db = new DB_CONNECT();
 

if (isset($_GET["uid"])) {
    $uid = $_GET['uid']; 
    // get a product from products table
    $result = mysql_query("SELECT name FROM users WHERE uid =$uid") or die(mysql_error());

 
        if (mysql_num_rows($result) > 0) {

            // user node
        $response["user"] = array();
        $name_list = array();
 
           while($row = mysql_fetch_array($result)){
            
            $wire = array();
            $wire["name"] = $row["name"];


            array_push($response["user"], $wire);  
        }
            // success
            $response["success"] = 1;

            // echoing JSON response
            echo json_encode($response);
        } else {
            // no product found
            $response["success"] = 0;
            $response["message"] = "No user found";
 
            // echo no users JSON
            echo json_encode($response);
        }
    }else {
    // required field is missing
    $response["success"] = 0;
    $response["message"] = "Required field(s) is missing";
 
    // echoing JSON response
    echo json_encode($response);
}
?>