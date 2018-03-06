# ISIM Node.js Example

This sample node.js app includes two services:
* **isimAuth** (GET): This tests Authentication to ISIM and returns a session object containing:
  * Cookie Jar with:
    * JSESSIONID
    * LtpaToken2
  * CSRFToken
  * response message (upon success)
  
* **isimCreatePerson** (POST): This authenticates the user, then creates a Person in ISIM and returns the request ID

For both there are details in the comments above each (in `app.js`).

I've also included a Postman Collection and Environment, that can be used to test these services.
</br>
In the Postman Environment, the following will need to be update:

Environment Variable | Description | example
---------------------|-------------|---------
**nodeurl** | url to your running node application | `http://localhost:8888`
**isimurl** | url to your ISIM serveer | `http://isimserver:9080`
**isimuser** | valid ISIM Account ID | itim manager
**isimpassword** | password for the ISIM Account | secret

* After copying the files to a node server, `npm install` must be run in order to install all the required packages/dependencies.

**NOTE: The code/documentation found here is NOT supported, and is supplied only as examples/reference.
Use at your own risk** :metal:
