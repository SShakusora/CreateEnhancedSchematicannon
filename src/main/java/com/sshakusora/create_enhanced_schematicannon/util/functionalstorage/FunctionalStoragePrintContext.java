package com.sshakusora.create_enhanced_schematicannon.util.functionalstorage;

public class FunctionalStoragePrintContext {
    private static final ThreadLocal<Boolean> SKIPPING_MISSING = ThreadLocal.withInitial(() -> false);

    public static void setSkippingMissing(boolean skippingMissing) {
        SKIPPING_MISSING.set(skippingMissing);
    }

    public static boolean isSkippingMissing() {
        return SKIPPING_MISSING.get();
    }

    public static void clear() {
        SKIPPING_MISSING.remove();
    }
}
