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

TAS-105: Ошибка в ответе при создании объявления с числом в name
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

TAS-106: Ошибочное сохранение объявления с отрицательным price
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
