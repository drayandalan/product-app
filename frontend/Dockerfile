# ---------- Build stage ----------
FROM node:20-alpine AS build
WORKDIR /app

# Install deps
COPY package.json package-lock.json* pnpm-lock.yaml* yarn.lock* ./
RUN npm ci || npm i

# Copy sources and build
COPY . .
RUN npm run build

# ---------- Runtime stage ----------
FROM nginx:1.27-alpine
COPY --from=build /app/dist /usr/share/nginx/html

# Minimal SPA config (serve index.html on unknown routes)
RUN printf 'server {\n\
  listen 3000;\n\
  server_name _;\n\
  root /usr/share/nginx/html;\n\
  index index.html;\n\
  location / {\n\
    try_files $uri /index.html;\n\
  }\n\
  location /api/ {\n\
    proxy_pass http://backend:8080;\n\
    proxy_set_header Host $host;\n\
    proxy_set_header X-Real-IP $remote_addr;\n\
    proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;\n\
    proxy_http_version 1.1;\n\
  }\n\
}\n' > /etc/nginx/conf.d/default.conf

ENV PORT=3000
EXPOSE 3000

CMD ["nginx","-g","daemon off;"]
