## Api do Santander feita durante o Curso da Dio

## Diagrama de Classes

```mermaid
classDiagram
    class User {
        - String name
        - Account account
        - Feature[] features
        - Card card
        - News[] news
    }

    class Account {
        - String number
        - String agency
        - Number balance
        - Number limit
    }

    class Feature {
        - String icon
        - String description
    }

    class Card {
        - String number
        - Number limit
    }

    class News {
        - String icon
        - String description
    }

    User "1" *--> "1" Account
    User "1"  *--> "N" Feature
    User  "1" *-->  "1" Card
    User  "1" *-->  "N" News
```

## Testes no postman urls 

Criar novo usuario
localhost:8080/users

Buscar usuario pelo id
localhost:8080/users/id

