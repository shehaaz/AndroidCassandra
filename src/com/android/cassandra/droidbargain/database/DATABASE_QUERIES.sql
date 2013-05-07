Example Venue JSON

{
    "venues": [
        {
            "nid": "68",
            "title": "Korovas",
            "address": "123 fake street",
            "rating": "100",
            "geolocation": "POINT (-73.57812 45.516377)"
        }
    ],
    "success": 1
}

GET ALL VENUES from TABLE = NODE

SELECT * 
FROM  `node` 
WHERE  `type` =  'venue'

LNG and LAT from the site

SELECT * 
FROM    `field_data_field_geo` 

POINT (-73.57812 45.516377)

Venue Address

SELECT * 
FROM  `field_data_field_address` 

Venue Rating
SELECT * 
FROM  `field_data_field_rating`

------------------------------------------------------------------------------------------------------
COMMITS: TABLE = REGISTRATION (LOOK AT entity_id and user_uid)

SELECT * 
FROM  `registration` 
LIMIT 0 , 100
-----------------------------------------------------------------------------------------------------
"get venue name by giving nid"
SELECT  V.title
FROM    node V
WHERE   V.nid = 70

"venues and their id's"
SELECT V.title, V.nid
FROM node V
WHERE V.type =  'venue'

"get name of user 70"
SELECT N.name 
FROM  users N
WHERE  N.uid =70
LIMIT 0 , 100

"heartbeat_activity for USER 57"
SELECT * 
FROM `heartbeat_activity` 
WHERE `uid` =57
AND `message_id` = 'heartbeat_edit_node'
AND `nid` <>0

"User i.d 85's Friends"
SELECT U.requestee_id
FROM user_relationships U
WHERE U.requester_id =85
AND U.approved =1

"combine venue and heartbeat_activity"
SELECT * 
FROM heartbeat_activity H, node V
WHERE H.uid =70
AND H.message_id =  'heartbeat_edit_node'
AND H.nid <>0
AND V.type =  'venue'
AND V.nid = H.nid

"venues attended by user testAccount's (uid =85) friends. Ordered Chronologically"
SELECT N.name, V.title
FROM user_relationships U, heartbeat_activity H, node V, users N
WHERE   U.requester_id =85
        AND (U.approved = 1) 
        AND (U.requestee_id = H.uid)
        AND (H.message_id = 'heartbeat_edit_node')
        AND (H.nid = V.nid)
        AND (U.requestee_id = N.uid)
        AND (V.type =  'venue')
ORDER BY H.uaid DESC



