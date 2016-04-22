package com.github.noobymatze.lambdawicket.model;

import java.io.Serializable;
import java.util.function.BiFunction;

/**
 *
 * @author Matthias Metzger, SOLVIT GmbH, mme@solvit.de
 */
@FunctionalInterface
public interface SerializableBiFunction<T, U, R> extends BiFunction<T, U, R>, Serializable {

}
