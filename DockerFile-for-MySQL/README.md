# DockerFile for creating a local MySQL container (only for testing purposes)
IE 2020/2021 DockerFile for creating a local MySQL container (only for testing purposes)

Install Docker in your machine, and then execute the following build command to create an image of MySQL with a simple datamodel for IoTaaS testing:

```
docker build . -t mysqliotaas
```

Then, create a container
```
docker run  -d -p 3306:3306 -it <imageID returned in the last message returned in the previous build command>
```

You can now access your MySQL server locally. Consult db.sql file to further details regarding user/password and created objects.