package io.student.rcc.jupiter.extension;

import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class TestMethodContextExtension implements BeforeEachCallback, AfterEachCallback {

    private static final ThreadLocal<ExtensionContext> store = new ThreadLocal<>();


    @Override
    public void afterEach(ExtensionContext context) {
        store.set(context);
    }

    @Override
    public void beforeEach(ExtensionContext context) {
        store.remove();
    }

    public static ExtensionContext context() {
        return store.get();
    }
}
