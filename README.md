# keycloak-sms-authenticator

Contains logic for:
 * sending sms on login and it's verification
 * support resend functionality by asking linked mobile phone

To install the SMS Authenticator:

Add the jar with dependencies to the Keycloak server:
  * `$ cp target/keycloak-sms-authenticator-1.0.0-SNAPSHOT-jar-with-dependencies.jar _KEYCLOAK_HOME_/standalone/deployments/`

Configure your REALM to use the SMS Authentication.
First create a new REALM (or select a previously created REALM).

Under Authentication > Flows:
* Copy 'Browse' flow to 'Browser with SMS' flow
* Click on 'Actions > Add execution on the 'Browser with SMS Forms' line and add the 'SMS Authentication'
* Set 'SMS Authentication' to 'REQUIRED'

Under Authentication > Bindings:
* Select 'Browser with SMS' as the 'Browser Flow' for the REALM.

Make sure that all users has `mobile_number` attribute.

One everything done on login you will be asked for sms code which you can find in logs.

For real world application just replace `LogsSmsProducer` with your implementation.

Development:
 * use docker compose to start keycloak
 * put jar inside `./_infrastructure/keycloak/deployments/` to deploy and test functionality
