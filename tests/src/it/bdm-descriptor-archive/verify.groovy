// Assert BDM descriptor zip exists
def file = new File(basedir, 'bdm/model/target/my-project-bdm-model-1.0.0-descriptor.zip');
assert file.exists(): 'BDM descriptor zip is missing'
assert file.isFile(): 'BDM descriptor zip is not a normal file'
