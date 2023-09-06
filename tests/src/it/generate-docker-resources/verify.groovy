// goal #1
assert new File(basedir, 'target/my-project-parent-local-1.0.0-output/Dockerfile').exists(): 'Dockerfile is missing'
assert new File(basedir, 'target/my-project-parent-local-1.0.0-output/.dockerignore').exists(): '.dockerignore is missing'

// goal #2
assert new File(basedir, 'target/test-application-output/Dockerfile').exists(): 'Dockerfile is missing'
assert new File(basedir, 'target/test-application-output/.dockerignore').exists(): '.dockerignore is missing'
