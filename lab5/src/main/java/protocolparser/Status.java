package protocolparser;

public enum Status {
    OK (200);

    private final int value;

    Status(int i) {
        value = i;
    }

    public int getValue() {
        return value;
    }
}
