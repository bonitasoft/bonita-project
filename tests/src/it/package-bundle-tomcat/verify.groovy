// Assert bundle archive exists
def file = new File(basedir, 'app/target/my-project-1.0.0-local-bundle.zip');
assert file.exists(): 'Bundle Tomcat archive is missing'
assert file.isFile(): 'Bundle Tomcat archive is not a normal file'

// Assert bundle configuration applied
def platformConfFile
new File(basedir, 'app/target/bundle').traverse(type: groovy.io.FileType.FILES, nameFilter: ~/bonita-platform-community-custom.properties/){
    platformConfFile = it
}
 "$platformConfFile Platform configuration property file is missing"
assert platformConfFile.text.contains('bonita.runtime.custom-application.install-provided-pages=true') : 'Invalid configuration'