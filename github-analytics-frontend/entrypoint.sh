#!/bin/sh
# Replace the backend URL placeholder at container startup
sed -i "s|BACKEND_URL_PLACEHOLDER|${BACKEND_URL}|g" /etc/nginx/conf.d/default.conf
nginx -g 'daemon off;'