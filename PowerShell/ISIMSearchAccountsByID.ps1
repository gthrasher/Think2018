# ID of the Account to search for
$acctId = $args[0]

# ISIM creds to authenticate with
$isimUserName = "itim manager"
$isimUserPassword = "secret"

# base ISIM URL
$baseUrl = "http://isimserver:9080"

# get JSESSIONID
$url = "$($baseUrl)/itim/restlogin/login.jsp"
$webrequest = Invoke-WebRequest -Uri $url -SessionVariable websession

# get LtpaToken2 (try block added to avoid returning the expected 404 error)
# 
$url2 = "$($baseUrl)/itim/j_security_check?j_username=$($isimUserName)&j_password=$($isimUserPassword)"
$webrequest2 =  try { 
                Invoke-WebRequest -Uri $url2 -WebSession $websession -Method POST
                } catch {
                    Write-Host $_Exception.Response.StatusCode
                    if($_Exception.Response.StatusCode -eq 'NotFound') {
                        Write-Host "expected 404 error"
                    } else {
                        Write-Host $_Exception.Response.StatusCode
                    }
                }

# get CSRFToken
$url3 = "$($baseUrl)/itim/rest/systemusers/me"
$webrequest3 = Invoke-WebRequest -Uri $url3 -WebSession $websession


# search for any accounts with id matching value passed to script (limiting to 1 result for quicker searches and since only concerned with existence of any account with matching ID)
$url4 = "$($baseUrl)/itim/rest/accounts?eruid=$($acctId)&limit=1"
$webrequest4 = Invoke-WebRequest -Uri $url4 -WebSession $websession 
$req4Array = $webrequest4 | ConvertFrom-Json
$resultCount = $req4Array.Count
Write-Host "Matching Account Count: $($resultCount)"


# is there a matching account?
if($resultCount -gt 0){
   Write-Host "Found matching Account ID!"

} else {
   Write-Host "No matching Account IDs found!"
} 