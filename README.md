# web-store-app-backend

Backend for an e-commerce web application developed as part of my bachelor's degree thesis. The project implements the full server-side layer of a web store, exposing a REST API consumed by the [frontend application](https://web-store-app-frontend.vercel.app).

## Table of contents

- [About the project](#about-the-project)
- [Technology stack](#technology-stack)
- [Architecture overview](#architecture-overview)
- [API reference](#api-reference)
  - [Authentication](#authentication-authapi)
  - [Products](#products-productapi)
  - [Categories](#categories-categoryapi)
  - [Brands](#brands-brandapi)
  - [Payment and transactions](#payment-and-transactions-paymentapi)
  - [User profile](#user-profile-userapi)
  - [Charts](#charts-chartapi)
- [Security model](#security-model)
- [Requirements](#requirements)
- [Configuration](#configuration)
- [Running the project locally](#running-the-project-locally)
- [Running the tests](#running-the-tests)
- [Project structure](#project-structure)

---

## About the project

This is the server-side component of an e-commerce system built for a bachelor's degree thesis. It provides a stateless REST API that handles:

- user registration and JWT-based authentication (access + refresh tokens)
- product catalogue management backed by Elasticsearch
- hierarchical category and brand management stored in MariaDB
- shopping cart and order/transaction processing
- user profile retrieval
- admin-only analytics (top products by brand)

The application is designed to work together with an Angular frontend but the API is completely decoupled and can be used independently.

---

## Technology stack

| Layer | Technology |
|---|---|
| Language | Java 17 |
| Framework | Spring Boot 3.0.5 |
| Persistence (relational) | Spring Data JPA + MariaDB 10.6 |
| Persistence (search) | Spring Data Elasticsearch + Elasticsearch 8.5.3 |
| Security | Spring Security + JJWT 0.11.5 |
| Validation | Spring Boot Validation (Jakarta Bean Validation) |
| Boilerplate reduction | Lombok |
| Build tool | Maven (Maven Wrapper included) |
| Infrastructure | Docker Compose |
| Monitoring / search UI | Kibana 8.5.3 |
| Testing | JUnit 5, Spring Boot Test, Testcontainers |

---

## Architecture overview

```
Frontend (Angular)
       │
       │  HTTP / REST
       ▼
Spring Boot application  (port 8080)
  ├── JwtAuthenticationFilter  – validates Bearer token on every request
  ├── Controllers              – thin layer, delegates to services
  ├── Services                 – business logic
  ├── Repositories
  │    ├── Spring Data JPA  →  MariaDB   (users, categories, brands, tokens, transactions, cart)
  │    └── Spring Data ES   →  Elasticsearch  (products)
  └── Security config          – role-based endpoint protection
```

MariaDB stores relational, transactional data (users, tokens, categories, brands, orders).  
Elasticsearch stores the product catalogue and powers full-text / filtered product search.

---

## API reference

URL path segments follow a consistent convention:

- `/public/` – no authentication required
- `/private/` – requires a valid JWT Bearer token
- `/private/` endpoints under `/chart/api` additionally require the `ADMIN` role

### Authentication (`/auth/api`)

| Method | Path | Auth | Description |
|--------|------|------|-------------|
| `POST` | `/auth/api/register` | Public | Register a new user account |
| `POST` | `/auth/api/authenticate` | Public | Log in and receive access + refresh tokens |
| `POST` | `/auth/api/refresh` | Public (refresh token in header) | Issue a new access token using the refresh token |
| `POST` | `/auth/api/logout` | Authenticated | Invalidate the current token and log out |

**Register request body**

```json
{
  "firstname": "Jane",
  "lastname": "Doe",
  "email": "jane@example.com",
  "password": "secret123"
}
```

**Authenticate request body**

```json
{
  "email": "jane@example.com",
  "password": "secret123"
}
```

**Authenticate response body**

```json
{
  "access_token": "<JWT>",
  "refresh_token": "<JWT>"
}
```

---

### Products (`/product/api`)

Products are stored and searched in Elasticsearch. Each product has: `id`, `name`, `brand`, `category`, `subcategory`, `price`, `stock`, `imageURL`, `description`.

| Method | Path | Auth | Description |
|--------|------|------|-------------|
| `GET` | `/product/api/public/search` | Public | Search products with filters (see parameters below) |
| `GET` | `/product/api/public/find/id/{id}` | Public | Get a single product by ID |
| `GET` | `/product/api/public/find/category/{category}` | Public | Get products by top-level category |
| `GET` | `/product/api/private/find/all` | Authenticated | Get all products (paginated) |
| `GET` | `/product/api/private/find/subcategory/{subcategory}` | Authenticated | Get products by subcategory (paginated) |
| `GET` | `/product/api/private/find/brand/{brand}` | Authenticated | Get products by brand (paginated) |
| `POST` | `/product/api/private/add` | Authenticated | Add a new product |
| `PUT` | `/product/api/private/update` | Authenticated | Update an existing product |
| `DELETE` | `/product/api/private/delete/{id}` | Authenticated | Delete a product by ID |

**Search query parameters**

| Parameter | Required | Description |
|-----------|----------|-------------|
| `category` | Yes | Top-level category filter |
| `name` | No | Full-text name search |
| `subcategory` | No | Subcategory filter |
| `brands` | No | Comma-separated brand names |
| `pMin` | No | Minimum price |
| `pMax` | No | Maximum price |
| `page` | Yes | Page number (0-based) |
| `size` | Yes | Page size |

---

### Categories (`/category/api`)

Categories are hierarchical — a top-level category can have subcategories (child categories with a `parentId`).

| Method | Path | Auth | Description |
|--------|------|------|-------------|
| `GET` | `/category/api/public/find/top` | Public | Get all top-level categories (no parent) |
| `GET` | `/category/api/public/find/category/{id}` | Public | Get a category by ID |
| `GET` | `/category/api/public/find/subcategory/name/{categoryName}` | Public | Get subcategories by parent category name |
| `GET` | `/category/api/public/find/subcategory/id/{parentId}` | Public | Get subcategories by parent category ID |
| `GET` | `/category/api/private/find/all` | Authenticated | Get all categories |
| `POST` | `/category/api/private/add` | Authenticated | Create a new category |
| `DELETE` | `/category/api/private/delete/{id}` | Authenticated | Delete a category by ID |

---

### Brands (`/brand/api`)

| Method | Path | Auth | Description |
|--------|------|------|-------------|
| `GET` | `/brand/api/public/find/all` | Public | Get all brands |
| `GET` | `/brand/api/public/find/{id}` | Public | Get a brand by ID |
| `GET` | `/brand/api/public/find/subcategory/id/{categoryId}` | Public | Get brands by subcategory ID |
| `GET` | `/brand/api/public/find/category/name/{categoryName}` | Public | Get brands by parent category name |
| `DELETE` | `/brand/api/private/delete/{id}` | Authenticated | Delete a brand by ID |

---

### Payment and transactions (`/payment/api`)

| Method | Path | Auth | Description |
|--------|------|------|-------------|
| `POST` | `/payment/api/public/transaction/add` | Public | Place an order as a guest |
| `POST` | `/payment/api/private/transaction/add` | Authenticated | Place an order as a logged-in user |
| `GET` | `/payment/api/private/transaction/find/all` | Authenticated | Get all transactions for the authenticated user (paginated) |
| `GET` | `/payment/api/private/cart/find/{cartId}` | Authenticated | Get all cart items for a given cart ID |

---

### User profile (`/user/api`)

| Method | Path | Auth | Description |
|--------|------|------|-------------|
| `GET` | `/user/api/private/profile` | Authenticated | Get the profile of the currently authenticated user |

---

### Charts (`/chart/api`)

These endpoints are restricted to users with the `ADMIN` role.

| Method | Path | Auth | Description |
|--------|------|------|-------------|
| `GET` | `/chart/api/private/{brand}` | Admin | Get the top-selling products for a brand. Use `?col=N` to control the number of results (default: 5) |

---

## Security model

The application uses **stateless JWT authentication**. Two token types are issued on login:

| Token | Default expiry | Purpose |
|-------|----------------|---------|
| Access token | 1 hour (3 600 000 ms) | Authorises API requests via `Authorization: Bearer <token>` header |
| Refresh token | 7 days (604 800 000 ms) | Obtains a new access token at `/auth/api/refresh` |

Tokens are stored in the database (`token` table) and invalidated on logout.

**Roles and permissions**

| Role | Permissions |
|------|-------------|
| `USER` | Access to all `/private/` endpoints (except chart) |
| `ADMIN` | All `USER` permissions + `admin:read`, `admin:create`, `admin:update`, `admin:delete`; access to `/chart/api/private/**` |

Newly registered users receive the `USER` role by default.

---

## Requirements

- **Java 17** or newer
- **Docker** and **Docker Compose** (for the infrastructure)
- **Maven 3.8+** — or use the included `./mvnw` wrapper (no local Maven needed)

---

## Configuration

All settings are in `src/main/resources/application.properties`.

| Property | Default value | Description |
|----------|---------------|-------------|
| `spring.datasource.url` | `jdbc:mariadb://localhost:3306/test` | MariaDB JDBC URL |
| `spring.datasource.username` | `root` | MariaDB username |
| `spring.datasource.password` | `your_password` | MariaDB password |
| `spring.jpa.hibernate.ddl-auto` | `none` | Schema is **not** auto-created; prepare it manually |
| `spring.security.jwt.config.secret-key` | (hex string) | HMAC secret used to sign JWTs — **change this in production** |
| `spring.security.jwt.config.access-expiration` | `3600000` | Access token lifetime in milliseconds |
| `spring.security.jwt.config.refresh-expiration` | `604800000` | Refresh token lifetime in milliseconds |
| `spring.security.web.config.allowed-cors-origins` | `http://localhost:4200,https://web-store-app-frontend.vercel.app` | Comma-separated list of allowed CORS origins |

If you change the MariaDB password in `docker-compose.yml`, update `spring.datasource.password` accordingly. If you serve the frontend from a different origin, add it to `allowed-cors-origins`.

---

## Running the project locally

### 1. Start the infrastructure

The included `docker-compose.yml` starts:

- **es01** and **es02** — Elasticsearch 8.5.3 cluster (2 nodes)
- **kib01** — Kibana 8.5.3
- **mariadb** — MariaDB 10.6

```bash
docker compose up -d
```

Exposed ports:

| Service | Port |
|---------|------|
| MariaDB | `3306` |
| Elasticsearch | `9200` |
| Kibana | `5601` |

> **Note:** Elasticsearch security (`xpack.security.enabled`) is set to `false` in the Docker configuration for local development convenience.

### 2. Prepare the database

The application connects to a MariaDB database named `test`. It does **not** create or migrate the schema automatically (`ddl-auto=none`), so you need to create the schema manually before the first run.

Connect to the MariaDB container and create the database:

```bash
docker exec -it mariadb mariadb -uroot -pyour_password -e "CREATE DATABASE IF NOT EXISTS test;"
```

Then apply your schema SQL to the `test` database.

After the schema is in place, insert the required top-level categories:

```sql
START TRANSACTION;

INSERT INTO category (name, parent_id)
VALUES
    ('Electronics', NULL),
    ('Beauty', NULL),
    ('Clothing', NULL),
    ('Home & Garden', NULL),
    ('Toys', NULL),
    ('Sports & Outdoors', NULL);

SET @electronics_id = (SELECT id FROM category WHERE name = 'Electronics');
SET @beauty_id = (SELECT id FROM category WHERE name = 'Beauty');
SET @clothing_id = (SELECT id FROM category WHERE name = 'Clothing');
SET @home_garden_id = (SELECT id FROM category WHERE name = 'Home & Garden');
SET @toys_id = (SELECT id FROM category WHERE name = 'Toys');
SET @sports_outdoors_id = (SELECT id FROM category WHERE name = 'Sports & Outdoors');

INSERT INTO category (name, parent_id)
VALUES
    -- Electronics
    ('Laptops', @electronics_id),
    ('Tablets', @electronics_id),
    ('Headphones', @electronics_id),
    ('Printers', @electronics_id),
    ('Desktops', @electronics_id),
    ('Smartphones', @electronics_id),
    ('TVs', @electronics_id),
    ('Home Audio', @electronics_id),
    ('Smartwatches', @electronics_id),
    ('Monitors', @electronics_id),
    ('Game Consoles', @electronics_id),
    ('Cameras', @electronics_id),

    -- Beauty
    ('Makeup', @beauty_id),
    ('Skincare', @beauty_id),
    ('Fragrances', @beauty_id),
    ('Bath & Body', @beauty_id),
    ('Haircare', @beauty_id),
    ('Tools & Brushes', @beauty_id),

    -- Clothing
    ('Women''s Clothing', @clothing_id),
    ('Kid''s Clothing', @clothing_id),
    ('Men''s Clothing', @clothing_id),
    ('Shoes', @clothing_id),
    ('Accessories', @clothing_id),

    -- Home & Garden
    ('Appliances', @home_garden_id),
    ('Kitchen & Dining', @home_garden_id),
    ('Tools', @home_garden_id),
    ('Home Appliances', @home_garden_id),
    ('Bedding', @home_garden_id),
    ('Furniture', @home_garden_id),
    ('Home Decor', @home_garden_id),

    -- Toys
    ('Building Sets', @toys_id),
    ('Action Figures', @toys_id),
    ('Kid''s Toys', @toys_id),
    ('Outdoor Play', @toys_id),
    ('Board Games', @toys_id),
    ('Dolls & Dollhouses', @toys_id),

    -- Sports & Outdoors
    ('Athletic Shoes', @sports_outdoors_id),
    ('Athletic Apparel', @sports_outdoors_id),
    ('Outdoor Apparel', @sports_outdoors_id),
    ('Outdoor Footwear', @sports_outdoors_id),
    ('Camping & Hiking Gear', @sports_outdoors_id),
    ('Backpacks', @sports_outdoors_id);

COMMIT;
```

### 3. Seed Elasticsearch products

For local development, you can seed the Elasticsearch `product` index directly from Kibana.

1. Start the infrastructure with `docker compose up -d`.
2. Open Kibana at `http://localhost:5601`.
3. Go to **Dev Tools** -> **Console**.
4. Run the following command.

Seed sample products:

```http
POST product/_bulk?refresh=true
{ "index": { "_id": "0ac87dfa-fa4b-4f19-9bb0-e27b8b4f5d71" } }
{ "id": "0ac87dfa-fa4b-4f19-9bb0-e27b8b4f5d71", "brand": "Microsoft", "category": "Electronics", "subcategory": "Tablets", "name": "Microsoft Surface Duo", "price": 1724.4, "stock": 356, "image_url": "https://picsum.photos/200/300?random=836", "description": "Get ready for the ultimate Microsoft Surface Duo experience with Microsoft. With its intuitive interface, this Microsoft Surface Duo is the perfect choice for anyone who loves to stay connected and entertained on the go." }
{ "index": { "_id": "4b0f02c9-5f7a-4f55-a3a5-76c0629d9b01" } }
{ "id": "4b0f02c9-5f7a-4f55-a3a5-76c0629d9b01", "brand": "Apple", "category": "Electronics", "subcategory": "Phones", "name": "Apple iPhone 15", "price": 1199.99, "stock": 120, "image_url": "https://picsum.photos/200/300?random=101", "description": "Apple iPhone 15 delivers fast performance, a bright display, and a reliable camera system for everyday mobile use." }
{ "index": { "_id": "d8a2a6fd-7657-48e0-8e92-3d4bb49dc171" } }
{ "id": "d8a2a6fd-7657-48e0-8e92-3d4bb49dc171", "brand": "Samsung", "category": "Electronics", "subcategory": "TVs", "name": "Samsung Crystal UHD TV", "price": 849.5, "stock": 42, "image_url": "https://picsum.photos/200/300?random=102", "description": "Samsung Crystal UHD TV offers crisp picture quality, smart streaming features, and a slim design for modern living rooms." }
{ "index": { "_id": "6f47d122-69a3-4a8e-9b64-b41e921b8d60" } }
{ "id": "6f47d122-69a3-4a8e-9b64-b41e921b8d60", "brand": "Sony", "category": "Electronics", "subcategory": "Headphones", "name": "Sony WH-1000XM5", "price": 399.0, "stock": 88, "image_url": "https://picsum.photos/200/300?random=103", "description": "Sony WH-1000XM5 headphones provide premium noise cancellation, detailed sound, and comfortable all-day listening." }
{ "index": { "_id": "a9e2ef61-8dd0-4d63-af0e-2d4fc941d0d4" } }
{ "id": "a9e2ef61-8dd0-4d63-af0e-2d4fc941d0d4", "brand": "Nike", "category": "Clothing", "subcategory": "Shoes", "name": "Nike Air Max Runner", "price": 139.95, "stock": 230, "image_url": "https://picsum.photos/200/300?random=104", "description": "Nike Air Max Runner combines lightweight cushioning with breathable materials for training, commuting, and casual wear." }
{ "index": { "_id": "be3f8b88-90ef-4a51-a629-3831301337b5" } }
{ "id": "be3f8b88-90ef-4a51-a629-3831301337b5", "brand": "Adidas", "category": "Sports & Outdoors", "subcategory": "Fitness", "name": "Adidas Training Mat", "price": 44.99, "stock": 310, "image_url": "https://picsum.photos/200/300?random=105", "description": "Adidas Training Mat gives stable cushioning for stretching, core work, yoga, and home workout sessions." }
{ "index": { "_id": "2cecf9b7-f0f2-4d2c-9189-962dc3c67d95" } }
{ "id": "2cecf9b7-f0f2-4d2c-9189-962dc3c67d95", "brand": "Dyson", "category": "Home & Garden", "subcategory": "Appliances", "name": "Dyson V15 Vacuum", "price": 749.0, "stock": 64, "image_url": "https://picsum.photos/200/300?random=106", "description": "Dyson V15 Vacuum is a cordless cleaner with strong suction, advanced filtration, and tools for carpets and hard floors." }
{ "index": { "_id": "f04442a3-884c-4cd2-9a31-d6f3fdcbdd4e" } }
{ "id": "f04442a3-884c-4cd2-9a31-d6f3fdcbdd4e", "brand": "Lego", "category": "Toys", "subcategory": "Building Sets", "name": "Lego City Explorer Set", "price": 89.99, "stock": 180, "image_url": "https://picsum.photos/200/300?random=107", "description": "Lego City Explorer Set includes detailed pieces for creative building, role play, and open-ended construction fun." }
{ "index": { "_id": "b76dd7cb-dc04-48a3-9f4e-3033b70bfd12" } }
{ "id": "b76dd7cb-dc04-48a3-9f4e-3033b70bfd12", "brand": "Maybelline", "category": "Beauty", "subcategory": "Makeup", "name": "Maybelline Fit Me Foundation", "price": 12.99, "stock": 540, "image_url": "https://picsum.photos/200/300?random=108", "description": "Maybelline Fit Me Foundation provides natural-looking coverage with a lightweight feel for everyday makeup routines." }
{ "index": { "_id": "73969c8c-b82e-42c1-97ce-ff0e809dafd1" } }
{ "id": "73969c8c-b82e-42c1-97ce-ff0e809dafd1", "brand": "The North Face", "category": "Clothing", "subcategory": "Jackets", "name": "The North Face Rain Jacket", "price": 179.0, "stock": 95, "image_url": "https://picsum.photos/200/300?random=109", "description": "The North Face Rain Jacket is a lightweight waterproof layer designed for daily commutes and outdoor activities." }
```

The `_id` and `id` values are intentionally the same. The backend uses the Elasticsearch document ID when fetching a product by ID.

Verify the seeded data:

```http
GET product/_search
{
  "size": 10,
  "query": {
    "match_all": {}
  }
}
```

You can also verify through the backend after it starts:

```bash
curl "http://localhost:8080/product/api/public/search?category=Electronics&page=0&size=10"
```

### 4. Run the backend

**Using the Maven Wrapper (recommended — no local Maven required):**

```bash
./mvnw spring-boot:run
```

**Using a locally installed Maven:**

```bash
mvn spring-boot:run
```

The application starts on **`http://localhost:8080`** by default.

### 5. Build and run a JAR

```bash
./mvnw clean package -DskipTests
java -jar target/web-store-app-backend-0.0.1-SNAPSHOT.jar
```

---

## Running the tests

The test suite uses **Testcontainers** to spin up a real Elasticsearch instance automatically — Docker must be running.

```bash
./mvnw test
```

---

## Project structure

```
web-store-app-backend/
├── src/
│   ├── main/
│   │   ├── java/com/web/store/app/backend/
│   │   │   ├── authentication/   # Registration, login, token refresh, logout
│   │   │   ├── brand/            # Brand entity, repository, service, controller
│   │   │   ├── category/         # Category hierarchy management
│   │   │   ├── chart/            # Admin analytics (top products by brand)
│   │   │   ├── config/           # Application-level beans (PasswordEncoder, etc.)
│   │   │   ├── payment/
│   │   │   │   └── process/
│   │   │   │       ├── cart/         # Shopping cart items (Elasticsearch document)
│   │   │   │       └── transaction/  # Order/transaction management
│   │   │   ├── product/          # Product catalogue (Elasticsearch)
│   │   │   ├── security/         # JWT filter, security config, CORS, properties
│   │   │   └── user/             # User entity, roles, permissions, profile
│   │   └── resources/
│   │       └── application.properties
│   └── test/                     # Integration tests (Testcontainers)
├── docker-compose.yml            # Local infrastructure
├── pom.xml                       # Maven build descriptor
└── mvnw / mvnw.cmd               # Maven Wrapper scripts
```
