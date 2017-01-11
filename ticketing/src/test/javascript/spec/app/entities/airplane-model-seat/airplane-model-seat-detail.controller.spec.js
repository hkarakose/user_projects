'use strict';

describe('Controller Tests', function() {

    describe('AirplaneModelSeat Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockAirplaneModelSeat, MockAirplaneModel;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockAirplaneModelSeat = jasmine.createSpy('MockAirplaneModelSeat');
            MockAirplaneModel = jasmine.createSpy('MockAirplaneModel');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'AirplaneModelSeat': MockAirplaneModelSeat,
                'AirplaneModel': MockAirplaneModel
            };
            createController = function() {
                $injector.get('$controller')("AirplaneModelSeatDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'ticketingApp:airplaneModelSeatUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
