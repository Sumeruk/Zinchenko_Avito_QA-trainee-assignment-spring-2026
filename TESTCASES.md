# Тест-кейсы для микросервиса 
    
baseURL: https://qa-internship.avito.com

api1: <ins> /api/1 </ins>

api2: <ins> /api/2 </ins>
_________
### Тест-кейсы:
- [Создание объявления (позитивные)](#Создание-объявления-(позитивные):)
- [Создание объявления (негативные)](#Создание-объявления-(негативные):)

# Создание объявления (позитивные):

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
    "status": "Сохранили объявление - {UUID объявления}"
}
```

Постусловие:
- отправить DELETE {{baseUrl}}/{{api2}}/item/{UUID объявления}

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
    "status": "Сохранили объявление - {UUID объявления}"
}
```

Постусловие:
- отправить DELETE {{baseUrl}}/{{api2}}/item/{UUID объявления}

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
    "status": "Сохранили объявление - {UUID объявления}"
}
```

Постусловие:
- отправить DELETE {{baseUrl}}/{{api2}}/item/{UUID объявления}

### Создание объявления (негативные)
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
        "message": "",
        "messages": {}
    },
    "status": "не передано тело объявления"
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
        "message": "",
        "messages": {}
    },
    "status": "не передан объект - объявление"
}
```

Постусловие: нет

<ins>**ID: TAS-006**</ins> 

Название: Создание объявления негативное с некорректным name

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
    "status": "Сохранили объявление - {UUID объявления}"
}
```



