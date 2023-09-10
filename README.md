# Aquariux
This is for Auqrius Technical Test at below

![image](https://github.com/cheejin96/aquariux/assets/20693378/a46a4db1-ec4b-4a14-8e88-52294ef12160)

# API
Below is the screenshots that show the steps to call the API

1. /stocks GET
This API is use to get the list of stock that the system supported.
![image](https://github.com/cheejin96/aquariux/assets/20693378/b4f81a56-a033-4ef4-bcb5-1674324248af)

2. /trade POST
| Param | Type| Description |
| --- | --- | --- |
| clientId | Integer | Target Client's ID |
| stockId | Integer | Target Stock's ID |
| quantity | BigDecimal | Target Stock's quantity |
| action | String | buy / sell |
This API is use to buy and/or sell the stock
![image](https://github.com/cheejin96/aquariux/assets/20693378/324f0720-cf9f-47c3-aded-20ae12f4c8d8)

3. /profile POST
This API is use to get Client's basic profile
| Param | Type| Description |
| --- | --- | --- |
| clientId | Integer | Target Client's ID |
![image](https://github.com/cheejin96/aquariux/assets/20693378/d66fc870-efb1-4f98-8a15-fd19fe53e662)

4. /client/stock POST
This API is used to get the Stocks that Client have purchased
| Param | Type| Description |
| --- | --- | --- |
| clientId | Integer | Target Client's ID |
![image](https://github.com/cheejin96/aquariux/assets/20693378/3620a680-57a8-4a9d-8df6-7b6170b0cbd5)

5. /transaction POST
This API is used to get the transaction records based on the parameter
| Param | Type| Description |
| --- | --- | --- |
| clientId | Integer | Target Client's ID |
| stockId | Integer | Target Stock's ID |
Scenario 1  
