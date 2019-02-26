###########################
###########################


###########################
JAVA_HOME=/usr/lib/jvm/java-8-openjdk-amd64
JAVA_RUN=${JAVA_HOME}/bin/java
###########################


APP_HOME=''
CLASSPATH='';
###########################
APP_PACKAGE='ws-scim-1.0.1.jar'
MAIN_CLASS='com.wowsanta.scim.SCIMServer';
PID_FILE='scim.pid'
VM_OPTION='-Xms64m -Xmx512m'
LOG_MODE='INFO'
###########################
APP_OUT=''
APP_ERR=''
APP_RUN_MODE='DEBUG'

######################################################
function find_apphome(){ 
cd ..;
APP_HOME=${PWD}
cd bin;
}

function find_classpath(){ 
    for i in `ls ${APP_HOME}/libs/*.jar`
    do
        CLASSPATH=${CLASSPATH}:${i}
    done
}
######################################################

find_apphome;
find_classpath;


LOG_PATH="${APP_HOME}/logs"
CONF_PATH="${APP_HOME}/config"
LOG_CONF="${CONF_PATH}/logback.xml"
CLASSPATH="${APP_HOME}/dist/${APP_PACKAGE}${CLASSPATH}"

######################################################

echo "HOME=${HOME}"
echo "JAVA_HOME=${JAVA_HOME}"
echo "APP_HOME=${APP_HOME}"
echo "APP_PACKAGE=${APP_PACKAGE}"
echo "MAIN_CLASS=${MAIN_CLASS}"
echo "CLASSPATH=${CLASSPATH}"
echo "LOG_PATH=${LOG_PATH}"
echo "CONF_PATH=${CONF_PATH}"

######################################################

###########################
###########################
echo ${PWD}

nohup ${JAVA_RUN} \
    -classpath ${CLASSPATH} \
    ${VM_OPTION} \
    -Dlogback.path=${LOG_PATH} \
    -Dlogback.mode=${LOG_MODE} \
    -Dlogback.configurationFile=${LOG_CONF} \
    -Dscim.instance="dev_wowsanta_im" \
    ${MAIN_CLASS} &

echo "$!" > ${PID_FILE}
