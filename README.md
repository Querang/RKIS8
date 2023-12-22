Борисюк Кирилл Алексеевич КИ21-16/2б
РКИС Лабораторная работа №8
Вариант-3


Инструкция запуска из консоли.

Скачайте проект или выполните команду:
```
git clone https://github.com/Querang/RKIS8.git
```

В консоли перейти в папку с проектом, после:
1) Запустить ActiveMQ:
```
activemq start
```
2) Создание базы данных:
```
psql -U postgres -h localhost -f car_db.sql  
```
3) Собрать проект при помощи команды:
```
.\mvnw.cmd package
```
4) Запустить программу:
```
java -jar target/RKIS8-0.0.1-SNAPSHOT.jar 
```
5) Открывать [страницу localhost](http://127.0.0.1:8080)

Для сборки необходим Maven - https://maven.apache.org/download.cgi


### Вариант 3: Автомобиль

Модифицировалась работа №7

Отправка сообщения и покупка реализованы лишь для web-контроллера. 
Сообщения отправляются контроллером и принимаются потоком, выводя их в консоль.
Изменено значение horsepower на quantity, при нажатии кнопки "купить" изменяется число, отвечающее за оставшийся товар.


### Примеры использования ПО:
![restExample.png](img/example2.png)\
_Начальный экран_


![restExample.png](img/example3.png)\
_После нажатия кнопки "ввести готовые данные"_


![restExample.png](img/example4.png)\
_После нажатия кнопки удалить все автомобили_


![restExample.png](img/example5.png)\
_Интерфейс меню добавления_


![restExample.png](img/example6.png)\
_После добавления_


![restExample.png](img/example7.png)\
_Фильтрация по цена в 432р_


![restExample.png](img/example8.png)\
_Меню редактирования отдельного автомобиля_


![restExample.png](img/example9.png)\
_Результат нажатия кнопки buy_


![restExample.png](img/example11.png)\
_Результат удаления_


![restExample.png](img/example10.png)\
_Пример вывода сообщений в консоль_