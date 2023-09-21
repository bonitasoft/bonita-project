import java.util.zip.ZipFile

// Assert application archive exists
def file = new File(basedir, 'app/target/my-project-1.0.0-local.zip')
assert file.exists(): 'Application archive is missing'
assert file.isFile(): 'Application archive is not a normal file'

// Assert archive content
def zipContent = []
new ZipFile(file).entries().findAll().each {
    zipContent << it.name
}
println "Application archive content: ${zipContent}"
def expectedZipContent = [
        'applications/',
        'applications/application.xml',
        'pages/',
        "pages/page-user-task-list-${bonitaProjectVersion}.zip".toString()
]
assert expectedZipContent.size() == zipContent.size(): 'Application archive does not content expected number of files'
assert expectedZipContent.containsAll(zipContent): 'Files are missing in application archive'
