// package com.github

// @Grab('net.java.dev.jets3t:jets3t:0.9.0')
import org.jets3t.service.impl.rest.httpclient.RestS3Service
import org.jets3t.service.model.S3Bucket
import org.jets3t.service.model.S3Object
import org.jets3t.service.security.AWSCredentials
import com.github.ExcelBuilder

def accessKey
def secretKey
def bucketName
def credentials
def s3Service
def bucket

main()

def main() {
	setup()
	def count = 2
	checkValidation()
	new ExcelBuilder(pathToInputXLS).eachLine([labels:true]) {
		def to = settings.input.pathPrefixForTo + toS3Path
		def from = settings.input.pathPrefixForFrom + fromLocalPath
		log "Upload $count; From: $from; To: $to"
		try {
			count++
			uploadFile(to, from)
		} catch (Exception e) {
			log("Error: ${e}")
		}
	}
	log('done')
}

def checkValidation() {
	log "Symbols validation started"
	new ExcelBuilder(pathToInputXLS).eachLine([labels:true]) {
		def to = settings.input.pathPrefixForTo + toS3Path
		def from = settings.input.pathPrefixForFrom + fromLocalPath
		log "Check From: $from; To: $to"
		settings.input.notValidSymbols.eachWithIndex { symbol, i ->
			if (to.contains(symbol)) {
				throw new Exception("to (S3 path) Have forbiden symbol '$symbol'")
			}
			if (from.contains(symbol)) {
				throw new Exception("from (local path) Have forbiden symbol '$symbol'")
			}
		}
  }
	log "Symbols validation done"
}

def uploadFile(def path, file) {
	S3Object s3Object =	new S3Object(new File(file))
	s3Object.setKey(path)
	if (settings.upload.rewrite) {
		s3Service.putObject(bucketName, s3Object);
	} else {
		if (s3Service.isObjectInBucket(bucketName, path)) {
			log(', skiped')
		} else {
			s3Service.putObject(bucketName, s3Object);
		}
	}
}

def makeFolder(def path) {
	s3Service.putObject(bucketName, new S3Object(path));
}

def log(def message) {
	def logFile = new File("${settings.log.file}")
	logFile << '\n' << message
	println message
}

def setup() {
	settings = Config.fromYaml(new File('s3.yml').text)
	pathToInputXLS = settings.input.xls
	bucketName = settings.s3.bucketName
	credentials = new AWSCredentials(settings.s3.accessKey, settings.s3.secretKey)
	s3Service = new RestS3Service(credentials)
}
