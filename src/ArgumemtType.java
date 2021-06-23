
enum ArgumentType {
    CREATE("create"), SEARCH("search"), DELETE("delete");
    private final String value;

    ArgumentType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
