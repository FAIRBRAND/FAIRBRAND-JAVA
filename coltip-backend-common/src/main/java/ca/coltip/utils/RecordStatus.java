package ca.coltip.utils;

public enum RecordStatus {
    AVAILABLE(1),
    DELETED(2);

    private final int code;

    RecordStatus(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}