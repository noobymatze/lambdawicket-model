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

## Should I care?

You don't need to. This is just a simple idea to make life a little easier. 
This comes with a caveat however. Every call to `map`, `flatMap` and friends
will introduce a new `ReadOnlyModel`, thus creating many unnecessary objects.

