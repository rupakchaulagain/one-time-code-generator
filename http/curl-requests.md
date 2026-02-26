# API requests (curl)

## Create One Time Code (CREATED)
```bash
curl -X POST http://localhost:8080/api/otc \
  -H "Content-Type: application/json" \
  -d '{"customerId":123,"bankId":45,"availableLoanLimit":120000,"expiryMinutes":59}'
```

## Get by id
```bash
curl http://localhost:8080/api/otc/1
```

## Activate (CREATED -> ACTIVE)
```bash
curl -X POST http://localhost:8080/api/otc/1/activate
```

## Use (ACTIVE -> USED)
```bash
curl -X POST http://localhost:8080/api/otc/1/use \
  -H "Content-Type: application/json" \
  -d '{"amount":5000}'
```

## Cancel (CREATED/ACTIVE -> CANCELLED)
```bash
curl -X POST http://localhost:8080/api/otc/1/cancel
```
