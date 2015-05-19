'use strict';

angular.module('leaguegenApp')
    .controller('FranjaController', function ($scope, Franja, Partit, FranjaSearch) {
        $scope.franjas = [];
        $scope.partits = Partit.query();
        $scope.loadAll = function() {
            Franja.query(function(result) {
               $scope.franjas = result;
            });
        };
        $scope.loadAll();

        $scope.showUpdate = function (id) {
            Franja.get({id: id}, function(result) {
                $scope.franja = result;
                $('#saveFranjaModal').modal('show');
            });
        };

        $scope.save = function () {
            if ($scope.franja.id != null) {
                Franja.update($scope.franja,
                    function () {
                        $scope.refresh();
                    });
            } else {
                Franja.save($scope.franja,
                    function () {
                        $scope.refresh();
                    });
            }
        };

        $scope.delete = function (id) {
            Franja.get({id: id}, function(result) {
                $scope.franja = result;
                $('#deleteFranjaConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            Franja.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deleteFranjaConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.search = function () {
            FranjaSearch.query({query: $scope.searchQuery}, function(result) {
                $scope.franjas = result;
            }, function(response) {
                if(response.status === 404) {
                    $scope.loadAll();
                }
            });
        };

        $scope.refresh = function () {
            $scope.loadAll();
            $('#saveFranjaModal').modal('hide');
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.franja = {dia: null, hora_inici: null, hora_fi: null, id: null};
            $scope.editForm.$setPristine();
            $scope.editForm.$setUntouched();
        };
    });
