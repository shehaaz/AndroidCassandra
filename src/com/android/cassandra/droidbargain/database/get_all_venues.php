<?php
 
/*
 * Following code will list all the venues
 */
 
// array for JSON response
$response = array();
 
// include db connect class
require_once __DIR__ . '/db_connect.php';
 
// connecting to db
$db = new DB_CONNECT();
 
// get all venues from venues table
$result = mysql_query("SELECT V.nid, V.title, G.field_geo_lon, G.field_geo_lat, A.field_address_thoroughfare
FROM node V, field_data_field_geo G, field_data_field_address A
WHERE (V.type = 'venue') AND
(V.nid = A.entity_id) AND
(V.nid = G.entity_id)") or die(mysql_error());
 
// check for empty result
if (mysql_num_rows($result) > 0) {
    // looping through all results
    // venues node
    $response["venues"] = array();
 
    while ($row = mysql_fetch_array($result)) {
        // temp user array
        $venues = array();
        $venues["nid"] = $row["nid"];
        $venues["title"] = $row["title"];
        $venues["field_geo_lon"] = $row["field_geo_lon"];
        $venues["field_geo_lat"] = $row["field_geo_lat"];
        $venues["field_address_thoroughfare"] = $row["field_address_thoroughfare"];
 
        // push single venues into final response array
        array_push($response["venues"], $venues);
    }
    // success
    $response["success"] = 1;
 
    // echoing JSON response
    echo json_encode($response);
} else {
    // no venues found
    $response["success"] = 0;
    $response["message"] = "No venues found";
 
    // echo no users JSON
    echo json_encode($response);
}
?>