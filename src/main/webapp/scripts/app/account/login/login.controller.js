'use strict';

angular.module('leaguegenApp')
    .controller('LoginController', function ($rootScope, $scope, $state, $timeout, Auth,Principal) {
        $scope.user = {};
        $scope.errors = {};

        $scope.rememberMe = true;
        $timeout(function (){angular.element('[ng-model="username"]').focus();});
        $scope.login = function () {
            Auth.login({
                username: $scope.username,
                password: $scope.password,
                rememberMe: $scope.rememberMe
            }).then(function () {
                $scope.authenticationError = false;
                //console.log(Principal.getIdentity());
                if(Principal.isInRole("ROLE_ADMIN")){
                    $state.go('temporada');
                } else if(Principal.isInRole("ROLE_CAPITA")){
                    $state.go('equip');
                } else if(Principal.isInRole("ROLE_USER")){
                    $state.go('classificacio');
                } else {
                    $state.go('home');
                }

                //if ($rootScope.previousStateName === 'register') {
                //    $state.go('home');
                //} else {
                    //$rootScope.back();
                //}
            }).catch(function () {
                $scope.authenticationError = true;
            });
        };
    });
