cucuModule.controller('mainController',
    function($scope, User) {
    $scope.user = User;
    });

cucuModule.controller('loggedUserController',
    function($scope, $location, User) {
    $scope.logout = function() {
    User.logout();
    $location.path("/");
    }});

cucuModule.controller('loginUserController',
	function($scope, $location, User) {
	$scope.login = function() {
	User.login();
    $location.path("/");
	}});

cucuModule.controller('registerUserController',
	function($scope, $location, User) {
	$scope.register = function() {
    User.register();
    $location.path("/");
    }});
