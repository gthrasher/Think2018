# PIM Postman Example
This contains a Postman Collection and Environment that shows how to:
* Authenticate and Search for a User in PIM
_NOTE: PIM REST APIs allow for Basic Authentication in each call...so Authentication and Execution are in a single request_

Import both the Collection and Environment into [Postman](https://www.getpostman.com/)
To configure for communication with your PIM server, modify the following variables in the **PIM Env** Environment:

Environment Variable | Description | Example
---------------------|-------------|-----------
**pimurl** | URL for PIM | `https://pimserver/ispim/rest`
**username** | PIM Account ID | `domain admin`
**password** | PIM Account password | `Test1234`
**filter** | filter string to be used in the Search | `(cn=Bob*)`

**Be sure to select the _PIM Env_ Environment when running the Collection**
