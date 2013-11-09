cucuModule.factory('User', function() {
	user = { username: "", password: "", email: "", authorized: false };
	user.register = function() {
	console.log("registering ", user);
	user.authorized = true;
	};
	user.login = function() {
	console.log("logging in ", user);
	user.authorized = true;
	};
	user.logout = function() {
	console.log("logging out ", user);
	user.clear();
	};
	user.clear = function() {
	user.username = "";
	user.password = "";
	user.email = "";
	user.authorized = false;
	}
	return user;
	});