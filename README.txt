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
applicationContext.xml bean wiring.

This portlet works in conjunction with:
trunk/source/portlets/maven-plugins/portlet-deployment-plugin

Please look at the source of that plugin, it helps w/deploying this portlet
to a remote luminis web application server.  You should look at:

trunk/source/portlets/maven-plugins/portlet-deployment-plugin/README
trunk/source/portlets/maven-plugins/portlet-deployment-plugin/example-maven-settings.xml

To see how JSR-168 portlets, and the ecsPortlet work with this deployment
plugin.
