#!/bin/bash
# haproxy/entrypoint.sh

haproxy -f /usr/local/etc/haproxy/haproxy.cfg -db -V