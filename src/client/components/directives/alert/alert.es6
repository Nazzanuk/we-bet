'use strict';

app.directive('alert', (Alert) => {
    return {
        templateUrl: 'alert.html',
        scope: {},

        link(scope, element, attrs) {

            var init = () => {
            };

            init();

            scope.getColour = Alert.getColour;
            scope.getMessage = Alert.getMessage;
            scope.getActive = Alert.getActive;
            scope.setActive = Alert.setActive;
            scope.switchActive = Alert.switchActive;
        }
    }
});
