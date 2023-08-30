// Assert application.properties exists
File applicationFile = new File(basedir, 'target/application.properties');
assert applicationFile.exists(): 'application.properties is missing'

// Assert application.properties content
Properties applicationProps = new Properties()
applicationFile.withInputStream {
    applicationProps.load(it)
}
assert applicationProps.version == '1.0.0': 'application.properties content is incorrect'
