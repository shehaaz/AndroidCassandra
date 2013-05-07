>>> import requests
>>> requests
<module 'requests' from '/usr/lib/python2.7/site-packages/requests-1.2.0-py2.7.e            gg/requests/__init__.pyc'>
>>> payload = {'username': 'bob', 'email': 'bob@bob.com'};
>>> r = requests.put("http://198.61.177.186:8080/virgil/data/android/users/2", d            ata=payload);
>>> r = requests.put("http://198.61.177.186:8080/virgil/data/android/users/2", "{\"foo\":\"3\",\"erg\":\"42\"}");
>>> r = requests.delete("http://198.61.177.186:8080/virgil/data/android/users/2");          
>>> r = requests.put("http://198.61.177.186:8080/virgil/data/android/users/2", "{\"name\":\"rifaadh\",\"email\":\"rifaadh@gmail.com\",\"skills\":[\"java\",\"html\"]}");

requests.put("http://198.61.177.186:8080/virgil/data/android/posts/1","{\"title\":\"OMG THESE Jeans are AMAZING\",\"body\":\"I found these amazing Jeans on sale at H&M for only 20 bucks\",\"price\":\"20\",\"location\":\"Forever 21\",\"latlng\":\"45.49806|-73.57506\"}");



{
  'firstName': 'ben',
  'skills': ['java', 'javascript', 'html'],
  'education': {
    school: 'cmu',
    majors: ['computer science', 'business']
  }
}

how to save composites

 requests.put("http://198.61.177.186:8080/virgil/data/android/users/2", "{\"name\":\"rifaadh\",\"email\":\"rifaadh@gmail.com\",\"skills\":\"[java,html]\"}");
 
 
 
 