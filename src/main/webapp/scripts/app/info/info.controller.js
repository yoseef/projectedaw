'use strict';

angular.module('leaguegenApp')
    .controller('InfoController', function ($scope, Principal) {
        var mapOptions = {
            zoom: 15,
            center: new google.maps.LatLng(42.273796, 2.964970),
            mapTypeId: google.maps.MapTypeId.roadmap
        }

        $scope.map = new google.maps.Map(document.getElementById('map'), mapOptions);

        $scope.markers = [];

        var infoWindow = new google.maps.InfoWindow();

        var createMarker = function (info){

            var marker = new google.maps.Marker({
                map: $scope.map,
                position: new google.maps.LatLng(info.lat, info.long),
                title: info.city
            });
            marker.content = '<div class="infoWindowContent">' + info.desc + '</div>';

            google.maps.event.addListener(marker, 'click', function(){
                infoWindow.setContent('<h2>' + marker.title + '</h2>' + marker.content);
                infoWindow.open($scope.map, marker);
            });

            $scope.markers.push(marker);

        }
        var marker =
            {
                city : 'Ies cendrassos',
                desc : '2 DAW =)!',
                lat : 42.273796,
                long : 2.964970
            }

        createMarker(marker);


        $scope.openInfoWindow = function(e, selectedMarker){
            e.preventDefault();
            google.maps.event.trigger(selectedMarker, 'click');
        }

    });
