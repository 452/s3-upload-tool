import org.jets3t.service.impl.rest.httpclient.RestS3Service
import org.jets3t.service.model.S3Bucket
import org.jets3t.service.model.S3Object
import org.jets3t.service.security.AWSCredentials
// @Grab('net.java.dev.jets3t:jets3t:0.9.0')
import au.com.bytecode.opencsv.*
// @Grab('com.xlson.groovycsv:groovycsv:1.1')
import static com.xlson.groovycsv.CsvParser.parseCsv

def accessKey
def secretKey
def bucketName
def credentials
def s3Service
def bucket

main()

def main() {
	setup()
	def filesForUpload = makeFilesForUploadList()
	filesForUpload.eachWithIndex { file, i ->
		uploadFile(settings.input.pathPrefixForTo + file.'to (S3 path)', settings.input.pathPrefixForFrom + file.'from (local path)')
		log("${i+1} $file")
	}
	log('done')
}

def makeFilesForUploadList() {
	String csvContents = new File(pathToInputCSV).text
	parseCsv(csvContents, autoDetect:true)
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
	pathToInputCSV = settings.input.csv
	bucketName = settings.s3.bucketName
	credentials = new AWSCredentials(settings.s3.accessKey, settings.s3.secretKey)
	s3Service = new RestS3Service(credentials)
}
