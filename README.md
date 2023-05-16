# 

## Model
www.msaez.io/#/storming/stmall_aaqqwweerr

## Before Running Services
### Make sure there is a Kafka server running
```
cd kafka
docker-compose up
```
- Check the Kafka messages:
```
cd kafka
docker-compose exec -it kafka /bin/bash
cd /bin
./kafka-console-consumer --bootstrap-server localhost:9092 --topic
```

## Run the backend micro-services
See the README.md files inside the each microservices directory:

- order
- delivery
- product
- customercenter


## Run API Gateway (Spring Gateway)
```
cd gateway
mvn spring-boot:run
```

## Test by API
- order
```
 http :8088/orders id="id" userId="userId" productId="productId" productName="productName" qty="qty" 
```
- delivery
```
 http :8088/deliveries id="id" userId="userId" orderId="orderId" productName="productName" qty="qty" productId="productId" status="status" courier="courier" 
```
- product
```
 http :8088/inventories id="id" productName="productName" stock="stock" 
```
- customercenter
```
```


## Run the frontend
```
cd frontend
npm i
npm run serve
```

## Test by UI
Open a browser to localhost:8088

## Required Utilities

- httpie (alternative for curl / POSTMAN) and network utils
```
sudo apt-get update
sudo apt-get install net-tools
sudo apt install iputils-ping
pip install httpie
```

- kubernetes utilities (kubectl)
```
curl -LO "https://dl.k8s.io/release/$(curl -L -s https://dl.k8s.io/release/stable.txt)/bin/linux/amd64/kubectl"
sudo install -o root -g root -m 0755 kubectl /usr/local/bin/kubectl
```

- aws cli (aws)
```
curl "https://awscli.amazonaws.com/awscli-exe-linux-x86_64.zip" -o "awscliv2.zip"
unzip awscliv2.zip
sudo ./aws/install
```

- eksctl 
```
curl --silent --location "https://github.com/weaveworks/eksctl/releases/latest/download/eksctl_$(uname -s)_amd64.tar.gz" | tar xz -C /tmp
sudo mv /tmp/eksctl /usr/local/bin
```




--

kafka 메시지 확인
cd kafka
docker-compose exec -it kafka /bin/bash
/bin/kafka-console-consumer --bootstrap-server localhost:9092 --topic stmall

시나리오

먼저 inventory에 제품을 추가한다.
http POST http://localhost:8083/inventories productName=notebook stock=100

제품을 주문
http POST http://localhost:8081/orders userId=wbkim productId=1 productName=notebook qty=3

제품 배송을 완료
http PUT http://localhost:8082/deliveries/1/completedelivery courier=gdhong

제품 재고 조회시 재고가 줄어든게 보여야함
http GET http://localhost:8083/inventories/1

제품을 환불
http DELETE http://localhost:8081/orders/1

제품환불 완료
http PUT http://localhost:8082/deliveries/1/returndelivery courier=gdhong

제품 재고 조회시 재고가 늘어난게 보여야함
http GET http://localhost:8083/inventories/1


kubenates kafka 설치

curl -fsSL -o get_helm.sh https://raw.githubusercontent.com/helm/helm/main/scripts/get-helm-3
chmod 700 get_helm.sh
./get_helm.sh

helm repo add bitnami https://charts.bitnami.com/bitnami
helm repo update
helm install my-kafka bitnami/kafka

kafka 메시지 확인
kubectl run my-kafka-client --restart='Never' --image docker.io/bitnami/kafka:2.8.0-debian-10-r0 --command -- sleep infinity
kubectl exec --tty -i my-kafka-client -- bash

# CONSUMER:
kafka-console-consumer.sh --bootstrap-server my-kafka:9092 --topic stmall --from-beginning

# siege

kubectl apply -f - <<EOF
apiVersion: v1
kind: Pod
metadata:
  name: siege
spec:
  containers:
  - name: siege
    image: apexacme/siege-nginx
EOF

kubectl exec -it siege -- /bin/bash
siege -c1 -t2S -v http://order:8080/orders


<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-actuator</artifactId>
</dependency>
설정으로 health check가 가능하게 되어있다.

readinessProbe에 정의한 대로 상태체크를 하여 새로운 버전으로 이동
livenessProbe에 정의한 주기에 따라 체크하여 문제가 있을시 재기동

liveness 테스트
kubectl apply -f - <<EOF
apiVersion: v1
kind: Pod
metadata:
  labels:
    test: liveness
  name: liveness-exec
spec:
  containers:
  - name: liveness
    image: k8s.gcr.io/busybox
    args:
    - /bin/sh
    - -c
    - touch /tmp/healthy; sleep 30; rm -rf /tmp/healthy; sleep 600
    livenessProbe:
      exec:
        command:
        - cat
        - /tmp/healthy
      initialDelaySeconds: 5
      periodSeconds: 5
EOF

상태확인
kubectl describe po liveness-exec

k scale deploy order --replicas=1