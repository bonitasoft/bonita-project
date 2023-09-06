// Assert docker properties
def testProps = getTestProperties('app/target/test-resources/test.properties')
assertProperty(testProps, 'docker.baseImageRepository', 'bonita')
assertProperty(testProps, 'docker.baseImageVersion', bonitaProjectVersion)
assertProperty(testProps, 'docker.baseImage', "bonita:$bonitaProjectVersion")
assertProperty(testProps, 'docker.imageRepository', 'my-project-local')
assertProperty(testProps, 'docker.imageName', 'my-project-local:1.0.0')
assertProperty(testProps, 'docker.noCache', 'true')
assertProperty(testProps, 'docker.buildArgLine', "build \".\" --build-arg BONITA_BASE_IMAGE=\"bonita:$bonitaProjectVersion\"  --no-cache=\"true\" --tag \"my-project-local:1.0.0\"")

Properties getTestProperties(String path) {
    File testPropsFile = new File(basedir, path);
    assert testPropsFile.exists(): "$path is missing"

    Properties testProps = new Properties()
    testPropsFile.withInputStream {
        testProps.load(it)
    }
    return testProps
}

static void assertProperty(Properties properties, String key, String expectedValue) {
    assert properties."$key" == expectedValue: "expected '$expectedValue' for property '$key' but was '${properties."$key"}'"
}
