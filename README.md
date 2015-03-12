# NetworkedChatApplication
Table of Contents
***
# 1 Introduction #
### 1.1 Purpose ###
The purpose of the software design document is to describe the implementation of *Project 5 - Networked Card Game*. The Networked Card Game allows between to 2 - 6 players to chat while playing the Rummy Card game. The intended audience is someone who has taken an introductory course in computer science and object oriented programming.
### 1.2 Scope ###
This document describes the implementation details of how the Networked Card Game works. The goal of this project was to develop an event-driven GUI application which allows players to play the card game along with chatting with each other through Networked Chat Application. Help instructions explaining how to play the game and the game has reset functionality to allow the user to restart or play a new game.
### 1.3 Overview ###
This document is organized into seven different major sections, with some subsections. A table of contents is provided at the top to allow for easy navigation. This is a comprehensive document that will familiarize one with the design and workings of the software.
### 1.4 Reference Material ###
http://www.cs.uic.edu/bin/view/CS342/Proj45S14  
http://rummy.com/rummyrules.html
***
# 2 System Overview #
The software was designed using several components. Referring to the "Head Start: Design Patterns" book that was assigned to the class, we designed the software using Model View Controller software architecture. The software provides Server/Client platform for a group of users to play the game of Rummy and communicate.

***
# 3 System Architecture #
### 3.1 Architectural Design ###
The high level system is best described as *Model View Controller* (MVC). Using this paradigm, the game is split up into three different components.

* Model -- There are four models -- CardModel, CardPileModel, PlayerModel, GameModel, ServerModel and ClientModel. CardModel defines the attributes/properties of a specific card. CardPileModel provides the instance of the game in which card pieces can be manipulated, deck attributes/properties, hand properties, and the game’s state. ServerModel is used to communicate information between the clients. PlayerModel defines the player’s data such as their CardPile, melds. GameModel is used to track the current state of the game such as player’s turn, score, and winner/loser. ChatModel is used to communicate with the other clients.
* View -- BoardView is the view/GUI for the game. All GUI components are created here and it minimum logic. NetworkChatView is the view to chat between the connected clients. NetworkServerView will display server information and data that is passing between the client/server. Controllers are attached here to facilitate actions/data between the Model and View. 
* Controller -- Five controllers are provided. MenuController handles all menu interactions. ClientController is used to setup the interactions between the client. ServerController is used to handle the interactions between the client. CardDrawController handles all the adding and discarding of cards for the user. UserHandController handles all the dragging/dropping events to communicate the desired move from the view to the model. 

![image00.png](https://bitbucket.org/repo/yMeXGB/images/2825722703-image00.png)

### 3.2 Decomposition Description ###
The systems shown in the diagram from section 3.1 are the Model, View, and Controller.

The systems shown in the diagram from section 3.1 are the Model, View, and the Controller.
The Model system is divided into five substems: CardModel, CardPileModel, PlayerModel, GameModel and ChatModel. The CardModel keeps the information about each card. CardPileModel is a collection of cards and keeps track of the particulars of the game, such as the order of actions of the game (dealing, discarding). PlayerModel will track information about a player such as their hand and melds. GameModel will track the state of the game is it is played. ChatModel is used to communicate with the other clients while playing the game.

The View system has two subsystems i.e. BoardView and NetworkChatView.The system is used to display the graphical part of the game such as the cards deck, user hand, score and the menus. NetworkChatView is used to display the communication between the different clients available while the game is being played. The NetworkServerView will contain the GUI for the server that displays state information.

The Controller system has five subsystems: CardDrawController, UserHandController, ServerController, ClientController and MenuController. The Controller system is used to send commands to update the model based on the cations performed on the View. The MenuController handles all Menu operations through an event handler. The CardDrawController handles all the operations that are invoked by dragging and dropping the cards one at a time according to the user. The ServerController sets up the server for the communication between the clients while the game is being played. The UserHandController is used to determine if a particular pair of cards chosen by the user can be placed on to the display pile. The ClientController is used to setup the client interactions for the communications while the game is being played.

### 3.3 Design Rationale ###
From reading texts on GUI programs, MVC is a preferred software architecture. Using the architecture, it was very easy to get the behavior of card decks to be consistent and easily replicated to any number of players. This also simplifies the way the game is reset by simply creating a new instance of the CardModel, disposing the old board view and plugging the updated model into the View is a big advantage. Since the classes are decoupled from each other, the code is reduced and reusable. It also made the game easier to create since we could work on the components separately and test them as they became ready. The game was able to be divided into smaller sub systems concurrently.

We did not run into any critical issues with this design.

***
# 4 Data Design #
### 4.1 Data Description ###
**Data Structures**  
In the CardPileModel class, the deck is stored in an ArrayList deck of cards. The CardModel will use primitives to store its data. The PlayerModel model will store a CardPileModel and other primitives to track its state. The GameModel will store PlayerModels and CardPileModels for draw/discard. ChatModel will store connection info. The controllers themselves do not contain any data structures. They use references provided to them by the models and their operations.

**Data storage**  
Since Rummy is a game generated and played at runtime, there is no defined data storage for this program.

### 4.2 Data Dictionary ###
The data dictionary is provided in the form of Javadocs. This will be updated as new classes and methods are added to the project.  
[Javadoc for project5](http://acolon.bitbucket.org/project5/)
***
# 5 Component Design #
As mentioned above, we used Model View Controller to implement the user interface for our game.

We have 4 model classes: CardModel, CardPileModel, EvaluateHandModel and ChatModel. These classes create the Model part of MVS (Model View Controller). A Model provides the logic and state models of the data for the game. 

The algorithm for evaluating hand is going to sort the cards and using the rules of Rummy, it will arrange the cards in ways that form valid melds.
  
***
# 6 Human Interface Design #
### 6.1 Overview of User Interface ###
The user interface meets all the criteria listed by the specification. Upon starting the application, the user will be presented with a choice to either be a client or server. If the user selects server, they will not actively be participating in the game. The server will also be the initiator of the game by selecting “Start game” if there are not 6 players. This is done automatically if 6 players join. They will be able to see information about what clients are connected and what data is being passed. If the user selects client, they will be presented with a game board that shows all players in the game. They will also be presented with a chat window that will be used for communicating.

### 6.2 Screen Images ###
![image01.png](https://bitbucket.org/repo/yMeXGB/images/3385192725-image01.png)

### 6.3 Screen Objects and Actions ###
In the game board, the user will be able to interact with cards by dragging and dropping the cards to create melds. The game board will also indicate whose turn it is. There will also be a button to indicate if the user is done with their turn. There menu interaction that performs simple operations such as exit, help, about, etc. 

In the chat view, the user can send messages to all users or selected users by using control/command + click to select recipients.

In the server view, you can only view data being passed through and connect/disconnect.

***
# 7 Requirements Matrix #
Requirement  | Where it is met
------------- | -------------
For project 5, your team is to write a GUI based program using Java Swing that will allow multiple players to play a card game across the network. | All files
The Java Socket and ServerSocket classes must to used for the networking implementation. No other networking related classes can be used for this project without explicit permission from Prof. Troy. (Note: the answer will be "No." in every case I can currently think of.) | ServerModel.java, ClientModel.java
The program created must be able to act as either the server or the client for the network connection. A menu item (or items) is to determine whether the program is currently in "client mode" or "server mode". The menu is also to allow the "client mode" users to connect or disconnect from a server. By default, the program should start in "disconnected client mode". Thus allowing the user to first determine whether the program will be the server (i.e. host) for a game or whether the user will be a client who will join a specific server. | ServerModel.java, ClientModel.java, ChatModel.java, MenuController.java BoardView.java
Your program must contain the basics of the Chat Program created for Project 4 to allow the users to talk to each other while the game is being set up and played. | ServerModel.java, ClientModel.java, ChatModel.java
The card game to be played is the basic rules for Rummy as defined at http://rummy.com/rummyrules.html. | GameModel.java, CardPileModel.java, Card.java, PlayerModel.java
You program must also allow varying numbers of users to play. Rummy allows from 2 - 6 players | All files
The program is to handle a number of distinct stages during the playing of the games. A rough list of stages is given below. Your team may wish to come up with more stages if you think it is nessecary. 1. Setting one player to be the server. 2. Allowing multiple players to connect to the server. 3. Playing each hand until the ending conditions have been met. 4. Determine and announce the winner. 5. Disconnecting all players from the server. 6. Shutting down the server. Note that Step 3 requires multiple Hands to be played. Each Hand requires mutliple turns to be played. So a lot is going on in Step 3. If you wish, you may add Step 4.5 which allows the same players to play another hand, but this is NOT required.| ServerModel.java, ClientModel.java, BoardView.java, GameModel.java, PlayerModel.java