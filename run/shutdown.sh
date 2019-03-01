PID=$(< scim.pid)

echo kill scim process : $PID

kill -9 $PID

rm scim.pid