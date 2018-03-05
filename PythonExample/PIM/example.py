'''
Created on Oct 31, 2017

@author: gthrasher
'''

import smtplib
import requests

print "Starting..."

url = 'https://pimserver/ispim/rest/credentials?embedded=resource.name,resource.alias' # replace pimserver with your pim server hostname
ret = requests.get(url,auth=('pimadmin','Test1234'),verify=False)                      # replace pimadmin and Test1234 with your admin domain credentials in PIM
print(ret.status_code)
creds = ret.json()
print(creds)
credInfoList = ""
for idx, cred in enumerate(creds):
    backColor = '#f2f2f2' if (idx % 2 == 0) else 'white'
    credInfoList += "<tr style='background-color: " + backColor + ";'><td>" + cred['_links']['self']['title'] + "</td>"
    credInfoList += "<td>" + cred['_embedded']['resource']['_attributes']['name'][0] + "</td></tr>"

#smtp stuff
sender = 'bsmith@email.lab'
receivers = 'tallen@email.lab'
message = """From: Bob Smith <bsmith@email.lab>
To: Tim Allen <tallen@email.lab>
MIME-Version: 1.0
Content-type: text/html
Subject: PIM Credentials

Here is a list of PIM Credentials and their associated Resources.

<table style='border-collapse; collapse; width: 70%;'>
<thead>
<tr><th style='padding-top: 12px; padding-bottom: 12px; text-align: left; background-color: #1f57a4; color: white; width: 50%;'> Credential ID</th><th style='padding-top: 12px; padding-bottom: 12px; text-align: left; background-color: #1f57a4; color: white; width: 50%;'> Resource Name</th></tr></thead><tbody>
"""
message += credInfoList + "</tbody></table>"

try:
    smtpObj = smtplib.SMTP("emailserver",25) # replace emailserver with your SMTP server hostname
    smtpObj.sendmail(sender, receivers, message)
    print "Successfully sent email"
except smtplib.SMTPException:
    print "Error: unable to send email"