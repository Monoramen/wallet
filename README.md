# Wallet test task

### Запуск контейнеров

Для запуска приложения в контейнерах написать команду в терминале, находясь в корневой дирректории.

```terminal
docker compose build --no-cache
docker compose up
```
### Нагрузочные тесты

Для проверки нагрузки изменения счета
```terminal
docker run --rm -v "$(pwd)/target-wallet-test.lua:/target-wallet-test.lua" --network host williamyeh/wrk -t20 -c100 -d120s --latency -s /target-wallet-test.lua http://localhost:8080
```




