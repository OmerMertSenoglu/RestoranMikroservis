# Restoran Mikroservis Sistemi

Restoran, rezervasyon ve API Gateway servislerinden oluşan Spring Boot mikroservis uygulamasi.

## Mimari

| Servis | Port | Gorev |
| --- | ---: | --- |
| Gateway | 8085 | Istekleri mikroservislere yonlendirir |
| Restoran Yonetimi | 8080 | Restoran CRUD islemleri |
| Rezervasyon Sistemi | 8082 | Rezervasyon CRUD islemleri ve restoran dogrulamasi |

Gateway uzerinden istekler su sekilde yonlendirilir:

- `/Restaurant/**` -> `http://localhost:8080`
- `/Reservation/**` -> `http://localhost:8082`

## Teknoloji

- Java 25 LTS
- Spring Boot 3.5.6
- Spring Cloud 2025.0.1
- Spring Cloud Gateway WebFlux
- Spring Data JPA
- PostgreSQL
- H2 (testler icin)
- Maven Wrapper

## Gereksinimler

- JDK 25
- PostgreSQL
- Windows icin PowerShell veya Maven Wrapper'i calistirabilen bir terminal

PostgreSQL'de `restaurantdb` adinda bir veritabani olusturun. Uygulamanin mevcut gelistirme ayarlari su bilgileri kullanir:

```text
Host: localhost
Port: 5432
Database: restaurantdb
Username: postgres
Password: 123456
```

Canli ortamda bu bilgileri degistirmeniz ve sifreleri kaynak kodunda tutmamaniz gerekir.

## Calistirma

Her servisi ayri bir terminalde baslatin. Siralama onemlidir: once restoran ve rezervasyon servisleri, sonra gateway.

### Restoran Yonetimi

```powershell
cd RestoranYonetimi
$env:JAVA_HOME="C:\Users\user\.jdk\jdk-25.0.2"
.\mvnw.cmd spring-boot:run
```

Servis: `http://localhost:8080`

### Rezervasyon Sistemi

```powershell
cd RezervasyonSistemi
$env:JAVA_HOME="C:\Users\user\.jdk\jdk-25.0.2"
.\mvnw.cmd spring-boot:run
```

Servis: `http://localhost:8082`

### Gateway

```powershell
cd gateway
$env:JAVA_HOME="C:\Users\user\.jdk\jdk-25.0.2"
.\mvnw.cmd spring-boot:run
```

Gateway: `http://localhost:8085`

## API Kullanimi

Tum ornekler gateway uzerinden gonderilebilir.

### Restoranlar

```http
POST http://localhost:8085/Restaurant/add
Content-Type: application/json

{
  "name": "Merkez Restoran",
  "address": "Istanbul",
  "description": "Aile restorani",
  "openingHours": "09:00-23:00",
  "contactNumber": "+90 555 000 0000",
  "email": "info@example.com"
}
```

```http
GET    http://localhost:8085/Restaurant/findAll
GET    http://localhost:8085/Restaurant/get/{id}
PUT    http://localhost:8085/Restaurant/update/{id}
DELETE http://localhost:8085/Restaurant/delete/{id}
```

Guncelleme istegi, ekleme ile ayni restoran alanlarini kullanir.

### Rezervasyonlar

```http
POST http://localhost:8085/Reservation/create
Content-Type: application/json

{
  "username": "ali",
  "reservationDate": "2026-09-18T19:30:00",
  "numberOfPeople": 4,
  "restaurantId": 1,
  "notes": "Pencere kenari"
}
```

```http
GET    http://localhost:8085/Reservation/findAll
GET    http://localhost:8085/Reservation/get/{id}
PUT    http://localhost:8085/Reservation/update/{id}
DELETE http://localhost:8085/Reservation/delete/{id}
```

Rezervasyon olusturulurken `restaurantId` degerinin restoran servisinde mevcut olmasi gerekir.

## Testler

Her modul icin temiz derleme ve test:

```powershell
$env:JAVA_HOME="C:\Users\user\.jdk\jdk-25.0.2"

cd gateway
.\mvnw.cmd clean test
cd ..\RestoranYonetimi
.\mvnw.cmd clean test
cd ..\RezervasyonSistemi
.\mvnw.cmd clean test
```

Testler PostgreSQL yerine test kaynaklarinda tanimli H2 veritabanini kullanir.

## Proje Yapisi

```text
gateway/
  src/main/java/
  src/main/resources/application.properties
RestoranYonetimi/
  src/main/java/.../controller/
  src/main/java/.../service/
  src/main/java/.../repository/
  src/main/resources/application.properties
RezervasyonSistemi/
  src/main/java/.../controller/
  src/main/java/.../service/
  src/main/java/.../repository/
  src/main/java/.../feignclient/
  src/main/resources/application.properties
```

Controller katmani HTTP isteklerini karsilar. Is kurallari ve veri erisimleri service katmaninda tutulur.
