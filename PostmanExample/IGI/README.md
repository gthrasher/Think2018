# IGI Postman Example
This contains a Postman Collection and Environment that shows how to:
* Authenticate to IGI
  * Store the returned Bearer Token
* Return a list of SCIM Resource Types
* Search for Users

Import both the Collection and Environment into [Postman](https://www.getpostman.com/)
To configure for communication with your IGI server, modify the following variables in the **IGI Env** Environment:

Environment Variable | Description | Example
---------------------|-------------|-----------
**igiurl** | URL for IGI | `https://igiserver:9343/igi/v2`
**username** | IGI Account ID | `admin`
**userpassword** | IGI Account password | `admin`
**adminRealm** | IGI Admin Realm (if logging in as `admin`) | `Admin`
**userRealm** | IGI User Realm (if logging in as a non-admin) | `Ideas`
**filter** | filter string to be used in the Search | `*Bob*`

**Be sure to select the _IGI Env_ Environment when running the Collection**

**NOTE: The code/documentation found here is NOT supported, and is supplied only as examples/reference.
Use at your own risk** :metal:
