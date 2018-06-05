# s3-upload-tool
groovy script for upload files from local machine to s3 bucket by CSV

```sh
gradle clean build uberjar
java -jar path/../s3UploadTool-0.0.1.jar
```

input CSV format
```csv
from (local path);to (S3 path)
```

s3.yml (must be in folder with jar) example
```yml
input:
  csv: /tmp/input.csv
  pathPrefixForFrom: ''
  pathPrefixForTo: ''
upload:
  rewrite: false
s3:
  accessKey:
  secretKey:
  bucketName:
log:
  file: s3.log
```
