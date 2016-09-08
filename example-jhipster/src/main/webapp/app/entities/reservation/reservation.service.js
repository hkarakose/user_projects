(function() {
    'use strict';
    angular
        .module('ticketingApp')
        .factory('Reservation', Reservation);

    Reservation.$inject = ['$resource', 'DateUtils'];

    function Reservation ($resource, DateUtils) {
        var resourceUrl =  'api/reservation/:id';

        return $resource('api/reservation/:flightId/:username');
        // return $resource('api/reservation/:id', { id: '@_id' }, {
        //     update: {
        //         method: 'PUT' // this method issues a PUT request
        //     }
        // });
    }
})();
