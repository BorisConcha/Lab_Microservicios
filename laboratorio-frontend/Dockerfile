FROM node:18-alpine AS build
WORKDIR /app

# Copiar archivos de configuración
COPY package*.json ./

# Instalar dependencias
RUN npm ci --legacy-peer-deps

# Copiar todo el código fuente
COPY . .

# Compilar la aplicación para producción
RUN npm run build -- --configuration production

# Etapa 2: Runtime con Nginx
FROM nginx:1.25-alpine

# Copiar archivos compilados al directorio de Nginx
COPY --from=build /app/dist/lab-results-frontend /usr/share/nginx/html

# Copiar configuración personalizada de Nginx
COPY nginx.conf /etc/nginx/conf.d/default.conf

# Exponer puerto 80
EXPOSE 80

# Agregar healthcheck
HEALTHCHECK --interval=30s --timeout=3s --start-period=10s --retries=3 \
  CMD wget --quiet --tries=1 --spider http://localhost/ || exit 1

# Comando de inicio
CMD ["nginx", "-g", "daemon off;"]