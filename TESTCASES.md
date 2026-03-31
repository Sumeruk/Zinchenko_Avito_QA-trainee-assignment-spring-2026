Тест-кейсы для микросервиса 

baseURL: https://qa-internship.avito.com
api1: /api/1
api2: /api/2

Создание объявления:

1. 
ID: TAS-001
Название: Создание объявления

Предусловие: нет

Шаги:
- отправить POST {{baseUrl}}/{{api1}}/item c телом
{"sellerID": 111111, "name": "Т001", "price": 15000, "statistics": {"likes": 0, "viewCount": 0, "contacts": 0}}


