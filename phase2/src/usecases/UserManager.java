package usecases;

import entities.UserType;
import gateways.IGateway;
import entities.User;
import org.apache.commons.text.RandomStringGenerator;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static entities.UserType.*;

/**
 * Manages the Users in the system
 */
public class UserManager {
    // === Instance Variables ===
    private final List<User> userList;
    private final List<String> usernamesList;
    private final List<String> emailList;
    private final IGateway<User> gateway;
    private final RandomStringGenerator generator;
    // === Methods ===

    /**
     * Initializes a UserManager object
     * @param gateway A gateway object of type IGateway<User> used to load data
     */
    public UserManager(IGateway<User> gateway) {
        this.generator = new RandomStringGenerator.Builder().withinRange('A', 'Z').build();
        this.gateway = gateway;
        userList = gateway.getAllElements();
        usernamesList = new ArrayList<>();
        emailList = new ArrayList<>();
        for (User user :
                userList) {
            usernamesList.add(user.getUsername());
            emailList.add(user.getUserEmail());
        }

    }

    /**
     * Create a user in the program
     * @param username the User's username
     * @param password the User's password
     * @param userEmail the User's email
     * @param type the User's type. (R, A, T)
     */
    public void createUser(String username, String password, String userEmail, UserType type) {
            User newUser = new User(username, password, userEmail, type);
            userList.add(newUser);
            usernamesList.add(username);
            emailList.add(userEmail);
    }

    /**
     * Deletes a user from the program
     * @param username The username of the User to delete
     */
    public void deleteUser(String username) {
        User user = retrieveUser(username);
        // Remove all the User's Events
        List<String> ownedEvents = user.getOwnedEvents();
        while (!ownedEvents.isEmpty()){
            deleteEvent(username, ownedEvents.get(ownedEvents.size()-1));

        }
        // Remove all the User's info
        userList.remove(user);
        usernamesList.remove(user.getUsername());
        emailList.remove(user.getUserEmail());
    }

    public void suspendUser(String username) {
        suspendUser(username, null);
    }

    public void suspendUser(String username, Duration duration) {
        User user = retrieveUser(username);
        user.setSuspended(true);
        setSuspensionChangeDate(user, duration);
    }

    public void unsuspendUser(String username) {
        unsuspendUser(username, null);
    }

    public void unsuspendUser(String username, Duration duration) {
        User user = retrieveUser(username);
        user.setSuspended(false);
        setSuspensionChangeDate(user, duration);
    }

    public void updateUserSuspension(String username) {
        User user = retrieveUser(username);
        LocalDateTime endDate = user.getSuspensionChangeDate();
        if (LocalDateTime.now().isAfter(endDate)) {
            boolean suspended = user.isSuspended();
            user.setSuspended(!suspended);
            setSuspensionChangeDate(user, null);
        }
    }

    /**
     * Logs in a user by checking the inputted password against the User's username
     * @param username The username of the user attempting to log in
     * @param password The password the user has inputted
     * @return boolean Whether the login was successful
     */
    public boolean logIn(String username, String password) {
        // returns true if successfully logged in, false if otherwise (like if password is wrong)
        // updates the loggedIn boolean to True

        User userToLogin = retrieveUser(username);
        // If the user doesn't exist
        if (userToLogin == null){
            return false;
        }
        // If the password and username match
        if (userToLogin.getPassword().equals(password)){
            userToLogin.setLoggedIn(true);
            return true;
        }
        else {
            return false;
        }
    }

    /**
     * Logs out a User within the system
     * @param username The username of the user attempting to log out
     * @return boolean Whether the logout was successful
     */
    public boolean logOut(String username){
        User userToLogout = retrieveUser(username);
        // If the user doesn't exist
        if (userToLogout == null){
            return false;
        }
        else {
            userToLogout.setLoggedIn(false);
            return true;
        }
    }

    /**
     * Update a users password to the newPassword
     * @param username The username of the User whose password is to be updated
     * @param newPassword The users new password
     * @return Whether the password was updated successfully
     */
    public boolean updatePassword(String username, String newPassword){
        User user = retrieveUser(username);
        if (user.isLoggedIn()){
            user.setPassword(newPassword);
            return true;
        }
        else {
            return false;
        }
    }

    /**
     * Generates a random temp password for the user
     * @param username Username of user who requested temp password
     */
    public void generateTempPass(String username) {
        User user = retrieveUser(username);
        user.setTempPass(generator.generate(10, 20));
    }

    /**
     * Gets user's temp password
     * @param username Username of user who requested temp password
     * @return String corresponding to the temp password
     */
    public String getTempPass(String username) {
        return retrieveUser(username).getTempPass();
    }

    /**
     * Update a users username to the newUsername
     * @param username The username of the User whose username is to be updated
     * @param newUsername The users new username
     * @return Whether the username was updated successfully
     */
    public boolean updateUsername(String username, String newUsername) {
        User user = retrieveUser(username);
        if (user.isLoggedIn()){
            usernamesList.remove(user.getUsername()); // Remove old usernamesList
            usernamesList.add(newUsername); // Add new usernamesList
            user.setUsername(newUsername); // Set new username
            // TODO update friends list
            return true;
        }
        else {
            return false;
        }
    }

    /**
     * Updates a users email to the newEmail
     * @param username The username of the User whose email is to be updated
     * @param newEmail The users new email
     * @return Whether the email was updated successfully
     */
    public boolean updateEmail(String username, String newEmail){
        User user = retrieveUser(username);
        if (user.isLoggedIn()){
            emailList.remove(user.getUserEmail()); // remove old email from emailList
            emailList.add(newEmail); // Add new email to emailList
            user.setUserEmail(newEmail);
            return true;
        }
        else {
            return false;
        }
    }

    /**
     * Deletes the Event and unregister everyone who is attending
     * @param username the username of the User who wishes to delete an Event they had created
     * @param eventID the eventID of the event that the user will delete
     * @return whether the user has deleted the event successfully
     */
    public boolean deleteEvent(String username, String eventID){
        User user = retrieveUser(username);
        if (!user.getOwnedEvents().contains(eventID))
            return false;
        user.getOwnedEvents().remove(eventID);
        for (User attendee : userList) {
            unAttendEvent(attendee.getUsername(), eventID);
        }
        return true;
    }

    /**
     * unregister this user from an Event
     * @param username the username of the User who wishes to unregister from an Event corresponding to the given eventID
     * @param eventID the event ID number of the event the user wishes to no longer attend
     * @return whether the user has unregistered from the event successfully
     */
    public boolean unAttendEvent(String username, String eventID) {
        User user = retrieveUser(username);
        if (user.getAttendingEvents().contains(eventID)) {
            user.getAttendingEvents().remove(eventID);
            return true;
        }
        else {
            return false;
        }
    }

    /**
     * create an Event that is hosted by the given User
     * @param username the username of the User who is hosting the event
     * @param eventID the eventID that the user is hosting
     * @return whether the Event has been successfully created
     */
    public boolean createEvent(String username, String eventID){
        // Add this event to the list of events the user has created
        User user = retrieveUser(username);
        user.getOwnedEvents().add(eventID);
        return true;
    }


    /**
     * Register the user to attend the event
     * @param username The username of the User who wishes to attend the event
     * @param eventID The event ID of the Event that the user wishes to attend
     * @return True if the user was able to register for the event. False if the event has no available space.
     */
    public boolean attendEvent(String username, String eventID) {
        User user = retrieveUser(username);
        user.getAttendingEvents().add(eventID);
        return true;
    }

    /**
     * Retrieve the events IDS for the events that a user has created
     * @param username The username of the User whose created event's eventID's are to be retrieved
     * @return a list of event IDS for the events the user has created
     */
    public List<String> getCreatedEvents(String username) {
        // return the events the user has created
        User user = retrieveUser(username);
        return user.getOwnedEvents();
    }

    /**
     * Retrieve the events IDS for the events that a user is attending
     * @param username The username of the User whose attended event's event ID's are to be retrieved
     * @return a list of event IDS for the events the user is attending
     */
    public List<String> getAttendingEvents(String username) {
        // return the events the user is attending
        User user = retrieveUser(username);
        return user.getAttendingEvents();
    }

    /**
     * Retrieve all usernames that are registered in UserManager
     * @return a list of all usernames of every User in UserManager's userList
     */
    public List<String> getUsernameList() {
        return usernamesList;
    }


    /**
     * Get the user with the matching username
     * @param username the username to attempt to find a matching user with
     * @return User If the user was found, otherwise return a null object
     * */
    public User retrieveUser(String username){
        for (User user :
                userList) {
            if (user.getUsername().equals(username)){
                return user;
            }
        }
        // If the loop ends and no users with the matching username are found, return null
        return null;
    }

    /**
     * Checks if the username is present within the program
     * @param username The username to check exists
     * @return Whether the username is taken
     */
    public boolean usernameIsUnique(String username){
        return !usernamesList.contains(username);
    }

    /**
     * Checks if the email is present within the program
     * @param email The email to check exists
     * @return Whether the email is taken
     */
    public boolean emailIsUnique(String email) {
        return !emailList.contains(email);
    }

    /**
     * Retrieves the designated User's type
     * @param username The username of the User whose type will be retrieved
     * @return User.UserType The type of the User, R.A.T
     */
    public UserType retrieveUserType(String username){
        User user = retrieveUser(username);
        return user.getUserType();
    }

    /**
     * Updates the designated user to the Regular type
     * @param username The username of the User to update
     * @return boolean If the change was successful
     */
    public boolean changeUserTypeToRegular(String username){
        User user = retrieveUser(username);
        user.setUserType(REGULAR);
        return true;
    }

    /**
     * Updates the designated user to the Admin type
     * @param username The username of the User to update
     * @return boolean If the change was successful
     */
    public boolean changeUserTypeToAdmin(String username){
        User user = retrieveUser(username);
        user.setUserType(ADMIN);
        return true;
    }

    public void saveAllUsers() {
        gateway.saveAllElements(userList);
    }

    public List<String> getFriends(String username) {
        User user = retrieveUser(username);
        return user.getFriends();
    }

    public void addFriend(String first, String second) {
        addToFriendsList(first, second);
        addToFriendsList(second, first);
    }

    public void removeFriend(String first, String second) {
        removeFromFriendsList(first, second);
        removeFromFriendsList(second, first);
    }

    private void addToFriendsList(String username, String friend) {
        User user = retrieveUser(username);
        List<String> friends = user.getFriends();
        if (!friends.contains(friend))
            friends.add(friend);
    }

    private void removeFromFriendsList(String username, String friend) {
        User user = retrieveUser(username);
        user.getFriends().remove(friend);
    }

    private void setSuspensionChangeDate(User user, Duration duration) {
        if (duration == null) {
            user.setSuspensionChangeDate(null);
        } else {
            LocalDateTime endDate = LocalDateTime.now().plus(duration);
            user.setSuspensionChangeDate(endDate);
        }
    }
}
