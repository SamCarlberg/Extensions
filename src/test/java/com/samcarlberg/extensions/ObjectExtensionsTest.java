package com.samcarlberg.extensions;

import lombok.experimental.ExtensionMethod;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Tests for {@link ObjectExtensions}.
 */
@ExtensionMethod(ObjectExtensions.class)
public class ObjectExtensionsTest {

    /**
     * Used to get generic inference working with null values.
     *
     * @param obj ignored
     * @param <T> the 'type' of the null value
     * @return a null value of the same 'type' as the given object
     */
    private <T> T nulled(T obj) {
        return null;
    }

    @Test
    public void isPresent() throws Exception {
        String present = "present";
        assertTrue(present.isPresent());
        assertFalse(nulled(present).isPresent());
    }

    @Test
    public void ifPresent() throws Exception {
        boolean[] ran = {false};
        String present = "present";
        nulled(present).ifPresent(v -> ran[0] = true);
        assertFalse(ran[0]);
        present.ifPresent(v -> ran[0] = true);
        assertTrue(ran[0]);
    }

    @Test
    public void orElse() throws Exception {
        String foo = "foo";
        assertEquals("foo", foo.orElse("bar"));
        assertEquals("bar", nulled(foo).orElse("bar"));
    }

    @Test
    public void orElseGet() throws Exception {
        String foo = "foo";
        assertEquals("foo", foo.orElseGet(() -> "bar"));
        assertEquals("bar", nulled(foo).orElseGet(() -> "bar"));
    }

    @Test
    public void orElseThrow() throws Exception {
        String present = "present";
        present.orElseThrow(() -> new AssertionError("Non-null value should not have thrown an exception"));
        try {
            nulled(present).orElseThrow(() -> new IllegalArgumentException("expected"));
            fail("orElseThrow on null value should have thrown an exception");
        } catch (IllegalArgumentException expected) {
            // Should have happened
        }
    }

    @Test
    public void map() throws Exception {
        String foo = "FOO";
        assertEquals("foo", foo.map(String::toLowerCase));
        assertNull(nulled(foo).map(s -> s.toLowerCase()));
    }

}