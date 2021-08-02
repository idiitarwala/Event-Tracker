package controllers;

import static controllers.EventController.*;

public enum Command {
    SIGN_UP("Sign Up"),
    LOGIN("Login"),
    TRIAL("Trial"),
    EXIT("Exit"),
    CREATE_EVENT("Create Event"),
    VIEW_ATTENDED("View Attended Events"),
    VIEW_UNATTENDED("View Not Attended Events"),
    VIEW_OWNED("View My Events"),
    VIEW_PUBLISHED("View Published Events"),
    EDIT_TEMPLATE("Edit Template"),
    ACCOUNT_MENU("Account Menu"),
    SAVE("Save"),
    LOG_OUT("Log Out"),
    CHANGE_USERNAME("Change Username"),
    CHANGE_PASSWORD("Change Password"),
    CHANGE_EMAIL("Change Email"),
    CHANGE_TO_ADMIN("Change User Type to Admin"),
    DELETE_ACCOUNT("Delete My Account"),
    GO_BACK("Go Back");

    private final String name;

    Command(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public ViewType getViewType() {
        switch (this) {
            case VIEW_ATTENDED:
                return ViewType.ATTENDING;
            case VIEW_UNATTENDED:
                return ViewType.NOT_ATTENDING;
            case VIEW_OWNED:
                return ViewType.OWNED;
            case VIEW_PUBLISHED:
                return ViewType.PUBLISHED;
            default:
                return null;
        }
    }
}
