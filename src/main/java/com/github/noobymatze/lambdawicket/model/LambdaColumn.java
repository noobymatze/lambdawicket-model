package com.github.noobymatze.lambdawicket.model;

import org.apache.wicket.extensions.markup.html.repeater.data.grid.ICellPopulator;
import org.apache.wicket.extensions.markup.html.repeater.data.table.AbstractColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.export.IExportableColumn;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;

/**
 * The LambdaColumn allows for a simple way to build Columns for a table.
 *
 * @author Matthias Metzger
 * @param <T>
 *          the type of the model in a particular row
 * @param <R>
 *          the type of the resulting value shown in this column
 * @param <S>
 *          the type of the sort property
 */
public class LambdaColumn<T, R, S> extends AbstractColumn<T, S> implements IExportableColumn<T, S> {

    private final SerializableFunction<T, R> getter;

    /**
     * Constructs a new LambdaColumn with the given displayModel and a
     * getter.
     * 
     * @param displayModel the column header
     * @param getter a function transforming the rowModel into the type of
     * this column
     */
    public LambdaColumn(IModel<String> displayModel, SerializableFunction<T, R> getter) {
        super(displayModel);

        this.getter = getter;
    }

    /**
     * Constructs a new LambdaColumn with the given displayModel, a sortProperty
     * and a getter.
     * 
     * @param displayModel the column header
     * @param sortProperty the property for sorting a table based on this column
     * @param getter a function transforming the rowModel into the type of
     * this column.
     */
    public LambdaColumn(IModel<String> displayModel, S sortProperty, SerializableFunction<T, R> getter) {
        super(displayModel, sortProperty);

        this.getter = getter;
    }

    /**
     * Returns a LambdaColumn applying the given mapper after applying the
     * getter.
     * 
     * @param <U> the new type of this column
     * @param mapper a function to be applied to the result of this getter
     * @return a new LambdaColumn
     */
    public <U> LambdaColumn<T, U, S> map(SerializableFunction<R, U> mapper) {
        return new LambdaColumn<>(
            getDisplayModel(),
            getSortProperty(),
            (SerializableFunction<T, U>) getter.andThen(mapper)
        );
    }

    /**
     * Returns a LambdaColumn applying the contained function of the mapper
     * IModel. This can be used to 
     * 
     * @param <U> the new type of this column
     * @param mapper a Model containing a function to be applied to the result 
     * of this getter.
     * @return a new LambdaColumn
     */
    public <U> LambdaColumn<T, U, S> apply(IModel<SerializableFunction<R, U>> mapper) {
        return new LambdaColumn<>(
            getDisplayModel(),
            getSortProperty(),
            (SerializableFunction<T, U>) getter.andThen(r -> mapper.getObject().apply(r))
        );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void populateItem(Item<ICellPopulator<T>> cellItem, String componentId, IModel<T> rowModel) {
        cellItem.add(new Label(componentId, getDataModel(rowModel)));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public IModel<?> getDataModel(IModel<T> rowModel) {
        // In Wicket 8 this might be done with
        // () -> getter.apply(rowModel.getObject());
        return ReadOnlyModel.of(rowModel).map(getter);
    }

}
