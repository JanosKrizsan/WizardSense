# Wizard Sense

![Magical Forces](https://i.imgur.com/mZGL1lf.png)

## What is this?

It is a web-application which provides Wizards, Warlocks, Mages, Witches, and any other magic users with enchanted items of all sorts. To purchase items, one would require to use the basic spell of Telepathy.

## Tech Used

- Java
- Java Servlets
- Jetty
- Dao Structure
- JavaScript
- Thymeleaf 

## Features

- User handling with registry, login, authentication
- Able to browse, sort items
- Persistent cart system
- No loss of cart contents upon logout
- Checkout, with e-mail sent
- Adding and saving address information
- Clever loading of already in-progress checkout
- Captivating style with cool theme

## Usage / Testing

As the application is not fully finished and still requires work and updates, usage depends on the developer who resuses code or ideas from it.

To test the application, please clone the "dev" branch and add the project as Maven. Based on the "pom.xml" file most IDEs should be able to install relevant dependencies as well. If this is not possible, you require the following ones:

- Jetty
- Java Servlets
- Thymeleaf

[How to Maven Dependency Plugin](http://maven.apache.org/plugins/maven-dependency-plugin/index.html)
[Install Maven](https://maven.apache.org/install.html)

After installing Maven Dependency Plugin, you could get your dependencies via:

`mvn dependency:copy-dependencies`

To download dependency sources:

`mvn dependency:copy-dependencies -Dclassifier=sources`

After this, the .jar files should be in the "target/dependencies" folder.

## Code Example

Simple Generic DAO
```
public interface GenericQueriesDao<T> {

    void add(T object) throws SQLException;
    T find(int id) throws SQLException;
    void remove(int id) throws SQLException;

    List<T> getAll() throws SQLException;
    void removeAll() throws SQLException;

}
```

Adding or removing items from cart
```
private void addOrRemoveProduct(HttpServletRequest req) throws SQLException {
        List<String> headers = Collections.list(req.getParameterNames());

        HttpSession session = req.getSession();


        int userId = (int)session.getAttribute("userID");

        Cart cart = cartDataStore.getCartByUserId(userId);

        if (headers.contains("increase")) {
            int prodId = Integer.parseInt(req.getParameter("increase"));

            cartDataStore.increaseOrDecreaseQuantity(cart, prodId, true);
        }
        else if (headers.contains("decrease")) {
            int prodId = Integer.parseInt(req.getParameter("decrease"));

            if(cartDataStore.getCartProductQuantity(cart, prodId) <= 1) {
                cartDataStore.clearProductFromCart(prodId);
            } else {
                cartDataStore.increaseOrDecreaseQuantity(cart, prodId, false);
            }
        }
    }
```

## Miscellaneous

Contributors:
- [Mark Ungvari](https://github.com/MarkUngvari)
- [Janos Krizsan](https://github.com/JanosKrizsan)

Please Note:<br>
This is a Codecool-related / educational project, thus as the end of our sprint came, we had to work on other things.
The most up-to-date code can be found on branches "dev" and "design".

[Img Source](https://wallhaven.cc/w/odjxw9)

