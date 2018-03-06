# PIM Python Example
This contains a py file and requirements (the latter to add smtplib in order to send email), 
to show how PIM REST APIs can be called/consumed within Python.

This example Authenticates to PIM and Searches for Credentials, all in one line.
This returns a subset of attributes...which are then parsed and formatted, then sent off in an email.

Update the following in example.py

Update Description | Example
-------------------|-----------
On `line 12`, update the `url` variable to reference your PIM server | `https://pimserver/ispim/rest/credentials....`
On `line 13`, update the account id and password in the `auth` param | `.... auth=('pimadmin', 'Test1234'),verify...`
In `lines 24 - 27`, update email addresses etc in the `sender`, `recievers`, `message`, `To` params | see example.py for example
In `line 41`, change `emailserver` to the hostname of your email server | `... smtplib.SMTP("emailserver",25)`

**NOTE: The code/documentation found here is NOT supported, and is supplied only as examples/reference.
Use at your own risk** :metal:

