# ♕ BYU CS 240 Chess

This project demonstrates mastery of proper software design, client/server architecture, networking using HTTP and WebSocket, database persistence, unit testing, serialization, and security.

## 10k Architecture Overview

The application implements a multiplayer chess server and a command line chess client.

[![Sequence Diagram](10k-architecture.png)](https://sequencediagram.org/index.html#initialData=C4S2BsFMAIGEAtIGckCh0AcCGAnUBjEbAO2DnBElIEZVs8RCSzYKrgAmO3AorU6AGVIOAG4jUAEyzAsAIyxIYAERnzFkdKgrFIuaKlaUa0ALQA+ISPE4AXNABWAexDFoAcywBbTcLEizS1VZBSVbbVc9HGgnADNYiN19QzZSDkCrfztHFzdPH1Q-Gwzg9TDEqJj4iuSjdmoMopF7LywAaxgvJ3FC6wCLaFLQyHCdSriEseSm6NMBurT7AFcMaWAYOSdcSRTjTka+7NaO6C6emZK1YdHI-Qma6N6ss3nU4Gpl1ZkNrZwdhfeByy9hwyBA7mIT2KAyGGhuSWi9wuc0sAI49nyMG6ElQQA)

## Modules

The application has three modules.

- **Client**: The command line program used to play a game of chess over the network.
- **Server**: The command line program that listens for network requests from the client and manages users and games.
- **Shared**: Code that is used by both the client and the server. This includes the rules of chess and tracking the state of a game.

## Starter Code

As you create your chess application you will move through specific phases of development. This starts with implementing the moves of chess and finishes with sending game moves over the network between your client and server. You will start each phase by copying course provided [starter-code](starter-code/) for that phase into the source code of the project. Do not copy a phases' starter code before you are ready to begin work on that phase.

## IntelliJ Support

Open the project directory in IntelliJ in order to develop, run, and debug your code using an IDE.

## Maven Support

You can use the following commands to build, test, package, and run your code.

| Command                    | Description                                     |
| -------------------------- | ----------------------------------------------- |
| `mvn compile`              | Builds the code                                 |
| `mvn package`              | Run the tests and build an Uber jar file        |
| `mvn package -DskipTests`  | Build an Uber jar file                          |
| `mvn install`              | Installs the packages into the local repository |
| `mvn test`                 | Run all the tests                               |
| `mvn -pl shared test`      | Run all the shared tests                        |
| `mvn -pl client exec:java` | Build and run the client `Main`                 |
| `mvn -pl server exec:java` | Build and run the server `Main`                 |

These commands are configured by the `pom.xml` (Project Object Model) files. There is a POM file in the root of the project, and one in each of the modules. The root POM defines any global dependencies and references the module POM files.

## Running the program using Java

Once you have compiled your project into an uber jar, you can execute it with the following command.

```sh
java -jar client/target/client-jar-with-dependencies.jar

♕ 240 Chess Client: chess.ChessPiece@7852e922
```

Here is the diagram of the server:
https://sequencediagram.org/index.html?presentationMode=readOnly#initialData=IYYwLg9gTgBAwgGwJYFMB2YBQAHYUxIhK4YwDKKUAbpZgCbBjABGwAzijACKMvsqZM6MFACebXETQBzGAAYAdAE5M0qBACu2AMQAWAMwAOAExKQMbcmkALMMwQbOAJRTSkbEYyQQ0mRKlIAWgA+ckoaKAAuGABtAAUAeTIAFQBdGAB6DQ4oAB00AG8AImzKNGAAWxQiyJKigBoYItw2NgB3aDoa5oamlArgJARu6oBfTApqShgQ7l5WDmiAMyQ0OgBVHIAKUqhyqoBKTGYoFGAAaxhN6bakMGsYXf3OYART4DpRGBQAD3cwNiYHhMBacQKzSYRaLXKDA4ATcLTcGhfzCaK6OT6fLFKqtYDSarRIoAUSg6iiMFe70+MCY53QRXG6DoQPm-BmEMRFLQGgQCARU1gszhoOiIHeYBQMJ2OThR0hSNCIv4YolKAAghp7ltgFrrHKBREOSjkGiYMY5HJsSUcs9uhpekVdfdkhB6Whus7rIyhGtBGpNDpoMAZJxtAAxODErhwdUwAAyEDcvlRQVCCop8SSaUyHFa3jQ1qelUJdUazXY7U63WwPozMyVbMWMBWa2lxcOx3elxhMDQEDALc0axg0BgLSrUDoMFWIGgp3ArJB7ORYUF0R5fNHsBhcJgt3u+-UMnHlY6U8NSNmqbA6LkAEZrbi2PjCU1SeTohpynroEgAF4oF0TJ+sqHDGmuUJXLKvCXkKjbLs24pnJKmral6Br1quN7RBaVqFDaZQlvajpeq67rdGgKBtGh3ogSyqjqFo2hqCg6AWFYtisexibSJoWA3g2kGUNEMRcMS8bEskxLpBkeZsAW+Q6nq5HsasMDWGcdCUPKXJCWBKDLKsdC0cpLpuugRwnGcly0apaAzmwM5oFQrxICyBkQRmG68vyWHXqaGB3o+BHPq+NTvmS0Bfj+9x-oBwG+h5TZgpy64wLRcJwfpKXRNpCAoKhepmdY9m6YKEE4ealrYvR-pMTopzTto2CrJc8b-DAADiJaAoJaVQTEXVSbJ0glkpZEWQ56maR8OnZcKuUtsZpmTe6VndhlKlTY5zmucgyWIal6Zcj5fILQFAS3jAGIhTiKB4gSEUklFFLfl68VAT6zJLnw4Grt5W33FlWEIX9hnLWs6p8j1uJbEcnkA6d8AQHyKDgAWAA8sMoHCwQXSaV24TVBFjbiEUxAoVOpHVjGBtodAAOxKHIKByBYxL6HAUYAGzwGq3Uln4gWDgNImxIkKSjeNaAlfZzkaVpOnWmTKAAHLEbUFSiDjdZ6YtR1GVDxVrZZXY2UDpU7e4e1uYd4Necjm5+frl1mrdT4PS+T1Eh+0WPLF1ifYlP2I2LFKZbBoNzIbMDIYwKA41sqsGmHJ3pTje5bAeDxUW0MCqwAklw5VGthIvE-hxRF1wEX3sY+i6LTAbMaIKB8hA+eWEgNhgG3Hf5wAUhA6k48LV1CYD8TrDmGSqxN23ugrs3aVABzWtgCDAG3UBwKj0DdAA6gAEoX0mOjXNT1434zRwZRsmSbi9m9ZFyW-LNurPt7m-aCjvpc7Am8AK43QfJ7R6b4XqfgDh9KAAEvp1TTsJCOeoQb6zBqKSGdAk41w2hbHGMA6AQAen2Ac3w-geF-iucOZ0XYVXLkTUBVcihhR9pFaBrBpynAAI6OA8N9UCKV-5QUzrBV+lwXC8IepKacc4EBjhtlSLSXw6ToCAVVDEWJQpe3Cr7V60QlEfBURcBkdU75LS0AwSUOCSzF0aB2FAjRN7b0oHveRa91EgLwrVJK9V6ZqG3hxHuXEoCBMQGcWA6psCb0IF4Hw49hCT2RmJCSUkZKZDoMwBaGCVRxwKngaGCA4Tw08Yw7xhR6JAA