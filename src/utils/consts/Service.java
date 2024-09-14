package utils.consts;

public enum Service {
    dictonary("GDictonary"),
    reciept("GReciept"),
    translator("GTranslator");

    public final String label;

    private Service(String label) {
        this.label = label;
    }

    @Override
    public String toString() {
        return "%s".formatted(label);
    }
}
