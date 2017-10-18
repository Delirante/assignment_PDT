# General course assignment

Build a map-based application, which lets the user see geo-based data on a map and filter/search through it in a meaningfull way. Specify the details and build it in your language of choice. The application should have 3 components:

1. Custom-styled background map, ideally built with [mapbox](http://mapbox.com). Hard-core mode: you can also serve the map tiles yourself using [mapnik](http://mapnik.org/) or similar tool.
2. Local server with [PostGIS](http://postgis.net/) and an API layer that exposes data in a [geojson format](http://geojson.org/).
3. The user-facing application (web, android, ios, your choice..) which calls the API and lets the user see and navigate in the map and shows the geodata. You can (and should) use existing components, such as the Mapbox SDK, or [Leaflet](http://leafletjs.com/).

## Example projects

- Showing nearby landmarks as colored circles, each type of landmark has different circle color and the more interesting the landmark is, the bigger the circle. Landmarks are sorted in a sidebar by distance to the user. It is possible to filter only certain landmark types (e.g., castles).

- Showing bicykle roads on a map. The roads are color-coded based on the road difficulty. The user can see various lists which help her choose an appropriate road, e.g. roads that cross a river, roads that are nearby lakes, roads that pass through multiple countries, etc.

## Data sources

- [Open Street Maps](https://www.openstreetmap.org/)

# My project

**Application description**: Application shows on map various healthcare objects in Bratislava city. 
User have option to mark which objects he want to show on map (doctors, dentists, hospitals ect.).
Every shown object on map have popup with its own description. Shown objects are color separated.
There are three main usecases which can my application do:

1. Show on map healthcare objects and fill them with color based on how far they are from user position.
2. Show on map healthcare objects in chosen area (Ruzinov, Petrzalka ect.) which have nearby parking place.
3. Show on map three nearsets healthcare objects in chosen area (Ruzinov, Petrzalka ect.).

Application screenshots:

![Screenshot](scr1.png)

![Screenshot](scr2.png)

**Data source**: `Open street map`

**Technologies used**: `Java, Spring, HTML, CSS, Javascript, Ajax, Leaflet, Postgresql, Postgis`

Application consits of three parts: Client side - [Frontend](#frontend), Server side - [Backend](#backend), [Database](#database).
Application have build in Tomcat server which listen on address http://localhost:8080/ after run.

## Frontend
As a frontend there is one html file called index.html. 
Index.html contains leaflet widget which is responsible for managing map and left panel which is contains buttons responsible for mentioned functionalities (three usecases).
Buttons in left panel call ajax requests to the backend. Backend process these requests and answer to frontend with http POSTs which contains geojson data. 
Answer from backend (geojson data) is inserted to the leaflet plugin. Plugin afterwards paint result to the map.

## Backend
As a backend there is REST api written in language Java with framework Spring.
Api is making calls to database and format result to geojson.

Api contains 3 REST services:
1. searchFromPoint
	1. Service is responsible for 1 usecase.
	2. Http call for this service may loooks like this:
		http://localhost:8080/searchFromPoint?doctor=true&dentist=true&hospital=true&clinic=true&pharmacy=true&point=LatLng(48.17135,%2017.06672)
2. searchInArea
	1. Service is responsible for 2 usecase.
	2. Http call for this service may loooks like this:
		http://localhost:8080/searchInArea?doctor=true&dentist=true&hospital=true&clinic=true&pharmacy=true&dropdown=0
3. searchTriples  
	1. Service is responsible for 3 usecase.
	2. Http call for this service may loooks like this:
		http://localhost:8080/searchTriples?doctor=true&dentist=false&hospital=false&clinic=true&pharmacy=true&dropdown=0

The backend application is written in Python and there are executed quries through library mentined earlier. It helps with working with geo data and formatting them to the geojson.

## Data


## Database
