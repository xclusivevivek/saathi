# Introduction
### Application to record information. To use it first create a schema which describe all the fields which are needed to capture information. 
### Once schema is created, we can store the information against that schema

# Api Details :

## Schema

### Create Schema :
#### Method : POST
#### Url: http://localhost:8090/schema/create
#### Payload :
{
"name":"address",
"fields": [{
"key" : "flatNo",
"fieldType" : "TEXT"
}]
}

### Update Schema :
#### Method : POST
#### Url: http://localhost:8090/schema/udate
#### Payload :
{
"name":"address",
"fields": [{
"key" : "flatNo",
"fieldType" : "TEXT"
}]
}

### Get Schema :
#### Method : GET
#### Url: http://localhost:8090/schema/get?name={name}

### Get Schema List :
#### Method : GET
#### Url: http://localhost:8090/schema

## Info Record

### Create Info Record :
#### Method : POST
#### Url: http://localhost:8090/inforecord/create
#### Payload :
{
"name":"MyAddress",
"schemaName":"address",
"values": {
"flatNo" : "A701"
}
}

### Update Info Record :
#### Method : POST
#### Url: http://localhost:8090/inforecord/update
#### Payload :
{
"name":"MyAddress",
"schemaName":"address",
"values": {
"flatNo" : "A701"
}
}

### Get Info Record :
#### Method : GET
#### Url: http://localhost:8090/inforecord/get?name={name}

### Delete Info Record :
#### Method : DELETE
#### Url: http://localhost:8090/inforecord/delete?name={name}



### Run configuration
#### Setup environment variables as follow
##### `KEYSTORE_PATH` : Path to the servers keystore file (pkcs12) 
##### `KEYSTORE_PASSWORD` : Keystore's password
##### `APP_ADMIN_USER` : APP Admin User
##### `APP_ADMIN_USER` : App Admin User's password
