# Product Rates


## Ejecutar la aplicación
Para lanzar la aplicación ejecutar el comando 

``./gradlew bootRun``

La aplicación se ejecutará sobre el puerto 8080.

### Para probar la aplicación
Podemos ejecutar el siguiente comando en la terminal cuando la aplicación se está ejecutando
```
curl --location --request GET 'http://localhost:8080/rate?application_date=2020-06-14%2010:00:00&product_id=35455&brand_id=1'
```
El formato de las fechas es el mismo que viene en la base de datos de ejemplo ```2020-06-14 00:00:00```
>**_NOTA_** ```2020-06-14 10:00:00``` -> ```2020-06-14%2010:00:00``` url encoded

## Ejecutar los tests
Se ejecutan con el comando


``./gradlew test``

## Notas
He añadido una cache al caso de uso que devuelve los precios de los artículos, he puesto a modo de ejemplo 20 minutos 
de duración de la persistencia en cache, pero en un caso real habría que estudiar esta duración en detalle y usar otro
tipo de cache que no fuese en memoria (por ejemplo una solución como Redis).

Los tests solicitados en la práctica se encuentran en el fichero ``/src/test/java/com/shop/prices/infrastructure/api/RateControllerAcceptanceTest.java``
Se podrían ejecutar por consola con la aplicación ejecutándose mediante los siguientes comandos:

```
curl --location --request GET 'http://localhost:8080/rate?application_date=2020-06-14%2010:00:00&product_id=35455&brand_id=1'
curl --location --request GET 'http://localhost:8080/rate?application_date=2020-06-14%2016:00:00&product_id=35455&brand_id=1'
curl --location --request GET 'http://localhost:8080/rate?application_date=2020-06-14%2021:00:00&product_id=35455&brand_id=1'
curl --location --request GET 'http://localhost:8080/rate?application_date=2020-06-15%2010:00:00&product_id=35455&brand_id=1'
curl --location --request GET 'http://localhost:8080/rate?application_date=2020-06-16%2021:00:00&product_id=35455&brand_id=1'
```
