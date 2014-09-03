#!/bin/sh
# CLASSPATH="$JAVA_HOME"/lib/
JAVA_OPTS="-Dfile.encoding=UTF8"
JAVA_HOME="D:/Servers/jdk1.5.0_07"
_RUNJAVA="$JAVA_HOME"/bin/java
AMAXGS_HOME=`pwd`
# AMAXGS_HOME="E:/Work/AGLOCOAdmin"
CLASSES="$AMAXGS_HOME"/bin
LIB="$AMAXGS_HOME"/lib
MAINCLASS=com.amaxgs.addon.Agloco
MAINARGS=$1
CLASSPATH=.:"$CLASSES":"$LIB"/activation.jar:"$LIB"/activemq.jar:"$LIB"/cglib.jar:"$LIB"/commons-beanutils.jar:"$LIB"/commons-collections.jar:"$LIB"/commons-configuration.jar:"$LIB"/commons-dbcp.jar:"$LIB"/commons-digester.jar:"$LIB"/commons-lang.jar:"$LIB"/commons-logging.jar:"$LIB"/commons-pool.jar:"$LIB"/concurrent.jar:"$LIB"/counter-ejb.jar:"$LIB"/dom4j.jar:"$LIB"/easyconf.jar:"$LIB"/hibernate3.jar:"$LIB"/j2ee-management.jar:"$LIB"/javassist.jar:"$LIB"/javax.servlet.jar:"$LIB"/javax.servlet.jsp.jar:"$LIB"/jcr.jar:"$LIB"/jgroups-all.jar:"$LIB"/jms.jar:"$LIB"/jta.jar:"$LIB"/jxl2.4.2.jar:"$LIB"/lock-ejb.jar:"$LIB"/log4j.jar:"$LIB"/lucene.jar:"$LIB"/mail-ejb.jar:"$LIB"/mail.jar:"$LIB"/mysql-connector-java-3.1.12-bin.jar:"$LIB"/naming-factory-dbcp.jar:"$LIB"/naming-factory.jar:"$LIB"/naming-resources.jar:"$LIB"/oscache-2.3.2.jar:"$LIB"/portal-ejb.jar:"$LIB"/portal-kernel.jar:"$LIB"/portlet.jar:"$LIB"/spring.jar:"$LIB"/struts.jar:"$LIB"/trove.jar:"$LIB"/util-java.jar:"$LIB"/velocity.jar:
echo "Using CLASSPATH:  $CLASSPATH"
echo "Using MAINCLASS:  $MAINCLASS"
echo "Using JAVA:       $_RUNJAVA"
echo "Using JAVA_OPTS:  $JAVA_OPTS"
$_RUNJAVA $JAVA_OPTS -classpath $CLASSPATH $MAINCLASS $MAINARGS
