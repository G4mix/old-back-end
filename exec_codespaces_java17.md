Atualize tudo:
```
sudo apt update
```

Instale a versão 17 do openjdk:
```
sudo apt install openjdk-17-jdk
```

Exporte as variáveis com esses dois comandos:
```
echo 'export JAVA_HOME=/usr/lib/jvm/java-17-openjdk-amd64' >> ~/.bashrc
```
```
echo 'export PATH=$JAVA_HOME/bin:$PATH' >> ~/.bashrc
```

Execute esse comando para recarregar o bash:
```
source ~/.bashrc
```

Agora pode executar como se fosse java 17 normalmente.