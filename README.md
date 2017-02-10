# icflights
Ryanair airlines flight locator service based on Ryanair API.

#### Features:
  - Searches for the direct Ryanair airlines flights and interconnected flights with 1 stop for a given date/time interval and airports.
  - Maximum duration between time of departure and arrival is 768 hours (about a month).

#### Documentation:

1. API specification https://aqueous-taiga-77873.herokuapp.com/swagger-ui.html

2. High level application design: 

![alt text][design]

[design]: http://www.zamkovyi.name/files/ICFlights.png "High level design"

3. Deployable war file is available here: http://www.zamkovyi.name/files/icflights.war

4. Demo is available here: https://aqueous-taiga-77873.herokuapp.com/

5. In order to search for a flight use the following endpoint 
https://aqueous-taiga-77873.herokuapp.com/timetable/interconnections?departure={departureAirport}&arrival={arrivalAirport}&departureDateTime={departureDateTime}&arrivalDateTime={arrivalDateTime}
    
    - Where: {depatrureAirport} and  {arrival} are the airports passed as IATA codes,
           {departureDateTime} and {arrivalDateTime} are the time interval in yyyy-MM-ddTHH:mm format.
    
    - Example: https://aqueous-taiga-77873.herokuapp.com/timetable/interconnections?departure=WRO&arrival=DUB&departureDateTime=2017-02-10T07:00&arrivalDateTime=2017-02-12T22:00 
