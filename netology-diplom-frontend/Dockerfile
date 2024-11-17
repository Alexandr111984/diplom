FROM node:19.9.0
WORKDIR /app
COPY package*.json ./
EXPOSE 8081
RUN npm install
COPY . .
CMD ["npm", "run", "serve"]