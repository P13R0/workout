# BACKEND

Instructions to deploy backend to Heroku:
* create file Procfile in the root with this instruction `web: java -jar build/libs/kotlin-api.jar --server.port=$PORT`
* login heroku with command `heroku login`
* wire project on heroku with command `heroku git:remote -a $PROJECT_NAME`
* set buildpack with command `heroku buildpacks:set heroku/jvm`
* deploy app with command `git push heroku main`
