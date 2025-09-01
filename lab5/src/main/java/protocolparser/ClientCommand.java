package protocolparser;

public enum ClientCommand {
    LOGIN ("login"),
    LIST ("list"),
    MESSAGE ("message"),
    LOGOUT ("logout"),
    PING ("ping"),
    REQUEST_CHAT ("request_chat"),
    ;

    private final String command;

    ClientCommand(String login) {
        this.command = login;
    }

    public static ClientCommand fromString(String command) {
        for (ClientCommand c : ClientCommand.values()) {
            if (c.getCommand().equals(command)) {
                return c;
            }
        }
        return null;
    }

    public String getCommand() {
        return command;
    }
}
