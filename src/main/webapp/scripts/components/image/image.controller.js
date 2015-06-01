'use strict';

angular.module('leaguegenApp')
    .controller('imageeController', function ($scope, imagee) {
        var files;
        $scope.setFile = function (f) {
            files = f;
        };
        $scope.uploadIt = function () {
            if (files != null && files.length > 0) {
                var filee = files[0];
                imagee.save({'file' : filee}, function (result) {
                    console.info(result.msg);
                });
            }
        };
    });






