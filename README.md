# keycloak-sms-authenticator

Contains logic for:
 * sending sms on login and it's verification
 * change mobile number during login with it's verification

To install the SMS Authenticator one has to:

* Add the jar to the Keycloak server:
  * `$ cp target/keycloak-sms-authenticator.jar _KEYCLOAK_HOME_/providers/`
  * `$ cp target/keycloak-sms-authenticator.jar _KEYCLOAK_HOME_/standalone/deployments/`

* Add two templates to the Keycloak server:
  * `$ cp templates/sms-validation.ftl _KEYCLOAK_HOME_/themes/<theme>/login/`
  * `$ cp templates/mobile-number.ftl _KEYCLOAK_HOME_/themes/<theme>/login/`


Configure your REALM to use the SMS Authentication.
First create a new REALM (or select a previously created REALM).

Under Authentication > Flows:
* Copy 'Browse' flow to 'Browser with SMS' flow
* Click on 'Actions > Add execution on the 'Browser with SMS Forms' line and add the 'SMS Authentication'
* Set 'SMS Authentication' to 'REQUIRED'


Under Authentication > Bindings:
* Select 'Browser with SMS' as the 'Browser Flow' for the REALM.

Under Authentication > Required Actions:
* Click on Register and select `Update Mobile Number` to add the Required Action to the REALM.
* Click on Register and select `Verify Mobile Number` to add the Required Action to the REALM.
* Make sure that for the `Update Mobile Number` and the `Verify Mobile Number` the 'Enabled' check box are checked.

TODO:
* implement Twilio
* add docker compose 
* add realm.json for faster setup
