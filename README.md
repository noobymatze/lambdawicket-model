# Type safe AbstractReadOnlyModel building

This small lib provides a type safe and easy way to compose functions for building
ad-hoc AbstractReadOnlyModels in Wicket.

Suppose there is a `Person` and we want to display the name of that person: 

```java
Person person = // getFromDB, same code, if it's an IModel<Person>
add(new Label("name", of(person).map(Person::getName).orElse("n/a")));
```

The current standard way to do this is the following.

```java
Person person = // getFromDB
add(new Label("name", new AbstractReadOnlyModel<String>() {

    @Override
    public String getObject() {
        return Optional.ofNullable(person).
            map(Person::getName).
            orElse("n/a");
    }

}));
```

In the following I will outline some other possibilities for using the `ReadOnlyModel`. I will assume the following entities with corresponding getters, setters and constructors: 

```java
enum House implements SerializableFunction<String, String> {
  Gryffindor(name -> "Roooaaaar " + name),
  Hufflepuff(name -> " " + name),
  Ravenclaw(name -> "Craaah " + name),
  Slytherin(name -> "Whaazzzz up " + name + "?";
  
  private final Function<String, String> greet;
  
  private House(...) {...}
  
  @Override
  public String apply(String name) {
    return greet.apply(name);
  }
  
}

class Street { final String name; }
class Address { final Street street; }
class Person { final Address address; final String name; }
```

Now imagine we would like to dynamically greet the user starting with a 'H':

```java
Person person = // ...
IModel<House> house = Model.of(Gryffindor);
add(new DropDownChoice("house", house, Arrays.asList(House.values())));

IModel<String> name = ReadOnlyModel.of(person).
  map(Person::getName).
  filter(name -> name.startsWith("H")).
  apply(house). // This works, because the House implements a Function
  orElse("n/a");

add(new Label("name", name));
```

But this is not only applicable to `IModel<T>`. It also works for a Column, like the `LambdaColumn`.

```java
IColumn<Person, String>[] cols = new IColumn[]{
  new LambdaColumn<>(of("Street"), Person::getAddress).
    map(Address::getStreet).
    map(Street::getName).
    orElse("n/a"),
  new LambdaColumn<>(of("Name"), Person::getName).
    filter(name -> name.startsWith("H")).
    apply(house).
    orElse("n/a");
};
```

We could even provide an implementation for the `LoadableDetachableModel`, although we would
need to be a little more careful about the implementation, because contrary to the `ReadOnlyModel`
it needs to detach the contained objects, especially when doing a `.map`-chain of calls. Perhaps
the implementation of `map` for this case will prove to be more useful:

```java
public abstract class LambdaLoadableModel<T> extends LoadableDetachableModel<T> {

  public final <R> LambdaLoadableModel<R> map(SerializableFunction<T, R> mapper) {
    return new LambdaLoadableModel<R>() {
      @Override
      public R load() {
        T object = LambdaLoadableModel.this.getObject();
        return object != null : mapper.apply(object) : null;
      }
      
      @Override
      public void detach() {
        super.detach();
        LambdaLoadableModel.this.detach();
      }
    };
  }

}
```

There are also other classes, like `ListModel` or `MapModel`, where it might make sense
to implement a `map` function.

## Should I care?

You don't need to. This is just a simple idea to make life a little easier. 
This comes with a caveat however. Every call to `map`, `flatMap` and friends
will introduce a new `ReadOnlyModel`, thus creating many unnecessary objects.

