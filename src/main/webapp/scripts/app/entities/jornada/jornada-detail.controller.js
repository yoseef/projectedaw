'use strict';

angular.module('leaguegenApp')
    .controller('JornadaDetailController', function ($scope, $stateParams, Jornada, Grup, Partit) {
        $scope.jornada = {};
        $scope.load = function (id) {
            Jornada.get({id: id}, function(result) {
              $scope.jornada = result;
            });
        };
        $scope.load($stateParams.id);
    });
