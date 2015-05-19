'use strict';

angular.module('leaguegenApp')
    .controller('TemporadaController', function ($scope, Temporada, Grup, TemporadaSearch) {
        $scope.temporadas = [];
        $scope.grups = Grup.query();
        $scope.loadAll = function() {
            Temporada.query(function(result) {
               $scope.temporadas = result;
            });
        };
        $scope.loadAll();

        $scope.showUpdate = function (id) {
            Temporada.get({id: id}, function(result) {
                $scope.temporada = result;
                $('#saveTemporadaModal').modal('show');
            });
        };

        $scope.save = function () {
            if ($scope.temporada.id != null) {
                Temporada.update($scope.temporada,
                    function () {
                        $scope.refresh();
                    });
            } else {
                Temporada.save($scope.temporada,
                    function () {
                        $scope.refresh();
                    });
            }
        };

        $scope.delete = function (id) {
            Temporada.get({id: id}, function(result) {
                $scope.temporada = result;
                $('#deleteTemporadaConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            Temporada.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deleteTemporadaConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.search = function () {
            TemporadaSearch.query({query: $scope.searchQuery}, function(result) {
                $scope.temporadas = result;
            }, function(response) {
                if(response.status === 404) {
                    $scope.loadAll();
                }
            });
        };

        $scope.refresh = function () {
            $scope.loadAll();
            $('#saveTemporadaModal').modal('hide');
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.temporada = {any: null, descripcio: null, id: null};
            $scope.editForm.$setPristine();
            $scope.editForm.$setUntouched();
        };
    });
