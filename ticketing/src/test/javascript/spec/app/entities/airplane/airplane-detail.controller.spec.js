'use strict';

describe('Controller Tests', function() {

    describe('Airplane Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockAirplane, MockAirplaneModel, MockAirlines;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockAirplane = jasmine.createSpy('MockAirplane');
            MockAirplaneModel = jasmine.createSpy('MockAirplaneModel');
            MockAirlines = jasmine.createSpy('MockAirlines');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'Airplane': MockAirplane,
                'AirplaneModel': MockAirplaneModel,
                'Airlines': MockAirlines
            };
            createController = function() {
                $injector.get('$controller')("AirplaneDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'ticketingApp:airplaneUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
