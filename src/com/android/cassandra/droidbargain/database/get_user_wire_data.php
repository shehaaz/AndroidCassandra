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
    $result = mysql_query("SELECT N.name, V.title
FROM user_relationships U, heartbeat_activity H, node V, users N
WHERE   U.requester_id =$uid
        AND (U.approved = 1) 
        AND (U.requestee_id = H.uid)
        AND (H.message_id = 'heartbeat_edit_node')
        AND (H.nid = V.nid)
        AND (U.requestee_id = N.uid)
        AND (V.type =  'venue')
ORDER BY H.uaid DESC") or die(mysql_error());

 
        if (mysql_num_rows($result) > 0) {

            // user node
        $response["wire"] = array();
        $name_list = array();
 
           while($row = mysql_fetch_array($result)){
            
            $wire = array();
            $wire["name"] = $row["name"];
            $wire["title"] = $row["title"];


            array_push($response["wire"], $wire);  
        }
            // success
            $response["success"] = 1;

            // echoing JSON response
            echo json_encode($response);
        } else {
            // no product found
            $response["success"] = 0;
            $response["message"] = "No wire found";
 
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