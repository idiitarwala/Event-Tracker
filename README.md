# Event-Tracker Project

**Project Contributors: Idris Tarwala, Angela Xiang, Freeman Cheng, Christopher Jung, Ifaz, Austin Blackman, Tony He, Hasti Toossi.**

## Running Instructions

Pull from Github and open the project in Intellij. To run program first install the GSON library: in Intellij go to Project structure and install GSON library from Maven (com.google.code.gson:gson:2.8.5) as well as the apache library (org.apache.commons:commons-text:1.9) also from maven. 

Note: This can also be done using any other Java compatible IDE.

To run the program, go to PhaseTwoMain.java and run.

UML diagram is included as a PNG and as a UML file.

## Overview of Functionality

The program is an event creation system.

### Users can:

- Trial the system
  - Create events but do not save
  - View other's events but cannot interact with them
- Create an account
  - Choose to be admin or regular user
- Log in to their account
- Log out of their account
- Create an event based on existing templates
  - Change the privacy status of their event to private, public or friends only
  - Delete their events
  - Edit their event
- Attend events created and published by other users (and their own)
- Unattend events that they've attended
- View their own events
- View other user's published events
  - Separated into ones they're attending and ones they are not
- Edit Account info
  - Change their username
  - Change their password
  - Change their email
  - Delete their account
  - Change from regular user to admin
- View all other users in the system
  - Friend/Unfriend each user
  - Message any user
- Receive announcements from the admin
- Message admins
- At any point, type "back" to return to a previous menu or if they are on a menu, they can choose the go back option on the menu.
- Save everything/all data

### Admin Users Can

- edit templates
- create new templates
- suspend/unsuspend users
- suspend/unsuspend events
- send announcements to all users
- see a shared inbox for admins
- make another user an admin

### The System

- All information is saved into files (now including menus) that is human-readable
  - Files can be edited externally
- All read into the program automatically

## Design Decisions and Notes

- Admin users have all the same permissions as a regular user plus they can edit template names

  - They do not have to go back to a different "regular" account to do things

- Trial users can only create events and view events, but they cannot attend or save their event

- Events have custom fields that are determined by the template used to create it

- Templates are abstract and new templates can be added from files (without changing the code)

- Templates are a collection of fields. Each field can be specified independently and the number of fields is very flexible.

- Events are created based on a template but once the event is created, it no longer depends on the template

- When a user gets suspended, their events are automatically turned to private so other users can no longer see them.

  Once a user is unsuspended, they will be able to manually edit the privacy status of their events to change it to what they desired.

- We completely changed the way menus work in our system. Now they are no longer hard-coded and are read into the system from a file like all of our other entities.

- The way we determine what users see in the menus has also been changed so now we have a more dynamic version and what users see can easily be changed through a file as well.
