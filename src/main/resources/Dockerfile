FROM harbor.inf.avlyun.org/inf/java:alpine

EXPOSE 9002
RUN mkdir -p /root/app/fluent
COPY ./ /root/app/fluent


CMD [ "supervisord", "-c", "/root/app/fluent/config/supervisor.conf" ]