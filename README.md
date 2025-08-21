# Product App (React + Spring Boot + Docker)

This project is a full-stack application with:

- **Frontend**: React + Vite (served with Nginx)  
- **Backend**: Spring Boot + H2 (in-memory DB)  
- **Deployment**: Docker Compose  

---

## 🚀 How to Run (Docker)

From the project root, build and start the app:

```bash
docker compose build --no-cache
docker compose up -d
```

---

## 🌐 Open the App

- **Frontend (Nginx)** → [http://localhost:5173](http://localhost:5173)  
- **Backend API** → [http://localhost:8080](http://localhost:8080)  
- **Health Check** → [http://localhost:8080/actuator/health](http://localhost:8080/actuator/health)  

---

## 📜 Logs & Status

Check running containers:

```bash
docker compose ps
```

View logs:

```bash
docker compose logs -f backend
docker compose logs -f frontend
```

---

## 🛑 Stop the App

To stop and remove containers:

```bash
docker compose down
```

---

## ⚙️ Run in Development (Without Docker)

If you prefer to run locally without Docker:

### Backend
```bash
cd backend
./mvnw spring-boot:run
```
The backend will be available at [http://localhost:8080](http://localhost:8080)

### Frontend
```bash
cd frontend
npm install
npm run dev
```
The frontend will be available at [http://localhost:5173](http://localhost:5173)

---
