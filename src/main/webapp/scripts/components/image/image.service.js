'use strict';

angular.module('leaguegenApp')
    .factory('imagee', function ($resource) {
        return $resource('imatge/:id', {}, {
            'query': { method: 'GET'},
            'get': {
                //method: 'GET',
                //transformResponse: function (data) {
                //    console.log(data);
                //    data = angular.fromJson(data);
                //    return data;
                //}
            },
            'save': {
                method: 'POST',
                transformRequest: function (data){
                    if (data === undefined)
                        return data;
                    var fd = new FormData();
                    angular.forEach(data, function(value, key) {
                        if (value instanceof FileList) {
                            if (value.length == 1) {
                                fd.append(key, value[0]);
                            } else {
                                angular.forEach(value, function(file, index) {
                                    fd.append(key + '_' + index, file);
                                });
                            }
                        } else {
                            fd.append(key, value);
                        }
                    });
                    return fd;
                },
                headers: {'Content-Type': undefined}
            },
            'update': { method:'PUT' }
        });
    });
