// Assert bundle archive exists
def file = new File(basedir, 'app/target/my-project-1.0.0-local-bundle.zip');
assert file.exists(): 'Bundle Tomcat archive is missing'
assert file.isFile(): 'Bundle Tomcat archive is not a normal file'

// Assert webapp classpath contains declared dependencies
def classpathFolder = new File(basedir, 'app/target/classpath/')
assert classpathFolder.isDirectory() : 'classpath folder is missing'
assert new File(classpathFolder, 'postgresql-42.6.0.jar').exists() : 'Postgresql driver dependency should be present in the webapp classpath'
assert new File(classpathFolder, 'checker-qual-3.31.0.jar').exists() : 'Postgresql driver transitive dependency should be present in the webapp classpath'
assert !new File(classpathFolder, 'bonita-connector-email-1.3.0.jar').exists() : 'bonita-connector-email should not be included in the webapp classpath'