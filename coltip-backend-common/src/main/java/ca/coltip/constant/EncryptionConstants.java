package ca.coltip.constant;

public interface EncryptionConstants {
    String TYPE_ENCRYPTION="AES/GCM/NoPadding";
    String STRING_SECRET_KEY_SPEC="AES";
    int GCM_TAG_LENGTH = 16;
    int AES_KEY_SIZE = 256;
    int GCM_IV_LENGTH = 12;
}
