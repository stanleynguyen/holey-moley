# Holey Moley

![banner](/documentation/images/banner.png)

Whack-A-Mole is a popular arcade redemption game invented in
1976 by Aaron Fechter of Creative Engineering, Inc. We have
all played this game at least once when we go to arcade game 
shops with friends during our childhood. We attempted to 
make it "multiplayer" by recording the score and compare 
among our group of friends. What if we can make it truly 
"multiplayer" and real-time? Introducing __Holey Moley__, 
the true multiplayer Whack-A-Mole game implemented with 
modern technologies. Maybe it's time to reconnect with that 
long-lost friend over a game of __Holey Moley__? Play it 
[NOW](https://holeymoley.herokuapp.com/)

## Game Concept

Our group want to create something that is retro with a 
taste of modern technology. The aim of __Holey Moley__ is to 
bring nostalgic feeling with more fun from the classic game
of Whack-A-Mole. As such, the game can bridge the generation 
gap, bringing family members of different generations 
together to play a game that each one of them knows.

As for the above concept, we decided to implement a 
real-time online multi-player version of Whack-A-Mole that 
can be played with smart phones. Inside __Holey Moley__, you 
can battle other players in a Whack-A-Mole showdown where 
you can use items to affect your opponent. Experience and 
Gold can be earn through matches for acquiring more powerful 
items.

## Game Components

The different components that make up our awesome __Holey 
Moley__

### Game Login

![login1](/documentation/screenshots/login1.png)
![login2](/documentation/screenshots/login2.png)
![login3](/documentation/screenshots/login3.png)
![login4](/documentation/screenshots/login4.png)

This is where users can register for new accounts, or log in
using their credentials.

### Game Lobby

![lobby1](/documentation/screenshots/lobby1.png)
![lobby2](/documentation/screenshots/lobby2.png)
![lobby3](/documentation/screenshots/lobby3.png)
![lobby4](/documentation/screenshots/lobby4.png)

This is where all the purchases, equipment of items, display 
of user info are made. Users can also start games via the 
game menu.

### Gameplay

![game1](/documentation/screenshots/game1.png)
![game2](/documentation/screenshots/game2.png)
![game3](/documentation/screenshots/game3.png)
![game4](/documentation/screenshots/game4.png)

This is where the actual game takes place, objective of the 
game is score higher, or out-live your opponents 
during the game. Users can hit mole to get more points, and 
energy to use items. These items enhances their advantages 
or sabotages their opponents.

## System Requirements

### User Requirements

- Users need to be able to join and play with another player in real time
- Users need to transfer and receive data instantaneously during game
- Users need a system (profiles, items, adds-on, etc) that is just right in term of complexity to have fun but not too steep learning curve for new players
- Users want a game that is aesthetically pleasing and responsive

### Functional and Non-Functional Requirements

#### Funtional Requirements:
- *Concept*: Modern Whack-A-Mole game that is way more fun 
to play but still easy and intuitive enough as its classic 
counterpart
- *Functionality*: The game should have a players system 
with levels and gold for acquiring items to use in game. The 
gameplay should be able to handle real-time user IO.
- *IO Operations*: The game needs to be responsive and 
reliable
- *Logic*: The game should have logical and modularized 
implementation

#### Non-Functional Requirements:
- *Responsiveness*: The game should be very responsive to 
user interactions
- *Reliability*: The game should be reliable and clean of 
bugs
- *Availability*: The game should be easily accessible and 
available all the time
- *Security*: The game should be secure. Users' data must be 
protected. Third-party services should be trustable
- *Cost*: The game should cost little to nothing

### Use Cases and Use Case Diagrams

#### Registration & Login

__ID__ | HoleyMoley_registeringNewAccount
:---:|---
__Name__ | Registering Account
__Objective__ | Registering new account to start playing Holey Moley
__Pre-Condition__ | User must go to our landing page
__Post-Condition__ | __Success__</br>a. Account successfully created</br>b. User redirected to lobby</br>__Failure__</br>a. Duplicate account</br>b. Registration fail
__Actors__ | __Primary__</br>a. Player</br>__Secondary__</br>a. Backend Server
__Trigger__ | "Register" button on landing page 
__Normal Flow__ | 1. User click on "Register" button and the prompt appears</br>2. User fill in all required information (e.g. username, password)</br>3. Information checking by server</br>4. New account successfully registered
__Alternative Flow__ | __Scenario 1:__</br>1. User enter information that doesn't satisfy conditions (e.g. duplicate username, not secure password)</br>2. Visual Warning on the page</br>3. User re-enter the information</br>__Scenario 2:__</br>1. Server Error</br>2. User is signified to try again later
__Interacts With__ | Landing page, User checking use case
__Open Issues__ | 1. How to signified to admin in case of server error?</br>2. User experience of filling information

![Registration Diagram](/documentation/images/signupdiagram.png)

__ID__ | HoleyMoley_loginToGame
:---: | ---
__Name__ | Login Account
__Objective__ | Login to start playing Holey Moley 
__Pre-Conditions__ | __Success__</br>a. Account successfully logged in</br>b. User is redirected to lobby</br>__Failure:__</br>a. Wrong credentials</br>b. Login fails 
__Actors__ | __Primary__</br>a. Player</br>__Secondary__</br>a. Backend Server 
__Trigger__ | "Login" button on landing page 
__Normal Flow__ | 1. User click on "Login" button and the prompt appears</br>2. User fill in all requested information (e.g. username and password)</br>3. Information checking by server</br>4. Login successful
__Alternative Flow__ | __Scenario 1:__</br>1. Wrong credentials</br>2. Visual warning on the page</br>3. User re-enter the information</br>__Scenario 2:__</br>1. Server Error</br>2. User is signified to try again later
__Interacts With__ | Landing Page 
__Open Issues__ | 1. How to signify admin in case of server error?</br>2. User experience of filling up information

![Login Diagram](/documentation/images/logindiagram.png)

#### Lobby

__ID__ | HoleyMoley_buyingPowerUps
:---: | ---
__Name__ | Shopping
__Objective__ | Getting power-ups to use inside game
__Pre-Conditions__ | Player must have earned enough gold and have at least required level to buy the desired item 
__Post-Conditions__ | __Success__</br>a. Payment went throught and item added to inventory</br>__Failure:__</br>a. Failed to pay or server error
__Actors__ | __Primary__</br>a. Player
__Trigger__ | "Shop" button in Game Lobby
__Normal Flow__ | 1. Player goes inside shop by pressing "Shop" in lobby screen</br>2. Player looks for desired item by searching/filtering/sorting</br>3. Player presses on the item</br>Player receives item in Inventory
__Alternative Flow__ | __Scenario 1:__</br>1. Not enough gold or level to buy</br>2. Player is signified that he/she doesn't have enough gold</br>__Scenario 2:__</br>1. Server Error</br>2. Player is signified that the transaction didn't go through and asked to try again later
__Interacts With__ | User login
__Open Issues__ | 1. How to prevent user from hacking gold?</br>2. How to notify admin when there's server error?

![Shop Diagram](/documentation/images/shopdiagram.png)

#### Gameplay

__ID__ | HoleyMoley_gamePlay
:---: | ---
__Name__ | Playing Holey Moley
__Objective__ | Getting most points before time's up or outliving your opponent
__Pre-Conditions__ | There must be at least 2 players
__Post-Conditions__ | __Success__</br>a. Time's up and winner is determined</br>__Failure__</br>a. Player leaves game before time's up
__Actors__ | __Primary__</br>a. Players</br>__Secondary__</br>a. Backend server providing  real-time data
__Trigger__ | "Random Match" button on game mode choosing screen 
__Normal Flow__ | 1. Both players have equipped the desired power-ups and searching for random match</br>2. 2 Players will be matched against each other</br>3. Players can accumulate energy to use their power-ups (e.g. Spawn bombs to the opponent, freeze the opponent) to give them advantages</br>4. Time's up</br>Winner is determined. Experience and gold are earned
__Alternative Flow__ | __Scenario 1:__</br>1. Player quits during them game</br>2. The other player is declared winner</br>3. The game is discontinued and the game room destroyed</br>__Scenario 2:__</br>1. Server Error</br>2. No penalty to players</br>3. Users are signified to try again later
__Interacts With__ | User login, Joining game
__Open Issues__ | 1. How strong the connection needs to be?</br>2. What is the penalty for in-game quitting?

![Gameplay Diagram](/documentation/images/gameplaydiagram.png)

## System Design

Clients (Users on Web Browsers or Phone App) communicate 
with a server in 2 ways:
- *HTTP*: For any users' data related requests like 
login/logout, purchases of items, equip items
- *WebSocket*: For in game interactions like signaling score 
update, usage of items

### Overall Architecture and Technologies Stack

![Overall Model](/documentation/images/overallmodel.png)

The server is written in [NodeJS](https://nodejs.org/) using 
a minimal frameworks called 
[Express](https://expressjs.com/).
The database used is [MongoDB](https://www.mongodb.com/).
The web application is in HTML, CSS, and Javascript without 
any external library. The mobile application is written in 
Java with [Android Studio](https://developer.android.com/studio/index.html).
For  realtime communication, [Socket.io](https://socket.io/)
is used.

Below are the communication models:

![Communication Model](/documentation/images/communicationmodel.png)

### Web App Architecture

#### Code Structure (inside /server folder)

The web app is structure based on the Model/View/Controllers
pattern. Explanation for each folder is below:
- */index.js*: entry point to start the application
- */package.json*: records of dependencies and app scripts
- */models*: data models for all users' data
- */api*: implementation of all API endpoints
- */controllers*: implementation of views rendering
- */middlewares*: middlewares to ensure endpoints security
- */routes*: routing actual endpoints to all implementation
- */socket*: implementation of web socket
- */views*: templates for views
- */public*: all assets, stylesheets, and client-side
javascript files served to users
- */test*: all tests for application's components

#### NodeJS backend

Our choice of this backend technology is because of its 
asynchronous nature, which makes it able to handle tens of 
thousands of concurrent connections (this will be further 
elaborated in [Concurrency](#concurrency) Section). The fact 
that our game does not need much CPU-intensive computations 
but rather than I/O supports makes NodeJS good option.

#### MongoDB database

We chose MongoDB as out database because of the flexible 
nature of a NoSQL database. Since we don't have much time to 
carefully craft the models but rather do it on the go as we 
implement the app, this flexibility will help our database 
to be change-tolerant and save our time as we carry out
migrations

#### Socket.io 

> Socket.IO enables real-time bidirectional event-based 
> communication.
> It works on every platform, browser or device, focusing 
> equally on reliability and speed.
>
> From Microsoft Office, Yammer, Zendesk, Trello... to 
> hackathon winners and little startups.
> 
> One of the most powerful JavaScript frameworks on GitHub, 
> and most depended-upon npm module.
>
> -- *Socket.io statement*

Socket.io is a Node.js module, so it runs in-process with 
Node. On the client side, it provides a library clients use 
to connect to the server.

![socket.io lib](/documentation/images/socketio.png)

#### JSON Web Token

> In authentication, when the user successfully logs in 
> using their credentials, a JSON Web Token will be returned 
> and must be saved locally (typically in local or session 
> storage, but cookies can also be used), instead of the 
> traditional approach of creating a session in the server 
> and returning a cookie.
>
> This is a stateless authentication mechanism as the user 
> state is never saved in server memory. The server's 
> protected routes will check for a valid JWT in the 
> Authorization header, and if it's present, the user will 
> be allowed to access protected resources.
>
> -- *Wikipedia*

We chose JSON Web Token to implement our users' logging as 
it's a simple but secure, and easy to implement on other 
platforms (such as the Android Native App)

#### Front-end Technologies (HTML/CSS/JS)

We choose to not use any third-party library but rather 
browser native implementation because that way, our game will
be more independent of any front-end vendor that constantly 
change.

> In Frontend Development, the only constant is change
>
> -- *Anonymous Author*

#### Heroku

> Invest in apps, not ops. Heroku handles the hard stuff — 
> patching and upgrading, 24/7 ops and security, build 
> systems, failovers, and more — so your developers can stay 
> focused on building great apps.
>
> Choose Heroku for the same reasons disruptive startups do: 
> it’s the best platform for building with modern 
> architectures, innovating quickly, and scaling precisely 
> to meet demand.
>
> -- *Heroku statement*

We chose Heroku as our hosting because of its simplicity and 
zero-cost. Heroku saves us time from devOps as we can easily
deploy with a single-line command. Moreover, Heroku has 
supports for HTTPS out-of-the-box as we care for our users'
privacy and security.

### Android App Architecture

#### Code Structure (inside /WhackAMole folder)

__/app/src/main/java/zouyun/com/example/whackamole: contains 
all our java codes for the activities__

- *WelcomeActivity.java*: default page on open, for users to 
login or register
- *TabsActivity.java*: game lobby that implements fragments 
for each page
- *Game.java*: fragment that allows user to choose the game 
mode
- *Inventory.java*: fragment that allows user to view their 
inventory and equip skills
- *InventoryAdapter.java*: extends BaseAdapter to populate 
the inventory
- *Shop.java*: fragment that allows user to view the shop 
and buy skills
- *ShopAdapter.java*: extends BaseAdapter to populate the 
shop
- *Profile.java*: fragment that shows user their profile
- *Parser.java*: a class to handle parsing of JSONArrays to 
Java arrays
- *GameActivity.java*: activity where the user plays the game

__/app/src/main/res: contains our resources for the game__

- */drawable*: contains assets in .png and .xml
- */layout*: contains activity layouts
- */values*: contains colour, dimensions, styles and string 
values
- */mipmap-**: contains our app logo

#### Java

We chose to implement using vanilla Java with Android Studio 
rather than any third-party implementation like Unity as
it's simple but elegant, enabling us to achieve more 
understanding and customizations.

## System Testing

In this section, we will go through how to start the app, 
the tests implementation, and rationale for each test

### Web Application Testing

All our unit tests for web application is implemented using 
[Mocha](https://mochajs.org/) and 
[Chai](http://chaijs.com/).   
Mocha is a feature-rich JavaScript test framework running on 
Node.js and in the browser, making asynchronous testing 
simple and fun. Mocha tests run serially, allowing for 
flexible and accurate reporting, while mapping uncaught 
exceptions to the correct test cases.   
Chai is a BDD / TDD 
assertion library for Node and the browser that can be 
delightfully paired with any Javascript testing framework.

#### Getting Started

The web app can be started with our preconfigured npm 
scripts.  
__Note__: Before starting the server, there will be a few
environment variables that need to be set. This can be 
achieve in *dev* mode using a `.env` file base on the sample 
`.env.sample`

```bash
# script for starting server
npm start
# script for starting server in dev mode
npm run dev
# script for running test suite
npm run test
npm run test:watch # with watch mode
```

#### Test Cases

Since our architecture is modularized, we can easily carry 
out testing on each unit of implementations to cover all out 
implementations. For the server and web app, we tested the 
all our api endpoints, and socket implmentation. The below
screenshot is 100% test coverage

![test coverage](/documentation/screenshots/test.png)

### Android App Testing

*Insert whatever lib choice and explanation for it*

#### Getting Started

*Insert how to run the test*

#### Test Cases

*Insert the tests carried out with a screenshot of test 
coverage*

## Concurrency

### Javascript/NodeJS

Javascript offers an __asynchronous__ behavior by default 
that sets it apart from other programming languages. In 
asynchronous programs, you can have two lines of code (L1 
followed by L2), where L1 schedules some task to be run in 
the future, but L2 runs before that task completes. For 
example, in a restaurant, if you order a steak, and then I 
order a glass of water, I will likely receive my order 
first, since it typically doesn't take as much time to serve 
a glass of water as it does to prepare and serve a steak.   
Note that asynchronous does __not__ mean the same thing as 
concurrent or multi-threaded. JavaScript can have 
asynchronous code, but it is generally __single-threaded__.

![jsthreading](/documentation/images/jsthreading.png)

#### Threading

All operations in Javascript are thread-safe since Javascript
programs are single-threaded, making it impossible for any 2 
statement to execute at the same time.    
Nevertheless, other than your code, everything else runs in 
parallel which might cause problem at run-time. To tackle 
this issue, we embrace [Functional 
Programming](https://en.wikipedia.org/wiki/Functional_programming),
encapsulating our operations into blocks that does not 
affect the global environment (making them as "pure" as 
possible).

#### NodeJS's Non-Blocking I/O

This is the secret sauce to NodeJS's high performance 
despite the fact that NodeJS applications are 
single-threaded. Much like Javascript in the browser, NodeJS 
utilizes a fixed-size C++ thread pool behind the scene to 
handle all blocking I/O tasks.    
NodeJS server will accept requests from clients with a 
single-threaded event loop and dispatch the task to C++ 
worker threads for processing, freeing the main event loop 
to accept new requests.

![nodethreading](/documentation/images/nodejsthreading.png)

#### Browser Javascript Asynchronousity

Thanks to the asynchronous nature of Javascript, our 
browsers like Google Chrome, Mozilla Fire fox can easily 
executes multiple task while waiting for users' input but 
still are very responsive to new user inputs/requests. This 
is achieved by decoupling main thread listeners from worker 
threads which do all the processing.

![browserthreading](/documentation/images/browserthread.png)

### Java/Android

#### Android Gameplay Concurrency

The game requires multiple tasks to execute simultaneously, 
e.g. moving the mole up and down while informing server the 
special power employed. Since there are a lot of animations 
in fast pace, it is important to utilize resources 
effectively, hence multithreading is used to improve 
performance and avoid busy waiting. Multithreading is 
especially used to make sure that certain action is 
triggered only when certain requirement is fulfilled. By 
using wait() and notifyAll(), busy waiting is avoided and 
computational space is saved. For example, in our game 
design, we will need the user to accumulate certain amount 
of energy (mana) in order to enable a special power. Hence 
every time a mole is popping out, the energy level is 
checked and if it passes the basic requirement, notifyAll() 
will wake up every thread that is waiting on the lock, then 
subsequent action will be performed. The diagram can be seen 
below. 

![game threading](/documentation/images/multithread_diagram.png)

In the player thread, each method is synchronized to achieve 
thread-safety so that only one operation in the player 
thread is able to operate at a time. Since the operations in 
the player thread are mainly number calculation takes 
minimal time, starvation is unlikely to happen to affect the 
performance. 

#### Android UI Concurrency

The user interface from the login page to the game lobby are 
made concurrent using AsyncTask. AsyncTask enables proper 
and easy use of the UI thread, allowing us to perform 
background operations and publish results on the UI thread 
without having to manipulate threads or handlers. 

Information is fetched from the server in the background to 
allow users to log in or register, as well as to get data to 
populate the shop, inventory and user profile. Since the 
fetching operation is expected to be short (depending on 
network connection), AsyncTask would work well for our 
application. 

When the background computation finishes, the 
onPostExecute() method will run, and the UI elements will be 
updated. For instance, the game lobby activity will first 
check for the user’s information in the background. During 
this short period of checking, a loading spinner will be 
shown while the inventory’s grid view is hidden. Once the 
user data is received, the inventory’s grid view is populate 
and shown to the user.

![Android UI Concurrency](/documentation/images/androiduiconcurrency.png)
