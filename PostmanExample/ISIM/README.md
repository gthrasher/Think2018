# ISIM Postman Example
This contains a Postman Collection and Environment that shows how to:
* Authenticate to ISIM
  * Get the JSESSIONID
  * Get the LtpaToken2
  * Get and store the CSRFToken
* Get the ID of the Organization
* Create a new User (in the Organization)

Import both the Collection and Environment into [Postman](https://www.getpostman.com/)
To configure for communication with your ISIM server, modify the following variables in the **ISIM Env Example** Environment:

Environment Variable | Description | Example
---------------------|-------------|-----------
**isimurl** | URL for ISIM | `https://isimserver:9443`
**isimusername** | ISIM Account ID | `itim manager`
**isimpassword** | ISIM Account password | `secret`

**Be sure to select the _ISIM Env Example_ Environment when running the Collection**

**NOTE: The code/documentation found here is NOT supported, and is supplied only as examples/reference.
Use at your own risk** :metal:
