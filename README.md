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
https://sequencediagram.org/index.html?presentationMode=readOnly#initialData=IYYwLg9gTgBAwgGwJYFMB2YBQAHYUxIhK4YwDKKUAbpTngUSWDABLBoAmCtu+hx7ZhWqEUdPo0EwAIsDDAAgiBAoAzqswc5wAEbBVKGBx2ZM6MFACeq3ETQBzGAAYAdAE5M9qBACu2AMQALADMABwATG4gMP7I9gAWYDoIPoYASij2SKoWckgQaJiIqKQAtAB85JQ0UABcMADaAAoA8mQAKgC6MAD0PgZQADpoAN4ARP2UaMAAtihjtRNjADQwY7jqAO7QHAvrK2soM8BICHvzAL6YwjUwFazsXJT18Y-cGVk5lBkAjqk5AAooCg-mowABKTBsTjcWD3G6ierAz5gSgAj7ZVFQX7-CHXaqiO6VWTyJQqdT1ewoMAAVQGAMmUGmc0hJMUyjUqiJRh09QAYkhODA6ZQ2TAdJYYIzmWIdMDgABrYUDGCbJBgeJSgYymDABDyjiSlAAD0xGjZZM5d3hBJU9RFUDZ+JEKmtlWhTzqMA13k2MAU+pQwEN7UV6AAosaVNgCAUoW9KG6qtRnt74r7-YHg5ZQwqI1GUDH8oUbonSvdiuZ6oEnMFhuM5upgFSFmtw1BvF69QbJfI82gxld0BxMBaOeokwi7TA0D4EAhnVRCfcx+TVPUQPLUQ6GQM2aztJaJ-djPUFBwOMrRdpF8viYfx+uYJug6iFD4NQDgB-4vvRw+125U9-Qvf0fydKdDHLd0Ey9DEvmxNQ5ywD1YUnapU3GaVZnmRYfAOMZvw1doIH7PYiPiQdFzLCtkCrGBwicJx6wmbUcL2fDVkIn8SLIxYKKo4dTC8XwAmgdgqRiPk4HDaQ4AUGAABkICyQpKzKSpSy9Zo2i6XoDHUYsWOwuYOIIjZVG2KBdkWbAqK07lUNTV4YRQZTVJxMEgRBXFIScuFNNtFB6gQFTBQBdzBU8nJIUg7lV05SlqR3EyUAPUlHyA3kYAFIUHTFCUtSmHDMDlIMlQdGcIGYAAzXwhWgGALKsy9BRAaBgXAf8MsA6DkyXadZ3nGBGvy7RVXVTVNm8Bwmv0SydlvV0+v8+ofQgP0aWmH9oCQAAvFAOEjaNY0Kfz0JTL11s27aNV2g6joLIs4wcvr1LAasnAARhYxtVGbXC2w7aB6h8W74nuw7BM4br2V6m0XWCq9HRvOKVwAxLny3FB30-Ci-wS49KmA89LzQFA-Vx39UaCpNVqUsK0AyVRkPjVy4QRmp6iwtjTLwgiKN49A9nJzYqfsjCOcqd76kY5jRlY4q+aWLjBdI4XFlF8WhxhzxvD8fwvBQdAYjiRIjZN9zfCwd7uS0+oGmkcNFPDdpw26HoDNUIy0C-Hj1bQGBBRgeIgw4ShYslxzYJeBMrY-aKwG80EYrZz07aCkKVOtiLs4TnywUjxH4oxikYCpMAqb94iA-SuGrRPbLcsvKmnTKxUwJr-sg65QUqD1JAR0Jrk+sg+ohoXNH7np67hXByHHpO4s07Q0fJbW9MNrniiF+OwtTuoqX4DojBPp+hW-oB1sxnbTtQfnqB9qhnWh9LkfOcRTvqfkJaoPvHrMbh24G+H81d4hCzQHXI8I9ibZWkCgYBhhW402LitGODN7DW2ZshGAAIjgxksH5WCF0uYwBGFcV6tESgfQYkxesL9hL6wCMCS8-hsCCiVIpTEMAADiOENC205qmBoPDXYe3sDhYYYCIFB0DqHYMEdD7R3ZrHVyXCch8MbInZOvkV5lkCojEKmJNFqAisY-hici4DT-jIN+SVK6gLVv2KBmVG78kFC3cCN525KipjI7Isj+7IFfgA48H9BpzknrTNBKi0wZi2jvR+D097PTOsQtel0N7xIfk-Re+9l6UOlifGhNZz4Nk5FfeoN9gZejBok3J0MQn1zCQY6xZ4vE-zRv-ZpT5kA5ADAgExqgAQuN6rA+oPDqS6mGiYp0w8SGfzgBAecKBwDFgADyzO0OUX+dN0HqLAEM7BCAULpKEV6cYEjGytgaC4O5nQKFRzesU2WdCFZXLUDcu5LgHlmF1iJA2HAADsbgnAoCcDEcMwQ4AyQAGzwGxrwkqgjWnCNaB0cRkjfZOJNsHeR4coDghYh8gAcuxRYMxLAmIlpdZRnpVFPDgNjEx2jgQpzxOdcJSMXxyBQCYgETLXx8pwpY3Z6NQlPgrlXHFkDYbQKyu4oUyCf4+K-v43uaAgmDzla4rl49Iliunug2eCSdpJMOikg+nLzlZK3qau65q8mpKUc86hZ9foVJbFU2+IMpQ5Ieo0nV8NWmf2VcAMV3ToEbmZThAEHyCZvwVTAAAkmgAYzBRZIrmHMxNGS2lZpQGKAEaoNQzgpuXHCybpBWOXEa2JgreUsqQicvRR97ZkLGB8qtrYvrhGCIER5tLXX0TlsSyt0ge19oHX8kcetRL+EsAg0KfpYhIASGARd84t4ACkIDBxMUUYpGdMmNCaDSPSPQPlSJlbIkOYcI4sWwAgYAi6oBLNClAPYAB1Fgya3YES7RO2ovb+2DtuHW+lIcEy7sFE29lOjC6tuPfmgAVnutA-KYMYZFQXVOXTbESvsdK-2zig0N3GTlDxX827yl8SR3FGqtVNPlXmz+E9DUwViSa-1FqnoH0KcfN1MBSkeqbF6oGd8-X1IDQw+ZrHpxhtwe1DAJw01FSZDhGty1I2PnsfywDozyM8kVZeLZKraMFqMBANQVVmAmkxGRlp-U2MGqnpxyDs8ABCwZE6WoKU8qh9Eazy3KWJwG1TJN6EvGy3Ega5N6oLTR8qMBE6HWfMsxqATuxh17GGEs0SIOwltZTLMIY8t+ZegFopQmax1gvp68LPquyldy2RBh+Hh6g2wFoVE+nx2rFSqsJ9L7KDvugIZomxnhTdd5Yl8aJbNSZuG6+jjDxYlYabSzE5uD8EbqIezEhmEwM0WqyOt55CZ2MPnV4F9ps13mygLdxAQZYAKGwE+wgeQ4wov6sIp2Ls3Ye2MC69zRWoOuWe3gVluGOXpJDdOEA3A8AjIjQRnpG4kdQAdMMibMCpvwMQdMhAyMILRJ02uDHL2hko7kxRgn1JDB6mJ2Z8NHW7HPkx1THHjm8fAXp6iIn1GUHWL2fWzHxzmB4JmAQ-b6c81HZdYF0+tCQsMKAA