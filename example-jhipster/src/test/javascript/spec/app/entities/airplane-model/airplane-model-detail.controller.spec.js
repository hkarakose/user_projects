'use strict';

describe('Controller Tests', function() {

    describe('AirplaneModel Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockAirplaneModel, MockAirplane, MockAirplaneModelSeat;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockAirplaneModel = jasmine.createSpy('MockAirplaneModel');
            MockAirplane = jasmine.createSpy('MockAirplane');
            MockAirplaneModelSeat = jasmine.createSpy('MockAirplaneModelSeat');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'AirplaneModel': MockAirplaneModel,
                'Airplane': MockAirplane,
                'AirplaneModelSeat': MockAirplaneModelSeat
            };
            createController = function() {
                $injector.get('$controller')("AirplaneModelDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'ticketingApp:airplaneModelUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
