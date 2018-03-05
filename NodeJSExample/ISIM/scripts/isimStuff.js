var rp			= require('request-promise-native').defaults({simple: false});
var bodyParser 	= require('body-parser');
var querystring = require('querystring');


/*
 * auth(url, username, password)
 * 
 * url: ISIM URL...just the protocol://hostname:port
 * username: ISIM Account ID
 * password: ISIM Account Password
 * 
 * returns a JSON object with the session cookies (containing JSESSIONID and LtpaToken2)
 * And the CSRFToken
 * 
 */

exports.auth = function(url, username, password){
	
	return new Promise((resolve, reject)=>{
		
		var jsessionurl	= url + '/itim/restlogin/login.jsp';
		var ltpaurl 	= url + '/itim/j_security_check';
		var csrfurl 	= url + '/itim/rest/systemusers/me';
		var cookieJar 	= rp.jar();
		var csrftoken;
		//try to get JSESSIONID
		rp({url: jsessionurl, jar: cookieJar})
			.then(function(htmlString){
				
				var formData = querystring.stringify({
					j_username: username,
					j_password: password
				});
							
				//try to get ltpaToken2
				var options = {
					url: ltpaurl,
					method: 'POST',
					headers: {
						'Content-Type': "application/x-www-form-urlencoded",
						'Content-Length': formData.length
					},
					jar: cookieJar,
					body: formData,
					resolveWithFullResponse: true
				};
				return rp(options);
			})
			.then(function(response){					
					//get CSRFToken
					return rp({url: csrfurl, jar: cookieJar, resolveWithFullResponse: true });
				})
			.then(function(response){
				csrftoken = response.headers.csrftoken;
				var payload = {
					message: "login successful",
					cookies: cookieJar,
					csrftoken: csrftoken
				};
				resolve(payload);
			})
			.catch(function(err){
				reject(err);
			});
	});
};
	
	/**
	 * creatPerson requires:
	 *  url (isim base url including any protocol and non-default port): example: 'http://isimserver:9080'
	 *  sessionObj: this is the session object returned from isim.auth and includes the session cookies and csrftoken
	 *  requestObj: this should be a JSON object in the following format:
	 *  {
	 *  	container: {
	 *  		type: "organizationunits",
	 *  		name: "testou"
	 *  	},
	 *  	person: {
	 *  		type: "Person",
	 *  		attributes: {
	 *  			cn: "Bob Smith",
	 *  			sn: "Smith",
	 *  			givenname: "Bob",
	 *  			mail: "bsmith@l2.lab"
	 *  	}
	 *  }
	 *  
	 *  returns the ISIM response
	 */
	
	exports.createPerson = function(url, sessionObj, requestObj){
		
		return new Promise((resolve, reject)=>{
			
			var containerurl	= url + '/itim/rest/organizationcontainers/' + requestObj.container.type;
			var createpersonurl = url + '/itim/rest/people';
			var cookieJar 	= sessionObj.cookies;
			var csrftoken	= sessionObj.csrftoken;
			//try to get containerID
			rp({
				url: containerurl, 
				jar: cookieJar,
				headers: {
					'csrftoken': csrftoken
					}
				})
				.then(function(jsonString){
					var jsonObj = JSON.parse(jsonString);
					
					//grab the id of the container from the href returned
					var hrefString = jsonObj[0]._links.self.href;
					var hrefArray = hrefString.split("/");
					var containerID = hrefArray[5];
					
					var personJson = {
						'profileName': requestObj.person.type,
						'orgID': containerID,
						'_attributes': requestObj.person.attributes
					};
					
					//using stringified JSON in body:, because json: forces Accept: application/json and fails against this service
					var options = {
							url: createpersonurl,
							method: 'POST',
							headers: {
								'Content-Type': "application/json",
								'csrftoken': csrftoken
							}, 
							jar: cookieJar,
							body: JSON.stringify(personJson)
					};
					return rp(options);
				})
				.then(function(response){
					resolve(response);
				})
				.catch(function(err){
					reject(err);
				});
		});	
};