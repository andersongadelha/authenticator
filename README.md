# Authenticato

Register user
POST /user
````json
{
    "name": "Fulano",
    "username":"fufu",
    "email":"teste.xablau@teste.com",
    "password": "123mudar",
    "roles": ["ROLE_ADMIN"]
}
````

Authenticate
POST /api/auth/login
````json
{
   "username":"fufu",
   "password": "123mudar"
}
````