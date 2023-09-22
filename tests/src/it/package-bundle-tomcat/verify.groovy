// Assert bundle archive exists
def file = new File(basedir, 'app/target/my-project-1.0.0-local-bundle.zip');
assert file.exists(): 'Bundle Tomcat archive is missing'
assert file.isFile(): 'Bundle Tomcat archive is not a normal file'
