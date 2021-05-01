# clojure-api

## build
```shell
docker build . -t heisenbugarchiteture/clojure-api
``` 

## Run
```shell
docker run -p 3000:3000 --name clojure-api heisenbugarchiteture/clojure-api
```

## Usage
```shell
curl -X POST -H "Content-Type: application/json"  "http://127.0.0.1:3000/people" -d '{"first_name": "Name", "surname": "Surname"}' | jq
```
