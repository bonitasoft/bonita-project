// replace variables in the integration pom.xml
def pom = new File(basedir, 'pom.xml')
pom.text = pom.text.replaceAll('@bonita-project-version@', bonitaProjectVersion)
// continue invoker execution
return true
