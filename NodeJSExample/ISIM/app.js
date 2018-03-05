
var http = require('http');
var express = require('express');
var request = require('request');
var rp		= require('request-promise-native').defaults({simple: false});
var bodyParser = require('body-parser');
var querystring = require('querystring');
var grey 		= require('./scripts/isimStuff');

var app = express();

app.use(bodyParser.json());
app.use(bodyParser.urlencoded({extended: false}));

// serve the files out of ./public as our main files
app.use(express.static(__dirname + "/public"));

var port = (process.env.PORT || process.env.VCAP_APP_PORT || 8888);

/*
* test authentication to ISIM
* send the following parameters in GET
* authuser: the ISIM Account ID (example: itim manager)
* authpassword: the ISIM Account password  (example: secret)
* isimurl: the ISIM URL (example: http://isimserver:9080)
* 
* this will create a "session" object that contains the cookie jar with JSESSIONID and LtpaToken2, the CSRFToken, 
* and the authentiation message (upon success) which will be passed back to the client
*/

app.get('/isimAuth', function(req, res, err){
	console.log("in isimAuth..testing auth for: " + req.query.authuser);
	
	var user	= req.query.authuser;
	var pwd		= req.query.authpassword;
	var isimURL = req.query.isimurl;
	grey.auth(isimURL, user, pwd)
		.then(session => {
			console.log("session csrftoken???? " + session.csrftoken);
			console.log("session cookies??? " + session.cookies.getCookies(isimURL));
			
			res.send(session.message);
		})
		.catch(error => console.log("errrrrrr: " + error));
});

/*
* test authentication to ISIM and creation of a new User in ISIM
* send the following body in POST
* Header: Content-Type: application/json
* authuser: the ISIM Account ID (example: itim manager)
* authpassword: the ISIM Account password  (example: secret)
* isimurl: the ISIM URL (example: http://isimserver:9080)
* container: the name of the OU you want to create the User in 
* newuser: a nested object containing the following
* cn: common name
* sn: surname
* any other valid attributes (example: mail, givenname)
* example:
*  {
*     "isimurl": "http://isimserver:9080",
*     "authuser": "itim manager",
*     "authpassword": "secret",
*     "container": "Support",
*     "newuser": {
*         "cn": "Bob Smith",
*         "sn": "Smith",
*         "givenname": "Bob",
*         "mail": "bsmith@email.lab"
*      }
*  }
* 
* this will authenticate the user to the specified ISIM server and will create a new Person with the provided data
* then will return the request id from ISIM
*/

app.post('/isimCreatePerson', function(req, res, err){
	console.log("in isimCreatePerson");
	
	console.log("request: " + JSON.stringify(req.body));
	
	var user	= req.body.authuser;
	var pwd		= req.body.authpassword;
	var isimURL = req.body.isimurl;
	grey.auth(isimURL, user, pwd)
		.then(session => {
			console.log("session csrftoken???? " + session.csrftoken);
			console.log("session cookies??? " + session.cookies.getCookies(isimURL));
			
			var requestObj = {
					container: {
						type: "organizationunits",
						name: req.body.container
					},
					person: {
						type: "Person",
						attributes: {
							cn: req.body.newuser.cn,
							sn: req.body.newuser.sn,
							givenname: req.body.newuser.givenname,
							mail: req.body.newuser.mail
						}
					}
			};
			
			return grey.createPerson(isimURL, session, requestObj );
		})
		.then(function(response){
			res.send(response);
		})
		.catch(error => console.log("errrrrrr: " + error));
});



//start server on the specified port and binding host
app.listen(port, "0.0.0.0", function() {
	console.log("server starting on port " + port);
});

