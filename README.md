# Project for the course Distributed Systems
## Theme: Build a simple ticket management system using Java EE (Jakarta) and communication protocols UDP, AMQP, REST, and gRPC.
## Running the project:
### Starting the server
1. The server needs to be started first
1. Import the Project in IntelliJ/Eclipse, with root directory **ticketservice**
1. If your JDK version is less than 11 you need to set 'sourceCompatibility' in **build.gradle** to that number
1. Open TicketServerMain.java
    1. Eclipse:
        1. click Run > Run Configurations > Java Application > New Configuration
        1. The config will be generated, you only need to set "Program arguments" in the "Arguments" tab
        1. Example: localhost 1234
    2. IntelliJ
        1. click on green trinagle next to the main-method of TicketServerMain > create 'TicketServerMain.main()'
        2. the config is generated, you only need to set 'Program arguments'
1. Run/Debug the configuration

The _shared_ project is automatically build before the server is started.
Look at the dependencies block in the server/build.gradle.

### Starting the client
1. Create a Run-Configuration for Main.java with the necessary program arguments, the same way as for the server
    1. Program arguments example: udp localhost 1234
2. Run/Debug the configuration
3. For testing purposes the client configuration can be run multiple times from the same machine(at least in IntelliJ). When closing one client all of close.

The _shared_ project is automatically included in the dependencies of the client and server.
If you are interested inspect the `idistrsys/build.gradle` to see all the relevant settings, configurations and tasks.
