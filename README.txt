In order to develop or use the ecsPortlet, you will need to be familiar with
Maven 2.  You will need to adjust the resources files located here:

src/main/resources
src/test/resources

You will notice that the there are some *.properties.example files.  You will
need to adjust those properties files to correspond to your environment and
exchange server.

The *ControllerTest.java files reach out to the actual exchange server with some
test credentials.  You will need to be able to adjust those test files to your
own environment.  The live testing was the only way I could see to test the
applicationContext.xml bean wiring.  You can skip these tests if you don't have
a live server to test against.

This portlet requires a way to get cached password credentials from the portal
into the portlet (via portlet.xml), and ultimately to exchange (assuming your
exchange credentials are the same as your portal login).  If you are using 
uPortal and CAS, you can leverage the CAS Clearpass extension:

https://wiki.jasig.org/display/CAS/Proxying+clear-text+credentials

This ClearPass extension will need to be deployed into your CAS war, and you
will need to include the clearpass-integration-uportal jar artifact in your
uportal/uportal-impl/pom.xml file, among other things - please see the following
link for more information on the portla steps necessary to use the Clearpass
extension:

https://wiki.jasig.org/display/CASUM/uPortal+ClearPass+Extension

