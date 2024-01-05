FROM oven/bun:1 as build

ARG VITE_API_URL

ENV VITE_API_URL=$VITE_API_URL

WORKDIR /webapp

COPY bun.lockb index.html package.json vite.config.ts tsconfig.json tsconfig.node.json ./
COPY public ./public
COPY src ./src

RUN bun install

RUN bunx --bun vite build


FROM nginx:latest

EXPOSE 3000

COPY ./nginx/default.conf /etc/nginx/conf.d/default.conf
COPY --from=build /webapp/dist /usr/share/nginx/html
