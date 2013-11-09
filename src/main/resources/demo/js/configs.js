cucuModule.config(function($routeProvider) {
    $routeProvider
    .when('/', {
    controller: 'loginUserController',
    templateUrl: 'login.htm'
    })
    .when('/login', {
    controller: 'loginUserController',
    templateUrl: 'login.htm'
    })
    .when('/register', {
    controller: 'registerUserController',
    templateUrl: 'register.htm'
    })
    .otherwise({
     redirectTo: '/'
    })
    });