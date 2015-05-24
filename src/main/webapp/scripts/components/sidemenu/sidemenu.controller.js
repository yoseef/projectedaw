'use strict';

angular.module('leaguegenApp')
    .controller('SidemenuCtrl', function ($scope, $location, $state, Auth, Principal) {
        $scope.isAuthenticated = Principal.isAuthenticated;
    });
