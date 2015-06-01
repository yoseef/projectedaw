'use strict';

angular.module('leaguegenApp')
    .controller('InfoController', function ($scope, Principal) {
        //Principal.identity().then(function(account) {
        //    $scope.account = account;
        //    $scope.isAuthenticated = Principal.isAuthenticated;
        //});
        function initialize() {
            var mapCanvas = document.getElementById('map-canvas');
            var mapOptions = {
                center: new google.maps.LatLng(44.5403, -78.5463),
                zoom: 8,
                mapTypeId: google.maps.MapTypeId.ROADMAP
            }
            var map = new google.maps.Map(mapCanvas, mapOptions)
        }
        google.maps.event.addDomListener(window, 'load', initialize);
    });
