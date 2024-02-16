package org.assabet.aztechs157;

import java.util.function.Supplier;

public class DynamicString {
    private final Supplier<String> stringSupplier;

    public DynamicString(final Supplier<String> stringSupplier) {
        this.stringSupplier = stringSupplier;
    }

    public String getCurrent() {
        return stringSupplier.get();
    }

}
