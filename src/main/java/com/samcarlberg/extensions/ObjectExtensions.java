package com.samcarlberg.extensions;

import lombok.NonNull;
import lombok.experimental.UtilityClass;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * {@link Optional Optional-style} utility methods designed to make it much easier to use null-checking in
 * a project.
 * <p>
 * For example, if trying to get a value from a map that may not be present:
 * <br>
 * With {@code Optional}:
 * <pre><code>
 *     class Foo {
 *         void foo(Map&lt;Foo, Bar&gt; map) {
 *             Optional.ofNullable(map.get(someKey))
 *                 .ifPresent(value -&gt; ...);
 *         }
 *     }
 * </code></pre>
 * <br>
 * And with {@code ObjectExtensions}:
 * <pre><code>
 *    {@literal @}ExtensionMethod(ObjectExtensions.class)
 *     class Foo {
 *         void foo(Map&lt;Foo, Bar&gt; map) {
 *             map.get(someKey).ifPresent(value -&gt; ...);
 *         }
 *     }
 * </code></pre>
 * <br>
 * Note that this class may be used without lombok's {@code @ExtensionMethod} annotation, but your code
 * will end up looking a lot like LISP, e.g.
 * <pre><code>
 *     int stringLength foo(String str) {
 *         return map(orElse(str, ""), String::length);
 *     }
 * </code></pre>
 *
 * @author Sam Carlberg
 * @see <a href="https://projectlombok.org/features/experimental/ExtensionMethod.html">lombok extension methods</a>
 */
@UtilityClass
public final class ObjectExtensions {

    /**
     * @param o the object to test
     * @return true if the object is <i>not null</i>, false if it is null
     * @see Optional#isPresent()
     */
    public static boolean isPresent(Object o) {
        return o != null;
    }

    /**
     * Performs the given operation iff the object is {@link #isPresent(Object) present}.
     *
     * @param object   the object to perform the operation on
     * @param consumer the operation to run iff the object is present
     * @param <T>      the type of the object
     * @see Optional#ifPresent(Consumer)
     */
    public static <T> void ifPresent(T object,
                                     @NonNull Consumer<? super T> consumer) {
        if (object != null) {
            consumer.accept(object);
        }
    }

    /**
     * Runs the given function if the object is not present
     *
     * @param object       the object to test
     * @param ifNotPresent the function to run if the object is not present
     * @param <T>          the type of the object
     */
    public static <T> void ifNotPresent(T object,
                                        @NonNull Runnable ifNotPresent) {
        if (object == null) {
            ifNotPresent.run();
        }
    }

    /**
     * Filters an object based on a predicate function.
     *
     * @param object          the object to test
     * @param testingFunction the function to use to test the object
     * @param <T>             the type of the object
     * @return {@code null} if {@code object} is not present, {@code object} if it passes the testing function,
     * or {@code null} if it is present but does not pass the testing function
     * @see Optional#filter(Predicate)
     */
    public static <T> T filter(T object,
                               @NonNull Predicate<? super T> testingFunction) {
        if (object == null) {
            return null;
        } else if (testingFunction.test(object)) {
            return object;
        } else {
            return null;
        }
    }

    /**
     * Returns the given object, or {@code defaultValue} if it is not present.
     *
     * @param object       the object to test
     * @param defaultValue the default value to use if {@code object} is null
     * @param <T>          the type of the object
     * @return {@code object} if it is present, otherwise {@code defaultValue}
     * @see Optional#orElse(Object)
     */
    public static <T> T orElse(T object,
                               @NonNull T defaultValue) {
        if (object != null) {
            return object;
        } else {
            return defaultValue;
        }
    }

    /**
     * Returns the given object, or the result of {@code defaultValueSupplier.get()} if it is not present.
     *
     * @param object               the object to test
     * @param defaultValueSupplier the supplier of the default value to use if {@code object} is null
     * @param <T>                  the type of the object
     * @return {@code object} if it is present, otherwise the result of {@code defaultValueSupplier.get()}
     * @see Optional#orElseGet(Supplier)
     */
    public static <T> T orElseGet(T object,
                                  @NonNull Supplier<? extends T> defaultValueSupplier) {
        if (object != null) {
            return object;
        } else {
            return defaultValueSupplier.get();
        }
    }

    /**
     * Returns the given object. Throws an exception X if it is not present.
     *
     * @param object            the object to test
     * @param exceptionSupplier a supplier for the exception to throw, e.g. {@code IllegalStateException::new}
     * @param <T>               the type of the object
     * @param <X>               the type of the exception to throw
     * @return the given object if it is present
     * @throws X if the given object is not present
     * @see Optional#orElseThrow(Supplier)
     */
    public static <T, X extends Throwable> T orElseThrow(T object,
                                                         @NonNull Supplier<? extends X> exceptionSupplier) throws X {
        if (object != null) {
            return object;
        } else {
            throw exceptionSupplier.get();
        }
    }

    /**
     * Maps the given object to a new value.
     * <br>
     * For example:
     * <pre><code>
     *     {@literal @}Nullable String s = getFoo().map(String::valueOf);
     * </code></pre>
     * <br>
     * Or if you want to be completely null-safe:
     * <pre><code>
     *     {@literal @}NonNull String s = getFoo().map(String::valueOf).orElse(DEFAULT_FOO_STRING);
     * </code></pre>
     * <br>
     * Note that since this method will return null if the {@code object} parameter is null, you'll need
     * to be careful with using this with primitives.
     * <pre><code>
     *     // BAD! Null pointer if fooString is null
     *     int i = fooString.map(String::length);
     *
     *     // Better - no null pointer
     *     int i = fooString.orElse(...).map(String::length);
     * </code></pre>
     *
     * @param object          the object to map
     * @param mappingFunction the function to use to map the given object
     * @param <T>             the type of the input object
     * @param <R>             the type of the result of the mapping function
     * @return the given object, or {@code null} if it is not present
     * @see Optional#map(Function)
     */
    public static <T, R> R map(T object,
                               @NonNull Function<? super T, ? extends R> mappingFunction) {
        if (object == null) {
            return null;
        }
        return mappingFunction.apply(object);
    }

}
