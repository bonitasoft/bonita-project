// Assert application archive exists
def file = new File(basedir, 'app/target/my-project-1.0.0-local.zip');
assert file.exists(): 'Application archive is missing'
assert file.isFile(): 'Application archive is not a normal file'
