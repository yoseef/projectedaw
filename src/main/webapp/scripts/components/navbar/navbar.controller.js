'use strict';

angular.module('leaguegenApp')
    .controller('NavbarController', function ($scope, $location, $state, Auth, Principal) {
        $scope.isAuthenticated = function() {
            var nick = Principal.getIdentity();
            if(nick){
                $scope.account = nick.login;
                console.log($scope.account);
            }

            return Principal.isAuthenticated();
        }
        $scope.$state = $state;

        $scope.logout = function () {
            $scope.account = null;
            Auth.logout();
            $state.go('home');
        };
    });
