// goal #1
def test1Props = getTestProperties('target/test1/test.properties')
assertProperty(test1Props, 'bonita.environment', 'MyEnvironment')
assertProperty(test1Props, 'bonita.environment.lowercase', 'myenvironment')
assertProperty(test1Props, 'project.artifactId', 'My-Project-Parent')
assertProperty(test1Props, 'project.artifactId.lowercase', 'my-project-parent')
assertProperty(test1Props, 'bonita.installProvidedPages', 'true')

// goal #2
def test2Props = getTestProperties('target/test2/test.properties')
assertProperty(test2Props, 'bonita.environment', 'my-Second-Environment')
assertProperty(test2Props, 'bonita.environment.lowercase', 'my-second-environment')
assertProperty(test2Props, 'project.artifactId', 'My-Project-Parent')
assertProperty(test2Props, 'project.artifactId.lowercase', 'my-project-parent')
assertProperty(test2Props, 'bonita.installProvidedPages', 'true')

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
