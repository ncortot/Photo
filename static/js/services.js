'use strict';

/* Services */

angular.module('photo.services', ['ngResource']).
  factory('Browser', ['$resource', function($resource) {
    return $resource('/api/browse/:path', {}, {
      query: {method: 'GET', params: {path: 'root'}, isArray: true}
    });
  }]).
  factory('Photo', ['$resource', function($resource) {
    return $resource('/api/photo/:photoId');
  }]);
