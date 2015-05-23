'use strict';

angular.module('leaguegenApp')
    .controller('ClassificacioController', function ($scope, Classificacio, Temporada, Grup, Equip, ClassificacioSearch) {
        $scope.classificacios = [];
        $scope.temporadas = Temporada.query();
        $scope.grups = Grup.query();
        $scope.equips = Equip.query();
        $scope.loadAll = function() {
            Classificacio.query(function(result) {
               $scope.classificacios = result;
            });
        };
        $scope.loadAll();

        $scope.showUpdate = function (id) {
            Classificacio.get({id: id}, function(result) {
                $scope.classificacio = result;
                $('#saveClassificacioModal').modal('show');
            });
        };

        $scope.save = function () {
            if ($scope.classificacio.id != null) {
                Classificacio.update($scope.classificacio,
                    function () {
                        $scope.refresh();
                    });
            } else {
                Classificacio.save($scope.classificacio,
                    function () {
                        $scope.refresh();
                    });
            }
        };

        $scope.delete = function (id) {
            Classificacio.get({id: id}, function(result) {
                $scope.classificacio = result;
                $('#deleteClassificacioConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            Classificacio.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deleteClassificacioConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.search = function () {
            ClassificacioSearch.query({query: $scope.searchQuery}, function(result) {
                $scope.classificacios = result;
            }, function(response) {
                if(response.status === 404) {
                    $scope.loadAll();
                }
            });
        };

        $scope.refresh = function () {
            $scope.loadAll();
            $('#saveClassificacioModal').modal('hide');
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.classificacio = {punts: null, id: null};
            $scope.editForm.$setPristine();
            $scope.editForm.$setUntouched();
        };
    });
