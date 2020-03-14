FROM harbor.inf.avlyun.org/inf/java:alpine

EXPOSE 9002
RUN mkdir -p /root/app/scala-web
COPY ./ /root/app/scala-web

CMD [ "supervisord", "-c", "/root/app/scala-web/config/supervisor.conf" ]