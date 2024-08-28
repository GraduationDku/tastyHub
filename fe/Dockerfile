FROM node:18

WORKDIR /app

COPY package.json .
RUN npm install

# .env 파일을 복사
COPY .env .env

COPY . .

RUN npm run build

CMD ["npm","run" ,"start:mac"]

EXPOSE 3000
