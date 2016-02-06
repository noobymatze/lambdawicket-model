package com.github.noobymatze.lambdawicket.model;

import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;

/**
 * ReadOnlyModel is an adapter base class for implementing models 
 * which have no detach logic and are read-only. It extends the 
 * {@link AbstractReadOnlyModel} with some powerful methods for
 * transforming the contained object in an ad-hoc manner.
 *
 * @author Matthias Metzger
 * @param <T> the model object
 */
public abstract class ReadOnlyModel<T> extends AbstractReadOnlyModel<T> {

    /**
     * Returns a ReadOnlyModel applying the given mapper to
     * the contained object, if it is not NULL.
     * 
     * @param <R> the new type of the contained object
     * @param mapper a mapper, to be applied to the contained object
     * @return a new ReadOnlyModel
     */
    public <R> ReadOnlyModel<R> map(SerializableFunction<T, R> mapper) {
        return new ReadOnlyModel<R>() {

            @Override
            public R getObject() {
                T object = ReadOnlyModel.this.getObject();
                if (object == null) {
                    return null;
                }
                else {
                    return mapper.apply(object);
                }
            }
        };
    }

    /**
     * Returns a ReadOnlyModel applying the given mapper to the contained
     * object, if it is not NULL.
     * 
     * @param <R> the new type of the contained object
     * @param mapper a mapper, to be applied to the contained object
     * @return a new ReadOnlyModel
     */
    public <R> ReadOnlyModel<R> flatMap(SerializableFunction<T, ReadOnlyModel<R>> mapper) {
        return new ReadOnlyModel<R>() {

            @Override
            public R getObject() {
                T object = ReadOnlyModel.this.getObject();
                if (object == null) {
                    return null;
                }
                else {
                    return mapper.apply(object).getObject();
                }
            }
        };
    }

    /**
     * Returns a ReadOnlyModel, returning either the contained object
     * or the given default value, depending on the nullness of the
     * contained object.
     * 
     * @param other a default value
     * @return a new ReadOnlyModel
     */
    public ReadOnlyModel<T> orElse(T other) {
        return new ReadOnlyModel<T>() {

            @Override
            public T getObject() {
                T object = ReadOnlyModel.this.getObject();
                if (object == null) {
                    return other;
                }
                else {
                    return object;
                }
            }
        };
    }

    /**
     * Returns a ReadOnlyModel, returning either the contained object
     * or invoking the given supplier to get a default value.
     * 
     * @param other a supplier to be used as a default
     * @return a new ReadOnlyModel
     */
    public ReadOnlyModel<T> orElseGet(SerializableSupplier<T> other) {
        return new ReadOnlyModel<T>() {

            @Override
            public T getObject() {
                T object = ReadOnlyModel.this.getObject();
                if (object == null) {
                    return other.get();
                }
                else {
                    return object;
                }
            }
        };
    }

    /**
     * Returns a ReadOnlyModel lifting the given object into the
     * Model.
     * 
     * @param <T> the type of the given object
     * @param object an object to be lifted into a ReadOnlyModel
     * @return a new ReadOnlyModel
     */
    public static <T> ReadOnlyModel<T> of(T object) {
        return new ReadOnlyModel<T>() {

            @Override
            public T getObject() {
                return object;
            }
        };
    }

    /**
     * Returns a ReadOnlyModel applying the given supplier to
     * get the object.
     * 
     * @param <T> the type of the given object
     * @param supplier a supplier, to be used to get a value
     * @return a new ReadOnlyModel
     */
    public static <T> ReadOnlyModel<T> of(SerializableSupplier<T> supplier) {
        return new ReadOnlyModel<T>() {

            @Override
            public T getObject() {
                return supplier.get();
            }
        };
    }

    /**
     * Returns a ReadOnlyModel using the getObject() method
     * of the given model.
     * 
     * @param <T> the type of the contained object
     * @param model a model, 
     * @return a new ReadOnlyModel
     */
    public static <T> ReadOnlyModel<T> of(IModel<T> model) {
        return of(model::getObject);
    }
    
}