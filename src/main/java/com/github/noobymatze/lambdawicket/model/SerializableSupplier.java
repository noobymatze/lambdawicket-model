package com.github.noobymatze.lambdawicket.model;

import java.io.Serializable;
import java.util.function.Supplier;

/**
 *
 * @author Matthias Metzger
 */
@FunctionalInterface
public interface SerializableSupplier<T> extends Supplier<T>, Serializable {
    
}
