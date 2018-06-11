# s3-upload-tool
groovy script for upload files from local machine to s3 bucket by XLS

```sh
gradle clean build uberjar
java -jar path/../s3UploadTool-0.0.1.jar
```

input XLS format
```xls
fromLocalPath toS3Path
```

s3.yml (must be in folder with jar) example
```yml
input:
  xls: /tmp/input.xls
  pathPrefixForFrom: ''
  pathPrefixForTo: ''
  notValidSymbols: '' # if be discovered forbiden symbols from list you will see this info in log file, non blocking
upload:
  rewrite: false
s3:
  accessKey:
  secretKey:
  bucketName:
log:
  file: s3.log
```
