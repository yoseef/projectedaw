'use strict';

angular.module('leaguegenApp')
    .controller('JornadaController', function ($scope, Jornada, Grup, Partit, JornadaSearch) {
        $scope.jornadas = [];
        $scope.grups = Grup.query();
        $scope.partits = Partit.query();
        $scope.loadAll = function() {
            Jornada.query(function(result) {
               $scope.jornadas = result;
            });
        };
        $scope.loadAll();

        $scope.showUpdate = function (id) {
            Jornada.get({id: id}, function(result) {
                $scope.jornada = result;
                $('#saveJornadaModal').modal('show');
            });
        };

        $scope.save = function () {
            if ($scope.jornada.id != null) {
                Jornada.update($scope.jornada,
                    function () {
                        $scope.refresh();
                    });
            } else {
                Jornada.save($scope.jornada,
                    function () {
                        $scope.refresh();
                    });
            }
        };

        $scope.delete = function (id) {
            Jornada.get({id: id}, function(result) {
                $scope.jornada = result;
                $('#deleteJornadaConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            Jornada.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deleteJornadaConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.search = function () {
            JornadaSearch.query({query: $scope.searchQuery}, function(result) {
                $scope.jornadas = result;
            }, function(response) {
                if(response.status === 404) {
                    $scope.loadAll();
                }
            });
        };

        $scope.refresh = function () {
            $scope.loadAll();
            $('#saveJornadaModal').modal('hide');
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.jornada = {numero: null, id: null};
            $scope.editForm.$setPristine();
            $scope.editForm.$setUntouched();
        };




    });
