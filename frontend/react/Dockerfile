FROM node:22-alpine
ARG api_abse_url
WORKDIR /app
COPY package*.json .
RUN npm i --silent
COPY . .
RUN echo "VITE_API_BASE_URL=${api_abse_url}" > .env
EXPOSE 5173
CMD ["npm", "run", "dev"]
