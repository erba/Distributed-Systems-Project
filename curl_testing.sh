# This script is an infinite loop, which sends a create/accept/reject REST-request in sequence
# used to test concurrency, i.e test the correct functioning of the GUI while this script is calling the server continuously

# works in git-bash (for windows)

# TODO test concurrent create & modify => 1) create new ticket 2) set ticket 1 to ACCEPTED 3) set ticket 1 to REJECTED
    # 1 script: createNewTicket loop
    # 2 script: modify loop
    # 3 script: getAllTickets loop
    # 4 script: search loop

while true
do
printf "\n\n==== NEW CREATE/ACCEPT/REJECT SEQUENCE" &&\
NewId=$(curl -s -H 'Content-Type:application/json' -H 'Accept:application/xml' -d '{"id":1,"reporter":"bash_curl","topic":"","description":"","type":"ENHANCEMENT","priority":"MINOR","status":"NEW"}' -w '\n%{http_code}' -X POST http://localhost:1337/tickets  | sed 's/.*<id>//;s/<\/id>.*//;2d;3d') &&\
printf "\n== CREATE new ticket, server-given ID: $NewId\n" &&\
AcceptTicket="{\"id\":$NewId,\"reporter\":\"bash_curl\",\"topic\":\"\",\"description\":\"\",\"type\":\"ENHANCEMENT\",\"priority\":\"MINOR\",\"status\":\"ACCEPTED\"}" &&\
printf "== ACCEPT ticket, server-response:\n" &&\
curl -s -H 'Content-Type:application/json' -H 'Accept:application/xml' -d $AcceptTicket -w '\n%{http_code}' -X PUT http://localhost:1337/tickets/$NewId &&\
RejectTicket="{\"id\":$NewId,\"reporter\":\"bash_curl\",\"topic\":\"\",\"description\":\"\",\"type\":\"ENHANCEMENT\",\"priority\":\"MINOR\",\"status\":\"REJECTED\"}" &&\
printf "\n== REJECT ticket, server-response:\n" &&\
curl -s -H 'Content-Type:application/json' -H 'Accept:application/xml' -d $RejectTicket -w '\n%{http_code}' -X PUT http://localhost:1337/tickets/$NewId
done

# curl -s http://localhost:1337/tickets
