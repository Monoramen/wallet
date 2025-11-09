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
docker run --rm --network wallet-network -v "$(pwd)/target-wallet-test.lua:/target-wallet-test.lua" williamyeh/wrk -t20 -c100 -d60s --latency -s /target-wallet-test.lua http://wallet-backend:8080
```

Для обращение к счету по UUID
```terminal
docker run --rm williamyeh/wrk -t20 -c100 -d60s http://wallet-backend:8080/api/v1/wallet/3c8e7b2a-1d4f-4a6c-9b32-7e5f8c3a9d01
```



