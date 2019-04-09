Example Scala project with backend server based on [http4s](http://http4s.org/)

## Running

Start SBT interactive mode using `sbt`, then execute the command

```
~server/reStart
```

This will start the server application, and automatically restart the server whenever a file has changed

Press Enter will exit the watch mode, and use `server/reStop` to shutdown the server.
