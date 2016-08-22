package com.samcarlberg.extensions;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Tests for {@link ObjectExtensions}.
 */
public class ObjectExtensionsTest {

    @Test
    public void isPresent() throws Exception {
        String present = "present";
        assertTrue(ObjectExtensions.isPresent(present));
        assertFalse(ObjectExtensions.isPresent(null));
    }

    @Test
    public void ifPresent() throws Exception {
        boolean[] ran = {false};
        String present = "present";
        ObjectExtensions.ifPresent(null, v -> ran[0] = true);
        assertFalse(ran[0]);
        ObjectExtensions.ifPresent(present, v -> ran[0] = true);
        assertTrue(ran[0]);
    }

    @Test
    public void ifNotPresent() throws Exception {
        boolean[] ran = {false};
        String present = "present";
        ObjectExtensions.ifNotPresent(present, () -> ran[0] = true);
        assertFalse(ran[0]);
        ObjectExtensions.ifNotPresent(null, () -> ran[0] = true);
        assertTrue(ran[0]);
    }

    @Test
    public void filter() throws Exception {
        String foo = "foo";
        assertEquals("foo", ObjectExtensions.filter(foo, x -> true));
        assertNull(ObjectExtensions.filter(foo, x -> false));
        assertNull(ObjectExtensions.filter(null, x -> true));
        assertNull(ObjectExtensions.filter(null, x -> false));
    }

    @Test
    public void orElse() throws Exception {
        String foo = "foo";
        assertEquals("foo", ObjectExtensions.orElse(foo, "bar"));
        assertEquals("bar", ObjectExtensions.orElse(null, "bar"));
    }

    @Test
    public void orElseGet() throws Exception {
        String foo = "foo";
        assertEquals("foo", ObjectExtensions.orElseGet(foo, () -> "bar"));
        assertEquals("bar", ObjectExtensions.orElseGet(null, () -> "bar"));
    }

    @Test
    public void orElseThrow() throws Exception {
        String present = "present";
        ObjectExtensions.orElseThrow(present, () -> new AssertionError("Non-null value should not have thrown an exception"));
        try {
            ObjectExtensions.orElseThrow(null, () -> new IllegalArgumentException("expected"));
            fail("orElseThrow on null value should have thrown an exception");
        } catch (IllegalArgumentException expected) {
            // Should have happened
        }
    }

    @Test
    public void map() throws Exception {
        String foo = "FOO";
        assertEquals("foo", ObjectExtensions.map(foo, String::toLowerCase));
        assertNull(ObjectExtensions.map((String) null, String::toLowerCase));
    }

}