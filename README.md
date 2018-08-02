# Gutenberg_Project

Gruppe : 
- Mathias Meldgaard Pedersen
- Joachim Dittman Jepsen
- Jonathan 
- Nikolai Hansen

Vi har lavet et lille rest api til denne opgave. 
Vi har brugt Jetty som server til rest api'et og lavet opstæning i maven.

Steps til at installere dette projekt:
1. Git clone
2. Build projekt (kan åbnes i NetBeans)
3. Brug evt. postman file (/Gutenberg_postman.json) til rest api'et

Rest api url's - alle kald er som Post request:
1. http://localhost:8080/api/books-by-city
body : { "city": "copenhagen" }

2. http://localhost:8080/api/cities-by-title 
body : { "title": "The Song Of Hiawatha" }

3. http://localhost:8080/api/books-by-author
body : {"author": "John Milton"}

4. http://localhost:8080/api/books-by-location
body : {"latitude": 55.67594, "longitude": 12.56553}

