
### Баг-репорты
- [Баги по созданию объявлений](https://github.com/Sumeruk/Zinchenko_Avito_QA-trainee-assignment-spring-2026/blob/main/BUGS.md#%D0%B1%D0%B0%D0%B3%D0%B8-%D0%BF%D0%BE-%D1%81%D0%BE%D0%B7%D0%B4%D0%B0%D0%BD%D0%B8%D1%8E-%D0%BE%D0%B1%D1%8A%D1%8F%D0%B2%D0%BB%D0%B5%D0%BD%D0%B8%D0%B9)
- [Баги по удалению](https://github.com/Sumeruk/Zinchenko_Avito_QA-trainee-assignment-spring-2026/blob/main/BUGS.md#%D0%B1%D0%B0%D0%B3%D0%B8-%D0%BF%D0%BE-%D1%83%D0%B4%D0%B0%D0%BB%D0%B5%D0%BD%D0%B8%D1%8E)
- [Баги по статистике](https://github.com/Sumeruk/Zinchenko_Avito_QA-trainee-assignment-spring-2026/blob/main/BUGS.md#%D0%B1%D0%B0%D0%B3%D0%B8-%D0%BF%D0%BE-%D1%81%D1%82%D0%B0%D1%82%D0%B8%D1%81%D1%82%D0%B8%D0%BA%D0%B5)
- [Баги по корнер-кейсам](https://github.com/Sumeruk/Zinchenko_Avito_QA-trainee-assignment-spring-2026/blob/main/BUGS.md#%D0%B1%D0%B0%D0%B3%D0%B8-%D0%BF%D0%BE-%D0%BA%D0%BE%D1%80%D0%BD%D0%B5%D1%80-%D0%BA%D0%B5%D0%B9%D1%81%D0%B0%D0%BC)

# Баги по созданию объявлений

TAS-101: Некорректная схема ответа на запрос создания объявления
--
Серьезность - высокая

Приоритет - высокий

Исполнитель: ответственный разработчик

Шаги:
- POST /api/1/item с телом
```
{
  "sellerID": 5935,
  "name": "Synergistic Iron Watch",
  "price": 9,
  "statistics": {
    "likes": 3,
    "viewCount": 3,
    "contacts": 3
  }
}
```

Ожидаемое поведение:
- ответ вида:
```
{
  "id": "<string>",
  "sellerId": "<integer>",
  "name": "<string>",
  "price": "<integer>",
  "statistics": {
    "likes": "<integer>",
    "viewCount": "<integer>",
    "contacts": "<integer>"
  },
  "createdAt": "<string>"
}
```

Фактическое поведение:
```
{"status":"Сохранили объявление - <UUID-объявления>"}
```

Стенд: 
https://qa-internship.avito.com

TAS-102: Не сохранилось объявление с нулевым sellerId
--
Серьезность - высокая

Приоритет - низкий

Исполнитель: ответственный разработчик

Шаги:
- POST /api/1/item с телом
```
{
  "sellerID": 0,
  "name": "Synergistic Iron Watch",
  "price": 9,
  "statistics": {
    "likes": 3,
    "viewCount": 3,
    "contacts": 3
  }
}
```

Ожидаемое поведение:
- статус 200
- ответ вида:
```
{
  "id": "<string>",
  "sellerId": "<integer>",
  "name": "<string>",
  "price": "<integer>",
  "statistics": {
    "likes": "<integer>",
    "viewCount": "<integer>",
    "contacts": "<integer>"
  },
  "createdAt": "<string>"
}
```

Фактическое поведение:
- статус 400
```
{"result":{"message":"поле sellerID обязательно","messages":{}},"status":"400"}
```

Стенд: 
https://qa-internship.avito.com

TAS-103: Не сохранилось объявление с нулевыми полями likes, viewCount, contacts
--
Серьезность - высокая

Приоритет - средний

Исполнитель: ответственный разработчик

Описание: должна быть возможность создать объявление с нулевыми likes, viewCount, contacts, 
поскольку объявление могут не добавить в избранное или ни разу не посмотреть или ни разу по нему не связаться

Шаги:
- POST /api/1/item с телом
```
{
  "sellerID": 5935,
  "name": "Synergistic Iron Watch",
  "price": 9,
  "statistics": {
    "likes": 0,
    "viewCount": 0,
    "contacts": 0
  }
}
```
- POST /api/1/item с телом
```
{
  "sellerID": 5935,
  "name": "Synergistic Iron Watch",
  "price": 9,
  "statistics": {
    "likes": 3,
    "viewCount": 3,
    "contacts": 0
  }
}
```
- POST /api/1/item с телом
```
{
  "sellerID": 5935,
  "name": "Synergistic Iron Watch",
  "price": 9,
  "statistics": {
    "likes": 3,
    "viewCount": 0,
    "contacts": 3
  }
}
```
- POST /api/1/item с телом
```
{
  "sellerID": 5935,
  "name": "Synergistic Iron Watch",
  "price": 9,
  "statistics": {
    "likes": 3,
    "viewCount": 3,
    "contacts": 0
  }
}
```
Ожидаемое поведение:
- статус каждого запроса 200
- ответ на каждый запрос вида:
```
{
  "id": "<string>",
  "sellerId": "<integer>",
  "name": "<string>",
  "price": "<integer>",
  "statistics": {
    "likes": "<integer>",
    "viewCount": "<integer>",
    "contacts": "<integer>"
  },
  "createdAt": "<string>"
}
```

Фактическое поведение:
- статус 400
```
{"result":{"message":"поле likes обязательно","messages":{}},"status":"400"}
```
```
{"result":{"message":"поле viewCount обязательно","messages":{}},"status":"400"}
```
```
{"result":{"message":"поле contacts обязательно","messages":{}},"status":"400"}
```

Стенд: 
https://qa-internship.avito.com

TAS-104: Ошибка в ответе при создании объявления с некорректным sellerId
--
Серьезность - низкая

Приоритет - низкий

Исполнитель: ответственный разработчик

Шаги:
- POST /api/1/item с телом
```
{
  "sellerID": <Строка или дробное число>,
  "name": "Synergistic Iron Watch",
  "price": 9,
  "statistics": {
    "likes": 3,
    "viewCount": 3,
    "contacts": 3
  }
}
```

Ожидаемое поведение:
- статус 400
- ответ вида:
```
{
  "result": {
    "messages": {},
    "message": "<сообщение о некорректности sellerId>"
  },
  "status": 400
}
```

Фактическое поведение:
- статус 400
```
{"result":{"message":"","messages":{}},"status":"не передано тело объявления"}
```

Стенд: 
https://qa-internship.avito.com

TAS-105: Ошибка в ответе при создании объявления с невалидным sellerId
--
Серьезность - низкая

Приоритет - низкий

Исполнитель: ответственный разработчик

Шаги:
- POST /api/1/item с телом
```
{
  "sellerID": abc01-1,
  "name": "Synergistic Iron Watch",
  "price": 9,
  "statistics": {
    "likes": 3,
    "viewCount": 3,
    "contacts": 3
  }
}
```

Ожидаемое поведение:
- статус 400
- ответ вида:
```
{
  "result": {
    "messages": {},
    "message": "<сообщение о некорректности sellerId>"
  },
  "status": 400
}
```

Фактическое поведение:
- статус 400
```
{"result":{"message":"","messages":{}},"status":"не передан объект - объявление"}
```

Стенд: 
https://qa-internship.avito.com

TAS-106: Ошибка в ответе при создании объявления с числом в name
--
Серьезность - низкая

Приоритет - низкий

Исполнитель: ответственный разработчик

Шаги:
- POST /api/1/item с телом
```
{
  "sellerID": 481164,
  "name": 10,
  "price": 9,
  "statistics": {
    "likes": 3,
    "viewCount": 3,
    "contacts": 3
  }
}
```

Ожидаемое поведение:
- статус 400
- ответ вида:
```
{
  "result": {
    "messages": {},
    "message": "<сообщение о некорректности name>" 
  },
  "status": 400
}
```

Фактическое поведение:
- статус 400
```
{"result":{"message":"","messages":{}},"status":"не передано тело объявления"}
```

Стенд: 
https://qa-internship.avito.com

TAS-107: Ошибочное сохранение объявления с отрицательным price
--
Серьезность - высокая

Приоритет - высокий

Исполнитель: ответственный разработчик

Шаги:
- POST /api/1/item с телом
```
{
  "sellerID": 12345,
  "name": "Synergistic Iron Watch",
  "price": <любое число меньше 0>,
  "statistics": {
    "likes": 3,
    "viewCount": 3,
    "contacts": 3
  }
}
```

Ожидаемое поведение:
- статус 400
- ответ вида:
```
{
  "result": {
    "messages": {},
    "message": "<сообщение о некорректности price>"
  },
  "status": 400
}
```

Фактическое поведение:
- статус 200
```
{"status":"Сохранили объявление - <UUID-объявления>"}
```

Стенд: 
https://qa-internship.avito.com

# Баги по удалению 

TAS-108: Не удаляется статистика при удалении объявления
--
Серьезность - средняя

Приоритет - средний

Исполнитель: ответственный разработчик

Шаги:
- POST /api/1/item с телом
```
{
  "sellerID": 1234,
  "name": "Synergistic Iron Watch",
  "price": 9,
  "statistics": {
    "likes": 3,
    "viewCount": 3,
    "contacts": 3
  }
}
```
- получить UUID из ответа
- отправить DELETE /api/2/item/<UUID-из-ответа>
- отправить DET /api/2/statistic/<UUID-из-ответа>

Ожидаемое поведение:
- статус 404
- ответ вида:
```
{
  "result": {
    "messages": {},
    "message": "<сообщение ненайденной статистике по этому объявлению>"
  },
  "status": 404
}
```

Фактическое поведение:
- статус 200
```
[
    {
        "contacts": 3,
        "likes": 3,
        "viewCount": 3
    }
]
```

Стенд: 
https://qa-internship.avito.com

# Баги по статистике 

TAS-109: Неверный код ответа при поиске статистики по невалидному UUID
--
Серьезность - низкая

Приоритет - низкий

Исполнитель: ответственный разработчик

Шаги:
- GET /api/2/statistic/Tempore

Ожидаемое поведение:
- статус 400
- ответ вида:
```
{
  "result": {
    "messages": {},
    "message": "<сообщение неверном формате id>"
  },
  "status": 400
}
```

Фактическое поведение:
- статус 404

Стенд: 
https://qa-internship.avito.com

# Баги по корнер-кейсам

TAS-110: Неверный формат ответа при попытках создания объявления с большими числовыми значениями
--
Серьезность - низкая

Приоритет - средний

Исполнитель: ответственный разработчик

Шаги:
- отправить POST /api/1/item с телом, где любое числовое поле больше 9 223 372 036 854
```
{
  "sellerID": <Значение больше 9 223 372 036 854>,
  "name": "Synergistic Iron Watch",
  "price": <Значение больше 9 223 372 036 854>,
  "statistics": {
    "likes": <Значение больше 9 223 372 036 854>,
    "viewCount": <Значение больше 9 223 372 036 854>,
    "contacts": <Значение больше 9 223 372 036 854>
  }
}
```

Ожидаемое поведение:
- статус 400
- ответ вида:
```
{
  "result": {
    "messages": {},
    "message": "<сообщение о неверном формате поля>"
  },
  "status": 400
}
```

Фактическое поведение:
```
{"result":{"message":"","messages":{}},"status":"не передано тело объявления"}
```

Стенд: 
https://qa-internship.avito.com

TAS-110: Неверный формат ответа при попытках создания объявления с длинными строками или невалидными символами
--
Серьезность - низкая

Приоритет - средний

Исполнитель: ответственный разработчик

Шаги:
- отправить POST /api/1/item с телом, где любое строковое поле длиннее 300 символов или содержит символы #$@&*?/`{[
```
{
  "sellerID": <Значение из диапазона 111111-999999>,
  "name": "Строка длиннее 300 символов или содержит символы #$@&*?/`{[",
  "price": <Значение из диапазона 1-9223372036854775807>,
  "statistics":
    {
      "likes": <Значение из диапазона 0-999999>,
      "viewCount": <Значение из диапазона 0-999999>,
      "contacts": <Значение из диапазона 0-999999>
    }
}
```

Ожидаемое поведение:
- статус 400
- ответ вида:
```
{
  "result": {
    "messages": {},
    "message": "<сообщение о неверном формате поля>"
  },
  "status": 400
}
```

Фактическое поведение:
- статус 200
```
{"status":"Сохранили объявление - <UUID-объявления>"}
```

Стенд: 
https://qa-internship.avito.com
