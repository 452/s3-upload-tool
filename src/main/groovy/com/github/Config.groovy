// @Grab('org.yaml:snakeyaml:1.21')
import org.yaml.snakeyaml.Yaml
import org.yaml.snakeyaml.constructor.Constructor

class Config {

    def input
    def upload
    def s3
    def log

    static Config fromYaml(yaml) {
        Constructor c = new Constructor(Config)
        new Yaml(c).load(yaml)
    }
}
