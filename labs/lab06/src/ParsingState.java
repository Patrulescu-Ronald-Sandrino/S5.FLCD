public enum ParsingState {
    NORMAL("q"),
    BACK("b"),
    FINAL("f"),
    ERROR("e");

    private final String value;

    ParsingState(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return this.value;
    }
}
