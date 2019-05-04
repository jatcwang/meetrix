Example Scala project with backend server based on [http4s](http://http4s.org/)

## Running

### The server

Start SBT interactive mode using `sbt`, then execute the command

```
~server/reStart
```

This will start the server application, and automatically restart the server whenever a file has changed

Press Enter will exit the watch mode, and use `server/reStop` to shutdown the server.

### The CLI (Command line interface) app

The CLI application is used to issue commands to the server (by making HTTP calls), which in turn queries the Meetup.com API.

To run the CLI app, run in SBT console:

```
cli/run somecommand --some=option
```

where any string passed after `cli/run` are passed to the CLI application as command line arguments


### Running and testing both the server and CLI

You can run two separate SBT console in two separate terminal windows. 
Use one to run the server app, and the other to run the CLI app

## Meetup API

https://www.meetup.com/meetup_api/docs/

Here are some example Meetup.com API endpoints you can use

```
/self/events?desc=true
    Show your past events
    
/self/groups
    Show the user's groups

/find/upcoming_events?text=scala&lat=51.52&lon=-0.1&self_groups=only
    Show/Search upcoming events, can restrict to list events from users groups only
    
/find/groups?radius=10&category=34&lat=51.52&lon=0.1
    Search for groups
```

Thus the command line interface should look something like:

```
meetrix myevents --past

meetrix mygroups

meetrix findgroups --radius=10 --category=tech --city=london --keyword=scala

meetrix findevents --radius=10 --category=tech --city=london --keyword=scala
```
