# Тест-кейсы для микросервиса 
    
baseURL: https://qa-internship.avito.com

api1: <ins> /api/1 </ins>

api2: <ins> /api/2 </ins>
_________
### Тест-кейсы:
- [Создание объявления позитивные](https://github.com/Sumeruk/Zinchenko_Avito_QA-trainee-assignment-spring-2026/blob/main/TESTCASES.md#%D1%81%D0%BE%D0%B7%D0%B4%D0%B0%D0%BD%D0%B8%D0%B5-%D0%BE%D0%B1%D1%8A%D1%8F%D0%B2%D0%BB%D0%B5%D0%BD%D0%B8%D1%8F-%D0%BF%D0%BE%D0%B7%D0%B8%D1%82%D0%B8%D0%B2%D0%BD%D1%8B%D0%B5)
- [Создание объявления негативные](https://github.com/Sumeruk/Zinchenko_Avito_QA-trainee-assignment-spring-2026/blob/main/TESTCASES.md#%D1%81%D0%BE%D0%B7%D0%B4%D0%B0%D0%BD%D0%B8%D0%B5-%D0%BE%D0%B1%D1%8A%D1%8F%D0%B2%D0%BB%D0%B5%D0%BD%D0%B8%D1%8F-%D0%BD%D0%B5%D0%B3%D0%B0%D1%82%D0%B8%D0%B2%D0%BD%D1%8B%D0%B5)

### Создание объявления позитивные

<ins>**ID: TAS-001**</ins>

Название: Создание объявления позитивное

Предусловие: нет

Шаги:
- отправить POST {{baseUrl}}/{{api1}}/item c телом
```
{
  "sellerID": 111111,
  "name": "Т001",
  "price": 15000,
  "statistics":
    {
      "likes": 2,
      "viewCount": 3,
      "contacts": 1
    }
}
```

Ожидаемый результат:
- status Code 200
- тело ответа:
```
{
  "id": "UUID-объявления",
  "sellerId": 111111,
  "name": "Т001",
  "price": 15000,
  "statistics": {
    "likes": 2,
    "viewCount": 3,
    "contacts": 1
  },
  "createdAt": "yyyy-MM-dd HH:mm:ss.SSSSSS Z Z"
}
```

Постусловие:
- отправить DELETE {{baseUrl}}/{{api2}}/item/{UUID-объявления}

<ins>**ID: TAS-002**</ins> 

Название: Создание объявления позитивное c отрицательным sellerId

Описание: поскольку в постановке сказано, что sellerId - целое число, оно может быть отрицательным

Предусловие: нет

Шаги:
- отправить POST {{baseUrl}}/{{api1}}/item c телом
```
{
  "sellerID": -111111,
  "name": "Т001",
  "price": 15000,
  "statistics":
    {
      "likes": 0,
      "viewCount": 0,
      "contacts": 0
    }
}
```

Ожидаемый результат:
- status Code 200
- тело ответа:
```
{
  "id": "UUID-объявления",
  "sellerId": -111111,
  "name": "Т001",
  "price": 15000,
  "statistics": {
    "likes": 0,
    "viewCount": 0,
    "contacts": 0
  },
  "createdAt": "yyyy-MM-dd HH:mm:ss.SSSSSS Z Z"
}
```

Постусловие:
- отправить DELETE {{baseUrl}}/{{api2}}/item/{UUID-объявления}

<ins>**ID: TAS-003**</ins> 

Название: Создание объявления позитивное c нулевым sellerId

Описание: поскольку в постановке сказано, что sellerId - целое число, оно может быть нулем

Предусловие: нет

Шаги:
- отправить POST {{baseUrl}}/{{api1}}/item c телом
```
{
  "sellerID": 0,
  "name": "Т001",
  "price": 15000,
  "statistics":
    {
      "likes": 0,
      "viewCount": 0,
      "contacts": 0
    }
}
```

Ожидаемый результат:
- status Code 200
- тело ответа:
```
{
  "id": "UUID-объявления",
  "sellerId": 0,
  "name": "Т001",
  "price": 15000,
  "statistics": {
    "likes": 0,
    "viewCount": 0,
    "contacts": 0
  },
  "createdAt": "yyyy-MM-dd HH:mm:ss.SSSSSS Z Z"
}
```

Постусловие:
- отправить DELETE {{baseUrl}}/{{api2}}/item/{UUID-объявления}


### Создание объявления негативные
<ins>**ID: TAS-004**</ins>

Название: Создание объявления с некорректными sellerId

Описание: поскольку sellerId должен быть целым числом, некорректными sellerId могут быть дробные значения, строки

Предусловие: нет

Шаги:
- отправить POST {{baseUrl}}/{{api1}}/item c телом
```
{
  "sellerID": 11111.3,
  "name": "Т002",
  "price": 15000,
  "statistics":
    {
      "likes": 0,
      "viewCount": 0,
      "contacts": 0
    }
}
```
- отправить POST {{baseUrl}}/{{api1}}/item c телом
```
{
  "sellerID": -11111.3,
  "name": "Т002",
  "price": 15000,
  "statistics":
    {
      "likes": 0,
      "viewCount": 0,
      "contacts": 0
    }
}
```
- отправить POST {{baseUrl}}/{{api1}}/item c телом
```
{
  "sellerID": "111111",
  "name": "Т002",
  "price": 15000,
  "statistics":
    {
      "likes": 0,
      "viewCount": 0,
      "contacts": 0
    }
}
```
- отправить POST {{baseUrl}}/{{api1}}/item c телом
```
{
  "sellerID": "abc",
  "name": "Т002",
  "price": 15000,
  "statistics":
    {
      "likes": 0,
      "viewCount": 0,
      "contacts": 0
    }
}
```

Ожидаемый результат:
- status Code 400
- тело ответа:
```
{
    "result": {
        "message": "не передано тело объявления",
        "messages": {}
    },
    "status": "400"
}
```

<ins>**ID: TAS-005**</ins>

Название: Создание объявления с невалидным sellerId

Предусловие: нет

Шаги:
- отправить POST {{baseUrl}}/{{api1}}/item c телом
```
{
  "sellerID": 01,
  "name": "Т002",
  "price": 15000,
  "statistics":
    {
      "likes": 0,
      "viewCount": 0,
      "contacts": 0
    }
}
```
- отправить POST {{baseUrl}}/{{api1}}/item c телом
```
{
  "sellerID": -0,
  "name": "Т002",
  "price": 15000,
  "statistics":
    {
      "likes": 0,
      "viewCount": 0,
      "contacts": 0
    }
}
```
- отправить POST {{baseUrl}}/{{api1}}/item c телом
```
{
  "sellerID": asdf,
  "name": "Т002",
  "price": 15000,
  "statistics":
    {
      "likes": 0,
      "viewCount": 0,
      "contacts": 0
    }
}
```

Постусловие: нет

Ожидаемый результат:
- status Code 400
- тело ответа:
```
{
    "result": {
        "message": "не передан объект - объявление",
        "messages": {}
    },
    "status": "400"
}
```

Постусловие: нет

<ins>**ID: TAS-006**</ins> 

Название: Создание объявления негативное с пустым name

Описание: объявление не может быть с пустым названием

Предусловие: нет

Шаги:
- отправить POST {{baseUrl}}/{{api1}}/item c телом
```
{
  "sellerID": 123443,
  "name": "",
  "price": 15000,
  "statistics":
    {
      "likes": 0,
      "viewCount": 0,
      "contacts": 0
    }
}
```

Ожидаемый результат:
- status Code 400
- тело ответа:
```
{
    "result": {
        "message": "поле name обязательно",
        "messages": {}
    },
    "status": "400"
}
```

<ins>**ID: TAS-007**</ins> 

Название: Создание объявления негативное с отрицательным price

Предусловие: нет

Шаги:
- отправить POST {{baseUrl}}/{{api1}}/item c телом
```
{
  "sellerID": 123443,
  "name": "TOS",
  "price": -15000,
  "statistics":
    {
      "likes": 0,
      "viewCount": 0,
      "contacts": 0
    }
}
```

Ожидаемый результат:
- status Code 400
- тело ответа:
```
{
    "result": {
        "message": "поле price обязательно",
        "messages": {}
    },
    "status": "400"
}
```
<ins>**ID: TAS-007**</ins> 

Название: Создание объявления негативное с нулевым price

Предусловие: нет

Шаги:
- отправить POST {{baseUrl}}/{{api1}}/item c телом
```
{
  "sellerID": 123443,
  "name": "TOS",
  "price": 0,
  "statistics":
    {
      "likes": 0,
      "viewCount": 0,
      "contacts": 0
    }
}
```

Ожидаемый результат:
- status Code 400
- тело ответа:
```
{
    "result": {
        "message": "поле price обязательно",
        "messages": {}
    },
    "status": "400"
}
```


