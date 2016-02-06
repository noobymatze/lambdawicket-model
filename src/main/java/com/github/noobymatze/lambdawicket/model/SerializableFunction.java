package com.github.noobymatze.lambdawicket.model;

import java.io.Serializable;
import java.util.function.Function;

/**
 * A Function implementing the Serializable Interface, so page serialization
 * doesn't throw a thousand errors.
 *
 * @author Matthias Metzger
 */
@FunctionalInterface
public interface SerializableFunction<T, R> extends Function<T, R>, Serializable {
    
}
