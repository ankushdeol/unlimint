#! /bin/bash

#
# set the path to java and run tests (all of them by default)
# add arguments if necessary
#

echo

print_usage () {
    echo "Usage: $0 [OPTIONS]"
    echo 'Compile the given modules and update them in the dev tomcat'
    echo "Examples: "
    echo "       $0"
    echo "       $0 --tests New60Features"
    echo "       $0 --tests New60Features.testAuthoringClearAll"
    echo
    echo 'This uses parameters from PropertyFile.properties, namely: '
    echo '   jdk11'
}

case "$1" in
    '--help' | '-h')
        print_usage
        exit
        ;;
esac

CONFIG_FILE="PropertyFile.properties"

config_read_file() {
  (grep -E "^${2}=" -m 1 "${1}" 2>/dev/null || echo "VAR=__UNDEFINED__") \
    | head -n 1 | cut -d '=' -f 2-
}

config_get() {
  val="$(config_read_file "$CONFIG_FILE" "${1}")"
  if [ "${val}" = "__UNDEFINED__" ]; then
    val="$2"
  fi
  printf -- "%s" "${val}"
}

jdkLocation=$(config_get jdk11)

# check if jdkLocation exist
if [[ ! -d "$jdkLocation/bin" ]]; then
# if [[ ! -d "/c/tmp" ]]; then
  echo -e "\e[31mThis script requires the variable jdk11 to be defined in PropertyFile.properties and to point"
  echo -e "\e[31mto a valid java 11 jdk."
  echo -e "\e[0m"
  exit
fi

PATH=$jdkLocation/bin:$PATH
export PATH

echo ./gradlew build test "$@"
./gradlew build test "$@"
