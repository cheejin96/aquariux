# Aquariux
This is for Auqrius Technical Test at below

![image](https://github.com/cheejin96/aquariux/assets/20693378/a46a4db1-ec4b-4a14-8e88-52294ef12160)

# API
Below is the screenshots that show the steps to call the API

### Stock list
| API | Details |
| --- | --- |
| URL | /stocks |
| TYPE | GET |
| Description | This API is use to get the list of stock that the system supported. |

![image](https://github.com/cheejin96/aquariux/assets/20693378/b4f81a56-a033-4ef4-bcb5-1674324248af)

### Trading
| API | Details |
| --- | --- |
| URL | /trade |
| TYPE | POST |
| Description | This API is use to buy and/or sell the stock |

| Parameter | Type | Description |
| --- | --- | --- |
| clientId | Integer | Target Client's ID |
| stockId | Integer | Target Stock's ID |
| quantity | BigDecimal | Target Stock's quantity |
| action | String | buy / sell |


![image](https://github.com/cheejin96/aquariux/assets/20693378/324f0720-cf9f-47c3-aded-20ae12f4c8d8)

### Profile
| API | Details |
| --- | --- |
| URL | /profile |
| TYPE | POST |
| Description | This API is use to get Client's basic profile |

| Param | Type| Description |
| --- | --- | --- |
| clientId | Integer | Target Client's ID |

![image](https://github.com/cheejin96/aquariux/assets/20693378/d66fc870-efb1-4f98-8a15-fd19fe53e662)

### Client's Stock
| API | Details |
| --- | --- |
| URL | /client/stock |
| TYPE | POST |
| Description | This API is used to get the Stocks that Client have purchased |

| Param | Type| Description |
| --- | --- | --- |
| clientId | Integer | Target Client's ID |

![image](https://github.com/cheejin96/aquariux/assets/20693378/3620a680-57a8-4a9d-8df6-7b6170b0cbd5)

### Transaction
| API | Details |
| --- | --- |
| URL | /transaction |
| TYPE | POST |
| Description | This API is used to get the transaction records based on the parameter |

| Param | Type| Description |
| --- | --- | --- |
| clientId | Integer | Target Client's ID |
| stockId | Integer | Target Stock's ID |

Scenario 1 
clientId and stockId is not null - return client transactions list on that particular stock 
![image](https://github.com/cheejin96/aquariux/assets/20693378/7180cce7-6bab-4027-a003-89b37373482a)

Scenario 2 
clientId is null and stockId is not null - return stock transactions list
![image](https://github.com/cheejin96/aquariux/assets/20693378/fa1e5306-1979-4ecb-a1fc-7c208c7c3261)

Scenario 3
clientId is not null and stockId is null - return client transactions list
![image](https://github.com/cheejin96/aquariux/assets/20693378/75945239-6a62-48d8-8cfa-5c9beb89bce0)
