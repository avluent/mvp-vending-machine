# MVP Vending Machine Challenge
The challenge was to design an API for a vending machine, allowing users with a “seller” role to add, update or remove products, while users with a “buyer” role can deposit coins into the machine and make purchases.

## Hungry for Candy?
This vending machine is great! It contains loads of digital candy for you to consume with hardly any barriers!

## To get it running
You will need [Docker](https://docs.docker.com/get-docker/) and [docker-compose](https://docs.docker.com/compose/install/) for the composition to work. Having installed those, you simply type the following command:
```
docker-compose up -d
```
This will boot up a containerized Postgres Database (including a small DB admin tool in case you need it) and the app itself. 

### Using and Testing
As described in the requirements was that a [Postman](https://www.postman.com/downloads/) collection was supposed to be created. This project's collection is located under **project-docs > postman**.

## Stack Choice
For this project, I selected [Kotlin](https://kotlinlang.org/) with [Ktor](https://ktor.io/). For building I used [Gradle](https://gradle.org/) and I used Docker Compose for compositioning. Most of the libraries used are native Kotlin and/or Ktor libraries. I used Exposed as the database controller library and I used [Jackson](https://github.com/FasterXML/jackson) for JSON s11n and the MicroUtils [KotlinLogger](https://github.com/MicroUtils/kotlin-logging) for logging.

## Design Choices
I figured basic authentication would be easiest to implement, so I did. The Postman collection is already configured to use the basic auth and I added some credentials in order to use the collection immediately.

For added security, an SSL certificate is automatically generated whenever the server boots. This is a self-signed certificate of course, so when using the browser, the warning must be worked around accordingly.

### Application Layout
For the layout of the application, I chose a structure like this:
- **Application:** The main logic (controller)
- **Data:** The application's state layer (database and (mock) data)
- **Server:** The API endpoint routes, server configuration and logic
- **Common:** Shared resources (interfaces, classes etc.)

## Problems and Issues
With every development project there can be unexpected issues. So too were there some during this project.

### Major Issue: Test Code
A major issue arose when coding the tests. Setting up the tests was straight forward at first, so I decided to setup one test and leave the test code for last. This turned out to be a good decision.

On coding the first test, I hadn't implemented authentication yet. When I did, I received 401 unauthorized. The Ktor test suite didn't provide a standard solution for implementing a test function with authentication.

That wasn't pretty of course, but nothing that couldn't be worked around. So I did, you can see how in the test code. When I resumed testing I got strange errors from the database controller library Exposed. This library did not seem to work the Ktor test suite AT ALL!! Therefore I wasn't able to provide more test code (as was required by the challenge).

#### Minor Issue: Logout
The endpoint is implemented and I think I did everything according to the Ktor guide. However, the logout feature doesn't quite work as it should. I wasn't able to figure out the root cause.

#### Minor Issue: Virtualized Build Composition
I did try to seperate build from deployment, but found that docker-compose isn't very good at authoring compositions with regard to startup order. See [here](https://docs.docker.com/compose/startup-order/). I now basically perform the build and the runtime in a single container.

#### Minor Issue: Missing Safety Checks
The application logic still has some major blindspots, such as a minus number of products in stock. Also, when a user changes his/her role from seller to buyer, what happens to all the products that were registered to this seller? Can this user still sell them as a buyer?

# TL;DR
- Love Kotlin & Ktor
- Exposed, the DB Controller lib collided with the Ktor testing suite. Yikes!
- Docker Compose has it's shortcomings
- Security was handled using SSL and Basic Auth
- A Postman collection was created inside the **project-docs** folder
- Much candy was consumed during this project!