# Тест-кейсы для микросервиса 
    

### baseURL: <ins> https://qa-internship.avito.com </ins>

### api1: <ins> /api/1 </ins>

### api2: <ins> /api/2 </ins>
_________

Создание объявления:

ID: TAS-001

Название: Создание объявления

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



