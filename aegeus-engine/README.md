Aegeus events parser engine
===========================

Aegeus event parser runs on Apache Spark. 

### Install

Aegeus event parser is built using Apache Maven.
To build event parser:

```
$ git clone git@github.com:trK54Ylmz/aegeus.git
$ cd aegeus/aegeus-engine
$ mvn package -DskipTests
```

### Usage

Testing first requires AWS credentials.

```
$ java -DappName=engine-app \
       -Dmaster=local \
       -Ds3Access=[Aws Access Key] \
       -Ds3Secret=[Aws Secret Key] \
       -Ds3Region=[Aws Region] \
       -Dschema=/home/user/schema \
       -Ddata=/home/user/input \
       -Doutput=/home/user/output \
       -Dtest=true \
       -jar aegeus-engine-0.1-jar-with-dependencies.jar
```


### License

Copyright 2015 Tarık Yılmaz

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.