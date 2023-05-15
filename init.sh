sudo apt-get update
sudo apt-get install net-tools
sudo apt install iputils-ping
pip install httpie

curl -LO "https://dl.k8s.io/release/$(curl -L -s https://dl.k8s.io/release/stable.txt)/bin/linux/amd64/kubectl"
sudo install -o root -g root -m 0755 kubectl /usr/local/bin/kubectl


source <(kubectl completion bash) # bash-completion 패키지를 먼저 설치한 후, bash의 자동 완성을 현재 셸에 설정한다
echo "source <(kubectl completion bash)" >> ~/.bashrc # 자동 완성을 bash 셸에 영구적으로 추가한다
echo "alias k=kubectl" >> ~/.bashrc
echo "complete -o default -F __start_kubectl k" >> ~/.bashrc

curl "https://awscli.amazonaws.com/awscli-exe-linux-x86_64.zip" -o "../awscliv2.zip"
unzip -o ../awscliv2.zip -d ../
sudo .././aws/install

curl --silent --location "https://github.com/weaveworks/eksctl/releases/latest/download/eksctl_$(uname -s)_amd64.tar.gz" | tar xz -C /tmp
sudo mv /tmp/eksctl /usr/local/bin

cd kafka
docker container rm kafka-kafka-1 kafka-zookeeper-1
docker-compose up

