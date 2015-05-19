'use strict';

angular.module('leaguegenApp')
    .controller('FranjaDetailController', function ($scope, $stateParams, Franja, Partit) {
        $scope.franja = {};
        $scope.load = function (id) {
            Franja.get({id: id}, function(result) {
              $scope.franja = result;
            });
        };
        $scope.load($stateParams.id);
    });
